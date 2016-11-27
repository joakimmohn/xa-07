(ns xa-07.routes.services
  (:require [compojure.api.sweet :refer :all]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.accessrules :refer [restrict]]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [buddy.hashers :as hashers]
            [xa-07.db.core :as db]
            [url62.core :refer [generate-id]]))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

(def service-routes
  (api
   (context "/api" []
    (POST "/login" []
          :body-params [email :- s/Str
                        password :- s/Str]
          (let [user (db/get-user {:email email})]
           (if (hashers/check password (user :pass))
            (assoc-in (ok) [:session :identity] {:id (user :id)})
            (unauthorized))))
    (POST "/logout" [:as req]
          (assoc (ok) :session nil))
    (POST "/users" [:as req]
          :body-params [email :- s/Str
                        full-name :- s/Str
                        password :- s/Str]
          (db/create-user! {:id (generate-id)
                            :email email
                            :full-name full-name
                            :pass (hashers/encrypt password)})
          (ok))
    (PUT "/users" [:as req]
         :auth-rules authenticated?
         :body-params [id :- s/Str
                       xcoordinate :- s/Int
                       ycoordinate :- s/Int
                       description :- s/Str
                       rate :- s/Int
                       active :- s/Bool]
         (db/update-user! {:id id
                           :xcoordinate xcoordinate
                           :ycoordinate ycoordinate
                           :description description
                           :rate rate
                           :active active})
         (ok))
    (GET "/users" []
         (let [users (db/get-users)]
          (ok users)))
    (GET "/users/:id" [id]
          (ok id))
    (GET "/auth" []
         :auth-rules authenticated?
         :current-user user
         (ok {:user user})))))
