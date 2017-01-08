---
layout: post
date: 2017-01-08
title: SSO
categories: [private]
tags: [private,ppp]
avatarimg: "/img/head.jpg"
author: wangyifan
published: false

---

# SSO

基础模式SSO访问流程主要有以下步骤：

- 访问服务：SSO客户端发送请求访问应用系统提供的服务资源。
-  定向认证：SSO客户端会重定向用户请求到SSO服务器。
-  用户认证：用户身份认证。
-  发放票据：SSO服务器会产生一个随机的Service Ticket。
-  验证票据：SSO服务器验证票据Service Ticket的合法性，验证通过后，允许客户端访问服务。
-  传输用户信息：SSO服务器验证票据通过后，传输用户认证结果信息给客户端。

下面是CAS 最基本的协议过程：

![](../../../../assets/private/ppp/cas.jpg)
