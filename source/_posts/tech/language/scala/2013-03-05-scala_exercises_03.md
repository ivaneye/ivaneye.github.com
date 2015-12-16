---
layout: post
title: 快学Scala习题解答—第三章 数组相关操作
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


数组相关操作
============

编写一段代码，将a设置为一个n个随机整数的数组，要求随机数介于0(包含)和n(不包含)之间
----------------------------------------------------------------------------------

random和yield的使用

```scala
import scala.math.random

def randomArray(n:Int)={
  for(i <- 0 until n) yield (random * n).toInt
}

println(randomArray(10).mkString(","))
```

编写一个循环，将整数数组中相邻的元素置换。例如,Array(1,2,3,4,5)经过置换后变为Array(2,1,4,3,5)
---------------------------------------------------------------------------------------------

对数组方法的使用

```scala
def reorderArray(arr:Array[Int]):Array[Int]={
  val t = arr.toBuffer
  for(i <- 1 until (t.length,2);tmp = t(i);j <- i - 1 until i){
    t(i) = t(j)
    t(j) = tmp
  }
  t.toArray
}

println(reorderArray(Array(1,2,3,4,5)).mkString(","))
```

<!-- more -->

重复前一个练习，不过这一次生成一个新的值交换过的数组。用for/yield
-----------------------------------------------------------------

```scala
def reorderArray(arr:Array[Int]):Array[Int]={
  (for(i <- 0 until (arr.length,2)) yield if (i + 1 < arr.length) Array(arr(i + 1),arr(i)) else Array(arr(i))).flatten.toArray
}

println(reorderArray(Array(1,2,3,4,5)).mkString(","))
```

感觉代码有点丑也难读。需要优化。

给定一个整数数组，产生一个新的数组，包含元数组中的所有正值，以原有顺序排列，之后的元素是所有零或负值，以原有顺序排列
--------------------------------------------------------------------------------------------------------------------

```scala
//使用循环
 def reorderArray(arr:Array[Int]):Array[Int]={
     val a = ArrayBuffer[Int]()
     val b = ArrayBuffer[Int]()
     arr.foreach(arg => if(arg > 0) a += arg else b += arg)
     a ++= b
     a.toArray
 }
//使用filter
def reorderArray(arr:Array[Int]):Array[Int]={
  val a = arr.filter(_ > 0).map(1 * _)
  val b = arr.filter(_ <= 0).map(1 * _)
  val c = a.toBuffer
  c ++= b
  c.toArray
}
```

如何计算Array[Double]的平均值？
-------------------------------

```scala
def aveArray(arr:Array[Double]):Double={
  arr.sum/arr.length
}
```

如何重新组织Array[Int]的元素将他们以反序排列？对于ArrayBuffer[Int]你又会怎么做呢？
----------------------------------------------------------------------------------

```scala
def reverseArray(arr:Array[Int]):Array[Int]={
  arr.reverse
}
```

编写一段代码，产出数组中的所有值，去掉重复项。(提示：查看Scaladoc)
------------------------------------------------------------------

产出数组的代码就不编写了。去重只需要调用api即可

```scala
 def distinctArray(arr:Array[Int]):Array[Int]={
   val t = arr.toBuffer
   t.distinct.toArray
 }
```

重新编写3.4节结尾的示例。收集负值元素的下标，反序，去掉最后一个下标，然后对每个下标调用a.remove(i)。比较这样做的效率和3.4节中另外两种方法的效率
-----------------------------------------------------------------------------------------------------------------------------------------------

性能嘛，自己比较吧!

```scala
 def removeArray(arr:Array[Int]):Array[Int]={
   val t = arr.toBuffer
   val idx = ArrayBuffer[Int]()
   for(i <- 0 until t.length){
     if(t(i) < 0)idx += i
   }
   idx.remove(0)
   idx.reverse
   idx.foreach(t.remove(_))
   t.toArray
 }
```

创建一个由java.util.TimeZone.getAvailableIDs返回ide时区集合，判断条件是它们在美洲。去掉"America/"前缀并排序
-----------------------------------------------------------------------------------------------------------

显示的都是中文时间，后续的过滤无法操作。只列出所有的时区

```scala
 val t = for(i <- getAvailableIDs) yield
           getTimeZone(i).getDisplayName()
```

引入java.awt.datatransfer.~并构建一个类型为SystemFlavorMap类型的对象~:
----------------------------------------------------------------------

val flavors =
SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]
然后以DataFlavor.imageFlavor为参数调用getNativesForFlavor方法，以Scala缓冲保存返回值。
(为什么用这样一个晦涩难懂的类？因为在Java标准库中很难找到使用java.util.List的代码)

```scala
val flavors = SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]
println(flavors.getNativesForFlavor(DataFlavor.imageFlavor).toArray.toBuffer.mkString(" | ")
```
