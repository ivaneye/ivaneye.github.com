---
layout: post
title: Java工具集
categories: jvm
tags: [java,cmd,tool]
avatarimg: "/img/head.jpg"
author: Ivan
published: false

---


# 简介

JDK自带了很多工具，本文逐个进行梳理!

# javac
# java
# javap
# jar
# jps

[官方文档](http://docs.oracle.com/javase/8/docs/technotes/tools/windows/jps.html#CHDCGECD)

jps是用来查询java进程的，和Linux下的ps功能类似，不过只查java的进程。

使用jps时，如果没有指定hostid，它只会显示本地环境中所有的Java进程；如果指定了hostid，它就会显示指定hostid上面的java进程，不过这需要远程服务上开启了jstatd服务.

## 命令格式

jps [options] [hostid]

## 常用参数说明

- -q 忽略输出的类名、Jar名以及传递给main方法的参数，只输出pid。
- -m 输出传递给main方法的参数，如果是内嵌的JVM则输出为null。
- -l 输出应用程序主类的完整包名，或者是应用程序JAR文件的完整路径。
- -v 输出传给JVM的参数。
- -V 不输出类名，jar文件名和传递给main方法的参数，只显示JVM标识
- -J 用于传递参数给jvm。例如:-J-Xms48m,设置启动jvm最小内存为48m

## 服务器标识

hostid指定了目标的服务器，它的语法如下：

```
[protocol:][[//]hostname][:port][/servername]
```
- protocol - 如果protocol及hostname都没有指定，那表示的是与当前环境相关的本地协议，如果指定了hostname却没有指定protocol，那么protocol的默认就是rmi。
- hostname - 服务器的IP或者名称，没有指定则表示本机。
- port - 远程rmi的端口，如果没有指定则默认为1099。
- servername - 注册到RMI注册中心中的jstatd的名称。

## 输出格式

```
lvmid [ [ classname | JARfilename | "Unknown"] [ arg* ] [ jvmarg* ] ]
```

# jstat
# jstatd