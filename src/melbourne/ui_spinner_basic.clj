(ns melbourne.ui-spinner-basic
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react-native :as n :include [:fn]]
             [js.react-native.ui-spinner-basic :as ui-spinner-basic]
             [melbourne.ui-helper :as ui-helper]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.base-font :as base-font]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js SpinnerBasicControls
  "creates spinner controls"
  {:added "0.1"}
  [#{[setValue
      value
      (:= step 1)
      max
      min
      (:.. rprops)]}]
  (return
   [:% ui-helper/HelperControl
    #{[:leftDisabled (<= value min)
       :rightDisabled (>= value max)
       :onLeft  (fn:> (setValue (j/max min (j/min max (-  value step)))))
       :onRight (fn:> (setValue (j/max min (j/min max (+  value step)))))
       (:.. rprops)]}]))

(defn.js SpinnerBasic
  "Creates a spinner"
  {:added "0.1"}
  [#{[design
      variant
      theme
      max
      min
      decimal
      step
      value
      setValue
      style
      styleDigit
      styleDigitText
      styleDecimal
      styleDecimalText
      (:.. rprops)]}]
  (var __variant
       (j/assign
        {:fg   {:key "primary"
                :tone "flatten"}
         :bg   {:key "background"
                :tone "darken"
                :ratio 1}
         :pressed {:fg {:key "primary"}
                   :bg {:key "primary"
                        :tone "sharpen"}}
         :highlighted {:fg {:key "neutral"}
                       :bg {:key "background"
                            :tone "darken"
                            :ratio 1}}
         :active  {:fg {:key "background"}
                   :bg {:key "primary"}}}
        variant))
  (var __style (base-font/getFontStyle (or (. __variant font)
                                           "h6")))
  (var __theme  (j/assign (base-theme/themeUiInput
                           (base-palette/designPalette design)
                           __variant)
                          theme))
  (var #{fgNormal} __theme)
  (return
   [:% ui-spinner-basic/SpinnerBasic
    #{[:theme __theme
       :style [{:padding 0}
               __style
               (:.. (j/arrayify style))]
       max
       min
       step
       decimal
       value
       setValue
       :styleDigit     [{:backgroundColor nil}
                        (:.. (j/arrayify styleDigit))]
       :styleDigitText [{:color fgNormal
                         :backgroundColor nil}
                        (:.. (j/arrayify styleDigitText))]
       :styleDecimal   [{:backgroundColor nil}
                        (:.. (j/arrayify styleDecimal))]
       :styleDecimalText [{:color fgNormal
                           :backgroundColor nil}
                          (:.. (j/arrayify styleDecimalText))]
       (:.. rprops)]}]))

(def.js MODULE (!:module))

