package com.jiabb.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.w3c.dom.ls.LSException;

import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/7 11:33
 * @since: 1.0
 */
public class GetChildren {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        List<String> children = zkClient.getChildren("/demo-zkClient");
        System.out.println(children);

        // 注册监听事件
        zkClient.subscribeChildChanges("/demo-zkClient", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> list) throws Exception {
                System.out.println(parentPath + " 's child change, currentChildren:" + list);
            }
        });
        zkClient.createPersistent("/demo-zkClient");
        Thread.sleep(1000);
        zkClient.createPersistent("/demo-zkClient/c1");
        Thread.sleep(1000);
        zkClient.delete("/demo-zkClient/c1");
        Thread.sleep(1000);
        zkClient.delete("/demo-zkClient");
        Thread.sleep(Integer.MAX_VALUE);

    }

}