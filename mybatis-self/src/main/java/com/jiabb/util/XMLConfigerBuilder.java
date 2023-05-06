package com.jiabb.util;

import com.jiabb.config.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:04
 * @since: 1.0
 */
public class XMLConfigerBuilder {

    private Configuration configuration;

    public XMLConfigerBuilder(Configuration configuration) {
        this.configuration = new Configuration();
    }

    public Configuration parseConfiguration(InputStream inputStream) throws
            DocumentException, PropertyVetoException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream);
        //<configuation>
        Element rootElement = document.getRootElement();
        List<Element> propertyElements =
                rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element propertyElement : propertyElements) {
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            properties.setProperty(name, value);
        }
        //连接池
        ComboPooledDataSource comboPooledDataSource = new
                ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        //填充 configuration
        configuration.setDataSource(comboPooledDataSource);
        //mapper 部分
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        XMLMapperBuilder xmlMapperBuilder = new
                XMLMapperBuilder(configuration);
        for (Element mapperElement : mapperElements) {
            String mapperPath = mapperElement.attributeValue("resource");
            InputStream resourceAsSteam =
                    Resources.getResourceAsSteam(mapperPath);
            xmlMapperBuilder.parse(resourceAsSteam);
        }
        return configuration;

    }
}
