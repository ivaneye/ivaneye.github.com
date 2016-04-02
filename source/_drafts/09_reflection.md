% 反射
% 王一帆
% 江苏企业大学

## ==与equals

-   ==和equals的区别是什么?
-   先来看一段代码

``` {.java}
public class Test{
    public static void main(String[] args){
        Shape s1 = new Circle(3);
        Shape s2 = new Circle(3);
        System.out.println(s1 == s2);    //输出?
        System.out.println(s1.equals(s2));  //输出?
    }
}
```

## 课堂练习

-   请运行如上代码,查看运行结果
-   思考为什么?


## Object

-   Java中所有的类都继承自Object

![](/home/ivan/my/teach/java/javase/09_reflection/file/1.jpg)

##

-   equals可以比较两个对象
-   toString方法打印对象信息
-   hashCode给定类一个唯一的hashcode,一般重写equals方法时,需要重写此方法
-   finalize垃圾回收机制
-   clone对象拷贝(浅拷贝)
-   getClass返回类型信息

## 分析

-   对于Circle来说,我们没有重写equals方法.所以实际上调用的是Object的equals方法
-   Object中equals方法的实现就是==

## 课堂练习

-   请重写Circle的equals方法,判断只要radius相同则equals为true

## 你的实现够好吗?

-   使用eclipse自动生成equals和hashCode方法

## getClass与instanceof

``` {.java}
public class Test{
    public static void main(String[] args){
        Shape s1 = new Circle(3);
        Shape s2 = new Circle(3);
        Shape s3 = new Shape();
        System.out.println(s1.getClass() == s2.getClass());    //输出?
        System.out.println(s1.getClass().equals(s2.getClass()));  //输出?
        System.out.println(s1.getClass() == s3.getClass());    //输出?
        System.out.println(s1.getClass().equals(s3.getClass()));  //输出?
        System.out.println(s1 instanceof Circle);    //输出?
        System.out.println(s1 instanceof Shape);    //输出?
    }
}
```

-   s1.getClass()输出什么类型?Shape还是Circle?
-   getClass返回的是具体的类型(就是new出来的类型).
-   instanceof用来判断某个对象是哪个类型,或者是哪个类型的子类

## 类型信息

-   从上面的例子可以看出,在运行时我们是可以获取引用所指向的具体的类型信息的
-   每个类都有一个Class对象(每当编写并且编译一个新类的时候,就会产生一个Class对象.它是被保存在一个同名的.class文件中的)
-   Class对象中包含了所表示的类的信息
-   JVM通过类加载器来加载这些类.
-   所有的类都是在第一次使用时,动态加载进去的(回忆关于初始化顺序的讨论)

## 一个例子

-   假设我们有一个画板,可以选择图形进行绘画
-   画板调用的是我们的Shape包
-   我们来模拟这样的情形

##

``` {.java}
public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println("请输入Shape类型");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name = br.readLine();
        while (true) {
            if("exit".equalsIgnoreCase(name))return;
            switch (name) {
            case "Circle":
                System.out.println(new Circle());
                break;
            case "Rect":
                System.out.println(new Rect());
                break;
            default:
                System.out.println("找不到Shape类型");
                break;
            }
            System.out.println("请输入Shape类型");
            name = br.readLine();
        }
    }
}
```

## 新增类型

-   如果我想新增其它类型呢?比如三角形Triangle
-   我需要修改哪些地方?
-   如果画板系统不能停止运行!或者不能修改呢?

## 统一管理

-   一个解决方案是,我们可以提供一个统一的类来提供类的创建.使客户端调用这个统一的类
-   请思考如何实现?

## 实现

``` {.java}
public class ShapeFactory{
    public static Shape create(String name){
        switch (name) {
        case "Circle":
            return new Circle();
        case "Rect":
            return new Rect();
        default:
            return null;
        }
    }
}
```

``` {.java}
public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println("请输入Shape类型");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name = br.readLine();
        while (true) {
            if("exit".equalsIgnoreCase(name))return;
            System.out.println(ShapeFactory.create(name));
            System.out.println("请输入Shape类型");
            name = br.readLine();
        }
    }
}
```

## 简单工厂模式

-   这个模式叫简单工厂(其实也称不上模式,只是个通用写法)
-   现在我们添加triangle类时,就可以添加Triangle并修改ShapeFactory.客户端不需要修改任何代码

## 课堂练习

-   请以debug模式启动应用
-   请添加Triangle类,并体验一下热部署

## 还能更简单吗?

-   现在每次新增类型的时候,都需要修改ShapeFactory类,同样很麻烦
-   可以不修改吗?

## Class.forName()

-   Class.forName()方法可以根据类的全限定名来获取对象引用

``` {.java}
public class ShapeFactory{
    public static Shape create(String name){
        try {
            return (Shape) Class.forName("com.learn." + name).newInstance();
        } catch (Exception e) {
        }
        return null;
    }
}
```

## 做了什么?

-   Class.forName根据传入的类名,去查找相应的类,并将其加载进来
-   .newInstance则会创建一个该类的对象并将其返回

## 反射

-   在运行时检测对象的类型；
-   动态构造某个类的对象；
-   检测类的属性和方法；
-   任意调用对象的方法；
-   修改构造函数、方法、属性的可见性；
-   以及其他。

运行时去操作类

## 反射API

-   反射相关类在java.lang.reflect包中
-   反射加载类不限定本地,你可以从网络上加载一个对象,并执行
-   通过反射,你可以获取类的任何信息
-   不论该属性或方法是不是public的

## 反射与注解

-   JUnit是一套测试框架
-   JUnit4使用注解来处理测试的执行

``` {.java}
public class DogTest {

    private Dog dog = new Dog();

    @Test
    public void testShout(){
        System.out.println("testShout execute");
        assertEquals("wawawa",dog.shout());
    }

    @Test
    public void testShout2(){
        System.out.println("testShout2 execute");
        assertEquals("wawawa",dog.shout());
    }
}
```

## 处理方法


``` {.java}
public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class clz = Class.forName("com.focus.ann.DogTest");
        DogTest dogTest = (DogTest) clz.newInstance();
        clz = clz.newInstance().getClass();
        Method[] methods = clz.getMethods();
        Method initMethod = null;
        for(Method m : methods){
            Before before = m.getAnnotation(Before.class);
            if(before != null){
                initMethod = m;
            }
        }
        for(Method m : methods){
            Test test = m.getAnnotation(Test.class);
            if(test != null){
                initMethod.invoke(dogTest,new Object[]{});
                m.invoke(dogTest,new Object[]{});
            }
        }
    }
```

## 哪里会使用?

-   一般框架使用较多

## 课后练习

-   查看Class类和java.lang.reflect包,熟悉其方法
-   测试getDeclaredMethod和getMethod的区别
-   请针对上节课的MyTest注解,编写注解处理程序
-   使添加了MyTest注解的方法在开始执行之前和执行之后,打印一串文字
-   新建一个类，请使用反射来实例化该类
-   给上面的类添加带有参数的构造方法，使用反射来实例化该类
-   请自定义一个注解，并将注解添加到上面的类上
-   使用反射来获取上面的注解
