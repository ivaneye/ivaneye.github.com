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

[TOC]

## 成也Class,败也Class

Java自95年发布以来，借由“一次编译，到处运行”的特性，在企业级开发中获得了巨大的成功。

而相对的，在桌面应用上却一直没有起色。虽然目前看来桌面应用的需求越来越小，因为web应用的体验越来越接近桌面应用了。

但实际上，即使桌面应用的需求量依然很大，Java也很难有大起色。因为帮助它在J2EE上取得成功的特性，在J2SE上反而成为了绊脚石！

- **虚拟机**：虚拟机不是Java独创的，实际上在Java之前就有语言使用了虚拟机。但是由于硬件跟不上，速度太慢而放弃了。Java正好赶上了硬件的发展足以支撑虚拟机的时期。但是在很长一段时间里，速度慢依然是Java被诟病的原因。速度慢的一个原因就是因为要先启动虚拟机。这在J2EE领域不算问题，因为应用不会频繁的启动。但是在桌面程序领域就不同了，使用完就关掉了，要使用的时候就再次打开。但是如果每次打开都要很久，谁能受得了？
- **一次编译，到处运行**：这是一个美好的愿望，初期由于awt的失败，导致大家戏谑的称Java为“一次编写，到处修改”，因为awt在各个系统上的展示有差异。后来通过swing,JavaFx1,JavaFx2在桌面展示方面的完善，展示部分的问题算是解决了。但是，有一个硬伤一直无法解决，也是解决不了的。那就是Class文件，为了做到“一次编译，到处运行”，就需要有统一结构，有统一结构就容易破解。Java自己就直接提供了反编译工具。这在J2EE也不算事，可以说是好事。首先，J2EE运行在服务端，用户是看不到源码的，所以也就没法反编译。同时，由于是同一结构且结构紧凑，可以通过网络的形式远程获取Class进行执行。而在J2SE，这就是个大问题了。大家都知道盗版软件在国内猖獗，虽然商业软件有各种加密手段，但还是阻挡不了破解！用C,C++写的二进制程序尚且如此，如果软件是用Java写的，那破解起来就更简单了，因为软件在客户机上，直接把代码反编译就可以了。可能这就是Java写的客户端软件没有市场的一个重要原因！

虽然由于上面的种种原因导致Java并不是很适合桌面型软件，但是在实际环境里还是有需要将软件部署到客户端端机器上的情况。为了保护自己辛苦编写的软件不被轻易破解，就需要进行相应的加密处理。

## 加密思路

实际上，只要时间足够长，任何密码都是可以被破解的。所以如果一个密码在软件有效期内不被破解，这就算是一个成功的加密！比如，你卖的软件有效期十年（有多少软件能用十年的？！），密码破解需要十年零一天，那这种加密方式是合适的！所以，加密的最经济的目的不是为了防止破解，而是为了加大破解难度，**使得在软件有效期内不被破解**即可！

在Java领域，有不少的加密方式，其中用得比较多的就是混淆了！下面一一进行列举！

## 解决方案

- 混淆
- 平台语言
- Java9
- Class文件加解密
  - 基于ClassLoader
  - 基于Instrumentation
  - 自定义类加载的处理（基于Instrumentation）
  - Class文件加解密的问题和解决
  - 底层方案
- 终极方案

### 混淆

最简单的方式就是混淆，市面上有各种混淆工具，开源的、商业的。

混淆的作用就是把class文件中，原本有意义的名字改成a,b,c这种没有意义的名字，来增加逻辑破解的难度。

### 平台语言

JVM上目前有200多种语言，比如:scala,clojure,kotlin,jruby这些语言，可以使用这些语言进行编写，生成的class自带"混淆"功能！

因为不是标准的Java代码编译的，所以相同的逻辑生成的class和用Java编写所生成的class差异较大，如果不熟悉对应语言，相应的破解也就比较麻烦了。

### Java9

Java9里提供了，Jlink 工具和 AOT（预先编译技术）。可以把 java 程序编译成可执行的二进制文件。

二进制文件相对class文件破解难度要大很多，无形中就实现了加密。不过Java9什么时候正式发布，还是个未知数～

### Class文件加解密

如果要自己实现Class的加解密，那最先想到的方法就是对Class文件进行加密，然后在执行的时候对Class进行解密了。

加密没什么好说的，选择一种加密方式直接对Class文件进行加密就可以了！

主要的工作在执行时对Class进行解密！网上比较多的是基于ClassLoader的方式，其实也可以通过Instrumentation的方式进行解密。

此方式对于正常的Java应用程序来说没什么问题，但是对于依赖了某些第三方库的程序，由于第三方库实现了自己的类加载机制，需要做额外处理，详见[自定义类加载的处理](#自定义类加载的处理（基于Instrumentation）)

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

这样的修改还是有问题的，因为Jetty自己还有一套类加载机制，通过asm直接读取的Class，主要是用来解析annotation，此修改对asm无效，相应修改见[自定义类加载的处理](#自定义类加载的处理（基于Instrumentation）)

#### 基于Instrumentation

Java5开始提供了Instrumentation功能，使用 Instrumentation，可以构建一个独立于应用程序的代理程序（Agent），用来监测和协助运行在 JVM 上的程序，甚至能够替换和修改某些类的定义。

这里需要做的就是在读取Class的时候进行解密，代码很简单

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

#### 自定义类加载的处理（基于Instrumentation）

上面的方案只是处理了JVM自己的Class的加载逻辑，像Tomcat/Jetty这类服务器有一套自己的类加载机制，Jsp的加载也有一套自己的机制，以及Spring,dubbo这样的第三方库，也有自己的一套类加载机制，这里分别给出解决方案。

整体思路是通过javassist[^注1]对相应的加载方法进行增强，增加解密逻辑！

- Tomcat/Jetty/Spring

这三个放在一起，是因为它们使用的都是asm的ClassReader来进行类加载的，不过Spring对asm进行了自行封装，实际就只是改了包名而已！

看下ClassReader的代码，大概就知道加载逻辑

```java
//主要逻辑就是这个构造方法，其余的构造方法最后都会调用这个构造方法
public ClassReader(byte[] var1, int var2, int var3) {
        ....
}
```

第一个参数就是Class的字节数组，只要在对这个byte[]进行解密处理就可以了。使用Instrumentation结合javassist[^注1]对其做个增强就可以了。

```java
if (className.endsWith("org/springframework/asm/ClassReader")
                || className.endsWith("org/objectweb/asm/ClassReader")) {
            try {
                ClassPool classPool = ClassPool.getDefault();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
                CtClass clazz = classPool.makeClass(byteArrayInputStream);
                CtConstructor[] constructors = clazz.getDeclaredConstructors();
                for (CtConstructor ctConstructor : constructors) {
                    CtClass[] ctClasses = ctConstructor.getParameterTypes();
                    if (ctClasses.length == 3) {
                        return fixConstruct(clazz, ctConstructor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
```

```java
private byte[] fixConstruct(CtClass clazz, CtConstructor constructor) {
        try {
            StringBuffer sb = new StringBuffer();
            //进行解密
            sb.append("$1=decryptIfNessary($1);\n");
            sb.append("$3=$1.length;\n");
            constructor.insertBefore(sb.toString());
            return clazz.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
```

- Jsp

Jsp的读取使用了org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader！

```java
//核心逻辑在这个构造方法里
public ClassFileReader(byte[] classFileBytes, char[] fileName, boolean fullyInitialize) throws ClassFormatException {
    ... 
}
```

和上面一样的方案，对classFileBytes进行解密即可！

- dubbo

dubbo使用了javassist.bytecode.ClassFile！所以需要使用javassist对javassist的ClassFile类进行增强。由于是Web项目，打破了双亲委托模型[^注2]，所以可以使用上层的javassist对下层的javassist进行增强。

```java
//这里的流是ClassFile流，进行处理即可
public ClassFile(DataInputStream in) throws IOException {
        read(in);
}
```

处理逻辑依然相同，不过这里处理的是流！

#### Class文件加解密的问题和解决

眼尖的应该已经看出对Class文件进行加解密的问题了，应该说是致命的！因为加解密本身就是使用Java的，这段Java没法进行加密操作，也就是加解密逻辑是暴露在用户眼底的，只要找到这段逻辑就知道了加解密逻辑了。当然，这段逻辑可以使用JNI来进行本地化.但是对字节数组和流的处理还是在Java里，导致的问题是，用户可以在此处做处理，将解密后的字节数组输出，就可以获取到解密后的Class了！

所以，**此方法建议和混淆一起使用，用于进一步增加破解难度！**

#### 底层方案

其实，Java9已经给出了方案，就是编译成二进制！但是目前Java9还没正式发布！

所以如果自行处理的话，最安全的做法，就是对JVM的native的defineClass进行处理！

```java
private native Class<?> defineClass0(String name, byte[] b, int off, int len,
                                         ProtectionDomain pd);

private native Class<?> defineClass1(String name, byte[] b, int off, int len,
                                         ProtectionDomain pd, String source);

private native Class<?> defineClass2(String name, java.nio.ByteBuffer b,
                                         int off, int len, ProtectionDomain pd,
                                         String source);
```

也就是对这三个方法的byte[]或ButeBuffer进行解密操作。这就需要去修改JVM底层C代码了。成本也相对较高！

### 终极方案

上面的方法均是使用技术手段进行加密操作。而实际终极方案可以不使用技术手段，可以通过法律手段来解决！当然成本就更高了！

## 总结

就目前来看，Java还是没有一个完美的加密方案，如上的方案都有或多或少的缺陷。或破解容易，或成本较高。

Java9通过模块化、Jlink和AOT应该能给出一个较经济的方法来解决这个问题！



[注1]:https://ivaneye.com/2015/05/20/classloader.html	"ClassLoader"
[注2]:https://ivaneye.com	"后续给出javassist实用手册"

#  