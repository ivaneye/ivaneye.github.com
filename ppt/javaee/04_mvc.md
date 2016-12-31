% MVC
% 王一帆
% 江苏企业大学

## Servlet与JSP

- Servlet的优势,劣势
- JSP的优势,劣势

## Servlet

- 优势

    逻辑处理

- 劣势

    页面展示

## JSP

- 优势

    页面展示

- 劣势

    逻辑处理

## 取长补短

- Servlet来进行逻辑处理
- JSP来进行页面展示

## 课堂练习

- 再次修改博客应用
- 通过new.jsp来展示页面
- 通过NewServlet来处理保存逻辑

## VC

- View : JSP
- Controller : Servlet

## 走近doPost方法

```java
String title = request.getParameter("title");
String content = request.getParameter("content");

try {
    String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection(url, "root", "root");
    Statement stmt = conn.createStatement();
    String sql = "insert into articles (title,content) values ('"
    + title + "','" + content + "');";
    stmt.executeUpdate(sql);
    conn.close();
  } catch (Exception e) {
    System.out.println("error");
}

response.sendRedirect("/hello/hello");
```

## 做了几件事情?

. . .

- 从请求中获取内容
- 保存到数据库
- 页面跳转
- 业务处理

## 单一职责

- 一个方法只处理一件事
- 能引起方法修改的触发点只有一个

## 方法分解

- Controller:获取数据,页面跳转
- Service:组装DAO和Model,事务管理
- DAO:保存数据到数据库

## ArticleService

```Java
public class ArticleService {

	public void save(String title, String content) {
		new ArticleDAO().save(title,content);
	}
}
```

## ArticleDAO

```java
public class ArticleDAO {

	public void save(String title, String content) {
		try {
			String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, "root", "root");
			Statement stmt = conn.createStatement();
			String sql = "insert into articles (title,content) values ('"
					+ title + "','" + content + "');";
			stmt.executeUpdate(sql);
			conn.close();
		} catch (Exception e) {
			System.out.println("error");
		}
	}
}
```

## Servlet

```java
public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		new ArticleService().save(title,content);

		response.sendRedirect("/04_mvc/article/suc.jsp");
	}
```

## 新增字段

- 如果有另一个逻辑,需要保存时间,该怎么办?
- 重载save方法?

## Model

- 失血模型:普通JavaBean
- 贫血模型(领域模型):包含了业务逻辑处理代码
- 充血模型:除了包含业务逻辑处理代码,还包括数据库操作(即DAO的工作)
- 胀血模型:除了包含业务逻辑处理代码和数据库操作(即DAO的工作),还包括事务管理

## 失血模型

```java
public class Article {
	private Long recId;
	private String title;
	private String content;
	private Integer status;
	private Date addTime;
	private Date updateTime;

  ....
```
## 对应Service

```java
public class ArticleService {

	public void save(Article article) {
		new ArticleDAO().save(article);
	}
}
```

## 对应DAO

```java
public void save(Article article) {
		try {
			String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, "root", "root");
			Statement stmt = conn.createStatement();
			String sql = "insert into articles (title,content) values ('"
					+ article.getTitle() + "','" + article.getContent() + "');";
			stmt.executeUpdate(sql);
			conn.close();
		} catch (Exception e) {
			System.out.println("error");
		}
	}
```

## 对应Servlet

```java
public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		Article article = new Article();
		article.setTitle(title);
		article.setContent(content);

		new ArticleService().save(article);

		response.sendRedirect("/04_mvc/article/suc.jsp");
	}
```

## 优缺点

- 优点

    使用Bean来封装内容,不需要方法重载

- 缺点

    命令式编程,与OO相违背

## 贫血模型

- 贫血模型只是将业务代码移到了JavaBean中

## 贫血模型的优缺点

- 优点：

    1. 各层单向依赖，结构清楚，易于实现和维护
    2. 设计简单易行，底层模型非常稳定


- 缺点：

    1. domain object的部分比较紧密依赖的持久化domain logic被分离到Service层，显得不够OO
    2. Service层过于厚重

## 充血模型

- 将数据库操作也包含到了Model中

## 充血模型的优缺点

- 优点：

    1. 更加符合OO的原则
    2. Service层很薄，只充当Facade的角色，不和DAO打交道。

- 缺点：

    1. DAO和domain object形成了双向依赖，复杂的双向依赖会导致很多潜在的问题。
    2. 如何划分Service层逻辑和domain层逻辑是非常含混的，在实际项目中，由于设计和开发人员的水平差异，可能导致整个结构的混乱无序。

## 胀血模型

- 去除了Service层

## 胀血模型的优缺点

- 优点：

    1. 简化了分层
    2. 也算符合OO

- 缺点：

    1. 很多不是domain logic的service逻辑也被强行放入domain object ，引起了domain ojbect模型的不稳定
    2. domain object暴露给web层过多的信息，可能引起意想不到的副作用。

## 事务管理

- 在应用中有些操作涉及多个操作,需要保证操作的一致性
- 例如:在博客应用中,删除一篇文章,那么文章对应的评论也就没有用处,可一起删除
- 此操作需要保证文章和评论删除操作的一致性.

## 目前问题

- 所有的数据库连接都是在DAO里创建,代码重复,耗时且不可统一管理
- 连接池可解决耗时问题(后续框架处理)
- 统一管理连接,方便事务管理

## ConnectionUtil统一管理连接

```java
public class ConnectionUtil {

	public static Connection getConnection() {
		Connection conn = null;
		try {
			String url = "jdbc:mysql://localhost:3306/blog?characterEncoding=utf8";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
```

## 重构ArticleDAO

```java
public class ArticleDAO {

	private Connection conn;

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void save(Article article) {
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into articles (title,content) values ('"
					+ article.getTitle() + "','" + article.getContent() + "');";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("error");
		}
	}
}
```

## 重构ArticleService

```java
public class ArticleService {

	public void save(Article article){
		ArticleDAO articleDAO = new ArticleDAO();
		Connection conn = ConnectionUtil.getConnection();
		articleDAO.setConn(conn);
		articleDAO.save(article);
	}
}
```

## 无事务管理

```Java
public class ArticleService {

	public void save(Article article){
		ArticleDAO articleDAO = new ArticleDAO();
		Connection conn = ConnectionUtil.getConnection();
		articleDAO.setConn(conn);

    //如下的两次保存要保持统一,是否可行?
		articleDAO.save(article);
		if(true){
			throw new RuntimeException();
		}
		articleDAO.save(article);
	}
}
```

## 添加事务处理
```java
public void save(Article article){
		ArticleDAO articleDAO = new ArticleDAO();
		Connection conn = ConnectionUtil.getConnection();
		articleDAO.setConn(conn);

		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		articleDAO.save(article);
		if(true){
			throw new RuntimeException();
		}
		articleDAO.save(article);

		try {
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
```

## 总结

- Servlet知识
- JSP知识
- MVC

## 课后作业

- 修改博客程序为MVC架构


