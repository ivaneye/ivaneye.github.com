---
layout: post
title: Clojure进阶:使用Clojure编写文字冒险游戏
categories: clojure
tags: [clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---

本文翻译自:[Casting SPELs in Clojure](http://www.lisperati.com/clojure-spels/casting.html)


![]({{site.CDN_PATH}}/assets/clojure/cs_01.jpg)


准备
====

任何学过Lisp的人都会说Lisp和其它语言有很大的不同.它有很多不可思议的地方.本文将告诉你它有哪些独特之处!

本文适用于Clojure,它是一个运行在JVM上的Lisp方言.Clojure的API和语法和
Common Lisp很类似,但是还是有足够多的区别,需要单独为其写个教程.

在大部分情况下,我们会说Lisp而不是Clojure,因为大部分的概念在Lisp中是通用的.我们会指出Clojure特有的内容.

![]({{site.CDN_PATH}}/assets/clojure/cs_02.jpg)

Clojure是运行在JVM之上的,所以你需要先安装JVM.如果你是MAC机,那么Java已经被安装过了.如果是Linux或者Windows系统,你需要到[Oracle Java官网](http://java.sun.com/javase/downloads/index.jsp)下载对应版本的Java.

而Clojure,你可以从它的[官网](http://clojure.org/)获得最新版本.下载完成后,你只需要解压缩,打开命令行,切换到解压缩目录,输入:

```sh
java -jar clojure.jar
```

如果没有问题,那么你将会看到Clojure输出提示

```sh
Clojure 1.6.0
user=>
```

教程中有很多Clojure代码片段,类似下面的样子:

```clojure
'(these kinds of boxes)
```

你只需要将这些代码片段拷贝到Clojure REPL中运行就可以了!当你学习完此教程,你将会有一个你自己的文字冒险游戏了!

<!-- more -->

语法和语义
==========

每一个编程语言是由语法和语义组成的.语法是组成你的程序的骨架,你必须要遵循它们,这样编译器才能知道你的程序里什么是什么,比如说什么是函数,什么是变量,等等!

而语义是个比较"随便"的东西,例如你的程序里有哪些不同的命令,或者在程序的哪个部分能访问到哪些变量!这里Lisp比较特别的地方就是,Lisp的语法比其它任何语言都要简单.

首先,Lisp语法规定,所有传递给Lisp编译器的文本需要是个list,当然这个list可以无限嵌套.每个list都必须使用括号包裹.

![]({{site.CDN_PATH}}/assets/clojure/cs_03.jpg)

另外,Lisp编译器使用两种模式来读取你的代码:代码模式和数据模式.当你在数据模式下,你可以将任何东西塞到你的list中.但是在代码模式下,你的list需要是叫做form的特殊类型.

![]({{site.CDN_PATH}}/assets/clojure/cs_04.jpg)

form也是个list,不过它的第一个符号被lisp编译器特殊对待了---一般被当做函数的名字.在这种情况下,编译器会将list中的其它元素作为函数参数传递给这个函数.默认情况下,编译器运行在代码模式下,除非你特意告诉它进入数据模式.

为我们的游戏世界定义数据
========================

为了进一步的学习form,让我们来创建一些form,来定义我们游戏世界里的数据.首先,我们的游戏有一些对象,玩家可以使用他们--让我们来定义吧:

```clojure
(def objects '(whiskey-bottle bucket frog chain))
```

让我们来看看这行代码是什么意思:Lisp编译器总是使用代码模式来读取内容,所以第一个符号(这里是def),肯定是个命令.

在这里,它的作用就是给某个变量设值:这里变量就是objects,而值是一个包含四个对象的list.这个list是数据(我们可不想编译器去调用一个叫做whiskey-bottle的函数),所以在读取这个list时
我们需要将其设值为数据模式.在list前面的哪个单引号就是干这个的:

![]({{site.CDN_PATH}}/assets/clojure/cs_05.jpg)

def命令就是用来设值的(如果你学过Common Lisp,你应该会知道它和CommonLisp中的setf命令等价,但是Clojure中没有setf命令)

现在我们在游戏里定义了一些对象,现在让我们来定义一下游戏地图.下面是我们的游戏世界:

![]({{site.CDN_PATH}}/assets/clojure/cs_06.jpg)

在这个简单的游戏里,只有三个地点:一个房子,它包含起居室,阁楼和花园.让我们来定义一个新变量,叫做game-map来描述这个游戏地图:

```clojure
(def game-map (hash-map
   'living-room '((you are in the living room
                   of a wizards house - there is a wizard
                   snoring loudly on the couch -)
                  (west door garden)
                  (upstairs stairway attic))
   'garden '((you are in a beautiful garden -
              there is a well in front of you -)
             (east door living-room))
   'attic '((you are in the attic of the
             wizards house - there is a giant
             welding torch in the corner -)
            (downstairs stairway living-room))))
```

这个map包含了三个地点的所有重要信息:每个地点都有个独立的名字,一个简短的描述,描述了我们能在这些地点看到什么,以及如何进入此处或从此处出去.

请注意这个包含了丰富信息的变量是如何定义的---Lisp程序员更喜欢用小巧的代码片段而不是一大片代码,因为小代码更容易理解.

现在我们有了一个地图和一组对象,让我们来创建另一个变量,来描述这些对象在地图的哪些地方.

```clojure
(def object-locations (hash-map
                       'whiskey-bottle 'living-room
                       'bucket 'living-room
                       'chain 'garden
                       'frog 'garden))
```

这里我们将每个对象和地点进行了关联.Clojure提供了Map这个数据结构.Map使用hash-map函数来创建,它需要一组参数类似(key1 value1 keys value2...).我们的game-map变量也是个Map---三个key分别是living-room,garden和attic.

我们定义了游戏世界,以及游戏世界中的对象,现在就剩下一件事了,就是描述玩家的地点!

```clojure
(def location 'living-room)
```

搞定,现在让我们来定义游戏操作吧!

![]({{site.CDN_PATH}}/assets/clojure/cs_07.jpg)


环顾我们的游戏世界
==================

我们想要的第一个命令能够告诉我们当前地点的描述.那么我们该怎么定义这个函数呢?它要知道我们想要描述的地点以及能够从map中查找地点的描述.如下:

```clojure
(defn describe-location [location game-map]
  (first (location game-map)))
```

defn定义了一个函数.函数的名字叫做describe-location,它需要两个参数:地点和游戏地图.这两个变量在函数定义的括号内,所以它们是局部变量,因此对于全局的location和game-map没有关系.

注意到了吗?Lisp中的函数与其它语言中的函数定义相比,更像是数学中的函数:它不打印信息或者弹出消息框:它所作的就是返回结果.

我们假设现在我们在起居室里!

![]({{site.CDN_PATH}}/assets/clojure/cs_08.jpg)

为了能找到起居室的描述,describe-locatin函数首先需要从地图中找到起居室.(location game-map)就是进行从game-map中查找内容的,并返回起居室的描述.然后first命令来处理返回值,取得返回的list的第一个元素,这个就是起居室的描述了. 现在我们来测试一下

```clojure
(describe-location 'living-room game-map)
```

```sh
 user=> (describe-location 'living-room game-map)
 (you are in the living-room of a wizard's house -
 there is a wizard snoring loudly on the couch -)
```

很完美!这就是我们要的结果!请注意我们在living-room前添加了一个单引号,因为这个符号是地点map的一个名称!但是,为什么我们没有在game-map前面添加单引号呢?这是因为我们需要编译器去查询这个符号所指向的数据(就是那个map)

函数式编码风格
==============

你可能已经发现了describe-location函数有几个让人不太舒服的地方.

第一,为什么要传递位置和map参数,而不是直接使用已经定义的全局变量?原因是Lisp程
序员喜欢写函数式风格的代码.函数式风格的代码,主要遵循下面三条规则:

-   只读取函数传递的参数或在函数内创建的变量
-   不改变已经被设值的变量的值
-   除了返回值,不去影响函数外的任何内容

你也许会怀疑在这种限制下你还能写代码吗?答案是:可以!为什么很多人对这些规则感到疑惑呢?一个很重要的原因是:遵循此种风格的代码更加的引用透明(referential transparency):这意味着,对于给定的代码,你传入相同的参数,永远返回相同的结果---这能减少程序的错误,也能提高程序的生产力!

当然了,你也会有一些非函数式风格的代码,因为不这么做,你无法和其它用户或外部内容进行交互.教程后面会有这些函数,他们不遵循上面的规则.

describe-location函数的另一个问题是,它没告诉我们怎么进入一个位置或者怎么从某个位置出来.让我们来编写这样的函数:

```clojure
(defn describe-path [path]
  `(there is a ~(second path) going ~(first path) from here -))
```

这个函数看起来很明了:它看起来更像是数据而不是函数.我们先来尝试调用它,看它做了些什么:

```clojure
(describe-path '(west door garden))
```

```sh
user=> (describe-path '(west door garden))
(user/there user/is user/a door user/going west user/from user/here clojure.core/-)
```

这是什么?!结果看起来很乱,包含了很多的/和一些其它的文字!这是因为Clojure会将命名空间的名字添加到表达式的前面.我们这里不深究细节,只给你提供消除这些内容的函数:

```clojure
(defn spel-print [list] (map (fn [x] (symbol (name x))) list))
```

修改调用方式

```clojure
(spel-print (describe-path '(west door garden)))
```

```sh
user=> (spel-print (describe-path '(west door garden)))
(there is a door going west from here -)
```

现在结果很清晰了:这个函数接收一个描述路径的list然后将其解析到一个句子里面.我们回过头来看这个函数,这个函数和它产生的数据非常的像:它就是拼接第一个和第二个list的元素到语句中!它是怎么做到的?使用语法quote!

还记得我们使用quote来从代码模式切换到数据模式吗?语法quote的功能类似,但是还不只这样.在语法quote里,我们还能使用'\~'再次从数据模式切换回代码模式.

![]({{site.CDN_PATH}}/assets/clojure/cs_09.png)

语法quote是List的一个很强大的功能!它能使我们的代码看起来像它创建的数据.这在函数式编码中很常见:创建这种样子的函数,使得我们的代码更易读也更稳健:

只要数据不变,函数就不需要修改.想象一下,你能否在VB或C中编写类似的代码?你可能需要将文字切成小块,然后在一点点的组装-这和数据本身看起来差距很大,更别说代码的稳健性了!

现在我们能描述一个路径,但是一个地点可能会有多个路径,所以让我们来创建一个函数叫做describe-paths:

```clojure
(defn describe-paths [location game-map]
  (apply concat (map describe-path (rest (get game-map location)))))
```

这个函数使用了另一个在函数式编程中很常用的技术:高阶函数.apply和map这两个函数能将其它的函数作为参数.map函数将另一个函数分别作用到list中的每个对象上,这里是调用describe-path函数.apply concat是为了减少多余的括号,没有多少功能性操作!我们来试试新函数

```clojure
(spel-print (describe-paths 'living-room game-map))
```

```sh
user=> (spel-print (describe-paths 'living-room game-map))
(there is a door going west from here -
there is a stairway going upstairs from here -)
```

漂亮!

最后,我们还剩下一件事要做:描述某个地点的某个对象!我们先写个帮助函数来告诉我们在某个地方是否有某个对象!

```clojure
(defn is-at? [obj loc obj-loc] (= (obj obj-loc) loc))
```

=也是个函数,它判断对象的地点是否和当前地点相同!

![]({{site.CDN_PATH}}/assets/clojure/cs_09.jpg)

我们来尝试一下:

```clojure
(is-at? 'whiskey-bottle 'living-room object-locations)
```

```sh
user=> (is-at? 'whiskey-bottle 'living-room object-locations)
true
```

返回结果是true,意味着whiskey-bottle在起居室.

现在让我们来使用这个函数描述地板:

```clojure
(defn describe-floor [loc objs obj-loc]
  (apply concat (map (fn [x]
                       `(you see a ~x on the floor -))
                     (filter (fn [x]
                               (is-at? x loc obj-loc)) objs))))
```

这个函数包含了很多新事物:首先,它有匿名函数(fn定义的函数).第一个fn干的事,和下面的函数做的事情是一样的:

```clojure
(defn blabla [x] `(you see a ~x on the floor.))
```

然后将这个blabla函数传递给map函数.filter函数是过滤掉那些在当前位置没有出现的物体.我们来试一下新函数:

```clojure
(spel-print (describe-floor 'living-room objects object-locations))
```

```sh
user=> (spel-print (describe-floor 'living-room objects object-locations))
(you see a whiskey-bottle on the floor - you see a bucket on the floor -)
```

现在,让我们来将这些函数串联起来,定义一个叫look的函数,使用全局变量(这个函数就不是函数式的了!)来描述所有的内容:

```clojure
(defn look []
  (spel-print (concat (describe-location location game-map)
          (describe-paths location game-map)
          (describe-floor location objects object-locations))))
```

![]({{site.CDN_PATH}}/assets/clojure/cs_10.jpg)

我们来试一下:

```sh
user=> (look)
(you are in the living room of a wizards house -
there is a wizard snoring loudly on the couch -
there is a door going west from here -
there is a stairway going upstairs from here -
you see a whiskey-bottle on the floor -
you see a bucket on the floor -)
```

很酷吧!

环游我们的游戏世界
==================

好了,现在我们能看我们的世界了,让我们来写一些代码来环游我们的世界.walk-direction包含了一些方向可以使我们走到那里:

```clojure
(defn walk-direction [direction]
  (let [next (first (filter (fn [x] (= direction (first x)))
                            (rest (location game-map))))]
    (cond next (do (def location (nth next 2)) (look))
          :else '(you cannot go that way -))))
```

这里的let用来创建局部变量next,用来描述玩家的方向.rest返回一个list,包含原list中除了第一个元素外的全部元素.如果用户输入了错误的方向,next会返回
().

cond类似于if-then条件:每个cond都包含一个值,lisp检查该值是否为真,如果为真则执行其后的动作.在这里,如果下一个位置不是nil,则会定义玩家的location到新位置,然后告诉玩家该位置的描述!如果next是nil,则告诉玩家,无法到达,请重试:

```clojure
(walk-direction 'west)
```

```sh
user=> (walk-direction 'west)
(you are in a beautiful garden -
there is a well in front of you -
there is a door going east from here -
you see a frog on the floor -
you see a chain on the floor -)
```

现在,我们通过创建look函数来简化描述.walk-direction也是类似的功能.但是它需要输入方向,而且还有个quote.我们能否告诉编译器west仅仅是个数据,而不是代码呢?

构建SPELs
=========

现在我们开始学习Lisp中一个很强大的功能:创建SPELs!SPEL是"语义增强逻辑"的简称,它能够从语言级别,按照我们的需求定制,对我们的代码添加新的行为-这是Lisp最为强大的一部分.为了开启SPELs,我们需要先激活Lisp编译器的SPEL

```clojure
(defmacro defspel [& rest] `(defmacro ~@rest))
```

现在,我们来编写我们的SPEL,叫做walk:

```clojure
(defspel walk [direction] `(walk-direction '~direction))
```

这段代码干了什么?它告诉编译器walk不是实际的名称,实际的名字叫walk-direction,并且direction前面有个quote.SPEL的主要功能就是能在我们的代码被编译器编译之前插入一些内容!

![]({{site.CDN_PATH}}/assets/clojure/cs_11.jpg)

注意到了吗?这段代码和我们之前写的describe-path很类似:在Lisp中,不只是代码和数据看起来很像,代码和特殊形式对于编译器来说也是一样的-高度的统一带来简明的设计!我们来试试新代码:

```clojure
(walk east)
```

```sh
user=> (walk east)
(you are in the living room of a wizards house -
there is a wizard snoring loudly on the couch -
there is a door going west from here -
there is a stairway going upstairs from here -
you see a whiskey-bottle on the floor -
you see a bucket on the floor -)
```

感觉好多了! 现在我们来创建一个命令来收集游戏里的物品

```clojure
(defn pickup-object [object]
  (cond (is-at? object location object-locations)
        (do
          (def object-locations (assoc object-locations object 'body))
          `(you are now carrying the ~object))
        :else '(you cannot get that.)))
```

这个函数检查物品是否在当前地点的地上-如果在,则将它放到list里面,并返回成功提示!否则提示失败! 现在我们来创建另一个SPEL来简化这条命令:

```clojure
(defspel pickup [object] `(spel-print (pickup-object '~object)))
```

调用

```clojure
(pickup whiskey-bottle)
```

```sh
user=> (pickup whiskey-bottle)
(you are now carrying the whiskey-bottle)
```

现在我们来添加更多有用的命令-首先,一个能让我们查看我们捡到的物品的函 数:

```clojure
(defn inventory []
  (filter (fn [x] (is-at? x 'body object-locations)) objects))
```

以及一个检查我们是否有某个物品的函数:

```clojure
(defn have? [object]
   (some #{object} (inventory)))
```

创建特殊操作
============

现在我们只剩下一件事情需要做了:添加一些特殊动作,使得玩家能够赢得游戏.第一条命令是让玩家在阁楼里给水桶焊接链条.

```clojure
(def chain-welded false)

(defn weld [subject object]
  (cond (and (= location 'attic)
             (= subject 'chain)
             (= object 'bucket)
             (have? 'chain)
             (have? 'bucket)
             (not chain-welded))
        (do (def chain-welded true)
            '(the chain is now securely welded to the bucket -))
        :else '(you cannot weld like that -)))
```

首先我们创建了一个新的全局变量来进行判断,我们是否进行了此操作.然后我们创建了一个weld函数,来确认此操作的条件是否完成,如果已完成则进行此操作.

![]({{site.CDN_PATH}}/assets/clojure/cs_12.jpg) 来试一下:

```clojure
(weld 'chain 'bucket)
```

```sh
user=> (weld 'chain 'bucket)
(you cannot weld like that -)
```

Oops...我们没有水桶,也没有链条,是吧?周围也没有焊接的机器!

现在,让我们创建一条命令来将链条和水桶放到井里:

```clojure
(def bucket-filled false)

(defn dunk [subject object]
  (cond (and (= location 'garden)
             (= subject 'bucket)
             (= object 'well)
             (have? 'bucket)
             chain-welded)
        (do (def bucket-filled true)
            '(the bucket is now full of water))
        :else '(you cannot dunk like that -)))
```

注意到了吗?这个命令和weld命令看起来好像!两条命令都需要检查位置,物体和对象!但是它们还是有不同,以至于我们不能将它们抽到一个函数里.太可惜了!

但是...这可是Lisp.我们不止能写函数,还能写SPEL!我们来创建了SPEL来处理:

```clojure
(defspel game-action [command subj obj place & args]
  `(defspel ~command [subject# object#]
     `(spel-print (cond (and (= location '~'~place)
                             (= '~subject# '~'~subj)
                             (= '~object# '~'~obj)
                             (have? '~'~subj))
                        ~@'~args
                        :else '(i cannot ~'~command like that -)))))
```

非常复杂的SPEL!它有很多怪异的quote,语法quote,逗号以及很多怪异的符号!更重要的是他是一个构建SPEL的SPEL!!即使是很有经验的Lisp程序员,也需要费下脑细胞才能写出这么个玩样!!(这里我们不管)

![]({{site.CDN_PATH}}/assets/clojure/cs_13.jpg)

这个SPEL的只是向你展示,你是否够聪明来理解这么复杂的SPEL.而且,即使这段代码很丑陋,如果它只需要写一次,并且能生成几百个命令,那么也是可以接受的!

让我们使用这个新的SPEL来替换我们的weld命令:

```clojure
(game-action weld chain bucket attic
   (cond (and (have? 'bucket) (def chain-welded true))
              '(the chain is now securely welded to the bucket -)
         :else '(you do not have a bucket -)))
```

现在我们来看看这条命令变得多容易理解:game-action这个SPEL使得我们能编写我们想要的核心代码,而不需要额外的信息.这就像我们创建了我们自己的专门创建游戏命令的编程语言.使用SPEL创建伪语言称为领域特定语言编程(DSL),它使得你的编码更加的快捷优美!

```clojure
(weld chain bucket)
```

```sh
user=> (weld chain bucket)
(you do not have a chain -)
```

...我们还没有做好焊接前的准备工作,但是这条命令生效了!

![]({{site.CDN_PATH}}/assets/clojure/cs_14.jpg)

下面我们重写dunk命令:

```clojure
(game-action dunk bucket well garden
             (cond chain-welded
                   (do (def bucket-filled true)
                       '(the bucket is now full of water))
                   :else '(the water level is too low to reach -)))
```

注意weld命令需要检验我们是否有物体,但是dunk不需要.我们的game-action这个SPEL使得这段代码易写易读.

![]({{site.CDN_PATH}}/assets/clojure/cs_15.jpg)

最后,就是将水泼到巫师身上:

```clojure
(game-action splash bucket wizard living-room
             (cond (not bucket-filled) '(the bucket has nothing in it -)
                   (have? 'frog) '(the wizard awakens and sees that you stole
                                       his frog -
                                       he is so upset he banishes you to the
                                       netherworlds - you lose! the end -)
                   :else '(the wizard awakens from his slumber and greets you
                               warmly -
                               he hands you the magic low-carb donut - you win!
                               the end -)))
```

![]({{site.CDN_PATH}}/assets/clojure/cs_16.jpg)

现在你已经编写完成了一个文字冒险游戏了!

点击[这里](http://www.lisperati.com/clojure-spels/cheat.html)是完整的游戏.

点击[这里](http://www.lisperati.com/clojure-spels/code.html)是代码.

为了使教程尽可能的简单,很多Lisp的执行细节被忽略了,所以最后,让我们来看看这些细节!

附录
====

现在,我们来聊一聊被忽略的细节!

首先,Clojure有一套很成熟的定义变量以及改变变量值的系统.在此教程中,我们只使用了def来设置和改变全局变量的值.而在真正的Clojure代码里,你不会这么做.取而代之,你会使用[Refs](http://clojure.org/refs),[Atoms和](http://clojure.org/atoms)[Agents](http://clojure.org/agents),它们提供了更清晰,以及线程安全的方式来管理数据.

另一个问题就是我们在代码中大量使用了符号(symbol)

```clojure
'(this is not how Lispers usually write text)
"Lispers write text using double quotes"
```

符号在Clojure有特殊含义,主要是用来持有函数,变量或其它内容的.所以,在Lisp中将符号作为文本信息描述是很奇怪的事情!使用字符串来显示文本信息可以避免这样的尴尬!不过,使用字符串的话,在教程里就没法讲很多关于符号的内容了!

还有就是SPEL在Lisp里面更普遍的叫法是"宏",使用defmacro来定义,但是这个名字不易于教学,所以没有提及.你可以阅读[此文](http://www.lisperati.com/clojure-spels/no_macros.html),这是我为什么没有使用"宏"这个名字的原因.

最后,在编写类似game-action这样的SPEL的时候,很可能会发生命名重复的问题.当你编写了足够多的lisp的时候,你会越来越能体会到这个问题了.

Q. 后面我该阅读哪些内容来扩充我的Lisp知识? A.
在[cliki网站](http://www.cliki.net/Lisp%2520books)有很多Lisp书籍可以下载.

如果你对于理论上的内容很感兴趣,那么我推荐Paul Graham的 [On Lisp](http://www.paulgraham.com/onlisp.html)电子书,它是免费的.他网站上的一些短文也很精彩.

如果你对实际应用比较感兴趣,那么大多数Lisp程序员对Perter Seibel编写的"Practical Common Lisp"这本书推崇有加,你可以从[这里](http://www.gigamonkeys.com/book/)获得

为什么没有使用"宏"这个词
========================

编写这个教程的一个意图是使用宏来解决真实的难题.而经常的,当我向没有Lisp经验的人解释宏这个概念的时候,我得到的答复往往是,"哦!C++里也有宏".当发生这种事情的时候,我就很难去解释宏的概念了.的确,Lisp中的宏和C++中的宏的确有几分相似,它们都是为了能通过编译器来改进代码的编写...

...所以,假设一下,如果John McCarthy使用了"add"而不是"cons"这个词来将元素添加到list中:我就真的很难解释cons是如何工作的了!

所以,我决定在此文中使用一个新的词汇:SPEL,语义增强逻辑的简称,它更易理解
一些:

-   它解释了Lisp宏的核心功能,能改变Lisp运行环境的行为
-   SPEL这个术语可以被用来很高雅的解释很多语言上观念.
-   这个术语不会导致Lisp中的宏与其它的宏被混为一谈
-   SPEL这个词重复的可能性非常低.Google搜索"macro 或者 macros 程序 -lisp -scheme"返回大概1150000条结果.而搜索"spel 或者 spels 程序 -lisp -scheme"值返回28400条结果.

所以,我希望,作为一个Lisp程序员,你能接受这个术语.当然了,像这样的新词汇会被接受的可能性非常低.

如果你有一个库或者是一个Lisp实现者,请先放下你手头上的工作,先在你的库里,添加下面这行代码:

```clojure
(defmacro defspel [& rest] `(defmacro ~@rest))
```

译者感想
========

-   本人对Lisp的宏还是有些了解的,所以个人无法接受SPEL这个新词汇
-   且SPEL使得代码不易阅读,就game-action这个SPEL来说,使用了两层,而使用宏只需要一层
-   附录中是我使用Clojure的惯用法重新改写的代码,且文字翻译成了中文.以及使用了宏而不是SPEL.各位可比较,自行选择

[源代码](/assets/clojure/game.zip)
