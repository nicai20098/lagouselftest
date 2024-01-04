package com.jiabb.context.factory;

import com.jiabb.annotation.*;
import com.jiabb.context.IOCContainer;
import com.jiabb.context.em.ProxyTypeEnum;
import com.jiabb.context.transaction.TransactionManager;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;

import javax.servlet.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 处理注解
 * @author fangyuan
 */
public class AnnotationBeanFactory extends AbstractBeanFactory {

    private Reflections reflections;

    private final static String backpacks = "com.lagou.edu";
    //需要被剥离
    private ServletContextEvent servletContextEvent;

    private Map<String, List<BeanField>> txCache = new HashMap<>(32);

    private Map<String,Method> duplicateRemoval = new HashMap<>(32);

    static class BeanField{

        private Object obj;

        private Field field;

        BeanField(Object obj,Field field){
            this.obj = obj;
            this.field = field;
        }

    }

    public AnnotationBeanFactory(IOCContainer iocContainer, ServletContextEvent servletContextEvent) {
        super(iocContainer);
        this.servletContextEvent = servletContextEvent;
        //默认取当前上下文路径 这里直接写死
    }

    /**
     * 初始化实例信息
     */
    @Override
    public void buildInstance() {

        reflections =  new Reflections( backpacks);

        //扫描包含Component 与Service注解接口
        Set<Class<?>> componentClassesList =  reflections.getTypesAnnotatedWith(Component.class);

        Set<Class<?>> serviceClassesList = reflections.getTypesAnnotatedWith(Service.class);

        Set<Class<?>> controllerClassesList = reflections.getTypesAnnotatedWith(RestController.class);

        this.buildComponent(componentClassesList);

        this.buildService(serviceClassesList);

        this.buildController(controllerClassesList);

    }

    @Override
    public void injectionProperty() {

        //先获取哪些Transaction需要被处理
        initTx();
        reflections =  new Reflections( backpacks,new FieldAnnotationsScanner());

        //扫描所有Autowired 对应的File
        Set<Field> fields = reflections.getFieldsAnnotatedWith(Autowired.class);

        fields.forEach(field -> {

            Autowired autowired = field.getAnnotation(Autowired.class);

            String propertyBeanId  = autowired.value();

            //获取filed对应的class名
            Class<?> classes = field.getDeclaringClass();
            //获取beanId
            String beanName = classes.getSimpleName();

            beanName = beanName.substring(0,1).toLowerCase()+beanName.substring(1);
            //获取对应bean
            Object obj = this.iocContainer.getSingleObject().get(beanName);
            if(obj == null ){
                throw new RuntimeException(beanName+"未进行初始化");
            }
            //获取对应的name
            String filedName = field.getName();

            if(propertyBeanId.isEmpty()){
                propertyBeanId =  filedName;
            }

            //将对应的bean注入到对应Bean的属性中
            try {
                Field objField =  obj.getClass().getDeclaredField(filedName);
                //获取对应Bean
                Object property = this.iocContainer.getSingleObject().get(propertyBeanId);
                //私有属性强制访问
                objField.setAccessible(true);
                objField.set(obj,property);

                if(duplicateRemoval.containsKey(propertyBeanId)){
                    BeanField beanField = new BeanField(obj,objField);
                    List<BeanField> beanFields = txCache.get(propertyBeanId);
                    if(beanFields == null){
                        beanFields = new ArrayList<>();
                    }
                    beanFields.add(beanField);
                    txCache.put(propertyBeanId,beanFields);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Autowired对应的Bean不存在:"+propertyBeanId);
            }

        });

    }

    private void initTx(){
        //扫描被method
        reflections =  new Reflections(backpacks ,new MethodAnnotationsScanner());

        //扫描所有Autowired 对应的File
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Transactional.class);

        methods.forEach(method -> {

            //获取方法对应的接口
            Class<?> methodClasses = method.getDeclaringClass();

            String classesName = methodClasses.getSimpleName();

            classesName = this.lowerCaseBeanName(classesName);
            //只会生成一个代理类
            if(!duplicateRemoval.containsKey(classesName)){
                //记录哪些类要被替换
                duplicateRemoval.put(classesName,method);
            }
        });

    }

    @Override
    public void initTransactional() {

        duplicateRemoval.forEach((classesName,method) -> {

            Transactional transactional = method.getAnnotation(Transactional.class);

            //获取方法对应的接口
            Class<?> methodClasses = method.getDeclaringClass();

            classesName = this.lowerCaseBeanName(classesName);
            //只会生成一个代理类
                String transactionalManageName  = transactional.value();

                Object o = this.iocContainer.getSingleObject().get(transactionalManageName);

                if(o == null){
                    throw new RuntimeException("事务管理器不存在");
                }
                TransactionManager tx = (TransactionManager) o;

                //获取实际类
                Object instance = this.iocContainer.getSingleObject().get(classesName);
                if(instance== null){
                    throw new RuntimeException("@Transactional所在类未初始化");
                }
                //类对应的实际 并生成代理对象

                Object proxyBean;
                if(ProxyTypeEnum.JDK.equals(transactional.type())){
                    proxyBean = ProxyFactory.getJdkProxy(tx,instance );
                }else{
                    proxyBean = ProxyFactory.getCglibProxy(tx,instance );
                }
               //写入代理对象 并注入对应
                this.iocContainer.getSingleObject().put(classesName,proxyBean);
                //替换
        });

        txCache.forEach((beanName,v)->{
            Object proxyBean =  this.iocContainer.getSingleObject().get(beanName);
            v.forEach(beanField -> {
                //私有属性强制访问
                try {
                    beanField.field.setAccessible(true);
                    beanField.field.set(beanField.obj,proxyBean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            });
        });

        //重新替换
    }


    public static void main(String[] args) {
        System.out.println(AnnotationBeanFactory.class.getSimpleName());
    }

    /**
     * 处理Component注解
     * @param componentClassesList
     */
    private void  buildComponent(Set<Class<?>> componentClassesList){

        componentClassesList.forEach(classes->{
            //获取该注解详细信息
            Component component = classes.getAnnotation(Component.class);
            String beanId = component.value() ;
            createInstance(beanId,classes);
        });
    }

    /**
     * 处理Service注解
     * @param serviceClassesList
     */
    private void  buildService(Set<Class<?>> serviceClassesList){

        serviceClassesList.forEach(classes->{
            //获取该注解详细信息
            Service service = classes.getAnnotation(Service.class);
            String beanId = service.value() ;
            createInstance(beanId,classes);
        });

    }

    /**
     * 处理Component注解
     * @param controllerClassesList
     */
    private void  buildController(Set<Class<?>> controllerClassesList){

        controllerClassesList.forEach(classes->{
            //获取该注解详细信息
            RestController restController = classes.getAnnotation(RestController.class);
            String beanId = restController.value() ;
            String url = restController.url();

            Class<? extends Servlet>  servletClass = (Class<? extends Servlet>) classes;
            try {
                Servlet servlet = servletContextEvent.getServletContext().createServlet(servletClass);

                ServletRegistration.Dynamic dynamic = servletContextEvent.getServletContext().addServlet(beanId, servlet);
                dynamic.addMapping(url);
                //写入Bean
                this.iocContainer.getSingleObject().put(beanId,servlet);
            } catch (ServletException e) {
                e.printStackTrace();
                throw new RuntimeException("无法初始化该RestController");
            }

        });
    }

    private Object createInstance(String beanId,Class<?> classes){
        //如果为空
        if(beanId.isEmpty()){
            beanId = classes.getSimpleName();
            //取类名首字母小写
            beanId = this.lowerCaseBeanName(beanId);
            if(this.iocContainer.contains(beanId)){
                throw new RuntimeException("当前Bean id已存在");
            }
        }

        return super.doCreateInstance(classes,beanId);
    }


    private String lowerCaseBeanName(String beanName){
        return beanName.substring(0,1).toLowerCase()+beanName.substring(1);
    }

}
