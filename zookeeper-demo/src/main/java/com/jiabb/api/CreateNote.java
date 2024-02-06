package com.jiabb.api;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/5 23:40
 * @since: 1.0
 */
public class CreateNote implements Watcher {

//    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper = null;

    /**
     * 建立会话 并创建节点
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("192.168.208.10:2181", 5000, new CreateNote());
        System.out.println(zooKeeper.getState());
//        countDownLatch.await();
        System.out.println("建立连接");
        Thread.sleep(Integer.MAX_VALUE);

    }

    /**
     * 创建节点的方法
     */
    private static void createNoteSync() throws InterruptedException, KeeperException {
        /**
         * path        节点创建的路径 /开头
         * data[]      节点创建要保存的数据，是个byte类型的
         * acl         节点创建的权限信息(4种类型)
         *                      ANYONE_ID_UNSAFE:表示任何人
         *                      AUTH_IDS:此ID仅可用于设置ACL。它将被客户机验证的ID替换。
         *                      OPEN_ACL_UNSAFE:这是一个完全开放的ACL(常用)-->world:anyone
         *                      CREATOR_ALL_ACL:此ACL授予创建者身份验证ID的所有权限
         * createMode   创建节点的类型(4种类型)
         *                      PERSISTENT:持久节点
         *                      PERSISTENT_SEQUENTIAL:持久顺序节点
         *                      EPHEMERAL:临时节点
         *                      EPHEMERAL_SEQUENTIAL:临时顺序节点
         * String node = zookeeper.create(path,data,acl,createMode);
         */
        // 持久节点
        String node_persistent = zooKeeper.create("/demo-persistent", "持久节点内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(node_persistent);
        // 临时节点
        String node_ephemeral = zooKeeper.create("/demo-ephemeral", "临时节点内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(node_ephemeral);
        // 持久顺序节点
        String node_persistent_sequential = zooKeeper.create("/demo-persistent-sequential", "持久顺序节点内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(node_persistent_sequential);
        // 临时顺序节点
        String node_ephemeral_sequential = zooKeeper.create("/demo-ephemeral-sequential", "临时顺序节点内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(node_ephemeral_sequential);

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
//            countDownLatch.countDown();
            try {
                createNoteSync();
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }
    }
}
