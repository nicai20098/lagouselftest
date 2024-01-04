package com.jiabb.context.em;

/**
 * @author 指定代理类型
 */
public enum ProxyTypeEnum {

    JDK("JDK"),
    CGLIB("CGLIB");

    private ProxyTypeEnum(String name){
        this.name=name;
    }
    private String name;


}
