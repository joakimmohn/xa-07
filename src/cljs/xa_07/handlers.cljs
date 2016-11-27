(ns xa-07.handlers
  (:require [xa-07.db :as db]
            [ajax.core :refer [PUT POST GET]]
            [re-frame.core :refer [dispatch reg-event-db]]))

(defn dispatch-multi [events]
  (doseq [event events]
    (dispatch event)))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :login
  (fn [db [_ data]]
    (POST "/api/login"
          {:params data
           :handler #(dispatch-multi [[:set-active-page :home][:ping-auth]])
           :error-handler #(js/console.log %)})
    db))

(reg-event-db
  :ping-auth
  (fn [db _]
    (GET "/api/auth"
         {:handler #(dispatch [:store-identity %])})
    db))

(reg-event-db
  :logout
  (fn [db _]
    (POST "/api/logout"
         {:handler #(dispatch [:remove-identity %])})
    db))

(reg-event-db
  :store-identity
  (fn [db [_ identity]]
    (assoc db :identity identity)))

(reg-event-db
  :remove-identity
  (fn [db [_ identity]]
    (dissoc db :identity)))

(reg-event-db
  :get-users
  (fn [db _]
    (GET "/api/users"
         {:handler #(dispatch [:store-users %])})
    db))

(reg-event-db
  :print-db
  (fn [db _]
   (js/console.log db)
   db))

(reg-event-db
  :store-users
  (fn [db [_ users]]
    (assoc db :users users)))

(reg-event-db
  :create-user
  (fn [db [_ {:keys [email full-name password]}]]
    (POST "/api/users"
          {:params {:email email
                    :password password
                    :full-name full-name}
           :handler #(dispatch [:set-active-page :home])
           :error-handler #(js/console.log %)})
    db))

(reg-event-db
  :update-user
  (fn [db [_ {:keys [id xcoordinate ycoordinate description rate active]}]]
    (PUT "/api/users"
         {:params {:id id
                   :xcoordinate xcoordinate
                   :ycoordinate ycoordinate
                   :description description
                   :rate rate
                   :active active}
          :handler #(js/console.log "success" %)
          :error-handler #(js/console.log %)})
   db))
