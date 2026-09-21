(ns pune.ui-console
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [xt.lang.base-lib :as k]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-text :as ui-text]]
   :export [MODULE]})

(def.js ConsoleTabStyle
  {:padding 2
   :paddingHorizontal 15
   :fontSize 12
   :borderWidth 1
   :borderStyle "solid"})

(defn.js Console
  "creates the console"
  {:added "0.1"}
  [#{[design
      variant
      style
      screens
      current
      setCurrent
      onClose
      (:.. rprops)]}]
  (var data (k/sort (k/obj-keys screens)))
  (var target (or (k/get-key screens current)
                  (k/get-key screens (k/first data))))
  (return
   [:% ui-static/Div
    {:design design
     :style [{:flex 1}
             (:.. (j/arrayify style))]}
    [:% ui-static/Div
     {:design design
      :style {:flexDirection "row"}
      :variant {:bg {:key "background"
                     :tone "sharpen"}}}
     [:% ui-text/ButtonAccent
      {:design design
       :style {:padding 2}
       :text "X"
       :onPress onClose}]
     [:% ui-text/TabsMinor
      #{[data
         :design design
         :style -/ConsoleTabStyle
         :value current
         :transformations {:bg nil}
         :setValue setCurrent]}]]
    (n/displayTarget target)]))

(def.js MODULE (!:module))
