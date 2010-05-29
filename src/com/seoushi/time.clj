;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; Functions for manipulating time
;;

(ns com.seoushi.time
  ;(:require )
  ;(:use )
  ;(:import )
  )


;; the last time the frame was updated
(def LAST-FRAME-TIME (ref ()))


;; gets the current time in milliseconds
(defn get-time []
  (System/currentTimeMillis))

;; get the time passed since a given time
(defn get-delta-time [last-time]
  (/ (- (get-time) last-time) 1000))

;; updates the last frame time
(defn update-time []
  (dosync (ref-set LAST-FRAME-TIME (get-time))))