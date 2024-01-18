package com.jiabb.server;

import com.jiabb.server.impl.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 启动入口
 * @author: jia_b
 * @date: 2024/1/18 20:58
 * @since: 1.0
 */
public class Bootstrap {

    /**
     * 定义socket 端口号
     */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    /**
     * 启动需要初始化展开的一些操作
     */
    public void start() throws IOException {


        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>> MiniCat start on port: " + port);
        /**
         * 完成MiniCat 1.0
         *  需求:浏览器请求 http://localhost:8080 返回一个固定的字符串到页面 "Hello MiniCat!"
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            // socket,接收到请求, 获取输出流
//            OutputStream outputStream = socket.getOutputStream();
//            String data = "Hello MiniCat!";
//            outputStream.write((HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data).getBytes());
//            socket.close();
//        }

        /**
         * 完成MiniCat 2.0
         *  需求: 封装Requset/Response对象, 返回html静态资源
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            // socket,接收到请求, 获取输出流
//            InputStream inputStream = socket.getInputStream();
//            // 封装Request/Response
//            Request request = new Request(inputStream);
//            Response response = new Response(socket.getOutputStream());
//            response.outputHtml(request.getUrl());
//            inputStream.close();
//            socket.close();
//
//        }


        /**
         * 完成MiniCat 3.0
         *  需求: 封装Requset/Response对象, 返回html静态资源
         */
        // 加载解析相关配置配置
        loadServlet();
        while (true) {
            Socket socket = serverSocket.accept();
            // socket,接收到请求, 获取输出流
            new RequestProcessor(socket, servletMap).start();

        }

    }


    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 加载解析web.xml 初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                Element servletNameElement = (Element)element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();
                Element servletClassElement = (Element)element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();

                Element selectServletMapping = (Element)rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = selectServletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet)Class.forName(servletClass).newInstance());
            }

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * MiniCat 启动入口
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动MiniCat
            bootstrap.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}