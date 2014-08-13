(ns rassilon.app
  (:require [com.stuartsierra.component :as component]))

(defn dev-application
  [conf]
  (let [{:keys [port
                trace-web-requests?]
         :or {port 8080
              trace-web-requests? false}} conf]
    (component/system-map
     :todo {})))


