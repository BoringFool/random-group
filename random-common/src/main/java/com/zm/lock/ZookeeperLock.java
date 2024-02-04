package com.zm.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ZookeeperLock implements AutoCloseable, Watcher {
    private final ZooKeeper zooKeeper;
    private String zNode;

    public ZookeeperLock() throws IOException {
        this.zooKeeper = new ZooKeeper("localhost:12181", 10000, this);
    }

    public Boolean getLock(String code) {
        try {
            //获取根节点状态
            Stat exists = zooKeeper.exists("/" + code, false);
            //根节点不存在则创建
            if (exists == null) {
                zooKeeper.create("/" + code, code.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            //创建瞬时节点
            zNode = zooKeeper.create("/" + code, code.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            //获取所有子节点并升序排序
            List<String> children = zooKeeper.getChildren("/" + code, false);
            Collections.sort(children);

            String firstNode = children.get(0);
            String lastNode = firstNode;
            //说明刚刚创建的节点是第一个节点，拿到了锁
            if (zNode.endsWith(firstNode)) {
                return true;
            }

            //查找当前节点的前一个节点，添加watch
            for (String node : children) {
                if (zNode.endsWith(node)) {
                    zooKeeper.exists("/" + code + "/" + lastNode, true);
                    break;
                } else {
                    lastNode = node;
                }
            }

            //阻塞
            synchronized (this) {
                wait();
            }

        } catch (InterruptedException | KeeperException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public void close() throws Exception {
        zooKeeper.delete(zNode, -1);
        zooKeeper.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify();
            }
        }
    }
}
