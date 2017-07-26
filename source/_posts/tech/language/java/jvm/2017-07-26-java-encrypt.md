---
layout: post
date: 2017-07-26
title: Java应用加解密方案
categories: java
tags: [java,jvm,encrypt]
avatarimg: "/img/head.jpg"
author: wangyifan
published: true
---

## J

Java语言的客户端软件没有市场的一个原因！



## 加密的原因

## 加密思路

## 解决方案

- 混淆
- 平台语言
- Java9
- Class文件加解密
  - 基于ClassLoader
  - 基于Instrumentation
- 自定义类加载的处理
  - Tomcat/Jetty
  - Spring
  - dubbo

### 混淆

最简单的方式就是混淆，市面上有各种混淆工具，开源的、商业的。

混淆的作用就是把class文件中，原本有意义的名字改成a,b,c这种没有意义的名字，来增加逻辑破解的难度。

### 平台语言

JVM上目前有200多中语言，比如:scala,clojure,kotlin,jruby这些语言，可以使用这些语言进行编写，生成的class自带"混淆"功能！

因为不是标准的Java代码编译的，所以相同的逻辑生成的class和用Java编写所生成的class差异较大，如果不熟悉对应语言，相应的破解也就比较麻烦了。

### Java9

Java9里提供了，Jlink 工具和 AOT（预先编译技术）。可以把 java 程序编译成可执行的二进制文件。

二进制文件相对class文件破解难度要大很多，无形中就实现了加密。不过Java9什么时候正式发布，还是个未知数～

### Class文件加解密

如果要自己实现Class的加解密，那最先想到的方法就是对Class文件进行加密，然后在执行的时候对Class进行解密了。

加密没什么好说的，选择一种加密方式直接对Class文件进行加密就可以了！

主要的工作在执行时对Class进行解密！网上比较多的是基于ClassLoader的方式，其实也可以通过Instrumentation进行增强的方式进行解密。

#### 基于ClassLoader

基于ClassLoader的问题是，你需要做各种适配，对Main的、对Jetty的、对Tomcat的。

- Main

如果你的应用是通过Main来执行的，那可以自定义一个ClassLoader，继承URLClassLoader，覆写它的findClass方法。主要就是里面获取Class字节码的逻辑

```java
public Class<?> findClass(final String name) throws ClassNotFoundException {
    ...
    Resource res = ucp.getResource(path, false);   //这里就是Class的数据，先对其做解密，再走后续流程  
    ...
}
```

编写完成后，通过此ClassLoader来启动你的应用程序。

- Jetty

对于Jetty需要实现一个ClassLoader，继承org.eclipse.jetty.webapp.WebAppClassLoader，同样的覆写findClass方法，方式和上面的相同。

编写完成后，需要将此类打包成jar，放到${jetty_home}/lib/ext下，修改contexts目录下，对应应用的xml文件

```xml
<Configure id="mycontext1" class="org.eclipse.jetty.webapp.WebAppContext">
  ...
  <Set name="classLoader">
          <New class="ClassLoader全限定名">
          <Arg><Ref id="mycontext1"/></Arg></New>
  </Set>
  ...
</Configure>
```

这样的修改还是有问题的，因为Jetty自己还有一套类加载机制，通过asm直接读取的Class，主要是用来解析annotation，此修改对asm无效，相应修改见[自定义类加载的处理](#自定义类加载的处理)

#### 基于Instrumentation

Java5开始提供了Instrumentation功能，使用 Instrumentation，可以构建一个独立于应用程序的代理程序（Agent），用来监测和协助运行在 JVM 上的程序，甚至能够替换和修改某些类的定义。

这里需要做的就是在读取Class的时进行解密，代码很简单

```java
public class PreMain {
    public static void premain(String agentArgs, Instrumentation inst)
            throws ClassNotFoundException, UnmodifiableClassException {
        inst.addTransformer(new Transformer());
    }
}
```

```java
class Transformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader l, String className, Class<?> c,
                            ProtectionDomain pd, byte[] b) throws IllegalClassFormatException {
        if (DecryptClass.isEncrypted(b)) {
            try {
                b = DecryptClass.decryptClass(b);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return b;
    }
}
```

配置MANIFEST.MF	

```
Premain-Class: A.B.C.PreMain
```

在运行应用时，添加-javaagent:path2PreMain.jar即可

### 自定义类加载的处理

#### Tomcat/Jetty

#### Spring

#### dubbo



## 总结



#  