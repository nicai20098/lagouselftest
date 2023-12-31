package com.jiabb.io;

import java.io.InputStream;

/**
 * @description: 数据源解析
 * @author: jia_b
 * @date: 2023/5/6 23:01
 * @since: 1.0
 */
public class Resources {

    //根据配置文件的路径，将配置文件加载成字节输入流，存储在内存中
    public static InputStream getResourceAsSteam(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

}
