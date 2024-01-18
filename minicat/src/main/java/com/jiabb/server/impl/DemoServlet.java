package com.jiabb.server.impl;

import com.jiabb.server.HttpProtocolUtil;
import com.jiabb.server.Request;
import com.jiabb.server.Response;

import java.io.IOException;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/18 22:02
 * @since: 1.0
 */
public class DemoServlet extends HttpServlet{
    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>DemoServlet GET</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.length()) + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>DemoServlet POSt</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.length()) + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}