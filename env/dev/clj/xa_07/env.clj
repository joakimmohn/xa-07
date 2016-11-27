(ns xa-07.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [xa-07.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[xa-07 started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[xa-07 has shut down successfully]=-"))
   :middleware wrap-dev})
