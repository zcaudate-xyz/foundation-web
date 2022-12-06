(ns melbourne.ui-swiper
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react-native.ui-swiper :as ui-swiper]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             ]
   :export [MODULE]})

(defn.js Swiper
  "creates a slim swiper"
  {:added "0.1"}
  [#{[design
      variant
      theme
      (:.. rprops)]}]
  (var __variant
       (j/assign
        {:fg   {:key "primary"
                :tone "flatten"}
         :bg   {:key "primary"
                :tone "darken"
                :ratio 1}
         :pressed {:fg {:key "primary"}
                   :bg {:key "primary"
                        :tone "sharpen"}}}
        variant))
  (var __theme  (j/assign (base-theme/themeUiButton
                           (base-palette/designPalette design)
                           __variant)
                          theme))
  (return
   [:% ui-swiper/Swiper
    #{[:theme __theme
       (:.. rprops)]}]))

(def.js MODULE (!:module))

