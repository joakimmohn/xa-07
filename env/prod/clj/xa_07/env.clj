(ns xa-07.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[xa-07 started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[xa-07 has shut down successfully]=-"))
   :middleware identity})
