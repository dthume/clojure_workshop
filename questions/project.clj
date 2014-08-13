(defproject org.dthume.clj.tutorials/rassilon "0.1.0-SNAPSHOT"
  :description "Tutorial Clojure App"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :main ^:skip-aot rassilon.core
  :target-path "target/%s"

  :source-paths ["src/main/clj"]
  :resource-paths ["src/main/resources"]
  :test-paths ["src/test/clj"]

  :plugins [[codox "0.8.9"]
            [lein-marginalia "0.7.1"]
            [lein-midje "3.0.0"]]

  :dependencies [[bidi "1.10.4"]
                 [buddy "0.2.0b1"]
                 [cheshire "5.3.1"]
                 [com.stuartsierra/component "0.2.1"]
                 [com.taoensso/timbre "3.2.1"]
                 [liberator "0.12.0"]
                 [org.clojure/clojure "1.7.0-alpha1"]
;                 [org.clojure/clojure "1.6.0"]
                 [ring/ring-core "1.3.0"]
                 [ring-middleware-format "0.3.2"
                  :exclusions [org.yaml/snakeyaml]]
                 [org.clojure/tools.namespace "0.2.5"]]

  :javac-options ["-target" "1.7" "-source" "1.7"]

  :repl-options {:timeout 60000}

  :codox {:defaults {:doc/format :markdown}
          :output-dir "target/docs/codox"}

  :profiles
  {:dev
   {:source-paths ["src/dev/clj"]
    :dependencies [[midje "1.6.3"]
                   [peridot "0.3.0"]]}
   
   :site {}

   :uberjar
   {:source-paths ["src/dist/clj"]
    :main rassilon.main
    :aot [rassilon.main]
    :global-vars {*assert* false
                  midje.sweet/*include-midje-checks* false}}}

  :aliases
  {"dev-doc"
   ^{:doc "Generate project documentation"}
   ["with-profile" "site"
    ["do"
     ["clean"]
     ["doc"]
     ["marg"
      "--dir" "target/docs/marg"
      "-m"]]]

   "dev-jar"
   ^{:doc "Create a standalone executable JAR file suitable for deployment"}
   ["do" ["clean"] ["midje"] ["clean"] ["uberjar"]]

   "dev-repl"
   ^{:doc "Start a clean development NREPL session"}
   ["do" ["clean"] ["repl"]]

   "dev-test"
   ^{:doc "Run development unit tests"}
   ["do" ["clean"] ["midje"]]})
