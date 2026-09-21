(ns pune.ui-console
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]
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
  (var data (. (xtd/obj-keys screens) (sort)))
  (var target (or (xt/x:get-key screens current)
                  (xt/x:get-key screens (xtd/first data))))
  (return
   [:% ui-static/Div
    {:design design
     :style [{:flex 1}
             (:.. (xtd/arrayify style))]}
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
