(ns rassilon.hof-test
  (:require [midje.sweet :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Higher Order Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(future-fact "`map` transforms sequences using a function"
  (map inc [1 2 3 4 5])
  => ?????

  (map ????? [1 2 3 4 5])
  => '(2 4 6 8 10))

(future-fact "`filter` removes items from a sequence using a predicate"
  (filter even? [1 2 3 4 5 6])
  => ?????

  (filter ????? [1 2 3 4 5 6])
  => '(3 6))

(future-fact "`remove` is the complement of `filter`"
  (remove even? [1 2 3 4 5 6])
  => ?????

  (remove ????? [1 2 3 4 5 6])
  => '(1 2 4 5))

(future-fact "`take` and `drop` take and drop the first `n` items from a sequence,
respectively"
  (take 3 [1 2 3 4 5])
  => ?????

  (drop ????? [1 2 3 4 5])
  => '(4 5))

(future-fact "`take-while` and `drop-while` take and drop items from a sequence based
on a predicate"
  (take-while #(< % 4) [1 2 3 4 5])
  => ????

  (drop-while ????? [1 2 3 4 5])
  => '(3 4 5))

(future-fact "reduce combines (reduces) a sequence of items to a single value using
a function, and an optional starting value"
  (reduce + [1 2 3 4 5])
  => ?????

  (reduce + ????? [1 2 3 4 5])
  => 25

  (reduce ????? [10 12 5 9 11])
  => 12)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Detour - Threading operators
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(future-fact "`->` (thread first) is useful for performing multiple operations on the
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
  => ?????)

(future-fact "`->>` (thread last) is useful for performing multiple operations on each
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
  => ?????)

(future-fact "`as->` (arrow) is useful for threading a sequence of operations in an
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
  => ?????)

(future-fact "threading makes it easy to write linq-style sequence operations"
  (->> [1 2 3 4 5]
       (map inc)
       (filter even?)
       (reduce + 10))
  => ?????)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Detour - `do`
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(future-fact "`do` sequences expressions, returning the value of the last expression.
Expressions before the last are presumed to perform some sort of side effect,
since their return value is discarded."
  (do 1 2 3)
  => 3

  (do
    (+ 1 2)
    (+ 2 2)
    (+ 3 2))
  => ?????)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; More Higher Order Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(future-fact "partial is useful for supplying some arguments to a function"
  (let [my-inc (partial + 1)]
    (my-inc 2))
  => ?????
  
  (let [hello (fn [s] ?????)]
    (hello "world!"))
  => "Hello, world!")

(def NM {:a {:b {:c 1}}})

(def PEOPLE {:people {:dave  {:gender :male   :age 30}
                      :kerri {:gender :female :age 21}
                      :john  {:gender :male   :age 25}
                      :jane  {:gender :female :age 27}}})

(future-fact "`get-in` and `assoc-in` are useful for nested associative collections.
They each take a collection and a sequence of keys."
  (get-in PEOPLE [:people :dave :gender])
  => ?????

  (get-in PEOPLE [:people :no-one :gender] ?????)
  => :unknown

  (assoc-in NM [:a :b :d] ?????)
  => {:a {:b {:c 1 :d 2}}}

  (-> PEOPLE
      (assoc-in [:people :rachael] {:gender :female :age 29})
      (get-in [:people :rachael :age]))
  => ?????)

(future-fact "`update-in` can be used to 'update' (nested) associative collections.
It takes a sequence, a sequence of keys, a function to apply and optional
extra arguments, and invokes the function on the value at the location
specified by the keys, passing any optional arguments"
  (-> {:a 1}
      (update-in [:a] inc))
  => ?????

  (-> PEOPLE
      (update-in [:people :dave :age] inc)
      (get-in [:people :dave]))
  => {:gender :male :age ?????}

  (-> PEOPLE
      (update-in [:people :dave] assoc :likes #{:coffee})
      (get-in [:people :dave]))
  => ?????

  (-> PEOPLE
      (update-in [:people :dave] dissoc :age)
      (get-in [:people :dave]))
  => ?????)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Lazy seqs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(future-fact "Seqs are lazy by default."
  (->> (range 1000000000000000000)
       (map #(do (println %1) %1))
       (take 5))
  => ?????)

