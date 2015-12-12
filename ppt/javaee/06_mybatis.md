% MyBatis
% 王一帆
% 江苏企业大学

## ORMapping

- 在前面,我们使用了一个对象来向sql传递数据
- 而这个对象与表对应
- 这样的一种关系叫做ORMapping

## 问题

- 目前针对每张表,我们需要手动创建一个对应的对象
- 每个对象都需要一个对应的操作类(DAO)
- 而这些代码都是机械性工作
- 机械性代码直接生成就好了
- 目前有很多框架可以来做这些事情
- 例如:Hibernate,ibatis/mybatis
- 它们将对数据库的操作映射为对Java对象的操作,所以称为ORMapping框架

## 给博客添加mybatis支持

- 在pom.xml里添加依赖关系,以及generator插件
- 配置generator配置文件
- 生成相应的代码
- 配置mybatis
- 编写代码

## 添加依赖

```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>3.2.7</version>
</dependency>
```

```xml
<build>
  <plugins>
	  ...
		<plugin>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-maven-plugin</artifactId>
			<version>1.3.0</version>
		</plugin>
	</plugins>
</build>
```

## 配置generator配置文件

- 在src/main/resources下新建文件generatorConfig.xml
- 官方地址[](http://mybatis.github.io/generator/index.html)

## generatorConfig.xml根节点

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
...
</generatorConfiguration>
```

## 配置数据库驱动jar

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>

  <classPathEntry location="/home/ivan/.m2/repository/mysql/mysql-connector-java/5.1.32/mysql-connector-java-5.1.32.jar" />

...
</generatorConfiguration>
```

## 配置各文件路径
```xml
	<context id="DB2Tables" targetRuntime="MyBatis3">
		<jdbcConnection driverClass="com.mysql.jdbc.Driver"
			connectionURL="jdbc:mysql://localhost:3306/blog" userId="root" password="root">
		</jdbcConnection>
		<javaTypeResolver>
			<property name="forceBigDecimals" value="false" />
		</javaTypeResolver>
		<javaModelGenerator targetPackage="com.blog.model.mybatis"
			targetProject="/home/ivan/my/teach/javaee/code/06_mybatis/src/main/java">
			<property name="enableSubPackages" value="true" />
			<property name="trimStrings" value="true" />
		</javaModelGenerator>
		<sqlMapGenerator targetPackage="com.blog.mybatis.config"
			targetProject="/home/ivan/my/teach/javaee/code/06_mybatis/src/main/resources">
			<property name="enableSubPackages" value="true" />
		</sqlMapGenerator>
		<javaClientGenerator type="XMLMAPPER"
			targetPackage="com.blog.dao.mybatis" targetProject="/home/ivan/my/teach/javaee/code/06_mybatis/src/main/java">
			<property name="enableSubPackages" value="true" />
		</javaClientGenerator>
		<table schema="blog" tableName="articles" domainObjectName="Article">
			<property name="useActualColumnNames" value="false" />
		</table>
	</context>
```

## 生成文件

```sh
mvn mybatis-generator:generate
```
或者

```sh
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

## 课堂练习

- 请自行生成相关表

## 文件说明

- Article:普通JavaBean
- ArticleExample:用来构建条件查询
- ArticleMapper.java:自动生成的DAO接口
- ArticleMapper.xml:操作配置文件

## 配置mybatis

- 在src/main/resources下新建mybatis-config.xml配置文件

## mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="com.mysql.jdbc.Driver" />
				<property name="url" value="jdbc:mysql://localhost:3306/blog?characterEncoding=utf8" />
				<property name="username" value="root" />
				<property name="password" value="root" />
			</dataSource>
		</environment>
	</environments>
	<mappers>
		<mapper resource="com/blog/mybatis/config/ArticleMapper.xml" />
	</mappers>
</configuration>
```

## 编写代码

- 修改ArticleService里的相关方法

```java
public void save(Article article){
		SqlSessionFactory sqlSessionFactory ;   //如何获取?
		SqlSession session = sqlSessionFactory.openSession();
		ArticleMapper articleMapper = session.getMapper(ArticleMapper.class);
		articleMapper.insert(article);
		session.commit();
		session.close();
}
```

## 回顾JDBC

```java
public void save(Article article){
		ArticleDAO articleDAO = new ArticleDAO();
		Connection conn = ConnectionUtil.getConnection();
		articleDAO.setConn(conn);
		conn.setAutoCommit(false);
		articleDAO.save(article);
		conn.commit();
		conn.close();
}
```

## 与JDBC比较

- 两者操作流程基本相同
- mybatis操作基于Session,jdbc基于Connection

## 如何获得SqlSessionFactory

- 官方样例代码

```java
String resource = "org/mybatis/example/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

## 何时获取?

- 每次操作时获取?
- 应用启动时获取?

## 每次操作时获取

- 耗费资源
- 耗费时间,影响效率

## 如何应用启动时获取呢?

- load-on-startup
- listener

## InitServlet

```java
public class InitServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		super.init();
		InputStream inputStream  = getServletContext().getClassLoader().getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SessionFactoryUtil.init(sqlSessionFactory);
	}
}
```

## web.xml

```xml
<servlet>
   <servlet-name>initServlet</servlet-name>
   <servlet-class>com.blog.InitServlet</servlet-class>
   <load-on-startup>1</load-on-startup>
</servlet>
```

## SessionFactoryUtil

```java
public class SessionFactoryUtil {

	private static SqlSessionFactory sqlSessionFactory;

	public static void init(SqlSessionFactory factory){
		sqlSessionFactory = factory;
	}

	public static SqlSessionFactory getSqlSessionFactory(){
		return sqlSessionFactory;
	}
}
```

## ArticleService

```java
public class ArticleService {

	public void save(Article article){
		SqlSessionFactory sqlSessionFactory = SessionFactoryUtil.getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		ArticleMapper articleMapper = session.getMapper(ArticleMapper.class);
		articleMapper.insert(article);
		session.commit();
		session.close();
	}
}
```

## 课堂练习

- 请自行修改save方法,改为使用mybatis实现

## Session

- 实际上在Mybatis中,实际的操作都是Session去执行的

## Session操作

```java
public class ArticleService {

	public void save(Article article){
		SqlSessionFactory sqlSessionFactory = SessionFactoryUtil.getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		session.insert("com.blog.mybatis.config.ArticleMapper.insert",article);
		session.commit();
		session.close();
	}
}
```
- com.blog.mybatis.config.ArticleMapper.insert 是什么?

## ArticleMapper.xml

```XML
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.blog.mybatis.config.ArticleMapper">
...
<insert id="insert" parameterType="com.blog.model.mybatis.Article">
    insert into articles (rec_id, title, status,
      add_time, update_time, content
      )
    values (#{recId,jdbcType=BIGINT}, #{title,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER},
      #{addTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, #{content,jdbcType=LONGVARCHAR}
      )
  </insert>
  ...
</mapper>
```

## *Mapper.xml

- cache – 给定命名空间的缓存配置。
- cache-ref – 其他命名空间缓存配置的引用。
- resultMap – 是最复杂也是最强大的元素，用来描述如何从数据库结果集中来加载对象。
- sql – 可被其他语句引用的可重用语句块。
- insert – 映射插入语句
- update – 映射更新语句
- delete – 映射删除语句
- select – 映射查询语句

## 官方文档

- http://mybatis.github.io/mybatis-3/zh/sqlmap-xml.html

## 动态SQL

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

## 课堂作业

- 请尝试自己编写sql,并调用执行.理解Mybatis的执行过程
- 请自行编写一个新的Mapper.xml文件,并将其引入,并调用

## 课后作业

- 将博客持久层修改为mybatis



