(ns app.views
  (:require [app.perlin :as perlin]))

(defn populate-canvas []
  (let [ctx (.getContext (first (.getElementsByTagName js/document "canvas")) "2d")]
    (doall
      (for [y (range 100)]
        (doall
          (for [x (range 100)]
            (let [value (* 0.5 (+ 1 (perlin/do-octave x y 8 0.005 1)))
                  rgb (js/Math.round (* 255 value))]
              (cond
                (> 0.5 value) (set! (.-fillStyle ctx) (str "rgba(0.0,0.0,"(* 2 rgb)",1.0)"))
                (> 0.9 value) (set! (.-fillStyle ctx) (str "rgba(0.0," rgb ","(js/Math.round (* rgb 0.5)) ",1.0)"))
                :else (set! (.-fillStyle ctx) (str "rgba("rgb","rgb","rgb",1.0)")))
              (.fillRect ctx x y 1 1))))))))
(defn app []
  [:div
   [:canvas {:width "500" :height "500" :style {:border "1px solid black"}}]
   (js/setTimeout #(populate-canvas) 1000)])
