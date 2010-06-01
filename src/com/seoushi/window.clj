;;
;; Author: Sean Chapel (seoushi@gmail.com)
;; Functions for creating and handling a java 2d drawing context
;;
(ns com.seoushi.window
  (:import (javax.swing JFrame JPanel JComponent)
      (java.awt.event WindowAdapter KeyAdapter MouseAdapter KeyEvent)
      (java.awt Dimension Canvas Graphics Color Toolkit)
      (java.awt.image BufferStrategy))
  (:use com.seoushi.time)
  )


(def LAST-PRESS-TIME (ref (get-time)))


(defn on-window-opened [canvas window-created]
   (let [#^BufferStrategy strategy (.getBufferStrategy canvas)
        graphics (.getDrawGraphics strategy)]
     (window-created {:buffer strategy :graphics graphics})))




;; creates a window for drawing on
(defn make-window [width height title mouse-listener key-listener window-created window-listener main-loop]
  (let [#^JFrame frame (JFrame. title)
        #^JPanel panel (doto (.getContentPane frame)
                         (.setPreferredSize (Dimension. width height))
                         (.setLayout nil))
        #^Canvas canvas (Canvas.)]
    (doto canvas
      (.setBounds 0 0 width height)
      (.setIgnoreRepaint true)
      (.addKeyListener (proxy [KeyAdapter] []
                         (keyPressed [e] (key-listener e :pressed))
                         (keyReleased [e] (key-listener e :released))))
      (.addMouseListener (proxy [MouseAdapter] []
                           (mouseClicked [e] (mouse-listener e)))))
    (.add panel canvas)
    (doto frame
      (.addWindowListener (proxy [WindowAdapter] []
                            (windowClosing [e] (window-listener frame))))
      (.pack)
      (.setResizable false)
      (.setVisible true))
    (.createBufferStrategy canvas 2)
    (main-loop (on-window-opened
                 canvas
                 window-created))))