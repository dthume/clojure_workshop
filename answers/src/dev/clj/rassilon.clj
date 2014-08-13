(ns rassilon
  (:require [clojure.tools.namespace.repl :refer (refresh set-refresh-dirs)]
            [rassilon.app :as app]
            [com.stuartsierra.component :as component]))

(set-refresh-dirs "src/main/clj" "src/dev/clj")

;; workaround for http://dev.clojure.org/jira/browse/CLJ-1152

(defn- protocol? [x]
  (and (instance? clojure.lang.PersistentArrayMap x)
       (boolean (:on-interface x))))

(defn- multifn? [x]
  (instance? clojure.lang.MultiFn x))

(let [ca (make-array Class 0)
      oa (make-array Object 0)]
  (defn- reset-multifn-cache [mf]
    (let [c (class mf)
            rc (.getDeclaredMethod c "resetCache" ca)]
        (.setAccessible rc true)
        (.invoke rc mf oa))))

(defn- reset-clojure-caches
  ([] (reset-clojure-caches (all-ns)))
  ([namespaces]
     (doseq [namespace namespaces
             :let [n (if (symbol? namespace) (find-ns namespace) namespace)]
             ns-binding (vals (ns-map n))
             :when (var? ns-binding)
             :let [ns-var (var-get ns-binding)]]
       (cond
        (protocol? ns-var) (-reset-methods ns-var)
        (multifn? ns-var) (reset-multifn-cache ns-var)))))

;; ## System

(def system nil)

(defn init []
  (alter-var-root
   #'system
   (fn [& _]
     (app/dev-application {:port 8080
                           :trace-web-requests? true}))))

(defn start []
  (try
    (alter-var-root #'system component/start)
    (catch Exception e
      (when-let [sys (-> e ex-data :system)]
        (println "Exception while starting")
        (.printStackTrace e)
        (println "Attempting to cleanly shutdown components")
        (try
          (component/stop sys)
          (catch Exception e1
            (println "Exception while trying to cleanly shutdown system. You may need to restart the repl"))))))
  (println "Started"))

(defn stop []
  (alter-var-root #'system
    (fn [s] (when s (component/stop s))))
  (println "Stopped"))

(defn destroy []
  (alter-var-root #'system (constantly nil))
  (println "Destroyed"))

(defn run-app []
  (println "Starting...")
  (init)
  (start))

(defn reset []
  (stop)
  (destroy)
  (reset-clojure-caches)
  (refresh :after 'rassilon/run-app))

