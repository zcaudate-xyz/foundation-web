(ns melbourne.ui-dropdown
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-tooltip :as ui-tooltip]
             [js.react-native.ui-modal :as ui-modal]
             [melbourne.ui-group :as ui-group]
             [melbourne.ui-toggle-button :as ui-toggle-button]
             [melbourne.base-palette :as base-palette]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js DropdownIndexedModal
  "creates the modal"
  {:added "0.1"}
  [#{[design
      variant
      theme
      items
      setIndex
      index
      visible
      setVisible
      display
      displayType
      format
      styleMenu
      styleMenuItem
      itemTransformations
      modalProps
      hostRef]}]
  (var #{mainBackground
         mainNeutral
         mainColor} (base-palette/designPalette design))
  (var [dims setDims] (r/local {}))
  (var wrap (r/useIsMountedWrap))
  (r/watch [visible]
    (n/measureRef hostRef (wrap setDims)))
  
  (var listElem
       [:% ui-group/ListIndexed
        #{[:key "list"
           design
           variant
           theme
           items
           setIndex
           index
           :styleContainer [{:overflow "auto"
                             :flex 1}
                            (:.. (j/arrayify styleMenu))]
           :style   [{:marginVertical 0
                      :borderRadius 0
                      :fontSize 13
                      :width (. dims width)
                      #_#_:maxWidth (. dims width)
                      :fontWeight "400"}
                     (:.. (j/arrayify styleMenuItem))]
           :onPress (fn:> (setVisible false))
           :format format
           :transformations (or {:bg nil}
                                itemTransformations)]}])
  (var modelElem
       [:% ui-modal/Modal
        #{[visible
           :onClose (fn:> (setVisible false))
           :styleBackdrop {:backgroundColor mainNeutral}]}
        [:% n/View
         {:style {:width 300
                  :height 500}}
         listElem]])
  (var tooltipElem
       [:% ui-tooltip/Tooltip
        #{[:hostRef hostRef
           :visible visible
           :setVisible setVisible
           :position "bottom"
           :alignment "start"
           :arrow {:backdrop true,
                   :backdropStyle
                   {:backgroundColor mainNeutral, :opacity 0.1},
                   :baseHeight 0,
                   :color mainBackground,
                   :placement "none"}]}
        [:% n/View
         {:style {:backgroundColor mainBackground,
                  :borderRadius 3
                  :maxWidth 400}}
         listElem]])
  (return
   (:? (== "screen" displayType)
       modelElem
       tooltipElem)))

(defn.js DropdownIndexed
  "creates a horizontal check"
  {:added "0.1"}
  ([#{[design
       variant
       variantModal
       theme
       index
       setIndex
       items
       displayType
       style
       styleContainer
       styleText
       styleMenu
       styleMenuItem
       itemTransformations
       (:= format k/identity)
       (:.. rprops)]}]
   (var [visible setVisible] (r/local (fn:> false)))
   (var hostRef (r/ref))
   (return
    [:% n/View
     {:style styleContainer}
     [:% ui-toggle-button/ToggleButton
      #{[design
         theme
         :variant (j/assign {:bg   {:key "background"
                                    :mix "primary"
                                    :ratio 1}
                             :hovered {:bg {:raw 1}}}
                            variant)
         :refLink hostRef
         :selected visible
         :onPress (fn:> (setVisible (not visible)))
         :text [:% n/Row
                {:key "text"
                 :style {:width "100%"
                         :alignItems "center"
                         :justifyContent "center"}}
                [:% n/Text (format (. items [index]) index)]
                [:% n/Fill {:style {:minWidth 10}}]
                [:% n/Icon
                 {:name "chevron-down"}]]
         :style [{:marginVertical 0
                  :borderRadius 0
                  :paddingVertical 6
                  :alignItems "center"
                  :justifyContent "center"
                  :fontSize 13
                  :fontWeight "400"}
                 (:.. (j/arrayify style))]
         :transformations {:bg nil}
         (:.. rprops)]}]
     [:% -/DropdownIndexedModal
      #{[design
         :variant (or variantModal variant)
         theme
         items
         setIndex
         index
         visible
         setVisible
         format
         displayType
         styleMenu
         styleMenuItem
         itemTransformations
         hostRef]}]])))

(defn.js Dropdown
  "creates a horizontal check"
  {:added "0.1"}
  ([props]
   (var #{[data
           valueFn
           value
           setValue
           (:.. rprops)]} props)
   
   (var #{setIndex
          items
          index} (r/convertIndex #{data
                                   valueFn
                                   value
                                   setValue}))
   (r/watch [value index data]
     (when (and (k/is-empty? value)
                (k/not-nil? index)
                (k/not-empty? data))
       (setValue ((or valueFn k/identity)
                  (. data [index])))))
   (return [:% -/DropdownIndexed
            #{[setIndex
               items
               index
               (:.. rprops)]}])))

(def.js MODULE (!:module))
