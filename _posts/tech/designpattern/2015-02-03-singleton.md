---
layout: post
title: 语言与模式-01单例模式
categories: [designpattern]
tags: [designpattern,java,clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 概述

设计模式是什么？如果要我现在来回答，那我的答案是：**设计模式是对于语言本身缺陷的一种补充！**

本系列文章通过对各个设计模式在Java与Clojure之间的实现比较来说明这一点！

关于Clojure相关内容，可以查看[这里](/tag/#clojure)!

其实使用Clojure来实现设计模式并不合适！因为Clojure是函数式语言，而设计模式是针对面向对象语言的。但是Clojure能提供近似的解决方案或者根本不需要此设计模式，且Clojure的解决方案更简单！简单到都不好意思叫做模式！

当然我也不是要黑Java，毕竟还要靠Java吃饭！Java作为工业化标准开发还是很不错的，只是表现力不足，模板代码较多而已！但是足够简单，易标准化。这就够了！

废话不多说，开始第一个设计模式！单例模式！

# 意图

保证一个类仅有一个实例，并提供一个访问它的全局访问点。

![]({{site.CDN_PATH}}/assets/designpattern/singleton.jpg)

# 适用性

- 当类只能有一个实例而且客户可以从一个众所周知的访问点访问它时。
- 当这个唯一实例应该是通过子类化可扩展的，并且客户应该无需更改代码就能使用一个扩展的实例时。

# Java实现

懒汉式：

```java
public class Singleton{
       private Singleton(){}

       public synchronized static Singleton getInstance(){
           if(single==null){
                single=new Singleton();
           }
           return single;
       }

      private static Singleton single;
}
```

<!-- more -->

饿汉式：

```java
public class Singleton{
       private Singleton(){}

       public static Singleton getInstance(){
                  return single;
       }
      private static Singleton single=new Singleton();
}
```

double-checked locking：

```java
public class Singleton{
       private Singleton(){}

       public static Singleton getInstance(){
           if(single==null){
               synchronized(Singleton.class){
                  if(single==null){
                     single=new Singleton();
                  }
               }
           }
           return single;
       }
      private volatile static Singleton single;
}
```

# Clojure实现

请问你获取对象是干嘛用的？

还不是为了调用它的属性或方法？只是因为Java最小粒度到Class，所以才要先创建对象，所以才导致了如上这么多的问题！所以才要所谓单例模式！

而Clojure最小粒度到函数(也就是Java中的方法)，也就没有所谓创建对象导致的开销！所以你需要什么方法就定义什么方法，直接调用就行了!

对于上面的例子，假设你是要调用Singleton里的prt方法，代码如下:

```java
public class Singleton{
       private Singleton(){}

       public static Singleton getInstance(){
           if(single==null){
               synchronized(Singleton.class){
                  if(single==null){
                     single=new Singleton();
                  }
               }
           }
           return single;
       }

       public void prt(){
         System.out.println("This is Singleton");
       }

      private volatile static Singleton single;
}
```

在Clojure里面你可以这么写:

```clojure
(defn prt []
  (println "This is Singleton"))

;调用
(prt)
```

需要模式吗？
