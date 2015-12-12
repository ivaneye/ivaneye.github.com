---
layout: post
title: Docker初探
categories: [docker]
tags: [docker]
avatarimg: "/img/head.jpg"
author: Ivan
---

# Docker的用途
在程序的生命周期中，主要有开发，测试，发布环节。每个环节都需要部署相应的环境。

简单来说，Java使得程序能够一次编译到处运行，但是需要部署的每台机器都要部署jdk，server等，而每次部署都会是个耗时的过程，可能jdk版本不同，机器系统不同等导致部署环境不一致，进而导致无法发布应用的情况。

Docker的作用就是将整个应用连带部署环境一起打包。就是说开发时配置好的环境，可以一起打包，拷贝到测试，发布机器上直接去发布即可。

# Docker的安装

windows下从下面的连接下载安装包!

https://github.com/boot2docker/windows-installer/releases

下载最新版本的：docker-install.exe即可。一路next，没什么好说的。

# 启动Docker

直接双击Boot2Docker Start即可启动。启动后会将c:/Users/{username}目录挂载过去，可以作为本地与docker的文件交互目录

# SSH连接
可以通过SSH工具连接到Docker上进行操作！在Docker界面上通过
```
boot2docker ip
```
可以获取Docker的IP。默认的用户名和密码是： docker/tcuser

<!-- more -->

# 下载镜像

通过如下地址下载:ubuntu-14.04-x86_64.tar.gz

http://download.openvz.org/template/precreated

# 拷贝镜像到Docker

将镜像文件拷贝到c:/Users/{username}目录下，即可在SSH中使用mv或者copy命令移动到Docker的任意目录下，例如要移动到/home下
```
mv /c/Users/{username}/ubuntu-14.04-x86_64.tar.gz /home
```

# 安装镜像

命令：cat ubuntu-14.04-x86_64.tar.gz |docker import - ubuntu:ubuntu14
速度非常快，大概10几秒就完成了。

# 查看镜像

查看： docker images

# 运行镜像

运行：docker run -i -t ubuntu:ubuntu14 /bin/bash

这相当于启动了一个Ubuntu，你可以通过命令下载文件，进行环境的配置，比如安装JDK等。

# 生成新镜像

- 查看当前容器
```
docker ps -a
```

- 生成新镜像
```
docker commit {containerId} new-image
```

- 导出镜像
```
docker save new-image > /tmp/new-image.tar
```

此镜像就可以拷贝到其他机器上进行安装，安装后就是个包含了完整jdk环境的Ubuntu14，当然你可以把应用也打包进去，那就直接部署这个镜像就完成了应用的发布了。
