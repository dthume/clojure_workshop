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

(fact "`and` takes a sequence of expressions, and returns the first logically
false value, or else the last value if all expressions are logically true.
`(and)` returns `true`.

`and` is short circuiting, so if an expression is logically false, subsequent
expressions will not be evaluated"
  (and 1 2 3 4)     => 4
  (and false true)  => false
  (and)             => true
  (and 4 3 2 1)     => 1
  (and 1 2 nil 3)   => nil)

(fact "`or` takes a sequence of expressions, and returns the first logically
true value, or else the last value if all expressions are logically false.
`(and)` returns `nil`.

`or` is short circuiting, so if an expression returns logically true, subsequent
expressions will not be evaluated."
  (or 1 2 3 4)      => 1
  (or false true)   => true
  (or)              => nil
  (or 4 3 2 1)      => 4
  (or nil 2 nil 3)  => 2)

(fact "`if` provides basic conditional expressions"
  (if (= 1 1)
    "foo"
    "bar")
  => "foo"

  (if (= 1 2)
    "foo"
    "bar")
  => "bar")

(fact "`when` is an `if` with no `else` expression"
  (when (= 1 1)
    "foo")
  => "foo"

  (when (= 1 2)
    "foo")
  => nil)

(fact "`cond` is like an if / else if / else"
  (defn test-cond
    [x]
    (cond
     (neg? x)  :neg
     (pos? x)  :pos
     ;; Note that :else is just a keyword which happens to be logically true
     ;; we could easily write `true :zero`
     :else     :zero))
  => #'test-cond

  (test-cond -1)    => :neg
  (test-cond 1)     => :pos
  (test-cond 0)     => :zero

  (defn fizz-buzz
    [n]
    (let [multiple-of-3 (zero? (mod n 3))
          multiple-of-5 (zero? (mod n 5))]
      (cond
       (and multiple-of-3 multiple-of-5) "fizzbuzz"
       multiple-of-3                     "fizz"
       multiple-of-5                     "buzz"
       :else                             n)))
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
