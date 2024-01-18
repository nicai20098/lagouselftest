package com.jiabb.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * @description: 封装Response对象, 需要依赖于OutputStream
 * 该对象需要提供核心方法, 输出html
 * @author: jia_b
 * @date: 2024/1/18 21:26
 * @since: 1.0
 */
public class Response {

    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 输出指定字符串
     */
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }

    /**
     * @param path 根据path 来获取静态资源的绝对路径, 进一步根据绝对路径读取静态文件,最终通过输出流输出
     */
    public void outputHtml(String path) throws IOException {
        // 获取静态资源文件的绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);
        //输入静态静态资源文件
        File file = new File(absoluteResourcePath);
        if (file.exists() && file.isFile()) {
            // 读取静态资源文件, 输出静态资源
            StaticResourceUtil.outputStaticResource(Files.newInputStream(file.toPath()), outputStream);
        } else {
            // 输出404
            output(HttpProtocolUtil.getHttpHeader404());
        }

    }


}