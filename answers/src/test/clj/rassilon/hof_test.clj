(ns rassilon.hof-test
  (:require [midje.sweet :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Higher Order Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "`map` transforms sequences using a function"
  (map inc [1 2 3 4 5])
  => '(2 3 4 5 6)

  (map #(* 2 %) [1 2 3 4 5])
  => '(2 4 6 8 10))

(fact "`filter` removes items from a sequence using a predicate"
  (filter even? [1 2 3 4 5 6])
  => '(2 4 6)

  (filter #(zero? (mod % 2)) [1 2 3 4 5 6])
  => '(2 4 6))

(fact "`remove` is the complement of `filter`"
  (remove even? [1 2 3 4 5 6])
  => '(1 3 5)

  (remove #(zero? (mod % 2)) [1 2 3 4 5 6])
  => '(1 3 5))

(fact "`take` and `drop` take and drop the first `n` items from a sequence,
respectively"
  (take 3 [1 2 3 4 5])
  => '(1 2 3)

  (drop 3 [1 2 3 4 5])
  => '(4 5))

(fact "`take-while` and `drop-while` take and drop items from a sequence based
on a predicate"
  (take-while #(< % 4) [1 2 3 4 5])
  => '(1 2 3)

  (drop-while #(< % 3) [1 2 3 4 5])
  => '(3 4 5))

(fact "reduce combines (reduces) a sequence of items to a single value using
a function, and an optional starting value"
  (reduce + [1 2 3 4 5])
  => 15

  (reduce + 10 [1 2 3 4 5])
  => 25)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Detour - Threading operators
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "`->` (thread first) is useful for performing multiple operations on the
'same' object"
  (-> []
      (conj :a)
      (conj :b)
      (conj :c))
  => (conj (conj (conj [] :a)
                 :b)
           :c)

  (-> []
      (conj :a)
      (conj :b)
      (conj :c))
  => [:a :b :c])

(fact "`->>` (thread last) is useful for performing multiple operations on each
item in a sequence of objects"

  (->> [1 2 3 4 5]
       (map inc)
       (filter even?))
  => (filter even?
             (map inc
                  [1 2 3 4 5]))

  (->> [1 2 3 4 5]
       (map inc)
       (filter even?))
  => [2 4 6])

(fact "`as->` (arrow) is useful for threading a sequence of operations in an
arbitrary way"

  (as-> [1 2 3 4 5] x
        (map inc x)
        (conj x 1)
        (filter odd? x))
  => (filter odd?
             (conj (map inc
                        [1 2 3 4 5])
                   1))

  (as-> [1 2 3 4 5] x
        (map inc x)
        (conj x 1)
        (filter odd? x))
  => [1 3 5])

(fact "threading makes it easy to write linq-style sequence operations"
  (->> [1 2 3 4 5]
       (map inc)
       (filter even?)
       (reduce + 10))
  => 22)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Detour - `do`
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "`do` sequences expressions, returning the value of the last expression.
Expressions before the last are presumed to perform some sort of side effect,
since their return value is discarded."
  (do 1 2 3)
  => 3

  (do
    (+ 1 2)
    (+ 2 2)
    (+ 3 2))
  => 5)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; More Higher Order Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "partial is useful for supplying some arguments to a function"
  (let [my-inc (partial + 1)]
    (my-inc 2))
  => 3
  
  (let [hello (partial str "Hello, ")]
    (hello "world!"))
  => "Hello, world!")

(def NM {:a {:b {:c 1}}})

(def PEOPLE {:people {:dave  {:gender :male   :age 30}
                      :kerri {:gender :female :age 21}
                      :john  {:gender :male   :age 25}
                      :jane  {:gender :female :age 27}}})

(fact "`get-in` and `assoc-in` are useful for nested associative collections.
They each take a collection and a sequence of keys."
  (get-in PEOPLE [:people :dave :gender])
  => :male

  (get-in PEOPLE [:people :no-one :gender] :unknown)
  => :unknown

  (assoc-in NM [:a :b :d] 2)
  => {:a {:b {:c 1 :d 2}}}

  (-> PEOPLE
      (assoc-in [:people :rachael] {:gender :female :age 29})
      (get-in [:people :rachael :age]))
  => 29)

(fact "`update-in` can be used to 'update' (nested) associative collections.
It takes a sequence, a sequence of keys, a function to apply and optional
extra arguments, and invokes the function on the value at the location
specified by the keys, passing any optional arguments"
  (-> {:a 1}
      (update-in [:a] inc))
  => {:a 2}

  (-> PEOPLE
      (update-in [:people :dave :age] inc)
      (get-in [:people :dave]))
  => {:gender :male :age 31}

  (-> PEOPLE
      (update-in [:people :dave] assoc :likes #{:coffee})
      (get-in [:people :dave]))
  => {:gender :male :age 30 :likes #{:coffee}}

  (-> PEOPLE
      (update-in [:people :dave] dissoc :age)
      (get-in [:people :dave]))
  => {:gender :male})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Lazy seqs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Seqs are lazy by default."
  (->> (range 1000000000000000000)
       (map #(do (println %1) %1))
       (take 5))
  => '(0 1 2 3 4))

