% 集合
% 王一帆
% 江苏企业大学

## 持有对象

-   引用:持有一个对象
-   数组:持有固定数量的对象
-   如果要持有不定数量的对象呢?
-   是否可以通过数组来实现持有不定数量的对象?

## 实现

``` {.java}
class AList{
    private Object[] arr;
    private int arrSize = 2;
    private int curIdx;
    private int length;

    public AList(){
        arr = new Object[arrSize];
    }

    public void add(Object obj){
        if(curIdx >= arrSize){
            arrSize += 2;
            Object[] temp = new Object[arrSize];
            for(int i = 0;i < arr.length; i++){
                temp[i] = arr[i];
            }
            arr = temp;
        }
        arr[curIdx] = obj;
        curIdx++;
        length++;
    }
}
```

## 集合的分类

##

-   Collection
    -   List 按照插入的顺序保存元素
    -   Set 不能有重复元素
    -   Quenue 按照排队规则来确定对象产生的顺序
-   Map 键值对,可以通过key来查询value

## 集合结构

![](/home/ivan/my/teach/java/javase/06_collection/file/5.jpg)

## Collection接口

##

``` {.example}
size():int
isEmpty():boolean
contains(Object):boolean
iterator():Iterator<E>
toArray():Object[]
toArray(T[]):T[]
add(E):boolean
remove(Object):boolean
containsAll(Collection<?>):boolean
addAll(Collection<? extends E>):boolean
removeAll(Collection<? extends E>):boolean
removeIf(Predicate<? super E>):boolean
retainAll(Collection<?>):boolean
clear():void
equals(Object):boolean
hashCode():int
spliterator():Spliterator<E>
stream():Stream<E>
parallelStream:Stream<E>
```

## 简单示例


``` {.java}
public class SimpleCollection{
    public static void main(String[] args){
        Collection c = new ArrayList();
        for(int i = 0;i < 5; i++){
            c.add(i);
        }
        System.out.println(c);   //如何打印?
    }
}
```

## 课堂练习


-   打印大于3的元素

``` {.java}
public class SimpleCollection{
    public static void main(String[] args){
        Collection c = new ArrayList();
        for(int i = 0;i < 5; i++){
            c.add(i);
        }
        System.out.println(c);
        for(int i = 0;i < 3; i++){
            c.remove(i);
        }
        System.out.println(c);
    }
}
```

## lambda表达式


``` {.java}
public class SimpleCollection{
    public static void main(String[] args){
        Collection c = new ArrayList();
        for(int i = 0;i < 5; i++){
            c.add(i);
        }
        c.stream()
         .filter(a -> (Integer)a >= 3)
         .forEach(System.out::println);
        System.out.println(c);
        c.removeIf(e-> (Integer)e < 3);
        System.out.println(c);
    }
}
```

## List

##

1. 有顺序的，元素可以重复；
2. 遍历：for，迭代；
3. 排序：Comparable Comparator Collections.sort()

-   ArrayList： 底层用数组实现的List； 特点：随机访问效率高，增删效率低
    轻量级 线程不安全；
-   LinkedList： 底层用双向循环链表实现的List；可以作为栈,队列或双端队列
    特点：随机访问效率低，增删效率高；
-   Vector: 底层用数组实现List接口的另一个类；
    特点：重量级，占据更多的系统开销，线程安全；

## 代码


``` {.java}
public class SimpleCollection{
    public static void main(String[] args){
        LinkedList c = new LinkedList();
        for(int i = 0;i < 5; i++){
            c.addLast(i);
        }
        System.out.println(c);
        System.out.println(c.removeFirst());
        System.out.println(c.peek());
        System.out.println(c.poll());
    }
}
```

## Set

1. 无顺序的，元素不可重复（值不相同）；
2. 遍历：迭代；
3. 排序：SortedSet

-   HashSet： 采用哈希算法来实现Set接口；
    唯一性保证：重复对象equals方法返回为true；
    重复对象hashCode方法返回相同的整数，不同对象hashCode尽量保证不同（提高效率）；
-   SortedSet： 对一个Set排序；
-   TreeSet： 在元素添加的同时，进行排序。也要给出排序规则；
    唯一性保证：根据排序规则，compareTo方法返回为0，就可以认定两个对象中有一个是重复对象。

## 代码

``` {.java}
public class SimpleCollection{
    public static void main(String[] args){
        Collection c = new HashSet();
        for(int i = 0;i < 5; i++){
            c.addLast(3);
        }
        System.out.println(c);
    }
}
```

## Map

1. 元素是键值对：key唯一不可重复，value可重复；
2. 遍历：先迭代遍历key的集合，再根据key得到value；

-   SortedMap：元素自动对key排序
-   HashMap: 轻量级，线程不安全，允许key或者value是null；
-   Hashtable： 重量级，线程安全，不允许key或者value是null；
    Properties：Hashtable的子类，key和value都是String
-   TreeMap：
    集合是指一个对象可以容纳了多个对象（不是引用），这个集合对象主要用来管理维护一系列相似的对象。

## 代码


``` {.java}
public class Statistics{
    public static void main(String[] args){
        Random rand = new Random(47);
        Map<Integer,Integer> m = new HashMap<Integer,Integer>();
        for(int i = 0; i < 10000; i++){
            int r = rand.nextInt(20);
            Integer freq = m.get(r);
            m.put(r,freq == null ? 1 : freq + 1);
        }
        System.out.println(m);
    }
}
```

## foreach与Iterator

-   foreach语法可以作用与任何Collection对象
-   所有的Collection都实现了Iterable接口
-   Iterable包含的iterator方法,返回一个Iterator类,提供给foreach在序列中
    进行移动

``` {.java}
public class SimpleCollection{
    public static void main(String[] args){
        LinkedList c = new LinkedList();
        for(int i = 0;i < 5; i++){
            c.addLast(i);
        }
        for(Object o : c){
            System.out.println(o);
        }
    }
}
```

## 课堂练习

-   使我们编写的AList也支持foreach语法


## 答案

``` {.java}
public class AList implements Iterable{
    ...
    private int idx;

    public static void main(String[] args) {
        AList list = new AList();
        list.add(1);
        list.add(2);
        list.add(3);
        for(Object o : list){
            System.out.println(o);
        }
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            @Override
            public boolean hasNext() {
                return idx < length;
            }

            @Override
            public Object next() {
                return arr[idx++];
            }
        };
    }
}
```

## 迭代器模式

-   提供一种方法访问一个容器对象中各个元素，而又不暴露该对象的内部细节。
-   在这里就是Iterable和Iterator


## 课后作业

-   查询API,熟悉各个集合类的方法
-   查询API,熟悉java.util.Arrays和java.util.Collections类
- 创建一个Gerbil类，宝航int gerbilNumber属性，在构造器中初始化它。添加一个hop方法，打印出gerbilNumber.创建一个ArrayList,并向其中添加一串Gerbil对象。使用get遍历List，并对每个Gerbil调用hop方法
- 创建一个类，然后创建一个用你的类对象进行初始化的数组。通过使用subList方法，创建你的List的子集，然后在你的List中移除这个子集

## 课后作业

- 修改练习1，使用Iterator遍历
- 编写一个方法，使用Iterator遍历Collection,并打印容器中每个对象。填充各种类型的Collection，然后测试
- 请使用栈对如下表达式求值：”+”表示将其后的字母入栈，”-”表示弹出栈顶字母并打印  +U+n+c---+e+r+t---+a-+i-+n+t+y---+-+r+u--+l+e+s---
- 用键值对填充一个HashMap.打印结果。抽取这些键值对，按照键进行排序，并将结果置于一个LinkedHashMap中，并展示
