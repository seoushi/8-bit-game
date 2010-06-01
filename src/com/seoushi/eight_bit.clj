;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; A game made for a coding competition over at gpwiki.org
;;


(ns com.seoushi.eight-bit
  (:import (java.awt Color Image)
    (java.awt.event KeyEvent)
    (java.io File)
    (javax.imageio ImageIO))
  (:use com.seoushi.window
    com.seoushi.image
    com.seoushi.sprite
    com.seoushi.time
    com.seoushi.animation
    com.seoushi.player)
  (:gen-class))


(def SCREEN-WIDTH 640)
(def SCREEN-HEIGHT 480)

(def MOVEMENT-PER-SEC 100) ;; in pixels



;; varibles
(def RUNNING (ref ()))
(def START-TIME (get-time))
(def PLAYER (ref ()))





;; callback events
(defn window-closing [frame]
  (.dispose frame)
  (println "window closing"))

(defn handle-keypress [event type]
  (let  [key (.getKeyCode event)
         was-left-key (= key (. KeyEvent VK_LEFT))
         was-right-key (= key (. KeyEvent VK_RIGHT))
         move-player #(dosync (ref-set PLAYER (player-move @PLAYER %)))
         moving (:moving @PLAYER)]
    (if (= type :pressed)
      (cond
        was-right-key (move-player :right)
        was-left-key (move-player :left)))
    (if (and (= type :released)
          (or (and (= moving :left) was-left-key)
            (and (= moving :right) was-right-key)))
      (move-player :none))))



(defn handle-mouse [event]
  (println "mouse was pressed"))

(defn window-closing [frame]
  (dosync (ref-set RUNNING false))
  (.dispose frame)
  (println "window closing"))



;; intialization
(defn window-created [window]
  (let [image (load-image "resources/images/player.png")
        sprites (make-sprite-set image 32 64 7)
        idle-anim (anim-make sprites 0 0 200)
        walk-anim (anim-make sprites 0 5 200)]
    (dosync (ref-set RUNNING true))
    (update-time)
    (dosync (ref-set PLAYER 
              (player-make :none
                100
                0
                100
                idle-anim
                (get-time)
                {:walk-left walk-anim
                 :walk-right walk-anim
                 :idle idle-anim})))
    window))


;; main game loop
(defn game-loop [window]
  (let [graphics (:graphics window)
        sprites (:sprites window)
        walk-anim (:walk-anim window)]
    (loop []
      (let [delta-time (get-delta-time @LAST-FRAME-TIME)]
        (update-time)
        (if @RUNNING
          (do
            (.setColor graphics (Color. 0 0 0))
            (.fillRect graphics 0 0 SCREEN-WIDTH SCREEN-HEIGHT)
            (dosync (ref-set PLAYER
                      (player-update-and-draw @PLAYER delta-time graphics)))
            (.show (:buffer window))
            (recur)))))))


(defn -main []
  (make-window SCREEN-WIDTH
    SCREEN-HEIGHT
    "8-Bit Game"
    handle-mouse
    handle-keypress
    window-created
    window-closing
    game-loop))

