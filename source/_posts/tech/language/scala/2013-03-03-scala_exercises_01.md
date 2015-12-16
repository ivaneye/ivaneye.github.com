---
layout: post
title: 快学Scala习题解答—第一章 基础
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


基础
====

在Scala REPL中键入3,然后按Tab键。有哪些方法可以被应用?
------------------------------------------------------

这个。。。。直接操作一遍就有结果了.此题不知是翻译的问题，还是原题的问题，在Scala
REPL中需要按3. 然后按Tab才会提示。 直接按3加Tab是没有提示的。下面是结果

```sh
!=             ##             %              &              *              +
-              /              <              <<             <=             ==
>              >=             >>             >>>            ^              asInstanceOf
equals         getClass       hashCode       isInstanceOf   toByte         toChar
toDouble       toFloat        toInt          toLong         toShort        toString
unary_+        unary_-        unary_~        |
```

列出的方法并不全，需要查询全部方法还是需要到Scaladoc中的Int,Double,RichInt,RichDouble等类中去查看。

在Scala REPL中，计算3的平方根,然后再对该值求平方。现在，这个结果与3相差多少？(提示:res变量是你的朋友)
-----------------------------------------------------------------------------------------------------

依次进行计算即可

```sh
scala> scala.math.sqrt(3)
warning: there were 1 deprecation warnings; re-run with -deprecation for details
res5: Double = 1.7320508075688772

scala> res5*res5
res6: Double = 2.9999999999999996

scala> 3 - res6
res7: Double = 4.440892098500626E-16
```

<!-- more -->

res变量是val还是var?
--------------------

val是不可变的，而var是可变的，只需要给res变量重新赋值就可以检测res是val还是var了

```sh
scala> res9 = 3
<console>:8: error: reassignment to val
       res9 = 3
            ^
```

Scala允许你用数字去乘字符串---去REPL中试一下"crazy"\*3。这个操作做什么？在Scaladoc中如何找到这个操作?
-----------------------------------------------------------------------------------------------------

```sh
scala> "crazy"*3
res11: String = crazycrazycrazy
```

从代码可以推断,\*是"crazy"这个字符串所具有的方法，但是Java中的String可没这个方法，很明显。此方法在StringOps中。

10 max 2的含义是什么？max方法定义在哪个类中？
---------------------------------------------

直接在REPL中执行

```sh
scala> 10 max 2
res0: Int = 10

scala> 7 max 8
res1: Int = 8

scala> 0 max 0
res2: Int = 0
```

可以看出,此方法返回两个数字中较大的那个。此方法Java中不存在，所以在RichInt中。

用BigInt计算2的1024次方
-----------------------

简单的API调用

```sh
scala> BigInt(2).pow(1024)
res4: scala.math.BigInt = 17976931348623159077293051907890247336179769789423065727343008115
7732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124
3777678934248654852763022196012460941194530829520850057688381506823424628814739131105408272
37163350510684586298239947245938479716304835356329624224137216
```

为了在使用probablePrime(100,Random)获取随机素数时不在probablePrime和Radom之前使用任何限定符，你需要引入什么？
-------------------------------------------------------------------------------------------------------------

so easy.
import需要的包啊。Random在scala.util中，而probablePrime是BigInt中的方法，引入即可

```scala
import scala.math.BigInt._
import scala.util.Random

probablePrime(3,Random)
```

创建随机文件的方式之一是生成一个随机的BigInt，然后将它转换成三十六进制，输出类似"qsnvbevtomcj38o06kul"这样的字符串。查阅Scaladoc，找到在Scala中实现该逻辑的办法。
-----------------------------------------------------------------------------------------------------------------------------------------------------------------

到BigInt中查找方法。

```sh
scala> scala.math.BigInt(scala.util.Random.nextInt).toString(36)
res21: String = utydx
```

在Scala中如何获取字符串的首字符和尾字符？
-----------------------------------------

```scala
//获取首字符
"Hello"(0)
"Hello".take(1)
//获取尾字符
"Hello".reverse(0)
"Hello".takeRight(1)
```

take,drop,takeRight和dropRight这些字符串函数是做什么用的？和substring相比，他们的优点和缺点都是哪些？
-----------------------------------------------------------------------------------------------------

查询API即可
take是从字符串首开始获取字符串,drop是从字符串首开始去除字符串。
takeRight和dropRight是从字符串尾开始操作。 这四个方法都是单方向的。
如果我想要字符串中间的子字符串，那么需要同时调用drop和dropRight，或者使用substring

