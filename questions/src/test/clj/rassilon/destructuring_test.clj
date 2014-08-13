(ns rassilon.destructuring-test
  (:require [midje.sweet :refer :all]))

(def L '(:a :b :c :d :e))
(def V [:a :b :c :d :e])

(future-fact "Sequential things can be destructured using a vector binding expression."
  (let [[a b c d e] L]
    c)
  => ?????

  (let [[a b & r] V]
    ?????)
  => [:a :b '(:c :d :e)])

(future-fact "You can get the whole list (in addition to destructured bindings) using :as"
  (let [[a b c d e :as the-list] L]
    [d the-list])
  => ?????

  (let [[a b & r :as the-vec] V]
    ?????)
  => [:a '(:c :d :e) [:a :b :c :d :e]])

(def M {:one 1
        :two 2
        :three 3
        :four 4
        :five 5})

(future-fact "Associative things can be destructured using a map binding expression"
  (let [{a :one b :three} M]
    (+ a b)
    => ?????))

(future-fact "`:as` still works for maps"
  (let [{a :one :as the-map} M]
    ?????)
  => [1 M])

(future-fact "plus maps support `:or` to provide defaults - `M` has no key `:foo`, so
we use the default for `b`."
  (let [{a :one b :foo
         :or {a 100 b 11}} M]
    (+ a b)
    => ?????))

(future-fact "Since keyword maps are so common, there is some syntactic sugar for
destructuring keyword maps"
  (let [{:keys [one two]} M]
    (+ one two)
    => 3))

(future-fact "Same goes for strings..."
  (let [{:strs [one two]} {"one" 1 ????? 2}]
    (+ one two)
    => 3))

(future-fact "...and symbols"
  (let [{:syms [one two]} {????? 1 'two 2}]
    (+ one two)
    => 3))

(future-fact "Destructuring works pretty much anywhere you can bind variables,
including function argument lists"

  (defn destructuring-fn
    [{:keys [a b] :or {a 1 b 2}}]
    (+ a b))

  (destructuring-fn {:a 3 :b 5})
  => ?????

  (destructuring-fn {:a ?????})
  => 5

  (destructuring-fn {????? 10})
  => 11)

(future-fact "If we combine rest arguments (&) with map destructuring, we get keyword
argument support for functions!"
  (defn kw-fn
    [& {:keys [a b] :or {a 1 b 2}}]
    (+ a b))

  (kw-fn :a 1 :b 3)
  => 4

  (kw-fn :a 10)
  => ?????)
