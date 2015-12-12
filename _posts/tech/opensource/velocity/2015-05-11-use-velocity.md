---
layout: post
title: 读源码-Velocity使用
categories: [velocity]
tags: [opensource,velocity,java]
avatarimg: "/img/head.jpg"
author: Ivan
published: false

---

# Velocity语法

- \#开头为操作
- $开头为取值

## 注释

- 单行注释

```
## This is a single line comment.
```

- 多行注释：

```
#* 
Thus begins a multi-line comment. 
Online visitors won't see this text 
because the Velocity Templating Engine will ignore it. 
*#
```

- Javadoc-style 注释：

```
#** This is a VTL comment block and may 
be used to store such information as the 
document author and versioning information: 
@author 
@version 5 
*#
```

<!-- more -->

## 引用：

在VTL中有三种类型的引用，变量，属性和方法。

- 变量：

必须以字母开头。
其余部分可包括:

```
alphabetic (a .. z, A .. Z)
numeric (0 .. 9)
hyphen ("-")
underscore ("_")
```

- 属性：

变量后面加.即可。
比如：

```
$customer.Address 
$purchase.Total
```

- 方法：

```
$purchase.getTotal() 
$page.setTitle( "My Home Page" )
```

## 设值：

```
#set( $a = "Velocity" )
```

## 循环：

```
#foreach( $mud in $mudsOnSpecial ) 

#end
$foreach.count 1...
$foreach.index 0...
```

## 判断:

```
#if(...)

#elseif(...)

#else

#end
```

## 引入：

```
#include("")   引入文件，不解析，只能引入TEMPLATE_ROOT下的文件，可引入多个文件，以逗号隔开
#parse("")     引入文件，解析,只能引入一个文件
```

## 中断：

```
#break
```

和Java中break类似，可中断#foreach, #parse, #evaluate, #define, #macro, or #@somebodymacro
默认中断当前范围的代码，带名称中断到特定范围。#break($macro)

## 停止：调试时使用

```
#stop
```

## 动态执行VTL:

```
#evaluate 
#set($source1 = "abc") 
#set($select = "1") 
#set($dynamicsource = "$source$select") 
## $dynamicsource is now the string '$source1' 
#evaluate($dynamicsource)
```

## 定义：

```
#define
定义一段VTL，供调用
#define( $block )Hello $who#end 
#set( $who = 'World!' ) 
$block
```

## 宏：

```
#macro
#define定义了变量，#macro定义了操作

#macro( d ) 
##d为方法名，如果需要参数，在d后面添加，空格隔开
<tr><td></td></tr> 
#end
#d()
```

## 宏$!bodyContent

```
#macro( d ) 
<tr><td>$!bodyContent</td></tr> 
#end

#@d()Hello!#end
```

## 转意符：

```
## The following line defines $email in this template: 
#set( $email = "foo" ) 
$email 
\$email
结果：
foo 
$email
```

# Velocity实现机制：

当你使用Velocity的时候，你会做下面几件事情:

- 初始化Velocity，只需要初始化一次。有两种方式可以进行初始化Singleton方式和"separate runtime instance".
- 创建一个Context对象。
- 将你的数据添加到Context中。
- 选择一个模板。
- 将模板和你的数据整合。

代码如下：

```java
import java.io.StringWriter; 
import org.apache.velocity.VelocityContext; 
import org.apache.velocity.Template; 
import org.apache.velocity.app.Velocity; 
import org.apache.velocity.exception.ResourceNotFoundException; 
import org.apache.velocity.exception.ParseErrorException; 
import org.apache.velocity.exception.MethodInvocationException; 

Velocity.init(); 
VelocityContext context = new VelocityContext(); 
context.put( "name", new String("Velocity") ); 
Template template = null; 
try { 
template = Velocity.getTemplate("mytemplate.vm"); 
} catch( ResourceNotFoundException rnfe ) { 
// couldn't find the template 
} catch( ParseErrorException pee ) {
// syntax error: problem parsing the template 
} catch( MethodInvocationException mie ) { 
// something invoked in the template // threw an exception 
} catch( Exception e ) {} 
StringWriter sw = new StringWriter(); 
template.merge( context, sw );
```

## Singleton与Separate 

Singleton模式下，jvm中只有一个Velocity实例，应用中可方便配置，代码如下:

```java
import org.apache.velocity.app.Velocity; 
import org.apache.velocity.Template; 

... /* * Configure the engine - as an example, we are using * ourselves as the logger - see logging examples */ 

Velocity.setProperty( Velocity.RUNTIME_LOG_LOGSYSTEM, this); 

/* * now initialize the engine */ 

Velocity.init(); 
... 
Template t = Velocity.getTemplate("foo.vm");
```

Separate模式下,jvm中会有多个Velocity实例，每个velocity可单独配置，针对各自的需求可调用不同的velocity.

```java
import org.apache.velocity.app.VelocityEngine; 
import org.apache.velocity.Template; 
... 
/* * create a new instance of the engine */ 
VelocityEngine ve = new VelocityEngine(); 
/* * configure the engine. In this case, we are using * ourselves as a logger (see logging examples..) */ 
ve.setProperty( VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this); 
/* * initialize the engine */ ve.init(); 
... 
Template t = ve.getTemplate("foo.vm");
```