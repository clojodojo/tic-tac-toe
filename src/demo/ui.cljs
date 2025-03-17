(ns demo.ui
  (:require
   [reagent.core :as r]))

;; "how can i represent any moment of my app as a dictionary"

(defonce state (r/atom {:game-board [[:x :o :x] [:o nil nil] [nil nil nil]]
                        :current-player :x}))

(defn reset-state! []
  (reset! state {:game-board [[nil nil nil] [nil nil nil] [nil nil nil]]
                 :current-player :x}))

(def flip
  {:x :o
   :o :x})

;; alternative implementations of flip:

(comment
  (defn flip-v0 [player]
    (if (= player :x)
      :o
      :x))

  (defn flip-v1 [player]
    (case player
      :x :o
      :o :x)))

(defn generate-win-coordinates []
  ;; TODO horizontal and vertical columns
  (let [n 3]
    [(for [x (range n)
           y (range n)
           :when (= (dec n) (+ y x))]
       [x y])
     (for [x (range n)
           y (range n)
           :when (= x y)]
       [x y])]))

#_(generate-coordinates)

(defn vals-at-locations 
  [board coordinates]
  (->> coordinates
       (map (fn [c] (get-in board c)))))

(defn check-win-given-coordinates 
  [board player coordinates]
  (when (->> (vals-at-locations board coordinates)
             (apply (partial = player)))
    coordinates))

(defn check-win
  [game-state player]
  (some
   (partial check-win-given-coordinates game-state player)
   (generate-win-coordinates)))

#_(check-win (:game-board @state) :x)

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
                  "⬜️")])])]]
   [:button {:on-click (fn [] (reset-state!))} "Reset"]
   [:div "Winner: " (cond 
                      (check-win (:game-board @state) :x) :x
                      (check-win (:game-board @state) :o) :o)]])

;; TODO, prevent update if cell already has value
