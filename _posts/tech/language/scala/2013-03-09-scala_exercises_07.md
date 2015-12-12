---
layout: post
title: 快学Scala习题解答—第七章 包和引入
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


包和引入
========

编写示例程序，展示为什么
------------------------

```scala
package com.horstmann.impatient
```

不同于

```scala
package com
package horstmann
package impatient
```

分别使用package的效果如下

```scala
package com {
  class T1() {}

  package horstmann {
    class T2(t: T1) {}

    package impatient {
      class T3(t1: T1, t2: T2) {}
    }
  }
}
```

子包里的类可以使用父包里的类。但是第一种方式不可以

```scala
package com.horstmann.impatient{
  class T4(t1:T1,t3:T3)      //无法找到T1
}
```

<!-- more -->

编写一段让你的Scala朋友们感到困惑的代码，使用一个不在顶部的com包
----------------------------------------------------------------

这样可以吗？

```scala
package com {
  class T1() {}

  package horstmann {
    class T2(t: T1) {}

    package impatient {
      class T3(t1: T1, t2: T2) {}
    }
  }
}

import com._

class TT(t1:T1){

}
```

编写一个包random,加入函数nextInt():Int,nextDouble():Double,setSeed(seed:Int):Unit。生成随机数的算法采用线性同余生成器:
----------------------------------------------------------------------------------------------------------------------

```sh
后值 = (前值 * a + b)mod 2^n
其中,a = 1664525,b=1013904223,n = 32,前值的初始值为seed
```

```scala
package random{
package object random{

  var seed:Int = _
  val a = BigDecimal(1664525)
  val b = BigDecimal(1013904223)
  val n = 32


  def nextInt():Int={
    val temp = (seed * a + b) % BigDecimal(2).pow(n)
    seed = temp.toInt
    seed
  }

  def nextDouble():Double={
    val temp = (seed * a + b) % BigDecimal(2).pow(n)
    seed = temp.toInt
    temp.toDouble
  }
}
}

package test {
import random.random

object Test extends App {
  random.seed = 4
  println(random.nextDouble())
  println(random.nextDouble())
  println(random.nextDouble())
  println(random.nextDouble())
  println(random.nextInt())
  println(random.nextInt())
  println(random.nextInt())
  println(random.nextInt())
}

}
```

在你看来Scala的设计者为什么要提供package object语法而不是简单的让你将函数和变量添加到包中呢？
---------------------------------------------------------------------------------------------

JVM不支持。。。

private[com] def giveRaise(rate:Double)的含义是什么？有用吗？
-------------------------------------------------------------

除了com包可访问，其他包都不能访问。

编写一段程序,将Java哈希映射中的所有元素拷贝到Scala哈希映射。用引入语句重命名这两个类。
--------------------------------------------------------------------------------------

```scala
import java.util.{HashMap => JavaHashMap}
import scala.collection.mutable.HashMap

object Test extends App{

  val map = new JavaHashMap[String,String]()
  map.put("1","a")
  map.put("2","b")
  map.put("3","c")

  val smap = new HashMap[String,String]()

  for(key <- map.keySet().toArray){
    smap += (key.toString -> map.get(key))
  }

  println(smap.mkString)
}
```

在前一个练习中，将所有引入语句移动到尽可能小的作用域里
------------------------------------------------------

```scala
object Test extends App{

  import java.util.{HashMap => JavaHashMap}

  val map = new JavaHashMap[String,String]()
  map.put("1","a")
  map.put("2","b")
  map.put("3","c")

  import scala.collection.mutable.HashMap

  val smap = new HashMap[String,String]()

  for(key <- map.keySet().toArray){
    smap += (key.toString -> map.get(key))
  }

  println(smap.mkString)
}
```

以下代码的作用是什么？这是个好主意吗？
--------------------------------------

```scala
import java._
import javax._
```

导入java和javax下的所有类。而java和javax下是没有类的。所以此代码无用

编写一段程序，引入java.lang.System类，从user.name系统属性读取用户名，从Console对象读取一个密码,如果密码不是"secret"，则在标准错误流中打印一个消息；如果密码是"secret"，则在标准输出流中打印一个问候消息。不要使用任何其他引入，也不要使用任何限定词(带句点的那种)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
import java.lang.System

object Test extends App{
  var password = Console.readLine()

  if (password equals "secret") System.out.println("Hello " + System.getProperty("user.name"))
  else System.err.println("password error!")
}
```

除了StringBuilder,还有哪些java.lang的成员是被scala包覆盖的？
------------------------------------------------------------

直接比对java.lang下的类和scala包下的类即可

