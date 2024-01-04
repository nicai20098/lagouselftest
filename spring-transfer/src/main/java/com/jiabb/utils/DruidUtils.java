package com.jiabb.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.jiabb.annotation.Component;

/**
 * @author 应癫
 */
@Component("druidUtils")
public class DruidUtils {

    private  DruidDataSource druidDataSource = new DruidDataSource();

    public DruidUtils(){
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/bank");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
    }

    public  DruidDataSource getInstance() {
        return druidDataSource;
    }

}
