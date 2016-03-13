% Docker与服务化
% 王一帆

## 虚拟机

- VMware
- VirtualBox

## 虚拟机的问题

- 速度慢
- 占用资源高
- 发布流程繁琐

## 容器一

- 1979年 — chroot
- 2000年 — FreeBSD Jails
- 2001年 — Linux VServer
- 2004年 — Solaris容器
- 2005年 — OpenVZ
- 2006年 — Process容器
- 2007年 — Control Groups

## 容器二

- 2008年 — LXC
- 2011年 — Warden
- 2013年 — LMCTFY
- 2013年 — Docker
- 2014年 — Rocket
- 2016年 — Windows容器

## 虚拟机与容器

![](file/containers_vms.png)

## 性能对比

|特性	|容器	|虚拟机|
|----|----|----|
|启动	|秒级|分钟级|
|磁盘容量|	一般为 MB|	一般为 GB|
|效能	|接近原生|	比较慢|
|系统支持量	|单机上千个容器	|一般几十个|

## 容器的工作

- 文件系统隔离
- 进程隔离
- 网络隔离
- 资源隔离和分组

## Docker是如何做的?

- libcontainer/lxc
- namespace
- cgroups
- 写时复制
- 日志
- 交互式shell

## Why Docker?

<!-- todo -->

## Docker的历史

Docker项目始于2013年3月，由当时的PaaS服务提供商dotCloud开发，dotClound也是YCombinator S10的毕业生。

2013年10月dotCloud公司名字也由dotCloud, Inc.改为Docker, Inc.，集中更多的精力放到了Docker相关的研发上。

## Docker能做什么?

- 加速本地开发和构建流程，使其更加高效、更加轻量化
- 使独立服务或应用程序在不同的环境中，得到相同的运行结果
- 使用Docker创建隔离的环境来进行测试
- Docker可以让开发者先在本机上构建一个复杂的程序或- 架构来进行测试，而不是一开始就在生产环境部署、测试
- 构建一个多用户的平台即服务(Paas)基础设施
- 为开发、测试提供一个轻量级的独立沙盒环境、或者将独立的沙盒环境用于技术教学
- 提供软件即服务(SaaS)应用程序
- 高性能、超大规模的宿主机部署
- 解决了“在我机器上/内网/本地是好的呀！”

## Docker架构

![](file/docker_architecture.svg)

## Docker组件

- Client
- Daemon
- Image: a read-only template
- Container: a running image
- Registry: place where images are hosted/shared---Docker Hub

## Linux

![](file/linux_docker_host.svg)

## windows

![](file/win_docker_host.svg)

## 安装Docker

[官方文档](https://docs.docker.com/engine/installation/linux/ubuntulinux/)

- Ubuntu apt-get
- Windows [Docker Toolbox](https://www.docker.com/toolbox)

## 系统要求

- kernel >= 3.10
- 64位操作系统
- 内核必须支持一种适合的存储驱动
- 内核必须支持开启cgroup和namespace功能

## 启动/停止Docker

```sh
sudo start docker
sudo stop docker
```

## Docker Info

```sh
sudo docker info
```

##

```
Containers: 2
Images: 22
Server Version: 1.9.1
Storage Driver: aufs
 Root Dir: /var/lib/docker/aufs
 Backing Filesystem: extfs
 Dirs: 26
 Dirperm1 Supported: false
Execution Driver: native-0.2
Logging Driver: json-file
Kernel Version: 3.13.0-24-generic
Operating System: Ubuntu 14.04.1 LTS
CPUs: 4
Total Memory: 5.693 GiB
```

## 运行第一个容器

```sh
sudo docker run -i -t ubuntu /bin/bash
```

![](file/first_run.jpg)

## 做了什么?

https://docs.docker.com/engine/understanding-docker/

## 查看容器

```
sudo docker ps
```

![](file/ps.jpg)

## 安装软件

```
apt-get install vim
```

## 停止容器

## 重启容器

##

## Dockerfile

## 应用



## Docker与Hydra

- Docker作为Hydra的环境
- Docker作为Hydra的容器

## 待整理

## cgroups
cgroups的一个设计目标是为不同的应用情况提供统一的接口，从控制单一进程(像nice)到操作系统层虚拟化(像opeNVZ，Linux-VServer，LXC)。cgroups提供：

资源限制：组可以被设置不超过设定的内存限制；这也包括虚拟内存。[3] 原来的分页机制是在Linux研讨会的Containers: Challenges with the memory resource controller and its performance报告中提出的。[4]
优先化：一些组可能会得到大量的CPU[5] 或磁盘输入输出通量。[6]
报告：用来衡量系统确实把多少资源用到适合的目的上。[7]
分离：为组分离命名空间，这样一个组不会看到另一个组的进程、网络连接和文件。[2]
控制：冻结组或检查点和重启动。[7]
