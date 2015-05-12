(ns puppy-service.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [cheshire.core :refer [parse-string]]
            [clj-http.client :as client]))

(defn parse-data [data]
  (let [posts (-> data
                  (clojure.string/replace #"var tumblr_api_read = " "")
                  (clojure.string/replace #";" "")
                  (parse-string true)
                  :posts)]
    (->> (filter #(= "photo" (:type %)) posts)
         (mapcat #(->> % :photos (map :photo-url-1280))))))

(defn fetch-puppies []
  (-> "http://puppygifs.tumblr.com/api/read/json" client/get :body parse-data))

(defapi service-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  (swagger-docs
    {:info {:title "Sample api"}})
  (context* "/api" []
            :tags ["tumbler"]

            (GET* "/puppy-links" []
                  :query-params []
                  :summary      "x+y with query-parameters. y defaults to 1."
                  :body [body [s/Str]]
                  (ok (fetch-puppies)))))
