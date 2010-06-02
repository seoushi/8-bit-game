;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; defines a player
;;

(ns com.seoushi.player
  ;(:require )
  (:use com.seoushi.time
    com.seoushi.sprite
    com.seoushi.animation
    com.seoushi.point
    com.seoushi.actions
    com.seoushi.constants)
  ;(:import )
  )




;; definition of a player
(defstruct player :facing :actions :action-start :speed :x :y :anims)


(defn apply-gravity [player delta-time]
  "moves the player down while it isn't touching the ground, (fall)"
  (let [y               (:y player)
        distance        (* gravity delta-time)
        new-y           (+ y distance)
        fall            #(assoc player :y %)
        ground-plane    100]    ;; this should be changed to look at the map
    (if (>= new-y ground-plane)   ;; stop falling if you hit the ground
      (assoc player :y 100)
      (fall new-y))))




(defn player-perform [player action]
  "tell the player to start performing an action"
  (cond
    (= action :face-left)   (assoc player
                              :facing :left)
    (= action :face-right)  (assoc player
                              :facing :right)
    (= action :stop)        (action-remove player :move)
    :else                   (action-perform player action)))


(defn player-sprite [player]
  "Gets the sprite for the player"
  (let [facing      (:facing player)
        anims       (:anims player)
        actions     (:actions player)
        flip        #(if (= :left facing)
                       (sprite-flip-x %)
                       %)
        jump        (action-find player :jump)
        move        (action-find player :move)
        anim        (cond
                      jump  (:jump  anims)
                      move  (:move  anims)
                      :else (:idle  anims))
        start-time  (cond
                      jump  (:start-time jump)
                      move  (:start-time move)
                      :else (get-time))]
    (flip (anim-get-frame anim start-time))))



(defn player-update [player delta-time]
  "updates the player"
  (let [actions         (:actions player)
        updates         (conj (map #(:update %) actions) ;;makes a list of action update functions
                          apply-gravity)]
    (loop [result   player
           funcs    updates]
      (if (empty? funcs)
        result
        (recur
          ((first funcs) result delta-time)
          (rest funcs))))))
;; I should be able to do the previous function with some form of reduce

 

(defn player-draw [player graphics]
  "draw the player"
    (draw-sprite graphics
      (player-sprite player)
      (:x player)
      (:y player))
    player)


(defn player-update-and-draw [player delta-time graphics]
  "updates then draw the player"
  (player-draw
    (player-update player delta-time)
    graphics))