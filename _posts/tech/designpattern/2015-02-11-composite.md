---
layout: post
title: 语言与模式-09组合模式
categories: [designpattern]
tags: [designpattern,java,clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---
# 意图

将对象组合成树形结构以表示“部分-整体”的层次结构。Composite使得用户对单个对象和组合对象的使用具有一致性。

![]({{site.CDN_PATH}}/assets/designpattern/composite.jpg)

# 适用性

- 你想表示对象的部分—整体层次结构。
- 你希望用户忽略组合对象与单个对象的不同，用户将统一地使用组合结构中的所有对象。

# Java实现

```java
public interface Node {
    void check();
    void add(Node node) throws Exception;
    void remove(Node node) throws Exception;
}
```

```java
public class ParentNode implements Node {
    private List<Node> list = new ArrayList<Node>();

    public void check() {
        System.out.println("ParentNode is checked");
        for(Node n : list){
          n.check();
        }
    }

    public void add(Node node) {
        list.add(node);
    }

    public void remove(Node node) {
        list.remove(node);
    }
}
```

<!-- more -->

```java
public class ChildNode implements Node {
    public void check() {
        System.out.println("ChildNode is Checked");
    }

    public void add(Node node) throws Exception {
        throw new Exception("This node can not add");
    }

    public void remove(Node node) throws Exception {
        throw new Exception("This node can not remove");
    }
}
```

```java
public class Main {

    public static void main(String[] args) throws Exception {
        Node node1 = new ChildNode();
        Node node2 = new ChildNode();
        Node node3 = new ParentNode();
        node3.add(node1);
        node3.add(node2);
        node1.check();
        node2.check();
        node3.check();
    }
}
```

# Clojure实现

实际上就是对树的操作！Clojure提供了clojure.zip,clojure.walk或者tree-seq来进行对树的操作！

操作起来也不是很方便就是了！
