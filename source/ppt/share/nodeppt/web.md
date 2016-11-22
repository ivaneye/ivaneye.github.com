title: 理解Web
speaker: 王一帆
url: http://www.ivaneye.com
transition: stick
theme: dark

[slide]
# 理解Web
## 平台架构部 王一帆

[slide]
# Web的本质是什么?
<!-- 讨论 -->

[slide]
# 处理**数据**

[slide]
# 访问URL后,到底发生什么?

[slide]
- http://www.example.com/hello.html

![](/web_file/03.jpg) {:&.moveIn}

[slide]
# 解析主机名

- 浏览器拿到网址之后首先会将主机名解析出来，对于 http://www.example.com/hello.html 来说就是将主机名www.example.com解析出来。

[slide]
# 查找ip

- 根据主机名，会首先查找IP，首先查询hosts文件，成功则返回其对应ip地址，如果没有查询到，则去查询DNS服务器，成功就会返回ip，否则会报告连接错误。

<!-- 域名/主机名只是方便人类识别，机器识别的是数字,IP最终还是会被转化为数字。就像编程语言只是方便我们开发而已，机器只认识0和1 -->

[slide]
# 发送http请求

- 浏览器会把自身相关信息与请求相关信息封装成HTTP请求消息改送给服务器。

```

GET /hello.html HTTP/1.1
Accept: text/plain; text/html
Accept-Language: en-gb
Connection: Keep-Alive
Host: www.example.com
User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
Accept-Encoding: gzip, deflate

...
```

[slide]
# 服务器处理请求

- 服务器读取HTTP请求中的内容，在经过解析主机、解析站点名称、解析访问资源后，会查找相关资源，如果查找成功，则返回状态码200，失败就会返回大名鼎鼎的404，在服务器监测到请求不存在的资源后，可以按照程序员设置的跳转到别的页面。所以有各种各样的404错误页面。

[slide]
# 服务器返回HTTP响应

- 服务器会将请求的资源封装成http响应
- 浏览器得到返回数据后可以会提取数据，然后调用解析内核进行翻译，最后显示出页面。
- 之后浏览器会对其引用的文件比如图片，CSS，JS等文件不断进行上述过程，直到所有文件都被下载下来之后，网页就会显示出来。

<!-- 访问网站流程,阐述数据在各个流程里的角色，确认重要性 10P -->

[slide]
# 流程划分

- 如何查找，定位资源 {:&.fadeIn}
- 如何发送请求
- 如何处理请求
- 如何返回响应
- 如何展示

[slide]

![](/web_file/Web.png)

[slide]
# 查找，定位资源

[slide]
# 从访问对象开始

- 从软件方面来看，程序需要一种方法来访问对象
- 可以通过声明变量来完成
- 使用变量，实际就是一种查找、定位的过程

[slide]
# 标识符

```java
int num = 3;
```

- 上面的代码创建了一个名为num的标识符(identifier)
- 标识符是一个名称，在这种情况下用来指定特定的内容
- 那这个标识符指向明确吗？<!-- 当我说num的时候，我是在指3吗？答案是看情况 -->

[slide]
# 作用范围

```java
public class VarTest {
    int num = 1;

    public void test(){
        System.out.println("num = " + num);
        num = 3;
        {
            int num = 2;
            System.out.println("num = " + num);
        }
        System.out.println("num = " + num);
    }
}
```

[slide]
# 扩大范围

```java
public class VarTest {
    int num = 1;
}
public class VarTest2 {
    int num = 2;
}
```

[slide]
# 逆向思考

- “作用范围”的作用是什么?

[slide]
# 确认资源

- 要确认资源，需要明确资源所在范围
- Java中（包名+类名+[方法名，属性名]）

[slide]
# 互联网上如何确认资源呢？

<!-- 从变量开始，引入标识符，继而引入URI -->

[slide]
# URI

- URI:Uniform Resource Identifier
- URI = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
- hier-part =
     - "//" authority path-abempty
     - / path-absolute
     - / path-rootless
     - / path-empty

[slide]
# URI/URL/URN

- URI:Uniform Resource Identifier
- URL:Uniform Resource Locators
- URN:Uniform Resource Name

[slide]
# 区别

- URI:按照规范定义的、可以唯一标识某一资源的字符串[RFC3986]
- URL:URL是URI的一种，不仅标识了Web资源，还指定了操作或者获取方式，同时指出了主要访问机制和网络位置。
- URN:URN是URI的一种。分两种情况，一种特指以urn方案开头的URI。另一种指包含了属性名的URI。它只是一个标识，并且不保证所表示的资源一定存在!

[slide]
# 例子

- http://www.example.com/hello.html?param=val#frag

[slide]
# URL

这是一个URL,其中:

- **http** 是定义如何访问资源的方式(scheme,协议类型)
- **www.example.com** 域名 (authority)
- **/hello.html** 是资源存放的路径 (path)

**所以它也是个URI**

[slide]
# URN

URN是URI的子集，包括名字（给定的命名空间内），但是不包括访问方式，如下所示：

- www.example.com/hello.html

[slide]
# Java URL处理

- URI
- URL

```
A URI is a uniform resource identifier while a URL is a uniform
resource locator.  Hence every URL is a URI, abstractly speaking, but
not every URI is a URL.  This is because there is another subcategory of
URIs, uniform resource names (URNs), which name resources but do not
specify how to locate them.
```
<!-- URI javadoc -->

[slide]
# Java URI

```
getQuery():String
getFragment():String
getPath():String
getPort():int
getHost():String
getAuthority():String
getScheme():String
toURL():URL
```

[slide]
# Java URL

```
toURI():URI
getFile():String
getHost():String
getPort():int
getAuthority():String
getPath():String
getQuery():String
openConnection():URLConnection
openStream():InputStream
```

[slide]
# Java Example

```java
URI uri = new URI("http://www.example.com/hello.html?param=val#frag");
System.out.println(uri.getAuthority());
System.out.println(uri.getPath());
System.out.println(uri.getQuery());
System.out.println(uri.getFragment());
URL url = new URL("http://www.baidu.com");
BufferedReader br = new BufferedReader(new
                InputStreamReader(url.openStream(),"UTF-8"));
String tmp;
while((tmp = br.readLine()) != null){
    System.out.println(tmp);
}
br.close();
```

[slide]
# 输出

```
www.example.com
/hello.html
param=val
frag
...
```

<!-- 流程细化：什么是URL,URI,如何根据URL找到服务 10p -->

[slide]
# 发送请求

[slide]
# HTTP请求

- 浏览器会把自身相关信息与请求相关信息封装成HTTP请求消息改送给服务器。

```
GET /hello.html HTTP/1.1
Accept: text/plain; text/html
Accept-Language: en-gb
Connection: Keep-Alive
Host: www.example.com
User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
Accept-Encoding: gzip, deflate

...
```

[slide]
# Request

- 请求首行
- 请求头
- 请求体

[slide]
# 如何传输?

- TCP/IP  {:&.fadeIn}

[slide]
# Java中的协议

- 接口(Interface) {:&.fadeIn}
- Protocol

[slide]
# OSI七层模型

- 应用层：针对特定应用的协议
- 表示层：设备固有数据格式和网络标准数据格式的转换
- 会话层：通信管理。负责建立和断开通信连接
- 传输层：管理两个节点之间的数据传输
- 网络层：地址管理与路由选择
- 数据链路层：互连设备之间传送和识别数据帧
- 物理层：以0,1代表电压的高低、灯光的闪灭
<!-- 类似JavaIO的装饰模式和高阶函数 -->

**在处理由上层传过来的数据时可以附上当前分层的协议所必须的首部信息**

[slide]
# JavaIO

```java
BufferedInputStream bi = new BufferedInputStream(
                            new FileInputStream(filename));
```

[slide]
# 装饰模式

- 动态地给一个对象添加一些额外的职责

![](/web_file/decorator.jpg)

[slide]
# 高阶函数

```clojure
(send
    (add-head
    (add-footer msg)))
```

[slide]
# Java8高阶函数

```java
Collections.sort(names,new Comparator<String>(){
    @Override
    public int compare(String first,String second){
        return first.length() - second.length();
    }
});
```

[slide]
# Java8高阶函数

```java
Collections.sort(names, (first, second) -> first.length() - second.length());
```

<!-- 发送出去了，就到了吗？没那么简单，还有各种中间环节 -->

[slide]
# 各种中间环节

- 路由器
- 代理
- DNS
- 网桥
- 交换机
- 网关
- 反向代理
- 中继器
- 防火墙
- CDN
- ...

<!-- web开发有涉及的，反向代理，从代理->反向代理 -->

[slide]
# 代理

![](/web_file/proxy.jpg)

[slide]
# 反向代理

![](/web_file/nproxy.jpg)

[slide]
# CDN

![](/web_file/cdn.jpg)

<!-- 如何发送数据，HTTP协议,TCP/IP.OSI七层模型，20P -->

<!-- 历经千辛万苦终于找到了服务器，服务器需要对请求进行处理 -->

[slide]
# 处理请求

<!-- 处理请求流程细分 -->

- 接收http请求
- 解析http请求
- 处理http请求

[slide]
# 接收http请求

- ServerSocket

```java
ServerSocket serverSocket = null;
int port = 8080;
serverSocket = new ServerSocket(port, 1,
                    InetAddress.getByName("127.0.0.1"));

Socket socket = serverSocket.accept();
InputStream input = socket.getInputStream();
OutputStream output = socket.getOutputStream();

...
```

[slide]
# 模型

![](/web_file/04.png)

[slide]
# 阻塞IO/非阻塞IO/同步IO/异步IO

- 一个IO操作其实分成了两个步骤:发起IO请求和实际的IO操作
- 阻塞IO和非阻塞IO的区别在于第一步：发起IO请求是否会被阻塞，如果阻塞直到完成那么就是传统的阻塞IO;如果不阻塞，那么就是非阻塞IO
- 同步IO和异步IO的区别就在于第二个步骤是否阻塞，如果实际的IO读写阻塞请求进程，那么就是同步IO，因此阻塞IO、非阻塞IO、IO复用、信号驱动IO都是同步IO;如果不阻塞，而是操作系统帮你做完IO操作再将结果返回给你，那么就是异步IO

[slide]
# 服务端(连接)[NIO]

```java
//获取一个ServerSocket通道
ServerSocketChannel serverChannel = ServerSocketChannel.open();
serverChannel.configureBlocking(false);
serverChannel.socket().bind(new InetSocketAddress(port));
//获取通道管理器
selector = Selector.open();
//将通道管理器与通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，
serverChannel.register(selector, SelectionKey.OP_ACCEPT);
```

[slide]
# 服务端(监听)[NIO]

```java
while(true){
   selector.select();//当有注册的事件到达时，方法返回，否则阻塞。
   for(SelectionKey key : selector.selectedKeys()){
       if(key.isAcceptable()){
           ServerSocketChannel server = (ServerSocketChannel)key.channel();
           SocketChannel channel = server.accept();
           channel.write(ByteBuffer.wrap(new String("send message to client").getBytes()));
           //在与客户端连接成功后，为客户端通道注册SelectionKey.OP_READ事件。
           channel.register(selector, SelectionKey.OP_READ);
       }else if(key.isReadable()){//有可读数据事件
           SocketChannel channel = (SocketChannel)key.channel();
           ByteBuffer buffer = ByteBuffer.allocate(10);
           int read = channel.read(buffer);
           byte[] data = buffer.array();
           String message = new String(data);
           System.out.println("receive message from client, size:" + buffer.position() + " msg: " + message);
       }
   }
}
```

[slide]
# 模型[NIO]

![](/web_file/06.png)

[slide]
# 半包问题

![](/web_file/07.png)

![](/web_file/08.png)

[slide]
# Reactor模型

[slide]
# AWT EVENTS

![](/web_file/09.jpg)

[slide]
# Reactor中的组件

- **Reactor**:Reactor是IO事件的派发者。
- **Acceptor**:Acceptor接受client连接，建立对应client的Handler，并向Reactor注册此Handler。
- **Handler**:和一个client通讯的实体，按这样的过程实现业务的处理。一般在基本的Handler基础上还会有更进一步的层次划分， 用来抽象诸如decode，process和encoder这些过程。比如对Web Server而言，decode通常是HTTP请求的解析， process的过程会进一步涉及到Listner和Servlet的调用。业务逻辑的处理在Reactor模式里被分散的IO事件所打破， 所以Handler需要有适当的机制在所需的信息还不全（读到一半）的时候保存上下文，并在下一次IO事件到来的 时候（另一半可读了）能继续中断的处理。为了简化设计，Handler通常被设计成状态机，按GoF的state pattern来 实现。

[slide]
# Reactor单线程模型

![](/web_file/10.png)

[slide]
# Reactor多线程模型

![](/web_file/11.png)

[slide]
# 主从Reactor模型

![](/web_file/12.png)

[slide]
# 解析http请求[Java]

```java
private String parseUri(String requestString) {
    int index1, index2;
    index1 = requestString.indexOf(' ');
    if (index1 != -1) {
        index2 = requestString.indexOf(' ', index1 + 1);
        if (index2 > index1)
            return requestString.substring(index1 + 1, index2);
    }
    return null;
}
```

<!-- Java处理HTTP,Socket编程,BIO,NIO,Thread,Reactor，30p -->
[slide]
# 如何看待请求?

<!-- 把请求解析为什么? -->

```
GET /hello.html HTTP/1.1
Accept: text/plain; text/html
Accept-Language: en-gb
Connection: Keep-Alive
Host: www.example.com
User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
Accept-Encoding: gzip, deflate

...
```

- 字符串？ {:&.moveIn}
- 数据？
- 对象？

[slide]
# 把请求看作对象

- Request对象

```java
Request
    method:String
    host:String
    port:int
    path:String
    accept:String
    ...
```

[slide]
# 把请求看作数据

```
{
    method:"GET"
    host:"www.example.com"
    port:80
    path:"/hello.html"
    accept:"text/plain; text/html"
}
```

<!-- 支线，OO VS Function Start-->

[slide]
# 面向对象VS函数式

- 定义一组对象，针对特定的对象编写特定的方法
- 定义一组函数，操作少量的数据结构
<!-- 表达式问题 -->

[slide]
# 表达式问题

- 贝尔实验室的Philip Wadler在1998年提出了表达式问题
- 通过案例定义数据类型，在不重新编译现有代码的情况下可以将新的案例添加到数据类型和数据类型的新函数中，同时保留静态类型安全（例如，没有转换）

[slide]
# 面向对象：轻松添加新的行（类型）

![](/web_file/press01.png)

- java.util.List 接口中的每一列都表示一种方法。为简单起见，它包括四种方法：List.add、List.get、List.clear 和 List.size。前四行中的每一行都表示实现 java.util.List 的一个类： ArrayList、LinkedList、Stack 和 Vector。这些行和列交叉处的单元格表示每一种类的方法的现有实现（通过标准 Java 类库提供）。在底部添加的第五行表示您可以编写的实现 java.util.List 的新类。对于行上的每一个单元格来说，您都可以编写在 java.util.List 中相应方法的您自己的实现，特定于您的新类。

[slide]
# 函数语言：轻松添加新的列（函数）

![](/web_file/press02.png)

- 列表示 Clojure 的标准集合 API 中的函数：conj、nth、empty 和 count。同样，行表示 Clojure 的内置集合类型：list、vector、map 和 set。这些行和列交叉处的单元格表示 Clojure 提供的这些函数的现有实现。通过定义新的函数，您可以向表添加新的列。假设您的新函数是用 Clojure 的内置函数编写的，则它将能够自动支持所有相同的类型。

<!-- 支线，OO VS Function End-->

[slide]
# Java的做法

- HttpServletRequest

```
getCookies(): Cookie[]
getDateHeader(String): long
getHeader(String): String
getHeaders(String): Enumeration
getHeaderNames(): Enumeration
getMethod(): String
getContextPath(): String
getQueryString(): String
getRemoteUser(): String
getRequestedSessionId(): String
getRequestURI(): String
getRequestURL(): StringBuffer
getServletPath(): String
getSession(boolean): HttpSession
getSession(): HttpSession
...
```

[slide]
# Server

- Tomcat
- Jetty
- Resin

<!-- 以上为server处理 -->

[slide]
# 如何定位资源？

- /hello.html

<!-- 目前为止我们只是定位到了服务器，如何定位到资源呢?
     就像我们定位到了类，我们如何定位到属性和方法? -->

[slide]
# 路由(Clojure示例)

```clojure
(defroutes app
  (GET "/" [] "<h1>Hello World</h1>")
  (GET "/hello.html" [] (render "hello.html"))
  (POST "/order/save" [order] (save order))
  (route/not-found "<h1>Page not found</h1>"))
```

[slide]
# 路由(Python示例)

```python
@route('/hello/<name>')
def index(name):
  return '<b>Hello {{name}}</b>!'
```

```python
def setup_routing():
  bottle.route('/', 'GET', index)
  bottle.route('/edit', ['GET', 'POST'], edit)
```

[slide]
# Servlet路由

- web.xml:部署描述文件 {:&.moveIn}
- 为什么使用XML作为描述文件?

<!-- 很多语言使用语言自身来作为描述语言，Java为什么使用XML作为描述语言? -->

[slide]
# web.xml

- ServletContext初始化参数
- Session配置
- Servlet声明
- Servlet映射
- 应用程序生命周期监听器类
- 过滤器定义和过滤器映射
- MIME类型映射
- 欢迎文件列表
- 错误页面
- 语言环境和编码映射
- 安全配置，包括login-config，security-constraint，security-constraint，security-role-ref和run-as

[slide]
# Servlet声明

<!-- 与前面的路由配置做比较 -->

```xml
<servlet>
    <servlet-name>helloServlet</servlet-name>
    <servlet-class>com.focustech.hello.HelloServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>helloServlet</servlet-name>
    <url-pattern>/hello</url-pattern>
</servlet-mapping>
```

[slide]
# Spring配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.0" xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
    <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>
            org.springframework.web.filter.CharacterEncodingFilter
        </filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    <listener>
        <listener-class>
            org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring/applicationContext.xml</param-value>
    </context-param>
```

[slide]
# Spring配置 续

```xml
...
<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>*.html</url-pattern>
</servlet-mapping>
...
<servlet>
    <servlet-name>DispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring/dispatcher-servlet.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>DispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>

<welcome-file-list>
    <welcome-file>login.html</welcome-file>
</welcome-file-list>
<error-page>
    <error-code>404</error-code>
    <location>/nopage.html</location>
</error-page>
<session-config>
    <session-timeout>360</session-timeout>
</session-config>
</web-app>
```

[slide]
# url-pattern

```xml
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
...
<servlet-mapping>
    <servlet-name>DispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>  
```

- "/"与"/\*"有什么区别？

[slide]
# Servlet规范

- 如果有完全匹配的路径，则直接调用此路径对应的servlet
- 如果找不到完全匹配路径，则按最长路径匹配规则进行匹配
- 如果URL包含了后缀(例如jsp)，那么就找能对这个后缀名处理的servlet
- 如果前三个规则都没找到对应的servlet，则容器将为请求提供相关内容，例如使用default servlet来进行处理

[slide]
# /与/\*比较

- /\*会覆盖所有其他的servlet请求，包括servlet容器提供的servlet，比如default servlet和JSP servlet
- /不会覆盖其它的servlet，但会匹配default请求

[slide]
# 对静态资源的处理

- 配置Tomcat的defaultServlet来处理静态文件。注意：要写在DispatcherServlet的前面
- 使用Spring静态资源处理配置。< mvc:default-servlet-handler/>或< mvc:resources/>(两者有什么区别?)

<!-- <mvc:default-servlet-handler/>将静态资源的处理经由Spring MVC框架交回Web应用服务器处理。而<mvc:resources/>更进一步，由Spring MVC框架自己处理静态资源，并添加一些有用的附加值功能。 -->

[slide]
# Filter顺序问题

```xml
<filter-mapping>
  <filter-name>f1</filter-name>
  <url-pattern>/a/*</url-pattern>
</filter-mapping>
<filter-mapping>
  <filter-name>f2</filter-name>
  <servlet-name>/a/b.do</servlet-name>
</filter-mapping>
<filter-mapping>
  <filter-name>f3</filter-name>
  <url-pattern>/a/c/*</url-pattern>
</filter-mapping>
<filter-mapping>
  <filter-name>f4</filter-name>
  <servlet-name>/a/d/e.do</servlet-name>
</filter-mapping>
<filter-mapping>
  <filter-name>f5</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```
- /a/s.do
- /a/b.do
- /a/d/e.do
- /b.do
- /a/c/p.do

[slide]
# 答案

- /a/s.do      1   5
- /a/b.do      1   5   2
- /a/d/e.do    1   5   4
- /b.do        5
- /a/c/p.do    1   3   5

[slide]
# Filter顺序

- 将 filter-mapping 元素包含与请求匹配的 url-pattern的筛选器按其在 web.xml 部署描述符中出现的顺序添加到链中。
- 将 filter-mapping 元素包含与请求匹配的 servlet-name 的筛选器添加在链中与 URL 模式匹配的筛选器之后。
- 链上先进先出的，链中最后的项目往往是最初请求的资源。

[slide]
# Listener

```xml
<listener>
    <listener-class>
        org.springframework.web.context.ContextLoaderListener
    </listener-class>
</listener>
```

[slide]
# 观察者模式

<!-- 从观察者模式到监听器 -->
![](/web_file/observer.jpg)

- 定义对象间的一种一对多的依赖关系,当一个对象的状态发生改变时, 所有依赖于它的对象都得到通知并被自动更新。

[slide]
# Subject

```java
public abstract class Subject {
    /**
    * 用来保存注册的观察者对象
    */
    private List<Observer> list = new ArrayList<Observer>();
    /**
    * 注册观察者对象
    * @param observer 观察者对象
    */
    public void attach(Observer observer){
        list.add(observer);
        System.out.println("Attached an observer");
    }
    /**
    * 删除观察者对象
    * @param observer 观察者对象
    */
    public void detach(Observer observer){
        list.remove(observer);
    }
    /**
    * 通知所有注册的观察者对象
    */
    public void nodifyObservers(String newState){
        for(Observer observer : list){
            observer.update(newState);
        }
    }
}
```

[slide]
# ConcreteSubject

```java
public class ConcreteSubject extends Subject{
    private String state;
    public String getState() {
        return state;
    }
    public void change(String newState){
        state = newState;
        System.out.println("主题状态为：" + state);
        //状态发生改变，通知各个观察者
        this.nodifyObservers(state);
    }
}
```

[slide]
# Observer

```java
public interface Observer {
    /**
    * 更新接口
    * @param state 更新的状态
    */
    public void update(String state);
}
```

[slide]
# ConcreteObserver

```java
public class ConcreteObserver implements Observer {
    //观察者的状态
    private String observerState;
    @Override
    public void update(String state) {
        /**
        * 更新观察者的状态，使其与目标的状态保持一致
        */
        observerState = state;
        System.out.println("状态为："+observerState);
    }
}
```

[slide]
# Client

```java
public class Client {
    public static void main(String[] args) {
        //创建主题对象
        ConcreteSubject subject = new ConcreteSubject();
        //创建观察者对象
        Observer observer = new ConcreteObserver();
        //将观察者对象登记到主题对象上
        subject.attach(observer);
        //改变主题对象的状态
        subject.change("new state");
    }
}
```

<!-- 默认Listener Start -->
[slide]
# ServletContextListener接口

- [接口方法] contextInitialized()与 contextDestroyed()
- [接收事件] ServletContextEvent
- [触发场景] 在Container加载Web应用程序时（例如启动 Container之后），会呼叫contextInitialized()，而当容器移除Web应用程序时，会呼叫contextDestroyed ()方法。

[slide]
# ServletContextAttributeListener

- [接口方法] attributeAdded()、 attributeReplaced()、attributeRemoved()
- [接收事件] ServletContextAttributeEvent
- [触发场景] 若有对象加入为application（ServletContext）对象的属性，则会呼叫attributeAdded()，同理在置换属性与移除属性时，会分别呼叫attributeReplaced()、attributeRemoved()。

[slide]
# HttpSessionListener

- [接口方法] sessionCreated()与sessionDestroyed ()
- [接收事件] HttpSessionEvent
- [触发场景] 在session （HttpSession）对象建立或被消灭时，会分别呼叫这两个方法。

[slide]
# HttpSessionAttributeListener

- [接口方法] attributeAdded()、 attributeReplaced()、attributeRemoved()
- [接收事件] HttpSessionBindingEvent
- [触发场景] 若有对象加入为session（HttpSession）对象的属性，则会呼叫attributeAdded()，同理在置换属性与移除属性时，会分别呼叫attributeReplaced()、 attributeRemoved()。

[slide]
# HttpSessionActivationListener

- [接口方法] sessionDidActivate()与 sessionWillPassivate()
- [接收事件] HttpSessionEvent
- [触发场景] Activate与Passivate是用于置换对象的动作，当session对象为了资源利用或负载平衡等原因而必须暂时储存至硬盘或其它储存器时（透 过对象序列化），所作的动作称之为Passivate，而硬盘或储存器上的session对象重新加载JVM时所采的动作称之为Activate，所以容易理解的，sessionDidActivate()与 sessionWillPassivate()分别于Activeate后与将Passivate前呼叫。

[slide]
# ServletRequestListener

- [接口方法] requestInitialized()与 requestDestroyed()
- [接收事件] RequestEvent
- [触发场景] 在request（HttpServletRequest）对象建立或被消灭时，会分别呼叫这两个方法。

[slide]
# ServletRequestAttributeListener

- [接口方法] attributeAdded()、 attributeReplaced()、attributeRemoved()
- [接收事件] HttpSessionBindingEvent
- [触发场景] 若有对象加入为request（HttpServletRequest）对象的属性，则会呼叫attributeAdded()，同理在置换属性与移除属性时，会分别呼叫attributeReplaced()、attributeRemoved()。

[slide]
# HttpSessionBindingListener

- [接口方法] valueBound()与valueUnbound()
- [接收事件] HttpSessionBindingEvent
- [触发场景] 实现HttpSessionBindingListener接口的类别，其实例如果被加入至session（HttpSession）对象的属性中，则会 呼叫 valueBound()，如果被从session（HttpSession）对象的属性中移除，则会呼叫valueUnbound()，实现HttpSessionBindingListener接口的类别不需在web.xml中设定。
<!-- 默认Listener End -->

[slide]
# ContextLoaderListener

```java
public class ContextLoaderListener
                extends ContextLoader
                implements ServletContextListener{...}
```

- 启动Web容器时，自动装配ApplicationContext的配置信息

[slide]
# 处理请求

```Java
package com.focustech.hello;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class HelloServlet extends HttpServlet{

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{
      ...
  }
}
```

<!-- 提问：servlet生命周期？ -->

[slide]
# 如何将请求对象传递给业务方法?

- **反射**  {:&.moveIn}

<!-- 提问：如何反射? -->

[slide]
# 反射

- Class.forName()
- getClass().getClassLoader().loadClass()

<!-- 提问：两者的区别? -->

[slide]
# 两者区别

- Class.forName()装载的class已经被初始化
- getClass.getClassLoader().loadClass()装载的class还没有被link

[slide]
# 元编程

什么叫元编程？

- **编写程序的程序!** {:&.moveIn}

<!-- 3D打印机的例子 -->

[slide]
# 常见元编程技术

- Lisp的宏
- C的宏
- C++的Template
- ...

[slide]
# C的宏

```c
#define x 1+1

printf("x * x = %d\n",(x * x));
```

[slide]
# Lisp的宏

```clojure
(defmacro prt [x]
    `(println (* ~x ~x)))

(prt (+ 1 1) (+ 1 1))
```

[slide]
# Java元编程

- **注解**  {:&.moveIn}

[slide]
# 注解

- 注解为我们在代码中添加信息提供了一种形式化的方法,使我们可以在稍后某个时刻非常方便地使用这些数据.
- 注解可以提供用来完整的描述程序所需要的信息,而这些信息是无法用Java来表达的
- 注解本身并不做任何事情
- 需要注解处理程序,来对注解来进行处理

**特殊语法的文本**

[slide]
# 示例

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTest {
    public Class clz() default Object.class;
    public String value();
}
```

[slide]
# 元注解

```
@Target     表示该注解可以用于什么地方
            CONSTRUCTOR,FIELD,LOCAL_VARIABLE,METHOD,PACKAGE,PARAMETER,TYPE
@Retention  表示需要在什么级别保存注解信息 SOURCE,CLASS,RUNTIME
@Documented 将此注解包含在Javadoc中
@Inherited  允许子类继承父类的注解
```

[slide]
# 注解元素

- 基本类型
- String
- Class
- enum
- Annotation
- 及其数组

[slide]
# 注解的使用

```java
public class Dog {
    @MyTest("testShout")   //@MyTest(value="testShout")
    public String shout(){
        return "wawawa";
    }
}
```

[slide]
# 处理方法

```java
public static void main(String[] args) {
    Class clz = Class.forName("com.focus.ann.Dog");
    Dog dog = (Dog) clz.newInstance();
    Method[] methods = clz.getMethods();
    Method initMethod = null;
    for(Method m : methods){
        MyTest myTest = m.getAnnotation(MyTest.class);
        if(myTest != null){
            m.invoke(dog,new Object[]{});
        }
    }
}
```

[slide]
# Web中的注解

- @WebServlet
- @WebFilter
- @WebInitParam
- @WebListener
- @MultipartConfig:表示请求期望是 mime/multipart 类型

<!-- Java规范实现,服务器，数据转换，HttpServletRequest,HttpServletResponse结构。实现思考！数据结构VS数据对象! 20p -->

[slide]
# Servlet架构

![](/web_file/10.jpg)

[slide]
# 模块化

- OSGi
- Jigsaw

[slide]
# ClassLoader

![](/web_file/classloader.jpg)

[slide]
# 双亲委托模型

- 首先由最顶层的类加载器Bootstrap ClassLoader试图加载，如果没加载到，
- 则把任务转交给Extension ClassLoader试图加载，如果也没加载到，
- 则转交给App ClassLoader 进行加载，如果它也没有加载得到的话，
- 则返回给委托的发起者，由它到指定的文件系统或网络等URL中加载该类。
- 如果它们都没有加载到这个类时，则抛出ClassNotFoundException异常。
- 否则将这个找到的类生成一个类的定义，并将它加载到内存当中，最后返回这个类在内存中的Class实例对象

[slide]
# 为什么使用双亲委托模型

- **使用双亲委托模型主要是为了安全性.** {:&.moveIn}
- 如果不使用这种委托模式，那我们就可以随时使用自定义的Object来动态替代java核心api中定义的类型， 这样会存在非常大的安全隐患，而双亲委托的方式，就可以避免这种情况，因为Object已经在启动时就被引导类加载器（Bootstrcp ClassLoader） 加载，所以用户自定义的ClassLoader永远也无法加载一个自己写的Object，除非你改变JDK中ClassLoader搜索类的默认算法。

[slide]
# Class的唯一性判断

- JVM在判定两个class是否相同时，**不仅要判断两个类名是否相同，而且要判断是否由同一个类加载器实例加载的**。 只有两者同时满足的情况下，JVM才认为这两个class是相同的。就算两个class是同一份class字节码， 如果被两个不同的ClassLoader实例所加载，JVM也会认为它们是两个不同class。 {:&.moveIn}

[slide]
# 示例

```java
import com.sun.nio.zipfs.ZipFileStore;
public class Test {
    public static void main(String[] args) {
        System.out.println("1".getClass().getClassLoader());
        System.out.println(ZipFileStore.class.getClassLoader());
        System.out.println(A.class.getClassLoader());
    }
}
class A{}
```

[slide]
# 输出

```
null                          //BootStrap ClassLoader输出为null
sun.misc.Launcher$ExtClassLoader@3e10c986
sun.misc.Launcher$AppClassLoader@610f7612
```

[slide]
# 自定义ClassLoader

- 继承java.lang.ClassLoader
- 重写父类的findClass方法
- **如没有特殊的要求，一般不建议重写loadClass搜索类的算法**

[slide]
# Tomcat ClassLoader

Java Web服务器需要解决以下四个问题：

- 同一个Web服务器里，各个Web项目之间各自使用的Java类库要互相隔离。
- 同一个Web服务器里，各个Web项目之间可以提供共享的Java类库。
- 服务器为了不受Web项目的影响，应该使服务器的类库与应用程序的类库互相独立。
- 对于支持JSP的Web服务器，应该支持热插拔（hotswap）功能。

[slide]
# Tomcat5

![](/web_file/tomcat5.jpg)


[slide]
# Tomcat6

![](/web_file/tomcat6.jpg)


[slide]
# Tomcat7

![](/web_file/tomcat7.png)

[slide]
# 对比

- Tomcat5,6,7的ClassLoader结构略有不同。
- Tomcat6中将CommonClassLoader,CatalinaClassLoader,SharedClassLoader合并，就一个CommonClassLoader.
- Tomcat7中没有ExtensionClassLoader.

[slide]
# 资源隔离

从上图可以看出，为了解决jar隔离和共享的问题。对于每个webapp,Tomcat都会创建一个WebAppClassLoader来加载应用，这样就保证了每个应用加载进来的class都是不同的(因为ClassLoader不同)! 而共享的jar放在tomcat-home/lib目录下，由CommonClassLoader来加载。通过双亲委托模式，提供给其下的所有webapp共享

[slide]
# 特别说明

**对于WebAppClassLoader是违反双亲委托模型的。如果加载的是jre或者servlet api则依然是双亲委托模型。 而如果不是的话，则会先尝试自行加载，如果找不到再委托父加载器加载。 这个应该是可以理解的，因为应用的class是由WebAppClassLoader加载的，是项目私有的**

[slide]
# web模块

- 编写一个类继承自Servlet，将该类打成JAR包，并且在JAR包的META-INF目录下放置一个 web-fragment.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-fragment
    xmlns=http://java.sun.com/xml/ns/javaee
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="3.0"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    http://java.sun.com/xml/ns/javaee/web-fragment_3_0.xsd"
    metadata-complete="true">
    <servlet>
        <servlet-name>fragment</servlet-name>
        <servlet-class>footmark.servlet.FragmentServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>fragment</servlet-name>
        <url-pattern>/fragment</url-pattern>
    </servlet-mapping>
</web-fragment>
```

[slide]
# OSGi模块化

- OSGi通过自定义ClassLoader实现包级别的访问权限控制
- 通过自定义ClassLoader实现多版本控制(一个bundle可以发布多个版本，而jar不能多版本共存)
- 可独立部署和卸载

[slide]
# OSGi ClassLoader

![](/web_file/OSGi_ClassLoader.png)

[slide]
# Cookie与Session

- Cookies是存储在客户端计算机上的文本文件，并保留了各种信息。
- 当下一次浏览器向 Web 服务器发送任何请求时，浏览器会把这些 Cookies 信息发送到服务器，服务器将使用这些信息来识别用户。
- 而Session通过在服务器端记录信息确定用户身份。

[slide]
# 新增Cookie

```java
Cookie cookie = new Cookie("username","admin");   // 新建Cookie
cookie.setMaxAge(Integer.MAX_VALUE);           // 设置生命周期为MAX_VALUE
cookie.setDomain(domain);
cookie.setPath("/");
response.addCookie(cookie);                    // 输出到客户端
```

[slide]
# 获取Cookie

```java
Cookie cookies = request.getCookies();
for (int i = 0; i < cookies.length; i++){
  cookie = cookies[i];
  System.out.print("名称：" + cookie.getName( ) + "，");
  System.out.print("值：" + cookie.getValue( )+" <br/>");
}
```

[slide]
# Session操作

```java
// 如果不存在 session 会话，则创建一个 session 对象
HttpSession session = request.getSession(true);
session.setAttribute(key, value);
session.setMaxInactiveInterval(30*60);//30分钟超时
```

[slide]
# 浏览器关闭Cookie

- 实际上使用Session时，还是会使用Cookie {:&.moveIn}
- 会在响应的头部新增一个Set-Cookie头保存SessionID
- 当再次请求时，请求会新增Cookie头，内容为SessionID，发送给服务器来获得相应的会话
- 如果浏览器关闭了Cookie，如果使用Session，则需要对url进行encode.
- response.encodeURL("...")

<!-- Servlet流程,基于XML,基于注解，注解讲解 30p -->

[slide]
# JDBC

```java
try{
   String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
   Class.forName("com.mysql.jdbc.Driver"); //这是干嘛用的?
   //Class.forName("org.gjt.mm.mysql.Driver");//和上面的区别？
   Connection conn = DriverManager.getConnection(url,"root","root");
   Statement stmt = conn.createStatement();
   String sql = "insert into articles (title,content) values ('"
                    + title + "','" + content + "');";
   stmt.executeUpdate(sql);
   conn.close();
}catch(Exception e){
    System.out.println("error");
}
```

[slide]
# JDBC API

- DriverManager: 这个类管理数据库驱动程序的列表。从Java应用程序的连接请求匹配的合适的数据库驱动程序，使用通讯子协议。第一个JDBC驱动程序识别某个子协议将被用来建立一个数据库连接。
- Driver: 此接口处理与数据库服务器的通信。将直接与驱动程序对象很少。相反，您可以使用DriverManager隔离对象，这种类型的管理对象。它也抽象与驱动程序对象与工作相关的细节
- Connection : 此接口与用于接触一个数据库的所有方法。连接对象通信的情况下，即，所有的通信是只通过与数据库连接对象。
- Statement : 使用接口提交到数据库的SQL语句创建的对象。一些派生的接口接受，除了执行存储过程的参数。
- ResultSet: 这些对象保存后，使用Statement对象执行SQL查询从数据库中检索数据。它作为一个迭代器，让您可以通过它的数据移动。
- SQLException: 这个类处理的数据库应用程序中发生的任何错误。

[slide]
# JDBC架构

![](/web_file/db01.gif)

[slide]
# Class.forName作用

- 将Driver自身，添加到DriverManager列表中

[slide]
# com.mysql.jdbc.Driver

```java
public class Driver extends NonRegisteringDriver
    implements java.sql.Driver {
    ...
    static {
        DriverManager.registerDriver(new Driver());
    }
}
```

[slide]
# static关键字

- static标示的属性或方法,是类属性或方法
- static标示的属性或方法直接使用类来调用
- 非static标示的属性或方法,是对象属性或方法
- 在非静态方法中可以访问静态方法
- 在静态方法中不可以调用非静态方法
- 可以使用this来调用静态方法或静态属性

[slide]
# 类加载顺序一

```java
public class LoadTest1 {
    int num = 3;
    int str = prt(num);
    static int static_str = sprt("初始化static_str");

    {System.out.println("LoadTest1:初始化块");}

    static{
        System.out.println("LoadTest1:静态初始化块");
    }

    public LoadTest1(){
        System.out.println("LoadTest1:默认构造方法 " + getNum());
    }

    public int prt(int num){
        System.out.println("LoadTest1: " + num);
        return 0;
    }

    public static int sprt(String text){
        System.out.println("LoadTest1: " + text);
        return 0;
    }

    public int getNum() { return num; }

    public static void main(String[] args){
        LoadTest1 loadTest = new LoadTest1();
        LoadTest1 loadTest1 = new LoadTest1();
    }
}
```

[slide]
# 结果

```
LoadTest1: 初始化static_str
LoadTest1:静态初始化块
LoadTest1: 3
LoadTest1:初始化块
LoadTest1:默认构造方法 3

LoadTest1: 3
LoadTest1:初始化块
LoadTest1:默认构造方法 3
```

[slide]
# 类加载顺序二

```java
public class LoadTest2 extends LoadTest1 {
    int num = 5;
    int str = prt(num);
    private static int static_str = sprt("初始化static_str");

    {System.out.println("LoadTest2:初始化块");}

    static{
        System.out.println("LoadTest2:静态初始化块");
    }

    public LoadTest2(){
        System.out.println("LoadTest2:默认构造方法 " + getNum());
    }

    public int prt(int num){
        System.out.println("LoadTest2:" + num);
        return 0;
    }

    public static int sprt(String text){
        System.out.println("LoadTest2:" + text);
        return 0;
    }

    public int getNum() { return num; }

    public static void main(String[] args){
        LoadTest1 loadTest = new LoadTest2();
    }
}
```

[slide]
# 结果

```
LoadTest1: 初始化static_str
LoadTest1:静态初始化块
LoadTest2:初始化static_str
LoadTest2:静态初始化块
LoadTest2:3           ???
LoadTest1:初始化块
LoadTest1:默认构造方法 0       ???
LoadTest2:5
LoadTest2:初始化块
LoadTest2:默认构造方法 5
```

[slide]
# org.gjt.mm.mysql.Driver

```java
public class Driver extends com.mysql.jdbc.Driver{
    ...
}
```

[slide]
# SPI

- Service Provider Interface
- 为某个接口寻找服务实现的机制

```
在面向的对象里，我们一般推荐基于接口编程。方便替换实现。为了实现可配置化，Java提供了SPI
```

[slide]
# 示例

```java
package com.focustech.search;

public interface Search {  
   public List search(String keyword);  
}
```

[slide]
# 示例

```java
package com.focustech.search.impl;

public interface SearchImplA {  
   public List search(String keyword){
       ...
   }  
}
```

```java
package com.focustech.search.impl;

public interface SearchImplB {  
   public List search(String keyword){
       ...
   }  
}
```

[slide]
# IOC

```java
public class Caller{
    @Autowired
    private Search search;

    public void call(){
        ...
        search.search("keyword");
        ...
    }
}
```

[slide]
# ServiceLoader

- SearchImplA所属jar包添加**META-INF/services/com.focustech.search.Search**文件

```
com.focustech.search.impl.SearchImplA
```

- SearchImplB所属jar包添加**META-INF/services/com.focustech.search.Search**文件

```
com.focustech.search.impl.SearchImplB
```

[slide]
# ServiceLoader调用

```java
public class Caller{
    private Search search;

    public Caller(){
        ServiceLoader<Search> serviceLoader = ServiceLoader.load(Search.class);  
        Iterator<Search> searchs = serviceLoader.iterator();  
        if (searchs.hasNext()) {  
            search = searchs.next();  
        }
    }

    public void call(){
        ...
        search.search("keyword");
        ...
    }
}
```


[slide]
# ServiceLoader在JDBC中的应用

- DriverManager

```
static {
    loadInitialDrivers();
    println("JDBC DriverManager initialized");
}

private static void loadInitialDrivers() {
    ...
    ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
    Iterator<Driver> driversIterator = loadedDrivers.iterator();
    try{
        while(driversIterator.hasNext()) {
            driversIterator.next();
        }
    } catch(Throwable t) {
    // Do nothing
    }
    ...
}
```

[slide]
# Resource

```xml
<Resource name="jndi/mybatis"   
                auth="Container"   
                type="javax.sql.DataSource"   
                driverClassName="com.mysql.jdbc.Driver"   
                url="jdbc:mysql://localhost:3306/appdb"   
                username="root"   
                password="123456"   
                maxActive="20"   
                maxIdle="10"   
                maxWait="10000"/>     
```

[slide]
# 获取

```java
public void testJNDI() {  
    ...
    DataSource ds = (DataSource)    
                context.lookup("java:comp/env/jndi/mybatis");  
    Connection conn = ds.getConnection();  
    ...
}  
```

[slide]
# JNDI

- JNDI:Java Naming and Directory Interface
- 通过指定的名称查找对象或者数据

[slide]
# 架构

![](/web_file/jndiarch.gif)

[slide]
# JNDI API

- javax.naming
- javax.naming.directory
- javax.naming.ldap
- javax.naming.event
- javax.naming.spi

<!-- 数据持久化处理!JDBC流程解析，实现! 20P -->

[slide]
# 返回响应

```
HTTP/1.1 200 OK
Date: Sat, 31 Dec 2005 23:59:59 GMT
Content-Type: text/html;charset=ISO-8859-1
Content-Length: 122

＜html＞
＜head＞
＜title＞hello＜/title＞
＜/head＞
＜body＞
＜!-- body content --＞
＜/body＞
＜/html＞
```

- 状态行
- 消息报头
- 响应正文

[slide]
# HttpServletResponse

```
addCookie(Cookie): void
containsHeader(String): boolean
encodeURL(String): String
encodeRedirectURL(String): String
sendError(int, String): void
sendError(int): void
sendRedirect(String): void
setHeader(String, String): void
addHeader(String, String): void
setStatus(int): void
...
```

[slide]
# 回写响应

```Java
public void doGet(HttpServletRequest request,
                HttpServletResponse response)
throws ServletException, IOException{
  PrintWriter out = response.getWriter();
  out.println("Hello");
}
```

[slide]
# Format Body

- json
- 模板

<!-- 返回响应 10p-->

[slide]
# 客户端接收

- 浏览器渲染
- HTML
- JavaScript
- CSS

<!-- 吕翔讲解 -->

[slide]
<!-- 整体流程 -->
![](/web_file/02.jpg)

[slide]
# 考试

<!-- 针对Web的理解，可以以任意形式总结给我，一起讨论! -->

[slide]
# 谢谢
