(ns melbourne.ui-button
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r]
             [js.react-native.ui-button :as ui-button]
             [xt.lang.base-lib :as k]
             [melbourne.addon-tooltip :as addon-tooltip]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.base-font :as base-font]]
   :export [MODULE]})

(defn.js Button
  "constructs a button"
  {:added "4.0"}
  [#{[design
      variant 
      theme
      style
      transformations
      addons
      tooltip
      (:= refLink (r/ref))
      (:.. rprops)]}]
  (var [chord setChord] (r/local {}))
  (var __variant (j/assign
                  {:fg {:key "background"}
                   :bg {:key "primary"}}
                  variant))
  (var __style   (base-font/getFontStyle (or (. __variant font)
                                             "h6")))
  (var __theme   (j/assign (base-theme/themeUiButton
                            (base-palette/designPalette design)
                            __variant)
                           theme))
  (return
   [:% ui-button/Button
    #{[:refLink refLink
       :onChord setChord
       :theme __theme
       :style [{:padding 8
                :borderRadius 3}
               __style
               (:.. (j/arrayify style))]
       :addons [(:? tooltip
                    (addon-tooltip/addonTooltip
                     refLink
                     (. chord hovering)
                     #{design
                       tooltip
                       {:variant (k/get-in design ["variant" "tooltip"])}}))
                (:.. (k/arrayify addons))]
       :transformations
       (j/assign
        {:bg (fn:> [#{pressing}]
                   {:style {:transform [{:scale (+ 1 (* 0.08 pressing))}]}})}
        transformations)
       (:.. rprops)]}]))

(def.js MODULE (!:module))
