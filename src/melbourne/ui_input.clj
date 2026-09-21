(ns melbourne.ui-input
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react-native.ui-input :as ui-input]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             ]
   :export [MODULE]})

(defn.js Input
  "constructs a themed input"
  {:added "0.1"}
  [#{[design
      variant
      theme
      styleContainer
      style
      (:.. rprops)]}]
  (var palette (base-palette/designPalette design))
  (var __variant
       (j/assign
        {:fg   {:key "neutral"}
         :bg   {:key "background"
                :mix "primary"
                :ratio 1}
         :pressed {:bg {:key "primary"}}
         :highlighted {:fg {:key "neutral"}
                       :bg   {:key "background"
                              :mix "primary"
                              :ratio 1}}
         :active  {:fg {:key "background"}
                   :bg {:key "primary"
                        :mix "neutral"
                        :ratio 4}}}
        variant))
  (var __theme  (j/assign (base-theme/themeUiInput
                           palette
                           __variant)
                          theme))
  (return
   [:% ui-input/Input
    #{[:theme __theme
       :selectionColor (. palette mainColor)
       :style [{:fontFamily "Helvetica"}
               (:.. (j/arrayify style))]
       :styleContainer [{:flex 1
                         :borderStyle "solid" 
                         :borderWidth 1
                         :borderColor "black"}
                        (:.. (j/arrayify styleContainer))]
       (:.. rprops)]}]))

(def.js MODULE (!:module))

