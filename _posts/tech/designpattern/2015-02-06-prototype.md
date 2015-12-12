---
layout: post
title: 语言与模式-04原型模式
categories: [designpattern]
tags: [designpattern,java,clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 意图

用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象。

![]({{site.CDN_PATH}}/assets/designpattern/prototype.jpg)

# 适用性

- 当要实例化的类是在运行时刻指定时，例如，通过动态装载；或者
- 为了避免创建一个与产品类层次平行的工厂类层次时；或者
- 当一个类的实例只能有几个不同状态组合中的一种时。建立相应数目的原型并克隆它们可能比每次用合适的状态手工实例化该类更方便一些。

# Java实现

```java
//浅拷贝
public class A implements Cloneable{

    private String str;
    private B b;

    public void setStr(String str){
        this.str = str;
    }

    public String getStr(){
        return str;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    protected Object clone(){
        A a = null;
        try {
            a = (A) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return a;
    }

    public static void main(String[] args) {
        A a = new A();
        a.setStr("Hello");
        B b = new B();
        b.setStr("Hello B");
        a.setB(b);
        System.out.println("a-str:" + a.getStr());
        System.out.println("a-b:" + a.getB().getStr());
        A ac = null;
        ac = (A) a.clone();
        System.out.println("ac-str:" + ac.getStr());
        System.out.println("ac-b:" + ac.getB().getStr());

        a.setStr("Hello A");
        b.setStr("Hello BB");
        a.setB(b);

        System.out.println("a-str:" + a.getStr());
        System.out.println("a-b:" + a.getB().getStr());
        System.out.println("ac-str:" + ac.getStr());
        System.out.println("ac-b:" + ac.getB().getStr());
    }
}
```

<!-- more -->

```java
public class B{
     private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
```

```sh
//输出结果
a-str:Hello
a-b:Hello B
ac-str:Hello
ac-b:Hello B
a-str:Hello A
a-b:Hello BB
ac-str:Hello
ac-b:Hello BB       --浅拷贝导致了，ac和a中的B是相同的引用，a中的b被修改后，ac中的b也被修改，这应该是不想要的结果
                    --所以需要深拷贝。深拷贝就是将所有涉及到的对象都进行Clone
```

```java
//深拷贝
public class A implements Cloneable{

    private String str;
    private B b;

    public void setStr(String str){
        this.str = str;
    }

    public String getStr(){
        return str;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    protected Object clone(){
        A a = null;
        try {
            a = (A) super.clone();
            a.b = (B) b.clone();       //此处对b进行clone
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return a;
    }

    public static void main(String[] args) {
        A a = new A();
        a.setStr("Hello");
        B b = new B();
        b.setStr("Hello B");
        a.setB(b);
        System.out.println("a-str:" + a.getStr());
        System.out.println("a-b:" + a.getB().getStr());
        A ac = null;
        ac = (A) a.clone();
        System.out.println("ac-str:" + ac.getStr());
        System.out.println("ac-b:" + ac.getB().getStr());

        a.setStr("Hello A");
        b.setStr("Hello BB");
        a.setB(b);

        System.out.println("a-str:" + a.getStr());
        System.out.println("a-b:" + a.getB().getStr());
        System.out.println("ac-str:" + ac.getStr());
        System.out.println("ac-b:" + ac.getB().getStr());
    }
}
```

```java
//B也要实现Cloneable并重写clone方法
public class B implements Cloneable{
     private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

```sh
//输出结果
/*
a-str:Hello
a-b:Hello B
ac-str:Hello
ac-b:Hello B
a-str:Hello A
a-b:Hello BB
ac-str:Hello
ac-b:Hello B        --此处a中的b被修改后，ac中的b不会被影响
*/
```

# Clojure实现

对于Clojure来说，根本就不需要原型模式！

首先请想想为什么Java需要原型模式？

根本原因是Java中的对象是可变的！就像上面的str属性，如果不拷贝，则两个引用指向同一个对象，当其中一个引用改变了str的值，那么另一个引用也会受到影响！

而在Clojure中，所有数据都是不可变的！当你修改了一个数据，原来的数据并没有变化，而是返回了一个新的数据。这样的话，在Clojure中根本就不需要原型模式。
