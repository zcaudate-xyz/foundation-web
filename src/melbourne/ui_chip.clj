(ns melbourne.ui-chip
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r]
             [js.react-native :as n :include [[:icon :entypo]]]
             [melbourne.ui-button :as ui-button]
             [melbourne.ui-static :as ui-static]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js Chip
  "constructs a error info demo"
  {:added "0.1"}
  [#{[(:= design {})
      (:= result {})
      variant
      text
      style
      onClose]}]
  (var __variant (j/assign {:bg {:key "primary"}
                            :fg {:key "background"}}
                           variant))
  (return
   [:% ui-static/Div
    #{design
      {:variant __variant
       :style [{:flexDirection "row"
                :margin 3
                :opacity 0.9
                :alignItems "center"
                :overflow "hidden"}
               (:.. (j/arrayify style))]}}
    [:% ui-static/Text
     #{design
       {:variant __variant
        :style {:padding 6
                :paddingRight (:? onClose 0)}}}
     text]
    (:? onClose
        [:<> 
         [:% ui-button/Button
          {:design design
           :variant (. __variant close)
           :style {:borderRadius 0
                   :marginLeft 5
                   :padding 5
                   :paddingLeft  4
                   :paddingRight 5}
           :onPress onClose
           :text [:% n/Icon
                  {:key "close"
                   :name "cross"
                   :size 12}]}]])]))

(def.js MODULE (!:module))

