---
layout: post
title: 快学Scala习题解答—第二章 控制结构和函数
categories: scala
tags: [java,scala]
author: Ivan

---



控制结构和函数
==============

一个数字如果为正数，则它的signum为1;如果是负数,则signum为-1;如果为0,则signum为0.编写一个函数来计算这个值
--------------------------------------------------------------------------------------------------------

简单的逻辑判断

```scala
def signum(num:Int){if(num>0)print(1)else if(num<0)print(-1)else print(0)}
```

Scala中已经有此方法了，刚才查找API的时候，应该能看到

```scala
BigInt(10).signum
```

一个空的快表达式{}的值是什么？类型是什么？
------------------------------------------

在REPL中就能看出来了

```sh
scala> val t = {}
t: Unit = ()
```

可以看出，它的值是()类型是Unit

指出在Scala中何种情况下赋值语句x=y=1是合法的。(提示：给x找个合适的类型定义)
---------------------------------------------------------------------------

题目已经给了明确的提示了。本章节中已经说过了，在scala中的赋值语句是Unit类型。所以只要x为Unit类型就可以了。

```sh
scala> var y=4;
y: Int = 4

scala> var x={}
x: Unit = ()

scala> x=y=7
x: Unit = ()
```

这也再次证明了{}是Unit类型

<!-- more -->

针对下列Java循环编写一个Scala版本:for(int i=10;i\>=0;i--)System.out.println(i);
-------------------------------------------------------------------------------

使用Scala版本改写就OK了

```scala
for(i <- 0 to 10 reverse)print(i)
```

编写一个过程countdown(n:Int)，打印从n到0的数字
----------------------------------------------

这个就是将上面的循环包装到过程中而已。还是换个写法吧。

```scala
def countdown(n:Int){
    0 to n reverse foreach print
}
```

编写一个for循环,计算字符串中所有字母的Unicode代码的乘积。举例来说，"Hello"中所有字符串的乘积为9415087488L
---------------------------------------------------------------------------------------------------------

```sh
scala> var t:Long = 1
t: Long = 1

scala> for(i <- "Hello"){
     | t = t * i.toLong
     | }

scala> t
res57: Long = 9415087488
```

同样是解决前一个练习的问题，但这次不使用循环。（提示：在Scaladoc中查看StringOps）
---------------------------------------------------------------------------------

```sh
scala> var t:Long = 1
t: Long = 1

scala> "Hello".foreach(t *= _.toLong)

scala> t
res59: Long = 9415087488
```

编写一个函数product(s:String)，计算前面练习中提到的乘积
-------------------------------------------------------

```scala
def product(s:String):Long={
    var t:Long = 1
    for(i <- s){
    t *= i.toLong
    }
    t
}
```

把前一个练习中的函数改成递归函数
--------------------------------

配合前一章的take和drop来实现

```scala
def product(s:String):Long={
    if(s.length == 1) return s.charAt(0).toLong
    else s.take(1).charAt(0).toLong * product(s.drop(1))
}
```

编写函数计算x^n^,其中n是整数，使用如下的递归定义:
-------------------------------------------------

-   x^n^=y^2^,如果n是正偶数的话，这里的y=x^(n/2)^
-   x^n^ = x\*x^(n-1)^,如果n是正奇数的话
-   x^0^ = 1
-   x^n^ = 1/x^(-n)^,如果n是负数的话

不得使用return语句

```scala
def mi(x:Double,n:Int):Double={
    if(n == 0) 1
    else if (n > 0 && n%2 == 0) mi(x,n/2) * mi(x,n/2)
    else if(n>0 && n%2 == 1) x * mi(x,n-1)
    else 1/mi(x,-n)
}
```
