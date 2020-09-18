package com.demo.example.demo.curator_demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class NormalOperation {

    /*基本操作*/

    /**
     * 创建会话
     */
    public static CuratorFramework createSession(String curatorNodeName, String connectionInfo) {
        //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionInfo, 5000, 3000, retryPolicy);
        client.start();
        client.usingNamespace(curatorNodeName);
        return client;
    }

    public static void createPath(String path, CuratorFramework client, byte[] bytes) throws Exception {
        client.create().creatingParentContainersIfNeeded().forPath(path, bytes);
    }

    public static void deletePath(String path, CuratorFramework client) throws Exception {
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
    }

    public static List<String> getChidrenNodes(String path, CuratorFramework client) throws Exception {
        return client.getChildren().forPath(path);
    }

    public static Stat getOrSetNode(String path, CuratorFramework client, byte[] bytes) throws Exception {
        Stat stat = new Stat();

        if (null == bytes || bytes.length == 0) {
            client.getData().storingStatIn(stat).forPath(path);
        } else {
            stat = client.setData().forPath(path, bytes);
        }
        return stat;
    }

    public static Stat checkNode(String path, CuratorFramework client) throws Exception {
        return client.checkExists().forPath(path);
    }

    /*监听器*/

    public static void main(String[] args) throws Exception {
        CuratorFramework client = createSession("curator-firstNode", "/*47.106.151.134:2181,47.106.151.134:2182,47.106.151.134:2183*/");

        client.delete().forPath("/create-firstName");
        client.create().forPath("/create-secondName", "createNodeDataOne".getBytes());
    }


}
