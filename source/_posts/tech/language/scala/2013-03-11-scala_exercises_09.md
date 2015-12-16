---
layout: post
title: 快学Scala习题解答—第九章 文件和正则表达式
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---

文件和正则表达式
================

编写一小段Scala代码，将某个文件中的行倒转顺序(将最后一行作为第一行,依此类推)
----------------------------------------------------------------------------

```scala
import io.Source
import java.io.PrintWriter

val path = "test.txt"

val reader = Source.fromFile(path).getLines()

val result = reader.toArray.reverse

val pw = new PrintWriter(path)

result.foreach(line => pw.write(line + "\n"))

pw.close()
```

编写Scala程序,从一个带有制表符的文件读取内容,将每个制表符替换成一组空格,使得制表符隔开的n列仍然保持纵向对齐,并将结果写入同一个文件
----------------------------------------------------------------------------------------------------------------------------------

```scala
import io.Source
import java.io.PrintWriter

val path = "test.txt"

val reader = Source.fromFile(path).getLines()

val result = for ( t <- reader) yield t.replaceAll("\\t","    ")

val pw = new PrintWriter(path)

result.foreach(line => pw.write(line + "\n"))

pw.close()
```

<!-- more -->

编写一小段Scala代码,从一个文件读取内容并把所有字符数大于12的单词打印到控制台。如果你能用单行代码完成会有额外奖励
----------------------------------------------------------------------------------------------------------------

```scala
import io.Source

Source.fromFile("test.txt").mkString.split("\\s+").foreach(arg => if(arg.length > 12) println(arg))
```

编写Scala程序，从包含浮点数的文本文件读取内容，打印出文件中所有浮点数之和，平均值，最大值和最小值
-------------------------------------------------------------------------------------------------

```scala
import io.Source

val nums = Source.fromFile("test.txt").mkString.split("\\s+")

var total = 0d

nums.foreach(total += _.toDouble)

println(total)
println(total/nums.length)
println(nums.max)
println(nums.min)
```

编写Scala程序，向文件中写入2的n次方及其倒数，指数n从0到20。对齐各列:
--------------------------------------------------------------------

```sh
   1         1
   2         0.5
   4         0.25
 ...         ...
```

```scala
import java.io.PrintWriter

val pw = new PrintWriter("test.txt")

for ( n <- 0 to 20){
  val t = BigDecimal(2).pow(n)
  pw.write(t.toString())
  pw.write("\t\t")
  pw.write((1/t).toString())
  pw.write("\n")
}

pw.close()
```

编写正则表达式,匹配Java或C++程序代码中类似"like this,maybe with " or\
"这样的带引号的字符串。编写Scala程序将某个源文件中所有类似的字符串打印出来
--------------------------------------------------------------------------

```scala
import io.Source

val source = Source.fromFile("test.txt").mkString

val pattern = "\\w+\\s+\"".r

pattern.findAllIn(source).foreach(println)
```

编写Scala程序，从文本文件读取内容，并打印出所有的非浮点数的词法单位。要求使用正则表达式
---------------------------------------------------------------------------------------

```scala
import io.Source

val source = Source.fromFile("test.txt").mkString

val pattern = """[^((\d+\.){0,1}\d+)^\s+]+""".r

pattern.findAllIn(source).foreach(println)
```

编写Scala程序打印出某个网页中所有img标签的src属性。使用正则表达式和分组
-----------------------------------------------------------------------

```scala
import io.Source

val source = Source.fromFile("D:\\ProgramCodes\\ScalaTest\\src\\test.txt").mkString
val pattern = """<img[^>]+(src\s*=\s*"[^>^"]+")[^>]*>""".r

for (pattern(str) <- pattern.findAllIn(source)) println(str)

```

编写Scala程序，盘点给定目录及其子目录中总共有多少以.class为扩展名的文件
-----------------------------------------------------------------------

```scala
import java.io.File

val path = "."

val dir = new File(path)


def subdirs(dir:File):Iterator[File]={
  val children = dir.listFiles().filter(_.getName.endsWith("class"))
  children.toIterator ++ dir.listFiles().filter(_.isDirectory).toIterator.flatMap(subdirs _)
}

val n = subdirs(dir).length

println(n)
```

扩展那个可序列化的Person类，让它能以一个集合保存某个人的朋友信息。构造出一些Person对象，让他们中的一些人成为朋友，然后将Array[Person]保存到文件。将这个数组从文件中重新读出来，校验朋友关系是否完好
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

注意,请在main中执行。脚本执行无法序列化。

```scala
import collection.mutable.ArrayBuffer
import java.io.{ObjectInputStream, FileOutputStream, FileInputStream, ObjectOutputStream}

class Person(var name:String) extends Serializable{

  val friends = new ArrayBuffer[Person]()

  def addFriend(friend : Person){
    friends += friend
  }

  override def toString() = {
    var str = "My name is " + name + " and my friends name is "
    friends.foreach(str += _.name + ",")
    str
  }
}


object Test extends App{
  val p1 = new Person("Ivan")
  val p2 = new Person("F2")
  val p3 = new Person("F3")

  p1.addFriend(p2)
  p1.addFriend(p3)
  println(p1)

  val out = new ObjectOutputStream(new FileOutputStream("test.txt"))
  out.writeObject(p1)
  out.close()

  val in =  new ObjectInputStream(new FileInputStream("test.txt"))
  val p = in.readObject().asInstanceOf[Person]
  println(p)
}
```
