---
layout: post
title: 快学Scala习题解答—第四章 映射和元组
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


映射和元组
==========

设置一个映射,其中包含你想要的一些装备，以及它们的价格。然后构建另一个映射，采用同一组键，但是价格上打9折
--------------------------------------------------------------------------------------------------------

映射的简单操作

```sh
scala> val map = Map("book"->10,"gun"->18,"ipad"->1000)
map: scala.collection.immutable.Map[java.lang.String,Int] = Map(book -> 10, gun -> 18, ipad -> 1000)

scala> for((k,v) <- map) yield (k,v * 0.9)
res3: scala.collection.immutable.Map[java.lang.String,Double] = Map(book -> 9.0, gun -> 16.2, ipad -> 900.0)
```

编写一段程序，从文件中读取单词。用一个可变映射来清点每个单词出现的频率。读取这些单词的操作可以使用java.util.Scanner:
--------------------------------------------------------------------------------------------------------------------

val in = new java.util.Scanner(new java.io.File("myfile.txt"))
while(in.hasNext()) 处理 in.next() 或者翻到第9章看看更Scala的做法。
最后，打印出所有单词和它们出现的次数。
@\<p\>当然使用Scala的方法啦。参考第9章@\</p\>
首先，创建一个文件myfile.txt。输入如下内容

```sh
test test ttt test ttt t test sss s
```

Scala代码如下

```scala
import scala.io.Source
import scala.collection.mutable.HashMap

//val source = Source.fromFile("myfile.txt")
//val tokens = source.mkString.split("\\s+")  //此写法tokens为空，不知为何

val source = Source.fromFile("myfile.txt").mkString

val tokens = source.split("\\s+")

val map = new HashMap[String,Int]

for(key <- tokens){
    map(key) = map.getOrElse(key,0) + 1
}

println(map.mkString(","))
```

<!-- more -->

重复前一个练习，这次用不可变的映射
----------------------------------

不可变映射与可变映射的区别就是，每次添加元素，都会返回一个新的映射

```scala
import scala.io.Source

val source = Source.fromFile("myfile.txt").mkString

val tokens = source.split("\\s+")

var map = Map[String,Int]()

for(key <- tokens){
  map += (key -> (map.getOrElse(key,0) + 1))
}

println(map.mkString(","))
```

重复前一个练习，这次使用已排序的映射，以便单词可以按顺序打印出来
----------------------------------------------------------------

和上面的代码没有什么区别，只是将映射修改为SortedMap

```scala
import scala.io.Source
import scala.collection.SortedMap

val source = Source.fromFile("myfile.txt").mkString

val tokens = source.split("\\s+")

var map = SortedMap[String,Int]()

for(key <- tokens){
  map += (key -> (map.getOrElse(key,0) + 1))
}

println(map.mkString(","))
```

重复前一个练习，这次使用java.util.TreeMap并使之适用于Scala API
--------------------------------------------------------------

主要涉及java与scala的转换类的使用

```scala
import scala.io.Source
import scala.collection.mutable.Map
import scala.collection.JavaConversions.mapAsScalaMap
import java.util.TreeMap

val source = Source.fromFile("myfile.txt").mkString

val tokens = source.split("\\s+")

val map:Map[String,Int] = new TreeMap[String,Int]

for(key <- tokens){
  map(key) = map.getOrElse(key,0) + 1
}

println(map.mkString(","))
```

定义一个链式哈希映射,将"Monday"映射到java.util.Calendar.MONDAY,依次类推加入其他日期。展示元素是以插入的顺序被访问的
-------------------------------------------------------------------------------------------------------------------

LinkedHashMap的使用

```scala
import scala.collection.mutable.LinkedHashMap
import java.util.Calendar

val map = new LinkedHashMap[String,Int]

map += ("Monday"->Calendar.MONDAY)
map += ("Tuesday"->Calendar.TUESDAY)
map += ("Wednesday"->Calendar.WEDNESDAY)
map += ("Thursday"->Calendar.THURSDAY)
map += ("Friday"->Calendar.FRIDAY)
map += ("Saturday"->Calendar.SATURDAY)
map += ("Sunday"->Calendar.SUNDAY)


println(map.mkString(","))
```

打印出所有Java系统属性的表格，
------------------------------

属性转scala map的使用

```scala
import scala.collection.JavaConversions.propertiesAsScalaMap

val props:scala.collection.Map[String,String] = System.getProperties()

val keys = props.keySet

val keyLengths = for( key <- keys ) yield key.length

val maxKeyLength = keyLengths.max

for(key <- keys) {
  print(key)
  print(" " * (maxKeyLength - key.length))
  print(" | ")
  println(props(key))
}
```

编写一个函数minmax(values:Array[Int]),返回数组中最小值和最大值的对偶
--------------------------------------------------------------------

```scala
def minmax(values:Array[Int])={
  (values.max,values.min)
}
```

编写一个函数Iteqgt(values:Array[int],v:Int),返回数组中小于v,等于v和大于v的数量，要求三个值一起返回
--------------------------------------------------------------------------------------------------

```scala
def iteqgt(values:Array[Int],v:Int)={
  val buf = values.toBuffer
  (values.count(_ < v),values.count(_ == v),values.count(_ > v))
}
```

当你将两个字符串拉链在一起，比如"Hello".zip("World")，会是什么结果？想出一个讲得通的用例
----------------------------------------------------------------------------------------

```sh
scala> "Hello".zip("World")
res0: scala.collection.immutable.IndexedSeq[(Char, Char)] = Vector((H,W), (e,o), (l,r), (l,l), (o,d))
```

StringOps中的zip定义如下

```sh
abstract def zip[B](that: GenIterable[B]): StringOps[(A, B)]
```

GenIterable是可遍历对象需要包含的trait，对于String来说，它是可遍历的。但是它的遍历是遍历单个字母。
所以拉链就针对每个字母来进行。

