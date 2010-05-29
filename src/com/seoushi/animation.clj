;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; Functions for creating and updating animations
;;

(ns com.seoushi.animation
  (:use com.seoushi.time))

;; creates an animation
(defn anim [sprite-sheet start-sprite end-sprite frame-delay]
  (loop [cur-sprite start-sprite
         sprites []]
    (if (> cur-sprite end-sprite)
      {:delay frame-delay 
       :frames sprites}
      (recur (inc cur-sprite)
        (conj sprites
          (nth sprite-sheet cur-sprite))))))

;; gets the current frame of an animation
(defn anim-get-frame [anim start-time]
  (let [time-passed (- (get-time) start-time)
        frame-delay (:delay anim)
        frames (:frames anim)]
    (nth frames
      (rem (/ time-passed frame-delay)
           (count frames)))))
