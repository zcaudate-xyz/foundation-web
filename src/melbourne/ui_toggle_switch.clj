(ns melbourne.ui-toggle-switch
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react-native.ui-toggle-switch :as ui-toggle-switch]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.base-font :as base-font]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]]
   :export [MODULE]})

(defn.js ToggleSwitch
  "creates the toggle switch"
  {:added "0.1"}
  [#{[design
      variant
      style
      theme
      selected
      (:= onText "")
      (:= offText "")
      (:.. rprops)]}]
  (var __variant (xtd/obj-assign-nested
                  {:fg   {:key "neutral"
                          :tone "sharpen"}
                   :bg   {:key "background"
                          :tone "flatten"}
                   :hovered {:bg {:raw 1}}
                   :active  {:fg {:key "primary"
                                  :tone "flatten"}
                             :bg {:key "background"
                                  :tone "mix"
                                  :mix "primary"
                                  :ratio 5}}
                   :text {:key "background"}}
                  variant))
  (var __style (base-font/getFontStyle (or (. __variant font)
                                           "h6")))
  (var __theme  (j/assign (base-theme/themeUiState
                           (base-palette/designPalette design)
                           __variant)
                          theme))
  (return
   [:% ui-toggle-switch/ToggleSwitch
    #{[:theme __theme
       :selected selected
       :style [{:padding 10}
               __style
               (:.. (j/arrayify style))]
       :knobStyle {:width 22
                   :height 22
                   :justifyContent "center"
                   :alignItems "center"}
       :axisStyle {:height 22
                   :width 44
                   :marginVertical 0}
       (:.. rprops)]}]))

(def.js MODULE (!:module))

(comment

  (comment
  (defn.js SwitchLight
    "switch light color palette"
    {:added "0.1"}
    [#{brandColor
       brandNeutral
       brandBackground}]
    (return
     {:bgNormal   (c/lighten brandBackground 0.9)
      :fgNormal   (c/lighten (c/saturate brandColor 0.3)
                             0.6)
      :fgPressed  1
      :bgPressed  0.6
      :fgHovered  1
      :bgHovered  0.9
      :bgActive   (c/lighten (c/saturate brandColor -0.2)
                             -0.5)
      :fgActive   (c/lighten brandColor #_(c/saturate brandColor -0.2)
                             -0.2)}))

  (defn.js SwitchDark
    "switch dark color palette"
    {:added "0.1"}
    [#{brandColor
       brandNeutral
       brandBackground}]
    (return
     {:bgNormal   (c/lighten brandBackground -0.9)
      :fgNormal   (c/saturate brandColor 0.4)
      :fgPressed  1
      :bgPressed  -0.9
      :fgHovered  1
      :bgHovered  -0.9
      :bgActive   brandNeutral #_(c/lighten brandBackground -0.9)
      :fgActive   (c/saturate brandColor -0.4)}))))



