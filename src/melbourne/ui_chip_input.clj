(ns melbourne.ui-chip-input
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-util :as ui-util]
             [melbourne.ui-input :as ui-input]
             [melbourne.ui-chip :as ui-chip]
             [melbourne.ui-button :as ui-button]
             [xt.lang.base-lib :as k]]
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
  (when (k/is-string? values)
    (:= values (k/json-decode values)))
  (when (k/is-empty? values)
    (:= values []))
  (var [showInput setShowInput] (r/local false))
  (var [currentText setCurrentText] (r/local ""))
  (var refInput (r/ref))
  
  (var visibleInput (or showInput
                        (k/is-empty? values)))
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
          (cond (k/not-empty? currentText)
                (do (setValues [(:.. values) currentText])
                    (setCurrentText "")
                    (setShowInput false))

                (k/not-empty? values)
                (setShowInput false)))
        :onChangeText
        (fn [text]
          (cond (j/endsWith text ",")
                (do (var out (k/trim (k/first (j/split text ","))))
                    (when (k/not-empty? out)
                      (setValues [(:.. values) out])
                      (setCurrentText "")))

                :else
                (setCurrentText text)))}]]
     
     [:% n/Row
      {:style {:flexWrap "wrap"
               :maxWidth 400
               :alignItems "center"}}
      (j/map values
             (fn:> [value i]
               [:% ui-chip/Chip
                #{design
                  {:key i
                   :text value
                   :onClose (fn:> (setValues (k/arr-omit values i)))}}]))
      (:? (k/not-empty? values)
          [:% ui-button/Button
           #{design
             {:variant {:fg {:key "primary"}
                        :bg {:key "background"}
                        :pressed {:bg {:key "background"}}}
              :outlined true
              :onPress (fn:> (:? (k/not-empty? values)
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
