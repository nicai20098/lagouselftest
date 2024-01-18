package com.jiabb.server;

/**
 * @description: httpServlet
 * @author: jia_b
 * @date: 2024/1/18 21:57
 * @since: 1.0
 */
public interface Servlet {

    void init() throws Exception;

    void destroy() throws Exception;

    void service(Request request, Response response) throws Exception;

}