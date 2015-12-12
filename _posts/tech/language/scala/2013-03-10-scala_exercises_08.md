---
layout: post
title: 快学Scala习题解答—第八章 继承
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


继承
====

扩展如下的BankAccount类，新类CheckingAccount对每次存款和取款都收取1美元的手续费
-------------------------------------------------------------------------------

```scala
class BankAccount(initialBalance:Double){
    private var balance = initialBalance
    def deposit(amount:Double) = { balance += amount; balance}
    def withdraw(amount:Double) = {balance -= amount; balance}
}
```

继承语法的使用。代码如下

```scala
class CheckingAccount(initialBalance:Double) extends BankAccount(initialBalance){
  override def deposit(amount: Double): Double = super.deposit(amount - 1)

  override def withdraw(amount: Double): Double = super.withdraw(amount + 1)
}
```

扩展前一个练习的BankAccount类，新类SavingsAccount每个月都有利息产生(earnMonthlyInterest方法被调用)，并且有每月三次免手续费的存款或取款。在earnMonthlyInterest方法中重置交易计数。
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
class SavingsAccount(initialBalance:Double) extends BankAccount(initialBalance){
  private var num:Int = _

  def earnMonthlyInterest()={
    num = 3
    super.deposit(1)
  }

  override def deposit(amount: Double): Double = {
    num -= 1
    if(num < 0) super.deposit(amount - 1) else super.deposit(amount)
  }

  override def withdraw(amount: Double): Double = {
    num -= 1
    if (num < 0) super.withdraw(amount + 1) else super.withdraw(amount)
  }
}
```

<!-- more -->

翻开你喜欢的Java或C++教科书，一定会找到用来讲解继承层级的实例，可能是员工，宠物，图形或类似的东西。用Scala来实现这个示例。
--------------------------------------------------------------------------------------------------------------------------

Thinking in Java中的代码

```java
class Art{
    Art(){System.out.println("Art constructor");}
}

class Drawing extends Art{
    Drawing() {System.out.println("Drawing constructor");}
}

public class Cartoon extends Drawing{
    public Cartoon() { System.out.println("Cartoon constructor");}
}
```

使用Scala改写如下

```scala
class Art{
  println("Art constructor")
}

class Drawing extends Art{
  println("Drawing constructor")
}

class Cartoon extends Drawing{
  println("Cartoon constructor")
}
```

定义一个抽象类Item,加入方法price和description。SimpleItem是一个在构造器中给出价格和描述的物件。利用val可以重写def这个事实。Bundle是一个可以包含其他物件的物件。其价格是打包中所有物件的价格之和。同时提供一个将物件添加到打包当中的机制，以及一个适合的description方法
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
import collection.mutable.ArrayBuffer


abstract class Item{
  def price():Double
  def description():String

  override def toString():String={
    "description:" + description() + "  price:" + price()
  }
}

class SimpleItem(val price:Double,val description:String) extends Item{

}

class Bundle extends Item{

  val items = new ArrayBuffer[Item]()

  def addItem(item:Item){
    items += item
  }

  def price(): Double = {
    var total = 0d
    items.foreach(total += _.price())
    total
  }

  def description(): String = {
    items.mkString(" ")
  }
}

```

设计一个Point类，其x和y坐标可以通过构造器提供。提供一个子类LabeledPoint，其构造器接受一个标签值和x,y坐标,比如:new LabeledPoint("Black Thursday",1929,230.07)
------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
class Point(x:Int,y:Int){
}

class LabeledPoint(label:String,x:Int,y:Int) extends Point(x,y){
}
```

定义一个抽象类Shape，一个抽象方法centerPoint，以及该抽象类的子类Rectangle和Circle。为子类提供合适的构造器，并重写centerPoint方法
--------------------------------------------------------------------------------------------------------------------------------

```scala
abstract class Shape{
  def centerPoint()
}

class Rectangle(startX:Int,startY:Int,endX:Int,endY:Int) extends Shape{
  def centerPoint() {}
}

class Circle(x:Int,y:Int,radius:Double) extends Shape{
  def centerPoint() {}
}
```

提供一个Square类，扩展自java.awt.Rectangle并且是三个构造器：一个以给定的端点和宽度构造正方形，一个以(0,0)为端点和给定的宽度构造正方形，一个以(0,0)为端点,0为宽度构造正方形
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
import java.awt.{Point, Rectangle}


class Square(point:Point,width:Int) extends Rectangle(point.x,point.y,width,width){

  def this(){
    this(new Point(0,0),0)
  }

  def this(width:Int){
    this(new Point(0,0),width)
  }
}
```

编译8.6节中的Person和SecretAgent类并使用javap分析类文件。总共有多少name的getter方法？它们分别取什么值？(提示：可以使用-c和-private选项)
---------------------------------------------------------------------------------------------------------------------------------------

总共两个。Person中取得的是传入的name,而SecretAgent中取得的是默认的"secret"

在8.10节的Creature类中，将val range替换成一个def。如果你在Ant子类中也用def的话会有什么效果？如果在子类中使用val又会有什么效果？为什么？
---------------------------------------------------------------------------------------------------------------------------------------

在Ant中使用def没有问题。但是如果使用val则无法编译。因为val只能重写不带参数的def。这里的def是带参数的

文件scala/collection/immutable/Stack.scala包含如下定义:
-------------------------------------------------------

```scala
class Stack[A] protected (protected val elems: List[A])
```

请解释protected关键字的含义。(提示：回顾我们在第5章中关于私有构造器的讨论)
此构造方法只能被其子类来调用,而不能被外界直接调用

