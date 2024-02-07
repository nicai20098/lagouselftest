package com.jiabb.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/2/7 12:16
 * @since: 1.0
 */
public class CuratorTest {

    public static void main(String[] args) throws Exception {

        // 不使用fluent风格
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retry);
        client.start();
        System.out.println("会话被创建了");

        // 使用fluent风格
        CuratorFramework base = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(30000)
                .retryPolicy(retry)
                .namespace("base") // 独立的命名空间 /base
                .build();
        base.start();

        base.create().forPath("test-path");

        base.create().forPath("test-path-have", "我是内容".getBytes());

        base.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("test-path-if-have");

        String deletePath = "delete-path";
        base.delete().deletingChildrenIfNeeded().withVersion(-1).forPath(deletePath);
        System.out.println("删除成功 ， 节点为：" + deletePath);

        byte[] bytes = base.getData().forPath("test-path");
        System.out.println(new String(bytes));

        Stat stat = new Stat();
        base.getData().storingStatIn(stat).forPath("test-path");
        System.out.println(stat);

        base.setData().withVersion(stat.getVersion()).forPath("test-update-path", "123".getBytes());

    }

}