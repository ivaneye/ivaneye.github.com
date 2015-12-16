---
layout: post
title: Clojure Web栈
categories: clojure
tags: [clojure,framework,mycode]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 简介
Clojure是动态语言，刚开始使用Clojure进行web开发时，可能都会想着Clojure有没有rails实现呢？毕竟Rails开发web还是很方便的。[Conjure](https://github.com/macourtney/Conjure)就是类Rails框架，不过已经三年无人维护了。

目前Clojure Web开发基本都是基于[ring](https://github.com/ring-clojure/ring)扩展的!这和Linux的设计有点类似,ring就是Clojure Web的核心，提供:

- 解析Request,Response的功能
- 处理功能Handler
- 拦截功能Middleware

而其它额外功能均在其上进行扩展。

本文主要介绍各个Clojure Web类库，通过这些类库，你能构建Clojure Web程序。

先从核心ring开始!

# ring

## Handler
## Request
## Response
## Middleware