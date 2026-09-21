(ns pune.common.data-swap
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :xtalk
  {:require [
             [xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as xtl]
             [xt.lang.common-string :as xts]
             [xt.lang.common-math :as xtm]]
   :export [MODULE]})

(defn.xt fprice-to-vprice
  "float price to vector price"
  {:added "0.1"}
  [fprice]
  (var decimal-floor
       (xtm/floor (xtm/log10 fprice)))
  (when (< decimal-floor -8)
    (xt/x:throw (xt/x:ex "floor too small" {:input fprice})))
  (cond (>= decimal-floor -4)
        (do (var units (xtm/round
                          (/ fprice
                             (xtm/pow 10
                                    (- (xtm/floor (xtm/log10 fprice))
                                       4)))))
            (return [(+ 4 decimal-floor)
                     units]))
        
        :else
        (do (var units (xtm/round
                        (* fprice
                           100000000)))
            (return [0 units]))))

(defn.xt vprice-to-fprice
  "vector price to float price"
  {:added "0.1"}
  [vprice]
  (var [power units] vprice)
  (when (< power 0)
    (xt/x:throw (xt/x:ex "power negative" {:input vprice})))
  (when (> power 99999)
    (xt/x:throw (xt/x:ex "units too high" {:input vprice})))
  (when (not (or (== power 0)
                 (>= units 10000)))
    (xt/x:throw (xt/x:ex "units too low" {:input vprice})))
  (return (* (xtm/pow 10 (- power 8))
             units)))

(defn.xt vprice-to-position
  "vector price to trade position"
  {:added "0.1"}
  [vprice]
  (var [power units] vprice)
  (when (< power 0)
    (xt/x:throw (xt/x:ex "power negative" {:input vprice})))
  (when (> power 99999)
    (xt/x:throw (xt/x:ex "units too high" {:input vprice})))
  (when (not (or (== power 0)
                 (>= units 10000)))
    (xt/x:throw (xt/x:ex "units too low" {:input vprice})))
  (return (+ (* 100000 power)
             units)))

(defn.xt fprice-to-position
  "float price to trade position"
  {:added "0.1"}
  [fprice]
  (var vprice (-/fprice-to-vprice fprice))
  (var [power units] vprice)
  (return (+ (* 100000 power)
             units)))

(defn.xt position-to-vprice
  "trade position to vector price"
  {:added "0.1"}
  [position]
  (var units (xtm/mod position 100000))
  (var power (xtm/floor (xtl/div position 100000)))
  (return [power
           units]))

(defn.xt position-to-fprice
  "trade position to float price"
  {:added "0.1"}
  [position]
  (var units (xtm/mod position 100000))
  (var power (xtm/floor (/ position 100000)))
  (return (* (xtm/pow 10 (- power 8))
             units)))

(defn.xt position-to-fdecimal
  "trade position to float price"
  {:added "0.1"}
  [position]
  (var power (xtm/floor (/ position 100000)))
  (return (xtm/max 0 (- 8 power))))

(defn.xt position-to-fstr
  "trade position to float price"
  {:added "0.1"}
  [position]
  (return
   (xts/to-fixed (-/position-to-fprice position)
               (-/position-to-fdecimal position))))

(defn.xt position-to-scale
  "trade position to float price"
  {:added "0.1"}
  [position]
  (return
   (* (-/position-to-fprice position)
      (xtm/pow 10 (-/position-to-fdecimal position)))))

(def.xt MODULE (!:module))
