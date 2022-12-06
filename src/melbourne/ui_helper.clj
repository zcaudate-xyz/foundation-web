(ns melbourne.ui-helper
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.core :as j]
             [melbourne.ui-button :as ui-button]]
   :export [MODULE]})

(defn.js HelperControl
  "creates a pair of left/right helper control"
  {:added "0.1"}
  [#{[design
      variant
      theme
      style
      styleContainer
      leftDisabled
      rightDisabled
      children
      onLeft
      onRight
      (:= iconProps {})
      (:= leftProps {})
      (:= rightProps {})]}]
  (return
   [:% n/Row
    {:style styleContainer}
    [:% ui-button/Button
     #{[design variant theme
        :text [:% n/Icon
               #{[:key  "left"
                  :name "chevron-small-left"
                  :size 15
                  (:.. iconProps)]}]
        :disabled leftDisabled
        :indicatorParams
        {:pressing {:default {:duration 50}}}
        :onPress onLeft
        :style [{:paddingVertical 5}
                (:..  (j/arrayify style))]
        (:.. leftProps)]}]
    (or children
        [:% n/Padding {:style {:width 3}}])
    [:% ui-button/Button
     #{[design variant theme
        :text [:% n/Icon
               #{[:key "right"
                  :name "chevron-small-right"
                  :size 15
                  (:.. iconProps)]}]
        :disabled rightDisabled
        :indicatorParams
        {:pressing {:default {:duration 50}}}
        :onPress onRight
        :style [{:paddingVertical 5}
                (:..  (j/arrayify style))]
        (:.. rightProps)]}]]))

(def.js MODULE (!:module))
