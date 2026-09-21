(ns melbourne.ui-toggle-button
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r]
             [js.react-native.ui-toggle-button :as ui-toggle-button]
             [xt.lang.base-lib :as k]
             [melbourne.addon-tooltip :as addon-tooltip]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.base-font :as base-font]]
   :export [MODULE]})

(defn.js ToggleButton
  "creates a toggle button"
  {:added "0.1"}
  [#{[(:= refLink (r/ref))
      design
      variant
      style
      theme
      transformations
      addons
      tooltip
      (:.. rprops)]}]
  (var palette  (base-palette/designPalette design))
  (var __variant (j/assign
                  {:fg {:key "neutral"}
                   :bg {:key "background"}
                   :active  {:fg {:key "background"}
                             :bg {:key "primary"}}}
                  variant))
  (var __style   (base-font/getFontStyle (or (. __variant font)
                                             "h6")))
  (var __theme   (j/assign (base-theme/themeUiState
                            (base-palette/designPalette design)
                            __variant)
                           theme))
  (var [chord setChord] (r/local {}))
  (return
   [:% ui-toggle-button/ToggleButton
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
                       {:variant (k/get-in design ["variant" "tooltip"])}
                       tooltip}))
                (:.. (k/arrayify addons))]
       :transformations
       (j/assign
        {:bg (fn:> [#{pressing}]
                   {:style {:transform [{:scale (+ 1 (* 0.08 pressing))}]}})}
        transformations)
       (:.. rprops)]}]))

(def.js MODULE (!:module))

