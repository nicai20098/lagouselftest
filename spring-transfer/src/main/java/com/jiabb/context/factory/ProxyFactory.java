package com.jiabb.context.factory;


import com.jiabb.context.MethodCglibInvocationHandler;
import com.jiabb.context.MethodJDKInvocationHandler;
import com.jiabb.context.transaction.TransactionManager;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @author fangyuan
 *
 *
 * 代理对象工厂：生成代理对象的
 */
public class ProxyFactory {

    /**
     * Jdk动态代理
     * @param obj  委托对象
     * @return   代理对象
     */
    public static Object getJdkProxy(TransactionManager transactionManager, Object obj) {
        // 获取代理对象
        return  Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new MethodJDKInvocationHandler(transactionManager,obj));
    }


    /**
     * 使用cglib动态代理生成代理对象
     * @param obj 委托对象
     * @return
     */
    public static Object getCglibProxy(TransactionManager transactionManager,Object obj) {
        return  Enhancer.create(obj.getClass(), new MethodCglibInvocationHandler(transactionManager,obj));
    }

}
