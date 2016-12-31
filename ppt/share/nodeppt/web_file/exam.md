## 写出下面的代码打印结果 - 8分

``` {.java}

public class Test{
    public static void main(String[] args){
        System.out.println(1 + 2 + 3);
        System.out.println("1" + 2 + 3);
        System.out.println(1 + "2" + 3);
        System.out.println(1 + 2 + "3");
    }
}
```

## 根据如下代码回答问题 - 8分

``` {.java}

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        test.pr();
    }

    int a = 1;
    public void pr(){
        {
            int a = 0;
            System.out.println(a);
        }
        System.out.println(a);
    }
}
```

-   请回答int a = 1;中的a为\_\_\_变量? - 2分
-   请回答int a = 0;中的a为\_\_\_变量? - 2分
-   如上代码,运行结果为\_\_? - 4分

## 如下代码运行结果为 - 8 分

``` {.java}

int a = 1;
int b = 2;
if(a > 1 && b++ > 1){
    System.out.println(b);
}

System.out.println(b);

if(a > 1 & b++ > 1){
    System.out.println(b);
}

System.out.println(b);
```

## 请回答如下问题 - 12 分

-   Java中继承使用\_\_ 关键字?
-   Java中实现接口使用\_\_ 关键字?
-   Java中的类默认继承\_\_ 类?
-   Java中类可以继承多个类吗?
-   Java中类可以实现多个接口吗?
-   Java中的抽象类和抽象方法需要使用\_\_ 关键字修饰?
- 子类覆写父类的方法，不能\_\_\_父类方法的访问控制权限.不能抛出比父类定义的异常\_\_\_的异常!为什么？
- 如何自定义一个受检查异常?如何自定义一个运行时异常?
- 受检查异常和运行时异常有什么区别？

## 请根据如下代码回答问题 - 18 分

``` {.java}

class A{
    int a = 1;
    public void say(){
        System.out.println("A");
    }
}
class B extends A{
    int a = 2;

    @Override
    public void say(){
        System.out.println("B");
    }
}
class C extends A{
    int a = 3;
    public void say(){
        System.out.println("C");
    }
}

//运行代码
B b = new B();      //1
b.say();
System.out.println(b.a);

A a = new B();      //2
a.say();
System.out.println(a.a);

C c = new B();      //3
c.say();
System.out.println(c.a);
```

-   如上代码中,B和C称为是A的\_?A称为是B,C的\_?它们之间的这种关系叫\_\_\_\_ 关系 - 6 分
-   B中的say方法对A中的say方法进行了\_\_  - 2 分
-   @Override称为\_\_?它的作用是?  - 4分
-   在运行代码中,1,2,3运行结果分别是什么?  - 6 分

## 按要求编写代码 - 10 分

-   给出如下序列["red","black","blue","pink","yellow"]
-   请编写方法,返回序列,序列中包含所有长度为偶数的元素!
-   请将该序列写入到list.txt文件中
-   重新从list.txt文件中读取此序列
-   请使用正则表达式,将序列中所有的l和i替换为S,返回替换后的序列!

## 按要求编写代码 - 4 分

打印字符串使用System.out.println()是否太麻烦了?

-   请自定义一个类Prt,并提供p()方法,来简化System.out.println()的功能
-   请在另一个包内调用该类！调用代码如下,请根据调用代码,来完成自定义类 （注意p方法的调用方式）
-   你能想出几种实现方式?

``` {.java}

public class Test{
    public static void main(String[] args){
        p("Hello");      //"Hello"
        p(1L);           //1
        List<String> list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        p(list);         //[a,b,c]
    }
}
```

## 请画出web项目目录结构 4分

## 请指出如下代码的区别 4分

```
<%  int i = 0; %>
<%! int i = 0; %>
```

## 请列出请求转发与重定向的区别 4分

## 请回答如下问题： 10分

定义Servlet步骤：

- 定义一个继承了\_\_\_\_\_\_的类
- 在\_\_\_\_文件里配置servlet
- 完成如下配置,使得当访问 /a/b 链接时能调用HelloServlet

```
<servlet>
  <servlet-name>？</servlet-name>
  <servlet-class>com.test.HelloServlet</servlet-class>
</servlet>

 <servlet-mapping>
    <servlet-name>?</servlet-name>
    <url-pattern>?</url-pattern>
 </servlet-mapping>
```

## 请回答如下问题 10分

- servlet何时被加载？
- 如何使servlet在服务器启动时加载?
- servlet生命周期是?
- 当使用get方式请求时会调用servlet的什么方法？post请求呢?
