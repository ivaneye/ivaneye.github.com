% 字符串
% 王一帆
% 江苏企业大学

## String

-   String对象是不可变的
-   Java中使用String类来处理字符串
-   Java中String类在java.lang包中
-   Java会自动导入java.lang包
-   java.lang包中包含了很多常用的类

## 创建字符串

-   字面量创建(JVM会做优化)
-   使用String类来创建
-   通过字符数组创建

``` {.java}

String greeting = "Hello world!";
String greeting = new String("Hello world!");
```

``` {.java}

public class StringDemo{
   public static void main(String args[]){
      char[] helloArray = { 'h', 'e', 'l', 'l', 'o', '.'};
      String helloString = new String(helloArray);
      System.out.println( helloString );
   }
}
```

## 字符串长度

-   字符串提供了length()方法来计算长度
-   数组中提供的是length字段而非方法

``` {.java}

public class StringDemo {
   public static void main(String args[]) {
      String palindrome = "Dot saw I was Tod";
      int len = palindrome.length();
      System.out.println( "String Length is : " + len );
   }
}
```

## 连接字符串


``` {.java}

string1.concat(string2);

"My name is ".concat("Zara");

"Hello," + " world" + "!"
```

-   Java中对+和+=进行了操作符重载,当遇到字符串时,会作为字符串连接符
-   除了上面所说的两个操作符重载外,Java不提供其它的操作符重载
-   Java也不允许自定义操作符重载

## 创建格式化字符串

-   Java提供了类似c的printf的方法printf和format

``` {.java}

System.out.printf("The value of the float variable is " +
                  "%f, while the value of the integer " +
                  "variable is %d, and the string " +
                  "is %s", floatVar, intVar, stringVar);
```

``` {.java}

String fs;
fs = String.format("The value of the float variable is " +
                   "%f, while the value of the integer " +
                   "variable is %d, and the string " +
                   "is %s", floatVar, intVar, stringVar);
System.out.println(fs);
```

## 字符串比较(课堂练习)

-   请回答如下的代码的打印结果
-   自己运行代码验证一下

``` {.java}

public class Test{
    public static void main(String[] args){
        String s1 = "hello";
        String s2 = "hello";
        String s3 = "he" + "llo";
        String s4 = new String("hello");
        String s5 = new String(s1);
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s4);
        System.out.println(s1 == s5);
        System.out.println(s5 == s4);
        System.out.println(s1.equals(s4));
        System.out.println(s3.equals(s4));
    }
}
```

## StringBuilder和StringBuffer


-   可变字符串
-   当需要做字符串连接的时候,考虑StringBuilder和StringBuffer
-   StringBuilder不是线程同步的
-   StringBuffer是线程同步的
-   确定不需要同步,且速度要求较高的情况下,使用StringBuilder

``` {.java}

public class Test{
    public static void main(String args[]){
       StringBuffer sBuffer = new StringBuffer(" test");
       sBuffer.append(" String Buffer");
       System.out.println(sBuffer);
   }
}
```

## 编译器优化与StringBuilder(了解)


``` {.java}

String a = "a";
String b = "b";
String c = a + b;
```

``` {.example}

javap -c className
```

``` {.java}

String a = "a";
String b = "";
for(int i = 0;i < 3; i++){
    b += a;
}
```

## toString方法

-   toString方法是Object中的方法,所以每个类都包含了toString方法
-   toString方法是用来打印类信息的
-   当你编写类似System.out.println(new Circle())的代码时,默认会调用
    Circle的toString方法
-   但是,默认的toString方法答应信息用处不大.默认toString只会打印类名
    @hashcode

## 课堂练习

-   请给Circle,Rect类添加toString方法,打印 类名[属性:属性值] 这样的信息
-   同时请打印出Circle和Rect的默认信息(类名@hashcode),请不要自己拼接

``` {.java}

public Circle{

    @Override
    public String toString(){
        return "Circle[radius:" + this.getRadius() + ",pi:" + pi + "]" + super.toString();
    }
}
```

## 常用方法

-   length()
-   charAt()
-   getBytes()
-   equals(),equalsIgnoreCase()
-   compareTo()
-   contains()
-   startsWith(),endsWith()
-   indexOf(),lastIndexOf()
-   substring()
-   concat()
-   replace(),replaceAll()
-   toLowerCase(),toUpperCase()
-   trim()
-   split()

## 课堂练习

自行查询API,按下面的要求编写程序

-   给定字符串"agedecbfceddecfbga"
-   请计算字符串长度
-   获得第一个c所在的位置
-   判断其是否以a开头,以b结尾
-   获得第7个位置上的字符
-   判断其是否包含"ede"和"edde"字符串,如果包含,请将所有的"ede"和"edde"替换为"HH"
-   请将字符串按照字符'g'和'd'来切分开
-   获取从第2个位置,到第8个位置的子字符串
-   将上面获得的子串与"dsbgdnem"进行比较,谁比较大?

## 正则表达式

-   大家在电脑上搜索文件的时候是否使用过通配符(\*,?)?
-   正则表达式是用来进行文本匹配的工具，只不过比起通配符，它能更精确地描述你的需求

## 常用的元字符

``` {.example}

代码  说明
.   匹配除换行符以外的任意字符
\w  匹配字母或数字或下划线或汉字
\s  匹配任意的空白符
\d  匹配数字
\b  匹配单词的开始或结束
^   匹配字符串的开始
$   匹配字符串的结束
```

## 常用的限定符

``` {.example}

代码/语法   说明
*   重复零次或更多次
+   重复一次或更多次
?   重复零次或一次
{n} 重复n次
{n,}    重复n次或更多次
{n,m}   重复n到m次
```

## 常用的反义代码

``` {.example}

代码/语法   说明
\W  匹配任意不是字母，数字，下划线，汉字的字符
\S  匹配任意不是空白符的字符
\D  匹配任意非数字的字符
\B  匹配不是单词开头或结束的位置
[^x]    匹配除了x以外的任意字符
[^aeiou]    匹配除了aeiou这几个字母以外的任意字符
```

## String中的正则表达式

## replaceAll

-   给定字符串"agedecbfceddecfbga"
-   判断其是否包含"ede"和"edde"字符串,如果包含,请将所有的"ede"和"edde"替换为"HH"

``` {.java}

String s = "agedecbfceddecfbga";
s.replaceAll("ed+e","HH");
```

## split(课堂练习)

-   请将字符串按照字符'g'和'd'来切分开

``` {.java}

String s = "agedecbfceddecfbga";
s.split("[gd]");
```

## 相关类

java.util.regex包主要包括以下三个类：

-   Pattern类

    pattern对象是一个正则表达式的编译表示。Pattern类没有公共构造方法。要创建一个Pattern对象，你必须首先调用其公共静态编译方法，它返回一个Pattern对象。该方法接受一个正则表达式作为它的第一个参数。

-   Matcher类

    Matcher对象是对输入字符串进行解释和匹配操作的引擎。与Pattern类一样，Matcher也没有公共构造方法。你需要调用Pattern对象的matcher方法来获得一个Matcher对象。

-   PatternSyntaxException

    PatternSyntaxException是一个非强制异常类，它表示一个正则表达式模式中的语法错误。

## 样例代码

``` {.java}

public class Reg {
    public static void main(String[] args) {
        String str = "agedecbfceddecfbga";
        Pattern pattern = Pattern.compile("ed+e");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
```

## 课堂练习

-   使用如上的代码,验证正则表达式字元字符,限定符和反义代码

## 课后作业

-   熟悉String的各种操作
-   自学java.util包
-   格式化当前时间，输出为 yyyy-MM-dd
-   请参考java.util.regex.Pattern文档，编写一个正则表达式，检查一个句子是否以大写字母开头，以句号结尾
-   编写一个程序，读取一个Java源代码文件，分别打印出所有注释和普通字符串
-   编写一个包含int,long,float,double和String属性的类。为它编写一个构造器，接收一个String参数。然后扫描该字符串，为各个属性赋值。再添加一个toString()方法，用来演示你的类是否工作正常

