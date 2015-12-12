---
layout: post
title: 快学Scala习题解答—第十四章 模式匹配和样例类
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


模式匹配和样例类
================

JDK发行包有一个src.zip文件包含了JDK的大多数源代码。解压并搜索样例标签(用正则表达式case [\^:]+:)。然后查找以//开头并包含[Ff]alls?thr的注释，捕获类似// Falls through或// just fall thru这样的注释。假定JDK的程序员们遵守Java编码习惯，在该写注释的地方写下了这些注释，有多少百分比的样例是会掉入到下一个分支的？
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

没读懂题意。。。。

利用模式匹配，编写一个swap函数，接受一个整数的对偶，返回对偶的两个组成部件互换位置的新对偶
------------------------------------------------------------------------------------------

```scala
def swap[S,T](tup: (S,T)) = {
    tup match {
        case (a ,b) => (b,a)
    }
}

println(swap[String,Int](("1",2)))
```

利用模式匹配，编写一个swap函数，交换数组中的前两个元素的位置，前提条件是数组长度至少为2
---------------------------------------------------------------------------------------

```scala
def swap(arr: Array[String]) = {
  arr match {
    case Array(a,b, ar @ _*) => Array(b,a) ++ ar
    case _ => arr
  }
}

println(swap(Array("1","2","3","4")).mkString)
```

<!-- more -->

添加一个样例类Multiple，作为Item的子类。举例来说，Multiple(10,Article("Blackwell Toster",29.95))描述的是10个烤面包机。当然了，你应该可以在第二个参数的位置接受任何Item，无论是Bundle还是另一个Multiple。扩展price函数以应对新的样例。
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
abstract class Item

case class Multiple(num : Int,item : Item) extends Item

case class Article(description : String , price : Double) extends Item
case class Bundle(description : String , discount : Double , item : Item*) extends Item

object Test extends App{

  def price(it : Item) : Double = it match {
      case Article(_,p) => p
      case Bundle(_,disc,its @ _*) => its.map(price _).sum - disc
      case Multiple(n,it) => n * price(it)
  }

  val p = price(Multiple(10,Article("Blackwell Toster",29.95)))
  println(p)
}
```

我们可以用列表制作只在叶子节点存放值的树。举例来说，列表((3 8) 2 (5))描述的是如下这样一棵树:
--------------------------------------------------------------------------------------------

```sh
      *
    / | \
   *  2  *
 /  \    |
3   8    5
```

不过，有些列表元素是数字，而另一些是列表。在Scala中，你不能拥有异构的列表，因此你必须使用List[Any]。编写一个leafSum函数，计算所有叶子节点中的元素之和，用模式匹配来区分数字和列表。

```scala
val l: List[Any] = List(List(3, 8), 2, List(5))

def leafSum(list: List[Any]): Int = {

  var total = 0

  list.foreach {
    lst =>
      lst match {
        case l: List[Any] => total += leafSum(l)
        case i: Int => total += i
      }
  }
  total
}

println(leafSum(l))
```

制作这样的树更好的做法是使用样例类。我们不妨从二叉树开始。
----------------------------------------------------------

```scala
sealed abstract class BinaryTree
case class Leaf(value : Int) extends BinaryTree
case class Node(left : BinaryTree,right : BinaryTree) extends BinaryTree
```

编写一个函数计算所有叶子节点中的元素之和。

```scala
sealed abstract class BinaryTree
case class Leaf(value : Int) extends BinaryTree
case class Node(left : BinaryTree,right : BinaryTree) extends BinaryTree

val r = Node(Leaf(3),Node(Leaf(3),Leaf(9)))

def leafSum(tree: BinaryTree): Int = {

      tree match {
        case Node(a,b) => leafSum(a) + leafSum(b)
        case Leaf(v) => v
      }

}

println(leafSum(r))
```

扩展前一个练习中的树，使得每个节点可以有任意多的后代，并重新实现leafSum函数。第五题中的树应该能够通过下述代码表示：
-------------------------------------------------------------------------------------------------------------------

```scala
Node(Node(Leaf(3),Leaf(8)),Leaf(2),Node(Leaf(5)))
```

```scala
sealed abstract class BinaryTree
case class Leaf(value: Int) extends BinaryTree
case class Node(tr: BinaryTree*) extends BinaryTree

object Test extends App {

  val r = Node(Node(Leaf(3), Leaf(8)), Leaf(2), Node(Leaf(5)))

  def leafSum(tree: BinaryTree): Int = {
    tree match {
      case Node(r @ _*) => r.map(leafSum).sum
      case Leaf(v) => v
    }
  }

  println(leafSum(r))

}
```

扩展前一个练习中的树，使得每个非叶子节点除了后代之外，能够存放一个操作符。然后编写一个eval函数来计算它的值。举例来说：
----------------------------------------------------------------------------------------------------------------------

```sh
      +
    / | \
   *  2  -
 /  \    |
3   8    5
```

上面这棵树的值为(3 \* 8) + 2 + (-5) = 21

```scala
sealed abstract class BinaryTree
case class Leaf(value: Int) extends BinaryTree
case class Node(ch : Char , tr: BinaryTree*) extends BinaryTree

object Test extends App {

  val r = Node('+' , Node('*',Leaf(3), Leaf(8)), Leaf(2), Node('-' , Leaf(5)))


  def eval(tree: BinaryTree): Int = {
    tree match {
      case Node(c : Char , r @ _*) => if( c == '+') r.map(eval).sum else if (c == '*') r.map(eval).reduceLeft(_ * _) else r.map(eval).foldLeft(0)(_ - _)
      case Leaf(v) => v
    }
  }

println(eval(r))

}
```

编写一个函数，计算List[Option[Int]]中所有非None值之和。不得使用match语句。
--------------------------------------------------------------------------

```scala
val l : List[Option[Int]] = List(Option(-1),None,Option(2))
println(l.map(_.getOrElse(0)).sum)
```

编写一个函数，将两个类型为Double=\>Option[Double]的函数组合在一起，产生另一个同样类型的函数。如果其中一个函数返回None，则组合函数也应返回None。例如：
-----------------------------------------------------------------------------------------------------------------------------------------------------

```scala
def f(x : Double) = if ( x >= 0) Some(sqrt(x)) else None
def g(x : Double) = if ( x != 1) Some( 1 / ( x - 1)) else None
val h = compose(f,g)
```

h(2)将得到Some(1)，而h(1)和h(0)将得到None

```scala
import scala.math.sqrt

def f(x : Double) = if ( x >= 0) Some(sqrt(x)) else None
def g(x : Double) = if ( x != 1) Some( 1 / ( x - 1)) else None
val h = compose(f,g)

def compose(f : (Double => Option[Double]), g : (Double => Option[Double])):(Double => Option[Double])={
  (x : Double) =>
    if (f(x) == None || g(x) == None) None
    else g(x)
}

println(h(2))
```
