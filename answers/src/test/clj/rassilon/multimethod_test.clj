(ns rassilon.multimethod-test
  (:require [midje.sweet :refer :all]))

(defn asteroid
  [n]
  {:type ::asteroid :name n})

(defn ship
  [n]
  {:type ::ship :name n})

(defn planet
  [n]
  {:type ::planet :name n})

(defn moon
  [n]
  {:type ::moon :name n})

(defmulti collide
  (fn [a b] [(:type a) (:type b)]))

(defn- collide-asteroid-moon
  [asteroid moon]
  moon)

(defn- collide-asteroid-planet
  [asteroid planet]
  planet)

(defn- collide-asteroid-ship
  [asteroid ship]
  asteroid)

(defn- collide-moon-ship
  [moon ship]
  moon)

(defn- collide-moon-planet
  [moon planet]
  planet)

(defn- collide-planet-ship
  [planet ship]
  planet)

(defmethod collide [::asteroid ::asteroid]
  [a1 a2]
  nil)

(defmethod collide [::asteroid ::planet]
  [asteroid planet]
  (collide-asteroid-planet asteroid planet))

(defmethod collide [::planet ::asteroid]
  [planet asteroid]
  (collide-asteroid-planet asteroid planet))

(defmethod collide [::asteroid ::ship]
  [asteroid ship]
  (collide-asteroid-ship asteroid ship))

(defmethod collide [::ship ::asteroid]
  [ship asteroid]
  (collide-asteroid-ship asteroid ship))

(defmethod collide [::asteroid ::moon]
  [asteroid moon]
  (collide-asteroid-moon asteroid moon))

(defmethod collide [::moon ::asteroid]
  [moon asteroid]
  (collide-asteroid-moon asteroid ship))

(defmethod collide [::moon ::moon]
  [m1 m2]
  nil)

(defmethod collide [::moon ::ship]
  [moon ship]
  (collide-moon-ship moon ship))

(defmethod collide [::ship ::moon]
  [ship moon]
  (collide-moon-ship moon ship))

(defmethod collide [::moon ::planet]
  [moon planet]
  (collide-moon-planet moon planet))

(defmethod collide [::planet ::moon]
  [planet moon]
  (collide-moon-planet moon planet))

(defmethod collide [::planet ::ship]
  [planet ship]
  (collide-planet-ship planet ship))

(defmethod collide [::ship ::planet]
  [ship planet]
  (collide-planet-ship planet ship))

(defmethod collide [::planet ::planet]
  [p1 p2]
  nil)

(defmethod collide [::ship ::ship]
  [s1 s2]
  nil)

(fact "multimethods allow arbitrary dispatch"
  (let [earth       (planet :earth)
        mars        (planet :mars)
        ganymede    (moon :ganymede)
        voyager     (ship :voyager)
        meteor      (asteroid :a1)]
    (collide earth ganymede)
    => earth

    (collide earth mars)
    => nil

    (collide voyager ganymede)
    => ganymede))
