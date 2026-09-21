(ns melbourne.slim-content
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.helper-color :as c]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as xtl]
             [xt.lang.common-data :as xtd]
             [xt.lang.common-string :as text]
             [melbourne.ui-static :as ui-static]
             [melbourne.base-palette :as base-palette]]
   :export [MODULE]})

(defn-.js getProps
  [entry propsInput]
  (cond (xtl/is-function? propsInput)
        (return (propsInput entry))

        :else (return propsInput)))

(defn-.js getField
  [entry field]
  (cond (xtl/nil? field)
        (return nil)
        
        (xtl/is-function? field)
        (return (field entry))
        
        :else (return (xtd/get-in entry (xtd/arrayify field)))))

(defn.js ContentTitle
  "creates the content title"
  {:added "4.0"}
  [#{[design
      palette
      entry
      style
      styleTitle
      styleSubtitle
      styleAction
      (:= titleProps {})
      (:= titleField (fn:>))
      (:= subtitleProps {})
      (:= subtitleField (fn:>))
      actionComponent
      (:= actionProps {})
      (:= actionField (fn:>))]}]
  (var title (-/getField entry titleField))
  (var subtitle (-/getField entry subtitleField))
  (return
   [:% n/View
    {:style style}
    (:? title
        [:% ui-static/Text
         #{[palette
            :design (j/assignNew design {:theme {:fg {:key "primary"}}})
            :style  [{:fontSize 14 :fontWeight "600"}
                     styleTitle]
            (:.. (-/getProps entry titleProps))]}
         title])
    (:? subtitle
        [:% ui-static/Text
         #{[palette design
            :style styleSubtitle
            (:.. (-/getProps entry subtitleProps))]}
         subtitle])
    (:? actionComponent
        [:% n/View {:style styleAction}
         (r/createElement actionComponent
                          (j/assign #{design entry palette}
                                    (-/getProps entry actionProps)))])]))

(defn.js ContentAvatar
  "creates the content avatar"
  {:added "4.0"}
  [#{[design
      palette
      entry
      style
      styleImage
      styleText
      (:= imageProps {})
      (:= imageField (fn:>))
      (:= textProps {})
      (:= textField (fn:> ""))]}]
  (var image (-/getField entry imageField))
  (:= palette (base-palette/getPalette design palette))
  (return
   [:% ui-static/Div
    {:design (j/assignNew
              design
              {:theme {:bg {:key "primary"
                            :mix "neutral"
                            :ratio 2}}})
     :style [{:height 50
              :width 50
              :borderRadius 25
              :justifyContent "center"
              :alignItems "center"}
             (:.. (j/arrayify style))]}
    (:? image
        [:% n/Image
         #{[:style [{:height 50 :width 50} styleImage]
            :source image
            (:.. (-/getProps entry imageProps))]}]
        [:% n/Text
         #{[:style [{:color (base-palette/getColor palette {:key "background"})}
                    (n/PlatformSelect {:web {:cursor "default"
                                             :userSelect "none"}})
                    (:.. (j/arrayify styleText))]
            (:.. (-/getProps entry textProps))]}
         (-/getField entry textField)])]))

(defn.js ContentCard
  "creates the content card"
  {:added "4.0"}
  [#{[design
      variant
      entry
      style
      styleLeft
      styleRight
      styleContent
      styleTitle
      leftComponent
      (:= leftProps (fn:>))
      titleComponent
      (:= titleProps (fn:>))
      rightComponent
      rightStyle
      (:= rightProps (fn:>))
      contentComponent
      (:= contentProps (fn:>))]}]
  (var palette (base-palette/designPalette design))
  (return
   [:% ui-static/Div
    #{design variant style}
    [:% n/Row
     (:? leftComponent
         [:% n/View {:style styleLeft}
          (r/createElement leftComponent
                           (j/assign #{design entry palette}
                                     (-/getProps entry leftProps)))])
     [:% n/View
      {:style {:flex 1}}
      [:% n/View
       {:style styleTitle}
       (:? titleComponent
           (r/createElement
            titleComponent
            (j/assign
             #{design palette entry}
             (-/getProps entry titleProps))))]
      [:% n/View
       {:style styleContent}
       (:? contentComponent
           (r/createElement
            contentComponent
            (j/assign
             #{design palette entry}
             (-/getProps entry contentProps))))]]
     (:? rightComponent
         [:% n/View {:style styleRight}
          (r/createElement rightComponent
                           (j/assign #{design entry palette}
                                     (-/getProps entry rightProps)))])]]))

(defn.js HeroCard
  "creates the hero card"
  {:added "4.0"}
  [#{[design
      palette
      entry
      style
      styleHeader
      styleContent
      headerComponent
      (:= headerProps (fn:>))
      contentComponent
      (:= contentProps (fn:>))]}]
  (:= palette (base-palette/getPalette design palette))
  (return
   [:% ui-static/Div
    {:palette palette
     :design design
     :style style}
    [:% n/View
     {:style styleHeader}
     (:? headerComponent
         (r/createElement
          headerComponent
          (j/assign
           #{design palette entry}
           (-/getProps entry headerProps))))]
    [:% n/View
     {:style styleContent}
     (:? contentComponent
         (r/createElement
          contentComponent
          (j/assign
           #{design palette entry}
           (-/getProps entry contentProps))))]]))

(def.js MODULE (!:module))

(comment
  
  )
