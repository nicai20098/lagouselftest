package com.jiabb.annotation;

import java.lang.annotation.*;

/**
 * 暂时只支持字段类型
 * ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER,ElementType.ANNOTATION_TYPE
 * @author fangyuan
 */
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    //默认为属性
    String value() ;

}
