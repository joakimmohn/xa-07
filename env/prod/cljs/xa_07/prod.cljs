(ns xa-07.app
  (:require [xa-07.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
