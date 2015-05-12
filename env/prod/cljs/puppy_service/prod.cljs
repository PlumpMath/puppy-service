(ns puppy-service.app
  (:require [puppy-service.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
