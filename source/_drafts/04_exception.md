% 异常处理
% 王一帆
% 江苏企业大学

##

-   你能保证你永远不犯错吗?
-   异常是指阻止当前方法或作用域继续执行的问题

## 课堂练习

-   编写代码从命令行获取两个数
-   并计算两数的除法

## 答案

``` {.java}

public class Test{
    public static void main(String[] args){
        String arg1 = args[0];
        String arg2 = args[1];
        int a = Integer.parseInt(arg1);
        int b = Integer.parseInt(arg2);
        System.out.println(a/b);
    }
}
```

## 导致异常的原因

-   用户输入了非法数据(比如说除0)
-   要打开的文件不存在
-   网络通信时连接中断,或者JVM内存溢出

## 异常的种类

-   CheckedException检查性异常：最具代表的检查性异常是用户错误或问题引起的异常，这是程序员无法预见的。例如要打开一个不存在文件时，一个异常就发生了，这些异常在编译时不能被简单地忽略。
-   RuntimeException运行时异常：
    运行时异常是可能被程序员避免的异常。与检查性异常相反，运行时异常可以在编译时被忽略。
-   Error错误：
    错误不是异常，而是脱离程序员控制的问题。错误在代码中通常被忽略。例如，当栈溢出时，一个错误就发生了，它们在编译也检查不到的。

## Exception类的层次

##

![](/home/ivan/my/teach/java/javase/04_exception/file/4.jpeg)

## CheckedException

-   **ClassNotFoundException**
    应用程序试图加载类时，找不到相应的类，抛出该异常。
-   **CloneNotSupportedException** 当调用 Object 类中的 clone
    方法克隆对象，但该对象的类无法实现 Cloneable 接口时，抛出该异常。
-   **IllegalAccessException** 拒绝访问一个类的时候，抛出该异常。
-   **InstantiationException** 当试图使用 Class 类中的 newInstance
    方法创建一个类的实例，而指定的类对象因为是一个接口或是一个抽象类而无法实例化时，抛出该异常。
-   **InterruptedException** 一个线程被另一个线程中断，抛出该异常。
-   **NoSuchFieldException** 请求的变量不存在
-   **NoSuchMethodException** 请求的方法不存在

## UnCheckedException 一

-   **ArithmeticException**
    当出现异常的运算条件时，抛出此异常。例如，一个整数"除以零"时，抛出此类的一个实例。
-   **ArrayIndexOutOfBoundsException**
    用非法索引访问数组时抛出的异常。如果索引为负或大于等于数组大小，则该索引为非法索引。
-   **ArrayStoreException**
    试图将错误类型的对象存储到一个对象数组时抛出的异常。
-   **ClassCastException**
    当试图将对象强制转换为不是实例的子类时，抛出该异常。
-   **IllegalArgumentException**
    抛出的异常表明向方法传递了一个不合法或不正确的参数。
-   **IllegalMonitorStateException**
    抛出的异常表明某一线程已经试图等待对象的监视器，或者试图通知其他正在等待对象的监视器而本身没有指定监视器的线程。

## UnCheckedException 二

-   **IllegalStateException**
    在非法或不适当的时间调用方法时产生的信号。换句话说，即 Java 环境或
    Java 应用程序没有处于请求操作所要求的适当状态下。
-   **IllegalThreadStateException**
    线程没有处于请求操作所要求的适当状态时抛出的异常。
-   **IndexOutOfBoundsException**
    指示某排序索引（例如对数组、字符串或向量的排序）超出范围时抛出。
-   **NegativeArraySizeException**
    如果应用程序试图创建大小为负的数组，则抛出该异常。
-   **NullPointerException** 当应用程序试图在需要对象的地方使用 null
    时，抛出该异常
-   **NumberFormatException**
    当应用程序试图将字符串转换成一种数值类型，但该字符串不能转换为适当格式时，抛出该异常。

## UnCheckedException 三

-   **SecurityException** 由安全管理器抛出的异常，指示存在安全侵犯。
-   **StringIndexOutOfBoundsException** 此异常由 String
    方法抛出，指示索引或者为负，或者超出字符串的大小。
-   **UnsupportedOperationException** 当不支持请求的操作时，抛出该异常。

## 异常捕获

``` {.java}
try {
   // 程序代码
}catch(ExceptionName e1) {
   //Catch 块
}catch(ExceptionName e2) {
   //Catch 块
}finally{
   //代码
}
```

## 否认总比沉默强

``` {.java}
try{
    throw new Exception("打碎了杯子!")
}catch(Exception e){
//    System.out.println("不是我");
//    System.out.println("偷偷把玻璃换了");
}
```

## 课堂练习

-   对刚才练习的代码进行异常捕获
-   请捕获所有的异常(请思考有几种方法?)
-   针对每种异常打印自定义错误信息


## 自定义异常

-   所有异常都必须是Throwable的子类。
-   如果希望写一个检查性异常类，则需要继承Exception类。
-   如果你想写一个运行时异常类，那么需要继承RuntimeException 类。

``` {.java}
class MyException extends Exception{
}
```

## 抛出异常

-   当你在当前方法中不能处理该异常时,可以直接抛出此异常,使用关键字throws
-   当你需要抛出自定义异常时,使用关键字throw

``` {.java}
public void test() throws Exception{}

public void test2(){   //这里还需要抛出吗?
    try{}catch(Exception e){throw new MyException();}
}
```

## finally

-   无论try中的代码是否发生异常!finally中的代码都能够执行

``` {.java}
public class Test{
    public static void main(String[] args){
        try{
            throw new MyException();
        }catch(Exception e){

        }finally{
            System.out.println("finally");
        }
    }
}
```

## finally的作用

-   主要用来恢复资源的初始状态.比如文件和网络的关闭,擦除屏幕上的图形
-   但是,finally每次都能完美的完成任务吗?思考哪些情况下会出现问题?

``` {.java}
    try{
        resource1.open();
        resource2.open();
        resource1.doSomething();
        resource2.doSomething();
    }catch{
    }finally{
        resource1.close();  //又抛出了异常
        resource2.close();
    }
```

## Java7的救赎

``` {.java}
try(Resources){
   // 程序代码
}catch(ExceptionName e1) {
   //Catch 块
}catch(ExceptionName e2) {
   //Catch 块
}
```

## 课后作业

-   编写一个类，在其main()方法的try块里面抛出一个Exception对象。传递一个String参数给Exception构造器。在catch子句里捕获此异常，并打印字符串参数。添加一个finally子句，打印一条信息已证明这里确实得到了执行
-   编写能产生并捕获ArrayIndexOutOfBoundsException异常的代码
-   使用extends关键字建立一个自定义异常。为这个类写一个接受字符串的构造器，把此参数保存在对象内部的字符串引用中。写一个方法显示此字符串。写一个try-catch子句，对这个新异常进行测试
-   定义三个自定义异常。编写一个类，在一个方法中抛出这三个异常。在main方法中调用该方法，仅使用一个catch子句捕获这三个异常

## 课后作业

-   为一个类定义两个方法:f()和g().在g()里，抛出一个自定义的新异常。在f()里，调用g(),捕获它抛出的异常，并且在catch子句里抛出另一个异常。在main方法里测试
-   重复上一个练习，但是在catch子句里把g()抛出的异常包装成Runtime Exception


