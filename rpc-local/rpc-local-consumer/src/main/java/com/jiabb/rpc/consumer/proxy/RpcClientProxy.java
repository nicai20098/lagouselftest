package com.jiabb.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.jiabb.rpc.common.RpcRequest;
import com.jiabb.rpc.common.RpcResponse;
import com.jiabb.rpc.consumer.client.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @description: 代理类
 * 1. 封装request请求对象
 * 2. 创建RpcClient
 * 3. 发送消息
 * 4. 返回结果
 * @author: jia_b
 * @date: 2024/1/30 23:24
 * @since: 1.0
 */
public class RpcClientProxy {

    public static Object createProxy(Class<?> serviceClass) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        request.setMethodName(method.getName());
                        RpcClient rpcClient = new RpcClient("127.0.0.1", 8899);
                        try {
                            Object response = rpcClient.send(JSON.toJSONString(request));
                            RpcResponse rpcResponse = JSON.parseObject(response.toString(), RpcResponse.class);
                            if (rpcResponse.getError() != null) {
                                throw new RuntimeException(rpcResponse.getError());
                            }
                            Object result = rpcResponse.getResult();
                            return JSON.parseObject(result.toString(), method.getReturnType());
                        } finally {
                            rpcClient.close();
                        }

                    }
                });
    }


}