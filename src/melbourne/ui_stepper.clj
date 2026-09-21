(ns melbourne.ui-stepper
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]
             [xt.lang.common-math :as xtm]
             
             [js.react :as r]
             [js.react-native :as n]
             [js.react-native.animate :as a]
             [js.react-native.physical-base :as physical-base]
             [js.react-native.model-roller :as model-roller]
             [melbourne.ui-group :as ui-group]]
   :export [MODULE]})

(defn.js StepperTabs
  "creates the stepper tabs"
  {:added "0.1"}
  [#{[design
      variant
      total
      style
      styleContainer
      index
      setIndex
      (:.. rprops)]}]
  (return
   [:% ui-group/TabsIndexed
    #{[design
       variant
       setIndex
       index
       :style [{:height 10
                :width 10
                :borderRadius 10
                :padding 2
                :marginHorizontal 6}
               (:.. (xtd/arrayify style))]
       :items (xtd/arr-repeat "" total)
       (:.. rprops)]}]))

(defn.js stepperOffset
  "offset function for the stepper"
  {:added "0.1"}
  [modelFn total offset index]
  (var v (- offset index))
  (var #{translate
         scale
         visible} (modelFn v))
  (return
   {:style {:opacity (:? visible
                         (xtm/mix 0 1 scale)
                         0)
            :zIndex (:? visible (* 100 scale) -100)}}))

(defn.js Stepper
  "creates a stepper"
  {:added "0.1"}
  [#{[index
      setIndex
      style
      pageStyle
      (:= offsetFn -/stepperOffset)
      (:= pages [])
      (:.. rprops)]}]
  (var total (xtm/max (xt/x:len pages) 1))
  (var iindicator   (a/useCircularIndicator
                     index
                     {:default {:type "timing"
                                :duration 500
                                :easing a/linear}}
                     
                     nil
                     nil
                     total
                     nil))
  (var modelFn (r/const (model-roller/roller-model total 10)))
  (var pageFn
       (fn [page i]
         (return
          [:% physical-base/Box
           {:key (+ "page" i)
            :style [{:position "absolute"}
                    (:.. (xtd/arrayify pageStyle))]
            :indicators {:offset iindicator}
            :children [[:% n/View
                        {:key "parent"}
                        page]]
            :transformations
            {:offset (fn:> [offset] (offsetFn modelFn total offset i))}}])))
  (return
   [:% n/View
    {:style [{:overflow "hidden"}
             (:.. (xtd/arrayify style))]}
    (xtd/arr-map pages pageFn)]))

(def.js MODULE (!:module))


