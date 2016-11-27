(ns xa-07.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [xa-07.core-test]))

(doo-tests 'xa-07.core-test)

