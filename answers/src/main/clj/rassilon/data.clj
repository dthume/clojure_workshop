(ns rassilon.data
  (:require [clojure.core.reducers :as r]))

(defn create-universe
  []
  [{:char/id           :char/doctor
    :char/is-from      :planet/gallifrey
    :char/species      :species/timelord}

   {:char/id           :char/davros
    :char/is-from      :planet/skaro
    :char/species      :species/kaled
    :char/relationship :relationship/enemy}

   {:char/id           :char/rose-tyler
    :char/is-from      :planet/earth
    :char/species      :species/human
    :char/relationship :relationship/companion}])


