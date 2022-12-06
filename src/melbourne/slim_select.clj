(ns melbourne.slim-select
  (:use code.test)
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react.ext-form :as ext-form]
             [xt.lang.event-form :as event-form]
             [melbourne.slim-common :as slim-common]
             [melbourne.ui-picker :as ui-picker]
             [melbourne.ui-dropdown :as ui-dropdown]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js FormPicker
  "creates a Picker"
  {:added "0.1"}
  [#{[design
      variant
      mini
      form
      meta
      label
      styleLabel
      labelHide labelNone
      field
      fieldProps
      data
      minWidth]}]
  (var #{value result} (ext-form/listenField form field
                                          (j/assign {:slim/type "picker"
                                                     :fn/type   "field"}
                                                    meta)))
  (var setValue (r/const (fn [v]
                           (event-form/set-field form field v)
                           (event-form/validate-field form field))))
  (return 
   [:% slim-common/FormEnclosed
    #{design
      mini
      {:variant (k/get-in design ["variant" "label"])}
      styleLabel
      labelHide labelNone
      label
      minWidth}
    [:% n/Row
     {:style {:marginTop 5}}
     [:% ui-picker/PickerControls
      #{design
        {:style {:paddingHorizontal 5
                 :marginHorizontal 2}
         #_#_:style {:paddingHorizontal 0
                 :paddingVertical 2
                     }
         :value value
         :setValue setValue}}
      [:% ui-picker/Picker
       #{[design variant data value
          :setValue setValue
          (:.. fieldProps)]}]]]]))

(defn.js FormDropdown
  "creates a Dropdown"
  {:added "0.1"}
  [#{[design
      variant
      mini
      form
      meta
      label
      styleLabel
      labelHide labelNone
      field
      fieldProps
      data
      minWidth
      active
      setActive]}]
  (var #{value result} (ext-form/listenField
                        form field
                        (j/assign {:slim/type "dropdown"
                                   :fn/type   "field"}
                                  meta)))
  (var setValue (r/const (fn [v]
                           (event-form/set-field form field v)
                           (event-form/validate-field form field))))
  (return 
   [:% slim-common/FormEnclosed
    #{design
      mini
      labelHide labelNone
      styleLabel
      {:variant (k/get-in design ["variant" "label"])}
      label
      minWidth}
    [:% ui-dropdown/Dropdown
     #{[design variant data value
        :styleContainer {:marginVertical 2
                         :marginHorizontal 2}
        :setValue setValue
        active
        setActive
        (:.. fieldProps)]}]]))

(def.js MODULE (!:module))

