% Servlet
% 王一帆
% 江苏企业大学

## 上节回顾

- 使用ServerSocket实现了静态服务器
- 使用接口+反射,实现动态服务器
- 引入xml,对url与bean进行配置
- Tomcat介绍

## 谁来定义接口?

- DynamicBean
- xml格式

## 规范

- Servlet&JSP规范

## Servlet简介

Java Servlet是运行在Web服务器或应用服务器上的程序它是作为来自Web浏览器或其他HTTP客户端的请求和HTTP服务器上的数据库或应用程序之间的中间层。

使用Servlet,你可以收集来自网页表单的用户输入，呈现来自数据库或者其他源的记录，还可以动态创建网页。

## 优势

Java Servlet通常情况下与使用 CGI（Common Gateway Interface，公共网关接口）实现的程序可以达到异曲同工的效果。但是相比于CGI，Servlet有以下几点优势：

- 性能明显更好。
- Servlet基于线程运行
- Servlet是独立于平台的，因为它们是用Java编写的。
- 服务器上的Java安全管理器执行了一系列限制，以保护服务器计算机上的资源。因此，Servlet是可信的。
- Java类库的全部功能对Servlet来说都是可用的。

## 第一个Servlet程序

- 在server的webapps目录下新建目录hello
- 在hello目录下新建目录WEB-INF
- 在WEB-INF下新建文件web.xml以及目录classes
- 在classes下新建类com.test.HelloServlet

## HelloServlet

```Java
package com.test;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class HelloServlet extends HttpServlet{

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{
      PrintWriter out = response.getWriter();
      out.println("Hello");
  }
}
```

## web.xml

```xml
  <servlet>
    <servlet-name>helloServlet</servlet-name>
    <servlet-class>com.test.HelloServlet</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>helloServlet</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>
```

## 访问

- 启动服务器
- 访问 http://localhost:8080/hello/hello

## 发生了什么?

- 服务器启动后,默认在8080端口监听
- 访问url,即访问服务器
- 第一个hello是应用名称
- 第二个hello是web.xml中配置的url-pattern

## Servlet生命周期

- Servlet通过调用init()方法进行初始化。
- Servlet调用service()方法来处理客户端的请求。
- Servlet通过调用destroy()方法终止（结束）。
- 最后，Servlet是由JVM的垃圾回收器进行垃圾回收的。

## init()方法

init方法被设计成只调用一次。它在第一次创建Servlet时被调用，在后续每次用户请求时不再调用。因此，它是用于一次性初始化.

Servlet创建于用户第一次调用对应于该Servlet的URL时，但是也可以指定Servlet在服务器第一次启动时被加载。

当用户调用一个Servlet时，就会创建一个Servlet实例，每一个用户请求都会产生一个新的线程，适当的时候移交给doGet或doPost方法。init()方法简单地创建或加载一些数据，这些数据将被用于Servlet的整个生命周期。

## init方法的定义

```java
public void init() throws ServletException {
  // 初始化代码...
}
```

## 获取初始化参数

```xml
  <servlet>
    <servlet-name>helloServlet</servlet-name>
    <servlet-class>com.test.HelloServlet</servlet-class>
    <init-param>
      <param-name>data</param-name>
      <param-value>123</param-value>
    </init-param>
  </servlet>

```

## 获取

```java
public void init() throws ServletException {
  //getServletConfig().getInitParameter("data");
}
```

## service()方法

service()方法是执行实际任务的主要方法。Servlet容器（即 Web 服务器）调用service()方法来处理来自客户端（浏览器）的请求，并把格式化的响应写回给客户端。

每次服务器接收到一个Servlet请求时，服务器会产生一个新的线程并调用服务。service()方法检查HTTP请求类型（GET、POST、PUT、DELETE等），并在适当的时候调用doGet、doPost、doPut，doDelete等方法。

## service方法的

```java
public void service(ServletRequest request,
                    ServletResponse response)
      throws ServletException, IOException{
}
```

## 不需要对service做处理

service()方法由容器调用，service方法在适当的时候调用doGet、doPost、doPut、doDelete等方法。所以，你不用对service()方法做任何动作，你只需要根据来自客户端的请求类型来重载doGet()或doPost()即可。

doGet() 和 doPost() 方法是每次服务请求中最常用的方法。下面是这两种方法的特征。

## doGet() 方法

GET请求来自于一个URL的正常请求，或者来自于一个未指定METHOD的HTML表单，它由doGet()方法处理。

```Java
public void doGet(HttpServletRequest request,
                  HttpServletResponse response)
    throws ServletException, IOException {
    // Servlet 代码
}
```

## doPost()方法

POST请求来自于一个特别指定了METHOD为POST的HTML表单，它由doPost()方法处理。

```java
public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
    // Servlet 代码
}
```

## destroy()方法

destroy()方法只会被调用一次，在Servlet生命周期结束时被调用。destroy()方法可以让你的Servlet关闭数据库连接、停止后台线程、把Cookie列表或点击计数器写入到磁盘，并执行其他类似的清理活动。

在调用destroy()方法之后，servlet对象被标记为垃圾回收。destroy方法定义如下所示：

```Java
  public void destroy() {
    // 终止化代码...
  }
```

## 架构图
![]({{site.CDN_PATH}}/home/ivan/my/teach/javaee/file/02.jpg)

## 示例
```java
public class HelloServlet extends HttpServlet{

   public void init(){
     System.out.println("init");
   }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{
      System.out.println("doGet");
  }

  public void destroy(){
      System.out.println("destroy");
  }
}
```

## 来做个博客吧

功能点:

- 管理员发布,修改,删除文章
- 用户发表评论
- 展示文章和评论

## 用户表

. . .

```sql
CREATE TABLE `users` (
  `rec_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `type` int(2) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

```

## 文章表

. . .

```sql
CREATE TABLE `articles` (
  `rec_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `status` int(2) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

```

## 评论表

. . .

```sql
CREATE TABLE `posts` (
  `rec_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `art_id` bigint(11) DEFAULT NULL,
  `user_id` bigint(11) DEFAULT NULL,
  `content` text NOT NULL,
  `status` int(2) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

```

## 发布文章

- 管理员输入标题,内容
- 点击提交按钮,保存

## 展示页面

```java
package com.blog.article;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class NewServlet extends HttpServlet{

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{
     response.setContentType("text/html");
     response.setCharacterEncoding("utf-8");

     PrintWriter out = response.getWriter();
     out.println("<form action='/hello/article/create' method='post'>");
     out.println("标题:<input type='text' name='title'/>");
     out.println("内容:<textarea name='content'></textarea>");
     out.println("<input type='submit' value='提交'/>");
     out.println("</form>");
  }
}
```

## 提交

```java
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{
     ...
  }
```

## 如何获取表单数据

- getParameter()：调用 request.getParameter()方法来获取表单参数的值。
- getParameterValues()：如果参数出现一次以上，则调用该方法，并返回多个值，例如复选框。
- getParameterNames()：如果想要得到当前请求中的所有参数的完整列表，则调用该方法。

## 代码

```Java
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{
     String title = request.getParameter("title");
     String content = request.getParameter("content");

     ...
  }
```

## 保存数据

```java
  try{
       String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
       Class.forName("com.mysql.jdbc.Driver");
       Connection conn = DriverManager.getConnection(url,"root","root");
       Statement stmt = conn.createStatement();
       String sql = "insert into articles (title,content) values ('" + title + "','" + content + "');";
       stmt.executeUpdate(sql);
       conn.close();
     }catch(Exception e){
       System.out.println("error");
     }
```

## 上传文件

```html
<form action="..." enctype="multipart/form-data"
            method="post"  >
<input type="file" name="file"  />
</form>
```

## 获取文件

```java
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request instanceof MultiPartRequestWrapper){
            MultiPartRequestWrapper mp=(MultiPartRequestWrapper)request;
            Enumeration<String> multi=mp.getFileParameterNames();
            String formFileTagName=null;
            for( ;multi.hasMoreElements();){
                String element=multi.nextElement();
                formFileTagName=element;
                break;
            }
            String fileName=mp.getFileNames(formFileTagName)[0];

            File[]files=mp.getFiles(formFileTagName);
            File uploadFile=files[0];
        }
    }

```

## 页面跳转

- 发布文章后要展示最新发布的文章

## 重定向与请求转发

```java
request.getRequestDispatcher("/new").forward(request, response);//转发到new

response.sendRedirect("/new");//重定向到new
```

## 请求转发

1. 客户浏览器发送http请求
2. web服务器接受此请求
3. 调用内部的一个方法在容器内部完成请求处理和转发动作
4. 将目标资源发送给客户
- 在这里，转发的路径必须是同一个web容器下的url，其不能转向到其他的web路径上去，中间传递的是自己的容器内的request。在客户浏览器路径栏显示的仍然是其第一次访问的路径，也就是说客户是感觉不到服务器做了转发的。转发行为是浏览器只做了一次访问请求。

## 重定向

1. 客户浏览器发送http请求
2. web服务器接受后发送302状态码响应及对应新的location给客户浏览器
3. 客户浏览器发现是302响应，则自动再发送一个新的http请求，请求url是新的location地址
4. 服务器根据此请求寻找资源并发送给客户。
- 在这里location可以重定向到任意URL，既然是浏览器重新发出了请求，则就没有什么request传递的概念了。在客户浏览器路径栏显示的是其重定向的路径，客户可以观察到地址的变化的。重定向行为是浏览器做了至少两次的访问请求的。

## 权限控制

- 发布文章只能管理员才能操作
- 如果其他人操作则显示无权限

## 代码

```java
public class NewServlet extends HttpServlet{

  public boolean isAdmin(){
    return false;
  }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{

    ...

     if(!isAdmin()){
       System.out.println("没有权限!");
       return;
     }

     ...
  }


  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
   throws ServletException, IOException{

    ...

     if(!isAdmin()){
       System.out.println("没有权限!");
       return;
     }

     ...
  }
}
```

## 重复代码

- 在每个需要权限控制的地方都需要如上的代码,非常的繁琐
- 是否有统一的处理方案?

## filter

- 在客户端的请求访问后端资源之前，拦截这些请求。
- 在服务器的响应发送回客户端之前，处理这些响应。

## filter的用途

- 身份验证过滤器（Authentication Filters）。
- 数据压缩过滤器（Data compression Filters）。
- 加密过滤器（Encryption Filters）。
- 触发资源访问事件过滤器。
- 图像转换过滤器（Image Conversion Filters）。
- 日志记录和审核过滤器（Logging and Auditing Filters）。
- MIME-TYPE 链过滤器（MIME-TYPE Chain Filters）。
- 标记化过滤器（Tokenizing Filters）。
- XSL/T 过滤器（XSL/T Filters），转换 XML 内容。

## filter的方法

- public void doFilter (ServletRequest, ServletResponse, FilterChain)
    该方法在每次一个请求/响应对因客户端在链的末端请求资源而通过链传递时由容器调用。
- public void init(FilterConfig filterConfig)
    该方法由 Web 容器调用，指示一个过滤器被放入服务。
- public void destroy()
    该方法由 Web 容器调用，指示一个过滤器被取出服务。

## 编写身份验证filter

```java
  ...
    public void  doFilter(ServletRequest request,
                 ServletResponse response,
                 FilterChain chain)
                 throws java.io.IOException, ServletException {

     PrintWriter out = response.getWriter();

     if(isAdmin()){
       System.out.println("没有权限!");
       return;
     }

      chain.doFilter(request,response);
    }
  ...
```

## 配置

```XML
  <filter>
      <filter-name>AuthFilter</filter-name>
      <filter-class>com.blog.filter.AuthFilter</filter-class>
  </filter>

  <filter-mapping>
      <filter-name>AuthFilter</filter-name>
      <url-pattern>/*</url-pattern>
  </filter-mapping>
```

## 其它配置

```XML
  <filter>
      <filter-name>AuthFilter</filter-name>
      <filter-class>com.blog.filter.AuthFilter</filter-class>
  </filter>

  <filter-mapping>
      <filter-name>AuthFilter</filter-name>
      <servlet-name>*Servlet</servlet-name>
      <dispatcher>REQUEST</dispatcher>
  </filter-mapping>
```

## dispatcher

- REQUEST:对请求生效(默认)
- FORWARD:对请求分派forward生效
- INCLUDE:对请求分派include生效(一般不用)
- ERROR:对错误处理请求生效

## 访问正常吗?

. . .

- 出现乱码了!!

. . .

- filter先于servlet执行,未设置编码!

## 添加encodingFilter

```java
    public void  doFilter(ServletRequest request,
                 ServletResponse response,
                 FilterChain chain)
                 throws java.io.IOException, ServletException {

     response.setCharacterEncoding("utf-8");

      chain.doFilter(request,response);
    }
```

## web.xml

```xml
  <filter>
      <filter-name>EncodeFilter</filter-name>
      <filter-class>com.blog.filter.EncodeFilter</filter-class>
  </filter>

  <filter-mapping>
      <filter-name>EncodeFilter</filter-name>
      <url-pattern>/*</url-pattern>
  </filter-mapping>
```

## 注意

- EncodeFilter一定要在AuthFilter的前面

## Filter顺序

- 将 filter-mapping 元素包含与请求匹配的 url-pattern的筛选器按其在 web.xml 部署描述符中出现的顺序添加到链中。
- 将 filter-mapping 元素包含与请求匹配的 servlet-name 的筛选器添加在链中与 URL 模式匹配的筛选器之后。
- 链上先进先出的，链中最后的项目往往是最初请求的资源。


## 示例

```
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

## 答案

- /a/s.do      1   5
- /a/b.do      1   5   2
- /a/d/e.do    1   5   4
- /b.do        5
- /a/c/p.do    1   3   5


## 完善isAdmin

- HTTP 是一种"无状态"协议，这意味着每次客户端检索网页时，客户端打开一个单独的连接到 Web 服务器，服务器会自动不保留之前客户端请求的任何记录。

. . .

- 如何判断用户是管理员?

## Cookie与Session

- Cookies是存储在客户端计算机上的文本文件，并保留了各种信息。
- 当下一次浏览器向 Web 服务器发送任何请求时，浏览器会把这些 Cookies 信息发送到服务器，服务器将使用这些信息来识别用户。
- 而Session通过在服务器端记录信息确定用户身份。

## 新增Cookie

```java
Cookie cookie = new Cookie("username","admin");   // 新建Cookie
cookie.setMaxAge(Integer.MAX_VALUE);           // 设置生命周期为MAX_VALUE
response.addCookie(cookie);                    // 输出到客户端
```

## 获取Cookie

```java
Cookie cookies = request.getCookies();
for (int i = 0; i < cookies.length; i++){
  cookie = cookies[i];
  System.out.print("名称：" + cookie.getName( ) + "，");
  System.out.print("值：" + cookie.getValue( )+" <br/>");
}
```

## Session操作

```java
// 如果不存在 session 会话，则创建一个 session 对象
HttpSession session = request.getSession(true);
session.setAttribute(key, value);
session.setMaxInactiveInterval(30*60);//30分钟超时
```

## 浏览器关闭Cookie

- 实际上使用Session时，还是会使用Cookie
- 会在响应的头部新增一个Set-Cookie头保存SessionID
- 当再次请求时，请求会新增Cookie头，内容为SessionID，发送给服务器来获得相应的会话
- 如果浏览器关闭了Cookie，如果使用Session，则需要对url进行encode.
- response.encodeURL("...")

## 课堂作业

- 编写代码，实现基于Cookie的用户登录

## servlet中的作用范围

- 应用:getServletContext.setAttribute
- 请求:request.setAttribute
- 会话:request.getSession.setAttribute

## 上下文初始化参数

```xml
<context-param>
  <param-name>data</param-name>
  <param-value>456</param-value>
</context-param>
```

## 获取

```java
getServletContext().getInitParameter("data");
```

## ServletConfig与ServletContext的区别?

- 从web.xml配置中是否能看出其区别?

## listener

## ServletContextListener接口

- [接口方法] contextInitialized()与 contextDestroyed()
- [接收事件] ServletContextEvent
- [触发场景] 在Container加载Web应用程序时（例如启动 Container之后），会呼叫contextInitialized()，而当容器移除Web应用程序时，会呼叫contextDestroyed ()方法。

## ServletContextAttributeListener

- [接口方法] attributeAdded()、 attributeReplaced()、attributeRemoved()
- [接收事件] ServletContextAttributeEvent
- [触发场景] 若有对象加入为application（ServletContext）对象的属性，则会呼叫attributeAdded()，同理在置换属性与移除属性时，会分别呼叫attributeReplaced()、attributeRemoved()。

## HttpSessionListener

- [接口方法] sessionCreated()与sessionDestroyed ()
- [接收事件] HttpSessionEvent
- [触发场景] 在session （HttpSession）对象建立或被消灭时，会分别呼叫这两个方法。

## HttpSessionAttributeListener

- [接口方法] attributeAdded()、 attributeReplaced()、attributeRemoved()
- [接收事件] HttpSessionBindingEvent
- [触发场景] 若有对象加入为session（HttpSession）对象的属性，则会呼叫attributeAdded()，同理在置换属性与移除属性时，会分别呼叫attributeReplaced()、 attributeRemoved()。

## HttpSessionActivationListener

- [接口方法] sessionDidActivate()与 sessionWillPassivate()
- [接收事件] HttpSessionEvent
- [触发场景] Activate与Passivate是用于置换对象的动作，当session对象为了资源利用或负载平衡等原因而必须暂时储存至硬盘或其它储存器时（透 过对象序列化），所作的动作称之为Passivate，而硬盘或储存器上的session对象重新加载JVM时所采的动作称之为Activate，所以容易理解的，sessionDidActivate()与 sessionWillPassivate()分别于Activeate后与将Passivate前呼叫。

## ServletRequestListener

- [接口方法] requestInitialized()与 requestDestroyed()
- [接收事件] RequestEvent
- [触发场景] 在request（HttpServletRequest）对象建立或被消灭时，会分别呼叫这两个方法。
## ServletRequestAttributeListener

- [接口方法] attributeAdded()、 attributeReplaced()、attributeRemoved()
- [接收事件] HttpSessionBindingEvent
- [触发场景] 若有对象加入为request（HttpServletRequest）对象的属性，则会呼叫attributeAdded()，同理在置换属性与移除属性时，会分别呼叫attributeReplaced()、attributeRemoved()。

## HttpSessionBindingListener

- [接口方法] valueBound()与valueUnbound()
- [接收事件] HttpSessionBindingEvent
- [触发场景] 实现HttpSessionBindingListener接口的类别，其实例如果被加入至session（HttpSession）对象的属性中，则会 呼叫 valueBound()，如果被从session（HttpSession）对象的属性中移除，则会呼叫valueUnbound()，实现HttpSessionBindingListener接口的类别不需在web.xml中设定。

## 作业

- 实现基于Session的用户登录，比较与Cookie的优缺点
- 完善博客功能

