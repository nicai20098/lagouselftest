package com.jiabb.server;


import java.io.IOException;
import java.io.InputStream;

/**
 * @description: 把用到的请求信息封装成request对象
 * @author: jia_b
 * @date: 2024/1/18 21:25
 * @since: 1.0
 */
public class Request {

    private String method; // 请求方式 POST/GET
    private String url;    // 请求路径 / /index.html

    private InputStream inputStream; //输入流 其他属性从输入流中解析出来

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        // 从输入流中获取请求信息
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String inputStr = new String(bytes);
        String firstLineStr = inputStr.split("\\n")[0];
        String[] first = firstLineStr.split(" ");
        this.method = first[0];
        this.url = first[1];

        System.out.println("method --> " + method + ";  url -->" + url + ";");
    }

    public Request() {
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}