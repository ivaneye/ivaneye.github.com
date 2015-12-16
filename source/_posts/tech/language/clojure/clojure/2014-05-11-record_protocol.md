---
layout: post
title: Clojure教程-Record和Protocol
categories: clojure
tags: [clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---


本文翻译自:[Learn Clojure using records and protocols](http://www.jayway.com/2013/02/05/learn-clojure-using-records-and-protocols/)

当我对Clojure的括号不再疑惑后,另一个让我质疑为何要学习Clojure的问题是使用REPL很爽,但是我怎么来构建大型项目?".

实际上,由于我的面向对象编程经验,我其实要问的是"我怎么才能将函数封装到类似class的东西里面去?".

在本文中,我将会介绍一种类似于Java的方式来构建大型的Clojure项目.

通过这种方式,希望你在学习Clojure的时候不会有太大的差异感!

在Java中,我们出于各种目的而使用类.例如典型的使用Spring的web应用,你会看到类似下面的结构:

-   Data transfer objects(DTO)
-   Services(REST API,controllers,DAO)
-   Rich object

DTO实际上就是个结构体,他没有任何的行为(即方法).为了最小化样板代码,我趋向于使用pulibc final属性去实现DTO.我认为DTO就是个模板(schema),它就像一个REST服务输出的文件.但是我发现很多客户端开发人员可不关注这个,而只关注代码形式.有时你会看到DTO被作为数据库访问的一部分.这些DTO被称为贫血模型.(译者注:猜测作者的意思是,有些开发人员不管类是DTO还是DAO,只要结构相同就随便用!)

Service实际上是包含了方法和注入了辅助Service的单例对象.不同的辅助Service提供了不同的服务.除了实现了接口外,Service只是包含了方法,和占位符很类似.单例模式导致的一个问题就是,新手无法预见到多线程里共享相同的实例出现意外的结果.他们将状态保存到私有属性里,而不需要从一个对象传递到另一个对象.很方便,但是是错误的做法.

Rich object是面向对象语言中的思想.即将数据及和数据相关的操作封装到一个类里面.我可没说getter和setter是相关操作!但是,rich object类在项目中用得较少.取而代之的是使用DTO作为Service的输入和输出.使用DAO来访问数据库,然后返回DTO.我没说这种方法是错误的,我好奇的是,既然对于目前的Java架构是好是坏我们都无法确认,那为什么还要强求使用Clojure去实现类似的东西呢?

<!-- more -->

先不管这么多,我们先看怎么在Clojure中做类似的事情?我直接给出如何做!

首先,DTO就是数据,而Clojure擅长数据处理,例如map,list和set.但是如果你想要类似结构体的东西,Clojure里提供了record.如果你了解Scala你会发现这玩样和case class很像.定义record的方式如下:

```clojure
(defrecord Person [firstName lastName])
```

这实际上创建了一个叫Person的Java类,它包含两个不可变的属性以及实现了hashCode和equals方法.record的行为模式和map很像,所以大部分适用于map的方法都适用与record!需要注意的是,虽然Clojure是动态类型,但是你可以使用类型提示来标示特定类型:

```clojure
(defrecord Person [^String firstName ^String lastName])
```

好,那现在我们来看看如何定义Service!让我们将问题分解为组织相关函数,定义接口及依赖注入!

和Java不同,Clojure提供了多种组织相关函数的方法.使用哪种方式完全取决于你想做什么.

首先,在Clojure中,函数不需要定义到一个类里面.取而代之的是,他们通过命名空间类管理,命名空间类似于Java中的包.如果你没有任何特别的要求,那么建议使用命名空间来管理函数.

如果你想定义类似class的东西,你需要先定义接口!那你需要使用protocol!你可以把它当做和Java中的接口类似的东西.实际上,除了使用protocol你也可以直接使用Java的接口,因为Clojure可以直接访问Java代码!创建protocol的方式如下:

```clojure
(defprotocol Greet
    (sayHello [this])
```

这和下面的代码功能相同:

```java
public interface Greet{
    Object sayHello();
}
```

有两点需要注意:

-   你需要将this包含在参数列表中!在面向对象语言中默认都包含了this,在类内部调用时默认包含了this.而在外部调用方法时,是使用对象名称.方法名,比如person.sayHello().而Clojure是函数式的,方法在前,调用方式如下(sayHello person)
-   方法返回的是Object.这是因为Clojure是函数式的,我们努力使其没有副作用.返回值为void的方法,实际上大部分是为了其副作用的!

实现protocol可以使用如下代码:

```clojure
(defrecord Person [firstName lastName]
    Greet
    (sayHello [this] (print "Hello,my name is " firstName)))
```

最后就是依赖注入了!结论是:依赖注入对与Clojure来说不是必要的!可以看看下面两篇文章!

-   [Dependency injection in
    Clojure](http://tech.puredanger.com/2010/03/01/dependency-injection-clojure/)
-   [What is the Clojure equivalent to Google
    Guice?](http://stackoverflow.com/questions/13085370/what-is-the-clojure-equivalent-to-google-guice)

# 总结

从Java过渡到Scala相对来说比较简单,因为你可以在Scala中编写类Java的代码,然后再慢慢过渡到函数式代码编写上.但是对于Clojure来说,没有这个过渡过程,所以比较难于适应!但是通过使用record和protocol可以使你能在Clojure中做类似Java的事情,从而简化你的过渡难度!

个人感受
========

不知道是英文不行,还是作者行文有问题!感觉作者的文字不够流畅!按照原文直译,总感觉不通顺!故做了删减调整!

对于依赖注入提供的两篇文章,评论比文章好得多!特别是第二篇的第一个评论!其实本文就简单介绍了Clojure的record和protocol入门而已!
