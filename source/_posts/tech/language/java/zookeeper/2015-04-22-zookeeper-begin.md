---
layout: post
title: ZooKeeper安装与Java调用
categories: zookeeper
tags: [java,zookeeper]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 下载

ZooKeeper目前是Apache顶级项目，可从[此处下载](http://zookeeper.apache.org/releases.html)

# 安装

- 将下载的压缩包，解压缩到任意路径下.(例如:d:/soft)
- 至ZooKeeper目录/conf下，会发现有一个zoo_sample.cfg，在当前目录拷贝一份，并重命名为zoo.cfg
- 进入ZooKeeper目录/bin下，通过zkServer.cmd或zkServer.sh启动ZooKeeper

# 客户端操作

- 在ZooKeeper目录/bin下，使用命令行启动zkCli.cmd或者zkCli.sh即可启动ZooKeeper客户端
- 在客户端可以执行: ls,get,set等命令，来展示，获取或设置值

```sh
ls /                        //列出/下的内容
set /test test              //给/test目录设值
get /test                   //获取/test目录的值
```

<!-- more -->

# Java操作

- 新建一个Maven项目
- 在pom.xml中添加ZooKeeper依赖
- 新建一个类，这里叫ZookeeperTest，编写如下代码

pom.xml添加依赖:

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>3.4.5</version>
    </dependency>
</dependencies>
```

ZookeeperTest代码:

```java
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by wangyifan on 2015/4/22.
 */
public class ZookeeperTest {
    private ZooKeeper zk = null;

    public ZookeeperTest() {
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 500000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println(event.getPath());
                    System.out.println(event.getType().name());
                    //System.out.println(event.getState().getIntValue());
                }
            });
            zk.exists("/root/childone", true);//观察这个节点发生的事件
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createNodes() {
        try {
            //创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
            zk.create("/root", "mydata".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
            zk.create("/root/childone", "childone".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNodes() {
        try {
            //取得/root/childone节点下的数据,返回byte[]
            System.out.println(new String(zk.getData("/root/childone", true, null)));

            //修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
            zk.setData("/root/childone", "childonemodify2".getBytes(), -1);

            System.out.println(new String(zk.getData("/root/childone", true, null)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNodes() {
        try {
            //第二个参数为版本，－1的话直接删除，无视版本
            zk.delete("/root/childone", -1);
            zk.delete("/root", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNodes() {
        try {
            //第二个参数为版本，－1的话直接删除，无视版本
            byte[] bytes = zk.getData("/zk_test",true,null);
            System.out.println("data is " + new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ZookeeperTest zkTest = new ZookeeperTest();
            //zkTest.createNodes();
//            zkTest.updateNodes();
            //zkTest.deleteNodes();
            zkTest.getNodes();

            while (true) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

# 基于Curator的操作

- ZooKeeper默认的Watcher是一次性的，即当触发了一次监听后，即失效，需重新注册
- 使用Curator可以简化此操作

添加pom依赖：

```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.7.1</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.7.1</version>
</dependency>
```

```java
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;

/**
 * Created by wangyifan on 2015/4/28.
 */
public class ZooKeeperWatcher {
    static CuratorFramework zkclient = null;
    static String nameSpace = "root";// 根节点

    static {
        String zkhost = "127.0.0.1:2181";// zk的host
        RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);// 重试机制
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost).connectionTimeoutMs(5000).sessionTimeoutMs(5000).retryPolicy(rp);
        builder.namespace(nameSpace);
        CuratorFramework zclient = builder.build();
        zkclient = zclient;
        zkclient.start();// 放在这前面执行
        zkclient.newNamespaceAwareEnsurePath("/" + nameSpace);
    }

    public static void watch() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(zkclient, "/", false);
        cache.start();

        System.out.println("监听开始/zk........");
        PathChildrenCacheListener plis = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("Event here " + event.getType());
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        System.out.println("Data: " + new String(zkclient.getData().forPath(event.getData().getPath())));
                        System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    case CHILD_UPDATED: {
                        System.out.println("Data: " + new String(zkclient.getData().forPath(event.getData().getPath())));
                        System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    case CHILD_REMOVED: {
                        System.out.println("Data: " + new String(zkclient.getData().forPath(event.getData().getPath())));
                        System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                }

            }
        };
        // 注册监听
        cache.getListenable().addListener(plis);
    }

    public static void main(String[] args) {
        try {
            ZooKeeperWatcher.watch();

            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
```

如上代码，当对/root目录进行操作时，即可监控到！