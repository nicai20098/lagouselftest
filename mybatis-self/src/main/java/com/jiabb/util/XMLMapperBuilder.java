package com.jiabb.util;

import com.jiabb.config.Configuration;
import com.jiabb.mapping.MappedStatement;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:06
 * @since: 1.0
 */
public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> select = rootElement.selectNodes("//select");
        for (Element element : select) { //id的值
            String id = element.attributeValue("id");
            String paramterType = element.attributeValue("paramterType");
            String resultType = element.attributeValue("resultType");
            MappedStatement mappedStatement = new MappedStatement();
            //输入参数class
            if (StringUtils.isNotEmpty(paramterType)) {
                Class<?> paramterTypeClass = getClassType(paramterType);
                mappedStatement.setParamterType(paramterTypeClass);
            }
            //返回结果class
            if (StringUtils.isNotEmpty(resultType)) {
                Class<?> resultTypeClass = getClassType(resultType);
                mappedStatement.setResultType(resultTypeClass);
            }
            //statementId
            String key = namespace + "." + id;
            //sql语句
            String textTrim = element.getTextTrim();
            //封装 mappedStatement
            mappedStatement.setId(id);
            mappedStatement.setSql(textTrim);
            //填充 configuration
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }
    }

    private Class<?> getClassType(String classType) throws ClassNotFoundException {
        return Class.forName(classType);
    }
}
