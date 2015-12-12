---
layout: post
title: 语言与模式-16责任链模式
categories: [designpattern]
tags: [designpattern,java,clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 意图

使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。

![]({{site.CDN_PATH}}/assets/designpattern/chain.jpg)

# 适用性

- 有多个的对象可以处理一个请求，哪个对象处理该请求运行时刻自动确定。
- 你想在不明确指定接收者的情况下，向多个对象中的一个提交一个请求。
- 可处理一个请求的对象集合应被动态指定。

# Java实现

```java
public abstract class Handler {

    protected Handler successor;

    public abstract void handlerRequest(String condition);


    public Handler getSuccessor() {
        return successor;
    }
    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }
```

<!-- more -->

```java
public class ConcreteHandler1 extends Handler {

    @Override
    public void handlerRequest(String condition) {
        // 如果是自己的责任，就自己处理，负责传给下家处理
        if(condition.equals("ConcreteHandler1")){
            System.out.println( "ConcreteHandler1 handled ");
            return ;
        }else{
            System.out.println( "ConcreteHandler1 passed ");
            getSuccessor().handlerRequest(condition);
        }
    }

}
```

```java
public class ConcreteHandler2 extends Handler {

    @Override
    public void handlerRequest(String condition) {
        // 如果是自己的责任，就自己处理，负责传给下家处理
        if(condition.equals("ConcreteHandler2")){
            System.out.println( "ConcreteHandler2 handled ");
            return ;
        }else{
            System.out.println( "ConcreteHandler2 passed ");
            getSuccessor().handlerRequest(condition);
        }
    }
}
```

```java
public class ConcreteHandlerN extends Handler {

    /**
     * 这里假设n是链的最后一个节点必须处理掉
     * 在实际情况下，可能出现环，或者是树形，
     * 这里并不一定是最后一个节点。
     */
    @Override
    public void handlerRequest(String condition) {
        System.out.println( "ConcreteHandlerN handled");
    }
}
```

```java
public class Client {

    public static void main(String[] args) {

        Handler handler1 = new ConcreteHandler1();
        Handler handler2 = new ConcreteHandler2();
        Handler handlern = new ConcreteHandlerN();

        //链起来
        handler1.setSuccessor(handler2);
        handler2.setSuccessor(handlern);

        //假设这个请求是ConcreteHandler2的责任
        handler1.handlerRequest("ConcreteHandler2");
    }
}
```

# Clojure实现

```clojure

(defn handler-request1 [condition]
  (if (= "ConcreteHandler1" condition)
    (println "ConcreteHandler1 handled ")
    (println "ConcreteHandler1 passed ")))

(defn handler-request2 [condition]
  (if (= "ConcreteHandler2" condition)
    (println "ConcreteHandler2 handled ")
    (println "ConcreteHandler2 passed ")))

(defn handler-requestn [condition]
  (println "ConcreteHandlern handled "))

(->> "ConcreteHandler2"
    handler-request1
    handler-request2
    handler-requestn)
```
