---
layout: post
title: 语言与模式-03抽象工厂模式
categories: [designpattern]
tags: [designpattern,java,clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 意图

提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。

![]({{site.CDN_PATH}}/assets/designpattern/abstract_factory.jpg)

# 适用性

- 一个系统要独立于它的产品的创建、组合和表示时。
- 一个系统要由多个产品系列中的一个来配置时。
- 当你要强调一系列相关的产品对象的设计以便进行联合使用时。
- 当你提供一个产品类库，而只想显示它们的接口而不是实现时。

# Java实现

表现在代码层面就是一个工厂可以返回多个不同的实例！

比如:Pig不仅分颜色，还分国家，比如荷兰Pig，新西兰Pig。

```java
public class RedPigFactory implements PigFactory{
     public Pig getNetherlandsPig(){
          return new RedNetherlandsPig();
     }

     public Pig getNewZealandPig(){
          return new RedNewZealandPig();
     }
}
```

<!-- more -->


# Clojure实现

而在Clojure中就是选择函数的条件多了！

```clojure
(defmulti run (fn [t] t))

(defmethod run
  [:RedPig :Netherlands]
  [t]
  (println "Netherlands RedPig run"))

(defmethod run
  [:GreenPig :Netherlands]
  [t]
  (println "Netherlands GreenPig run"))

(defmethod run
  [:BluePig :Netherlands]
  [t]
  (println "Netherlands BluePig run"))

(defmethod run
  [:RedPig :NewZealand]
  [t]
  (println "NewZealand RedPig run"))

(defmethod run
  [:GreenPig :NewZealand]
  [t]
  (println "NewZealand GreenPig run"))

(defmethod run
  [:BluePig :NewZealand]
  [t]
  (println "NewZealand BluePig run"))

(run [:BluePig :Netherlands])
```
