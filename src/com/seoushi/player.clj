
(ns com.seoushi.player
  ;(:require )
  (:use com.seoushi.time
    com.seoushi.sprite
    com.seoushi.animation)
  ;(:import )
  )

;; definition of a player
(defstruct player :direction :speed :x :y :anim :anim-start :anims)


;; starts moving a player in a direction
(defn player-move [player direction]
  (if (= direction (:direction player))
    player
    (let [anims (:anims player)]
      (assoc player
        :direction direction
        :anim (cond
                (= direction :left) (:walk-left anims)
                (= direction :right) (:walk-right anims)
                :else (:idle anims))
        :anim-start (:anim-start player))))) ;; this really should be (get-time) but java canvas does not handle key repeats correctly



;; should be called once a frame to update the players position
(defn player-update [player delta-time]
  (assoc player
    :x (let [dir (:direction player)
             x (:x player)
             distance (* (:speed player) delta-time)]
         (cond
           (= dir :left) (- x distance)
           (= dir :right) (+ x distance)
           :else (:x player)))))


;; draws the player on the screen
(defn player-draw [player graphics]
  (draw-sprite graphics
    (anim-get-frame
      (:anim player)
      (:anim-start player))
    (:x player)
    (:y player))
  player)


;;updates and draws the player
(defn player-update-and-draw [player delta-time graphics]
  (player-draw
    (player-update player delta-time)
    graphics))