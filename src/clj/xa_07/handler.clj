(ns xa-07.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [xa-07.layout :refer [error-page]]
            [xa-07.routes.home :refer [home-routes]]
            [xa-07.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [xa-07.env :refer [defaults]]
            [mount.core :as mount]
            [xa-07.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    #'service-routes
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
