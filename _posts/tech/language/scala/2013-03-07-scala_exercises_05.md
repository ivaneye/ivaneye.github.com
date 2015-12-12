---
layout: post
title: 快学Scala习题解答—第五章 类
categories: scala
tags: [java,scala]
avatarimg: "/img/head.jpg"
author: Ivan

---


类
==

改进5.1节的Counter类,让它不要在Int.MaxValue时变成负数
-----------------------------------------------------

加个判断就OK了

```scala
class Count{
  private var value = Int.MaxValue
  def increment(){if(value < Int.MaxValue) value + 1 else value }
  def current = value
}
```

编写一个BankAccount类，加入deposit和withdraw方法，和一个只读的balance属性
-------------------------------------------------------------------------

```scala
class BankAccount(val balance:Int = 0){
  def deposit(){}
  def withdraw(){}
}
```

编写一个Time类，加入只读属性hours和minutes，和一个检查某一时刻是否早于另一时刻的方法before(other:Time):Boolean。Time对象应该以new Time(hrs,min)方式构建。其中hrs以军用时间格式呈现(介于0和23之间)
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```scala
class Time(val hours:Int,val minutes:Int){

   def before(other:Time):Boolean={
     hours < other.hours || (hours == other.hours && minutes < other.minutes)
   }

   override def toString():String={
     hours + " : " + minutes
   }
}
```

<!-- more -->

重新实现前一个类中的Time类，将内部呈现改成午夜起的分钟数(介于0到24\*60-1之间)。不要改变公有接口。也就是说，客户端代码不应因你的修改而受影响
-------------------------------------------------------------------------------------------------------------------------------------------

```scala
 class Time(val hours:Int,val minutes:Int){

   def before(other:Time):Boolean={
     hours < other.hours || (hours == other.hours && minutes < other.minutes)
   }

   override def toString():String={
      hours * 60 + minutes
   }
 }
```

创建一个Student类，加入可读写的JavaBeans属性name(类型为String)和id(类型为Long)。有哪些方法被生产？(用javap查看。)你可以在Scala中调用JavaBeans的getter和setter方法吗？应该这样做吗？
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

生成了name(),name\_=(),id(),id\_=(),setName(),getName(),setId(),getId()
编写代码如下

```scala
import scala.reflect.BeanProperty

class Student{

    @BeanProperty var name:String = _
    @BeanProperty var id:Long = _
}
```

javap -c Student 后显示如下

```sh
Compiled from "Student.scala"
public class Student extends java.lang.Object implements scala.ScalaObject{
public java.lang.String name();
  Code:
   0:   aload_0
   1:   getfield        #13; //Field name:Ljava/lang/String;
   4:   areturn

public void name_$eq(java.lang.String);
  Code:
   0:   aload_0
   1:   aload_1
   2:   putfield        #13; //Field name:Ljava/lang/String;
   5:   return

public void setName(java.lang.String);
  Code:
   0:   aload_0
   1:   aload_1
   2:   putfield        #13; //Field name:Ljava/lang/String;
   5:   return

public long id();
  Code:
   0:   aload_0
   1:   getfield        #19; //Field id:J
   4:   lreturn

public void id_$eq(long);
  Code:
   0:   aload_0
   1:   lload_1
   2:   putfield        #19; //Field id:J
   5:   return

public void setId(long);
  Code:
   0:   aload_0
   1:   lload_1
   2:   putfield        #19; //Field id:J
   5:   return

public long getId();
  Code:
   0:   aload_0
   1:   invokevirtual   #25; //Method id:()J
   4:   lreturn

public java.lang.String getName();
  Code:
   0:   aload_0
   1:   invokevirtual   #28; //Method name:()Ljava/lang/String;
   4:   areturn

public Student();
  Code:
   0:   aload_0
   1:   invokespecial   #34; //Method java/lang/Object."<init>":()V
   4:   return

}
```

在5.2节的Person类中提供一个主构造器,将负年龄转换为0
---------------------------------------------------

```scala
class Person(var age:Int){
  age = if(age < 0) 0 else age
}
```

编写一个Person类，其主构造器接受一个字符串，该字符串包含名字，空格和姓，如new Person("Fred Smith")。提供只读属性firstName和lastName。主构造器参数应该是var,val还是普通参数？为什么？
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

必须为val。如果为var，则对应的此字符串有get和set方法，而Person中的firstName和lastName为只读的,所以不能重复赋值。如果为var则会重复赋值而报错

创建一个Car类，以只读属性对应制造商，型号名称，型号年份以及一个可读写的属性用于车牌。提供四组构造器。每个构造器fc都要求制造商和型号为必填。型号年份和车牌可选，如果未填，则型号年份为-1，车牌为空串。你会选择哪一个作为你的主构造器？为什么？
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

这个没太明白题意。。。

```scala
class Car(val maker:String,val typeName:String,val year:Int = -1,var carLice:String = ""){
}
```

在Java,C\#或C++重做前一个练习。Scala相比之下精简多少？
------------------------------------------------------

这个就不写了。

考虑如下的类
------------

```scala
class Employ(val name:String,var salary:Double){
    def this(){this("John Q. Public",0.0)}
}
```

重写该类,使用显示的字段定义，和一个缺省主构造器。你更倾向于使用哪种形式？为什么？

```scala
class Employ{
    val name:String = "John Q. Public"
    var salary:Double = 0.0
}
```

个人更喜欢第二种方式，简单明了。

