package com.jiabb;

import java.time.temporal.ValueRange;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @description: 一致性hash 包含虚拟节点
 * @author: jia_b
 * @date: 2024/1/20 20:52
 * @since: 1.0
 */
public class ConsistentHashWithVirtual {

    public static void main(String[] args) {
        // step 1 初始化: 把服务器节点ip的哈希值对应到哈希环
        // 定义服务器ip
        String[] tomcatServers = new String[] {"123.111.0.0", "123.101.3.1", "111.20.35.2", "126.98.26.3"};

        SortedMap<Integer, String> hashServerMap = new TreeMap<>();

        // 定义针对每个真实服务器虚拟出来几个系欸但
        int virtualCount = 3;

        for (String tomcatServer : tomcatServers) {
            // 求出每个ip的hash值,对应到hash环上, 存储hash值与ip的对应关系
            int serverHash = Math.abs(tomcatServer.hashCode());
            // 存储hash值与ip的对应关系
            hashServerMap.put(serverHash, tomcatServer);

            // 处理虚拟节点
            for (int i = 0; i < virtualCount; i++) {
                int virtualHash = Math.abs((tomcatServer + "#" + i).hashCode());
                hashServerMap.put(virtualHash, "由虚拟节点映射出来的请求:" + tomcatServer);
            }

        }
        // step 2 针对客户端ip求出hash值
        // 定义客户端ip
        String[] clients = new String[] {"10.78.12.3", "113.25.63.1", "126.12.3.8"};
        for (String client : clients) {
            int clientHash = Math.abs(client.hashCode());
            // step 3 针对客户端,找出能够处理当前客户端请求的服务器(哈希环上顺时针最近)
            // 根据客户端ip的哈希值去找那个服务器节点能处理
            SortedMap<Integer, String> integerStringSortedMap = hashServerMap.tailMap(clientHash);
            Integer firstKey = null;
            if (integerStringSortedMap.isEmpty()) {
                // 取哈希环上的顺时针第一台服务器
                firstKey = hashServerMap.firstKey();
            } else {
                firstKey = integerStringSortedMap.firstKey();
            }
            System.out.println("客户端:" + client + " 被路由到服务器编号为:" + hashServerMap.get(firstKey));
        }





    }

}