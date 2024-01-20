package com.jiabb;

/**
 * @description: 普通哈希
 * @author: jia_b
 * @date: 2024/1/20 20:45
 * @since: 1.0
 */
public class GeneralHash {

    public static void main(String[] args) {
        // 定义客户端ip
        String[] clients = new String[] {"10.78.12.3", "113.25.63.1", "126.12.3.8"};
        // 定义服务器数量
        int serverCount = 3; // 对应编号0, 1, 2
        // 路由计算
        // 根据index锁定应该路由的服务器
        for (String client : clients) {
            int hash = Math.abs(client.hashCode());
            int index = hash%serverCount;
            System.out.println("客户端:" + client + " 被路由到服务器编号为:" + index);

        }


    }

}