---
layout: post
title: 读源码-JUnit4实现
categories: [junit4]
tags: [opensource,junit4,java]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 内容回顾

- 上篇内容主要介绍了JUnit4的使用
- 如何编写测试类
- 如何运行单个测试类
- 如何运行多个测试类
- 如何设置Class级别的setUp()和tearDown()方法
- 如何多次运行测试

本篇文章将梳理JUnit4源码

# 代码结构

上篇最后给出了测试UML图

![]({{site.CDN_PATH}}/assets/opensource/junit4/diagram.png)

在JUnit3中

- 所有的测试类都继承了TestCase这个类
- TestCase这个类继承了Assert类，实现了Test接口

而这里

- 测试类不集成任何的父类
- 也没有Test接口

完全通过注解来处理，所以这里主要就梳理一下JUnit4注解处理相关代码

# JUnitCore

JUnit4的入口方法在org.junit.runner.JUnitCore中，我们从这里开始。

核心执行方法为run方法

```java
public Result run(Runner runner) {
    Result result = new Result();
    RunListener listener = result.createListener();
    notifier.addFirstListener(listener);
    try {
        notifier.fireTestRunStarted(runner.getDescription());
        runner.run(notifier);
        notifier.fireTestRunFinished(result);
    } finally {
        removeListener(listener);
    }
    return result;
}
```

结构与JUnit3中的TestRunner的doRun方法类似。

- 添加监听器
- 执行测试

<!-- more -->

# JUnit4ClassRunner

JUnit4ClassRunner就是执行JUnit4测试类的处理类。

```java
public void run(final RunNotifier notifier) {
    new ClassRoadie(notifier, testClass, getDescription(), new Runnable() {
        public void run() {
            runMethods(notifier);
        }
    }).runProtected();
}
```

runProtected方法如下

```java
public void runProtected() {
        try {
            runBefores();
            runUnprotected();
        } catch (FailedBefore e) {
        } finally {
            runAfters();
        }
    }
```

这里的结构和JUnit3的TestResult类的run()方法和runProtected()很像。

- runProtected定义了执行整体流程
- runMethods(notifier)为实际执行的方法,其实就是遍历所有测试方法来执行

```java
protected void runMethods(final RunNotifier notifier) {
    for (Method method : testMethods) {
        invokeTestMethod(method, notifier);
    }
}
```

而这些测试i方法是如何得到的呢？当然通过反射了，相关代码在TestClass中

```java
public List<Method> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {
    List<Method> results = new ArrayList<Method>();
    for (Class<?> eachClass : getSuperClasses(klass)) {
        Method[] methods = MethodSorter.getDeclaredMethods(eachClass);
        for (Method eachMethod : methods) {
            Annotation annotation = eachMethod.getAnnotation(annotationClass);
            if (annotation != null && !isShadowed(eachMethod, results)) {
                results.add(eachMethod);
            }
        }
    }
    if (runsTopToBottom(annotationClass)) {
        Collections.reverse(results);
    }
    return results;
}
```

如果熟悉了JUnit3的流程，那么JUnit4的流程就相对的好梳理很多了，少了很多的继承关系!