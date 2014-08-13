(ns user
  (:require [clojure.pprint :as pp]
            [clojure.repl :refer :all]))

(require 'rassilon)

(defn run-app []
  ((resolve (symbol "rassilon" "run-app"))))

(defn stop []
  ((resolve (symbol "rassilon" "stop"))))

(defn reset []
  ((resolve (symbol "rassilon" "reset"))))

(defn get-system []
  @(resolve (symbol "rassilon" "system")))
