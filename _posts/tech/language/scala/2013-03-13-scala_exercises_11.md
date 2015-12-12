---
layout: post
title: 快学Scala习题解答—第十一章 操作符
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


操作符
======

根据优先级规则,3 + 4 -\> 5和3 -\> 4 + 5是如何被求值的？
-------------------------------------------------------

在REPL中执行即可得到结果。都是从左至右执行

BigInt类有一个pow方法,但没有用操作符字符。Scala类库的设计者为什么没有选用\*\*(像Fortran那样)或者\^(像Pascal那样)作为乘方操作符呢？
----------------------------------------------------------------------------------------------------------------------------------

Scala中的操作符就是方法，其优先级是根据首字母来判断的，优先级如下

```sh
最高优先级:除以下字符外的操作符字符
 * / %
+ -
:
= !
< >
&
ˆ
|
非操作符
最低优先级:赋值操作符
```

一般乘方的操作符是优于乘法操作的，如果使用\*\*作为乘方的话，那么其优先级则与\*相同，而如果使用\^*的*话，则优先级低于\*操作。优先级都是有问题的。故没有使用这两种操作符


<!-- more -->

实现Fraction类，支持+~\*~/操作。支持约分，例如将15/-6变为-5/2。除以最大公约数,像这样:
-------------------------------------------------------------------------------------

```scala
class Fraction(n:Int,d:Int){
    private val num:Int = if(d==0) 1 else n * sign(d)/gcd(n,d);
    private val den:Int = if(d==0) 0 else d * sign(d)/gcd(n,d);
    override def toString = num + "/" + den
    def sign(a:Int) = if(a > 0) 1 else if (a < 0) -1 else 0
    def gcd(a:Int,b:Int):Int = if(b==0) abs(a) else gcd(b,a%b)
    ...
}
```

```scala
import scala.math.abs

class Fraction(n: Int, d: Int) {
  private val num: Int = if (d == 0) 1 else n * sign(d) / gcd(n, d);
  private val den: Int = if (d == 0) 0 else d * sign(d) / gcd(n, d);

  override def toString = num + "/" + den

  def sign(a: Int) = if (a > 0) 1 else if (a < 0) -1 else 0

  def gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)

  def +(other:Fraction):Fraction={
    newFrac((this.num * other.den) + (other.num * this.den),this.den * other.den)
  }

  def -(other:Fraction):Fraction={
    newFrac((this.num * other.den) - (other.num * this.den),this.den * other.den)
  }

  def *(other:Fraction):Fraction={
    newFrac(this.num * other.num,this.den * other.den)
  }

  def /(other:Fraction):Fraction={
    newFrac(this.num * other.den,this.den * other.num)
  }

  private def newFrac(a:Int,b:Int):Fraction={
    val x:Int = if (b == 0) 1 else a * sign(b) / gcd(a, b);
    val y:Int = if (b == 0) 0 else b * sign(b) / gcd(a, b);
    new Fraction(x,y)
  }
}

object Test extends App{
  val f = new Fraction(15,-6)
  val p = new Fraction(20,60)
  println(f)
  println(p)
  println(f + p)
  println(f - p)
  println(f * p)
  println(f / p)
}
```

实现一个Money类,加入美元和美分字段。提供+,-操作符以及比较操作符==和\<。举例来说，Money(1,75)+Money(0,50)==Money(2,25)应为true。你应该同时提供\*和/操作符吗？为什么？
--------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala

class Money(val dollar:BigInt,val cent:BigInt){

  def +(other:Money):Money={
    val (a,b) = (this.cent + other.cent) /% 100
    new Money(this.dollar + other.dollar + a,b)
  }

  def -(other:Money):Money={
    val (d,c) = (this.toCent() - other.toCent()) /% 100
    new Money(d,c)
  }

  private def toCent()={
    this.dollar * 100 + this.cent
  }

  def ==(other:Money):Boolean = this.dollar == other.dollar && this.cent == other.cent

  def <(other:Money):Boolean = this.dollar < other.dollar || (this.dollar == other.dollar && this.cent < other.cent)

  override def toString = "dollar = " + dollar + " cent = " + cent
}

object Money{
  def apply(dollar:Int,cent:Int):Money={
    new Money(dollar,cent)
  }

  def main(args:Array[String]){

    val m1 = Money(1,200)
    val m2 = Money(2,2)
    println(m1 + m2)
    println(m1 - m2)
    println(m1 == m2)
    println(m1 < m2)
    println(Money(1,75)+Money(0,50))
    println(Money(1,75)+Money(0,50)==Money(2,25))

  }
}
```

不需要提供\*和/操作。对于金额来说没有乘除操作

提供操作符用于构造HTML表格。例如:Table() | "Java" | "Scala" || "Gosling" | "Odersky" || "JVM" | "JVM,.NET"应产出:\<table\>\<tr\>\<td\>Java\</td\>\</tr\>\<td\>Scala\</td\>\</tr\>\<tr\>\<td\>Gosling...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
class Table{

  var s:String = ""

  def |(str:String):Table={
    val t = Table()
    t.s = this.s + "<td>" + str + "</td>"
    t
  }

  def ||(str:String):Table={
    val t = Table()
    t.s = this.s + "</tr><tr><td>" + str + "</td>"
    t
  }

  override def toString():String={
    "<table><tr>" + this.s + "</tr></table>"
  }
}

object Table{

  def apply():Table={
    new Table()
  }

  def main(args: Array[String]) {
    println(Table() | "Java" | "Scala" || "Gosling" | "Odersky" || "JVM" | "JVM,.NET")
  }
}
```

提供一个ASCIIArt类，其对象包含类似这样的图形:
---------------------------------------------

```sh
 /\_/\
( ' ' )
(  -  )
 | | |
(__|__)
提供将两个ASCIIArt图形横向或纵向结合的操作符。选用适当优先级的操作符命名。纵向结合的实例
 /\_/\     -----
( ' ' )  / Hello \
(  -  ) <  Scala |
 | | |   \ Coder /
(__|__)    -----
```

```scala
import collection.mutable.ArrayBuffer

class ASCIIArt(str:String){
  val arr:ArrayBuffer[ArrayBuffer[String]] = new ArrayBuffer[ArrayBuffer[String]]()

  if (str != null && !str.trim.eq("")){
    str.split("[\r\n]+").foreach{
      line =>
      val s = new ArrayBuffer[String]()
      s += line
      arr += s
    }
  }

  def this(){
    this("")
  }

  def +(other:ASCIIArt):ASCIIArt={
    val art = new ASCIIArt()
    val length = if (this.arr.length >= other.arr.length) this.arr.length else other.arr.length
    for(i <- 0 until length){
      val s = new ArrayBuffer[String]()
      val thisArr:ArrayBuffer[String] = if (i < this.arr.length) this.arr(i) else new ArrayBuffer[String]()
      val otherArr:ArrayBuffer[String] = if (i < other.arr.length) other.arr(i) else new ArrayBuffer[String]()
      thisArr.foreach(s += _)
      otherArr.foreach(s += _)
      art.arr += s
    }
    art
  }

  def *(other:ASCIIArt):ASCIIArt={
    val art = new ASCIIArt()
    this.arr.foreach(art.arr += _)
    other.arr.foreach(art.arr += _)
    art
  }

  override def toString()={
    var ss:String = ""
    arr.foreach{
      ss += _.mkString(" ") + "\n"
    }
    ss
  }
}

object Test extends App{
  val a = new ASCIIArt(""" /\_/\
                         |( ' ' )
                         |(  -  )
                         | | | |
                         |(__|__)
                         |""".stripMargin)
  val b = new ASCIIArt( """    -----
                          |  / Hello \
                          | <  Scala |
                          |  \ Coder /
                          |    -----
                          |""".stripMargin)
  println(a + b * b)
  println((a + b) * b)
  println(a * b)
}
```

实现一个BigSequence类,将64个bit的序列打包在一个Long值中。提供apply和update操作来获取和设置某个具体的bit
-------------------------------------------------------------------------------------------------------

```scala
class BigSequence{
  var num = new Array[Int](64)

  for (i <- 0 until num.length){
    num(i) = -1
  }

  def pack():Long={
    num.filter(_ >= 0).mkString.toLong
  }
}

object BigSequence{

  def apply(num:Int):BigSequence={
    val b = new BigSequence
    var i = 0
    num.toString.foreach{
      n=>
      b.num(i) = n.getNumericValue
      i+=1
    }
    b
  }

  def main(args: Array[String]) {
    val b = BigSequence(10100)
    println(b.pack())
  }
}
```

提供一个Matrix类---你可以选择需要的是一个2\*2的矩阵，任意大小的正方形矩阵，或m\*n的矩阵。支持+和\*操作。\*操作应同样适用于单值，例如mat\*2。单个元素可以通过mat(row,col)得到
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
class Matrix(val x:Int,val y:Int){

  def +(other:Matrix):Matrix={
    Matrix(this.x + other.x,this.y + other.y)
  }

  def +(other:Int):Matrix={
    Matrix(this.x + other,this.y + other)
  }

  def *(other:Matrix):Matrix={
    Matrix(this.x * other.x,this.y * other.y)
  }

  def *(other:Int):Matrix={
    Matrix(this.x * other,this.y * other)
  }

  override def toString()={
    var str = ""
    for(i <- 1 to x){
      for(j <- 1 to y){
        str += "*"
      }
      str += "\n"
    }
    str
  }
}

object Matrix{
  def apply(x:Int,y:Int):Matrix= new Matrix(x,y)

  def main(args: Array[String]) {
    val m = Matrix(2,2)
    val n = Matrix(3,4)
    println(m)
    println(n)
    println(m + n)
    println()
    println(m * n)
    println()
    println(m + 2)
    println()
    println(n * 2)
    println()
  }
}
```

为RichFile类定义unapply操作，提取文件路径，名称和扩展名。举例来说，文件/home/cay/readme.txt的路径为/home/cay,名称为readme,扩展名为txt
-------------------------------------------------------------------------------------------------------------------------------------

```scala
class RichFile(val path:String){}

object RichFile{
  def apply(path:String):RichFile={
    new RichFile(path)
  }

  def unapply(richFile:RichFile) = {
    if(richFile.path == null){
      None
    } else {
      val reg = "([/\\w+]+)/(\\w+)\\.(\\w+)".r
      val reg(r1,r2,r3) = richFile.path
      Some((r1,r2,r3))
    }
  }

  def main(args: Array[String]) {
    val richFile = RichFile("/home/cay/readme.txt")
    val RichFile(r1,r2,r3) = richFile
    println(r1)
    println(r2)
    println(r3)
  }
}
```

为RichFile类定义一个unapplySeq，提取所有路径段。举例来说，对于/home/cay/readme.txt，你应该产出三个路径段的序列:home,cay和readme.txt
-----------------------------------------------------------------------------------------------------------------------------------

```scala
class RichFile(val path:String){}

object RichFile{
  def apply(path:String):RichFile={
    new RichFile(path)
  }

  def unapplySeq(richFile:RichFile):Option[Seq[String]]={
    if(richFile.path == null){
      None
    } else {
      Some(richFile.path.split("/"))
    }
  }

  def main(args: Array[String]) {
    val richFile = RichFile("/home/cay/readme.txt")
    val RichFile(r @ _*) = richFile
    println(r)
  }
}
```
