package com.jiabb.context.factory;

import com.jiabb.context.BeanFactory;
import com.jiabb.context.IOCContainer;

import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    protected IOCContainer iocContainer;

    public AbstractBeanFactory(IOCContainer iocContainer){
        this.iocContainer=iocContainer;
    }

    // 任务二：对外提供获取实例对象的接口（根据id获取）
    @Override
    public   Object getBean(String id) {
        return iocContainer.getSingleObject().get(id);
    }

    public final void doCreateInstance(String classes,String beanId){
        try {
            this.doCreateInstance(Class.forName(classes),beanId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化对象实例
     */
    public abstract void buildInstance();

    /**
     * 注入对象
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public abstract void injectionProperty();

    /**
     * 处理事务
     */
    public abstract void initTransactional();

    public final Object doCreateInstance(Class<?> classes,String beanId){

        //初始化Bean
        // 通过反射技术实例化对象
        Object o = null;  // 实例化之后的对象
        try {
            o = classes.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 存储到map中缓存池中 后续解决循环依赖的问题
        this.iocContainer.getSingleObject().put(beanId,o);
        return o;
    }

}
