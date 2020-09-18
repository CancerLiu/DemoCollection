package com.demo.example.demo.curator_demo;

import javafx.util.Pair;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.function.Consumer;

/**
 * 监视器相关的内容
 * <p>
 * ConnectionStateListener可以使用此监听器来保证至少当前客户端和服务器端还连接着
 */
public class SoundMonitor {
    private static final String PATH = "/example/pathCache";

    /**
     * 具体是监控Path的子节点的创建、删除、数据更新
     * <p>
     * 最后记得关闭cache和client(先cache后client)
     *
     * @param connectionInfo
     * @param path
     * @param consumer
     * @param obj
     * @return
     * @throws Exception
     */
    public static Pair<CuratorFramework, PathChildrenCache> getPathCacheData(String connectionInfo, String path, Consumer consumer, Object obj) throws Exception {
        //创建client并启动
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionInfo, new ExponentialBackoffRetry(1000, 3));
        client.start();

        //client启动后将之与cache关联
        PathChildrenCache cache = new PathChildrenCache(client, path, true);


        //定义PathCache监听器类及监听之后的逻辑，然后放入逻辑.这里client1和event分别表示绑定的CuratorFramework和PathChildrenCacheEvent
        PathChildrenCacheListener cacheListener = (client1, event) -> {
            System.out.println("事件类型：" + event.getType());
            if (null != event.getData()) {
                //这里写监听之后的逻辑
                consumer.accept(obj);
                System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
            }
        };
        cache.start();
        cache.getListenable().addListener(cacheListener);

        //可以通过cache的getCurrentData(...)方法得到监听到的所有子节点
        return new Pair<>(client, cache);
    }


    /**
     * 监听path节点的创建，删除和数据更新
     * 还是记得关闭，先cache后client
     *
     * @param connectionInfo
     * @param path
     * @param consumer
     * @param obj
     * @throws Exception
     */
    public static Pair<CuratorFramework, NodeCache> getNodeCacheData(String connectionInfo, String path, Consumer consumer, Object obj) throws Exception {
        //创建client并启动
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionInfo, new ExponentialBackoffRetry(1000, 3));
        client.start();

        //创建NodeCache并加入监听器，然后启动
        final NodeCache cache = new NodeCache(client, path);
        NodeCacheListener listener = () -> {
            //
            ChildData data = cache.getCurrentData();
            if (null != data) {
                consumer.accept(obj);
                System.out.println("节点数据：" + new String(cache.getCurrentData().getData()));
            } else {
                System.out.println("节点被删除!");
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();

        return new Pair<>(client, cache);
    }


    public static Pair<CuratorFramework, TreeCache> getTreeCacheData(String connectionInfo, String path, Consumer consumer, Object obj) throws Exception {
        //创建client并启动
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionInfo, new ExponentialBackoffRetry(1000, 3));
        client.start();

        //创建监听对象，与client对象做关联
        TreeCache cache = new TreeCache(client, path);
        TreeCacheListener listener = (client1, event) -> {
            System.out.println("事件类型：" + event.getType() +
                    " | 路径：" + (null != event.getData() ? event.getData().getPath() : null));
            if (null != event.getData()) {
                consumer.accept(obj);
                System.out.println("数据为: " + event.getData().getData());
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();

        return new Pair<>(client, cache);
    }
}
