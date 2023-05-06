package com.jiabb.util;

import java.io.InputStream;

/**
 * @description: 数据源解析
 * @author: jia_b
 * @date: 2023/5/6 23:01
 * @since: 1.0
 */
public class Resources {

    public static InputStream getResourceAsSteam(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

}
