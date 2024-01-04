package com.jiabb.context;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author IOC容器 存储整个IOC生成的对象
 *
 */
@Getter
@Setter
public class IOCContainer {

    //存储对象
    private  Map<String,Object> singleObject = new ConcurrentHashMap<>(256);

    //缓存池子 解决循环依赖问题
    private  Map<String,Object> cacheSingleBeanObject = new ConcurrentHashMap<>(256);

    //缓存池子 解决循环依赖问题
    private  Map<String,Object> cacheSingleObject = new ConcurrentHashMap<>(256);


    public  IOCContainer(){

    }

    public boolean contains(String beanName){
        return singleObject.containsKey(beanName);
    }


}
