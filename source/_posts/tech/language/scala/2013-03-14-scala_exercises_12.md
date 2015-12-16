---
layout: post
title: 快学Scala习题解答—第十二章 高阶函数
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


高阶函数
========

编写函数values(fun:(Int)=\>Int,low:Int,high:Int),该函数输出一个集合，对应给定区间内给定函数的输入和输出。比如，values(x=\>x\*x,-5,5)应该产出一个对偶的集合(-5,25),(-4,16),(-3,9),...,(5,25)
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
object Test extends App {
  def values(fun: (Int) => Int, low: Int, high: Int) = {
    var arr = List[(Int,Int)]()
    low to high foreach {
      num =>
      arr = (num, fun(num)) :: arr
    }
    arr
  }

  println(values(x => x * x, -5, 5).mkString)
}
```

如何用reduceLeft得到数组中的最大元素?
-------------------------------------

```scala
object Test extends App {
  val arr = Array(3,2,6,8,4,6,9,3,6,7,1,2)
  print(arr.reduceLeft((a,b)=>if (a>b) a else b))
}
```

用to和reduceLeft实现阶乘函数,不得使用循环或递归
-----------------------------------------------

```scala
println(1 to 10 reduceLeft(_ * _))
```

前一个实现需要处理一个特殊情况，即n\<1的情况。展示如何用foldLeft来避免这个需要。
--------------------------------------------------------------------------------

```scala
println((1 to -10).foldLeft(1)(_ * _))
```

<!-- more -->


编写函数largest(fun:(Int)=\>Int,inputs:Seq[Int]),输出在给定输入序列中给定函数的最大值。举例来说，largest(x=\>10\*x-x\*x,1 to 10)应该返回25.不得使用循环或递归
-------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
object Test extends App {

  def largest(fun:(Int)=>Int,inputs:Seq[Int])={
    val s = inputs.reduceLeft((a,b)=>if (fun(a) > fun(b)) a else b)
    fun(s)
  }

  println(largest(x=>10*x-x*x,1 to 10))
}
```

修改前一个函数，返回最大的输出对应的输入。举例来说,largestAt(fun:(Int)=\>Int,inputs:Seq[Int])应该返回5。不得使用循环或递归
--------------------------------------------------------------------------------------------------------------------------

```scala
object Test extends App {

  def largest(fun:(Int)=>Int,inputs:Seq[Int])={
    inputs.reduceLeft((a,b)=>if (fun(a) > fun(b)) a else b)
  }

  println(largest(x=>10*x-x*x,1 to 10))
}
```

要得到一个序列的对偶很容易，比如:
---------------------------------

```scala
val pairs = (1 to 10) zip (11 to 20)
```

假定你想要对这个序列做某中操作---比如，给对偶中的值求和，但是你不能直接使用:

```scala
pairs.map(_ + _)
```

函数\_ + \_
接受两个Int作为参数，而不是(Int,Int)对偶。编写函数adjustToPair,该函数接受一个类型为(Int,Int)=\>Int的函数作为参数，并返回一个等效的,
可以以对偶作为参数的函数。举例来说就是:adjustToPair(\_ \*
\_)((6,7))应得到42。然后用这个函数通过map计算出各个对偶的元素之和

```scala
object Test extends App {

  var list = List[Int]()

  def adjustToPair(fun:(Int,Int)=>Int)(tup:(Int, Int))={
     list = fun(tup._1,tup._2) :: list
     this
  }

  def map(fun:(Int,Int)=>Int):Int={
    list.reduceLeft(fun)
  }

  val pairs = (1 to 10) zip (11 to 20)
  for ((a,b) <- pairs){
    adjustToPair(_ * _)((a,b))
  }
  println(map(_ + _))
}
```

在12.8节中，你看到了用于两组字符串数组的corresponds方法。做出一个对该方法的调用，让它帮我们判断某个字符串数组里的所有元素的长度是否和某个给定的整数数组相对应
-------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
object Test extends App {

  val a = Array("asd","df","aadc")
  val b = Array(3,2,4)
  val c = Array(3,2,1)

  println(a.corresponds(b)(_.length == _))
  println(a.corresponds(c)(_.length == _))
}
```

不使用柯里化实现corresponds。然后尝试从前一个练习的代码来调用。你遇到了什么问题？
---------------------------------------------------------------------------------

没有柯里化则不能使用前一个练习里的代码方式来调用

实现一个unless控制抽象，工作机制类似if,但条件是反过来的。第一个参数需要是换名调用的参数吗？你需要柯里化吗？
-----------------------------------------------------------------------------------------------------------

```scala
object Test extends App {

  def unless(condition: =>Boolean)(block: =>Unit){
    if (!condition){
      block
    }
  }

  var x = 10
  unless(x == 0){
    x -= 1
    println(x)
  }
}
```

需要换名和柯里化

