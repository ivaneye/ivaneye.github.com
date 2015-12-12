---
layout: post
title: Clojure教程-环境搭建与Hello World
categories: clojure
tags: [clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---


环境搭建
========

Clojure是JVM上的一种语言，其语法是Lisp格式，所以称为是JVM上的Lisp方言。

Clojure依赖于JDK。请确保你的机器上安装了JDK6+。Clojure的安装非常的简单。
可以分为两种方式:

-   通过Leiningen安装
-   手动安装

通过Leiningen是比较普遍也很方便的方式。我们先通过Leiningen来安装。

通过Leiningen安装
-----------------

Leiningen和Maven比较类似，是Clojure语言的管理工具。其实它就是基于Maven
构建的。我们先来安装Leiningen

-   首先下载[lein
    script](https://raw.github.com/technomancy/leiningen/stable/bin/lein)
-   将lein script放置到你的path下(linux下直接放到用户目录下即可)，保存文
    件名叫lein。
-   将其权限设置为可执行(chmod a+x lein)
-   打开shell，输入lein即可

leiningen会自动下载需要的依赖。运行完成后,Clojure开发环境即安装完成。

手动安装
--------

leiningen需要以项目为单位才能运行。比如你想运行一个hello.clj程序，如果
你通过leiningen来运行，则hello.clj需要在lein项目内，且设置为core，才能
运行,比较麻烦(这个问题在Maven中同样存在)。

而通过手动安装可以解决这个问题。

-   首先下载[Clojure](http://repo1.maven.org/maven2/org/clojure/clojure/1.5.1/clojure-1.5.1.zip),
    目前稳定版本是1.5.1
-   解压到任意位置，其中最主要的文件就是clojure-1.5.1.jar
-   在path下编写脚本clj,输入如下内容

```sh
java -jar /home/ivan/soft/clojure-1.5.1/clojure-1.5.1.jar $1
 #clojure.jar请输入你本机的路径
```

这样就可以执行单个的Clojure文件了

<!-- more -->

HelloWorld
==========

REPL
----

老掉牙的入门程序。如果你想体验Clojure。那么只需要在Shell中运行

```sh
lein repl
```

则会运行一个Clojure的REPL环境，你可以直接输入Clojure代码，REPL直接反馈结果。

试试输入如下代码

```clojure
(println "Hello World")
```

结果如图

![]({{site.CDN_PATH}}/assets/clojure/repl.png)

运行单个文件
------------

如果你想将Clojure代码保存到文件中去运行，那么将上面的代码保存到.clj结尾的文件中，并在shell中运行如下命令即可

```sh
clj hello.clj
```

编辑器
------

Clojure编辑器主要还是文本编辑器，比如emacs,vim等,大部分的文本编辑器都有Clojure语言的支持。

IDE的话，Intellij IDEA有Clojure环境，不过速度太慢了。一般编写使用文本编辑器即可。
这里使用Emacs的clojure-mode作为开发环境。

界面如图

![]({{site.CDN_PATH}}/assets/clojure/emacs.png)

另外还有lighttable，详细介绍可见[这里](http://lighttable.com/)

相关文章见[这里]({% post_url /tech/language/clojure/lighttable/2014-11-28-pigtheme %})

