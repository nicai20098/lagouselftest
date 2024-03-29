package com.jiabb.mvcframework.servlet;

import com.jiabb.mvcframework.annotations.LocalAutowired;
import com.jiabb.mvcframework.annotations.LocalController;
import com.jiabb.mvcframework.annotations.LocalRequestMapping;
import com.jiabb.mvcframework.annotations.LocalService;
import com.jiabb.mvcframework.pojo.Handler;
import jdk.nashorn.internal.ir.CallNode;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/7 23:02
 * @since: 1.0
 */
public class LocalDispatcherServlet extends HttpServlet {

    //存放解析配置类
    private Properties properties = new Properties();
    // 缓存扫描到的类的全限定类名
    private List<String> classNames = new ArrayList<>();
    //ioc容器
    private Map<String, Object> ioc = new HashMap<>();
    //handleMapping
    private Map<String, Handler> handleMapping = new HashMap<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理请求 根据url 找到对应method方法 进行调用
        String requestURI = req.getRequestURI();
        Handler handler = handleMapping.get(requestURI);
        Method method = handler.getMethod();

        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            Map<String, String[]> parameterMap = req.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String value = StringUtils.join(entry.getValue(), ",");
                if (handler.getParamIndexMapping().containsKey(entry.getKey())) {
                    Integer index = handler.getParamIndexMapping().get(entry.getKey());
                    args[index] = value;
                }
            }
            Integer reqIndex = handler.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());
            args[reqIndex] = req;
            Integer resIndex = handler.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName());
            args[resIndex] = resp;
            method.invoke(handler.getController(), args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //加载配置文件springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);
        //扫描相关的类, 扫描注解
        doScan(properties.getProperty("scanPackage"));
        //初始化bean对象(实现ioc容器 基于注解)
        try {
            doInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //实现依赖注入
        odAutoWired();
        //构造一个handleMapping处理器映射器, 将配置好的url和method建立映射关系
        initHandleMapping();

        System.out.println("mvc 加载完成...");
        //等待请求进入 处理请求
    }

    // 构造一个handleMapping处理器映射器
    // 目的: 将url和 method建立关联
    private void initHandleMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(LocalController.class)) {
                continue;
            }
            String baseUrl = "";
            if (aClass.isAnnotationPresent(LocalRequestMapping.class)) {
                LocalRequestMapping annotation = aClass.getAnnotation(LocalRequestMapping.class);
                baseUrl = annotation.value();
            }
            //获取方法
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(LocalRequestMapping.class)) {
                    continue;
                }

                LocalRequestMapping annotation = method.getAnnotation(LocalRequestMapping.class);
                String url = baseUrl + annotation.value();

                Handler handler = new Handler(entry.getValue(), method, Pattern.compile(url));
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Class<?> type = parameter.getType();
                    if (Objects.equals(HttpServletRequest.class, type) || Objects.equals(HttpServletResponse.class, type))  {
                        handler.getParamIndexMapping().put(type.getSimpleName(), i);
                    } else {
                        handler.getParamIndexMapping().put(parameter.getName(), i);
                    }
                }

                handleMapping.put(url, handler);
            }

        }

    }

    //实现依赖注入
    private void odAutoWired() {
        if (ioc.isEmpty()) {
            return;
        }
        //有对象再进行依赖注入
        //遍历ioc中所有对象, 查看对象中的字段,是否有@LocalAutoWired注解,如果有需要维护依赖注入关系
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //获取bean对象中的字段信息
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            //遍历判断处理
            for (Field declaredField : declaredFields) {
                if (!declaredField.isAnnotationPresent(LocalAutowired.class)) {
                    continue;
                }
                //有该注解
                LocalAutowired annotation = declaredField.getAnnotation(LocalAutowired.class);
                String beanName = annotation.value();
                if (Objects.equals("", beanName.trim())) {
                    //没有配置bean id 那就需要根据当前字段类型注入(接口注入)
                    beanName = declaredField.getType().getName();
                }
                //开启赋值
                declaredField.setAccessible(true);

                try {
                    Object o = ioc.get(beanName);
                    declaredField.set(entry.getValue(), o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }


            }

        }


    }

    //实现ioc容器
    //基于classNames缓存的类的全限定类名,以及反射技术,完成对象的创建和管理
    private void doInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        if (classNames.size() == 0) {
            return;
        }
        for (String className : classNames) {
            //反射
            Class<?> aClass = Class.forName(className);
            //区分controller 区分service
            if (aClass.isAnnotationPresent(LocalController.class)) {
                // controller的id此处不做过多处理 不去value 就拿类的首字母小写作为id 保存到ioc中
                String simpleName = aClass.getSimpleName();
                simpleName = lowerFirst(simpleName);
                //实例化放入ioc中
                ioc.put(simpleName, aClass.newInstance());
            } else if (aClass.isAnnotationPresent(LocalService.class)) {
                LocalService annotation = aClass.getAnnotation(LocalService.class);
                //获取注解值
                String beanName = annotation.value();
                //如果没有指定id 则使用首字母小写
                if (Objects.equals("",beanName.trim())) {
                    beanName = lowerFirst(aClass.getSimpleName());
                }
                ioc.put(beanName, aClass.newInstance());
            }
            //service 层往往是有接口的,面向接口开发,此时再以接口名为id放入一份到ioc中,便于后期根据接口类型注入
            Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                ioc.put(anInterface.getName(), aClass.newInstance());
            }
        }

    }

    //首字母小写方法
    private String lowerFirst(String str) {
        char[] charArray = str.toCharArray();
        if ('A' <= charArray[0] && charArray[0] <= 'Z') {
            charArray[0] += 32;
        }
        return String.valueOf(charArray);
    }

    //扫描类
    private void doScan(String scanPackage) {
        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + scanPackage.replaceAll("\\.", "/");

        File pack = new File(scanPackagePath);
        File[] files = pack.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //递归处理
                doScan(scanPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }

    }

    //加载配置文件
    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}