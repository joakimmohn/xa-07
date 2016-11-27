(ns user
  (:require [mount.core :as mount]
            [xa-07.figwheel :refer [start-fw stop-fw cljs]]
            xa-07.core))

(defn start []
  (mount/start-without #'xa-07.core/repl-server))

(defn stop []
  (mount/stop-except #'xa-07.core/repl-server))

(defn restart []
  (stop)
  (start))


