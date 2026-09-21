(ns melbourne.ui-chip-input
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [
             [js.react :as r]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-util :as ui-util]
             [melbourne.ui-input :as ui-input]
             [melbourne.ui-chip :as ui-chip]
             [melbourne.ui-button :as ui-button]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as xtl]
             [xt.lang.common-data :as xtd]
             [xt.lang.common-string :as xts]]
   :export [MODULE]})

(defn.js ChipInput
  "constructs a themed input"
  {:added "0.1"}
  [#{[design
      variant
      theme
      styleContainer
      style
      values
      setValues
      (:.. rprops)]}]
  (when (xtl/is-string? values)
    (:= values (xt/x:json-decode values)))
  (when (xtd/is-empty? values)
    (:= values []))
  (var [showInput setShowInput] (r/local false))
  (var [currentText setCurrentText] (r/local ""))
  (var refInput (r/ref))
  
  (var visibleInput (or showInput
                        (xtd/is-empty? values)))
  (return
   [:% n/Row
    {:style styleContainer}
    [:% n/View
     [:% ui-util/Fold
      {:visible visibleInput}
      [:% ui-input/Input
       {:design design
        :refLink refInput
        :value currentText
        :onFocus (fn:> (setShowInput true))
        :onBlur  (fn:> (setShowInput false))
        :onSubmitEditing
        (fn []
          (cond (xtd/not-empty? currentText)
                (do (setValues [(:.. values) currentText])
                    (setCurrentText "")
                    (setShowInput false))

                (xtd/not-empty? values)
                (setShowInput false)))
        :onChangeText
        (fn [text]
          (cond (xts/ends-with? text ",")
                (do (var out (xts/trim (xtd/first (xts/split text ","))))
                    (when (xtd/not-empty? out)
                      (setValues [(:.. values) out])
                      (setCurrentText "")))

                :else
                (setCurrentText text)))}]]
     
     [:% n/Row
      {:style {:flexWrap "wrap"
               :maxWidth 400
               :alignItems "center"}}
      (xtd/arr-map values
             (fn:> [value i]
               [:% ui-chip/Chip
                #{design
                  {:key i
                   :text value
                   :onClose (fn:> (setValues (xtd/arr-omit values i)))}}]))
      (:? (xtd/not-empty? values)
          [:% ui-button/Button
           #{design
             {:variant {:fg {:key "primary"}
                        :bg {:key "background"}
                        :pressed {:bg {:key "background"}}}
              :outlined true
              :onPress (fn:> (:? (xtd/not-empty? values)
                                 (setShowInput (not showInput))))
              :style {:borderRadius 0
                      :margin 3
                      :padding 4
                      :paddingHorizontal 7
                      :width 30
                      :borderWidth 1
                      :textAlign "center"}
              :text [:% n/Icon
                     {:key "icon"
                      :name (:? visibleInput "minus" "plus")}]}}])]]]))

(def.js MODULE (!:module))
