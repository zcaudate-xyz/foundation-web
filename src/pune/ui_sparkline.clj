(ns pune.ui-sparkline
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react-native :as n :include [:fn :svg]]
             [js.react :as r :include [:fn]]
             [js.core :as j]
             [melbourne.base-palette :as base-palette]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js getPath
  [values width height maxValue minValue]
  (when (k/is-empty? values)
    (return ""))
  (var out [])
  (var maxX (- (k/len values) 1))
  (var maxY (+ (or maxValue
                   (k/max (:.. values)))
               2))
  (var minY (- (or minValue
                   (k/min (:.. values)))
               2))
  (k/for:array [[i v] values]
    (x:arr-push out (k/cat (j/round (/ (* width i)
                                       maxX))
                           ","
                           (- height
                              (* height
                                 (/ (- v minY)
                                    (- maxY minY)))))))
  (return (+ "M " (j/join out " L "))))

(defn.js Sparkline
  [#{design
     variant
     values
     width
     height
     style
     pathStyle
     maxValue
     minValue}]
  (var path (-/getPath values width height
                       maxValue
                       minValue))
  (var palette  (base-palette/designPalette design))
  (var __variant (j/assign
                  {:fg {:key "primary"}}
                  variant))
  (return
   [:% n/Svg
    {:height height
     :width width
     :style (j/assign
             {:backgroundColor (:? (. __variant bg)
                                   (base-palette/getColor
                                    palette
                                    (. __variant bg)))}
             style)}
    (r/% n/Path
         (j/assign
          {:d path
           :fill "none"
           :stroke (base-palette/getColor
                    palette
                    (. __variant fg))
           :strokeWidth 1}
          pathStyle))]))

(def.js MODULE (!:module))

