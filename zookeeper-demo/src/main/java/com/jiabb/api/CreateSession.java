package com.jiabb.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/5 23:40
 * @since: 1.0
 */
public class CreateSession implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    /**
     * 建立会话
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * 客户端可以通过创建一个Zp实例来连接zk服务器
         * new ZooKeeper(connectString, sessionTimeOut, Watcher)
         * connectString: 连接地址 IP端口
         * sessionTimeOut: 会话超时时间： 单位毫秒
         * Watcher： 监听器（当特定的事件触发监听时，zk会通过watcher通知到客户端）
         */
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1：2181", 5000, new CreateSession());
        System.out.println(zooKeeper.getState());
        countDownLatch.await();
        System.out.println("建立连接");
    }

    /**
     * 当前类实现了watcher接口，重写了process方法，该方法负责处理来自zookeeper服务端的watcher通知，
     * 在收到服务端发送过来的syncConnected事件之后，解除主程序在countDownLatch上的等待阻塞，至此，会话创建完毕
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        //当连接创建了，服务端发送给客户端syncConnected事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
        }
    }
}
