package com.jiabb.context;

import com.jiabb.context.factory.AnnotationBeanFactory;
import com.jiabb.context.factory.XmlBeanFactory;

import javax.servlet.ServletContextEvent;

/**
 *
 */
public class BeanFactoryRegister {

    private IOCContainer iocContainer;

    private ServletContextEvent servletContextEvent;

    public BeanFactoryRegister(IOCContainer iocContainer,ServletContextEvent servletContextEvent){
        this.iocContainer = iocContainer;
        this.servletContextEvent=servletContextEvent;
    }

    public void init(){

        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(iocContainer);

        AnnotationBeanFactory annotationBeanFactory = new AnnotationBeanFactory(iocContainer,servletContextEvent);

        xmlBeanFactory.buildInstance();

        annotationBeanFactory.buildInstance();

        xmlBeanFactory.injectionProperty();

        annotationBeanFactory.injectionProperty();

        xmlBeanFactory.initTransactional();

        annotationBeanFactory.initTransactional();

    }


}
