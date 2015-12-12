---
layout: post
title: Go语言初探
categories: [golang]
tags: [golang,language]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 简介

最近研究Docker，由于Docker由golang编写，为了更好的梳理Docker，所以先过了一下golang。

其实之前看过golang，对其语法无爱，所以就没太关注。

这次先简单梳理一下个人感觉比较有新意的地方！

# Less is more

golang推崇少即是多理念，语法简单，关键字就20多个，半天过一遍语法绰绰有余！

相对的Java关键字近100个，死板的语法，一堆的模板代码，这也是Java一直被喷的地方！

PS：要说"少即是多"理念的话，应该首推Lisp，比如Clojure,关键字不超过10个，其它功能全部是在这几个关键字上实现的，且语法高度统一

就看最简单的Hello World就行了

```java
//java实现
public class Hello{
    public static void main(String[] args){
        System.out.println("Hello World");
    }
}
```

```go
//go实现
package main

import "fmt"

func main() {
	fmt.Println("Hello World")
}
```

```clojure
;clojure实现
(println "Hello World")
```

就HelloWorld这个例子看，golang和Java算是半斤八两!对比可以看出如下几个区别:

- golang不是面向对象语言，所以不是基于类和对象来构建代码的
- golang不像Java会默认导入例如java.lang包，需要手动import
- golang的启动方法也叫main，不过没有参数，且这个方法必须要在main这个包里
- golang没有访问权限控制符，它的访问控制是靠大小写来控制的(看到这你能理解为什么Println的P要大写了吧?)

<!-- more -->

# go fmt

go fmt算是个比较"变态"的功能。语言级提供format功能。连大括号的位置都有限制，否则无法编译通过!

```go
//go实现
package main

import "fmt"

func main()
{  //编译报错，不能换行
	fmt.Println("Hello World")
}
```

明显的双刃剑！

- 好处是统一了编码风格!比如Java在做项目时，基本上都需要使用相同的IDE，并且要统一formater。而eclipse即使是相同的formater文件，也会出想格式化不同的情况。golang在语言级别就避免了这种情况。
- 坏处是统一了编码风格!就像上面的括号不能换行，Java开发人员还好，平时括号就是同行的。但是如果是个C#开发人员估计就受不了了，好像C#代码一般都是括号换行的

# 目录结构

golang不仅在代码层面有规范，在目录结构上也有规范!

先说下GOPATH

GOPATH和Java里的ClassPath比较类似，ClassPath是Java用来找类的路径。GOPATH是golang用来找go代码，包及打包路径的。

比如设置了如下的GOPATH

```
export GOPATH=/home/ivan/godemo
```

那么其下默认目录结构是这样的:

```
src 存放源代码（比如：.go .c .h .s等）
pkg 编译后生成的文件（比如：.a），包括要引用的其它第三方包
bin 编译后生成的可执行文件（为了方便，可以把此目录加入到$PATH变量中）
```

同样的它规范了项目结构。对于使用Maven的Java开发来说，比较习惯。

gofmt和目录结构在语言层面处理了Java原来外部工具做的事情！

# 变量声明

这也是一个比较变态的特性。

如果你声明了一个变量，但是没有使用。抱歉，编译不过去！

```go
var a int
//.\hello.go:10: a declared and not used
```

go可以同时赋值多个变量

```go
func main() {
	 a,b := 3,4
	 fmt.Println(a,b)
}
```

那么如果你想交换两个变量的值，就很简单了

```go
func main() {
	 a,b := 3,4
	 fmt.Println(a,b)
	 b,a = a,b
	 fmt.Println(a,b)
}
```

# 面向类型

golang是面向类型的语言，其所有特性都是基于类型的！

## 封装

乍看之下，golang是以函数进行封装的。而实际上函数也是一种类型，且函数可以设置"Receiver"来实现对象的功能!

先看第一个特性，函数是一种类型，那么函数就可以被赋值给变量:

```go
package main

import "fmt"

func main() {
	a := say
	a()
}

func say(){
	fmt.Println("Hello")
}
```

这和函数式编程语言很像！

再看，使用golang来实现一个"对象"!

```go
package main

import "fmt"

func main() {
	r := Rect{2,3}
	c := Circle{3}
	area(r)
	area(c)
}

type Rect struct {
	width int
	height int
}

type Circle struct {
	radius float32
}

func area(shape Rect) {
	fmt.Println(shape.width * shape.height)
}

func area(shape Circle) {
	fmt.Println(3.14 * 3.14 * shape.radius)
}
```

- 这里定义了两个struct，和C里的结构很像
- 然后定义了两个area函数，参数分别是Rect和Circle，以Java经验来说，好像可以，方法重载嘛
- 但是golang里不行，只能使用不同的函数名
- 且这里是将struct作为参数传递给了area函数。使用面向对象开发人员的话就是很不OO

可以做如下修改

```go
//例子
package main

import "fmt"

func main() {
	r := Rect{2,3}
	c := Circle{3}
	r.area()
	c.area()
}

type Rect struct {
	width int
	height int
}

type Circle struct {
	radius float32
}

func (this Rect) area() {
	fmt.Println(this.width * this.height)
}

func (this Circle) area() {
	fmt.Println(3.14 * 3.14 * this.radius)
}
```

- 在func设置了该方法的接收者
- 两个函数名字相同，但是接收者不同，也就变成了不同的函数了
- 调用方式也变成了很OO的obj.method()方式了

## 继承

golang可以通过匿名字段来实现Java里的继承

```go
//例子
package main

import "fmt"

func main() {
	r := Rect{2,3,Shape{"Red"}}
	c := Circle{3,Shape{"Blue"}}
	r.area()
	c.area()
}

type Shape struct {
	color string
}

type Rect struct {
	width int
	height int
	Shape
}

type Circle struct {
	radius float32
	Shape
}

func (this Shape) area() {
	fmt.Println("Shape Color is",this.color)
}

func (this Rect) area() {
	fmt.Println(this.width * this.height)
	fmt.Println("Rect Color is",this.color)
}

// func (this Circle) area() {
// 	fmt.Println(3.14 * 3.14 * this.radius)
// 	fmt.Println("Circle Color is",this.Shape.color)
// }

```

- 对于Shape里的color字段，既可以通过this.Shape.color这样访问，也可以直接通过this.color来方法
- 假如Rect里面也有了color这个字段了，通过this.color访问的就是Rect的color了，Shape里的color被覆盖了
- 对于函数也可以同样的覆盖

## 多态

golang里通过interface可以实现多态。这里的interface和Java里的interface不是一个概念

```go
//例子
package main

import "fmt"

func main() {
	r := Rect{2,3}
	c := Circle{3}
	exec(r)
	exec(c)
}

func exec(a Area) {
	a.area()
}

type Area interface {
	area()
}

type Rect struct {
	width int
	height int
}

type Circle struct {
	radius float32
}

func (this Rect) area() {
	fmt.Println(this.width * this.height)
}

func (this Circle) area() {
	fmt.Println(3.14 * 3.14 * this.radius)
}
```

- Rect和Circle并没有像在Java里一样实现了interface,而是有和在interface里面定义的方法相同的方法
- exec函数接收参数为interface

# 传值还是传引用还是传指针？

看如下Java代码

```java
public class Test {
    public static void change(int num){
        num = 0;
    }

    public static void main(String[] args) {
        int num = 1;
        change(num);
        System.out.println("num = " + num);
    }
}
```

大家肯定都知道，这样是改不了num的值的。因为传递给change方法的是num的拷贝，而不是num自身。

golang里，也是一样的

```go
//例子
package main

import (
	"fmt"
)

func main() {
	num := 1
	change(num)
	fmt.Println(num)
}

func change(num int){
	num = 0
}
```

打印结果也是1

但是go可以传指针，和C很类似。

```go
//例子
package main

import (
	"fmt"
)

func main() {
	num := 1
	change(&num)
	fmt.Println(num)
}

func change(num *int){
	*num = 0
}
```



# 线程与协程

Java一直被推崇的就是自带了线程实现，而golang自带协程实现。

如果说线程是轻量级的进程的话，协程就是轻量级的线程。

我们现在都知道，线程多了以后，会严重增加CPU负担，需要不停的切换上下文，可能导致切换上下文的时间比实际线程时间还长。

所以Java现在对性能要求高的服务，不会针对每个请求而新建一个线程，而是通过长连接的方式来进行处理！避免上述问题!

而协程没有这方面的顾虑。你可以创建成千上万的协程，而没有太大的性能损耗。

最重要的是一个go关键字即可实现!协程在golang里叫goroutine

```go
package main

import (
	"fmt"
	"runtime"
)

func main() {
	for i := 0; i < 10; i++{
		go exec()
		fmt.Println("From main")
		runtime.Gosched()
	}
}

func exec() {
	fmt.Println("From Exec")
}
```

- 这里的runtime.Gosched()是为了让CPU把时间片让给别人,否则主线程结束了，goroutine还未执行
- 类似Java里的sleep

goroutine通过Channel来进行通信

```go
//例子
package main

import (
	"fmt"
	"runtime"
)

func main() {
	a := make(chan int)
	for i := 0; i < 10; i++{
		go exec(a)
		i := <- a
		fmt.Println("From main",i)
		runtime.Gosched()
	}
}

func exec(a chan int) {
	fmt.Println("From Exec")
	a <- 2
}
```

- goroutine乍看起来和Actor还是挺像的，Actor也是通过消息进行交互
- 所以Java里也可以通过Actor来实现多线程应用(AKKA框架)
- 使用AKKA测试性能好像并没有Java纯线程的高，需要进一步测试

# 打包

Java是打包为一个jar文件，可以在任何安装了相应版本的JRE的机器上运行。

golang直接将Runtime给打包进去了。例如在windows下，使用go build命令，直接将应用打包为exe文件，双击即可运行。

相应的在linux下也可打包为对应的可运行文件。即使在windows下，也同样可以打包其它平台的包。

虽说包有点大。有1m多，但是相对Java来说还是小太多了，一个JRE可就将近10m了。

# 垃圾回收机制

这个好像比较被人喷，和JavaGC比起来，golang的GC还是太年轻了!

# 总结

本文只是简单的梳理了一下golang的比较令人印象深刻的特性。

- 语言级别的格式化支持
- 协程
- 跨平台打包
