package com.jiabb.api;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/5 23:40
 * @since: 1.0
 */
public class UpdateNote implements Watcher {

    private static ZooKeeper zooKeeper = null;

    /**
     * 建立会话 并创建节点
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("192.168.208.10:2181", 5000, new UpdateNote());
        System.out.println(zooKeeper.getState());
        System.out.println("建立连接");
        Thread.sleep(Integer.MAX_VALUE);

    }


    /**
     * 当前类实现了watcher接口，重写了process方法，该方法负责处理来自zookeeper服务端的watcher通知，
     * 在收到服务端发送过来的syncConnected事件之后，解除主程序在countDownLatch上的等待阻塞，至此，会话创建完毕
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("回调事件发生了");
        //当连接创建了，服务端发送给客户端syncConnected事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("状态修改为了SyncConnected");
            // 更新数据节点内容的方法
            updateNoteDataSync();
        }
    }

    private void updateNoteDataSync() {
        /**
         * path 路径
         * data 要修改的内容 byte[]
         * version 为-1 便是对最新版本的数据进行修改
         */
        try {
            byte[] data = zooKeeper.getData("/demo-persistent", false, null);
            System.out.println("修改前数据:" + new String(data));
            // stat 状态信息对象
            Stat stat = zooKeeper.setData("/demo-persistent", "客户端修改了节点数据".getBytes(), -1);
            byte[] data1 = zooKeeper.getData("/demo-persistent", false, null);
            System.out.println("修改后数据:" + new String(data1));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
