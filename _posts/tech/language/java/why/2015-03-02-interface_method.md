---
layout: post
title: 刨根问底-01为什么Java接口中的方法默认是public？
categories: [why]
tags: [why,java]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 概述

去年担任了公司Java培训班的培训讲师！由于时间关系，课堂上只是讲了How，而很少涉及到Why!

故计划编写此"刨根问底"系列文章，主要解释Java中的Why!

此为该系列文章的第一篇，为什么Java接口中的方法默认是public的？

# 解释

首先我们来看下接口的定义，在《Thinking in Java》中，对接口做了如下定义:

```sh
An interface says, "All classes that implement this particular interface will look like this." Thus, any code that uses a particular interface knows what methods might be called for that interface, and that’s all. So the interface is used to establish a "protocol" between classes. (Some object-oriented programming languages have a keyword called protocol to do the
same thing.)

接口意味着："所有实现了这个特定接口的类都应该可以被当作这个接口来看待"。也就是说，所有使用此特定接口的代码都应该知道该接口有哪些方法可以被调用，而且仅此而已！所以接口被用来定义接口之间的协议！(有些语言使用protocol这个关键字来做Java中interface关键字所要做的事情，例如:Clojure)
```

简单说来，接口就是用来定义一个"协议"的！这么看来，protocol这个关键字应该比interface这个关键字更加的准确！

```java
//Java接口定义
public interface Person{
  void say();
}

//这样会不会更合理？
public protocol Person{
  void say();
}

//Clojure类似代码
(defprotocol Person
  (say [this]))
```

上面的接口就定义了一个"协议",这个协议就是:"所有遵循这个协议(实现此接口)的类，必然有一个叫做say()的方法"。

<!-- more -->

```java
public class PersonA implements Person{
  public void say(){
    System.out.println("Hello PersonA");
  }
}

public class PersonB implements Person{
  public void say(){
    System.out.println("Hello PersonB");
  }
}
```

这里PersonA遵循了这个协议，PersonB也遵循了这个协议。那在我们使用的时候，我们完全就没必要关心是PersonA还是PersonB，我们只关心它是不是一个Person就可以了！

```java
public class Test{
  public static void main(String[] args){
    //不管是PersonA还是PersonB，肯定是个Person
    Person p = new PersonA(); //或 new PersonB();
    //不管是PersonA还是PersonB，肯定有say()方法
    p.say();
  }
}
```

我们知道Java里面有四种访问权限控制符！private,protected,package(default),public!

那为什么接口的方法默认是public的呢？我们使用假设法来一个个的看！

假设接口的方法是private的！

```java
public interface Person{
  private void say();
}
```

private的访问控制是只在类内部可见！这里就相当于说:"所有遵循这个协议(实现此接口)的类，必然有一个方法，至于方法是什么，呵呵，你们猜！"。这是作死的节奏！

假设接口的方法是protected的！

```java
public interface Person{
  protected void say();
}
```

protected的访问控制是只在类或子类内部可见！这里就相当于说:"所有遵循这个协议(实现此接口)的类，必然有一个方法，至于方法是什么，你来当我儿子啊，我再告诉你！"。这个就更是作死了～

最后，假设接口的方法是默认修饰符！


```java
public interface Person{
   //假设这里不会自动加public修饰符
   void say();
}
```

默认访问控制是只在相同的包下的类可见！这里就相当于说:"所有遵循这个协议(实现此接口)的类，必然有一个方法，至于方法是什么，你到我包间来，我再告诉你！"。这个好像还能接受！但是你进去你看，全是人，感觉像传销啊！原因是所有需要和此协议交互的类都必须要在协议所在的包内，导致此包过大，且杂乱！

最后看public修饰:

public访问控制是任何的类都可以访问！

- 不像private,不告诉你方法是什么
- 不像protected，需要继承才告诉你
- 不像默认访问控制符，需要在相同的包下才告诉你

综上所述，接口方法设为public为最合适选择!

