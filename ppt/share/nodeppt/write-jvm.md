title: 使用Kotlin实现一个虚拟机
speaker: 王一帆
url: http://ivaneye.com
transition: stick
theme: dark

[slide]
# 使用Kotlin实现一个虚拟机
## 王一帆

[slide]
## 源起

- 《自己动手写Java虚拟机》 {:&.moveIn}
- JVM是如何运行的？
- ![](http://orixnpicm.bkt.clouddn.com/17-6-14/12046408.jpg)

[slide]
## Kotlin简介

[slide]
## 简洁

- 使用一行代码创建一个包含 getters、 setters、 equals()、 hashCode()、 toString() 以及 copy() 的 POJO：

```kotlin
data class Customer(val name: String, val email: String, val company: String)
```

[slide]
- 或者使用 lambda 表达式来过滤列表：

```kotlin
val positiveNumbers = list.filter { it > 0 }
```

[slide]
- 想要单例？创建一个 object 就可以了：

```kotlin
object ThisIsASingleton {
    val companyName: String = "JetBrains"
}
```

[slide]
## 安全

- 彻底告别那些烦人的 NullPointerException，毕竟价值万亿。

```kotlin
var output: String
output = null   // 编译错误
```

[slide]
- Kotlin 可以保护你避免对可空类型的误操作

```kotlin
val name: String? = null    // 可空类型
println(name.length())      // 编译错误
```

[slide]
- 并且如果你检查类型是正确的，编译器会为你做自动类型转换

```kotlin
fun calculateTotal(obj: Any) {
    if (obj is Invoice)
        obj.calculateTotal()
}
```

[slide]
## 互操作性

- 使用 JVM 上的任何现有库，因为有 100％ 的兼容性，包括 SAM 支持。

```kotlin
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

Flowable
    .fromCallable {
        Thread.sleep(1000) //  模仿高开销的计算
        "Done"
    }
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.single())
    .subscribe(::println, Throwable::printStackTrace)
```

[slide]
- 无论是 JVM 还是 JavaScript 目标平台，都可用 Kotlin 写代码然后部署到你想要的地方

```kotlin
import kotlin.browser.window

fun onLoad() {
    window.document.body!!.innerHTML += "<br/>Hello, Kotlin!"
}
```

[slide]
## 工具化

- 一门语言需要工具化，而在 JetBrains，这正是我们做得最好的地方！
- ![](http://orixnpicm.bkt.clouddn.com/17-6-14/35271960.jpg)
- ![](http://orixnpicm.bkt.clouddn.com/17-6-14/58573621.jpg)

[slide]
## 吸引我的地方

- JetBrains
- 谷歌宣布Kotlin作为Android主力语言
- 扩展方法

[slide]
## join

```java
List<String> list = new ArrayList<String>(){{
    this.add("a");    
    this.add("b");    
    this.add("c");    
}};
```
```java
StringUtils.join(list,",");
```

[slide]
## 扩展方法

```kotlin
infix fun Collection<String>.join(s : String):String{
    val sb = StringBuffer()
    this.fold(sb){a,b -> a.append(b).append(s)}
    return sb.substring(0,sb.length - 1)
}
```
```kotlin
list.join(",")
```

[slide]
## 目录

- 类的查找
    - 虚拟机如何查找类
    - 设计虚拟机查找类
- 解析
    - 类结构
        - 整体结构
        - magic
        - 主版本，从版本
        - 常量池
        - 类访问标识符，this_class,super_class
        - 接口
        - 字段
        - 方法
        - 属性
    - 解析思路
        - 整体解析思路
        - 特殊结构解析
- 代码演示

[slide]
## 简单示例

```java
public class A {
    public void print() {
        System.out.println("Using Class A") ;
    }
}
```
```java
public class B {
    public void print() {
        System.out.println("Using Class B") ;
    }
}
```
```java
public class Main {
    public static void main(String args[]) {
        A a1 = new A() ;
        a1.print() ;
        B b1 = new B() ;
        b1.print() ;
    }
}
```

[slide]
## 加载过程

```
java –verbose:class Main
```
```
...
[Loaded Main from file:/E:/code/temp/]
...
[Loaded java.lang.Void from C:\Program Files\Java\jdk1.8.0_45\jre\lib\rt.jar]
[Loaded A from file:/E:/code/temp/]
Using Class A
[Loaded B from file:/E:/code/temp/]
Using Class B
...
```

[slide]
## 加载方式

- 双亲委托模型

[slide]
## 加载路径

- jar
- 文件
- URL
- ...

[slide]

```kotlin
class ClassPath {
    lateinit var bootClassPathFinder: Finder
    lateinit var extClassPathFinder: Finder
    lateinit var userClassPathFinder: Finder

    constructor(cpStr: String) {
        parseBootAndExtClasspath()
        parseUserClasspath(cpStr)
    }

    private fun parseBootAndExtClasspath() {
        val javaHome = System.getenv().filter {
                "JAVA_HOME".equals(it.key)
            }["JAVA_HOME"] ?: throw RuntimeException("Can not find JAVA_HOME")
        val jreLibPath = "$javaHome/jre/lib"
        val jreExtPath = "$javaHome/jre/lib/ext"
        bootClassPathFinder = DirFinder(jreLibPath)
        extClassPathFinder = DirFinder(jreExtPath)
    }

    private fun parseUserClasspath(cpStr: String) {
        userClassPathFinder = DirFinder(cpStr)
    }

    fun readClass(className: String): ByteArray? {
        try {
            var result = bootClassPathFinder.readClass(className)
            if (result == null) {
                result = extClassPathFinder.readClass(className)
            }
            if (result == null) {
                result = userClassPathFinder.readClass(className)
            }
            return result
        } catch(e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
```

[slide]
## Finder

```kotlin
interface Finder {
    /**
     * 从指定路径读取类内容，返回字节数组
     */
    fun readClass(className: String): ByteArray?
}
```

[slide]
## JarFinder

```kotlin
class JarFinder(val jarPath: String):Finder {
    override fun readClass(className: String): ByteArray? {
        val zipFile = ZipFile(jarPath)
        for(e in zipFile.entries()){
            if(e.name.contains(className)){
                return zipFile.getInputStream(e).use { it.readBytes() }
            }
        }
        return null
    }
}
```

[slide]
## FileFinder

```kotlin
class FileFinder(val libPath: String) : Finder {
    override fun readClass(className: String): ByteArray? {
        if (libPath.replace(Regex.fromLiteral("\\"),"/").contains(className)) {
            return File(libPath).readBytes()
        }
        return null
    }
}
```

[slide]
## DirFinder

```kotlin
class DirFinder : Finder {

    val finders = ArrayList<Finder>()

    constructor(cpStr: String) {
        val dir = File(cpStr)
        val files = dir.walk().filter { file -> file.name.endsWith(".jar") || file.name.endsWith(".class") }
        files.forEach { f ->
            if (f.name.endsWith(".jar")) {
                finders.add(JarFinder(f.absolutePath))
            } else if (f.name.endsWith(".class")) {
                finders.add(FileFinder(f.absolutePath))
            }
        }
    }

    override fun readClass(className: String): ByteArray? {
        for(f in finders){
            var result = f.readClass(className)
            if (result != null) return result;
        }
        return null
    }
}
```

[slide]
## 如何从URL读取Class?

- Class文件需要是一个什么样的结构，才方便网络传输？

[slide]
## Nio传输消息的方式

- 定长
- 特定结束符
- 自定义结构

[slide]
## Class文件结构

|类型|名称|数量|
|---|---|---|
|u4|magic|1|
|u2|minor_version|1|
|u2|major_version|1|
|u2|constant_pool_count|1|
|cp_info|constant_pool|constant_pool_count - 1|
|u2|access_flags|1|
|u2|this_class|1|
|u2|super_class|1|

[slide]
## Class文件结构(续)

|类型|名称|数量|
|---|---|---|
|u2|interfaces_count|1|
|u2|interfaces|interfaces_count|
|u2|fields_count|1|
|field_info|fields|fields_count|
|u2|methods_count|1|
|method_info|methods|methods_count|
|u2|attributes_count|1|
|attribute_info|attributes|attributes_count|

[slide]
## 说明

- u2,u4：无符号2/4字节
- constant_pool:
- constant_pool长度：

[slide]
## 结构优缺点

- 结构紧凑

[slide]
## 类似结构

- HTTP请求结构
- ![](http://orixnpicm.bkt.clouddn.com/17-6-14/11321780.jpg)

[slide]
## magic

- 文件类型判断
- windows后缀判断文件类型的弊端

[slide]
## 项目地址

[https://github.com/ivaneye/kt-jvm](https://github.com/ivaneye/kt-jvm)

[slide]
# 谢谢
