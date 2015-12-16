---
layout: post
title: Java模块化:jigsaw初体验
categories: module
tags: [jigsaw,java,module]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 简介
Java模块化一拖再拖，目前jdk9发布了包含jigsaw的先行版。本文为[Project Jigsaw: Module System Quick-Start Guide](http://openjdk.java.net/projects/jigsaw/quick-start)的简译，及使用感受总结。

# 翻译正文

本文提供了一些简单的例子方便开发者熟悉模块化。

例子中的文件路径使用反斜杠和冒号分隔。Windows用户请修改为反斜杠和分号。

## Greetings

第一个例子是个叫com.greetings的模块，它只是简单的打印"Greetings!"。这个模块包含两个源文件:模块定义文件(mmodule-info.java)和主类。

按规定，源文件需要在一个目录下，这个目录表示模块，目录名即模块名。

```
src/com.greetings/com/greetings/Main.java
src/com.greetings/module-info.java
```
```
$ cat src/com.greetings/module-info.java
```

```java
module com.greetings { }
```

```
$ cat src/com.greetings/com/greetings/Main.java
```

```java
package com.greetings;
public class Main {
    public static void main(String[] args) {
        System.out.println("Greetings!");
    }
}
```

<!-- more -->

使用下面的命令，将源代码编译到mods/com.greetings目录下

```
$ mkdir -p mods/com.greetings

$ javac -d mods/com.greetings \
        src/com.greetings/module-info.java \
        src/com.greetings/com/greetings/Main.java
```

通过下面的命令运行:

```
$ java -modulepath mods -m com.greetings/com.greetings.Main
```

+ -modulepath 是模块路径，它可以指定一个或多个模块路径。
+ -m 指定主模块，斜杠后的值为主模块中的主类

### 说明
就目前这个例子还看不出太多内容，和普通的开发的区别就是新增了module-info.java这个类，来描述模块信息。

## Greetings world
 第二个例子升级前面的例子来说明模块间的依赖。模块com.greetings依赖org.astro。模块org.astro导出包org.astro.

 ```
src/org.astro/module-info.java
src/org.astro/org/astro/World.java
src/com.greetings/com/greetings/Main.java
src/com.greetings/module-info.java
```
```
$ cat src/org.astro/module-info.java
```
```
module org.astro {
    exports org.astro;
}
```
```
$ cat src/org.astro/org/astro/World.java
```
```java
package org.astro;
public class World {
    public static String name() {
        return "world";
    }
}
```
```
$ cat src/com.greetings/module-info.java
```
```
module com.greetings {
    requires org.astro;
}
```
```
$ cat src/com.greetings/com/greetings/Main.java
```
```java
package com.greetings;
import org.astro.World;
public class Main {
    public static void main(String[] args) {
        System.out.format("Greetings %s!%n", World.name());
    }
}
```
分别翻译org.astro模块和com.greetings模块.在编译com.greetings模块时需要指定模块路径，这样才能解析对org.astro的依赖.

```
$ mkdir mods/org.astro mods/com.greetings
```
```
$ javac -d mods/org.astro \
        src/org.astro/module-info.java src/org.astro/org/astro/World.java

$ javac -modulepath mods -d mods/com.greetings \
        src/com.greetings/module-info.java src/com.greetings/com/greetings/Main.java
```
运行和前面的例子相同:

```
$ java -modulepath mods -m com.greetings/com.greetings.Main
```
打印出:

```
Greetings world!
```

### 说明
这里主要通过module-info.java来进行导入导出的声明。主要是为了解决模块间依赖关系的处理的。

Java中的访问权限控制符是在类上的，而module-info.java中的导入导出声明是包层面的。

## 多模块编译

前面的例子中模块com.greetings和模块org.astro是分开编译的。当然也可以多个模块一起来编译.

```
$ mkdir mods

$ javac -d mods -modulesourcepath src $(find src -name "*.java")

$ find mods -type f

    mods/com.greetings/com/greetings/Main.class
    mods/com.greetings/module-info.class
    mods/org.astro/module-info.class
    mods/org.astro/org/astro/World.class
```

## 打包
前面的例子只是简单的编译，而一般为了便于发布，会将模块打包成模块化的jar包。模块化的jar包就是在包的根目录下有一个module-info.class文件，来定义模块信息。
下面的命令在mlib目录下打一个叫org.astro@1.0.jar的模块化jar包和一个叫com.greetings.jar的模块化jar包。

```
$ mkdir mlib

$ jar --create --archive=mlib/org.astro@1.0.jar \
        --module-version=1.0 -C mods/org.astro .

$ jar --create --archive=mlib/com.greetings.jar \
        --main-class=com.greetings.Main -C mods/com.greetings .

$ ls mlib

    com.greetings.jar   org.astro@1.0.jar
```
在这个例子中，模块org.astro打包时指定版本为1.0。模块com.greetings打包时指定主类为com.greetings.Main,这样我们可以直接执行模块com.greetings而不需要指明主类。

```
$ java -mp mlib -m com.greetings

    Greetings world!
```

-modulepath可以简写为-mp。jar命令行工具新增了许多新属性，其中一个是打印模块化jar的模块定义:

```
$ jar --print-module-descriptor --archive=mlib/org.astro@1.0.jar

    Name:
      org.astro@1.0
    Requires:
      java.base [ MANDATED ]
    Exports:
      org.astro
```

## 缺少导入或导出

现在让我们看一下在前面的例子里，如果我们没有对模块进行导入导出声明，会有什么错误!

```
$ cat src/com.greetings/module-info.java
```
```java
module com.greetings {
    // requires org.astro;
}
```
```
$ javac -modulepath mods -d mods/com.greetings \
        src/com.greetings/module-info.java src/com.greetings/com/greetings/Main.java

    src/com.greetings/com/greetings/Main.java:2: error: package org.astro does not exist
    import org.astro.World;
                    ^
    src/com.greetings/com/greetings/Main.java:5: error: cannot find symbol
            System.out.format("Greetings %s!%n", World.name());
                                                 ^
      symbol:   variable World
      location: class Main
    2 errors
```
现在恢复导入，注释掉导出!

```
$ cat src/com.greetings/module-info.java
```
```java
module com.greetings {
    requires org.astro;
}
```
```
$ cat src/org.astro/module-info.java
```
```java
module org.astro {
    // exports org.astro;
}
```
```
$ javac -modulepath mods -d mods/com.greetings \
       src/com.greetings/module-info.java src/com.greetings/com/greetings/Main.java

    src/com.greetings/com/greetings/Main.java:2: error: package org.astro does not exist
    import org.astro.World;
                    ^
    src/com.greetings/com/greetings/Main.java:5: error: cannot find symbol
            System.out.format("Greetings %s!%n", World.name());
                                                 ^
      symbol:   variable World
      location: class Main
    2 errors\
```

## 服务

服务可以接口服务的消费模块和服务生产模块。

在这个例子中，有一个服务消费模块和一个服务生产模块

+ 模块com.socket导出一个网络socket的API。这个API在包com.socket中，所以这个包需要导出。这个API可选择具体的实现。
服务类型是同模块下的com.socket.spi.NetworkSocketProvider，所以com.socket.spi这个包也要导出
+ 模块org.fastsocket是服务生产模块。它提供了一个com.socket.spi.NetworkSocketProvider的实现，它不需要导出任何包

下面是模块com.socket的源码：

```
$ cat src/com.socket/module-info.java
```
```java
module com.socket {
    exports com.socket;
    exports com.socket.spi;
    uses com.socket.spi.NetworkSocketProvider;
}
```
```
$ cat src/com.socket/com/socket/NetworkSocket.java
```
```java
package com.socket;

import java.io.Closeable;
import java.util.Iterator;
import java.util.ServiceLoader;

import com.socket.spi.NetworkSocketProvider;

public abstract class NetworkSocket implements Closeable {
    protected NetworkSocket() { }

    public static NetworkSocket open() {
        ServiceLoader<NetworkSocketProvider> sl
            = ServiceLoader.load(NetworkSocketProvider.class);
        Iterator<NetworkSocketProvider> iter = sl.iterator();
        if (!iter.hasNext())
            throw new RuntimeException("No service providers found!");
        NetworkSocketProvider provider = iter.next();
        return provider.openNetworkSocket();
    }
}
```
```
$ cat src/com.socket/com/socket/spi/NetworkSocketProvider.java
```
```java
package com.socket.spi;

import com.socket.NetworkSocket;

public abstract class NetworkSocketProvider {
    protected NetworkSocketProvider() { }

    public abstract NetworkSocket openNetworkSocket();
}
```
下面是模块org.fastsocket的源码:

```
$ cat src/org.fastsocket/module-info.java
```
```java
module org.fastsocket {
    requires com.socket;
    provides com.socket.spi.NetworkSocketProvider
        with org.fastsocket.FastNetworkSocketProvider;
}
```
```
$ cat src/org.fastsocket/org/fastsocket/FastNetworkSocketProvider.java
```
```java
package org.fastsocket;

import com.socket.NetworkSocket;
import com.socket.spi.NetworkSocketProvider;

public class FastNetworkSocketProvider extends NetworkSocketProvider {
    public FastNetworkSocketProvider() { }

    @Override
    public NetworkSocket openNetworkSocket() {
        return new FastNetworkSocket();
    }
}
```
```
$ cat src/org.fastsocket/org/fastsocket/FastNetworkSocket.java
```
```java
package org.fastsocket;

import com.socket.NetworkSocket;

class FastNetworkSocket extends NetworkSocket {
    FastNetworkSocket() { }
    public void close() { }
}
```

编译:

```
$ mkdir mods
$ javac -d mods -modulesourcepath src $(find src -name "*.java")
```
最后修改模块com.greetings来使用这个API

```
$ cat src/com.greetings/module-info.java
```
```java
module com.greetings {
    requires com.socket;
}
```
```
$ cat src/com.greetings/com/greetings/Main.java
```
```java
package com.greetings;

import com.socket.NetworkSocket;

public class Main {
    public static void main(String[] args) {
        NetworkSocket s = NetworkSocket.open();
        System.out.println(s.getClass());
    }
}
```
```
$ javac -d mods/com.greetings/ -mp mods $(find src/com.greetings/ -name "*.java")
```
最后我们来运行:

```
$ java -mp mods -m com.greetings/com.greetings.Main

class org.fastsocket.FastNetworkSocket
```
从输出可以看到服务的生产者已经被确定，并被当作NetworkSocket的工厂类来使用.

### 说明
上面说API可以动态选择具体的实现，如何选择并没有说明！

## 链接(The linker)

jlink是链接工具可以根据模块间的传递依赖来链接模块，构建出一个自定义的模块化运行时镜像(image)

目前这个工具需要被链接的模块是以模块化的jar或JMOD格式提供的。

下面的例子船舰一个运行时镜像，它包含了com.greetings模块和它的依赖:

```
jlink --modulepath $JAVA_HOME/jmods:mlib --addmods com.greetings --output greetingsapp
```
$JAVA_HOME/jmods目录包含了java.base.jmod和其他标准JDK模块。如果你使用你自己构建的OpenJDK，
那么jmod文件在$BUILDOUTPUT/images/jmods目录下，$BUILDOUTPUT是构建输出目录。

mlib目录包含了模块com.greetings需要的内容.

jlink工具支持很多高级属性，请通过jlink --help查看

在这里，jlink工具通过默认规则链接服务，所以镜像里会包含你其实并不需要的内容.

## javac -Xmodule and java -Xoverride

以前开发者修改类时，首先从CVS检出j，比如ava.util.concurrent类，编译源码，然后通过-Xbootclasspath/p来部署。

-Xbootclasspath/p 已经废弃了，现在可使用-Xoverride来实现同样的功能。-Xoverride可以用一个模块中的类覆盖另一个模块中的类。
后面，javac在编译一个包里的类时，如果已经存在在某个模块中，则会有警告。如果要为某个以存在的模块编译类，需要添加-Xmodule属性.

下面是个例子，它编译一个新版本的java.util.concurrent.ConcurrentHashMap，并使用它:

```
javac -Xmodule:java.base -d mypatches/java.base \
        src/java.base/java/util/concurrent/ConcurrentHashMap.java
```
```
java -Xoverride:mypatches ...
```

# 总结

+ 总体来说，初次体验jigsaw感觉要优于OSGi
+ jigsaw不需要第三方运行环境，对于开发者来说要比OSGi更友好
+ jigsaw自带解决传递依赖的工具
+ jigsaw在编译期就能处理模块间的依赖，而不像OSGi需要到运行时才报模块依赖问题。主要还是第二点:jigsaw不需要第三方运行环境
+ jigsaw可以动态选择实现，但是如何实现例子中没有提到!(网上查资料说:jigsaw并不支持OSGi那样的dynamic Service，就是说不支持动态的装载和卸载Service了。需要进一步证实)
