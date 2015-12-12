---
layout: post
title: 快学Scala习题解答—第十章 特质
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


特质
====

java.awt.Rectangle类有两个很有用的方法translate和grow,但可惜的是像java.awt.geom.Ellipse2D这样的类没有。在Scala中，你可以解决掉这个问题。定义一个RenctangleLike特质,加入具体的translate和grow方法。提供任何你需要用来实现的抽象方法,以便你可以像如下代码这样混入该特质:
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
val egg = new java.awt.geom.Ellipse2D.Double(5,10,20,30) with RectangleLike
egg.translate(10,-10)
egg.grow(10,20)
```

使用自身类型使得trait可以操作x,y

```scala
import java.awt.geom.Ellipse2D


trait RectangleLike{
  this:Ellipse2D.Double=>
  def translate(x:Double,y:Double){
    this.x = x
    this.y = y
  }
  def grow(x:Double,y:Double){
    this.x += x
    this.y += y
  }
}

object Test extends App{
  val egg = new Ellipse2D.Double(5,10,20,30) with RectangleLike
  println("x = " + egg.getX + " y = " + egg.getY)
  egg.translate(10,-10)
  println("x = " + egg.getX + " y = " + egg.getY)
  egg.grow(10,20)
  println("x = " + egg.getX + " y = " + egg.getY)
}
```

<!-- more -->

通过把scala.math.Ordered[Point]混入java.awt.Point的方式，定义OrderedPoint类。按辞典编辑方式排序，也就是说，如果x\<x'或者x=x'且y\<y'则(x,y)\<(x',y')
---------------------------------------------------------------------------------------------------------------------------------------------------

```scala
import java.awt.Point

class OrderedPoint extends Point with Ordered[Point]{
  def compare(that: Point): Int = if (this.x <= that.x && this.y < that.y) -1
                                   else if(this.x == that.x && this.y == that.y) 0
                                   else 1
}
```

查看BitSet类,将它的所有超类和特质绘制成一张图。忽略类型参数([...]中的所有内容)。然后给出该特质的线性化规格说明
--------------------------------------------------------------------------------------------------------------

这个略

提供一个CryptoLogger类，将日志消息以凯撒密码加密。缺省情况下密匙为3，不过使用者也可以重写它。提供缺省密匙和-3作为密匙是的使用示例
---------------------------------------------------------------------------------------------------------------------------------

```scala
trait Logger{
  def log(str:String,key:Int = 3):String
}

class CryptoLogger extends Logger{

  def log(str: String, key:Int): String = {
    for ( i <- str) yield if (key >= 0) (97 + ((i - 97 + key)%26)).toChar else (97 + ((i - 97 + 26 + key)%26)).toChar
  }
}

object Test extends App{
    val plain = "chenzhen";
    println("明文为：" + plain);
    println("加密后为：" + new CryptoLogger().log(plain));
    println("加密后为：" + new CryptoLogger().log(plain,-3));
}
```

JavaBean规范里有一种提法叫做属性变更监听器(property change listener)，这是bean用来通知其属性变更的标准方式。PropertyChangeSupport类对于任何想要支持属性变更通知其属性变更监听器的bean而言是个便捷的超类。但可惜已有其他超类的类---比如JComponent---必须重新实现相应的方法。将PropertyChangeSupport重新实现为一个特质,然后将它混入到java.awt.Point类中
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
import java.awt.Point
import java.beans.PropertyChangeSupport

trait PropertyChange extends PropertyChangeSupport

val p = new Point() with PropertyChange
```

在Java AWT类库中,我们有一个Container类，一个可以用于各种组件的Component子类。举例来说,Button是一个Component,但Panel是Container。这是一个运转中的组合模式。Swing有JComponent和JContainer,但如果你仔细看的话，你会发现一些奇怪的细节。尽管把其他组件添加到比如JButton中毫无意义,JComponent依然扩展自Container。Swing的设计者们理想情况下应该会更倾向于图10-4中的设计。但在Java中那是不可能的。请解释这是为什么？Scala中如何用特质来设计出这样的效果?
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

![]({{site.CDN_PATH}}file:scala/01.jpg)
Java只能单继承,JContainer不能同时继承自Container和JComponent。Scala可以通过特质解决这个问题.

市面上有不下数十种关于Scala特质的教程,用的都是些"在叫的狗"啦，"讲哲学的青蛙"啦之类的傻乎乎的例子。阅读和理解这些机巧的继承层级很乏味且对于理解问题没什么帮助,但自己设计一套继承层级就不同了,会很有启发。做一个你自己的关于特质的继承层级，要求体现出叠加在一起的特质,具体的和抽象的方法，以及具体的和抽象的字段
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala


trait Fly{
  def fly(){
    println("flying")
  }

  def flywithnowing()
}

trait Walk{
  def walk(){
    println("walk")
  }
}

class Bird{
  var name:String = _
}

class BlueBird extends Bird with Fly with Walk{
  def flywithnowing() {
    println("BlueBird flywithnowing")
  }
}

object Test extends App{
  val b = new BlueBird()
  b.walk()
  b.flywithnowing()
  b.fly()
}
```

在java.io类库中，你可以通过BufferedInputStream修饰器来给输入流增加缓冲机制。用特质来重新实现缓冲。简单起见，重写read方法
------------------------------------------------------------------------------------------------------------------------

后续JavaIO详细讨论

使用本章的日志生成器特质,给前一个练习中的方案增加日志功能，要求体现缓冲的效果
-----------------------------------------------------------------------------

同上

实现一个IterableInputStream类，扩展java.io.InputStream并混入Iterable[Byte]特质
------------------------------------------------------------------------------

```scala
import java.io.InputStream

class IterableInputStream extends InputStream with Iterable[Byte]{
  def read(): Int = 0

  def iterator: Iterator[Byte] = null
}
```
