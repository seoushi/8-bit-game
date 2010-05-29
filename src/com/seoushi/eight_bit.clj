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
    com.seoushi.sprite
    com.seoushi.time
    com.seoushi.animation)
  (:gen-class))


(def SCREEN-WIDTH 640)
(def SCREEN-HEIGHT 480)

(def MOVEMENT-PER-SEC 100) ;; in pixels



;; varibles
(def RUNNING (ref ()))
(def START-TIME (get-time))



;; callback events
(defn window-closing [frame]
  (.dispose frame)
  (println "window closing"))

(defn handle-keypress [event]
  (println "key was pressed"))

(defn handle-mouse [event]
  (println "mouse was pressed"))

(defn window-closing [frame]
  (dosync (ref-set RUNNING false))
  (.dispose frame)
  (println "window closing"))



;; intialization
(defn window-created [window]
  (let [image (load-image "resources/images/player.png")
        sprites (make-sprite-set image 32 64 7)]
    (dosync (ref-set RUNNING true))
    (update-time)
    (conj window {:sprites sprites
                  :walk-anim (anim sprites 0 5 200)})))


;; main game loop
(defn game-loop [window]
  (let [graphics (:graphics window)
        sprites (:sprites window)
        walk-anim (:walk-anim window)]
    (loop [position 0]
      (let [delta-time (get-delta-time @LAST-FRAME-TIME)]
        (update-time)
        (if @RUNNING
          (do
            (.setColor graphics (Color. 0 0 0))
            (.fillRect graphics 0 0 SCREEN-WIDTH SCREEN-HEIGHT)
            (draw-sprite graphics 
              (anim-get-frame
                walk-anim 
                START-TIME)
              position
              100)
            (.show (:buffer window))

            (recur (+ position (* delta-time MOVEMENT-PER-SEC)))))))))


(defn -main []
  (make-window SCREEN-WIDTH
    SCREEN-HEIGHT
    "8-Bit Game"
    handle-mouse
    handle-keypress
    window-created
    window-closing
    game-loop))

