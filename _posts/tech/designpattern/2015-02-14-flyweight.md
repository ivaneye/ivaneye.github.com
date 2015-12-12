---
layout: post
title: 语言与模式-12享元模式
categories: [designpattern]
tags: [designpattern,java,clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---
# 意图

运用共享技术有效地支持大量细粒度的对象。

![]({{site.CDN_PATH}}/assets/designpattern/flyweight.jpg)

# 适用性

- 一个应用程序使用了大量的对象。
- 完全由于使用大量的对象，造成很大的存储开销。
- 对象的大多数状态都可变为外部状态。
- 如果删除对象的外部状态，那么可以用相对较少的共享对象取代很多组对象。
- 应用程序不依赖于对象标识。由于Flyweight对象可以被共享，对于概念上明显有别的对象，标识测试将返回真值。

# Java实现

抽象享元角色类

```java
public interface Flyweight {
    //一个示意性方法，参数state是外蕴状态
    public void operation(String state);
}
```

```java
public class ConcreteFlyweight implements Flyweight {
    private Character intrinsicState = null;
    /**
     * 构造函数，内蕴状态作为参数传入
     * @param state
     */
    public ConcreteFlyweight(Character state){
        this.intrinsicState = state;
    }


    /**
     * 外蕴状态作为参数传入方法中，改变方法的行为，
     * 但是并不改变对象的内蕴状态。
     */
    @Override
    public void operation(String state) {
        // TODO Auto-generated method stub
        System.out.println("Intrinsic State = " + this.intrinsicState);
        System.out.println("Extrinsic State = " + state);
    }

}
```

<!-- more -->

```java
public class FlyweightFactory {
    private Map<Character,Flyweight> files = new HashMap<Character,Flyweight>();

    public Flyweight factory(Character state){
        //先从缓存中查找对象
        Flyweight fly = files.get(state);
        if(fly == null){
            //如果对象不存在则创建一个新的Flyweight对象
            fly = new ConcreteFlyweight(state);
            //把这个新的Flyweight对象添加到缓存中
            files.put(state, fly);
        }
        return fly;
    }
}
```

调用

```java
public class Client {

    public static void main(String[] args) {
        FlyweightFactory factory = new FlyweightFactory();
        Flyweight fly = factory.factory(new Character('a'));
        fly.operation("First Call");

        fly = factory.factory(new Character('b'));
        fly.operation("Second Call");

        fly = factory.factory(new Character('a'));
        fly.operation("Third Call");
    }

}
```

# Clojure实现

Clojure就不需要这种模式！其所有数据都是唯一的！
