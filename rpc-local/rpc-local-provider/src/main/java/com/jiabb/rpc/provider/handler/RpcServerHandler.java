package com.jiabb.rpc.provider.handler;

import com.alibaba.fastjson.JSON;
import com.jiabb.rpc.common.RpcRequest;
import com.jiabb.rpc.common.RpcResponse;
import com.jiabb.rpc.provider.anno.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @description: 1. 将标有@RpcService注解的bean缓存
 *               2. 接收客户端请求
 *               3. 根据传递过来的beanName从缓存中查找到对应的bean
 *               4. 解析请求中的请求方法, 参数类型 参数信息
 *               5. 反射调用bean的方法
 *               6. 给客户进行响应
 * @author: jia_b
 * @date: 2024/1/30 22:48
 * @since: 1.0
 */
@Component
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    private static final Map<String, Object> SERVICE_INSTANCE_MAP = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        RpcRequest request = JSON.parseObject(s, RpcRequest.class);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        try {
            rpcResponse.setResult(handler(request));
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setError(e.getMessage());
        }
        channelHandlerContext.writeAndFlush(JSON.toJSONString(rpcResponse));
    }

    public Object handler(RpcRequest request) throws InvocationTargetException {
        String className = request.getClassName();
        Object serviceBean = SERVICE_INSTANCE_MAP.get(className);
        if (Objects.isNull(serviceBean)) {
            throw new RuntimeException("根据beanName找不到服务, beanName:" + className);
        }
        Class<?> serviceBeanClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        FastClass fastClass = FastClass.create(serviceBeanClass);
        FastMethod method = fastClass.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serverMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serverMap.size() > 0) {
            Set<Map.Entry<String, Object>> entries = serverMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object serviceBean = entry.getValue();
                if (serviceBean.getClass().getInterfaces().length == 0) {
                    throw new RuntimeException("服务必须实现接口");
                }
                String name = serviceBean.getClass().getInterfaces()[0].getName();
                // 默认取第一个接口作为bean的名称
                SERVICE_INSTANCE_MAP.put(name, serviceBean);
            }
        }

    }
}