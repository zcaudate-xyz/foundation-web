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
             [xt.lang.common-math :as xtm]
             [melbourne.base-palette :as base-palette]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]
             [xt.lang.common-string :as xts]]
   :export [MODULE]})

(defn.js getPath
  [values width height maxValue minValue]
  (when (xtd/is-empty? values)
    (return ""))
  (var out [])
  (var maxX (- (xt/x:len values) 1))
  (var maxY (+ (or maxValue
                   (xtm/max (:.. values)))
               2))
  (var minY (- (or minValue
                   (xtm/min (:.. values)))
               2))
  (xt/for:array [[i v] values]
    (xt/x:arr-push out (xt/x:cat (xtm/round (/ (* width i)
                                       maxX))
                           ","
                           (- height
                              (* height
                                 (/ (- v minY)
                                    (- maxY minY)))))))
  (return (+ "M " (xts/join " L " out))))

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
  (var __variant (Object.assign
                  {:fg {:key "primary"}}
                  variant))
  (return
   [:% n/Svg
    {:height height
     :width width
     :style (Object.assign
             {:backgroundColor (:? (. __variant bg)
                                   (base-palette/getColor
                                    palette
                                    (. __variant bg)))}
             style)}
    (r/% n/Path
         (Object.assign
          {:d path
           :fill "none"
           :stroke (base-palette/getColor
                    palette
                    (. __variant fg))
           :strokeWidth 1}
          pathStyle))]))

(def.js MODULE (!:module))
