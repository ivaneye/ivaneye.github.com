---
typora-root-url: ./
typora-copy-images-to: files/write-jvm
---

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
- ![](/files/write-jvm/talk.png)

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
- ![](/files/write-jvm/tooling1.png)
- ![](/files/write-jvm/tooling2.png)

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

## Class结构

- 整体：自定义结构
- 局部：定长头+自定义体

[slide]

## 结构优缺点

- 优点
  - 结构紧凑
  - 易于扩展
- 缺点
  - 不易于阅读

[slide]

## 类似结构

- HTTP请求结构
- ![](/files/write-jvm/http.jpg)

[slide]
## Class文件结构

| 类型      | 名称                  | 数量                      |
| ------- | ------------------- | ----------------------- |
| u4      | magic               | 1                       |
| u2      | minor_version       | 1                       |
| u2      | major_version       | 1                       |
| u2      | constant_pool_count | 1                       |
| cp_info | constant_pool       | constant_pool_count - 1 |
| u2      | access_flags        | 1                       |
| u2      | this_class          | 1                       |
| u2      | super_class         | 1                       |

[slide]
## Class文件结构(续) 

| 类型             | 名称               | 数量               |
| -------------- | ---------------- | ---------------- |
| u2             | interfaces_count | 1                |
| u2             | interfaces       | interfaces_count |
| u2             | fields_count     | 1                |
| field_info     | fields           | fields_count     |
| u2             | methods_count    | 1                |
| method_info    | methods          | methods_count    |
| u2             | attributes_count | 1                |
| attribute_info | attributes       | attributes_count |

[slide]

## u1/u2/u4

- 无符号1/2/4字节
- 为什么使用无符号类型？Java里明明就不默认支持无符号类型！
- Kotlin里怎么表示无符号类型？
- 延伸：为什么用补码这么诡异的方式来表示负数？

[slide]

## 为什么使用无符号类型？

- 没有负数的需求

[slide]

## Kotlin里怎么表示无符号类型？

```kotlin
//转化为无符号整型
fun Byte.toPositiveInt() = toInt() and 0xFF
//转化为无符号长整型
fun Byte.toPositiveLong() = toLong() and 0xFF
```

```kotlin
class U1(val data: Byte) {
    fun toInt(): Int {
        return data.toPositiveInt()
    }
}
class U2(val data: ByteArray) {...}
class U4(val data: ByteArray) {...}
```

[slide]

## 为什么用补码这么诡异的方式来表示负数？

- 当第一个字符为0时表示正数，当第一个字符为1时表示负数，不是很容易理解吗？
- 为什么用补码表示负数？

```
//首位为符号位
10000001         // -1
00000001         // 1
//补码形式,取反加一
11111111         // -1
```

[slide]

## 权衡的结果

- 假设10000001表示-1，那么10000010表示什么？-2吧？
- -1 + 1 结果是多少？0吧？
- 0 = -1 + 1 = 10000001 + 00000001 = 10000010 = -2
- 0 = -2 ?!
- 假设11111111表示-1
- 那么 -1 + 1 = 

```
   11111111 
+  00000001 
--------------
  100000000    =    0
  ^
  溢出
```

**方便计算机的计算！**

[slide]

## 换一种理解方式

- -1 + 1 = (0 - 1) + 1 = (1,0000,0000 - 0000,0001) + 0000,0001 = 1111,1111 + 0000,0001 = 1,0000,0000 = 0

[slide]

## 代码实现

```kotlin
class ClassInfo {
    lateinit var magic: U4
    lateinit var minorVersion: U2
    lateinit var majorVersion: U2
    lateinit var constantPoolCount: U2
    lateinit var cpInfos: Map<Int, Constant>   //constant包中的对象，这里为什么使用Map？
    lateinit var accessFlags: U2
    lateinit var thisClass: U2
    lateinit var superClass: U2
    lateinit var interfacesCount: U2
    var interfaceInfos: Array<U2> = emptyArray()   // 常量池中的索引
    lateinit var fieldsCount: U2
    var fieldInfos: Array<FieldInfo> = emptyArray()
    lateinit var methodsCount: U2
    var methodInfos: Array<MethodInfo> = emptyArray()
    lateinit var attributesCount: U2
    var attributeInfos: Array<Attribute> = emptyArray()
  ...
}
```

[slide]

## CommonReader

```kotlin
class CommonReader(var data: ByteArray) {

    fun readU1(): U1 {
        val result = U1(data[0])
        data = data.drop(1).toByteArray()
        return result
    }

    fun readU2(): U2 {
        return U2(readByteArray(2))
    }

    fun readU4(): U4 {
        return U4(readByteArray(4))
    }

    fun readByteArray(size: Int): ByteArray {
        val result = data.sliceArray(IntRange(0, size - 1))
        drop(size)
        return result
    }

    fun drop(size: Int) {
        data = data.drop(size).toByteArray()
    }
}
```



[slide]

## magic

- CAFEBABE
- 快速检查Class文件

[slide]

## 解析代码

```kotlin
private fun readMagic() {
  classInfo.magic = commonReader.readU4()
}
```



[slide]

## 类似功能

- 文件类型判断

1. JPEG/JPG - 文件头标识 (2 bytes): \$ff, \$d8 (SOI) (JPEG 文件标识) - 文件结束标识 (2 bytes): \$ff, $d9 (EOI) 
2. TGA - 未压缩的前5字节   00 00 02 00 00 - RLE压缩的前5字节   00 00 10 00 00
3. PNG - 文件头标识 (8 bytes)   89 50 4E 47 0D 0A 1A 0A
4. GIF - 文件头标识 (6 bytes)   47 49 46 38 39(37) 61                         G  I  F  8  9 (7)  a
5. BMP - 文件头标识 (2 bytes)   42 4D                         B  M
6. PCX - 文件头标识 (1 bytes)   0A
7. TIFF - 文件头标识 (2 bytes)  4D 4D 或 49 49
8. ICO - 文件头标识 (8 bytes)   00 00 01 00 01 00 20 20 
9. CUR - 文件头标识 (8 bytes)   00 00 02 00 01 00 20 20
10. IFF - 文件头标识 (4 bytes)   46 4F 52 4D                        F  O  R  M
11. ANI - 文件头标识 (4 bytes)   52 49 46 46                         R  I  F  F

[slide]

## 优势/劣势

- 优势   {:&.moveIn}
  - 不宜修改
  - 准确
- 劣势
  - ![1498543957108](/files/write-jvm/1498543957108.png)



[slide]

## minor_version/major_version

- 检查兼容性
- java.lang.UnsupportedClassVersionError

[slide]

## major版本对应关系：

| version     | major | hex  |
| ----------- | ----- | ---- |
| Java SE 9   | 53    | 0x35 |
| Java SE 8   | 52    | 0x34 |
| Java SE 7   | 51    | 0x33 |
| Java SE 6.0 | 50    | 0x32 |
| Java SE 5.0 | 49    | 0x31 |
| JDK 1.4     | 48    | 0x30 |
| JDK 1.3     | 47    | 0x2F |
| JDK 1.2     | 46    | 0x2E |
| JDK 1.1     | 45    | 0x2D |

 

[slide]

## 解析代码

```kotlin
private fun readMinorVersion() {
  classInfo.minorVersion = commonReader.readU2()
}

private fun readMajorVersion() {
  classInfo.majorVersion = commonReader.readU2()
}
```



[slide]

## 常量池

- constant_pool_count:常量池长度
- constant_pool：下标从1开始，长度为常量池长度 - 1，或者更短
- 如果class文件中的其他地方引用了索引为0的常量池项， 就说明它不引用任何常量池项
- **常量的通用结构:tag(u1) + info[]。tag指定类型，info[]中为该类型的内容**

[slide]

## 常量类型

| **常量池中数据项类型**               | **Tag** | **类型描述**                                 |
| --------------------------- | ------- | ---------------------------------------- |
| CONSTANT_Utf8               | 1       | UTF-8编码的Unicode字符串                       |
| CONSTANT_Integer            | 3       | int类型字面值                                 |
| CONSTANT_Float              | 4       | float类型字面值                               |
| CONSTANT_Long               | 5       | long类型字面值                                |
| CONSTANT_Double             | 6       | double类型字面值                              |
| CONSTANT_Class              | 7       | 对一个类或接口的符号引用                             |
| CONSTANT_String             | 8       | String类型字面值                              |
| CONSTANT_Fieldref           | 9       | 对一个字段的符号引用                               |
| CONSTANT_Methodref          | 10      | 对一个类中声明的方法的符号引用                          |
| CONSTANT_InterfaceMethodref | 11      | 对一个接口中声明的方法的符号引用                         |
| CONSTANT_NameAndType        | 12      | 对一个字段或方法的部分符号引用                          |
| CONSTANT_MethodHandle       | 15      | 表示方法句柄                                   |
| CONSTANT_MethodType         | 16      | 表示方法类型                                   |
| CONSTANT_InvokeDynamic      | 18      | 表示 invokedynamic 指令所使用到的引导方法,引导方法使用到动态调用名称、参数和请求返回类型、以及可以选择性的附加被称为静态参数(Static Arguments)的常量序列 |

[slide]

## 代码结构

![1498550491304](/files/write-jvm/1498550491304.png)

[slide]

## CONSTANT_Long/CONSTANT_Double

- tag(u1) + high_bytes(u4) + low_bytes(u4)
- 在class文件的常量池表中，所有的8字节常量均占两个表成员的空间(jvm-spec 4.4.5)
- 这是一个历史原因，JVM开始开发时还是32位机为主流

[slide]

## 代码

```kotlin
class ConstantLong(val tag: U1, val highBytes: U4, val lowBytes: U4, val classInfo: ClassInfo) : Constant {
    override fun type(): String {
        return "Long"
    }

    override fun value(): String {
        return "${U8(highBytes.toByteArray(), lowBytes.toByteArray()).toLong()}L"
    }

    override fun toString(): String {
        return "${type()}    ${value()}"
    }
}
```

[slide]

## 代码

```kotlin
class ConstantNameAndType(val tag: U1, val nameIndex: U2, val descIndex: U2, val classInfo: ClassInfo) : Constant {
    override fun type(): String {
        return "NameAndType"
    }

    override fun value(): String {
        return "\"${classInfo.cpInfos[nameIndex.toInt()]!!.value()}\":${classInfo.cpInfos[descIndex.toInt()]!!.value()}"
    }

    override fun toString(): String {
        return "${type()}    #${nameIndex.toInt()}:#${descIndex.toInt()}    // ${value()}"
    }
}
```

[slide]

## 解析代码

```kotlin
private fun readConstantPool() {
        //常量池下标从1开始
        val cpInfoMap = HashMap<Int, Constant>()
        var i = 1
        while (i < classInfo.constantPoolCount()) {
            val tag = commonReader.readU1()
            when (tag.toInt()) {
                1 -> {
                    val length = commonReader.readU2()
                    cpInfoMap.put(i, ConstantUtf8(tag, length, commonReader.readByteArray(length.toInt()), classInfo))
                }
                3 -> {
                    cpInfoMap.put(i, ConstantInteger(tag, commonReader.readU4(), classInfo))
                }
                4 -> {
                    cpInfoMap.put(i, ConstantFloat(tag, commonReader.readU4(), classInfo))
                }
                5 -> {
                    cpInfoMap.put(i, ConstantLong(tag, commonReader.readU4(), commonReader.readU4(), classInfo))
                    i += 1
                }
                6 -> {
                    cpInfoMap.put(i, ConstantDouble(tag, commonReader.readU4(), commonReader.readU4(), classInfo))
                    i += 1
                }
              ...
            }
        }
}
```

[slide]

## access_flag

- 用于识别一些类或接口层次的访问信息，包括：这个Class是类还是接口，是否定义为public类型，abstract类型，如果是类的话，是否声明为final，等等

| 标志名称           | 标志值    | 含义                                       |
| -------------- | ------ | ---------------------------------------- |
| ACC_PUBLIC     | 0x0001 | 是否为public类型                              |
| ACC_FINAL      | 0x0010 | 是否被声明为final，只有类可以设置，接口不能设置该标志            |
| ACC_SUPER      | 0x0020 | 是否允许使用invokespecial字节码指令（查了一下该命令的作用为"调用超类的构造方法，实例的构造方法，私有方法"），JDK1.2以后的编译器编译出来的class文件该标志都为真 |
| ACC_INTERFACE  | 0x0200 | 标识这是一个接口                                 |
| ACC_ABSTRACT   | 0x0400 | 是否被声明为abstract类型，对于接口和抽象类来说此标志为真，其他类为假   |
| ACC_SYNTHETIC  | 0x1000 | 标识这个类并非由用户代码生成                           |
| ACC_ANNOTATION | 0x2000 | 标识这是一个注解                                 |
| ACC_ENUM       | 0x4000 | 标识这是一个枚举                                 |

[slide]

## 解析代码

```kotlin
private fun readAccessFlag() {
  classInfo.accessFlags = commonReader.readU2()
}
```

[slide]

##  this_class、super_class、interfaces

- this_class和super_class都是一个u2类型的数据
- interfaces是一组u2类型的数据集合
- Class文件中由这三项数据来确定这个类的继承关系。
- 各自指向一个类型为COMNSTANT_Class_info的类描述符常量，通过该常量中的索引值找到定义在COMNSTANT_Utf8_info类型的常量中的全限定名字符串

[slide]

## 解析代码

```kotlin
private fun readThisClass() {
  classInfo.thisClass = commonReader.readU2()
}

private fun readSuperClass() {
  classInfo.superClass = commonReader.readU2()
}

private fun readInterfacesCount() {
  classInfo.interfacesCount = commonReader.readU2()
}

private fun readInterfaces() {
  val count = classInfo.interfacesCount.toInt()
  if (count > 0) {
    val arr = ArrayList<U2>()
    for (i in IntRange(0, count - 1)) {
      arr.add(commonReader.readU2())
    }
    classInfo.interfaceInfos = arr.toTypedArray()
  }
}
```

[slide]

## fields/methods

- fields描述接口或类中声明的变量
- methods描述解扩或类里生命的变量
- 两者结构相同

  u2 access_flags
  u2 name_index
  u2 descriptor_index
  u2 attributes_count
  attribute_info attributes[attributes_count]
  [slide]

## 解析代码

```kotlin
private fun readFieldCount() {
  classInfo.fieldsCount = commonReader.readU2()
}

private fun readFields() {
  val count = classInfo.fieldsCount.toInt()
  if (count > 0) {
    val arr = ArrayList<FieldInfo>()
    for (i in IntRange(0, count - 1)) {
      val accFlag = commonReader.readU2()
      val nameIdx = commonReader.readU2()
      val descIdx = commonReader.readU2()
      val attrCount = commonReader.readU2()
      val attrs = readAttributes(attrCount.toInt())
      val fieldInfo = FieldInfo(accFlag, nameIdx, descIdx, attrCount, attrs, classInfo)
      arr.add(fieldInfo)
    }
    classInfo.fieldInfos = arr.toTypedArray()
  }
}
```

[slide]

## 解析代码

```kotlin
private fun readMethodCount() {
  classInfo.methodsCount = commonReader.readU2()
}

private fun readMethods() {
  val count = classInfo.methodsCount.toInt()
  if (count > 0) {
    val arr = ArrayList<MethodInfo>()
    for (i in IntRange(0, count - 1)) {
      val accFlag = commonReader.readU2()
      val nameIdx = commonReader.readU2()
      val descIdx = commonReader.readU2()
      val attrCount = commonReader.readU2()
      val attrs = readAttributes(attrCount.toInt())
      val methodInfo = MethodInfo(accFlag, nameIdx, descIdx, attrCount, attrs, classInfo)
      arr.add(methodInfo)
    }
    classInfo.methodInfos = arr.toTypedArray()
  }
}
```

[slide]

## attributes

- 属性计数器，attributes_count的值表示当前 Class 文件attributes表的成员个数
- attributes表中每一项都是一个attribute_info 结构的数据项
- 任一 Java 虚拟机实现可以自动忽略 Class 文件的 attributes表中的若干 （甚至全部） 它不可识别的属性项
- 任何规范未定义的属性不能影响Class文件的语义，只能提供附加的描述信息 

[slide]

## Jvm8属性

![1498621119526](/files/write-jvm/1498621119526.png)

[slide]

## 属性通用结构

![1498621208613](/files/write-jvm/1498621208613.png)

[slide]

## 解析代码

```kotlin
private fun readAttributes() {
  val count = classInfo.attributesCount.toInt()
  classInfo.attributeInfos = readAttributes(count)
}

private fun readAttributes(count: Int): Array<Attribute> {
  if (count > 0) {
    val arr = ArrayList<Attribute>()
    for (i in IntRange(0, count - 1)) {
      val attributeNameIndex = commonReader.readU2()
      val name = classInfo.cpInfos[attributeNameIndex.toInt()]!!.value()
      when (name) {
        "ConstantValue" -> {
          val attr = ConstantValue()
          attr.attributeNameIndex = attributeNameIndex
          attr.attributeLength = commonReader.readU4()
          attr.constantValueIndex = commonReader.readU2()
          attr.classInfo = classInfo
          arr.add(attr)
        }
        ...
      }
    }
  }
}
```

[slide]

##  一些信息

- constant_pool长度为u2
- interfaces_count长度为u2
- fields_count长度为u2
- methods_count长度为u2
- attributes_count长度为u2

u2 = 2^16 - 1 = 65535

** 如果超出了65535会怎么样？**

[slide]

## 一个测试

```java
public class Temp{
	private int _0=0;
	private int _1=1;
	private int _2=2;    
    ......
    private int _65532=65532;
	private int _65533=65533;
	private int _65534=65534;
}
```

[slide]

## 结果

![1498459723147](/files/write-jvm/1498459723147.png)

[slide]

## 后续

- 一个个孤立的类如何连接起来？

[slide]

## Code属性

```kotlin
"Code" -> {
  val attr = Code()
  attr.attributeNameIndex = attributeNameIndex
  attr.attributeLength = commonReader.readU4()
  attr.maxStack = commonReader.readU2()
  attr.maxLocals = commonReader.readU2()
  attr.codeLength = commonReader.readU4()
  //解析code
  val codeArr = ArrayList<U1>()
  for (i in IntRange(0, attr.codeLength.toInt() - 1)) {
    codeArr.add(commonReader.readU1())
  }
  attr.code = codeArr.toTypedArray()
  attr.exceptionTableLength = commonReader.readU2()
  //解析exceptionTable
  val exceptionArr = ArrayList<ExceptionTable>()
  for (i in IntRange(0, attr.exceptionTableLength.toInt() - 1)) {
    val exceptionTable = ExceptionTable()
    exceptionTable.startPc = commonReader.readU2()
    exceptionTable.endPc = commonReader.readU2()
    exceptionTable.handlerPc = commonReader.readU2()
    exceptionTable.cacheType = commonReader.readU2()
    exceptionArr.add(exceptionTable)
  }
  attr.exceptionTable = exceptionArr.toTypedArray()
  attr.attributesCount = commonReader.readU2()
  attr.attributes = readAttributes(attr.attributesCount.toInt())
  arr.add(attr)
}
```



[slide]

## 项目地址

[https://github.com/ivaneye/kt-jvm](https://github.com/ivaneye/kt-jvm)



[slide]

# 谢谢
