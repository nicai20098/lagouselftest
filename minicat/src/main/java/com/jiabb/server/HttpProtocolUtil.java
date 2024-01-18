package com.jiabb.server;

/**
 * @description: http 协议工具类, 主要提供响应头信息, 这里只提供200 和 404的情况
 * @author: jia_b
 * @date: 2024/1/18 21:11
 * @since: 1.0
 */
public class HttpProtocolUtil {


    /**
     * 为响应码200提供请求头信息
     */
    public static String getHttpHeader200(long contentLength) {
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + contentLength + " \n" +
                "\r\n";
    }

    /**
     * 为响应码404提供相应信息
     */
    public static String getHttpHeader404() {
        String str404 = "<h1>404 not found</h1>";
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + str404.getBytes().length + " \n" +
                "\r\n" + str404;
    }


}