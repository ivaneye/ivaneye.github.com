title: Web
speaker: 王一帆
url: http://www.ivaneye.com
transition: stick
theme: dark

[slide]
# Web
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

- 服务器读取HTTP请求中的内容，在经过解析主机，解析站点名称，解析访问资源后，会查找相关资源，如果查找成功，则返回状态码200，失败就会返回大名鼎鼎的404，在服务器监测到请求不存在的资源后，可以按照程序员设置的跳转到别的页面。所以有各种各样的404错误页面。

[slide]
# 服务器返回HTTP响应

- 服务器会将请求的资源封装成http响应
- 浏览器得到返回数据后可以会提取数据，然后调用解析内核进行翻译，最后显示出页面。
- 之后浏览器会对其引用的文件比如图片，CSS，JS等文件不断进行上述过程，直到所有文件都被下载下来之后，网页就会显示出来。

[slide]
<!-- 整体流程 -->
![](/web_file/02.jpg)

<!-- 访问网站流程,阐述数据在各个流程里的角色，确认重要性 10P -->

[slide]
# 流程划分

- 如何查找，定位资源 {:&.fadeIn}
- 如何发送请求
- 如何处理请求
- 如何返回响应
- 如何展示

[slide]
# 查找，定位资源

[slide]
# URI/URL/URN

- URI:Uniform Resource Identifier
- URL:Uniform Resource Locators
- URN:Uniform Resource Name

[slide]
# 区别

- URI:按照规范定义的、可以唯一标识某一资源的字符串[RFC3986]
    - URI = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
    - hier-part =
         - "//" authority path-abempty
         - / path-absolute
         - / path-rootless
         - / path-empty
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
BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
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
# TCP/IP

[slide]
# OSI七层模型

<!-- 类似JavaIO的装饰模式 -->

[slide]
# 各种中间环节

- DNS
- 代理
- 反向代理
- 路由器
- 中继器
- 防火墙
- CDN

<!-- 如何发送数据，HTTP协议,TCP/IP.OSI七层模型，20P -->

[slide]
# 处理请求

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

![](/web_file/10.jpg)

[slide]
# Reactor多线程模型

![](/web_file/11.jpg)

[slide]
# 主从Reactor模型

![](/web_file/12.jpg)

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

[slide]
# 如何看待请求?

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

- 字符串？
- 数据？
- 对象？

[slide]
# 把请求看作数据

- clojure示例
- golang示例

[slide]
# 把请求看作对象

- HttpServletRequest

<!-- Java处理HTTP,Socket编程,BIO,NIO,Thread,Reactor，30p -->

[slide]
#
<!-- Java规范实现,服务器，数据转换，HttpServletRequest,HttpServletResponse结构。实现思考！数据结构VS数据对象! 20p -->

[slide]
#
<!-- Servlet流程,基于XML,基于注解，注解讲解 30p -->

[slide]
# JDBC

```java
try{
   String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
   Class.forName("com.mysql.jdbc.Driver"); //这是干嘛用的?
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
# JDBC架构

[slide]
# ServiceLoader

<!-- 数据持久化处理!JDBC流程解析，实现! 20P -->

[slide]
# 返回响应

[slide]
# Response

[slide]
#
<!-- HTML,CSS,JS 展示数据 10p -->

[slide]
# 谢谢
