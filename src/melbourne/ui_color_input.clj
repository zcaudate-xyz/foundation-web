(ns melbourne.ui-color-input
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [
             [js.react :as r]
             [js.react-native :as n :include [:fn]]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-input :as ui-input]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]]
   :export [MODULE]})

(defn.js ColorInput
  "constructs a themed input"
  {:added "0.1"}
  [#{[design
      variant
      theme
      styleContainer
      style
      value
      setValue
      (:.. rprops)]}]
  (var [currentText setCurrentText] (r/local value))
  (r/watch [value] (setCurrentText value))
  (return
   [:% n/Row
    {:style [{:alignItems "center"}
             styleContainer]}
    
    [:% ui-input/Input
     #{design
       {:value currentText
        :onSubmitEditing (fn []
                           (setValue currentText))
        :onChangeText setCurrentText
        :onBlur (fn:> (setCurrentText value))}}]
    (:? (xtd/not-empty? value)
        [:% n/View
         {:style {:backgroundColor value
                  :height 30
                  :width 30}}])]))

(def.js MODULE (!:module))

