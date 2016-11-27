(ns xa-07.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [reagent.core :as reagent :refer [atom]]
            [xa-07.core :as rc]))

(deftest test-home
  (is (= true true)))

