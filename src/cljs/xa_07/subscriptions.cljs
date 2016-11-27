(ns xa-07.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
 :users
 (fn [db _]
   (:users db)))

(reg-sub
 :identity
 (fn [db _]
   (:identity db)))
