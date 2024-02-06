package com.jiabb.api;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/5 23:40
 * @since: 1.0
 */
public class GetNoteData implements Watcher {

    private static ZooKeeper zooKeeper = null;

    /**
     * 建立会话 并创建节点
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("192.168.208.10:2181", 5000, new GetNoteData());
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
            getNoteData();
            getChildren();
        }

        //子节点列表发生改变时, 服务器端会发生noteChildrenChanged事件通知要重新获取子节点列表
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            getChildren();
        }
    }

    /**
     * 获取某个节点的数据
     */
    private void getNoteData() {
        /**
         * path : 获取数据的路径
         * watch: 是否开启监听
         * stat : 节点状态信息 null: 表示获取最新版本的数据
         */
        try {
            byte[] data = zooKeeper.getData("/demo-persistent", false, null);
            System.out.println(new String(data));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某个节点的子节点列表方法
     */
    private void getChildren() {
        /**
         * path 路径
         * watch 是否要监听, 当子节列表发生变化, 会触发监听
         */
        try {
            List<String> children = zooKeeper.getChildren("/", false);
            System.out.println(children);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
