;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; A game made for a coding competition over at gpwiki.org
;;


(ns com.seoushi.eight-bit
  (:import (java.awt Color Image)
    (java.io File)
    (javax.imageio ImageIO))
  (:use com.seoushi.window
    com.seoushi.image
    com.seoushi.sprite)
  (:gen-class))


(def SCREEN-WIDTH 640)
(def SCREEN-HEIGHT 480)






(defn get-time []
  (System/currentTimeMillis))


;; callback events
(defn window-closing [frame]
  (.dispose frame)
  (println "window closing"))

(defn handle-keypress [event]
  (println "key was pressed"))

(defn handle-mouse [event]
  (println "mouse was pressed"))

(defn window-closing [frame]
  (.dispose frame)
  (println "window closing"))


;; intialization
(defn window-created [window]
  (conj window {:player-tiles (load-image "resources/images/player.png")}))


;; main game loop
(defn game-loop [window]
  (.setColor (:graphics window) (Color. 0 0 0))
  (.fillRect (:graphics window) 0 0 SCREEN-WIDTH SCREEN-HEIGHT)
  (draw-image (:graphics window) (:player-tiles window) 100 50)
  (draw-image-part (:graphics window) 
    (:player-tiles window)
    0 0
    300 100
    32 64)
  (.show (:buffer window)))


(defn -main []
  (make-window SCREEN-WIDTH
    SCREEN-HEIGHT
    "8-Bit Game"
    handle-mouse
    handle-keypress
    window-created
    window-closing
    game-loop))

