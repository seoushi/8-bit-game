
(ns com.seoushi.player
  ;(:require )
  (:use com.seoushi.time
    com.seoushi.sprite
    com.seoushi.animation)
  ;(:import )
  )

;; makes a player with all relevant info
(defn player-make [moving speed x y anim anim-start anims]
  {:moving moving
   :speed speed
   :x x
   :y y
   :anim anim
   :anim-start anim-start
   :anims anims})

;; starts moving a player in a direction
(defn player-move [player direction]
  (if (= direction (:moving player))
    player
    (let [anims (:anims player)]
      (player-make
        direction
        (:speed player)
        (:x player)
        (:y player)
        (cond
          (= direction :left) (:walk-left anims)
          (= direction :right) (:walk-right anims)
          :else (:idle anims))
        (:anim-start player) ;; this really should be (get-time) but java canvas does not handle key repeats correctly
        anims))))


;; should be called once a frame to update the players position
(defn player-update [player delta-time]
  (player-make
    (:moving player)
    (:speed player)

    (let [moving (:moving player)
          x (:x player)
          speed (:speed player)
          distance (* speed delta-time)]
      (cond
        (= moving :left) (- x distance)
        (= moving :right) (+ x distance)
        :else (:x player)))
    (:y player)
    (:anim player)

    (:anim-start player)
    (:anims player)))


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