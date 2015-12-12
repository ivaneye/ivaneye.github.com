---
layout: post
title: 快学Scala习题解答—第六章 对象
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


对象
====

编写一个Conversions对象，加入inchesToCentimeters,gallonsToLiters和milesToKilometers方法
---------------------------------------------------------------------------------------

```scala
object Conversions{
    def inchesToCentimeters(){}
    def gallonsToLiters(){}
    def milesToKilometers(){}
}
```

前一个练习不是很面向对象。提供一个通用的超类UnitConversion并定义扩展该超类的InchesToCentimeters,GallonsToLiters和MilesToKilometers对象
--------------------------------------------------------------------------------------------------------------------------------------

```scala
abstract class UnitConversion{

  def inchesToCentimeters(){}
  def gallonsToLiters(){}
  def milesToKilometers(){}

}

object InchesToCentimeters extends UnitConversion{
  override def inchesToCentimeters() {}
}

object GallonsToLiters extends UnitConversion{
  override def gallonsToLiters() {}
}

object MilesToKilometers extends UnitConversion{
  override def milesToKilometers() {}
}
```

<!-- more -->

定义一个扩展自java.awt.Point的Origin对象。为什么说这实际上不是个好主意？(仔细看Point类的方法)
---------------------------------------------------------------------------------------------

Point中的getLocation方法返回的是Point对象，如果想返回Origin对象，需要Origin类才行

```scala
object Origin extends Point with App{

  override def getLocation: Point = super.getLocation

  Origin.move(2,3)
  println(Origin.toString)

}
```

定义一个Point类和一个伴生对象,使得我们可以不用new而直接用Point(3,4)来构造Point实例
----------------------------------------------------------------------------------

apply方法的使用

```scala
class Point(x:Int,y:Int){
  override def toString: String = "x = " + x + " y = " + y
}

object Point extends App{
  def apply(x:Int,y:Int)={
    new Point(x,y)
  }

  val p = Point(1,2)
  println(p)
}
```

编写一个Scala应用程序,使用App特质,以反序打印命令行参数,用空格隔开。举例来说,scala Reverse Hello World应该打印World Hello
------------------------------------------------------------------------------------------------------------------------

```scala
object Reverse extends App{
  args.reverse.foreach(arg => print(arg  + " "))
}
```

编写一个扑克牌4种花色的枚举,让其toString方法分别返回♣,@\<span style="color:red"\>♦@\</span\>,@\<span style="color:red"\>♥@\</span\>,♠
-------------------------------------------------------------------------------------------------------------------------------------

```scala
object Card extends Enumeration with App{
  val M = Value("♣")
  val T = Value("♠")
  val H = Value("♥")
  val F = Value("♦")

  println(Card.M)
  println(Card.T)
  println(Card.H)
  println(Card.F)
}
```

实现一个函数,检查某张牌的花色是否为红色
---------------------------------------

```scala
object Card extends Enumeration with App{
  val M = Value("♣")
  val T = Value("♠")
  val H = Value("♥")
  val F = Value("♦")

  def color(c:Card.Value){
    if(c == Card.M || c == Card.T) print("Black")
    else print("Red")
  }

  color(Card.H)
}
```

编写一个枚举,描述RGB立方体的8个角。ID使用颜色值(例如:红色是0xff0000)
--------------------------------------------------------------------

```scala
object RGB extends Enumeration with App{
  val RED = Value(0xff0000,"Red")
  val BLACK = Value(0x000000,"Black")
  val GREEN = Value(0x00ff00,"Green")
  val CYAN = Value(0x00ffff,"Cyan")
  val YELLOW = Value(0xffff00,"Yellow")
  val WHITE = Value(0xffffff,"White")
  val BLUE = Value(0x0000ff,"Blue")
  val MAGENTA = Value(0xff00ff,"Magenta")
}
```
