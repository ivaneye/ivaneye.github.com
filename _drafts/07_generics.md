% 泛型
% 王一帆
% 江苏企业大学

## 泛型以前

``` {.java}
List list = new LinkedList();
list.add(new Integer(0));
Integer x = (Integer) list.get(0);
```

-   add方法可以塞入任意的类型,没有任何的限制
-   get方法获取的类型永远是Object,当需要具体类型时,则需要强制转换
-   这样会带来什么问题呢?
-   带来的问题

##

``` {.java}
List list = new LinkedList();
list.add(new Integer(0));           //可以塞入任意类型
list.add("Str");                    //可以塞入字符串
Integer x = (Integer) list.get(1);   //是否能编译通过?是否能执行?
```

## 泛型的使用

``` {.java}
List<Integer> list = new LinkedList<Integer>();
list.add(new Integer(0));
list.add("Str");           //编译错误
Integer x = list.get(0);
```

## 泛型类

``` {.java}
public class Prt<T>{
    public void print(T t){
        System.out.println(t);
    }
}
```

-   不需要方法重载

## 泛型与子类化

-   引入泛型就是为了安全性
-   将运行时的问题,提前到了编译期
-   请思考如下代码是否有问题

``` {.java}
List<String> ls = new ArrayList<String>();
List<Object> lo = ls;   //编译是否错误?运行是否错误?

class Shape{}
class Circle extends Shape{}

List<Shape> list = new ArrayList();
List<Circle> c = new ArrayList();
list = c;     //编译是否错误?运行是否错误?

public void pri(List<Shape> list){...}  //是否可传入List<Circle>?
```

## 带来的问题

-   Shape是Circle的父类
-   List\<Shape\>并不是List\<Circle\>的父集合
-   无法使用多态
-   是否可以解决?

## 通配符

``` {.java}
void printCollection(Collection c){
    Iterator i = c.iterator();
    for (k = 0; k < c.size(); k++){
        System.out.println(i.next());
    }
}
```

-   可以传入任何集合吗?

``` {.java}
void printCollection(Collection<Object> c){
    for (Object e : c) {
        System.out.println(e);
    }
}
```

-   可以传入任何集合吗?

``` {.java}
void printCollection(Collection<?> c){
    for (Object e : c) {
        System.out.println(e);
    }
}
```

## 边界


-   带通配符的集合可以接收任何类型的集合
-   那如果只想传入指定类的集合或指定类的子类集合呢?

``` {.java}
public class Shape{}
public class Circle extends Shape{}
public class Rectangle extends Shape {}

public void drawAll(List<Shape> shapes) {
    ...
}
```

``` {.java}
public void drawAll(List<? extends Shape> shapes) {
    ...
}
```

## 泛型方法


-   可以单独将方法编写为泛型方法
-   当可以使用泛型方法时,建议使用泛型方法

``` {.java}
public <T> void prt(T bean){
   ...
}
```

## 泛型与数组

-   数组与泛型不能很好的集成
-   无法创建泛型数组

``` {.java}
T[] arr = new T[10];
```

-   但是可以强制转型

``` {.java}
T[] arr = (T[])new Object[10];
```

-   当需要使用泛型的时候,建议和集合一起使用

## 课堂练习

-   修改上节的AList类,实现泛型化

## 答案

``` {.java}
class AList<T>{
    private T[] arr;
    private int arrSize = 2;
    private int curIdx;
    private int length;

    public AList(){
        arr = (T[]) new Object[arrSize];
    }

    public void add(T obj){
        if(curIdx >= arrSize){
            arrSize += 2;
            T[] temp = (T[]) new Object[arrSize];
            for(int i = 0;i < arr.length; i++){
                temp[i] = arr[i];
            }
            arr = temp;
        }
        arr[curIdx] = obj;
        curIdx++;
        length++;
    }

    public T get(int idx){
        return arr[idx];
    }
}

```

## 擦除

-   Java中的泛型使用的是擦除技术来实现
-   其实就是为了提高安全性,而在编译时多做了一些检查
-   泛型在编译后将会消失,在运行时和不使用泛型时没有任何区别
-   add时都是Object,get时都使用了强制转换

##

``` {.java}
public class Test1{

    public static void main(String[] args){
        List list = new ArrayList();
        list.add(1);
        int a = (Integer)list.get(0);
    }
}
```

``` {.java}
import java.util.*;

public class Test2{

    public static void main(String[] args){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        int a = list.get(0);
    }
}
```

-   反编译如上两段代码,有区别吗?


## 课后作业

- 创建一个Holder类，使其能够持有具有相同类型的3个对象，并提供相应的读写方法访问这些对象，以及一个可以初始化其持有的3个对象的构造器
- 创建一个泛型单向链表SList，为了简单起见，不要实现List接口。列表中的每个Link对象都应该包含一个队列表中下一个元素的引用。创建你自己的SListIterator，同样为了简单起见，不要实现ListIterator。SList中除了toString之外唯一的方法应该是iterator(),它将产生一个SListIterator。在SList中插入和移除元素的唯一方式就是通过SListIterator。编写代码来演示SList
- 使用LinkedList作为底层实现，定义你自己的SortedSet
