package com.jiabb.server.impl;

import com.jiabb.server.Request;
import com.jiabb.server.Response;
import com.jiabb.server.Servlet;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/18 21:59
 * @since: 1.0
 */
public abstract class HttpServlet implements Servlet {

    public abstract void doGet(Request request, Response response);
    public abstract void doPost(Request request, Response response);

    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }


    }
}