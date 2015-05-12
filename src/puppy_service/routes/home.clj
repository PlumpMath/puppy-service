(ns puppy-service.routes.home
  (:require [puppy-service.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defroutes home-routes
  (GET "/" [] (home-page))
  )
