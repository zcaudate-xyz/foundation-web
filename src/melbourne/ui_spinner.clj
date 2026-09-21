(ns melbourne.ui-spinner
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[xt.lang.common-math :as xtm]
             [js.react-native :as n :include [:fn]]
             [js.react-native.ui-spinner :as ui-spinner]
             [melbourne.ui-helper :as ui-helper]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.base-font :as base-font]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]]
   :export [MODULE]})

(defn.js SpinnerControls
  "creates spinner controls"
  {:added "0.1"}
  [#{[setValue
      value
      step
      max
      min
      (:.. rprops)]}]
  (return
   [:% ui-helper/HelperControl
    #{[:leftDisabled (<= value min)
       :rightDisabled (>= value max)
       :onLeft  (fn:> (setValue (xtm/max min (xtm/min max (-  value step)))))
       :onRight (fn:> (setValue (xtm/max min (xtm/min max (+  value step)))))
       (:.. rprops)]}]))

(defn.js SpinnerValues
  "creates only spinner values"
  {:added "0.1"}
  [#{[design
      variant
      theme
      max
      min
      step
      value
      style
      styleDigit
      styleDigitText
      styleDecimal
      styleDecimalText
      (:.. rprops)]}]
  (var __variant (xtd/obj-assign-nested
                  {:fg   {:key "primary"
                          :tone "flatten"}
                   :bg   {:key "background"
                          :tone "augment"}}
                  variant))
  (var __style (base-font/getFontStyle (or (. __variant font)
                                           "h6")))
  (var __theme  (Object.assign (base-theme/themeNormal
                           (base-palette/designPalette design)
                           __variant)
                          theme))
  (var #{fgNormal
         bgNormal} __theme)
  (return
   [:% n/Row
    {:style [{:padding 0
              :backgroundColor bgNormal}
             __style
             (:.. (xtd/arrayify style))]}
    [:% ui-spinner/SpinnerValues
     #{[max
        min
        value
        :styleDigit     [{:backgroundColor nil}
                         (:.. (xtd/arrayify styleDigit))]
        :styleDigitText [{:color fgNormal
                          :backgroundColor nil}
                         (:.. (xtd/arrayify styleDigitText))]
        :styleDecimal   [{:backgroundColor nil}
                         (:.. (xtd/arrayify styleDecimal))]
        :styleDecimalText [{:color fgNormal
                            :backgroundColor nil}
                           (:.. (xtd/arrayify styleDecimalText))]
        (:.. rprops)]}]]))

(defn.js Spinner
  "Creates a spinner"
  {:added "0.1"}
  [#{[design
      variant
      theme
      max
      min
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
       (Object.assign
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
  (var __theme  (Object.assign (base-theme/themeUiInput
                           (base-palette/designPalette design)
                           __variant)
                          theme))
  (var #{fgNormal} __theme)
  (return
   [:% ui-spinner/Spinner
    #{[:theme __theme
       :style [{:padding 0}
               __style
               (:.. (xtd/arrayify style))]
       max
       min
       step
       value
       setValue
       :styleDigit     [{:backgroundColor nil}
                        (:.. (xtd/arrayify styleDigit))]
       :styleDigitText [{:color fgNormal
                         :backgroundColor nil}
                        (:.. (xtd/arrayify styleDigitText))]
       :styleDecimal   [{:backgroundColor nil}
                        (:.. (xtd/arrayify styleDecimal))]
       :styleDecimalText [{:color fgNormal
                           :backgroundColor nil}
                          (:.. (xtd/arrayify styleDecimalText))]
       (:.. rprops)]}]))

(def.js MODULE (!:module))

