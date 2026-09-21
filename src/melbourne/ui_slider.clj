(ns melbourne.ui-slider
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[xt.lang.common-data :as xtd]
             [js.react-native.ui-slider :as ui-slider]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.base-font :as base-font]
             ]
   :export [MODULE]})

(defn.js Slider
  "creates a horizontal slider"
  {:added "0.1"}
  [#{[design
      variant
      theme
      style
      
      max
      min
      step
      
      value
      setValue
      layout
      
      (:= length 150)
      (:.. rprops)]}]
  
  (var __variant (Object.assign
                  {:fg   {:key "primary"}
                   :bg   {:key "background"
                          :tone "diminish"}
                   :hovered {:bg {:raw 1}}
                   :pressed {:bg   {:key "primary"}
                             :fg   {:key "background"}}
                   :highlighted {:fg {:key "neutral"}
                                 :bg {:key "background"
                                      :tone "darken"
                                      :ratio 1}}
                   :active  {:fg {:key "background"}
                             :bg {:key "primary"}}}
                  variant))
  (var __style (base-font/getFontStyle (or (. __variant font)
                                           "h6")))
  (var __theme  (Object.assign (base-theme/themeUiInput
                           (base-palette/designPalette design)
                           __variant)
                          theme))
  
  (return
   [:% ui-slider/Slider
    #{[:theme __theme
       :style [{:padding 0}
               __style
               (:.. (xtd/arrayify style))]
       max
       min
       step
       length
       value
       setValue
       layout
       (:.. rprops)]}]))

(def.js MODULE (!:module))

