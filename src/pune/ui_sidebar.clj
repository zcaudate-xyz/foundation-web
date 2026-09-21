(ns pune.ui-sidebar
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[xt.lang.common-data :as xtd]
             [xt.lang.common-string :as xts]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]]
   :export [MODULE]})

(defn.js SidebarMenu
  [#{[design
      sectionKey
      setSectionKey
      (:= sections [])]}]
  (var itemProps
       (xtd/arr-map sections (fn:> {:styleContainer  {:width 52}
                              :transformations {:bg nil}})))
  (return
   [:% ui-static/Div
    {:design design
     :variant {:fg {:key    "primary"
                    :tone   "flatten"}}
     :style {:borderWidth 1
             :borderStyle "solid"
             :margin 0}}
    [:% ui-text/TabsAccent
     {:design design
      :variant {:bg {:key    "background"
                     :tone   "augment"}
                :active {:fg {:key    "background"
                              :tone   "augment"}
                         :bg {:key    "primary"
                              :tone "flatten"}}}
      :data  sections
      :value sectionKey
      :itemProps itemProps
      :setValue setSectionKey
      :styleContainer {:margin 0}
      :style {:fontSize 10
              :fontWeight "500"
              :paddingHorizontal 2
              :padding 0
              :textAlign "center"
              :paddingVertical 5
              :marginHorizontal 0
              :borderRadius 0}
      :format xts/to-uppercase}]]))

(defn.js Sidebar
  [#{design
     children
     sections
     sectionKey
     setSectionKey}]
  (return
   [:% ui-static/Div
    {:design design
     :variant {:bg {:key    "background"
                    :tone   "augment"}}
     :style {:width 158
             :marginHorizontal 1}}
    children
    [:% n/Fill]
    [:% -/SidebarMenu
     #{design
       sectionKey
       setSectionKey
       sections}]]))

(def.js MODULE (!:module))
