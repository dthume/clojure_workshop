(ns rassilon.fn-test
  (:require [midje.sweet :refer :all]))

(fact "`ifn?` tests is something is a function (implements `clojure.lang.IFn`)"
  (ifn? +)
  => true
  
  (ifn? 1)
  => false)

(fact "Functions can be defined with `fn`"
  (ifn? (fn [a b] (+ a b)))
  => true)

(fact "To call a function, we write a list of the form `(function arg1...argN)`"
  ((fn [a b] (+ a b)) 1 2)
  => 3

  ((fn [a b] (- a b)) 5 3)
  => 2)

(fact "Functions are first class, so we can assign them to let bindings or
variables. Because symbols are resolved to their value, we can then use the
bound names anywhere we can use a function literal (just like with primitives
and collections)"
  (def my-add (fn [a b] (+ a b)))
  => #'my-add

  (my-add 1 2)
  => 3

  (let [my-subtract (fn [a b] (- a b))]
    (my-subtract 10 5)
    => 5))

(facts "BTW - I meant it when I said it's written in the same datastructures
that it processes."
  
  (fact "`fn` is just a symbol!"
    (symbol? (first '(fn [a b] (+ a b)))))

  (fact "the argument list really is a vector!"
    (vector? (second '(fn [a b] (+ a b))))
    => true)

  (fact "the arguments inside the arg vector are just symbols!"
    (symbol?
     (first
      (second
       '(fn [a b] (+ a b)))))))

(fact "Because defining top level functions is a rather common task, we can use
`defn`, which provides syntactic sugar on top of `def` + `fn`."
  (defn my-multiply [a b] (* a b))
  => #'my-multiply

  (my-multiply 2 4)
  => 8)

(fact "Multiple arities are supported, by using nested lists of ([args] & body)"
  (defn multi-add
    ([a]
       a)
    ([a b]
       (+ a b))
    ([a b c]
       (+ a b c)))
  => #'multi-add

  (multi-add 1)
  => 1

  (multi-add 1 2)
  => 3

  (multi-add 1 2 3)
  => 6)

(fact "varargs are also supported, using `&`"
  (defn varargs-add
    [& args]
    (apply + args))
  => #'varargs-add

  (varargs-add 1 2 3 4 5)
  => 15)

(fact "That last example used `apply`, which works much like `function.apply`
in Javascript - it takes a function, and 1 or more arguments, the last of which
should be a sequence, and calls the function with the combined sequence of
arguments. This makes it useful for working with functions generically"
  (apply + [1])
  => 1

  (apply + 1 [2])
  => 3

  (apply + [1 2])
  => 3

  (apply + 1 2 [])
  => 3

  (apply + 1 2 3 [4 5])
  => 15

  (apply my-add [1 2])
  => 3)

(fact "We can also use an anonymous function literal, which allows us to write
simple functions in an extremely succinct way"

  (#(+ %1 %2) 1 2)
  => 3

  (let [f #(- %1 %2)]
    (f 3 2)
    => 1))

(fact "If we plan to use a single argument, we can use bare `%`"
  (#(+ % %) 3)
  => 6)

(fact "rest args are still supported"
  (#(apply + %&) 1 2 3 4 5)
  => 15)

(fact "Functions support pre and post conditions - % can be used inside `:post`
conditions to refer to the function result"
  (defn constrained-fn
    [a b]
    {:pre [(pos? a) (pos? b)]
     :post [(pos? %)]}
    (+ a b))
  => #'constrained-fn

  (constrained-fn 1 2)
  => 3

  (constrained-fn -1 2)
  => (throws AssertionError)

  (constrained-fn 1 -2)
  => (throws AssertionError))

