% 注解
% 王一帆
% 江苏企业大学

## 注解的作用

-   注解为我们在代码中添加信息提供了一种形式化的方法,使我们可以在稍后某个时刻非常方便地使用这些数据.
-   注解可以提供用来完整的描述程序所需要的信息,而这些信息是无法用Java来表达的(回忆一下之前我们使用了什么注解?)
-   注解本身并不做任何事情
-   需要注解处理程序,来对注解来进行处理

## 内置注解

``` {.example}
@Override : 当前方法定义将覆盖超类中的方法
@Deprecated : 如果程序员使用了注解了该注解的方法,则会发出警告
@SuppressWarnings : 关闭不当的编译器警告信息
```

## 课堂练习

-   分别测试以上三个内置注解,体会一下注解的作用

## 注解的注解(元注解)

-   除了上面的三个注解外.Java提供了四个注解,专门负责创建新的注解

``` {.example}
@Target     表示该注解可以用于什么地方
            CONSTRUCTOR,FIELD,LOCAL_VARIABLE,METHOD,PACKAGE,PARAMETER,TYPE
@Retention  表示需要在什么级别保存注解信息 SOURCE,CLASS,RUNTIME
@Documented 将此注解包含在Javadoc中
@Inherited  允许子类继承父类的注解
```

## 编写注解

``` {.java}
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTest {
    public Class clz() default Object.class;
    public String value();
}
```

-   上面定义了一个叫做MyTest的注解,定义方法和接口很类似,只是interface前多了一个@
-   该注解可以注解在方法上
-   在运行时可识别
-   其中包含了两个元素clz和value.其中clz有默认值为Object.class
-   value没有默认值,在使用注解时必须提供.当名字为value时,可以省略value名称
-   没有元素的注解称为标记注解(@Override,@Deprecated,@SuppressWarnings是标记注解吗?)

## 注解元素

-   基本类型
-   String
-   Class
-   enum
-   Annotation
-   及其数组

## 注解的使用

``` {.java}
public class Dog {
    @MyTest("testShout")   //@MyTest(value="testShout")
    public String shout(){
        return "wawawa";
    }
}
```

## 课堂练习

-   尝试修改注解中的元素类型
-   并对Dog中的注解进行相应的修改

## 包注解

``` {.java}
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PKLog {
    public Class clz() default Object.class;
    public String value();
}
```

## 课堂练习

-   尝试在Dog上使用PKLog注解

## package-info.java用途

-   为标注在包上Annotation提供便利；
-   声明友好类和包常量；
-   提供包的整体注释说明。

## package-info.java注意点

-   首先，它不能随便被创建。在Eclipse中，package-info文件不能随便被创建，会报“Typename is notvalid”错误，类名无效，Java变量定义规范是：字母、数字、下划线，\$符号。
-   其次，服务的对象很特殊。一个类是一类或一组事物的描述，package-info是描述和记录本包信息。
-   最后，类不能带有public、private访问权限。package-info.java再怎么特殊，也是一个类文件，也会被编译成package-info.class，但是在package-info.java中只能声明默认访问权限的类，也就是友好类。
-   还有几个特殊的地方，比如不可以继承，没有接口，没有类间关系（关联、组合、聚合等等）等。

## 课堂练习

-   请在eclipse中创建package-info.java,并添加PKLog注解

## 后续

-   目前没有看出注解的任何用处?
-   注解本身并不做事情,需要注解处理工具来处理
-   如何编写注解处理工具?
-   你需要先学习反射!

