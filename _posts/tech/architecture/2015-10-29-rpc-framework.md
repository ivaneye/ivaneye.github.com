---
layout: post
title: 分布式服务框架的架构思路
categories: [architecture]
tags: [architecture,rpc,distributed]
avatarimg: "/img/head.jpg"
author: Ivan
published: false

---

# 目标

高可用：
高伸缩：
高扩展：
高容错：

# 服务框架的核心

服务框架的核心实际上是远程方法的调用。

要实现远程方法调用大体上有如下几种方案:

- RPC（Remote Procedure Call Protocol）

RPC使用C/S方式，发送请求到服务器，等待服务器返回结果。这个请求包括一个参数集和一个文本集，通常形成“classname.methodname”形式。优点是跨语言跨平台，C端、S端有更大的独立性，缺点是不支持对象，无法在编译器检查错误，只能在运行期检查。

- RMI

RMI 采用stubs 和 skeletons 来进行远程对象(remote object)的通讯。stub 充当远程对象的客户端代理，有着和远程对象相同的远程接口，远程对象的调用实际是通过调用该对象的客户端代理对象stub来完成的，通过该机制RMI就好比它是本地工作，采用tcp/ip协议，客户端直接调用服务端上的一些方法。优点是强类型，编译期可检查错误，缺点是只能基于JAVA语言，客户机与服务器紧耦合。

- WebService

Web Service提供的服务是基于web容器的，底层使用http协议，类似一个远程的服务提供者，比如天气预报服务，对各地客户端提供天气预报，是一种请求应答的机制，是跨系统跨平台的。就是通过一个servlet，提供服务出去。

首先客户端从服务器的到WebService的WSDL，同时在客户端声称一个代理类(Proxy Class) 这个代理类负责与WebService
服务器进行Request 和Response 当一个数据（XML格式的）被封装成SOAP格式的数据流发送到服务器端的时候，就会生成一个进程对象并且把接收到这个Request的SOAP包进行解析，然后对事物进行处理，处理结束以后再对这个计算结果进行SOAP
包装，然后把这个包作为一个Response发送给客户端的代理类(Proxy Class)，同样地，这个代理类也对这个SOAP包进行解析处理，继而进行后续操作。这就是WebService的一个运行过程。

Web Service大体上分为5个层次:
 1. Http传输信道
 2. XML的数据格式
 3. SOAP封装格式
 4. WSDL的描述方式
 5. UDDI  UDDI是一种目录服务，企业可以使用它对Webservices进行注册和搜索

## 三种方式比较

1、RPC与RMI

（1）RPC 跨语言，而 RMI只支持Java。
（2）RMI 调用远程对象方法，允许方法返回 Java 对象以及基本数据类型，而RPC 不支持对象的概念，传送到 RPC 服务的消息由外部数据表示 (External Data Representation, XDR) 语言表示，这种语言抽象了字节序类和数据类型结构之间的差异。只有由 XDR 定义的数据类型才能被传递， 可以说 RMI 是面向对象方式的 Java RPC 。
（3）在方法调用上，RMI中，远程接口使每个远程方法都具有方法签名。如果一个方法在服务器上执行，但是没有相匹配的签名被添加到这个远程接口上，那么这个新方法就不能被RMI客户方所调用。
在RPC中，当一个请求到达RPC服务器时，这个请求就包含了一个参数集和一个文本值，通常形成“classname.methodname”的形式。这就向RPC服务器表明，被请求的方法在为 “classname”的类中，名叫“methodname”。然后RPC服务器就去搜索与之相匹配的类和方法，并把它作为那种方法参数类型的输入。这里的参数类型是与RPC请求中的类型是匹配的。一旦匹配成功，这个方法就被调用了，其结果被编码后返回客户方。


3、Webservice与RMI

RMI是在tcp协议上传递可序列化的java对象，只能用在java虚拟机上，绑定语言，客户端和服务端都必须是java
webservice没有这个限制，webservice是在http协议上传递xml文本文件，与语言和平台无关

## 最终选择

- RMI:局限在Java语言，对于框架来说并不适用
- WebService:基于http，此协议对远程方法调用来说不是特别合适，性能有影响
- RPC:不限定协议，便于自定义实现。故最终选择自行实现RPC

## RPC

![]({{site.CDN_PATH}}/assets/tech/architecture/rpc.png)

1）服务消费方（client）调用以本地调用方式调用服务；
2）client stub接收到调用后负责将方法、参数等组装成能够进行网络传输的消息体；
3）client stub找到服务地址，并将消息发送到服务端；
4）server stub收到消息后进行解码；
5）server stub根据解码结果调用本地的服务；
6）本地服务执行并将结果返回给server stub；
7）server stub将返回结果打包成消息并发送至消费方；
8）client stub接收到消息，并进行解码；
9）服务消费方得到最终结果。

RPC的目标就是要2~8这些步骤都封装起来，让用户对这些细节透明。

# 参考文档

[你应该知道的RPC原理](http://blog.jobbole.com/92290/)