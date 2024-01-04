package com.jiabb.context.factory;

import com.jiabb.context.IOCContainer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author fangyuan
 *
 * 解析xml配置文件
 */
public class XmlBeanFactory extends AbstractBeanFactory {

    private Element rootElement;

    public XmlBeanFactory(IOCContainer iocContainer) {
        super(iocContainer);
        // 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
        // 加载xml
        InputStream resourceAsStream = XmlBeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        // 解析xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            this.rootElement = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void buildInstance(){

        List<Element> beanList = rootElement.selectNodes("//bean");
        for (int i = 0; i < beanList.size(); i++) {
            Element element =  beanList.get(i);
            // 处理每个bean元素，获取到该元素的id 和 class 属性
            String id = element.attributeValue("id");        // accountDao
            String clazz = element.attributeValue("class");  // com.jiabb.dao.impl.JdbcAccountDaoImpl
            this.doCreateInstance(clazz,id);
        }

    }

    @Override
    public void injectionProperty() {
        try {
            // 实例化完成之后维护对象的依赖关系，检查哪些对象需要传值进入，根据它的配置，我们传入相应的值
            // 有property子元素的bean就有传值需求
            List<Element> propertyList = this.rootElement.selectNodes("//property");
            // 解析property，获取父元素
            for (int i = 0; i < propertyList.size(); i++) {
                Element element =  propertyList.get(i);   //<property name="AccountDao" ref="accountDao"></property>
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                // 找到当前需要被处理依赖关系的bean
                Element parent = element.getParent();

                // 调用父元素对象的反射功能
                String parentId = parent.attributeValue("id");
                Object parentObject = iocContainer.getSingleObject().get(parentId);

                // 遍历父对象中的所有方法，找到"set" + name
                Method[] methods = parentObject.getClass().getMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    if(method.getName().equalsIgnoreCase("set" + name)) {  // 该方法就是 setAccountDao(AccountDao accountDao)

                        method.invoke(parentObject,iocContainer.getSingleObject().get(ref));

                    }
                }
                // 把处理之后的parentObject重新放到map中
                iocContainer.getSingleObject().put(parentId,parentObject);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initTransactional() {

    }


}
