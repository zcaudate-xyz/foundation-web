(ns melbourne.slim-error
  (:use code.test)
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :playground/web-basic
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-tooltip :as ui-tooltip]
             [xt.lang.base-lib :as k]
             [xt.lang.base-text :as base-text]
             [melbourne.ui-button :as ui-button]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-font :as base-font]]
   :export [MODULE]})

(defn.js ErrorInfo
  "constructs a error info demo"
  {:added "0.1"}
  [#{[(:= design {})
      (:= result {})
      style
      onClose]}]
  (var #{tag message status debug} (or result {}))
  (var #{mainBackground
         mainError
         mainNeutral} (base-palette/designPalette design))
  (var [visible setVisible] (r/local (fn:> false)))
  
  (var buttonRef (r/ref))
  (var buttonLabel (+ (j/toUpperCase (base-text/tag-string (or tag "")))
                      (:? message (+ " - " message) "")))
  (var errorText (n/format-entry result))
  (var errorVariant {:bg {:key "error"}
                     :pressed {:bg {:key "error"}}})
  (return
   [:% n/Row
    {:style [{:backgroundColor mainError
              :borderRadius 3
              #_#_:height 35
              :opacity 0.9
              :alignItems "center"
              :paddingHorizontal 10}
             (:.. (j/arrayify style))]}
    [:<>
     [:% ui-button/Button
      {:refLink buttonRef
       :design design
       :variant errorVariant
       :style {:padding 5
               :fontWeight "400"
               :fontSize 12}
       :onPressIn  (fn:> (setVisible true))
       :onPressOut (fn:> (setVisible false))
       :text (:? (k/is-empty? buttonLabel)
                 "UNKNOWN ERROR"
                 buttonLabel)
       :transformations {:bg nil}}]
     [:% ui-tooltip/Tooltip
      {:hostRef buttonRef
       :position "bottom"
       :alignment "center"
       :visible visible
       :arrow {:color mainNeutral}}
      [:% n/View
       {:style {:backgroundColor mainNeutral
                :borderRadius 5
                :padding 10
                :minWidth 250}}
       [:% n/Text
        {:style [base-font/fontText
                 {:textAlign "left"
                  :color mainBackground}]}
        (:? (k/is-empty? errorText)
            "tag: system/unknown_error"
            errorText)]]]]
    [:% n/Padding {:style {:flex 1}}]
    [:% ui-button/Button
     {:design design
      :variant errorVariant
      #_#_:style {:padding 5}
      :onPress onClose
      :text [:% n/Icon
             {:key "close"
              :name "cross"
              :size 15}]}]]))

(def.js MODULE (!:module))
