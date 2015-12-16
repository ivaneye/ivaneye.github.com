(ns compiler.comp
  (:import (clojure.lang PersistentQueue)))

;计算器解释器
(defn calc [exp]
  (if (number? exp) exp
      (let [[fun e1 e2] exp
              v1 (calc e1)
              v2 (calc e2)]
          ((eval fun) v1 v2))))


;表达式解释器
;; 空环境
;(def env0 (atom []))
(def v0 (atom []))
;
;;; 扩展。对环境 env 进行扩展，把 x 映射到 v，得到一个新的环境
;(defn ext-env [x v env] (reset! env (conj @env {x v})))
;
;;; 查找。在环境中 env 中查找 x 的值
;
;(defn lookup [x env]
;  (if-let [r (filter #(get % x) @env)]
;    (get (first r) x)))

(defn add-value [v]
  (reset! v0 (conj @v0 v)))

(defn get-value []
  (let [r (peek @v0)]
    (reset! v0 (pop @v0))
    r))

;; 闭包的数据结构定义，包含一个函数定义 f 和它定义时所在的环境

;(struct Closure (f env))

;; 解释器的递归定义（接受两个参数，表达式 exp 和环境 env）

;; 共 5 种情况（变量，函数，调用，数字，算术表达式）

(defn interp1 [exp]
                (cond                                          ; 模式匹配 exp 的以下情况（分支）
                    (symbol? exp) (get-value)          ; 变量
                    (number? exp) exp                               ; 数字
                    (= 'fn (first exp))
                          (interp1 (last exp))
                    (= 2 (count exp))
                    (let [[e1 e2] exp
                          r (add-value e2)
                          v1 (interp1 e1)
                          v2 (interp1 e2)]             ;函数计算
                      v1
                      )
                    (= 3 (count exp))
                         (let [[fun e1 e2] exp             ;算数计算
                             v1 (interp1 e1)
                             v2 (interp1 e2)]
                         ((eval fun) v1 v2))))

;; 解释器的“用户界面”函数。它把 interp1 包装起来，掩盖第二个参数，初始值为 env0

(defn interp2 [exp]
          (interp1 exp))

(defmacro interp [exp]
  `(interp2 '~exp))





(interp (+ 1 2))

;; => 3

(interp (* 2 3))

;; => 6

(interp (* 2 (+ 3 4)))

;; => 14

(interp (* (+ 1 2) (+ 3 4)))

;; => 21

(interp (((fn [x] (fn [y] (* x y))) 5) 3))

;; => 6

(interp ((fn [x] (* 2 x)) 3))

;; => 6



(interp ((fn [y] (((fn [y] (fn [x] (* y 2))) 5) 0)) 4))

;; => 6;; (interp ‘(1 2))

