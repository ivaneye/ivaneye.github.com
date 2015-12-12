---
layout: post
title: 语言与模式-10装饰模式
categories: [designpattern]
tags: [designpattern,java,clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---
# 意图

动态地给一个对象添加一些额外的职责。就增加功能来说，Decorator模式相比生成子类更为灵活。

![]({{site.CDN_PATH}}/assets/designpattern/decorator.jpg)

# 适用性

- 在不影响其他对象的情况下，以动态、透明的方式给单个对象添加职责。
- 处理那些可以撤消的职责。
- 当不能采用生成子类的方法进行扩充时。一种情况是，可能有大量独立的扩展，为支持每一种组合将产生大量的子类，使得子类数目呈爆炸性增长。另一种情况可能是因为类定义被隐藏，或类定义不能用于生成子类。

# Java实现

JavaIO库的使用可能如下

```java
BufferedInputStream bi = new BufferedInputStream(new FileInputStream(filename));
```

我们都知道FileInputStream是用来读取文件的，BufferedInputStream是提供了缓存的能力。我们分别看下他们的源代码

```java
public class BufferedInputStream extends FilterInputStream
...

public class FilterInputStream extends InputStream
...

public class FileInputStream extends InputStream
...
```

BufferedInputStream继承了FilterInputStream，FilterInputStream和FileInputStream一样都继承自InputStream.

可以看出InputStream是公共父类。

FilterInputStream是装饰类的公共父类，看看FilterInputStream的源代码就知道了，他只是做了简单的方法委托。

BufferedInputStream继承了FilterInputStream，并添加了缓存的方法（其实就是用一个字节数组保存字节，一次性读出）。

<!-- more -->

# Clojure实现

笑而不语～
