---
layout: post
title: Clojure教程-命名空间
categories: clojure
tags: [clojure]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 版本
本文翻译自[Clojure Namespaces and Vars](http://clojure-doc.org/articles/language/namespaces.html)

本文涵盖如下内容:

+ Clojure命名空间和var概述
+ 如何定义命名空间
+ 如何使用其它命名空间里的函数
+ require,refer和use
+ 常见错误和典型错误,以及导致这些错误的原因
+ 命名空间和代码管理

版权:

This work is licensed under a Creative Commons Attribution 3.0 Unported License (including images & stylesheets). The source is available on Github.

涵盖Clojure版本:Clojure 1.5

# 概述
Clojure的函数通过命名空间来组织.Clojure命名空间和Java的包或者Python的模块很类似.命名空间实际上就是个map,将名字映射到了var上.在大部分情况下,这些var持有这些函数.

# 定义一个命名空间
一般情况下使用clojure.core/ns宏来定义命名空间.最基本的形式下,它将名字作为符号.

```clojure
(ns superlib.core)
```

命名空间可以由点号切割的好多段组成

```clojure
(ns megacorp.service.core)
```

需要注意的是,请尽量避免使用单段的命名空间,以免与其它开发人员的命名空间相冲突.如果库或者应用属于某个组织,那么建议以如下形式作为命名空间.[组织名称].[包名|应用名].[函数组名]
例如

```clojure
(ns clojurewerkz.welle.kv)

(ns megacorp.search.indexer.core)
```
<!-- more -->

另外,ns宏可以包含如下形式:

+ (:require ...)
+ (:import ...)
+ (:use ...)
+ (:refer-clojure ...)
+ (:gen-class ...)

这些其实就是clojure.core/import,clojure.core/require等等这些的简写形式而已

## :require
:require形式可以使你的代码能访问其它命名空间的Clojure代码.例如

```clojure
(ns megacorp.profitd.scheduling
  (:require clojure.set))

;; Now it is possible to do:
;; (clojure.set/difference #{1 2 3} #{3 4 5})
```

此代码将保证clojure.set命名空间被加载,编译并且可以通过clojure.set名来调用.当然可以给加载的命名空间取个别名:

```clojure
(ns megacorp.profitd.scheduling
  (:require [clojure.set :as cs]))

;; Now it is possible to do:
;; (cs/difference #{1 2 3} #{3 4 5})
```

一次导入两个命名空间的例子;

```clojure
(ns megacorp.profitd.scheduling
  (:require [clojure.set  :as cs]
            [clojure.walk :as walk]))
```

### :refer选项
如果想在当前命名空间里通过简写名称来引用clojure.set空间里的函数,可以通过refer来实现:

```clojure
(ns megacorp.profitd.scheduling
  (:require [clojure.set :refer [difference intersection]]))

;; Now it is possible to do:
;; (difference #{1 2 3} #{3 4 5})
```

:require形式中的:refer特性为Clojure1.4新增特性.

可能有时需要引入某个命名空间下所有的函数:

```clojure
(ns megacorp.profitd.scheduling
  (:require [clojure.set :refer :all]))

;; Now it is possible to do:
;; (difference #{1 2 3} #{3 4 5})
```


## :import

:import的作用是在当前命名空间引入Java类:

```clojure
(ns megacorp.profitd.scheduling
  (:import java.util.concurrent.Executors))
```

执行上面的代码后,java.util.concurrent.Executors类将会被引入,请可以直接通过名字Executors来使用.可以同时引入多个类.

```clojure
(ns megacorp.profitd.scheduling
  (:import java.util.concurrent.Executors
           java.util.concurrent.TimeUnit
           java.util.Date))
```

如果引入的多个类在同一个包下面,就像上面那样,可以使用如下的简介方式:

```clojure
(ns megacorp.profitd.scheduling
  (:import [java.util.concurrent Executors TimeUnit]
           java.util.Date))
```
虽然导入的list被叫做list,实际上可以使用任意的Clojure的集合(一般使用vector)

## 当前命名空间

Clojure将通过\*ns\*来持有当前的命名空间.使用def形式定义的var被添加到了当前命名空间中.

## :refer-clojure

我们在使用像clojure.core/get这样的函数和clojure.core/defn这样的宏的时候我们不需要使用它的全限定名.这是因为Clojure默认将clojure.core下的内容全部加载进了当前命名空间里了.所以如果你定义了一个函数名和clojure.core里的重复了(比如find),你将会得到一个警告.

```sh
WARNING: find already refers to: #'clojure.core/find in namespace:    megacorp.profitd.scheduling, being replaced by: #'megacorp.profitd.scheduling/find
```

这个警告的意思是在megacorp.profitd.scheduling这个命名空间里,已经有一个clojure.core/find了,但是现在它被你定义的函数覆盖了.请记住,Clojure是很动态的语言,命名空间就是map而已.

解决这个问题的办法有:你可以重命名你的函数或者不引入clojure.core里的这个函数

```clojure
(ns megacorp.profitd.scheduling
  (:refer-clojure :exclude [find]))

(defn find
  "Finds a needle in the haystack."
  [^String haystack]
  (comment ...))
```
在这里,如果你想使用clojure.core/find的话,你需要通过全限定名来使用:

```clojure
(ns megacorp.profitd.scheduling
  (:refer-clojure :exclude [find]))

(defn find
  "Finds a needle in the haystack."
  [^String haystack]
  (clojure.core/find haystack :needle))
```

## :use

Clojure在1.4之前,:require是不支持:refer的,只能使用:use

```clojure
(ns megacorp.profitd.scheduling-test
  (:use clojure.test))
```

在上面的例子中,clojure.test里的所有内容都被引入到了当前命名空间中.但是一般不会这样使用,建议是只引入需要的函数:

```clojure
(ns megacorp.profitd.scheduling-test
  (:use clojure.test :only [deftest testing is]))
```

1.4以前的做法

```clojure
(ns megacorp.profitd.scheduling-test
  (:require clojure.test :refer [deftest testing is]))
```

而现在鼓励的做法是使用:require,通过:refer来进行限制.

## 文档与元数据

命名空间可以包含说明文档.你可以在ns宏里添加:

```clojure
(ns superlib.core
  "Core functionality of Superlib.

   Other parts of Superlib depend on functions and macros in this namespace."
  (:require [clojure.set :refer [union difference]]))
```

或者元数据

```clojure
(ns ^{:doc "Core functionality of Superlib.
            Other parts of Superlib depend on functions and macros in this namespace."
      :author "Joe Smith"}
   superlib.core
  (:require [clojure.set :refer [union difference]]))
```
元数据可以包含任意的键,例如:author,很多工具可以使用(像Codox,Cadastre或者lein-clojuredocs)

# 如何在REPL里使用其它命名空间的函数

ns宏是你经常需要使用的,它引入其它命名空间的函数.但是它在REPL里不太方便.这里可以直接使用require:

```clojure
;; Will be available as clojure.set, e.g. clojure.set/difference.
(require 'clojure.set)

;; Will be available as io, e.g. io/resource.
(require '[clojure.java.io :as io])
It takes a quoted libspec. The libspec is either a namespace name or a collection (typically a vector) of [name :as alias] or [name :refer [fns]]:

(require '[clojure.set :refer [difference]])

(difference #{1 2 3} #{3 4 5 6})  ; ⇒ #{1 2}
```

:as和:refer可以一起使用

```clojure
(require '[clojure.set :as cs :refer [difference]])

(difference #{1 2 3} #{3 4 5 6})  ; ⇒ #{1 2}
(cs/union #{1 2 3} #{3 4 5 6})    ; ⇒ #{1 2 3 4 5 6}
```

clojure.core/use可以做和clojure.core/require一样的事情,但是不推荐使用了.

# 命名空间和编译

Clojure是一个需要编译的语言:代码在被加载的时候进行编译.

命名空间可以包含var或者去继承协议,添加多重方法实现或载入其它库.所以为了完成编译,你需要引入需要的命名空间.

# 私有Vars

Vars(包括defn宏定义的函数)可以设为私有的.有两种方法可以来做这件事情:使用元数据或者defn-宏

```clojure
(ns megacorp.superlib)

;;
;; Implementation
;;

(def ^{:private true}
  source-name "supersource")

(defn- data-stream
  [source]
  (comment ...))
```

# 常量Vars

Vars可以设为常量,通过:const元数据来设置.这将会促使Clojure编译器将其编译为常量:

```clojure
(ns megacorp.epicgame)

;;
;; Implementation
;;

(def ^{:const true}
  default-score 100)
```

# 如何通过名称来查找和执行函数

可以通过clojure.core/resolve在制定的命名空间里通过名字查找函数.名字需要使用引号修饰.返回值可以直接当做函数使用,比如,当做参数传递给高阶函数:

```clojure
(resolve 'clojure.set 'difference)  ; ⇒ #'clojure.set/difference

(let [f (resolve 'clojure.set 'difference)]
   (f #{1 2 3} #{3 4 5 6}))  ; ⇒ #{1 2}
```

# 编译异常

本节讨论一些常见的编译错误.

## ClassNotFoundException

这个异常的意思是JVM无法加载类.可能是因为拼写错误,或者在classpath上没有这个类.可能是你的项目没有很好的处理依赖关系.

```sh
user=> (import java.uyil.concurrent.TimeUnit)
ClassNotFoundException java.uyil.concurrent.TimeUnit  java.net.URLClassLoader$1.run (URLClassLoader.java:366)
```

在上面的例子中,java.uyil.concurrent.TimeUnit拼写错误,应该是java.util.concurrent.TimeUnit


## CompilerException java.lang.RuntimeException: No such var

这个错误的意思是,使用了一个不存在的var.这可能是拼写错误,或者不正确的宏展开等类似问题.

```sh
user=> (clojure.java.io/resouce "thought_leaders_quotes.csv")
CompilerException java.lang.RuntimeException: No such var: clojure.java.io/resouce, compiling:(NO_SOURCE_PATH:1)
```

在上面的例子中,clojure.java.io/resouce应该写成clojure.java.io/resource.NO_SOURCE_PATH的意思是编译是在repl里触发的,而不是一个Clojure源文件.
