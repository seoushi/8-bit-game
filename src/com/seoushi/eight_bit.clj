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
    com.seoushi.player
    com.seoushi.constants
    com.seoushi.world-1-1)
  (:gen-class))


(def SCREEN-WIDTH 640)
(def SCREEN-HEIGHT 480)

(def MOVEMENT-PER-SEC 100) ;; in pixels



;; varibles
(def RUNNING (ref ()))
(def PLAYER (ref ()))





;; callback events
(defn window-closing [frame]
  (.dispose frame)
  (println "window closing"))

(defn handle-keyboard [event type]
  "handles the keyboard press and release"
  (let  [key        (.getKeyCode event)
         left-key?  (= key (. KeyEvent VK_LEFT))
         right-key? (= key (. KeyEvent VK_RIGHT))
         jump-key?  (= key (. KeyEvent VK_SPACE))
         perform    #(dosync (ref-set PLAYER
                               (player-perform @PLAYER %)))]
    (if (= type :pressed)
      (cond
        right-key?  (do (perform :face-right)
                      (perform :move))
        left-key?   (do (perform :face-left)
                      (perform :move))
        jump-key?   (perform :jump))
      ;; if released
      (cond
        right-key?  (perform :stop-right)
        left-key?   (perform :stop-left)))))


(defn handle-mouse [event]
  (println "mouse was pressed"))

(defn window-closing [frame]
  (dosync (ref-set RUNNING false))
  (.dispose frame)
  (println "window closing"))



;; intialization
(defn window-created [window]
  (let [player-image    (load-image "resources/images/player.png")
        sprites         (make-sprite-set player-image 32 48 8)
        idle-anim       (anim-make sprites 0 0 200)
        walk-anim       (anim-make sprites 1 6 200)
        jump-anim       (anim-make sprites 7 7 200)
        world-image     (load-image "resources/images/tileset.png")
        tiles           (make-sprite-set world-image 32 32 20)]
    (dosync (ref-set RUNNING true))
    (update-time)
    (dosync (ref-set PLAYER
              (struct player
                :right
                []
                (get-time)
                100
                0
                world-plane
                {:move  walk-anim
                 :jump  jump-anim
                 :idle  idle-anim})))
    (assoc window
      :tiles tiles)))


;; main game loop
(defn game-loop [window]
  (let [graphics    (:graphics window)
        sprites     (:sprites window)
        walk-anim   (:walk-anim window)]
    (loop []
      (let [delta-time (get-delta-time @LAST-FRAME-TIME)]
        (update-time)
        (if @RUNNING
          (do
            (.setColor graphics (Color. 0 0 0))
            (.fillRect graphics 0 0 SCREEN-WIDTH SCREEN-HEIGHT)
            (world-draw graphics (:tiles window) world-1-1)
            (dosync (ref-set PLAYER
                      (player-update-and-draw @PLAYER delta-time graphics)))
            (.show (:buffer window))
            (recur)))))))


(defn -main []
  (make-window SCREEN-WIDTH
    SCREEN-HEIGHT
    "8-Bit Game"
    handle-mouse
    handle-keyboard
    window-created
    window-closing
    game-loop))

