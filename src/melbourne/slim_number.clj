(ns melbourne.slim-number
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react.ext-form :as ext-form]
             [js.react-native :as n :include [:fn]]
             [xt.event.base-form :as event-form]
             [melbourne.slim-common :as slim-common]
             [melbourne.ui-spinner :as ui-spinner]
             [melbourne.ui-spinner-basic :as ui-spinner-basic]
             [melbourne.ui-slider :as ui-slider]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]]
   :export [MODULE]})

(defn.js FormSpinner
  "creates a Spinner"
  {:added "0.1"}
  [#{[design
      mini
      variant
      form
      meta
      label
      labelHide labelNone
      styleLabel
      labelWidth
      field
      fieldProps
      minWidth
      max
      min
      step
      decimal]}]
  (var #{value result} (ext-form/listenField form field
                                          (j/assign {:slim/type "spinner"
                                                     :fn/type   "field"}
                                                    meta)))
  (return 
   [:% slim-common/FormEnclosed
    #{design
      mini
      {:variant (xtd/get-in design ["variant" "label"])}
      styleLabel
      label
      labelHide labelNone
      minWidth}
    [:% n/Row
     {:style {:marginTop 5}}
     [:% ui-spinner/SpinnerControls
      #{[design
         variant
         :value value
         :setValue (event-form/field-fn form field)
         max
         min
         step
         :style {:paddingHorizontal 5}]}
      [:% ui-spinner/Spinner
      #{[design
         variant
         :style {:marginHorizontal 5
                 #_#_:padding 3}
         :value value
         :setValue (event-form/field-fn form field)
         max
         min
         step
         decimal
         (:.. fieldProps)]}]]]]))

(defn.js FormSpinnerBasic
  "creates a SpinnerBasic"
  {:added "0.1"}
  [#{[design
      mini
      variant
      form
      meta
      label
      labelHide labelNone
      styleLabel
      labelWidth
      field
      fieldProps
      minWidth
      max
      min
      step
      decimal]}]
  (var #{value result} (ext-form/listenField form field
                                          (j/assign {:slim/type "spinner"
                                                     :fn/type   "field"}
                                                    meta)))
  (return 
   [:% slim-common/FormEnclosed
    #{design
      mini
      {:variant (xtd/get-in design ["variant" "label"])}
      styleLabel
      label
      labelHide labelNone
      minWidth}
    [:% n/Row
     {:style {:marginTop 5}}
     [:% ui-spinner-basic/SpinnerBasicControls
      #{[design
         variant
         :value value
         :setValue (event-form/field-fn form field)
         max
         min
         step
         :style {:paddingHorizontal 5}]}
      [:% ui-spinner-basic/SpinnerBasic
      #{[design
         variant
         :style {:marginHorizontal 5
                 #_#_:padding 3}
         :value value
         :setValue (event-form/field-fn form field)
         max
         min
         step
         decimal
         (:.. fieldProps)]}]]]]))

(defn.js FormSlider
  "creates a Slider"
  {:added "0.1"}
  [#{[design
      mini
      variant
      form
      meta
      styleLabel
      
      label
      labelHide labelNone
      
      field
      fieldProps
      minWidth
      max
      min
      step
      decimal]}]
  (var #{value result} (ext-form/listenField form field
                                          (j/assign {:slim/type "slider"
                                                     :fn/type   "field"}
                                                    meta)))
  (return 
   [:% slim-common/FormEnclosed
    #{design
      mini
      {:variant (xtd/get-in design ["variant" "label"])}
      styleLabel
      label
      labelHide labelNone
      minWidth}
    [:% ui-slider/Slider
     #{[design variant
        :style {:marginTop 10
                :marginBottom 5}
        :value value
        :setValue (event-form/field-fn form field)
        max
        min
        step
        decimal
        (:.. fieldProps)]}]]))

(def.js MODULE (!:module))



