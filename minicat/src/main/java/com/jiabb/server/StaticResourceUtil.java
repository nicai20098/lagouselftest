package com.jiabb.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/18 21:40
 * @since: 1.0
 */
public class StaticResourceUtil {

    /**
     * 获取静态资源我呢见的绝对路径
     */
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    /**
     * 读取静态资源文件输入流, 通过输出流输出
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
       int count = 0;
       while (count == 0) {
           count = inputStream.available();
       }
       int resourceSize = count;
       outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());
        long written = 0;
        int byteSize = 1024;
        byte[] bytes = new byte[byteSize];
        while (written < resourceSize) {
            if (written + byteSize > resourceSize) {
                byteSize = (int) (resourceSize - written);
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written+=byteSize;
        }
    }


}