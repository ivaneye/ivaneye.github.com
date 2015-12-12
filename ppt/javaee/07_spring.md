% IOC与AOP
% 王一帆
% 江苏企业大学

## 上节回顾

- mvc
- 基于jdbc的model
- 基于mybatis的model

## 切换

- 比如我们想将jdbc切换到mybatis
- 又比如我们原来使用的mybatis,现在想使用jdbc
- 如何切换?

## 现状

- jdbc通过connection操作,mybatis通过session操作,没有共通点
- 两者的大致流程相同,功能相同
- 是否有方法可以方便的进行切换?

## 抽象

- 我们的应用目前分为了View,Controller,Service,DAO,Model
- 持久化框架涉及到了DAO和Model,Model是通用的,所以实际涉及到的是DAO
- 而与DAO进行交互的是Service,而交互是基于实际的对象进行的
- 如果基于接口是否可以使切换变得简单?

## 面向接口编程

- DAO全称Database Access Object,数据库访问对象
- 顾名思义主要是进行数据库操作
- 我们可以统一抽象出DAO接口

## ArticleDAO接口

- save
- update
- delete
- query

## 代码

```java
public interface ArticleDAO {

	public void save(Article article);

	public void update(Article article);

	public void delete(Article article);

	public void query(Article article);

}
```

## JDBC实现ArticleDAO接口

```java
public class ArticleDAOJDBCImpl implements ArticleDAO {

	private Connection conn;

	public void save(Article article) {
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into articles (title,content) values ('"
					+ article.getTitle() + "','" + article.getContent() + "');";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("error");
		}
	}

...
}
```

## mybatis实现ArticleDAO接口

```java
public class ArticleDAOMyBatisImpl implements ArticleDAO {

	private SqlSession session;

	public void save(Article article) {
		session.insert("articles.insert", article);
	}

...

}
```

## ArticleService实现

```java
public class ArticleService {

	public void save(Article article){
		ArticleDAO articleDAO = new ArticleDAOJDBCImpl();
    //or
    ArticleDAO articleDAO = new ArticleDAOMyBatisImpl();

    //暂时忽略事务管理
		articleDAO.save(article);
	}
}
```

- 此时切换实现,只需要实现不同的DAO实现即可!

## 到此为止了吗?

. . .

- 是否真正做到了面向接口编程?
- 如果有100个service,每个service里有10个方法,需要替换实现,需要修改多少代码?
- 问题在哪里?

## 矛盾

- 我们倡导面向接口编程,面向抽象编程,高内聚,低耦合
- 但是我们无法消除耦合,当我们创建对象的时候,不可避免的要触及到实现
- 有办法解决吗?

## IOC

- IOC: Inverse Of Control 控制反转
    一般情况下我们需要对象时需要手动去创建
    使用IOC后,当我们需要对象时,只需要到IOC容器中去获取
- DI: Dependency Injection 依赖注入

## 使用IOC后

```java
public class ArticleService {

	public void save(Article article){
		ArticleDAO articleDAO = IOContainer.getBean("articleDAO");

		articleDAO.save(article);
	}
}
```

## 如何实现IOContainer?

- IOC其实就是将代码配置化
- 将需要实现的类配置到配置文件中,启动应用后实例化到IOC容器中
- 当需要对象时,从IOC中去获取
- 需要修改实现,只需要修改配置文件即可

## 配置文件

- 当然使用xml了

```xml
<beans>
	<bean id="articleDAO" class="com.blog.dao.ArticleDAOJDBCImpl"/>
</beans>
```

## 创建IOContainer类

```java
public class IOContainer {

	private static Map<String, Object> beans = new HashMap<String, Object>();

	public static void init(InputStream is) {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader xmlReader;
		try {
			xmlReader = factory.createXMLStreamReader(is);
			while (xmlReader.hasNext()) {
				int event = xmlReader.next();
				if (event == XMLStreamConstants.START_ELEMENT) {
					if (!"beans".equalsIgnoreCase(xmlReader.getLocalName())) {
						String id = xmlReader.getAttributeValue(null, "id");
						String clz = xmlReader.getAttributeValue(null, "class");
						beans.put(id, Class.forName(clz).newInstance());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <T>  T getBean(String id){
		return (T) beans.get(id);
	}
}
```

## 修改InitServlet

```java
public class InitServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		super.init();
		InputStream inputStream  = getServletContext().getClassLoader().getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SessionFactoryUtil.init(sqlSessionFactory);

    //初始化IOC容器
		InputStream inputStream2  = getServletContext().getClassLoader().getResourceAsStream("beans.xml");
		IOContainer.init(inputStream2);
	}
}
```

## 课堂练习

- 请编写代码实现IOC容器,体会IOC对编码带来的改变

## 轮子已经有了

- IOC容器有很多
- 最常用的是Spring

## 引入Spring

- pom.xml添加如下配置

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-web</artifactId>
  <version>4.1.0.RELEASE</version>
</dependency>
```

## 配置web.xml

```xml
<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<!-- 默认配置在WEB-INF目录下 -->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:/applicationContext.xml</param-value>
</context-param>
```

## 配置applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:aop="http://www.springframework.org/schema/aop"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd ">

<bean id="articleDAO" class="com.blog.dao.ArticleDAOJDBCImpl"/>

</beans>
```

## 修改InitServlet

```java
public class InitServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		super.init();
		InputStream inputStream  = getServletContext().getClassLoader().getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SessionFactoryUtil.init(sqlSessionFactory);

		//初始化IOC容器
		InputStream inputStream2  = getServletContext().getClassLoader().getResourceAsStream("beans.xml");
		IOContainer.init(inputStream2);

	//初始化SpringContext	ApplicationContext.init(WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext()));
	}
}
```

## 新建ApplicationContext.java

```java
public class ApplicationContext {

	private static WebApplicationContext webContext;

	public static void init(WebApplicationContext context) {
		webContext = context;
	}

	public static <T> T getBean(String id){
		return (T) webContext.getBean(id);
	}
}
```

## 修改ArticleService

```java
public class ArticleService {

	public void save(Article article){
		ArticleDAO articleDAO = ApplicationContext.getBean("articleDAO");

		articleDAO.save(article);
	}
}
```

## 课堂练习

- 引入Spring

## 进一步整合

- Spring对各种框架提供了友好的支持
- 对Mybatis的支持有点特殊,由于Mybatis发布未赶上Spring版本的发布,所以Mybatis与Spring的支持为第三方支持

## Spring整合jdbc

- 对jdbc我们编写了ConnectionUtil来获取Connection
- 且没有连接池的功能
- 我们可以通过Spring来管理数据源,并提供连接池的支持

## pom.xml添加依赖

```java
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-jdbc</artifactId>
  <version>4.1.0.RELEASE</version>
</dependency>
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-dbcp2</artifactId>
  <version>2.0.1</version>
</dependency>
```

## 新建datasource.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd ">

	<bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource">
		<property name="driverClassName"  value="com.mysql.jdbc.Driver"/>
		<property name="url"  value="jdbc:mysql://localhost:3306/blog?characterEncoding=utf8"/>
		<property name="username"  value="root"/>
		<property name="password"  value="root"/>
	</bean>

  <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate"
		abstract="false" lazy-init="false" autowire="default">
		<property name="dataSource" ref="dataSource"/>
	</bean>

</beans>
```

## 说明

- 首先声明了一个dataSource的bean,就是数据库连接配置
- 接着声明了jdbcTemplate Bean,注入了属性dataSource
- jdbcTemplate为Spring提供的简化jdbc操作的助手类

## 修改ArticleDAOJDBCImpl

```java
public class ArticleDAOJDBCImpl implements ArticleDAO {

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void save(Article article) {
			String sql = "insert into articles (title,content) values ('"
					+ article.getTitle() + "','" + article.getContent() + "');";
			jdbcTemplate.execute(sql);
	}

...
}
```

## applicationContext.xml

```xml
<import resource="datasource/datasource.xml"/>

<bean id="articleDAO" class="com.blog.dao.ArticleDAOJDBCImpl">
	<property name="jdbcTemplate"  ref="jdbcTemplate"/>
</bean>
```
- Spring配置文件可以通过import来引入,方便将配置按模块划分
- Spring在创建bean时,可以依据配置来设置属性,这也是称为依赖注入的原因

## 课堂练习

- 添加Spring-jdbc支持

## Spring整合Mybatis

- mybatis没有官方支持,为第三方支持

## pom.xml添加依赖

```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-spring</artifactId>
  <version>1.2.2</version>
</dependency>
```

## datasource.xml

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource" />
  <property name="mapperLocations" value="classpath*:com/blog/mybatis/config/**/*.xml" />
</bean>
```

## 修改ArticleDAOMyBatisImpl

```java
public class ArticleDAOMyBatisImpl implements ArticleDAO {

	private SqlSession session;
	private SqlSessionFactory sqlSessionFactory;

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void save(Article article) {
		session.insert("com.blog.mybatis.config.ArticleMapper.insert", article);
	}
...
}
```

## 修改applicationContext.xmlns

```xml
<bean id="articleDAO" class="com.blog.dao.ArticleDAOMyBatisImpl">
	<property name="sqlSessionFactory"  ref="sqlSessionFactory"/>
</bean>
```

## 使用Mapper

- 在Spring中使用Mapper类来操作,也很方便

## 继续修改ArticleDAOMyBatisImpl

```java
public class ArticleDAOMyBatisImpl implements ArticleDAO {

	private ArticleMapper articleMapper;

	public ArticleMapper getArticleMapper() {
		return articleMapper;
	}

	public void setArticleMapper(ArticleMapper articleMapper) {
		this.articleMapper = articleMapper;
	}

	public void save(Article article) {
		articleMapper.insert(article);
	}
...
}
```

## 修改applicationContext.xml

```xml
<bean id="articleDAO" class="com.blog.dao.ArticleDAOMyBatisImpl">
	<property name="articleMapper"  ref="articleMapper"/>
</bean>

<bean id="articleMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
  <property name="mapperInterface" value="com.blog.dao.ArticleMapper" />
  <property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>
```

## 课堂练习

- 请添加Spring-mybatis支持
- 将Service添加到Spring进行管理

## 事务管理

- 每次我们在Service中调用DAO,都需要手动开启事务,最后再提交事务
- 很明显的重复代码,是否可以消除?
- 我们以mybatis的session为例,来进行处理

## 代理模式

- 为其他对象提供一种代理以控制对这个对象的访问。
- 就像人们到商店去买东西，商店会提供一些附加服务，但是商店是不会生产东西的，商店是到工厂去拿东西。商店就是一个代理。
- 具体流程就像这样，用户想要一个item，于是到Shop去buyItem，而商店是不会生产item的，他就到ItemFactory去让工厂生产item，然后卖给客户，并提供售前售后服务。

## 示例

```java
//工厂接口
public interface ItemFactory{
     public Item getItem();
}

//工厂实现,Item就不实现了，随便怎么写都行
public class ItemFactoryImpl implements ItemFactory{
      public Item getItem(){
          return new Item();
      }
}

//商店类，就是代理
public class Shop implements ItemFactory{

     private ItemFactory factory = new ItemFactoryImpl();

     public Item getItem(){
          System.out.println("附加服务");
          return factory.getItem();
     }
}

//实际调用
public class Main{
     public static void main(String[] args){
          ItemFactory f = new Shop();
          f.getItem();
     }
}
```

## 实现ArticleService代理

- 首先需要抽象出Service接口
- ArticleService实现Service接口,其中只包含数据库处理
- 编写ArticleServiceProxy类,包含事务处理

## Service接口

```java
public interface Service<T> {

	public void save(T t) ;

}
```

## 修改ArticleService

```java
public class ArticleService implements Service<Article>{
  ...

  public void save(Article article) {
		articleDAO.save(article);
	}
}
```

## 编写ArticleServiceProxy

```java
public class ArticleServiceProxy implements Service<Article>{

  ...

  private ArticleService articleService = new ArticleService();

  ...

	public void save(Article article) {
		SqlSession session = sqlSessionFactory.openSession(false);

		articleDAO.init(session);

		articleService.setArticleDAO(articleDAO);
		articleService.save(article);

		session.commit();
	}
}
```

## applicationContext.xml

```xml
<bean id="articleService" class="com.blog.service.ArticleServiceProxy">
	<property name="articleDAO"  ref="articleDAO"/>
	 <property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>
```

- 对应的获取代码

```java
Service articleService = ApplicationContext.getBean("articleService");
```

## 问题

- 针对每个Service都需要编写一个Proxy,非常的繁琐
- 而实际上由于第三方库的限制,当使用Spring管理mybatis时,则不能手动控制事务了!

## 声明式事务

```xml
<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<!--所有以find开头的方法都是只读的 -->
			<tx:method name="find*" read-only="true" />
			<tx:method name="save*" />
			<!--其他方法使用默认事务策略 -->
			<tx:method name="*" />
		</tx:attributes>
</tx:advice>
	<!-- AOP配置 -->
<aop:config>
		<aop:pointcut id="myPointcut" expression="execution(* com.blog.service.*.*(..))" />
		<!--将定义好的事务处理策略应用到上述的切入点 -->
		<aop:advisor advice-ref="txAdvice" pointcut-ref="myPointcut" />
</aop:config>
```

## 课堂练习

- 添加声明式事务管理

## 使用注解简化Spring的配置

- Spring提供了基于注解的配置

## 修改ArticleService

```java
@Service
@Transactional
public class ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

  ...
```

## 修改ArticleMybatisDAOImpl

```java
@Component("articleDAO")
public class ArticleDAOMyBatisImpl implements ArticleDAO {

	@Autowired
	private ArticleMapper articleMapper;

  ...

```

## 修改applicationContext.xml

```xml
<context:component-scan base-package="com.blog.dao"/>
<context:component-scan base-package="com.blog.service"/>

<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
  <property name="basePackage" value="com.blog.mapper" />
</bean>
```

## 修改datasource.xml

```xml
<tx:annotation-driven transaction-manager="transactionManager"/>
```

## 课后作业

- 完善应用的Spring支持

