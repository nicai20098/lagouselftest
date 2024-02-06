package com.jiabb.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @description: TODO
 * @author: jiabb-b
 * @date: 2024/2/06 006 11:05
 * @since: 1.0
 */
public class CreateSession {


    public static void main(String[] args) {


        ZkClient zkClient = new ZkClient("192.168.208.10:2181");
        zkClient.createPersistent("/demo-zkClient/c1", true);
        System.out.println("success create zNode.");

    }

}
