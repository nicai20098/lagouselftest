package com.jiabb.api;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Objects;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/5 23:40
 * @since: 1.0
 */
public class DeleteNote implements Watcher {

    private static ZooKeeper zooKeeper = null;

    /**
     * 建立会话 并创建节点
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("192.168.208.10:2181", 5000, new DeleteNote());
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
            // 删除数据节点内容的方法
            deleteNoteDataSync();
        }
    }

    private void deleteNoteDataSync() {
        /**
         * path 路径
         * version 为-1 便是对最新版本的数据进行修改
         * zookeeper.exists(path, watcher) 判断节点是否存在
         * zookeeper.delete(path, version) 删除节点
         */
        try {
            Stat exists = zooKeeper.exists("/demo-persistent", false);
            System.out.println(Objects.isNull(exists) ? "该节点不存在" : "该节点存在");
            if (Objects.nonNull(exists)) {
                zooKeeper.delete("/demo-persistent", -1);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
