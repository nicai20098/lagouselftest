package com.jiabb.annotation;

import com.jiabb.context.em.ProxyTypeEnum;

import java.lang.annotation.*;

/**
 * 事务管理器注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {

    //管理器名称
    String value() default "transactionManager";

    //代理类型
    ProxyTypeEnum type() default ProxyTypeEnum.JDK;

    //定义哪些异常需要被回滚
    Class<? extends Throwable>[] rollbackFor() default {};

}
