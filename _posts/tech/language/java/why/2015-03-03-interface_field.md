---
layout: post
title: 刨根问底-02为什么Java接口中的成员变量被设计为必须是public static final的？
categories: [why]
tags: [why,java]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 概述

此为"刨根问底"系列文章的第二篇，接上篇，为什么Java接口中的成员变量被设计为必须是public static final的？

# 网络答案

在网络上搜索到了如下两篇还算靠谱的解答!

[网络解答1](http://shaomeng95.iteye.com/blog/998820)

[网络解答2](http://www.xyzws.com/javafaq/why-do-we-have-only-public-static-final-variables-in-interfaces/129)

# 解释

上篇我们用反证法来证明了接口方法设为public为最合适选择!

这里我们依然可以使用反证法来解释!

假设接口中的成员变量不是public static final的!

先将属性设置为private的!

```java
public interface Person{
  private int num;
}
```

OK!private的属性，当然只能在接口内部使用了!但是在接口里面给谁使用呢？接口里的方法只有方法头可没方法体！所以设为private，那就永远都访问不到了！

好，那我们改成protected!

```java
public interface Person{
  protected int num;
}
```

这样子类就能访问了！但是外部如何访问呢？方法是有的:

<!-- more -->

```java
public interface Person{
  protected int num;

  int getNum();
  void setNum(int num);
}

class PersonA implements Person{
  public int getNum(){
    return num;
  }
  public void setNum(int num){
    this.num = num;
  }
}
```

如果有100个类实现了Person接口！请问有多少重复代码?

接着是默认访问权限控制符:

```java
public interface Person{
  //非public static final
  int num;
}
```

对于包外的类来说，和protected一样的问题!这里不废话了！

最后就只剩下public了！

```java
public interface Person{
  public int num;
}

public class PersonA implements Person{
  public int num = 2;
}

public class Test{
  public static void main(String[] args){
    PersonA pa = new PersonA();
    System.out.println(pa.num); //打印结果是？
    Person p = pa;
    System.out.println(p.num); //打印结果是？
  }
}
```

如上代码你会发现，两者打印结果居然不相同！这就好像是，这个"协议"公开是一套，而如果你有关系，会给你另一套"内部协议"!

究其原因，就是这个成员变量是属于实现了这个接口的对象的，并不属于接口本身。因为你需要先实例化实现了此接口的类才可以操作这个成员变量，而接口本身并不能实例化！

所以如果想这个成员变量为接口所有，只能将其设为static!

```java
public interface Person{
  public static int num;
}

public class PersonA implements Person{}
public class PersonB implements Person{}

public class Test{
  public static void main(String[] args){
    System.out.println(Person.num); //结果？
    PersonA.num = 2;
    System.out.println(Person.num); //结果？
    PersonB.num = 2;
    System.out.println(Person.num); //结果？
  }
}
```

如上代码的意思就是，A和B遵循了"协议"Person，但是只要A或B感觉不满意就可以随便改！那还叫"协议"吗?

所以只能加上final来限制修改!

而对于添加public final的效果，和只添加public的问题相同！不多说！

从上面的修改可以看出，public static final修饰符，缺少任何之一都会导致"协议"的不公正!


你可以把接口看作是合同!成员变量看作合同说明，确定后当然是不能变的!至于如何去执行这个合同，那就随便了！
