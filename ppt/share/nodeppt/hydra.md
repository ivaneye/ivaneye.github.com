title: Hydra实现与使用
speaker: 王一帆
url: http://ivaneye.com
transition: stick
theme: dark

[slide]
# Hydra实现与使用
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
- 注意:服务节点默认提供了Spring支持(还包括数据源等支持)，此处scope请设置为provided，重要！！！

[slide]
## 几点说明

Hydra依据Maven的依赖关系来管理服务依赖:

1. 除了Spring，日志和Hydra开发包依赖必须以provided方式引入外。其余包均可以默认方式引入。
2. 如果一个服务依赖了另一个服务，则建议两个服务一起发布。由于是依赖Maven的依赖关系，假设A依赖B，当发布A时，B会一起被发布，故不需要再次发布B
3. 如果想将多个服务打包发布，可建立一个父Maven项目，引入需要发布的服务，发布此父项目即可

[slide]
## 版本管理

如果需要自定义版本信息，请配置jar插件。如果不需要自定义版本信息，可忽略此步骤！

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
## Step4:打包

```
mvn clean deploy
```

[slide]
## 调试

- 基于Maven插件，提供了与运行时相同环境的测试环境

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
## 执行

- **在服务所在项目根目录下执行**：
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
- 下载服务所需要的所有依赖包，包括配置的hydra_base_serviceassembly和hydra_deploy_service
- 启动服务节点
- 依据hydra_beans创建公共bean
- 发布服务，包括hydra_deploy_service
- 所有服务注册到内存注册中心中

[slide]
## 测试

- 使用客户端调用
- 编写测试代码(与客户端代码相同)

[slide]
# 客户端开发(基于Spring)

- 引入依赖
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
## 配置

- Spring配置文件添加如下配置:
```xml
<!--多个包以逗号隔开-->
<bean class="com.focustech.hydra.client.spring.scanner.MockerScannerRegister">
    <property name="basePackage" value="${path.to.ServiceInterfacePackages}"/>
</bean>
```

- strategy.properties
```properties
App=hydra_client
SubscribeAddress=192.168.6.78
SubscribePot=8997
...
```

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

- **当在本地测试环境中实现了Hello接口，那么测试时Spring自动注入本地实现！**

[slide]
# 环境搭建

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

- bundle:bundle是由于Hydra2使用了OSGi进行实现，**bundle是OSGi里定义的概念**。而在Hydra3中已放弃OSGi，所以没有bundle的概念，类似的组件为“服务集”！
- 实例：UMC中的”实例“指的是其发布的项目，例如：通过UMC发布的Hydra3就是UMC的一个实例。如果是Hydra里的实例，则特指"服务集实例"
- 服务节点：Hydra中的概念，指部署到机器上的，可以发布“服务集”的运行时容器
- 管理中心：对应Hydra2的注册中心，在注册中心的基础上添加了对服务节点、服务、客户端的管理与配置功能
- 服务集定义：通过Maven的groupId,artifactId,version定位的jar及其所有compile范围的jar包集合。这是一个逻辑概念
- 服务集实例：发布到服务节点上的服务集，这是一个物理概念，即服务集定义的运行时！

[slide]
## 关键配置


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
