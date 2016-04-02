% SpringMVC
% 王一帆
% 江苏企业大学

## 目前Controller的问题

- 与容器耦合严重request,response
- 需要手动组装Article
- 需要手动获取ArticleService对象
- web.xml配置繁琐

## 如何解决?

- 对于web.xml配置繁琐,servlet&jsp3.0规范,提供了基于注解的配置
- 对于与容器的耦合,则需要第三方框架的支持

## 前端控制器

- 大部分框架提供的都是前端控制器模式
- 原始的请求,根据web.xml配置,调用相应的servlet来执行

## 图示

- 以SpringMVC为例

![](/home/ivan/my/teach/javaee/file/springmvc_01.jpg)

## 实际上...

- 所谓的前段控制器不过就是普通的Servlet或者Filter

## 引入SpringMVC

- 引入SpringMVC
- 编码控制
- 安全控制

## 修改pom.xml

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>4.1.0.RELEASE</version>
</dependency>
```

## 配置web.xml

```xml
<servlet>
  <servlet-name>springmvc</servlet-name>
  <servlet-class>
  org.springframework.web.servlet.DispatcherServlet
  </servlet-class>
  <init-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:springmvc-config.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>springmvc</servlet-name>
  <url-pattern>/</url-pattern>
</servlet-mapping>
```

## 说明

- 这里配置的即所谓的前端控制器
- SpringMVC提供的是一个称为DispatcherServlet的Servlet
- url-pattern为根路径,即所有请求全部由前端控制器去处理
- 此Servlet在启动时加载
- 并加载springmvc-config.xml配置文件进行初始化
- 如果不配置文件,则默认为{servlet-name}-servlet.xml

## 新建springmvc-config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:util="http://www.springframework.org/schema/util" xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.1.xsd
    http://www.springframework.org/schema/beans  http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.1.xsd
    http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.1.xsd">

	<context:component-scan base-package="com.blog.controller"/>

	<mvc:annotation-driven />

	<bean id="viewResolver"
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/jsp/" />
		<property name="suffix" value=".jsp" />
	</bean>

</beans>
```

## 说明

- 第一行:扫描com.blog.controller包,将其中的类加载为Controller
- 第二行:配置默认的HandlerMapping和HandlerAdapter
- HandlerMapping将请求映射到处理器及其拦截器上
- HandlerAdapter把处理器包装为适配器,从而支持多种类型的处理器
- viewResolver:渲染视图


## 移动jsp文件

- 移动.jsp文件到/jsp目录下
- 因为在上面的配置中,我们配置视图根路径为/jsp
- 并且以.jsp结尾

## 编写ArticleController

```java
@Controller
@RequestMapping("/article")
public class ArticleController {

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String index() {
		return "/article/new";
	}

}
```

## 说明

- Controller注解标明此类为一个Controller
- RequestMapping将url与此Controller进行了映射
- 此处访问http://localhost:port/article/new 即会返回/article/new.jsp页面

## 课堂练习

- 请自行添加SpringMVC支持
- 请尝试添加保存Article的方法

## 传值

- 在Servlet中,我们需要使用getParameter来获取值,然后设置到对象中
- 而在SpringMVC中如何处理呢?

## 代码

```java
@RequestMapping(value = "/new", method = RequestMethod.POST)
public String _new(Article article) {
  //save...
  return "/article/suc";
}
```

- 只需要在方法参数中传入Article对象作为参数,SpringMVC会自动根据其属性进行赋值

## 不仅仅如此

- Spring可以获取url中的参数,例如/article/1

```java
@RequestMapping(value = "/article/{id}", method = RequestMethod.POST)
public String _new(@PathVariable String id) {
  ...
}
```

## 保存数据

- 我们可以像在Servlet中那样,使用ApplicationContext

```java
@RequestMapping(value = "/new", method = RequestMethod.POST)
public String _new(Article article) {
  ArticleService service = ApplicationContext.getBean("articleService");
  service.save(article);
  return "/article/suc";
}
```

## 注入

- 目前我们的Controller也是被Spring管理的
- 所以,可以直接注入

```java
@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String _new(Article article) {
		articleService.save(article);
		return "/article/suc";
	}
}
```

## 课堂练习

- 请完成保存Article的代码
- 与Servlet对应代码保存,体会SpringMVC所带来的好处(或者你觉得有哪些缺点?)

## 重定向

- 当你保存完成后,跳转到了成功页面.
- 但是当你刷新页面后,你会发现数据再次提交
- 一般情况下,保存数据后,我们要重定向到成功页面,保证即使再次刷新,也不会重复提交
- servert中是使用response.sendRedirect来实现(回忆一下重定向与请求转发)

## SpringMVC重定向

- SpringMVC的重定向非常的简单
- 添加redirect:前缀即可

```java
@Controller
@RequestMapping("/article")
public class ArticleController {

	...

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String _new(Model model,Article article) {
		articleService.save(article);
		return "redirect:/article/success";
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String suc() {
		return "/article/suc";
	}
}
```

## 编码

- 请尝试输入中文并保存!
- 发现什么?
- 回忆在Servlet中的处理方式!

## SpringMVC处理编码

- SpringMVC中处理编码的方式一模一样
- 不过SpringMVC提供了相应的filter
- 我们只需要在web.xml中配置即可

```java
<filter>
  <filter-name>encodingFilter</filter-name>
  <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
  <init-param>
    <param-name>encoding</param-name>
    <param-value>UTF-8</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>encodingFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

## 返回值

- SpringMVC通过Model向页面传递值

```java
@RequestMapping(value = "/new", method = RequestMethod.GET)
public String index(Model model) {
  model.addAttribute("hello","From ArticleController");
  return "/article/new";
}
```

## 页面获取值

```jsp
<%=request.getAttribute("hello") %>
```

## 请回答如下问题

- 请回忆filter的工作流程
- 以及如何添加filter?
- filter主要作用有哪些?

## 拦截器

- SpringMVC中也有类似的组件,称为Interceptor

## 拦截器与Filter

- Filter
    实现Filter接口
    覆写doFilter方法
    配置web.xml
- Interceptor
    继承HandlerInterceptorAdapter
    配置bean
    覆写preHandler,postHandler等方法

## 编写拦截器

```java
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        System.out.println(handlerMethod.getBean());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(handler.getClass());
        super.postHandle(request, response, handler, modelAndView);
    }
}
```

## 配置bean

```xml
 <mvc:interceptors>
      <ref bean="logInterceptor"/>
 </mvc:interceptors>

 <bean id="logInterceptor" class="com.blog.interceptor.LogInterceptor"/>
```

## 拦截制定url

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/article/new"/>
        <ref bean="logInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

## 排除拦截url

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:exclude-mapping path="/article/new"/>
        <ref bean="logInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

## 课堂作业

- 请尝试添加自定义拦截器
- 尝试实现简单的登陆拦截器
    除了登陆url,其它url全部需要先登陆
    检测用户是否登陆
    如果未登陆则跳转到登陆页面
    否则继续方法

## 数据绑定进阶

- 多选框绑定
- 日期绑定

## 多选框绑定

- SpringMVC会根据提交的form中的字段的name与对象中的属性进行对比,如果相同则赋值
- 那么对于多选框如何赋值呢?
- 在Servlet中是如何做的?

## SpringMVC中的处理

- 例如我们有如下的多选框

```html
<input type="checkbox" name="type" value="1">T1</input>
<input type="checkbox" name="type" value="2">T2</input>
<input type="checkbox" name="type" value="3">T3</input>
```

## 如何获取?

- request.getParameterValues?
- 自动绑定?

## 添加type属性

```java
private String type;

public String getType() {
    return type;
}

public void setType(String type) {
    this.type = type;
}
```

## 课堂练习

- 尝试如上获取方法,看看获取到什么值?

## 基于下标的绑定

```java
    private List<String> type = new ArrayList<String>();

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }
```

## 对应的html

```html
<input type="checkbox" name="type[0]" value="1">T1</input>
<input type="checkbox" name="type[1]" value="2">T2</input>
<input type="checkbox" name="type[2]" value="3">T3</input>
```

## 日期绑定

- 无论Java中的任何类型,任何结构,在页面中都只是扁平化的文字显示而已
- 就日期而言,在页面中是个特殊格式化的字符串(比如:2014-01-02)
- 那如何将字符串接收为java.util.Date类型呢?

## html

```html
添加日期:<input type='text' name='addTime' />
```

## 课堂练习

-  尝试提交日期,跟踪后台获取日期

## 尝试结果

- yyyy/mm/dd格式可以获取
- yyyy-mm-dd格式无法获取

## 默认格式转换

- springMVC提供了默认的格式化
- 不过适用日期类型为yyyy/mm/dd
- 如果需要yyyy-mm-dd则需要自定义操作

## 自定义日期格式化类

```java
public class DateConversion implements Converter<String,Date> {

    @Override
    public Date convert(String s) {
        if(s == null) return null;
        if(s.trim().equals("")) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            return sdf.parse(s);
        }catch(Exception e){
            return null;
        }
    }
}
```

## 配置格式化器

```xml
<mvc:annotation-driven conversion-service="conversionService"/>

    <bean id="conversionService"
          class="org.springframework.context.support.ConversionServiceFactoryBean">
        <property name="converters">
            <set>
                <bean class="com.blog.conversion.DateConversion"/>
            </set>
        </property>
    </bean>
```

## 课堂作业

- 请编写代码尝试绑定多选框值及日期

## 数据校验

- 你无法使你的用户不输入违法字符
- 但是我们可以拦截
- springMVC自带验证器,并支持jsr-303,jsr-349

## 添加依赖

```xml
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-validator</artifactId>
  <version>5.1.2.Final</version>
</dependency>
```

## 给model添加注解

```java
@NotEmpty(message = "标题不能为空")
    private String title;
```

## 修改ArticleController

```java
@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String _new(Model model,@Valid ArticleDomain article,BindingResult result) {
        if(result.hasErrors()){
            ...
        }else{
            ...
        }
	}
```

## 课堂作业

- 请添加数据验证

## 文件上传

```xml
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.3</version>
</dependency>
```

## bean

```xml
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="300000"/>
    </bean>
```

## html

```html
<form action="" method="post" enctype="multipart/form-data">
<input type="file" name="file"/>
</form>
```

## Controller

```java
public String _new(Model model,MultipartFile file) {
         // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"
                        + file.getOriginalFilename();
                // 转存文件
                file.transferTo(new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
```

## 课后作业

- 引入SpringMVC
