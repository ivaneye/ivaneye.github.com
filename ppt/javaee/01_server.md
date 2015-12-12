% Server
% 王一帆
% 江苏企业大学

## 资源访问

- http://www.abc.com/1.html

当我们在浏览器中输入类似上面的链接后,做了什么事情?


![]({{site.CDN_PATH}}/home/ivan/my/teach/javaee/file/server.jpg)

## HTTP请求报文

```
GET /1.html HTTP/1.1
Accept: text/plain; text/html
Accept-Language: en-gb
Connection: Keep-Alive
Host: localhost
User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
Content-Length: 33
Content-Type: application/x-www-form-urlencoded
Accept-Encoding: gzip, deflate

...
```

- 起始行
- 请求首部 header
- 请求体 body

## 如何处理HTTP请求报文

- 解析http请求
- 根据请求获取内容
- 返回响应

## 获取http请求

- ServerSocket

## 示例

```java
ServerSocket serverSocket = null;
int port = 8080;
serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));

Socket socket = serverSocket.accept();
InputStream input = socket.getInputStream();
OutputStream output = socket.getOutputStream();

...
```

## 解析http请求

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

## 响应

```java
byte[] bytes = new byte[BUFFER_SIZE];
FileInputStream fis = null;
File file = new File(HttpServer.WEB_ROOT, request.getUri());
if (file.exists()) {
  fis = new FileInputStream(file);
  int ch = fis.read(bytes, 0, BUFFER_SIZE);
  while (ch!=-1) {
    output.write(bytes, 0, ch);
    ch = fis.read(bytes, 0, BUFFER_SIZE);
  }
}
```

## 课堂练习

- 请自行编写静态服务器

## 如何进行逻辑处理?

- 上面的服务器只能访问已经存在的静态页面,称为静态服务器
- 如何根据用户的请求来动态的处理,例如:展示当前用户的信息
- 能够动态展示内容的服务器,称为动态服务器

## 如何给服务器添加动态逻辑处理?

- 如何进行逻辑处理?
- 如何实现?
- 请回忆我们学习的Java基础

## 实现逻辑

- 请求以/dynamic开头,则为动态请求,执行相应的类,并返回处理结果
- 否则访问静态字资源

## 核心代码

```java
Class myClass = loader.loadClass("dynamic.bean." + servletName);

DynamicBean bean = (DynamicBean) myClass.newInstance();
bean.service( request,response);
```

## 课堂练习

- 实现动态服务器

## 发现问题

- 刚才的课堂练习中,有发现什么问题吗?

## 问题

- 编写的bean需要在特定目录下,如果需要编写到其它包下,则需要修改相应的代码
- url访问的必须是bean名称,无法使用其它名称
- 多个url无法请求同一个bean

## 如何解决?

- 使用配置文件,将url与bean进行匹配
- 在Java中,一般使用XML来进行配置

## XML

- XML 指可扩展标记语言（eXtensible Markup Language）
- XML 被设计用来传输和存储数据

## 例子

```XML
<?xml version="1.0"?>
<note>
  <to>B</to>
  <from>A</from>
  <title>Title here</title>
  <content>Content here</content>
</note>
```

## html与xml
```xml
<html>
  <head></head>
  <body>
    <input type="text" value="Hello"/>
  </body>
</html>
```
- 请问如上代码是html?还是xml?

## html与xml比较

- html是用来展示的!xml主要用来传输和存储信息!两者的用途不同
- html的标签可以不配对,浏览器对其友好!xml的标签必须配对,或者有结束符号!
- html的标签为事先定义,如果没有标签则无法解析!xml标签可随意,只要对应即可!
- html通过浏览器来解析,xml需要自行编写解析程序

## 定义xml

- dtd
- Schema

## dtd

```DTD
<!ELEMENT note (to, from, title, content)>
<!ELEMENT to (#PCDATA)>
<!ELEMENT from (#PCDATA)>
<!ELEMENT title (#PCDATA)>
<!ELEMENT content (#PCDATA)>
```

- 第 1 行定义 note 元素有四个子元素："to, from, title, content"
- 第 2-5 行定义了 to, from, title, content 元素的类型是 "#PCDATA"

## Schema

```xml
<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.example.org/bean"
	xmlns:tns="http://www.example.org/bean" elementFormDefault="qualified">

	<element name="note">
		<complexType>
			<sequence>
				<element name="to" type="string" />
				<element name="from" type="string" />
				<element name="title" type="string" />
				<element name="content" type="string" />
			</sequence>
		</complexType>
	</element>

</schema>
```

## 解释

- xmlns="http://www.w3.org/2001/XMLSchema"

    显示 schema 中用到的元素和数据类型来自命名空间 "http://www.w3.org/2001/XMLSchema"。

- targetNamespace="http://www.w3school.com.cn"
    显示被此 schema 定义的元素来自命名空间： "http://www.w3school.com.cn"。

- xmlns="http://www.w3school.com.cn"
    指出默认的命名空间是 "http://www.w3school.com.cn"。

- elementFormDefault="qualified"
    指出任何 XML 实例文档所使用的且在此 schema 中声明过的元素必须被命名空间限定。

## xml引入schema

```xml
<?xml version="1.0" encoding="UTF-8"?>
<note xmlns="http://www.example.org/bean" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.example.org/bean bean.xsd ">
	<to>to</to>
	<from>from</from>
	<title>title</title>
	<content>content</content>
</note>
```

## 解析XML

- DOM:采用建立树形结构的方式访问XML文档
    优点：允许编辑和更新XML文档，可以随机访问文档中的数据，可以使用XPath（XML Path Language，是一种从XML文档中搜索节点的查询语言）查询。
    缺点：需要一次性加载整个文档到内存中，对于大型文档，会造成性能问题。
- SAX:采用的事件模型
    优点：对XML文件的访问采用流的概念，在任何时候内存中只有当前节点，解决了DOM的性能问题。
    缺点：是只读的，并且只能向前，不能在文档中执行向后导航操作。
- Stax:Java6提供的解析xml的包

## 使用Stax来解析XML
```java
//获得一个XMLInputFactory的实例
XMLInputFactory factory = XMLInputFactory.newInstance();

//读取xml文件流
File xmlFile = new File("/home/ivan/my/teach/javaee/code/01_server/src/dynamic/bean.xml");
FileInputStream fis = new FileInputStream(xmlFile);

//构建xmlReader
XMLStreamReader xmlReader = factory.createXMLStreamReader(fis);

while(xmlReader.hasNext()){
  int event = xmlReader.next();
  // 如果是元素的开始
  if (event == XMLStreamConstants.START_ELEMENT) {
    if (!"note".equalsIgnoreCase(xmlReader.getLocalName())) {
      System.out.println(xmlReader.getElementText());
    }
  }
}
```

## 思考

- 是否可以使用xml来配置url与bean的映射关系?

## 示例

```xml
<root>
  <mapping>
    <url>/test</url>
    <bean>com.focustech.BeanName</bean>
  </mapping>

  <mapping>
    <url>/test2</url>
    <bean>com.focustech.BeanName</bean>
  </mapping>
</root>
```


## 不需要重新发明轮子

- Tomcat
- jetty
- GlassFish
- Websphere
- WebLogic

## Tomcat

- bin目录:启动脚本的目录

- etc目录:配置文件的目录

- lib目录:库文件目录

- webapps目录:应用部署目录

## 作业

- 理解服务器的执行流程,复习IO,反射等Java基础知识
- 给服务器添加xml配置,使其能根据xml配置,来解析url并进行相应的处理
- 给服务器添加新的处理逻辑,体会添加xml配置后带来的优势!
- 熟悉Tomcat服务器


