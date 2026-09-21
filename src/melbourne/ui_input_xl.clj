(ns melbourne.ui-input-xl
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react-native.helper-color :as c]
             [js.react-native :as n :include [:fn]]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-math :as xtm]
             [melbourne.ui-input :as ui-input]
             [melbourne.ui-input-xl :as ui-input-xl]
             [melbourne.base-palette :as base-palette]]
   :export [MODULE]})

(defn.js inputPlaceHolder
  "Creates the input placeholder"
  {:added "0.1"}
  [placeholder
   design]
  (var #{mainNeutral
         mainBackground} (base-palette/designPalette design))
  (return {:component n/Text
           :key "placeholder"
           :numberOfLines 1
           :style {:position "absolute"
                   :fontSize 20
                   :top 0
                   :zIndex -100
                   :opacity 0
                   :fontWeight "400"
                   :textShadowColor mainNeutral}
           :children [placeholder]
           :transformations
           (fn [#{emptying
                  focusing
                  highlighted}]
             (var active (j/max (- 1 emptying)
                                focusing))
             (var color (c/interpolateColor
                         mainNeutral
                         mainBackground
                         (:? #_(< 0.01 highlighted)
                             (- 1 active))))
             (return {:style {:fontSize   (xtm/mix 18  10  active)
                              :opacity 0.6
                              :color (c/toHSL color)
                              :transform
                              [{:translateY (xtm/mix 15 53 active)}
                               {:translateX (xtm/mix 10 -5  active)}]}}))}))

(defn.js InputXL
  "creates the large input"
  {:added "0.1"}
  [#{[design
      variant
      placeholder
      style
      styleContainer
      inner
      (:.. rprops)]}]
  (return [:% ui-input/Input
           #{[design
              variant
              :style [{:height 50
                       :paddingLeft 8
                       :fontSize 20}
                      (:.. (j/arrayify style))]
              :styleContainer [{:flex 1
                                :borderRadius 5
                                :height 50}
                               (:.. (j/arrayify styleContainer))]
              :inner [(-/inputPlaceHolder placeholder design)
                      (:.. (j/arrayify inner))]
              :outlined true
              (:.. rprops)]}]))

(def.js MODULE (!:module))
