---
layout: post
title: 读源码-JUnit3设计
categories: [junit3]
tags: [opensource,junit3,java]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 内容回顾

- 主要梳理了JUnit3的源码

本篇主要说明JUnit3的代码设计及为什么这样设计！

# run方法

上篇提出了两个问题！第一个问题是:为什么Test中的run方法要传入一个TestResult，然后再将自身传递给TestResult去执行呢？

首先从名字可以看出,TestResult类是用来保存测试结果的。实际上是对测试提供了一个统一的收集测试结果的对象。这实际上是个设计模式，叫Collecting Parameter模式。

## Collecting Parameter模式定义

当你需要从几个方法中收集结果时，你应当给方法增加一个参数，并传递一个会替你收集参数的对象。

TestResult就是这样一个对象，它收集的结果可以方便在各个终端显示。

# runProtected方法

上篇第二个问题:

为什么TestResult的run()方法中，要先实例化一个Protectable对象，而不直接调用test的runBare方法呢？

实际上，这主要是为了规避代码重复!

查看源码，我们可以看到TestSetup类中的run方法，也实例化一个Protectable对象，继而进行调用。

```java
public void run(final TestResult result) {
  Protectable p= new Protectable() {
    public void protect() throws Exception {
      setUp();
      basicRun(result);
      tearDown();
    }
  };
  result.runProtected(this, p);
}
```

在这里，Protectable中封装了不同的执行逻辑，然后传递给runProtected方法来执行。

<!-- more -->

# startTest和endTest方法

上篇提到了startTest和endTest方法，只是简单的提到了它们是触发添加的Listener的。我们来看下它们的实现。

```java
public void startTest(Test test) {
	final int count= test.countTestCases();
	synchronized(this) {
		fRunTests+= count;
	}
	for (Enumeration e= cloneListeners().elements(); e.hasMoreElements(); ) {
		((TestListener)e.nextElement()).startTest(test);
	}
}

public void endTest(Test test) {
	for (Enumeration e= cloneListeners().elements(); e.hasMoreElements(); ) {
		((TestListener)e.nextElement()).endTest(test);
	}
}
```

它们只是去遍历TestListener去执行其对应的方法。

这实际上是观察者模式。

![]({{site.CDN_PATH}}/assets/designpattern/observer.jpg)

- TestResult是ConceteSubject
- TestListener为Observer
- TestListener子类为ConceteObserver

这里的Listener主要是为了回显信息的

# runBare方法

runBare方法如下:

```java
public void runBare() throws Throwable {
  Throwable exception= null;
  setUp();
  try {
    runTest();
  } catch (Throwable running) {
    exception= running;
  } finally {
    try {
      tearDown();
    } catch (Throwable tearingDown) {
      if (exception == null) exception= tearingDown;
    }
  }
  if (exception != null) throw exception;
}
```

runBare()方法定义了测试执行的整体框架，而对应的实现都由子类来实现了！

这里实际上是模板方法模式。看下模板方法的UML图

![]({{site.CDN_PATH}}/assets/designpattern/template.jpg)

- 这里TestCase就相当于这里的AbstractClass
- 而PersonTest,AnimalTest就相当于ConcreteClass

# runTest方法

我们看runTest方法

```java
protected void runTest() throws Throwable {
		assertNotNull(fName); // Some VMs crash when calling getMethod(null,null);
		Method runMethod= null;
		try {
			// use getMethod to get all public inherited
			// methods. getDeclaredMethods returns all
			// methods of this class but excludes the
			// inherited ones.
			runMethod= getClass().getMethod(fName, (Class[])null);
		} catch (NoSuchMethodException e) {
			fail("Method \""+fName+"\" not found");
		}
		if (!Modifier.isPublic(runMethod.getModifiers())) {
			fail("Method \""+fName+"\" should be public");
		}

		try {
			runMethod.invoke(this, (Object[])new Class[0]);
		}
		catch (InvocationTargetException e) {
			e.fillInStackTrace();
			throw e.getTargetException();
		}
		catch (IllegalAccessException e) {
			e.fillInStackTrace();
			throw e;
		}
}
```

# TestSuite与TestCase

上篇提到了，对于TestCase来说，在执行时，JUnit框架会自动将其封装为TestSuite后执行，TestSuite实际上是TestCase的集合。很显然的组合模式。

![]({{site.CDN_PATH}}/assets/designpattern/composite.jpg)

- 这里TestSuite就是Composite
- TestCase就是Leaf
- Test接口是Component

# RepeatedTest与TestSetup

- RepeatedTest和TestSetup类给测试类提供了额外的功能
- 再看UML图，两个类都继承TestDecorator。很明显的装饰模式

看下装饰模式的UML图

![]({{site.CDN_PATH}}/assets/designpattern/decorator.jpg)

- 很明显TestDecorator就是Decorator
- RepaetedTest和TestSetup就是其子类
- Test接口是Component
- TestCase是ConcreteComponent

# 测试执行

JUnit通过继承TestCase来实现具体的测试，执行测试则是执行具体实现的test*()方法

![]({{site.CDN_PATH}}/assets/designpattern/command.jpg)

- TestRunner相当于命令模式中的客户（Client）角色
- TestCase担当Command和Reciver两个角色
- 具体的测试为ConcreteCommand
- TestResult为Invoker

# 测试方法的执行

在执行测试方法时，在runTest()方法内，使用了反射，来执行test*()方法.

```java
protected void runTest() throws Throwable {
	assertNotNull(fName); // Some VMs crash when calling getMethod(null,null);
	Method runMethod= null;
	try {
		// use getMethod to get all public inherited
		// methods. getDeclaredMethods returns all
		// methods of this class but excludes the
		// inherited ones.
		runMethod= getClass().getMethod(fName, (Class[])null);
	} catch (NoSuchMethodException e) {
		fail("Method \""+fName+"\" not found");
	}
	if (!Modifier.isPublic(runMethod.getModifiers())) {
		fail("Method \""+fName+"\" should be public");
	}

	try {
		runMethod.invoke(this, (Object[])new Class[0]);
	}
	catch (InvocationTargetException e) {
		e.fillInStackTrace();
		throw e.getTargetException();
	}
	catch (IllegalAccessException e) {
		e.fillInStackTrace();
		throw e;
	}
}
```

这里网上不少文章分析这里是适配器模式，个人认为比较牵强。

![]({{site.CDN_PATH}}/assets/designpattern/adapter.jpg)

从上图可以看出，标准适配器模式Adapter与Adaptee是两个不同的对象。这里是实际上是一个类，可能有人说这是适配器模式的变体，那照这么说，方法委托就是适配器模式咯？！

其实这里就是通过反射进行的方法委托而已，没必要上升到设计模式的层面。
