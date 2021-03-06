;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; a sprite is a subsection of an image
;;

(ns com.seoushi.sprite
  (:require [clojure.contrib.math :as math])
  )


;; creates a sprite
(defn make-sprite [image x1 y1 x2 y2]
  {:image image
   :x1 x1
   :y1 y1
   :x2 x2
   :y2 y2})


;; gets the sprites width
(defn sprite-width [sprite]
  (math/abs (- (:x2 sprite) (:x1 sprite))))


;; gets the sprites height
(defn sprite-height [sprite]
  (math/abs (- (:y2 sprite) (:y1 sprite))))


;; draws a sprite at a given position
(defn draw-sprite [graphics sprite x y]
  (.drawImage graphics
    (:image sprite)
    x y (+ x (sprite-width sprite)) (+ y (sprite-height sprite))
    (:x1 sprite) (:y1 sprite) (:x2 sprite) (:y2 sprite)
    nil))

;; flips the x coordinates of the sprite
(defn sprite-flip-x [sprite]
  (assoc sprite
    :x1 (:x2 sprite)
    :x2 (:x1 sprite)))

;; makes an array of sprites from an image. recurses through the image from top to bottom
(defn make-sprite-set [image width height max]
  (let [max-cols (math/floor (/ (.getWidth image) width))
        max-rows (math/floor (/ (.getHeight image) height))]
               (loop [col 0
                      row 0
                      num-left max
                      sprites []]
                 (if (>= col max-cols)
                   (recur 0 (+ row 1) num-left sprites)
                   (if (>= row max-rows)
                     sprites
                     (recur (inc col)
                       row
                       (dec num-left)
                       (conj sprites (make-sprite image
                                       (* col width)
                                       (* row height)
                                       (* (inc col) width)
                                       (* (inc row) height)))))))))