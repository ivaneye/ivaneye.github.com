% 多线程
% 王一帆
% 江苏企业大学

## 想喝茶吗?

-   想泡壶茶喝。当时的情况是：开水没有。开水壶要洗，茶壶茶杯要洗，火已升
    了，茶叶也有了，怎么办？
-   摘自华罗庚《统筹方法平话》

## 最笨的解决方案

-   洗水壶,装水,烧水,等水开,洗茶杯,泡茶!
-   有人会这么干吗?
-   但是,我们的程序只能这么干!

## 模拟

-   茶杯类,包含洗的方法

``` {.java}
public class Cup {
    public void wash(){
        System.out.println("开始洗茶杯");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        System.out.println("结束洗茶杯");
    }
}
```

##

-   茶壶类,包含洗和烧水的方法

``` {.java}
public class Kettle {
    public void wash() {
        System.out.println("开始洗茶壶");
        try {
                Thread.sleep(3000);
        } catch (InterruptedException e) {}
        System.out.println("结束洗茶壶");
    }

    public void burn() {
        System.out.println("开始烧水");
        try {
                Thread.sleep(4000);
        } catch (InterruptedException e) {}
        System.out.println("结束烧水");
    }
}
```

##

-   测试类

``` {.java}
public class Person {

    public static void main(String[] args) {
        Cup cup = new Cup();
        Kettle k = new Kettle();
        k.wash();
        k.burn();
        cup.wash();
    }

}
```

## 多线程

-   多线程也称并发
-   它是我们可以将程序划分为多个分离的,独立运行的任务
-   一个线程就是在进程中的一个单一的顺序控制流

## 线程的生命周期

![](/home/ivan/my/teach/java/javase/10_thread/file/1.jpg)

## 描述

-   新状态:一个新产生的线程从新状态开始了它的生命周期。它保持这个状态知道程序start这个线程。
-   运行状态:当一个新状态的线程被start以后，线程就变成可运行状态，一个线程在此状态下被认为是开始执行其任务
-   就绪状态:当一个线程等待另外一个线程执行一个任务的时候，该线程就进入就绪状态。当另一个线程给就绪状态的线程发送信号时，该线程才重新切换到运行状态。
-   休眠状态:由于一个线程的时间片用完了，该线程从运行状态进入休眠状态。当时间间隔到期或者等待的时间发生了，该状态的线程切换到运行状态。
-   终止状态:一个运行状态的线程完成任务或者其他终止条件发生，该线程就切换到终止状态。

## Java中的线程类

Java中可以使用两种方式来实现多线程

-   继承java.lang.Thread
-   实现java.lang.Runnable

## 修改程序

-   对于上面的程序,在烧水的过程中,是可以去洗杯子的
-   也就是说烧水这个过程和洗杯子这个过程可以并行
-   我们分别通过Thread类和Runnable接口来实现这个过程

## 继承Thread类

``` {.java}
public class Kettle extends Thread{

    ...

    @Override
    public void run() {
        burn();
    }
}
```

-   只需要继承Thread类
-   覆写run方法即可.这里run方法就是调用烧水方法

## 调用

``` {.java}
public class Person {

    public static void main(String[] args) {
        Cup cup = new Cup();
        Kettle k = new Kettle();
        k.wash();
        k.run();    //OK?
        cup.wash();
    }

}
```

-   这样写有问题吗?

## 修改

-   k.run()只是简单的方法调用,并不会启动线程
-   要启动线程需要调用start()方法

```java
    public class Person {

        public static void main(String[] args) {
            Cup cup = new Cup();
            Kettle k = new Kettle();
            k.wash();
            k.start();
            cup.wash();
        }
    }
```

## 课堂练习

-   请自行测试上面的代码
-   请修改继承Thread类为实现Runnable接口
-   尝试调用看看!

## 实现Runnable

``` {.java}
public class Kettle implements Runnable{

    ...

    @Override
    public void run() {
        burn();
    }
}
```

-   调用

``` {.java}
public class Person {
    public static void main(String[] args) {
        Cup cup = new Cup();
        Kettle k = new Kettle();
        Thread t = new Thread(k);
        k.wash();
        t.start();
        cup.wash();
    }
}
```

## 很完美?

-   No.这是噩梦的开始

## 再看一个例子

-   你在使用ATM取钱
-   而恰巧,你父母在通过ATM机给你汇钱
-   会发生什么事情呢?

## 实例

``` {.java}
public class You extends Thread{
    private ATM atm;

    public You(ATM atm){
        this.atm = atm;
    }

    @Override
    public void run() {
        atm.minus(10);
    }
}
```

##

``` {.java}
public class Parent extends Thread{
    private ATM atm;

    public Parent(ATM atm){
        this.atm = atm;
    }

    @Override
    public void run() {
        atm.add(11);
    }
}
```

##

``` {.java}
public class ATM {

    private long money = 100;

    public void add(long money){
        this.money += money;
    }

    public void minus(long money){
        this.money -= money;
    }

    public void show(){
        System.out.println(money);
    }
}
```

##

-   测试

``` {.java}
public static void main(String[] args) throws InterruptedException{
    ATM atm = new ATM();
    You you = new You(atm);
    Parent p = new Parent(atm);
    atm.show();
    you.start();
    p.start();
    Thread.sleep(1000);
    atm.show();
}
```

-   好像没什么问题

## 稍微做个修改

##

``` {.java}
public class ATM {

    private long money = 100;

    public void add(long money){
        long temp = this.money + money;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.money = temp;
    }

    public void minus(long money){
        long temp = this.money - money;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.money = temp;
    }

    public void show(){
        System.out.println(money);
    }
}
```

-   再运行试试

##

-   你的钱要么是90,要么是111
-   你可能会想变成111不是挺好的?你觉得银行会放过你吗?!
-   为什么add和minus方法做如上调整.实际Java在计算时就是类似的计算过程.也
    就是说+,-操作不是原子性的

## 如何解决

-   最简单的方式是添加synchronized关键字

## 课堂练习

-   请编写如上代码,测试运行
-   给add方法和minus方法添加synchronized关键字,再次运行

## 死锁

-   synchronized可以给当前方法或语句块加锁,使得其它线程无法去调用
-   那么可能出现这种情况.两个线程t1,t2.t1锁住一个任务,等待t2的任务.而t2锁住了的任务在等待t1的任务.这就导致了死锁

##

``` {.java}
public class Test implements Runnable {
    public int flag = 1;
    static Object o1 = new Object(), o2 = new Object();
    public void run() {
        System.out.println("flag=" + flag);
        if(flag == 1) {
            synchronized(o1) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized(o2) {
                    System.out.println("1");
                }
            }
        }
        if(flag == 0) {
            synchronized(o2) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized(o1) {
                    System.out.println("0");
                }
            }
        }
    }

    public static void main(String[] args) {
        Test td1 = new Test();
        Test td2 = new Test();
        td1.flag = 1;
        td2.flag = 0;
        Thread t1 = new Thread(td1);
        Thread t2 = new Thread(td2);
        t1.start();
        t2.start();
    }
}
```

## 线程的调度和优先级

每一个Java线程都有一个优先级，这样有助于操作系统确定线程的调度顺序。Java优先级在MIN\~PRIORITY\~（1）和MAX\~PRIORITY\~（10）之间的范围内。默认情况下，每一个线程都会分配一个优先级NORM\~PRIORITY\~（5）。
具有较高优先级的线程对程序更重要，并且应该在低优先级的线程之前分配处理器时间。然而，线程优先级不能保证线程执行的顺序，而且非常依赖于平台。

## 后台线程(守护线程)

-   后台进程是指在程序运行的时候后台提供的一种通用服务的线程,并且这种线程并不属于程序中不可或缺的部分

``` {.java}
Thread d = new Thread();
d.setDaemon(true);   //必须在start前设定
d.start();
```

-   在主线程结束后,如果有其它非后台线程,则程序会等待其它线程结束
-   而对于后台线程,当主线程结束后,程序直接结束

## Next

-   这些内容只是线程的入门
-   线程除了Lock机制还有jdk7新增的fork/join框架,以及其它语言的一些实现.比如Scala的Actor,Clojure的STM
-   如果需要了解详细的线程机制,可以参考\<Java并发编程实践\>

## 课后练习

- 实现一个Runnable。在run()内部打印一个消息，然后调用yield()。重复这个操作三次，然后从run中返回。在构造器中放置一条启动消息，并且放置一条在任务终止时的关闭消息。使用线程创建大量的这种任务并驱动它们

