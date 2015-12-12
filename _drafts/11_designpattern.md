% 设计模式
% 王一帆
% 江苏企业大学


## 已经学过的设计模式

-   装饰模式
-   迭代器模式
-   简单工厂

## 什么是设计模式

-   每一个模式描述了一个在我们周围不断重复发生的问题,以及该问题的解决方案
的核心。这样,你就能一次又一次地使用该方案而不必做重复劳动---摘自GOF\<设
计模式可复用面向对象软件的基础\>
-   消极一点的看法就是,设计模式是为了弥补语言语法上的不足
-   GOF中定义了23中设计模式,就目前来看,有些模式已经不那么常用了.比如工厂
    模式(后续学习的SpringIOC,可以完全起到工厂的作用).迭代器模式(绝大部分
    情况下,你不需要自己去重新定义集合类.Java提供了足够你使用的集合了)

## 设计模式的使用

-   建议重构出设计模式,而不是套用设计模式
-   我们在前面的使用,都是一步步的为解决问题而不断演变出来的
-   设计模式只是给出参考,具体的设计还是需要依据具体问题而定

## 常用设计模式

## 单例模式(Singleton)

- Singleton顾名思义就是只能创建一个实例对象。
- 如何保证只能创建一个对象呢？

. . .

``` {.java}
  public class Singleton{
         private Singleton(){}
  }
```

##

- 既然构造方法是私有的，那么从外面不可能创建Singleton实例了
- 那么如何创建对象呢？

. . .

``` {.java}
  public class Singleton{
         private Singleton(){}
         public static Singleton getInstance(){
              return [Singleton Instance];
        }
  }
```

##

- getInstance方法要返回一个Singleton实例,就要一个Singleton类型的变量来存储。声明一个Singleton类型的属性
- 同样需要是static的,为什么？

``` {.java}
  public class Singleton{
         private Singleton(){}

         public static Singleton getInstance(){
              single=new Singleton();
              return single;
        }
        private static Singleton single;
  }
```

##

- 如此就能获得Singleton的实例了。
- 这样能保证只生成一个实例吗？

. . .

``` {.java}
public class Singleton{
       private Singleton(){}

       public static Singleton getInstance(){
           if(single==null){
                single=new Singleton();
           }
           return single;
       }
      private static Singleton single;
}
```

##

- 假设有两个线程:thread1，thread2。
- thread1运行到第5行，然后跳到了thread2。也运行到第5行之后。此时两线程都得到single为空。
- 此时能保证只生成一个实例吗？

``` {.java}
public class Singleton{
       private Singleton(){}

       public synchronized static Singleton getInstance(){
           if(single==null){
                single=new Singleton();
           }
           return single;
       }
      private static Singleton single;
}
```
## 另一种解决方案

``` {.java}
public class Singleton{
       private Singleton(){}

       public static Singleton getInstance(){
                  return single;
       }
      private static Singleton single=new Singleton();
}
```

## double-checkedlocking

``` {.java}
public class Singleton{
       private Singleton(){}

       public static Singleton getInstance(){
           if(single==null){
               synchronized(Singleton.class){
                  if(single==null){
                     single=new Singleton();
                  }
               }
           }
           return single;
       }
      private volatile static Singleton single;
}
```


## 代理模式(Proxy)

- 代理模式的一种作用和代理商很类似。就像人们到商店去买东西，商店会提供一些附加服务，但是商店是不会生产东西的，商店是到工厂去拿东西。商店就是一个代理。
- 具体流程就像这样，用户想要一个item，于是到Shop去buyItem，而商店是不会生产item的，他就到ItemFactory去让工厂生产item，然后卖给客户，并提供售前售后服务。代码如下：

## 工厂接口

``` {.java}
public interface ItemFactory{
     public Item getItem();
}
```

## 工厂实现

``` {.java}
//Item就不实现了，随便怎么写都行
public class ItemFactoryImpl implements ItemFactory{
      public Item getItem(){
          return new Item();
      }
}
```

## 商店类

``` {.java}
//就是代理
public class Shop implements ItemFactory{

     private ItemFactory factory = new ItemFactoryImpl();

     public Item getItem(){
          System.out.println("附加服务");
          return factory.getItem();
     }
}
```

## 调用

``` {.java}
public class Main{
     public static void main(String[] args){
          ItemFactory f = new Shop();
          f.getItem();
     }
}
```

- 从代码可以看到,Shop中的getItem的getItem委托ItemFactoryImpl的getItem来生产Item，另外附加了自己的"附加服务"。
- 这是代理模式的一种作用。代理模式能做安全保护的作用。比如登录，如果用户名或密码不正确就不允许登录，想想怎么实现。

## 适配器模式(Adapter)


- 适配器模式的作用就是将一个类的接口转换成另外一个接口。这种模式一般是用在使用已有系统的情况。
- 假设目前做一个新系统，有如下代码结构。

``` {.java}
public interface Car{
     public void drive();
}

public class Benz implements Car{
     public void drive(){
          System.out.println("Benz run");
     }
}

public class Cruze implements Car{
     public void drive(){
          System.out.println("Cruze run");
     }
}
```

##

有一个老系统，里面有如下代码

``` {.java}
public class Smart{
     public void run(){
         System.out.println("Smart run");
     }
}
```

##

很明显，Smart和Benz,Cruze不属于同类。而实际上Smart和Benz,Cruze一样都属于Car，怎么办呢？

``` {.java}
public class SmartCar extends Smart implements Car{
      public void drive(){
           this.run();
      }
}
```

- 这种方式叫做类的适配器。
- 但是，如果Car是个类，而不是接口呢？Java是不支持多继承的！

##

``` {.java}
public class SmartCar implements Car{
     private Smart smart = new Smart();

     public void drive(){
          smart.run();
     }
}
```

- 这种方式就叫做对象的适配器。

## 组合模式(Composite)

- 将对象组合成树形结构以表示“部分 -整体”的层次结构。Composite使得用户对单个对象和组合对象的使用具有一致性。
- 组合模式主要使用在树形结构上

## 示例

``` {.java}
public interface Node {
    void check();
    void add(Node node) throws Exception;
    void remove(Node node) throws Exception;
}
```

## 示例

``` {.java}
public class ParentNode implements Node {
    private List list = new ArrayList();

    public void check() {
        System.out.println("ParentNode is checked");
    }

    public void add(Node node) {
        list.add(node);
    }

    public void remove(Node node) {
        list.remove(node);
    }
}
```

## 示例

``` {.java}
public class ChildNode implements Node {
    public void check() {
        System.out.println("ChildNode is Checked");
    }

    public void add(Node node) throws Exception {
        throw new Exception("This node can not add");
    }

    public void remove(Node node) throws Exception {
        throw new Exception("This node can not remove");
    }
}
```

## 示例

``` {.java}
public class Main {
    public static void main(String[] args) throws Exception {
        Node node1 = new ChildNode();
        Node node2 = new ChildNode();
        Node node3 = new ParentNode();
        node3.add(node1);
        node3.add(node2);
        node1.check();
        node2.check();
        node3.check();
    }
}
```

## UML图

![](/home/ivan/my/teach/java/javase/11_design/file/6.png)

## 模板模式(Template)

举个很简单的例子,你要做个网站,需要写页面,但是页面的头和尾都是不变的?如何做?

``` {.java}
public class Page{
    public void show(){
        System.out.println("头部");
        System.out.println("内容");
        System.out.println("尾部");
    }
}
```

-   头部和尾部是不变的,而内容是变化的
-   所以我们抽出变化的部分"内容"

##

``` {.java}
public class Page{

    public void show(){
        System.out.println("头部");
        content();
        System.out.println("尾部");
    }

    public void content(){
        System.out.println("内容");
    }
}
```

-   实际上详细的内容需要到具体的页面上才知道!
-   每个页面的结构不变,都是show()方法
-   而content不同,我们就可以将content方法延迟到子类去实现

##

``` {.java}
public abstract class Page{
    public void show(){
        System.out.println("头部");
        content();
        System.out.println("尾部");
    }

    public abstract void content();
}
```

##

``` {.java}
public class Index extends Page{
    public void content(){
        System.out.println("首页内容");
    }
}
public class Detail extends Page{
    public void content(){
        System.out.println("明细页面内容");
    }
}
```

## 观察者模式(Observer)

- Observer直译为中文为观察者。所以此模式又称为观察者模式。个人认为叫通知模式更好理解
- 至于用到哪儿，根据代码应该就能猜得出来。一个对象的改变会影响其他对象的响应，而且对象个数不确定。
- 我们来模拟一下3Q大战！

##

``` {.java}
//QQ Client
public interface QQ{
     public boolean login(String username,String password);
     public void popWin(String msg);
}
```

##

``` {.java}
//QQ Server
public interface QQServer{

    private List<QQ> list = new ArrayList();
    public void addQQ(QQ qq){
         list.add(qq);
    }

    public void popAll(String msg){
        for(QQ qq : list) qq.popWin(msg);
    }
}
```

##

``` {.java}
//QQ实现
public class MyQQ implements QQ{
     public boolean login(String username,String password){
           //这里使用网络发送请求到QQServer，如果用户名密码正确，就调用addQQ方法,于是我们的QQ就上战场了
     }

     public void popWin(String msg){
          System.out.println(msg);
     }
}
```

这样，只要QQServer一做出"艰难的决定"，我们就都遭殃了！

## 状态模式(State)

- 状态模式和策略模式很像，不过状态模式多了一个状态的改变。可以说，策略模式给了你积木，让你随意的搭积木，而状态模式多了一些限制，就是一个积木后面要跟另一个积木，而不能随意搭。
- 相信大家都在网上买过东西，买东西的流程：选择商品-》付款-》卖家发货-》买家确认-》完成。这其中对应了订单状态的变化：未付款-》已付款-》已发货-》完成交易。代码如下：

##

``` {.java}
public class Order{
     private static int UN_PAY = 0;
     private static int PAYED = 1;
     private static int SEND = 2;
     private static int DONE = 3;    //四种状态，可以使用枚举

     private int state = UN_PAY;     //当前状态

     public void pay(){
          if(state = UN_PAY){
               pay money;
               state = PAYED;
          }else{
               exception;
          }
     }

     public void send(){
             .....        //同样的if,else
     }

     ....
}
```

##

- 可以看出如上代码充斥了很多的if,else，当状态更多时，此类膨胀很厉害，且难以维护
- 状态模式将各个状态封装成一个个的类，并做该状态下可以做的事情，Order只是简单的委托。

##

```java
public class Order{
     private State unPayState;
     private State payedState;
     private State sendState;
     private State doneState;    //四种状态，四种类,对应的get,set方法省略

     private State state = unPayState;     //当前状态

     public Order(){
        unPayState = new UnPayState(this);
        payedState = new PayedState(this);            //传入Order对象，为了修改Order的状态
        ...
     }

     public void pay(){
          state.pay();
     }

     public void send(){
          state.send();
     }

     ....
}
```

##

```java
//State接口
public interface State{
     public void pay();
     public void send();
     ...
}

//UnPayState
public class UnPayState implements State{

     private Order order;
     public UnPayState(Order order){
           this.order = order;
     }

     public void pay(){
         //pay money
         order.setState(order.getPayedState());
     }

     public void send(){
         //exception: can't send
     }
     .....
}
```

## 策略模式(Strategy)

- 定义一系列的算法 ,把它们一个个封装起来 ,并且使它们可相互替换。本模式使得算法可独立于使用它的客户而变化。
- 策略模式的结构很简单，其实就是面向接口编程。具体是对其的理解。

![](/home/ivan/my/teach/java/javase/11_design/file/8.gif)

##

- 策略模式将一个个的具体策略（逻辑、算法或行为）封装到了一个个的子类中，而由用户（客户端）自己决定调用哪个策略！换一种说法，策略模式将逻辑从服务端提到了客户端，本来由服务器端来判断的事情交由客户端自己决定，服务器端只提供应有的功能。就像给你积木，让你自己搭积木一样。
- 举个简单的例子，写个简单计算器，可能会这么写：

##

``` {.java}
//判断符号，来决定做怎样的计算。这就导致了，当添加新计算逻辑的时候，此类要不断维护
if(type.equals("+")) return a+b;
else if(type.equals("-")) return a-b;
....
```

```java
//而策略模式将加减乘除封装到一个个的类中
public class AddStrategy implements Strategy{
     public double caculate(double a,double b){
           return a + b;
     }
}

//当调用时，比如当按下了界面上的+按钮，则将AddStrategy设置给客户类
client.setStrategy(new AddStrategy());
return client.result();    //result方法委托caculate方法即可
```
```

- 当需要再添加新算法时，添加相应的子类即可（当然了，客户端还是要修改的）！

## 职责链模式(Chain of Responsibility)

- 责任链模式的典型应用就是过滤器！

``` {.java}
//模拟Request和Response类
public class Request{
    private HashMap<String,String> map = new HashMap<String,String>();

    public void setAttribute(String key,String value){
         map.put(key,value);
    }

    public String getAttribute(String key){
        return  map.get(key);
    }

    public String toString(){
        String return_str = "";
        Set key = map.keySet();
         Iterator i = key.iterator();
         while (i.hasNext()){
             return_str = return_str + "=====" + map.get(i.next());
         }
         return return_str;
    }
}
```

##

```java
public class Response{
    private HashMap<String,String> map = new HashMap<String,String>();

    public void setAttribute(String key,String value){
         map.put(key,value);
    }

    public String getAttribute(String key){
        return  map.get(key);
    }

    public String toString(){
        String return_str = "";
        Set key = map.keySet();
         Iterator i = key.iterator();
         while (i.hasNext()){
             return_str = return_str + "=====" + map.get(i.next());
         }
         return return_str;
    }
}
```

##

```java
//Filter接口及其实现类
public interface Filter{
    public void doFilter(Request request,Response response,FilterChain chain);
}

public class CheckFilter implements Filter{
    @Override
    public void doFilter(Request request,Response response,FilterChain chain){
          request.setAttribute("name",request.getAttribute("name")+"Check");
          chain.doFilter(request,response,chain);
          response.setAttribute("name",response.getAttribute("name")+"Check");
    }
}

public class LoginFilter implements Filter{

    @Override
    public void doFilter(Request request,Response response,FilterChain chain){
          request.setAttribute("name",request.getAttribute("name")+"Login");
          chain.doFilter(request,response,chain);
          response.setAttribute("name",response.getAttribute("name")+"Login");
    }
}
```

##

```java
//FilterChain维护了一个Filter的链
public class FilterChain {

    private LinkedList<Filter> list = new LinkedList<Filter>();
    private int index = 0;


    public FilterChain addFilter(Filter filter){
        list.add(filter);
        return this;
    }

    public void doFilter(Request request,Response response,FilterChain chain){
        if(index == list.size()) {
            return;
        }

        Filter f = list.get(index);
        index ++;
        f.doFilter(request, response, chain);
    }
}
```

##

```java
//测试
public class  ChainTest{

    @Before
    public void init(){

    }

    @Test
    public void testChain(){
        Request r = new Request();
        Response p = new Response();
        r.setAttribute("name","Ivan");
        p.setAttribute("name","Ivan");

        FilterChain chain = new FilterChain();
        chain.addFilter(new LoginFilter()).addFilter(new CheckFilter());

        chain.doFilter(r,p,chain);

        System.out.println(r.toString());
        System.out.println(p.toString());
    }
}
```

##

- 如上代码是责任链模式的一个变种。你可以去除FilterChain类，在每个Filter里面维护一个Filter实例，指向下一个Filter（链表的结构），在doFilter中进行判断。这样就是一个基本的责任链模式了。

