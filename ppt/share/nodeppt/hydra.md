---
typora-root-url: ./
typora-copy-images-to: files\hydra
---

title: Hydra使用与实现
speaker: 王一帆
url: http://ivaneye.com
transition: stick
theme: dark

[slide]
# Hydra使用与实现
## 王一帆

[slide]
# 从编写一个服务开始

- Step1:新建Maven项目
- Step2:编写接口与实现
- Step3:编写spring-context.xml
- Step4:打包

[slide]
## Step1:新建Maven项目

- 在pom中引入如下依赖:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>3.2.10.RELEASE</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.focustech.hydra.servicenode</groupId>
    <artifactId>hydra-annotation-api</artifactId>
    <version>3.4.11</version>
    <scope>provided</scope>
</dependency>
```

- hydra-annotation-api为Hydra提供的服务开发包！
- 注意：服务节点默认提供了Spring支持(还包括数据源等支持)，此处scope请设置为**provided**，重要！！！

[slide]
## 服务节点默认支持

- Spring
    - spring-orm:spring-jdbc,spring-tx
    - spring-aspects:aspectjweaver
    - spring-web:spring-beans,spring-aop,aopalliance,spring-core
    - spring-context-support:spring-context
- commons-dbcp2:commons-pool2
- velocity:commons-collections
- commons-lang
- ojdbc6
- mysql-connector-java
- slf4j-log4j12:slf4j-api,log4j

[slide]
## 问题

如果在项目中引入了服务节点默认提供的依赖，且没有设置provided。则可能会出现**找不到对象**的问题!


[slide]
## 原因

![container](/files/hydra/container.jpg)

[slide]
## 几点说明

Hydra依据Maven的依赖关系来管理服务依赖:

1. 除了Spring，日志和Hydra开发包依赖必须以provided方式引入外。其余包均可以默认方式引入。
2. 如果一个服务依赖了另一个服务，则建议两个服务一起发布。由于是依赖Maven的依赖关系，假设A依赖B，当发布A时，B会一起被发布，故不需要再次发布B
3. 如果想将多个服务打包发布，可建立一个父Maven项目，引入需要发布的服务，发布此父项目即可

[slide]
## 版本管理

如果需要自定义版本信息，请配置jar插件。如果不需要自定义版本信息，可忽略此步骤(**请不要这么干**)！

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

此配置会在MANIFEST.MF中添加类似如下信息:

- Implementation-Title: hello
- Implementation-Version: 1.0.0
- Implementation-Vendor-Id: com.focustech.hydra

服务节点会以此获取版本号，否则以发布时间作为版本号！

[slide]
## Step2:编写接口与实现

```java
//接口
public interface Hello {
    String say(String name);
}
```
```java
//实现
import com.focustech.hydra.annotation.HydraService;

@HydraService(desc="这是个测试服务")
public class HelloImpl implements Hello {

    public String say3(String name) {
        return "1.0.0 From HelloImpl = " + name;
    }
}
```

[slide]
## 说明

- 接口和实现最好放在不同的模块下，例如:prj-api,prj-impl
- prj-api提供给客户端使用。客户端和服务端基于接口编程，开发解耦
- HydraService注解标注的类才会被发布为服务

[slide]
## Step3:编写spring-context.xml

- 在resources目录下，新建spring-context.xml文件,文件名及文件路径不能变！

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
  http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  http://www.springframework.org/schema/context/spring-context.xsd">

  <context:component-scan base-package="com.focustech.hello" name-generator="com.focustech.hydra.processor.HydraServiceBeanNameGenerator"/>
 </beans>
```

- **name-generator必须配置，用以生成服务名称!**

[slide]
## 说明

- 此配置和spring项目的配置完全相同，就是借助了Spring的加载机制，进行服务的发布操作
- base-package配置服务所在的包，扫描查找HydraService注解
- HydraServiceBeanNameGenerator继承了AnnotationBeanNameGenerator，覆写了determineBeanNameFromAnnotation方法，针对被HydraService注解的类，生成的beanName为该类的全限定名。而不是类名首字母小写。例如上面的beanName就是com.focustech.hydra.Hello
- 注意是Hello不是HelloImpl。HydraService使用接口作为服务名，而不是实现类作为服务名。
- 如果一个实现类实现了多个接口，那么默认找第一个接口。可以通过interfaces来指定接口
- 此全限定名就是用来和从客户端发送的服务调用请求进行对应匹配的

[slide]
## Step4:打包

```
mvn clean deploy
```

- 建议每次都clean，好像eclipse的Maven编译不clean的话，有老代码不会更新

[slide]
## 调试

- 基于Maven插件，提供了与运行时相同环境的测试环境
- 包括：公共bean配置，服务集发布，服务列表查询，客户端查询
- 不包括：黑白名单配置，服务权重配置等功能，本地测试没需求

[slide]
## 调试方法

- 引入插件
- 配置
- 执行
- 测试

[slide]
## 引入插件

```xml
<plugin>
    <executions>
        <execution>
            <id>run</id>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
    <groupId>com.focustech.hydra.servicenode</groupId>
    <artifactId>hydra-servicenode-runner-plugin</artifactId>
    <version>3.4.11</version>
</plugin>
```

[slide]
## 配置

```xml
<plugin>
    ...
    <configuration>
        <hydra_version>3.4.11</hydra_version>
        <hydra_maven_local_repository>D:/maven/repository</hydra_maven_local_repository>
        <hydra_base_serviceassembly>com.focustech.hydra.assembly:hydra-assembly-base:3.4.11</hydra_base_serviceassembly>
        <registry_localcallbackaddr>192.168.5.65</registry_localcallbackaddr>
        <hydra_beans>[{"name":"dataSource","className":"org.apache.commons.dbcp2.BasicDataSource","props":{"username":"root","driverClassName":"com.mysql.jdbc.Driver","`pasword":"Yw5dKYYBJKDJILjdjkf","url":"jdbc:mysql://192.168.0.191:3306/hydra"}}]</hydra_beans>
        <hydra_deploy_service>com.focustech.hydra:otherServer:1.0.0</hydra_deploy_service>
    </configuration>
</plugin>
```

[slide]
## 说明

- **这个插件需要配置在服务所在的模块中！**
- hydra_version：插件启动时所构建的hydra环境是哪个版本的
- hydra_maven_local_repository：hydra从Maven下载的jar放到哪里
- hydra_base_serviceassembly：基础jar包。注意这里是替换。如果你需要新增基础jar包，你需要包含hydra-assembly-base，逗号隔开。因为这是替换
- registry_localcallbackaddr：注册到注册中心的Ip地址
- hydra_beans：配置的公共bean
- hydra_deploy_service：除了项目服务集，如果要发布其它服务集，可以在这里配置

[slide]
## 执行

- **在服务所在模块目录下执行**：
```
clean hydra-servicenode-runner:run
```

[slide]
## 做了什么？

- 启动一个内存注册中心，并提供简易命令行！支持命令
    - ls service : 打印注册过来的服务
    - ls client : 打印注册过来的客户端
    - exit/quit : 退出
    - help : 帮助
- 在target目录下构建hydra目录结构
- 下载服务所需要的所有依赖包，包括配置的hydra_base_serviceassembly和hydra_deploy_service
- 启动服务节点
- 依据hydra_beans创建公共bean
- 发布服务，包括hydra_deploy_service
- 所有服务注册到内存注册中心中

[slide]
## 测试调用

- 使用客户端调用
- 编写测试代码(与客户端代码相同)

[slide]
# 客户端开发(基于Spring)

- 引入依赖(支持硬编码，Spring,LTW方式，引入不同的依赖)
- 配置
- 调用

[slide]
## 引入依赖

```xml

<dependency>
    <groupId>com.focustech.hydra.client</groupId>
    <artifactId>hydra-client-spring</artifactId>
    <version>3.4.11</version>
</dependency>
<!-- 由于需要调用上面的Hello服务，所以需要引入上面的依赖！-->
<dependency>
    <groupId>com.focustech.hydra</groupId>
    <artifactId>hello-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

[slide]
## 配置一

- Spring配置文件添加如下配置:
```xml
<!--多个包以逗号隔开-->
<bean class="com.focustech.hydra.client.spring.scanner.MockerScannerRegister">
    <property name="basePackage" value="${path.to.ServiceInterfacePackages}"/>
</bean>
```

- 指向hello-api中的接口所在的包
- 在启动时，会针对这些接口自动生成代理类，添加到Spring容器中(类似Mybatis的Mapper构建流程)
- 生成的代理类，作用是构建消息，发生到指定的服务节点

[slide]
## 配置二

- strategy.properties
```properties
App=hydra_client
SubscribeAddress=192.168.6.78
SubscribePort=8997
...
```

- App是应用名，和ip构成唯一id。为了解决一台机器上部署两个应用的场景（这里老版本Hydra有个问题，注册中心为其配置的字段长度较短，无法支持较长的App名称。导致客户端注册失败！现已修改为100，可检查hydra_client表的name字段确认。）
- SubscribeAddress和SubscribePort确定注册到哪个注册中心上去

[slide]
## 调用

```java
@Component
public class Test {

    @Autowired
    public Hello hello;

    public void run() {
        System.out.println(hello.say("Name"));
    }
}
```

[slide]
## 做了什么

- 应用启动时扫描接口包，生成代理类。注意：**当在本地测试环境中实现了Hello接口，那么测试时Spring自动注入本地实现！**，后续调用就是直接调用本地实现
- 如果没有调用BeanFactory.init()方法，那么在第一次调用服务的时候会进行初始化！
- 初始化的动作如下
    - 加载strategy.properties
    - 从注册中心获取服务列表
    - 将服务列表保存到cache/cache3目录下*.hydra文件里
- 构建调用信息：接口的全限定名+方法名+方法参数+接口所在的jar的版本
- 根据接口全限定名+接口，到服务列表中找匹配的服务
    - 是否全匹配
    - 如果不匹配，找版本号最新的
- 将调用信息发送到匹配到的服务节点去
- 服务端执行服务，返回结果

[slide]
# 环境部署

- PTL,UMC和Hydra的区别
- 几个概念
- 关键配置

[slide]
## PTL,UMC和Hydra的区别

**umc-ptl.vemic.com/workspace.do** 是一个整合页面！

- PTL：是一个权限管理系统
- UMC：UMC是平台架构部开发的一套可视化项目发布与监控系统
- Hydra：分布式服务框架

[slide]
## 几个概念

- **bundle**:bundle是由于Hydra2使用了OSGi进行实现，**bundle是OSGi里定义的概念**。而在Hydra3中已放弃OSGi，所以没有bundle的概念，类似的组件为“服务集”！
- **实例**：UMC中的”实例“指的是其发布的项目，例如：通过UMC发布的Hydra3就是UMC的一个实例。如果是Hydra里的实例，则特指"服务集实例"
- **服务节点**：Hydra中的概念，指部署到机器上的，可以发布“服务集”的运行时容器
- **管理中心**：对应Hydra2的注册中心，在注册中心的基础上添加了对服务节点、服务、客户端的管理与配置功能
- **服务集定义**：通过Maven的groupId,artifactId,version定位的jar及其所有compile范围的jar包集合。这是一个逻辑概念
- **服务集实例**：发布到服务节点上的服务集，这是一个物理概念，即服务集定义的运行时！

[slide]
## 关键配置

- 注册中心
    - BuildParams_serverPort：注册中心web页访问端口
    - BuildParams_hydra.SubscribeAddress：注册中心提供注册的Ip（本机IP）
    - BuildParams_hydra.SubscribePort：注册中心提供注册的端口

- 服务节点
    - BuildParams_hydra.pigeon.serverPort：提供给客户端调用的端口
    - BuildParams_registry.address:对应注册中心的SubscribeAddress
    - BuildParams_registry.port：对应注册中心的SubscribePort

[slide]
# 服务发布与调用

- 整体架构
- 通信
- 协议
- 序列化
- 流程

[slide]

![](/files/hydra/hydra.jpg)

[slide]

![container](/files/hydra/container.jpg)

[slide]
## 通信

![nio](/files/hydra/nio.jpg)

[slide]
## 协议

- 协议头(定长)
    - body.length
    - containerId
    - timeout
- 协议体
    - 调用的具体服务信息

[slide]
## 序列化

- **protostuff** {:&.moveIn}
- 为什么选择protostuff?
- 序列化不需要结构描述文件（例如:protobuf的*.proto）！

[slide]
## 取舍

- 由于Hydra是框架，无法知道业务数据结构，也就没法提前定义结构描述文件。所以只能运行时去获取数据结构
- 导致的问题是，如果客户端与服务端两边的序列化数据结构不相同，则会报序列化错误！
- 所以必须要保证客户端与服务端两边的Model是一致的！
- 这也是要在服务上设置版本号的一个原因

[slide]
## 发布流程

**界面操作**:
- 在服务集定义界面新建一条记录
- 点击发布，进入发布页面
- 选择需要发布到哪些服务节点，设置发布版本号，一些额外属性
- 点击发布

**系统流程**：
- **管理中心**：保存服务集定义至数据库，只有数据库操作
- **管理中心**：将服务集发布到指定服务节点
- **服务节点**：接收到消息，从Maven仓库下载指定的服务集及所有依赖包

[slide]
## 启动服务

**界面操作**:
- 在服务实例列表，或发布成功页面，点击启动

**系统流程**：
- **管理中心**：发送消息给对应的服务节点，进行服务启动
- **服务节点**：将服务集和依赖包拷贝到work目录下，以服务集实例id为名称的目录下
- **服务节点**：启动容器，加载对应目录下的jar
- **服务节点**：构建服务列表信息，返回给管理中心
- **管理中心**：接收到服务列表，保存到数据库。并推送到所有连接上来的客户端
- **客户端**：更新本地服务列表

[slide]
## 服务列表补充说明

- 客户端接收到服务列表后会将其写入到本地的cache/cache3目录下，\*.hydra文件
    - 如果此文件没数据，说明客户端获取服务列表信息失败。请检查客户端与注册中心的通信
- 管理中心每隔30秒会推送服务列表至客户端
    - 避免客户端首次启动，获取服务列表失败后无法调用服务的问题
    - 管理中心会维护一个服务列表的缓存，只有服务节点的服务出现变化的时候，才会触发变更，从数据库中重新拉取数据。避免频繁查询数据库，影响管理中心的性能
    - 客户端在接收到服务列表后，会首先比对列表hash，如果和本地的不同才会更新服务列表，否则直接忽略
- 服务列表的变更完全由服务节点的相关操作决定
    - 服务节点里服务的启动、停止都会首先触发服务列表的变更
    - 如果整个节点停止，注册中心通过心跳检测到节点停止，会自动更新服务列表，并将最新的服务列表推送到客户端


[slide]
## 停止服务

**界面操作**:
- 在服务实例列表，或发布成功页面，点击停止

**系统流程**：
- **管理中心**：置对应服务的状态为停止，并推送服务列表至客户端
- **管理中心**：等待3秒后，发送消息给对应的服务节点，进行服务停止
- **服务节点**：停止容器，销毁加载的资源

[slide]
## 优雅启停

- 在服务启动的过程中，服务节点在完全启动服务后，才推送服务列表至管理中心，管理中心再推送至客户端。确保客户端在服务启动后才能调用服务
- 在服务停止的过程中，管理中心先置服务的逻辑状态，并推送服务列表至客户端。确保客户端先停止对即将停止的服务的调用。推送完成后，等待三秒后，再通知服务节点去停止(可能有些服务已经发送到了服务节点，正在执行)。
- 在新版本中，服务节点上新增了一个是否对外服务的按钮。确保节点的优雅启停。假设要使用UMC升级服务节点。可以在服务节点列表先点击停止对外服务，此操作会将该节点所有的服务从服务列表中剔除，并推送服务列表至客户端。然后再去UMC停止节点，可以避免在停止服务节点的过程中，客户端调用的问题

[slide]
## 调用流程

**操作**:
- 直接通过hydra客户端API调用即可

**系统流程**:
- **客户端**：根据调用，构建调用服务名称+版本号
- **客户端**：从本地服务列表中查找对应的服务。查找规则如下：
    - 首先，完全匹配查找服务
    - 如果找不到，则查找本地的最新版本的服务
    - 最新版本的比较规则为：前面的三位为数字比较，后面为字符串比较。例如:3.0.9-beta9大于3.0.9-beta10,小于3.0.10-beta9
- **客户端**：根据找到的服务，向对应的服务节点发起服务调用
- **服务节点**：首先解析协议头，找到对应的容器id。将请求直接丢给对应容器处理
- **服务节点**：容器根据服务名称，找到对应的方法进行调用。将调用结果返回
- **客户端**：接收到结果，进行后续处理

[slide]
## 超时

- 客户端发送服务调用后，会等待服务端的响应
- 此时客户端维护了一个超时监控列表，会有线程不断的扫描这个列表。当超过设置的超时间后还没有得到响应就会抛超时异常
    - **client-side**：如果消息本身没发出去，则报客户端超时。出现此类问题，一般为连接问题。及客户端与服务端的连接不通导致
    - **server-side**：如果消息发送出去了，但是指定时间内没有得到响应，则报服务端超时。出现此类问题，一是可能服务本身调用超时，需查看服务端日志，根据requestId找到对应的日志查看执行时间；二是服务端队列阻塞。可以到UMC查看队列信息来确定。

[slide]
# 日志

- 客户端日志
- 管理中心日志
- 服务节点日志

[slide]
## 客户端日志

- 客户端日志根据客户端应用的配置
- 日志输出主要有两个包：
    - com.focustech.hydra：hydra相关
    - com.focustech.pigeon：通信相关

例如：注册中心推送服务列表信息至客户端，日志输出在com.focustech.hydra下。有类似"Receive serviceinfo from Registry!"字样的日志输出,info级别。如果需要更新，则有类似"Y(^\_^)Y load serviceinfo success from registrycenter"字样的日志输出，info级别！同时会打印出接收到的数据！

[slide]
## 管理中心日志

- 管理中心日志
    - naja.log:naja框架日志，包括pigeon日志
    - registry.log:注册中心日志
    - hydra_conn_info.log:注册中心连接日志

[slide]
## 服务节点日志

- 服务节点日志有
    - hydra_debug.log
    - hydra_info.log
    - hydra_conn_info.log：客户端调用的连接信息
    - hydra_error.log

[slide]
# 问题及解决

[slide]
## 何时依赖hydra-client-adapter，何时依赖hydra-client

目前发现大家的项目都是通过模板进行初始生成的。模板考虑了Hydra2和Hydra3共存的情况。所以目前发现项目组所有的应用都是依赖的是hydra-client-adapter这个包！

实际上hydra-client-adapter这个依赖主要是用来解决Hydra2和Hydra3共存的问题的，是一个折中解决方案。**如果项目中只用Hydra3的话，强烈建议使用hydra-client的依赖！**

[slide]
## 何时strategy.properties生效,何时strategy3.properties生效

当依赖了hydra-client-adapter，并使用了其中的BeanFactory时，strategy3.properties才会生效！否则strategy.properties生效！

[slide]
## strategy.properties/strategy3.properties的项目路径

strategy3.properties应该放置在classpath的根目录下。如果是Maven项目则放置在resources根目录下！

之前发现edt项目的strategy3.properties是放置在source的根目录下，默认情况下,maven是不会将其下的properties文件拷贝到target目录下的。导致打包发布后，strategy3.properties文件不存在，继而出现getBean error的错误！

将strategy3.properties文件放至正确的位置即可解决此问题！

[slide]
## 客户端无法找到服务

目前出现此问题的原因及解决方案如下：

1. 在服务集实例列表中查看此服务集是否为对私服务，是->2，否->3
2. 查看白名单数量是否为0！是->4,否->5
3. 查看服务集下的服务数量是否为0，是->6，否->7
4. 将客户端添加到白名单中
5. 查看客户端缓存cache3目录下，数字最大的.hydra文件，文件中是否有调用的服务(有乱码，但是能通过名称搜索到调用的服务名称)！是->7，否->8
6. 服务启动失败，请到服务节点下，查看日志信息，进行错误信息的定位(日志文件地址：hydra自身的日志文件hydra_debug.log,hydra_error.log这两个文件的路径，请查看UMC实例中配置的log路径。业务日子对应的路径，请查看Hydra菜单中，Appender中配置的路径)
7. 查看服务的添加时间是否为服务集的启动时间(在0.0.16版本的debug模式下，每次启动会自动删除服务集下所有的服务，再次添加)。是->10，否->9
8. 清空本地cache目录，再重启客户端，再次查看缓存文件
9. 可能为本地注册时间较长，稍等片刻后再次调用；如长时间失败，则重启本地debug
10. 是否为多线程并发初始化，多线程情况下，对初次调用有些问题，会进行修复。临时解决方案是在并发前先触发一次调用，使得服务列表已经缓存到了本地

[slide]
## debug模式下客户端调用报方法出现参数不匹配的问题

主要原因为本地接口修改后，没有进行install！目前项目分api和impl，debug的配置在impl中，impl依赖了api！在debug的时候，会根据依赖从Maven仓库中查找依赖的api！如果修改了api，没有install，则获取的依然是老的api，导致实现与依赖不匹配！

[slide]
## 新版本解决的问题

- 同版本服务集重新发布后无法成功启动
- 重启服务集页面假死
- 无法在管理中心对本地debug注册上来的服务进行操作(使用本地debug)
- debug下公共bean的推送(使用本地debug)
- debug模式下添加/删除黑白名单失败(使用本地debug)

[slide]
# 谢谢
