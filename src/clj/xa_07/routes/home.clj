(ns xa-07.routes.home
  (:require [xa-07.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/login" [] (home-page))
  (GET "/signup" [] (home-page))
  (GET "/start-sitting" [] (home-page)))
