;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; Functions for loading and drawing images
;;

(ns com.seoushi.image
  (:import (java.awt Image)
    (java.io File)
    (javax.imageio ImageIO)))

;; loads an image
(defn load-image [filename]
  (. ImageIO read (File. filename)))

;; draws the whole image to a location on the screen
(defn draw-image [graphics image x y]
  (.drawImage graphics image x y nil))

;; draws part of image to the screen
(defn draw-image-part [graphics image src-x src-y dst-x dst-y width height]
  (.drawImage graphics
    image
    dst-x dst-y (+ dst-x width) (+ dst-y height)
    src-x src-y (+ src-x width) (+ src-y height)
    nil))