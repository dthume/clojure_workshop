(ns rassilon.flow-test
  (:require [midje.sweet :refer :all]))

(fact "`nil`, `false` are logically false, everything else is logically true"
  true    => TRUTHY
  false   => FALSEY
  nil     => FALSEY
  1       => TRUTHY
  0       => TRUTHY ;; 0 is not false!
  "foo"   => TRUTHY
  "false" => TRUTHY)

(future-fact "`and` takes a sequence of expressions, and returns the first logically
false value, or else the last value if all expressions are logically true.
`(and)` returns `true`.

`and` is short circuiting, so if an expression is logically false, subsequent
expressions will not be evaluated"
  (and 1 2 3 4)     => ?????
  (and ????? true)  => false
  (and)             => ?????
  (and 4 3 2 ?????) => 1
  (and 1 2 ????? 3) => nil)

(future-fact "`or` takes a sequence of expressions, and returns the first logically
true value, or else the last value if all expressions are logically false.
`(and)` returns `nil`.

`or` is short circuiting, so if an expression returns logically true, subsequent
expressions will not be evaluated."
  (or 1 2 3 4)          => ?????
  (or false ?????)      => true
  (or)                  => ?????
  (or ????? 3 2 1)      => 4
  (or nil ????? nil 3)  => 2)

(future-fact "`if` provides basic conditional expressions"
  (if (= 1 1)
    "foo"
    "bar")
  => ?????

  (if (= 1 2)
    "foo"
    ?????)
  => "bar")

(future-fact "`when` is an `if` with no `else` expression"
  (when (= 1 1)
    "foo")
  => ?????

  (when (= 1 2)
    "foo")
  => ?????)

(future-fact "`cond` is like an if / else if / else"
  (defn test-cond
    [x]
    (cond
     (neg? x)  :neg
     (pos? x)  :pos
     ;; Note that :else is just a keyword which happens to be logically true
     ;; we could easily write `true :zero`
     :else     :zero))
  => #'test-cond

  (test-cond -1)    => ?????
  (test-cond ?????) => :pos
  (test-cond ?????) => :zero

  (defn fizz-buzz
    [n]
    ?????)
  => #'fizz-buzz

  (fizz-buzz 1)  => 1
  (fizz-buzz 2)  => 2
  (fizz-buzz 3)  => "fizz"
  (fizz-buzz 4)  => 4
  (fizz-buzz 5)  => "buzz"
  (fizz-buzz 6)  => "fizz"
  (fizz-buzz 15) => "fizzbuzz")

(fact "Exception handling is almost identical to Java"
  (try
    (/ "foo" "bar")
    (catch Exception e
      "woops"))
  => "woops")
