package com.jiabb.server;

import com.jiabb.server.impl.HttpServlet;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/18 22:29
 * @since: 1.0
 */
public class RequestProcessor extends Thread {

    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            // 封装Request/Response
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            HttpServlet httpServlet = servletMap.get(request.getUrl());
            if (servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            } else {
                httpServlet.service(request, response);
            }
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}