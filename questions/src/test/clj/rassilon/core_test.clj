(ns rassilon.core-test
  (:require [rassilon.core :refer :all]
            [midje.sweet :refer :all]))

(fact "Simple fact examples"
  (+ 1 1)               => 2

  (* 2 2)               => 4

  (/ 2 2)               => 1

  (conj [1] 2)          => [1 2]

  (conj '(2) 1)         => [1 2])

