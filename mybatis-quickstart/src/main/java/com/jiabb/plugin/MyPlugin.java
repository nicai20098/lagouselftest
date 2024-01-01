package com.jiabb.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * @description: 自定义插件
 * @author: jia_b
 * @date: 2024/1/1 20:47
 * @since: 1.0
 */
@Intercepts({
        @Signature(type = StatementHandler.class,
                    method = "prepare",
                    args = {Connection.class, Integer.class})
})
public class MyPlugin implements Interceptor {

    /**
     * 拦截方法：是要被拦截的目标对象的目标方法被执行时，每次都会执行intercept
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("对方法进行增强");
        return invocation.proceed();
    }

    /**
     * 主要为了把当前的拦截器生成代理存到拦截器链中
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 获取配置文件参数
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("获取到的参数信息 ->" + properties);
    }
}