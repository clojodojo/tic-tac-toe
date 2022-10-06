(ns demo.ui
  (:require
   [reagent.core :as r]))

(defonce state (r/atom {:game-board [[:x :o :x] [:o nil nil] [nil nil nil]]
                        :current-player :x}))

;; "how can i represent any moment of my app as a dictionary"
(defn flip0 [player]
  (if (= player :x)
    :o
    :x))

(defn flip1 [player]
  (case player
    :x :o
    :o :x))

(def flip
  {:x :o
   :o :x})

(defn app-view []
  [:div
   [:table
    [:tbody
      (for [[row-index row] (map-indexed vector (:game-board @state))]
        [:tr
         (for [[col-index item] (map-indexed vector row)]
          [:td {:on-click (fn []
                            (swap! state assoc-in [:game-board row-index col-index] (:current-player @state))
                            (swap! state update :current-player flip))}
           (case item
                  :x "❌"
                  :o "⭕"
                  "⬜️")])])]]])

(def test-game-state
  {:game-board   [[\x \o \x] [\o nil nil] [nil nil nil]]
   :move-history [[[0 0] \x] [[0 1] \o] [[0 2] \x] [[1 0] \o]]
   :current-move-index 3})
