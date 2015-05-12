(ns puppy-service.core
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent.session :as session]
            [reagent-forms.core :refer [bind-fields]]
            [ajax.core :refer [GET POST]])
  (:require-macros [secretary.core :refer [defroute]]))

(defn fetch-puppies! [puppies]
  (GET "/api/puppy-links"
       {:handler #(reset! puppies (vec (partition-all 6 %)))}))


(defn puppies [puppy-links]
  [:div.text-center
   (for [row (partition-all 3 puppy-links)]
     ^{:key row}
     [:div.row
      (for [puppy-link row]
        ^{:key puppy-link}
        [:div.col-sm-4 [:img {:src puppy-link}]])])])


(defn forward [i pages]
  (if (< i (dec pages)) (inc i) i))

(defn back [i pages]
  (if (pos? i) (dec i) i))

(defn nav-link [page i]
  [:li {:on-click #(reset! page i)
        :class (when (= i @page) "active")}
   [:span i]])

(defn pager [pages page]
  [:div.row.text-center
   [:div.col-sm-12
    [:ul.pagination.pagination-lg
     (concat
      [[:li
        {:on-click #(swap! page back pages)
         :class (when (= @page 0) "disabled")}
        [:span "«"]]]
      (map (partial nav-link page) (range pages))
      [[:li
        {:on-click #(swap! page forward pages)
         :class (when (= @page (dec pages)) "disabled")}
        [:span "»"]]])]]])

;;todo make a list example
(defn home-page []
  (let [page (atom 0)
        puppy-links (atom nil)]
    (fetch-puppies! puppy-links)
    (fn []
      (if (not-empty @puppy-links)
        [:div
         [pager (count @puppy-links) page]
         [puppies (@puppy-links @page)]]
        [:div "Standby for puppies!"]))))

(defn mount-components []
  (reagent/render-component [home-page] (.getElementById js/document "app")))

(defn init! []
  (session/put! :page :home)
  (mount-components))


