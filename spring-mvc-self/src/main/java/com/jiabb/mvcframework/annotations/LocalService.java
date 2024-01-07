package com.jiabb.mvcframework.annotations;

import java.lang.annotation.*;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/7 23:04
 * @since: 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalService {

    String value() default "";
}