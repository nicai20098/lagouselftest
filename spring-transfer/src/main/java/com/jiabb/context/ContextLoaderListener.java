package com.jiabb.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 监听初始化整个IOC容器
 * @author  fangyuan
 *
 */
public class ContextLoaderListener implements ServletContextListener {


    /**
     * 初始化
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        //初始化servlet对象 将对象注入到Servlet中

        IOCContainer iocContainer = new IOCContainer();

        BeanFactoryRegister beanFactoryRegister = new BeanFactoryRegister(iocContainer,servletContextEvent);

        beanFactoryRegister.init();
    }

    /**
     * 销毁
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
