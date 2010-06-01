;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; defines a player and his actions
;;

(ns com.seoushi.player
  ;(:require )
  (:use com.seoushi.time
    com.seoushi.sprite
    com.seoushi.animation
    com.seoushi.point)
  ;(:import )
  )


;; definition of a player
(defstruct player :facing :action :action-start :speed :x :y :anims)




(defn action-move [player delta-time]
  "updates the horizontal position of a player (moves it)"
  (let [facing      (:facing player)
        x           (:x player)
        distance    (* (:speed player) delta-time)
        move        #(assoc player :x %)]
    (cond
      (= facing :left)  (move (- x distance))
      (= facing :right) (move (+ x distance))
      :else player)))


(defn action-jump [player delta-time]
  "updates the vertical position of a player (makes em jump)"
  (let [y           (:y player)
        distance    (* (/ (:speed player) 2) delta-time)
        jump        #(assoc player :y %)]
    (action-move    ;; while jumping you can be moving
      (jump y)      ;; TODO: this should actually change the y value, need to get in falling first tho
      delta-time)))


(defn action-idle [player delta-time]
  "defines the idle action, do nothing"
  player)



;; a map of possible actions and thier update function
(def player-actions
  {:walk action-move
   :jump action-jump
   :idle action-idle})



(defn player-perform [player action]
  "tell the player to start performing an action"
  (let [cur-action  (:action player)
        facing      (:facing player)]
    (cond
      (= action cur-action)         player  ;; don't start an action if it's currently running
      ;; note that the walk actions are different because they
      ;; change thier facing and need to be handles separately
      (= action :walk-left)       (if (and (= facing :left) (= cur-action :walk))
                                    player
                                    (assoc player
                                      :facing :left
                                      :action :walk
                                      :action-start (get-time)))
      (= action :walk-right)      (if (and (= facing :right) (= cur-action :walk))
                                    player
                                    (assoc player
                                      :facing :right
                                      :action :walk
                                      :action-start (get-time)))
      :else                       (assoc player
                                    :action action
                                    :action-start (get-time)))))


(defn player-sprite [player]
  "Gets the sprite for the player"
  (let [facing  (:facing player)
        anims   (:anims player)
        action  (:action player)
        anim    (action anims)
        sprite  (anim-get-frame
                  anim
                  (:action-start player))]
    (if (= :left facing)
      (sprite-flip-x sprite)
      sprite)))


(defn player-update [player delta-time]
  "updates the player"
  (let [action          (:action player)
        update-func     (action player-actions)]
    (update-func player delta-time)))
 

(defn player-draw [player graphics]
  "draw the player"
  (let [anims   (:anims player)
        idle    (:idle anims)
        sprite  (player-sprite player)]
    (draw-sprite graphics
      sprite
      (:x player)
      (:y player))
    player))
    


;;updates and draws the player
(defn player-update-and-draw [player delta-time graphics]
  (player-draw
    (player-update player delta-time)
    graphics))