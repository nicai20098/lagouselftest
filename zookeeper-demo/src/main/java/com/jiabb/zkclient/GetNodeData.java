package com.jiabb.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/7 11:33
 * @since: 1.0
 */
public class GetNodeData {

    public static void main(String[] args) throws InterruptedException {
        String path = "/demo-zkClient";
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        // 判断节点是否存在
        boolean exists = zkClient.exists(path);
        if (!exists) {
            zkClient.createEphemeral(path);
        }

        // 注册监听事件
        zkClient.subscribeDataChanges(path, new IZkDataListener() {

            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(s + " 's 内容更新, 当前内容:" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(s + " 's 内容删除");
            }
        });
        // 获取节点数据
        Object o = zkClient.readData(path);
        System.out.println(o);
        // 获取节点数据
        zkClient.writeData(path, "456");
        System.out.println(o);
        zkClient.delete(path);

    }

}