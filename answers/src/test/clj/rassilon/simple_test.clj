(ns rassilon.simple-test
  (:require [rassilon.core :refer :all]
            [midje.sweet :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Expressions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(facts "Literals evaluate to themselves"
  1      => 1
  1/2    => 1/2
  true   => true
  false  => false
  "foo"  => "foo"
  :foo   => :foo
  'foo   => 'foo)

(facts "Keywords and symbols can also be constructed at runtime"
  (symbol "foo")           => 'foo
  (symbol "foo" "bar")     => 'foo/bar
  (keyword "foo")          => :foo
  (keyword "foo" "bar")    => :foo/bar)

(fact "`str` is the equivalent of StringBuilder / `.toString`"
  (str "f" "o" "o")        => "foo"
  (str "foo" "bar")        => "foobar")

(fact "`type` returns `:type` metadata of `x`, or class of `x` if none."
  (type 1)                 => java.lang.Long
  (type 1.5)               => java.lang.Double
  (type "foo")             => java.lang.String
  (type 1/2)               => clojure.lang.Ratio
  (type true)              => java.lang.Boolean
  (type [:foo])            => clojure.lang.PersistentVector
  (type #{:foo})           => clojure.lang.PersistentHashSet
  (type '(:foo))           => clojure.lang.PersistentList
  (type {:foo 1})          => clojure.lang.PersistentArrayMap)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Data Structure literals
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Core data structures have corresponding literals"
  (fact "vector uses [v1 v2 v3]"
    [1 2 3]
    => (vector 1 2 3))
 
  (fact "list uses '(v1 v2 v3) - note the quote!"
    '(1 2 3)
    => (list 1 2 3))

  (fact "set uses #{v1 v2 v3}"
    #{1 2 3}
    => (hash-set 1 2 3))

  (fact "map uses {k1 v1 k2 v2 k3 v3}"
    {:a 1 :b 2 :c 3}
    => (hash-map :a 1 :b 2 :c 3)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Working with data structures
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def L '(:a :b :c :d :e))

(def V [:a :b :c :d :e])

(def S #{:a :b :c :d :e})

(def M {:one 1
        :two 2
        :three 3
        :four 4
        :five 5})

(facts "`conj` adds items to collections"
  (fact "Lists `conj` at the front"
    (conj L :foo)
    => '(:foo :a :b :c :d :e))
  
  (fact "Vectors `conj` at the end"
    (conj V :foo)
    => [:a :b :c :d :e :foo])
  
  (fact "Sets are unordered"
    (conj S :foo)
    => #{:foo :a :b :c :d :e})

  (fact "Adding a duplicate item to a set is a no-op"
    (conj S :a)
    => #{:a :b :c :d :e})

  (fact "You can treat a map as a collection of entries, where each entry is a
`[key value]` vector"
    (conj M [:six 6])
    => {:one 1 :two 2 :three 3 :four 4 :five 5 :six 6}))

(facts "lists and vectors can be used like stacks"
  (fact "lists `peek` and `pop` at the front"
    (peek L) => :a

    (pop L) => '(:b :c :d :e))

  (fact "vectors `peek` and `pop` at the end"
    (peek V) => :e
    
    (pop V)  => [:a :b :c :d]))

(fact "`nth` gets items by index, in O(log32n) time for vectors,
and O(n) time for sequences."
  (nth L 1) => :b

  (nth V 3) => :d)

(fact "First gets the first item in a sequential thing, in constant time"
  (first L)
  => :a

  (first V)
  => :a)

(fact "You can get the first entry from a map, but since `M` is an unsorted map,
we don't know what the 'first' entry will be"
  (count (first M))
  => 2)

(fact "Rest returns a (possibly empty) seq of items except the first.
Note that the returned value is an `ISeq`, rather than the input collection
type."
  (rest L)
  => '(:b :c :d :e)

  (list? (rest V))
  => false

  (seq? (rest V))
  => true

  (rest V)
  => '(:b :c :d :e)

  (vector? (rest V))
  => false

  (seq? (rest V))
  => true)

(fact "Count is constant time for lists, vectors and sets"
  (count L)
  => 5

  (count V)
  => 5

  (count S)
  => 5)

(fact "Vectors support constant time slicing."
  (subvec V 1 4)
  => [:b :c :d]

  (subvec V 1)
  => [:b :c :d :e])

(facts "Vectors and maps are both 'associative' collections"
  (fact "For vectors, the key must be an integer"
    (assoc V 1 :foo)
    => [:a :foo :c :d :e]

    (get V 1)
    => :b

    (get V 100)
    => nil

    (get V 100 :my-default-value)
    => :my-default-value)

  (fact "Modiyfing indexes outside the range of the vector will fail"
    (assoc V 100 :foo)
    => (throws IndexOutOfBoundsException))

  (fact "For maps the key can be any (immutable) value"
    (assoc M :six 6)
    => {:one 1 :two 2 :three 3 :four 4 :five 5 :six 6}
    
    (assoc M :one 10)
    => {:one 10 :two 2 :three 3 :four 4 :five 5}
    
    (assoc M 1 1)
    => {1 1 :one 1 :two 2 :three 3 :four 4 :five 5}
    
    (assoc M "foo" "bar")
    => {"foo" "bar" :one 1 :two 2 :three 3 :four 4 :five 5}
    
    (get M :one)
    => 1
    
    (get M :foo)
    => nil

    (get M :foo :my-default-value)
    => :my-default-value))

(fact "Keys can be removed from maps using dissoc"
  (dissoc M :one)
  => {:two 2 :three 3 :four 4 :five 5}

  (dissoc M :one :two :foo)
  => {:three 3 :four 4 :five 5})

(fact "Vectors, maps and sets are also functions of their keys, although there
are some annoying minor differences"
  (M :one)
  => 1

  (M :foo)
  => nil

  ;; Somewhat annoyingly, this two arg version only works for maps
  (M :foo :my-default)
  => :my-default

  (V 0)
  => :a

  ;; Also annoying - the behaviour for vectors differs from `get`
  (V 100)
  => (throws IndexOutOfBoundsException)

  (S :a)
  => :a

  (S :foo)
  => nil)
