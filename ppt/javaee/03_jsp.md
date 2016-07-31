% JSP
% 王一帆
% 江苏企业大学

## Servlet的问题

- 页面输出太过繁琐---字符串拼接
- 对设计师不友好

## JSP

- JSP（全称Java Server Pages）是由Sun Microsystems公司倡导和许多公司参与共同创建的一种使软件开发者可以响应客户端请求，而动态生成HTML、XML或其他格式文档的Web网页的技术标准。
- JSP技术是以Java语言作为脚本语言的，JSP网页为整个服务器端的Java库单元提供了一个接口来服务于HTTP的应用程序。
- JSP文件后缀名为(*.jsp)。
- JSP开发的WEB应用可以跨平台使用，既可以运行在Linux上也能运行在Windows上。

## 第一个JSP程序

```html
<html>
    <head>
           <title>第一个JSP程序</title>
    </head>
    <body>
             <%
                  out.println("Hello World！");
           %>
    </body>
</html>
```

## 做了什么

- 和普通的网页一样，浏览器发送一个HTTP请求给服务器。
- Web服务器识别出这是一个对JSP网页的请求，并且将该请求传递给JSP引擎。通过使用URL或者.jsp文件来完成。
- JSP引擎从磁盘中载入JSP文件，然后将它们转化为servlet。这种转化只是简单地将所有模板文本改println()语句，并且将所有的JSP元素转化成Java代码。
- JSP引擎将servlet编译成可执行类，并且将原始请求传递给servlet引擎。
- Web服务器的组件将会调用servlet引擎，然后载入并执行servlet类。在执行过程中，servlet产生HTML格式的输出并将其内嵌于HTTP response中上交给Web服务器。
- Web服务器以静态HTML网页的形式将HTTP response返回到浏览器中。
- 最终，Web浏览器处理HTTP response中动态产生的HTML网页，就好像在处理静态网页一样。

## JSP生命周期

- 编译阶段：

    servlet容器编译servlet源文件，生成servlet类  jspName_jsp.java

- 初始化阶段：

    加载与JSP对应的servlet类，创建其实例，并调用它的初始化方法 jspInit()

- 执行阶段：

    调用与JSP对应的servlet实例的服务方法 _jspService()

- 销毁阶段：

    调用与JSP对应的servlet实例的销毁方法，然后销毁servlet实例   jspDestroy()

## *_jsp.java

- 以tomcat为例,其生成的*_jsp.java编译后的类文件在tomcat\work\Catalina\localhost\project_name\org\apache\jsp
- 如果使用eclipse，则位置在类似下面的路径下workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp2\work\Catalina\localhost\iim_proj\org\apache\jsp
- 对于class文件可以到http://jd.benow.ca/下载JD-GUI来反编译为java文件

## JSP语法-脚本程序

```jsp
  <%
     int i = 0;
     out.println("Hello World！");
  %>
```

## JSP语法-JSP声明

```jsp
<%! int i = 0; %>
```

## 课堂练习

- 请编写JSP程序,比较Jsp脚本程序,与JSP声明的区别
- 请尝试编写一个程序，每刷新一次页面，计数器加1

## 初始化配置

- servlet

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

```java
public void init() throws ServletException {
  //getServletConfig().getInitParameter("data");
}
```

## jsp初始化配置


```xml
  <servlet>
    <servlet-name>helloServlet</servlet-name>
    <jsp-file>/new.jsp</jsp-file>
    <init-param>
      <param-name>data</param-name>
      <param-value>123</param-value>
    </init-param>
  </servlet>

```

```jsp
<%!
  public void jspInit() {
    //getServletConfig().getInitParameter("data");
  }
%>
```


## JSP语法-JSP表达式

```jsp
<%= (new java.util.Date()).toLocaleString()%>
```

## JSP语法-JSP注释

```JSP
<%-- 这里可以填写 JSP 注释 --%>
```

## 思考

- JSP注释与Java注释以及HTML注释比较

## JSP语法-条件判断与循环

```jsp
<% if(a > 1){ %>
    <a>link</a>
<% } %>
```

## 课堂练习

- 请编写if...else...语句
- 请编写while语句

## 使用JSP来开发博客

- 发布文章

. . .

- 在项目根目录新建文件article/new.jsp

. . .

```HTML
<form action='' method='post'>
		标题:<input type='text' name='title'/>
		内容:<textarea name='content'></textarea>
		<input type='submit' value='提交'/>
</form>
```

## 访问

- 浏览器输入 http://localhost:8080/project_name/article/new.jsp

## 与servlet比较

- servlet

    编写NewServlet
    使用String拼接html
    配置web.xml

- JSP

    在相应目录先新建new.jsp即可

## 提交

- jsp可以接收请求
- 在article下新建create.jsp

```Java
	<%
	String title = request.getParameter("title");
	title = new String(title.getBytes("iso-8859-1"),"utf-8");
	String content = request.getParameter("content");
	content = new String(content.getBytes("iso-8859-1"),"utf-8");

	try {
		String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url, "root", "root");
		Statement stmt = conn.createStatement();
		String sql = "insert into articles (title,content) values ('"
				+ title + "','" + content + "');";
		System.out.println(sql);
		stmt.executeUpdate(sql);
		conn.close();
	} catch (Exception e) {
		System.out.println("error");
	}

	response.sendRedirect("/03_jsp/article/suc.jsp");
	%>
```

## 比较

- response.sendRedirect()跳转变化
- 没有传入request,response参数,即可直接使用
- import方式变化

## 课堂练习

- 使用jsp编写新建文章的页面

## jsp指令

JSP指令用来设置整个JSP页面相关的属性，如网页的编码方式和脚本语言。

## page指令

- 定义网页依赖属性，比如脚本语言、error页面、缓存需求等等

## 属性

- buffer	指定out对象使用缓冲区的大小
- autoFlush	控制out对象的 缓存区
- contentType	指定当前JSP页面的MIME类型和字符编码
- errorPage	指定当JSP页面发生异常时需要转向的错误处理页面
- isErrorPage	指定当前页面是否可以作为另一个JSP页面的错误处理页面
- extends	指定servlet从哪一个类继承
- import	导入要使用的Java类
- info	定义JSP页面的描述信息
- isThreadSafe	指定对JSP页面的访问是否为线程安全
- language	定义JSP页面所用的脚本语言，默认是Java
- session	指定JSP页面是否使用session
- isELIgnored	指定是否执行EL表达式
- isScriptingEnabled	确定脚本元素能否被使用

## 示例

```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date,com.test.Hello" %>
```

```java
package com.test;
public class Hello{
  private String name;

  getter;setter;

  public void say(String name){
    System.out.println("Hi " + name);
  }
}
```

## 课堂练习

- 新增自定义类，并在jsp中调用

## Include指令

- JSP可以通过include指令来包含其他文件。被包含的文件可以是JSP文件、HTML文件或文本文件。包含的文件就好像是该JSP文件的一部分，会被同时编译执行。

```jsp
<%@ include file="relative url" %>
```

## include指令的用途

- 设置通用页面,减少代码重复

## 课堂练习

- 给博客添加通用的页头和页尾

## Taglib指令

- JSP API允许用户自定义标签，一个自定义标签库就是自定义标签的集合。
- Taglib指令引入一个自定义标签集合的定义，包括库路径、自定义标签。

```Jsp
<%@ taglib uri="uri" prefix="prefixOfTag" %>
```
- uri属性确定标签库的位置，prefix属性指定标签库的前缀。


## JSP动作元素

- 动态地插入文件、重用JavaBean组件、把用户重定向到另外的页面、为Java插件生成HTML代码。

## 动作元素

- jsp:include	在页面被请求的时候引入一个文件。
- jsp:useBean	寻找或者实例化一个JavaBean。
- jsp:setProperty	设置JavaBean的属性。
- jsp:getProperty	输出某个JavaBean的属性。
- jsp:forward	把请求转到一个新的页面。
- jsp:plugin	根据浏览器类型为Java插件生成OBJECT或EMBED标记。
- jsp:element	定义动态XML元素
- jsp:attribute	设置动态定义的XML元素属性。
- jsp:body	设置动态定义的XML元素内容。
- jsp:text	在JSP页面和文档中使用写入文本的模板

## <jsp:include>动作元素

<jsp:include>动作元素用来包含静态和动态的文件。该动作把指定文件插入正在生成的页面。

```Java
<jsp:include page="relative URL" flush="true" />
```

- page	包含在页面中的相对URL地址。
- flush	布尔属性，定义在包含资源前是否刷新缓存区。

## include指令与jsp:include动作元素的区别一

- include指令是编译阶段的指令，即include所包含的文件的内容是编译的时候插入到JSP文件中，JSP引擎在判断JSP页面未被修改，否则视为已被修改。由于被包含的文件是在编译时才插入的，因此如果只修改了include文件内容，而没有对JSP修改，得到的结构将不会改变，所以直接执行已经存在的字节码文件，而没有重新编译。

-  jsp:include动作是在主页面被请求时，将次级页面的输出包含进来。

## 思考

- 请问include指令生成了几个class?jsp:include动作呢?

## <jsp:useBean>动作元素

- jsp:useBean动作用来装载一个将在JSP页面中使用的JavaBean。
- jsp:useBean动作最简单的语法为：

```java
<jsp:useBean id="name" class="package.class" />
```
在类载入后，我们就可以通过 jsp:setProperty 和 jsp:getProperty 动作来修改和检索bean的属性。

## useBean属性列表

- class	指定Bean的完整包名。
- type	指定将引用该对象变量的类型。
- beanName	通过 java.beans.Beans 的 instantiate() 方法指定Bean的名字。

## <jsp:setProperty>动作元素

-　jsp:setProperty用来设置已经实例化的Bean对象的属性

```jsp
<jsp:useBean id="myName" ... />
...
<jsp:setProperty name="myName" property="someProperty" .../>
```

```jsp
<jsp:useBean id="myName" ... >
...
  <jsp:setProperty name="myName" property="someProperty" .../>
</jsp:useBean>
```

## setProperty属性列表

- name	name属性是必需的。它表示要设置属性的是哪个Bean。
- property	property属性是必需的。它表示要设置哪个属性。有一个特殊用法：如果property的值是"*"，表示所有名字和Bean属性名字匹配的请求参数都将被传递给相应的属性set方法。
- value	value 属性是可选的。该属性用来指定Bean属性的值。
- value和param不能同时使用，但可以使用其中任意一个。
- param	param 是可选的。它指定用哪个请求参数作为Bean属性的值。如果当前请求没有参数，则什么事情也不做，系统不会把null传递给Bean属性的set方法。

## <jsp:getProperty>动作元素

-　jsp:getProperty动作提取指定Bean属性的值，转换成字符串，然后输出。


```jsp
<jsp:useBean id="myName" ... />
...
<jsp:getProperty name="myName" property="someProperty" .../>
```

## getProperty属性

- name	要检索的Bean属性名称。Bean必须已定义。
- property	表示要提取Bean属性的值

## <jsp:forward> 动作元素
　
- jsp:forward动作把请求转到另外的页面。

```jsp
<jsp:forward page="Relative URL" />
```

## forward属性

- page	page属性包含的是一个相对URL。page的值既可以直接给出，也可以在请求的时候动态计算，可以是一个JSP页面或者一个 Java Servlet.

## jsp:plugin动作元素

- jsp:plugin动作用来根据浏览器的类型，插入通过Java插件 运行Java Applet所必需的OBJECT或EMBED元素。如果需要的插件不存在，它会下载插件，然后执行Java组件。 Java组件可以是一个applet或一个JavaBean。

## jsp:element 、jsp:attribute、jsp:body动作元素

- jsp:element 、jsp:attribute、jsp:body动作元素动态定义XML元素。动态是非常重要的，这就意味着XML元素在编译时是动态生成的而非静态。

## jsp:text动作元素

- jsp:text动作元素允许在JSP页面和文档中使用写入文本的模板

```jsp
<jsp:text>Template data</jsp:text>
```

- 以上文本模板不能包含其他元素，只能只能包含文本和EL表达式（注：EL表达式将在后续章节中介绍）。请注意，在XML文件中，不能使用表达式如 ${whatever > 0}，因为>符号是非法的。 你可以使用 ${whatever gt 0}表达式或者嵌入在一个CDATA部分的值。

## 隐含对象

JSP隐含对象是JSP容器为每个页面提供的Java对象，开发者可以直接使用它们而不用显式声明。

## 提供的隐含对象

- request	HttpServletRequest类的实例
- response	HttpServletResponse类的实例
- out	PrintWriter类的实例，用于把结果输出至网页上
- session	HttpSession类的实例
- application	ServletContext类的实例，与应用上下午有关
- config	ServletConfig类的实例
- pageContext	PageContext类的实例，提供对JSP页面所有对象以及命名空间的访问
- page	类似于Java类中的this关键字
- Exception	Exception类的对象，代表发生错误的JSP页面中对应的异常对象

## request对象

request对象是javax.servlet.http.HttpServletRequest 类的实例。每当客户端请求一个JSP页面时，JSP引擎就会制造一个新的request对象来代表这个请求。

## response对象

response对象是javax.servlet.http.HttpServletResponse类的实例。当服务器创建request对象时会同时创建用于响应这个客户端的response对象。

## out对象

out对象是 javax.servlet.jsp.JspWriter 类的实例，用来在response对象中写入内容。

## session对象

session对象是 javax.servlet.http.HttpSession 类的实例。和Java Servlets中的session对象有一样的行为。

## application对象

- application对象直接包装了servlet的ServletContext类的对象，是javax.servlet.ServletContext 类的实例。

- 这个对象在JSP页面的整个生命周期中都代表着这个JSP页面。这个对象在JSP页面初始化时被创建，随着jspDestroy()方法的调用而被移除。

## config对象

- config对象是 javax.servlet.ServletConfig 类的实例，直接包装了servlet的ServletConfig类的对象。

- 这个对象允许开发者访问Servlet或者JSP引擎的初始化参数，比如文件路径等。

## pageContext 对象

- pageContext对象是javax.servlet.jsp.PageContext 类的实例，用来代表整个JSP页面。

- 这个对象主要用来访问页面信息，同时过滤掉大部分实现细节。

- 这个对象存储了request对象和response对象的引用。application对象，config对象，session对象，out对象可以通过访问这个对象的属性来导出。

- pageContext对象也包含了传给JSP页面的指令信息，包括缓存信息，ErrorPage URL,页面scope等。

- PageContext类定义了一些字段，包括PAGE_SCOPE，REQUEST_SCOPE，SESSION_SCOPE， APPLICATION_SCOPE。它也提供了40余种方法，有一半继承自javax.servlet.jsp.JspContext 类。

## 示例

```jsp
<%
  //页面作用域
  pageContext.setAttribute("a",1);
  pageContext.getAttribute("a");

  //会话作用域
  pageContext.setAttribute("a",2,PageContext.SESSION_SCOPE);
  pageContext.getAttribute("a",PageContext.SESSION_SCOPE);
  //等价于
  session.getAttribute("a");

  //先从最小的作用域进行查找，也就是页面作用域，找不到再往大范围查找
  pageContext.findAttribute("a");  //结果?
%>
```

## page 对象

- 这个对象就是页面实例的引用。它可以被看做是整个JSP页面的代表。

- page 对象就是this对象的同义词。


## exception 对象

exception 对象包装了从先前页面中抛出的异常信息。它通常被用来产生对出错条件的适当响应。

## jsp作用范围

- servlet
    - 应用:getServletContext.setAttribute
    - 请求:request.setAttribute
    - 会话:request.getSession.setAttribute

. . .

- jsp
    - 应用:application.setAttribute
    - 请求:request.setAttribute
    - 会话:session.setAttribute
    - 页面:pageContext.setAttribute

## EL表达式

- EL表达式是为了简化Jsp中的Java代码

## 语法

```
${name}
```

- 从某一范围内获取名字叫name的变量的值
- 依序从Page、Request、Session、Application范围查找
- 等价于pageContext.findAttribute("name")

## [ ]与.运算符

- EL 提供“.“和“[ ]“两种运算符来存取数据。
- 当要存取的属性名称中包含一些特殊字符，如 . 或 - 等并非字母或数字的符号，就一定要使用“[ ]“。例如：
- ${ user. My-Name}应当改为${user["My-Name"]}
- 如果要动态取值时，就可以用“[ ]“来做，而“.“无法做到动态取值。例如：
- ${sessionScope.user[data]}中data是一个变量

## EL表达式作用范围

- pageContext   pageScope
- request requestScope
- session sessionScope
- application applicationScope

## 思考

- 如果想从页面范围内获取name值，EL表达式如何写?

. . .

```
${pageScope.name}
```

## EL隐式对象一

- pageContext
	JSP 页的上下文。它可以用于访问 JSP 隐式对象，如请求、响应、会话、输出、servletContext 等。例如，${pageContext.response} 为页面的响应对象赋值。

- param
	将请求参数名称映射到单个字符串参数值（通过调用 ServletRequest.getParameter (String name) 获得）。getParameter (String) 方法返回带有特定名称的参数。表达式 ${param . name}相当于 request.getParameter (name)。

- paramValues
	将请求参数名称映射到一个数值数组（通过调用 ServletRequest.getParameter (String name) 获得）。它与 param 隐式对象非常类似，但它检索一个字符串数组而不是单个值。表达式 ${paramvalues. name} 相当于 request.getParamterValues(name)。

- header
	将请求头名称映射到单个字符串头值（通过调用 ServletRequest.getHeader(String name) 获得）。表达式 ${header. name} 相当于 request.getHeader(name)。

## EL隐式对象二

- headerValues
	将请求头名称映射到一个数值数组（通过调用 ServletRequest.getHeaders(String) 获得）。它与头隐式对象非常类似。表达式 ${headerValues. name} 相当于 request.getHeaderValues(name)。

- cookie
	将 cookie 名称映射到单个 cookie 对象。向服务器发出的客户端请求可以获得一个或多个 cookie。表达式 ${cookie. name .value} 返回带有特定名称的第一个 cookie 值。如果请求包含多个同名的 cookie，则应该使用 ${headerValues. name} 表达式。

- initParam
	将上下文初始化参数名称映射到单个值（通过调用 ServletContext.getInitparameter(String name) 获得）。

## EL隐式对象三

- pageScope
	将页面范围的变量名称映射到其值。例如，EL 表达式可以使用 ${pageScope.objectName} 访问一个 JSP 中页面范围的对象，还可以使用 ${pageScope .objectName. attributeName} 访问对象的属性。

- requestScope
	将请求范围的变量名称映射到其值。该对象允许访问请求对象的属性。例如，EL 表达式可以使用 ${requestScope. objectName} 访问一个 JSP 请求范围的对象，还可以使用 ${requestScope. objectName. attributeName} 访问对象的属性。

- sessionScope
	将会话范围的变量名称映射到其值。该对象允许访问会话对象的属性。例如：
${sessionScope. name}

- applicationScope
  将应用程序范围的变量名称映射到其值。该隐式对象允许访问应用程序范围的对象。

## JSTL

- JSP标准标签库（JSTL）是一个JSP标签集合，它封装了JSP应用的通用核心功能。
- JSTL支持通用的、结构化的任务，比如迭代，条件判断，XML文档操作，国际化标签，SQL标签。 除了这些，它还提供了一个框架来使用集成JSTL的自定义标签。
- 根据JSTL标签所提供的功能，可以将其分为5个类别。
    - 核心标签
    - 格式化标签
    - SQL 标签
    - XML 标签
    - JSTL 函数

## JSTL 库安装

- 从Apache的标准标签库中下载的二进包(jakarta-taglibs-standard-current.zip)。下载地址：http://archive.apache.org/dist/jakarta/taglibs/standard/binaries/
- 下载jakarta-taglibs-standard-1.1.1.zip 包并解压，将jakarta-taglibs-standard-1.1.1/lib/下的两个jar文件：standard.jar和jstl.jar文件拷贝到/WEB-INF/lib/下。
- 使用任何库，你必须在每个JSP文件中的头部包含<taglib>标签。
- 核心标签是最常用的JSTL标签。引用核心标签库的语法如下：

```
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
```

## 核心标签

```
    <c:out> 	用于在JSP中显示数据，就像<%= ... >
    <c:set> 	用于保存数据
    <c:remove> 	用于删除数据
    <c:catch> 	用来处理产生错误的异常状况，并且将错误信息储存起来
    <c:if> 	与我们在一般程序中用的if一样
    <c:choose> 	本身只当做<c:when>和<c:otherwise>的父标签
    <c:when> 	<c:choose>的子标签，用来判断条件是否成立
    <c:otherwise> 	<c:choose>的子标签，接在<c:when>标签后，当<c:when>标签判断为false时被执行
    <c:import> 	检索一个绝对或相对 URL，然后将其内容暴露给页面
    <c:forEach> 	基础迭代标签，接受多种集合类型
    <c:forTokens> 	根据指定的分隔符来分隔内容并迭代输出
    <c:param> 	用来给包含或重定向的页面传递参数
    <c:redirect> 	重定向至一个新的URL.
    <c:url> 	使用可选的查询参数来创造一个URL
```

## if

```
<c:if test="${var.index % 2 == 0}">
 ssss
</c:if>
```

- <c:if>并没有提供else子句，使用的时候可能有些不便，此时我们可以通过<c:choose>

## choose

```
<c:choose>
<c:when test="${var.index % 2 == 0}">
1
</c:when>
<c:otherwise>
2
</c:otherwise>
</c:choose>
```

## forEach

```
<c:forEach items=“collection” var=“name” varStatus=“status” begin=“int“ end=”int” step=“int” >
           //循环体
</c:forEach>
```

- items:是集合，用EL表达式；
- var:变量名，存放items
- varStatus: 显示循环状态的变量
- index:从0开始;
- count:元素位置,从1开始;
- first:如果是第一个元素则显示true;
- last:如果是最后一个元素则显示true;
- begin:循环的初始值(整型)；
- end: 循环结束 ;
- step:步长,循环间隔的数值；

## 标签实际上...

- 标签实际上是继承了SimpleTagSupport的类

## 创建"Hello"标签

- 我们来创建一个自定义标签叫作<ex:Hello>，标签格式为：

```
<ex:Hello />
```

## HelloTag类

```
package com.test;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.*;

public class HelloTag extends SimpleTagSupport {

  public void doTag() throws JspException, IOException {
    JspWriter out = getJspContext().getOut();
    out.println("Hello Custom Tag!");
  }
}
```

- 以下代码重写了doTag()方法，方法中使用了getJspContext()方法来获取当前的JspContext对象，并将"Hello Custom Tag!"传递给JspWriter对象。

## custom.tld

- WEB-INF\custom.tld。

```xml
<taglib>
  <tlib-version>1.0</tlib-version>
  <jsp-version>2.0</jsp-version>
  <short-name>Example TLD</short-name>
  <tag>
    <name>Hello</name>
    <tag-class>com.tutorialspoint.HelloTag</tag-class>
    <body-content>empty</body-content>
  </tag>
</taglib>
```

## 使用

```
<%@ taglib prefix="ex" uri="WEB-INF/custom.tld"%>
<html>
  <head>
    <title>A sample custom tag</title>
  </head>
  <body>
    <ex:Hello/>
  </body>
</html>
```

- 输出结果为：Hello Custom Tag!

## 访问标签体

- 你可以像标准标签库一样在标签中包含消息内容

```
<ex:Hello>
   This is message body
</ex:Hello>
```

## 处理类

package com.test;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.*;

public class HelloTag extends SimpleTagSupport {

   StringWriter sw = new StringWriter();
   public void doTag()
      throws JspException, IOException
    {
       getJspBody().invoke(sw);
       getJspContext().getOut().println(sw.toString());
    }

}

## 修改TLD文件

```xml
<taglib>
  <tlib-version>1.0</tlib-version>
  <jsp-version>2.0</jsp-version>
  <short-name>Example TLD with Body</short-name>
  <tag>
    <name>Hello</name>
    <tag-class>com.tutorialspoint.HelloTag</tag-class>
    <body-content>scriptless</body-content>
  </tag>
</taglib>
```

## 使用

```xml
<%@ taglib prefix="ex" uri="WEB-INF/custom.tld"%>
<html>
  <head>
    <title>A sample custom tag</title>
  </head>
  <body>
    <ex:Hello>
        This is message body
    </ex:Hello>
  </body>
</html>
```

- 输出结果如下所示：This is message body

## 标签属性

- 你可以在自定义标准中设置各种属性，要接收属性值,自定义标签类必须实现setter方法

```
package com.test;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.*;

public class HelloTag extends SimpleTagSupport {

   private String message;

   public void setMessage(String msg) {
      this.message = msg;
   }

   StringWriter sw = new StringWriter();

   public void doTag()
      throws JspException, IOException
    {
       if (message != null) {
          /* 从属性中使用消息 */
          JspWriter out = getJspContext().getOut();
          out.println( message );
       }
       else {
          /* 从内容体中使用消息 */
          getJspBody().invoke(sw);
          getJspContext().getOut().println(sw.toString());
       }
   }
}
```

## tld文件

```
<taglib>
  <tlib-version>1.0</tlib-version>
  <jsp-version>2.0</jsp-version>
  <short-name>Example TLD with Body</short-name>
  <tag>
    <name>Hello</name>
    <tag-class>com.tutorialspoint.HelloTag</tag-class>
    <body-content>scriptless</body-content>
    <attribute>
       <name>message</name>
    </attribute>
  </tag>
</taglib>
```

## 使用

```
<%@ taglib prefix="ex" uri="WEB-INF/custom.tld"%>
<html>
  <head>
    <title>A sample custom tag</title>
  </head>
  <body>
    <ex:Hello message="This is custom tag" />
  </body>
</html>
```

- 以上实例数据输出结果为：This is custom tag

## 作业

- 使用JSP重写博客程序
- 请自学velocity


