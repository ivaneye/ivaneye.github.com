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

[silde]
# 确认资源

- 要确认资源，需要明确资源所在范围
- Java中（包名+类名+[方法名，属性名]）

[silde]
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
A URI is a uniform resource <i>identifier</i> while a URL is a uniform
resource <i>locator</i>.  Hence every URL is a URI, abstractly speaking, but
not every URI is a URL.  This is because there is another subcategory of
URIs, uniform resource <i>names</i> (URNs), which name resources but do not
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

- 字符串？
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

[slide]
# 面向对象VS函数式

- 定义一组对象，维护对象之间的关系
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

[slide]
# Java8 lambda表达式

[slide]
# Server

- Tomcat
- Jetty
- Resin

<!-- server处理 -->

# 处理HTTP请求

- 业务处理

[slide]
#
<!-- Java规范实现,服务器，数据转换，HttpServletRequest,HttpServletResponse结构。实现思考！数据结构VS数据对象! 20p -->

[slide]
# Servlet架构

![](/web_file/10.jpg)

[slide]
# Session

[slide]
# Filter

[slide]
# Listener

[slide]
# 注解

[slide]
# web.xml

[slide]
# web模块

[slide]
# 请求分派

[slide]
# Web应用

[slide]
# 应用声明周期
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
<!-- 数据持久化处理!JDBC流程解析，实现! 20P -->

[slide]
# 返回响应

[slide]
# Response

<!-- 返回响应 10p-->

[slide]
#
<!-- HTML,CSS,JS 展示数据 10p -->

[slide]
# 谢谢
