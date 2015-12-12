---
layout: post
title: 实时发布-嵌入式OSGi的应用
categories: [architecture]
tags: [architecture,osgi]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 场景

单机应用已经越来越不能符合目前越来越复杂的产品需求了。即使是小型应用，至少也需要部署2台以上的服务器做集群。且应用必须24小时对外服务,可用性得达到n个9。这就对发布有了更高的要求。

也就催生了灰度发布这样的发布过程。而即使是这样，还是需要经历大致如下的发布过程:

- 下载代码
- 打包
- 停止服务器
- 部署
- 启动服务器

而业界一直诟病JVM的启动速度，再加上如果项目比较大，编译过程比较长，发布机器比较多，那么做一次完整的发布可能需要几个小时。万一中途出了问题，要回退，又要几个小时。

是否可以解决这样的问题呢？而OSGi恰是一个不错的选择!

# OSGi

OSGi是一个优雅、完整和动态的组件模型，提供了完整的模块化运行环境。

应用程序（称为bundle）无需重新引导可以被远程安装、启动、升级和卸载。

其主要应用在嵌入式开发中，而在JavaSE和JavaEE方面则少有建树。其最著名的使用就是eclipse了。究其原因主要有:

- 增加开发难度：需要开发人员更关心模块的划分，处理模块与模块之间的依赖关系(模块间的导入导出),这是一个好的方面，但是
- 没有完善的工具：模块间的依赖关系需要开发人员手动处理(有相应的工具，但不能百分百处理依赖关系)。
- 额外的运行环境：应用(bundle)需要运行在实现了OSGi规范的容器内，导致了模块间的依赖关系需要在运行时才能验证是否有问题。也就是说无法在编译期验证模块间的关系。同时也增加了测试及调试的难度。

可以看出，OSGi的主要缺点是开发较繁琐。而针对前面所提到的问题，OSGi解决了如下几个问题:

- 项目模块化，对于项目的更新与发布不再需要发布整个项目，只需要发布需要更新的模块即可。提高了编译打包的速度。
- OSGi可在运行时的对bundle进行安装、启动、升级和卸载。提高了部署的速度。(这里就需要吐槽一下eclipse了，它是基于osgi的，但是每次安装插件都要重启是要搞哪样？！)
- 支持多版本发布。在OSGi容器内可发布相同应用的不同版本

OSGi是如何做到这些的呢？其实OSGi实现了一套自身的ClassLoader，具体可见[此文]({% post_url /tech/language/java/jvm/2015-05-20-classloader %})

# OSGi容器

目前OSGi容器主要有Knopflerfish, Apache Felix, Equinox, Spring DM。其具体比较请见[此文](http://www.cnblogs.com/longkerdandy/archive/2010/09/29/OSGi_Compare.html)

以及其上的一些应用，方便在OSGi上进行开发，比如Karaf,ServiceMix等。

<!-- more -->

# OSGi的使用方式

OSGi容器有两种使用方式:

- 作为容器使用：

        OSGi容器作为外层，所有的应用均部署在OSGi容器内。那么所有的应用都需要bundle化，但是上面说了,bundle化不是一个方便的过程。
        且OSGi在非嵌入式领域并不是很流行，虽然之前业界一直在推广，但最终效果并不理想，Spring最后也放弃了对OGSi的支持。
        所以当你的应用较大时，bundle化会是一个比较大的绊脚石。

- 嵌入式使用

        基于上面的原因，我们可以将OSGi容器作为嵌入式容器使用，即基本的模块在OSGi外部运行，也就不需要bundle化了，
        变动比较频繁的模块部署到OSGi容器内，使用OSGi便利的部署机制。
        比如:项目中依赖的Spring,Mybatis等jar包可以在OSGi容器外部署，而业务模块则部署到OSGi容器内

# Felix安装

这里使用felix作为OSGi容器来演示嵌入式OSGi的使用！felix可到Apache网站下载!

felix目录结构如下:

```
-bin:felix.jar路径，其实felix只需要这个jar包就可以运行了
-bundle:部署的bundle目录,如果你有需要部署的bundle，将其拷贝到此目录下，启动felix时会自动部署
-cache:bundle缓存目录
-conf:配置文件目录
-doc:相关文档
```

在根目录运行如下命令即可启动

```
java -jar bin/felix.jar
```

- bundle目录下默认有四个bundle，提供了类似命令行功能。启动时自动部署了。可以输入lb，来查看已安装的bundle

```
g! lb
START LEVEL 1
   ID|State      |Level|Name
    0|Active     |    0|System Bundle (5.4.0)|5.4.0
    1|Active     |    1|Apache Felix Bundle Repository (2.0.6)|2.0.6
    2|Active     |    1|Apache Felix Gogo Command (0.16.0)|0.16.0
    3|Active     |    1|Apache Felix Gogo Runtime (0.16.2)|0.16.2
    4|Active     |    1|Apache Felix Gogo Shell (0.10.0)|0.10.0
g!
```

这是普通的使用felix的方式。不做过多介绍。主要介绍嵌入式Felix的应用。

# 嵌入启动Felix

## 创建Maven项目

- 首先创建一个普通的Maven项目
- 在pom.xml中添加felix依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.felix</groupId>
        <artifactId>org.apache.felix.main</artifactId>
        <version>5.4.0</version>
    </dependency>
</dependencies>
```

## 编写启动类

- 启动felix的核心代码如下

```java
FrameworkFactory factory = getFrameworkFactory();
m_fwk = factory.newFramework(configProps);
m_fwk.init();
AutoProcessor.process(configProps, m_fwk.getBundleContext());
m_fwk.start();
m_fwk.waitForStop(0);
System.exit(0);
```

- getFrameworkFactory()方法通过jdk6的ServiceLoader来加载FrameworkFactory实现，并实例化返回，具体代码如下
- 通过configProps来构建Framework，configProps是Map<String, String>类型，里面为felix及osgi相关配置，具体配置后面介绍
- 初始化Framework
- 配置属性和发布bundle
- 启动Framework

```java
//getFrameworkFactory()方法实现代码
private static FrameworkFactory getFrameworkFactory() throws Exception {
    URL url = Main.class.getClassLoader().getResource(
                                                        "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
    if (url != null) {
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            for (String s = br.readLine(); s != null; s = br.readLine()) {
                s = s.trim();
                // Try to load first non-empty, non-commented line.
                if ((s.length() > 0) && (s.charAt(0) != '#')) {
                    return (FrameworkFactory) Class.forName(s).newInstance();
                }
            }
        } finally {
            if (br != null)
                br.close();
        }
    }
    throw new Exception("Could not find framework factory.");
}
```

## Felix属性

框架属性

- org.osgi.framework.executionenvironment - osgi执行JVM环境，不必特殊设置
- org.osgi.framework.storage - bundle缓存的完整路径，可使用下面的felix.cache.rootdir作为前缀拼接
- felix.cache.rootdir - bundle缓存的root地址
- org.osgi.framework.storage.clean - 是否需要刷新bundle缓存，"none"或"onFirstInit"，默认为"none"
- felix.cache.filelimit - 限制bundle缓存数量，默认是0，即无限制
- felix.cache.locking - 是否开启锁，限制并发访问，默认开启
- felix.cache.bufsize - 设置缓存的缓冲区大小，默认4096
- org.osgi.framework.system.packages - 以逗号隔开的包名，来确定哪些包通过系统bundle来加载，就是lb命令列出的第一个bundle
- org.osgi.framework.system.packages.extra - 和org.osgi.framework.system.packages功能相同，放额外的包
- org.osgi.framework.bootdelegation - 以逗号隔开的包(支持模糊匹配，上面两个属性不支持)，委托给父ClassLoader加载(由org.osgi.framework.bundle.parent定义)，OSGi容器内的bundle不需要Import即可使用此包。OSGi不建议使用此属性，破坏了模块化
- org.osgi.framework.bundle.parent - 指明哪个ClassLoader将用来加载bootdelegation属性所指定的包。boot表示启动的根ClassLoader,app表示应用ClassLoader,ext表示ExtClassLoader,framework表示容器的ClassLoader,默认是boot
- felix.bootdelegation.implicit - 配置容器是否要判断哪些包是否是delegate的，默认开启
- felix.systembundle.activators - 用来配置系统Bundle的启动器对象，这个配置只能通过类来配置，不能通过配置文件，因为设置的值是个对象实例
- felix.log.logger - 设置一个org.apache.felix.framework.Logger实例，同样只能通过类来配置
- felix.log.level - 日志级别(1 = error, 2 = warning, 3 = information, and 4 = debug). 默认为1
- org.osgi.framework.startlevel.beginning - 框架启动级别，默认为1
- felix.startlevel.bundle - bundle启动级别，默认为1
- felix.service.urlhandlers - 是否开启URL Handler，默认开启。开启后会调用URL.setURLStreamHandlerFactory()和URLConnection.setContentHandlerFactory()

启动属性

- felix.auto.deploy.dir - 配置自动部署bundle的目录，默认为当前目录下的bundle目录
- felix.auto.deploy.action - 使用一个以逗号隔开的字符串配置在auto-deploy目录中的bundle所要执行的动作，包括install, update, start和uninstall。如果没有配置，或者配置出错，则auto-deploy目录中的bundle将不会做任何动作
- felix.auto.install.<n> - 空格隔开的bundle URL，<n>是启动级别，当启动级别低于felix.startlevel.bundle设置的值，则自动安装
- felix.auto.start.<n> - 空格隔开的bundle URL，<n>是启动级别，当启动级别低于felix.startlevel.bundle设置的值，则自动安装并启动
- felix.shutdown.hook - 配置是否需要一个关闭钩子，来进行关闭时的清理工作。默认为true

# 与Felix交互

## 构建bundle

- 创建Maven项目，在pom.xml配置如下插件,打包方式为bundle

```xml
<packaging>bundle</packaging>

...

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.felix</groupId>
            <artifactId>maven-bundle-plugin</artifactId>
            <version>2.5.3</version>
            <extensions>true</extensions>
            <configuration>
                <instructions>
                    <Bundle-Name>demo</Bundle-Name>
                    <Bundle-SymbolicName>demo</Bundle-SymbolicName>
                    <Implementation-Title>demo</Implementation-Title>
                    <Implementation-Version>1.0.0</Implementation-Version>
                    <Export-Package></Export-Package>
                    <Import-Package></Import-Package>
                    <Bundle-Activator>org.embedosgi.activator.Activator</Bundle-Activator>
                </instructions>
            </configuration>
        </plugin>
    </plugins>
</build>
```

- Export-Package和Import-Package为空，说明此bundle不对外导出任何包，也不导入任何包
- Bundle-Activator定义了一个启动器org.embedosgi.activator.Activator,它包含了在OSGi容器启动此bundle时需要做的处理，代码如下

```java
package org.embedosgi.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by wangyifan on 2015/11/9.
 */
public class Activator implements BundleActivator {
    public void start(BundleContext bundleContext) throws Exception {
        Dictionary<String,String> dict = new Hashtable<String, String>();
        String version = bundleContext.getBundle().getVersion().toString();
        dict.put("version",version);
        Object bean = Class.forName("org.embedosgi.demo.impl.HelloImpl").newInstance();
		bundleContext.registerService("org.embedosgi.demo.Hello", bean, dict);
        System.out.println("Reg Hello Service End!" + version);
    }

    public void stop(BundleContext bundleContext) throws Exception {

    }
}

```

其主要作用就是将HelloImpl对象对外发布为Hello服务，并设置版本号为自身bundle的版本号，即在pom.xml中设置的Version

Hello和HelloImpl很简单

```java
package org.embedosgi.demo;

/**
 * Created by wangyifan on 2015/11/9.
 */
public interface Hello {
    String say(String name);
}

package org.embedosgi.demo.impl;
import org.embedosgi.demo.Hello;

/**
 * Created by wangyifan on 2015/11/9.
 */
public class HelloImpl implements Hello {
	public String say(String name) {
		return "Hello " + name;
	}
}

```

通过maven的package命令打包，即可打包成一个bundle

# 本地部署与debug

打包成bundle后，一般情况下你需要把bundle发布到OSGi容器内去部署，这里就是发布到felix中。而目前我们使用了内嵌式的felix，可直接在本地部署。方便调试。

- 首先，将上面的Felix启动应用打包，发布到本地maven仓库中
- 在bundle项目中添加依赖

```
<!--这是我在上面创建的Felix启动项目的依赖配置，请根据自己的项目做修改-->
<dependencies>
    <dependency>
        <groupId>com.ivan.osgi</groupId>
        <artifactId>osgi</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

- 将felix中的conf目录拷贝到bundle应用的根目录
- 新建一个Application启动
- 设置Main Class为Felix启动应用中的Main类，我这里是org.embedosgi.main.Main
- 在VM options中添加-Dfelix.auto.start.2=file:/E:/code/embedosgi/demo/target/demo-1.0.0.jar

        上面的配置可参考前面的属性说明!这里路径指到bundle应用的打包路径。

- 添加一个运行前的mvn package动作
- 最后运行

这样的话，每次运行时都会打包这个bundle，然后自动将其部署到了Felix容器中了。且支持debug

## 外部类获取OSGi服务

这里测试如何在org.embedosgi.main.Main中调用bundle中发布的Hello服务。

其实很简单，OSGi通过BundleContext来管理bundle,在上面发布服务的时候你也看到了，也是通过BundleContext来发布服务的。

而Framework提供了获取BundleContext的方法getBundleContext()，只要获取到BundleContext就可以获取服务了。相关代码如下:

```java
BundleContext context = m_fwk.getBundleContext();
String filter = "(&(objectClass=org.embedosgi.demo.Hello)(version=1.0.0))";
Filter f = context.createFilter(filter);
ServiceTracker serviceTracker = new ServiceTracker(context, f, null);
serviceTracker.open();
Object o = serviceTracker.getService();
Class clz = o.getClass();
System.out.println(this.getClass().getClassLoader() + " | " + clz.getClassLoader());
Method method = clz.getDeclaredMethod("say", String.class);
System.out.println(method.invoke(o, "Ivan"));
```

- 获取BundleContext
- 构建LDAP Filter语法字符串.LDAP语法请见[此处](http://www.ldapexplorer.com/en/manual/109010000-ldap-filter-syntax.htm)
- 根据Filter语法字符串创建Filter
- 通过BundleContext和Filter构建ServiceTracker，此类可以通过Filter在Context中查找到符合条件的Service
- 打开ServiceTracker，必要操作
- 获取service
- 后面就是通过反射调用了

## 内部bundle调用外部类

我们在Felix中启动项目中新增一个类HostHello

```java
package org.embedosgi.host;

/**
 * Created by wangyifan on 2015/11/9.
 */
public class HostHello {
    public String name(){
        return "HostName";
    }
}
```

只是简单的返回一个字符串

在Bundle项目中，修改HelloImpl类来获取这个类

```java
package org.embedosgi.demo.impl;
import org.embedosgi.demo.Hello;
import org.embedosgi.host.HostHello;

/**
 * Created by wangyifan on 2015/11/9.
 */
public class HelloImpl implements Hello {
	public String say(String name) {
		return "Hello " + name + new HostHello().name();
	}
}
```

如何能使HelloImpl调用到HostHello的name方法呢？

其实有两个方法可以实现!

- 配置org.osgi.framework.system.packages或org.osgi.framework.system.packages.extra
- 配置org.osgi.framework.bootdelegation和org.osgi.framework.bundle.parent

*我们先看第一个方法:*

只需要在conf/config.properties中配置

```
org.osgi.framework.system.packages=org.embedosgi.host
或者
org.osgi.framework.system.packages.extra=org.embedosgi.host
```

然后修改Bundle项目的pom.xml文件

```xml
<plugin>
    <groupId>org.apache.felix</groupId>
    <artifactId>maven-bundle-plugin</artifactId>
    <version>2.5.3</version>
    <extensions>true</extensions>
    <configuration>
        <instructions>
            <Bundle-Name>demo</Bundle-Name>
            <Bundle-SymbolicName>demo</Bundle-SymbolicName>
            <Implementation-Title>demo</Implementation-Title>
            <Implementation-Version>1.0.0</Implementation-Version>
            <Export-Package></Export-Package>
            <Import-Package>*</Import-Package>
            <Bundle-Activator>org.embedosgi.activator.Activator</Bundle-Activator>
        </instructions>
    </configuration>
</plugin>
```

\*号表示自动生成需要的导入包，你也可以将Import-Package标签删除，默认就是自动导入

也就是说，通过系统Bundle导出了org.embedosgi.host这个包，然后在Bundle项目中导入了这个包。这样就可以在Bundle中调用了

*第二种方法*

配置

```
org.osgi.framework.bootdelegation=sun.*,com.sun.*,org.osgi.framework,org.osgi.framework.*,org.embedosgi.host
org.osgi.framework.bundle.parent=app
```

这里的意思是所有以sun,com.sun,org.osgi.framework和org.embedosgi开头的包都通过AppClassLoader加载，加载后对所有bundle可见。

Bundle项目不需要做任何导入导出！

## ClassLoader结构图

第二种方式的ClassLoader结构图如下:

![]({{site.CDN_PATH}}/assets/tech/architecture/osgi.png)

HelloImpl在遇到HostHello类时，发现配置了org.osgi.framework.bootdelegation，那么直接委托给AppClassLoader来加载。

可以稍微修改下代码，打印出ClassLaoder即可得到!

```java
package org.embedosgi.demo.impl;
import org.embedosgi.demo.Hello;
import org.embedosgi.host.HostHello;

/**
 * Created by wangyifan on 2015/11/9.
 */
public class HelloImpl implements Hello {
	public String say(String name) {
		System.out.println("HostHello ClassLoader = " + HostHello.class.getClassLoader());
		return "Hello " + name + new HostHello().name();
	}
}
```

打印结果

```
HostHello ClassLoader = sun.misc.Launcher$AppClassLoader@610f7612
```

## 多版本

OSGi支持多版本发布，即可以在一个OSGi容器内发布多个不同版本的应用。比如这里我们有一个demo-1.0.0.jar的应用。

我们可以对HelloImpl稍做修改，发布一个demo-1.0.1.jar的版本。两个版本可以并存。调用时只需要通过LDAP过滤即可。

# 总结

本文介绍了

- 如何嵌入式的启动一个OSGi容器
- 如何与OSGi bundle进行交互
- 多版本

通过如上内容，我们可以将应用中基础的部分固化，而业务代码动态化，来加快代码的迭代速度。

同时也可实现如服务框架，结合MVVM模式，可实现易扩展的Web应用。想象空间还是很大的。

最后给出[项目代码]({{site.CDN_PATH}}/assets/tech/architecture/embedosgi.zip)

