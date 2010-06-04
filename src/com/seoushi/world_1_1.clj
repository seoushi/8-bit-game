
(ns com.seoushi.world-1-1
  ;(:require )
  (:use com.seoushi.sprite)
  ;(:import )
  )

(def world-1-1
  [[0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   [3 3 3 3 3 3 1 0 2 3 3 3 3 3 3 3 3 3 3 3]
   [4 4 4 4 4 4 5 0 6 4 4 4 4 4 4 4 4 4 4 4]
   ])

(defn world-draw-row [graphics y tiles columns]
  "draws a row of tiles for a world map"
  (loop [x 0
         cols columns]
    (if (empty? cols)
      nil
      (do
        (draw-sprite graphics
          (nth tiles (first cols))
          x y)
        (recur (+ x 32) (rest cols))))))     ;;32 is the width of a tile, perhaps this should be a contant


(defn world-draw [graphics tiles map]
  "draws a world map"
  (loop [y 0
         rows map]
    (if (empty? rows)
      nil
      (do
        (world-draw-row graphics y tiles (first rows))
        (recur (+ y 32) (rest rows))))))    ;;32 is the height of a tile, perhaps this should be a contant


