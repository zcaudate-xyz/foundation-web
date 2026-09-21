(ns melbourne.addon-validation
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :playground/web-basic
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react-native.helper-color :as c]
             [js.react :as r]
             [js.react-native :as n]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js addonValidation
  "creates the form validation"
  {:added "0.1"}
  [#{[(:= design {})
      palette
      result
      styleText
      style]}]
  (var #{status message} result)
  (var [label setLabel]  (r/local (:? (== status "errored") message "")))
  (r/watch [status message]
    (when (== status "errored")
      (setLabel message)))

  (:= palette (base-palette/getPalette design palette))
  (var [fgColor bgColor] (base-theme/themeBase
                          palette
                          (j/assign
                           {:fg {:key "neutral"}
                            :bg {:key "primary"}}
                           (k/get-in design
                                     ["theme" "validation"]))))
  (var #{mainError
         mainPrimary
         mainNeutral
         mainBackground} palette)
  (return
   {:component n/View
    :inner [{:component n/Text
             :children [label]
             :style [{:color "white"
                      :textAlign "right"
                      :fontSize 12
                      :padding 2
                      :paddingHorizontal 5
                      :borderRadius 3
                      :backgroundColor mainBackground}
                     (:.. (j/arrayify styleText))]
             :transformations
             {:focusing
              (fn [focusing]
                (return {:style {:color (:? (< 0.5 focusing)
                                            mainPrimary
                                            mainBackground)
                                 :backgroundColor
                                 (c/mix [mainError
                                         mainBackground]
                                        focusing)}}))}}]
    :style [{:flexDirection "row-reverse"
             :overflow "hidden"
             :marginHorizontal 2
             :height 0}
            (:.. (j/arrayify style))]
    :transformations
    {:highlighted (fn [highlighted]
                    (return {:style {:height (:? (< 0.3 highlighted)
                                                 (* 18 highlighted)
                                                 0)}}))}}))

(def.js MODULE (!:module))
