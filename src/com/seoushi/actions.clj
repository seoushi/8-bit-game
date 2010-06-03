;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; defines entity actions
;;

(ns com.seoushi.actions
  ;(:require)
  (:use com.seoushi.constants
    com.seoushi.time)
  ;(:import )
  )



;;definition of an action
(defstruct action :name :check :update :start-time)


;; helper functions
(defn action-find [entity action-name]
  "trys to find an action in the entity"
  (first (filter #(= action-name (:name %))
           (:actions entity))))


(defn action-add [entity action]
  "adds an action if it doesn't exist already and passes the action's requirements"
  (if (and (nil? (action-find entity (:name action)))
        ((:check action) entity))
    (assoc entity
      :actions (conj (:actions entity) action))
    entity))


(defn action-remove [entity action-name]
  "removed an actions if it exists"
  (assoc entity
        :actions (remove #(= action-name (:name %))
                   (:actions entity))))


;; move action
(defn move-update [entity delta-time]
  "updates the horizontal position of an entity (moves it)"
  (let [facing      (:facing entity)
        x           (:x entity)
        distance    (* (:speed entity) delta-time)
        move        #(assoc entity :x %)]
    (cond
      (= facing :left)  (move (- x distance))
      (= facing :right) (move (+ x distance))
      :else entity)))


(def action-move (struct action
                   :move
                   #(identity %) ;; always be able to start moving
                   move-update
                   0))



;; jump action

(defn jump-check [entity]
  "checks if a jump should start"
  (= 100 (:y entity))) ;; TODO: this 100 is the ground plane, this should check the map eventually

(defn jump-update [entity delta-time]
  "updates the vertical position of a player (makes em jump)"
  (let [y               (:y entity)
        distance        (* 2 gravity delta-time)
        action          (action-find entity :jump)
        start-time      (:start-time action)
        time-passed     (- (get-time) start-time)]
    (if (>= time-passed jump-time)  ;; entity is done jumping
      (action-remove entity :jump)
      (assoc entity
        :y (- y distance)))))

(def action-jump (struct action
                   :jump
                   jump-check
                   jump-update
                   0))



(def actions
  {:move    action-move
   :jump    action-jump})

(defn action-perform [entity action-name]
  "makes an entity perform an action"
  (action-add entity (assoc (action-name actions)
                       :start-time (get-time))))