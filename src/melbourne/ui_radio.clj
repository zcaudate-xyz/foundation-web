(ns melbourne.ui-radio
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n]
             [js.react-native.ui-radio-box :as ui-radio-box]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.base-font :as base-font]
             [melbourne.ui-static :as ui-static]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js RadioBox
  "creates a horizontal radio"
  {:added "0.1"}
  [#{[design
      variant
      style
      theme
      (:.. rprops)]}]
  (var __variant (j/assign
                  {:fg   {:key "neutral"}
                   :bg   {:key "background"
                          :tone "darken"
                          :ratio 1}
                   :pressed {:bg {:key "primary"}}
                   :highlighted {:fg {:key "neutral"}
                                 :bg {:key "background"
                                      :tone "darken"
                                      :ratio 1}}
                   :active  {:fg {:key "background"}
                             :bg {:key "primary"}}}
                  variant))
  (var __style  (base-font/getFontStyle (or (. __variant font)
                                            "h6")))
  (var __theme  (j/assign (base-theme/themeUiInput
                           (base-palette/designPalette design)
                           __variant)
                          theme))
  (return
   [:% ui-radio-box/RadioBox
    #{[:theme __theme
       :style [{:padding 0}
               __style
               (:.. (j/arrayify style))]
       (:.. rprops)]}]))

(defn.js RadioGroupIndexed
  "creates a group of radio boxes"
  {:added "0.1"}
  ([#{[design
       variant
       theme
       items
       setIndex
       index
       onChange
       style
       styleText
       styleContainer
       (:= itemProps [])
       (:= format k/identity)]}]
   (var itemFn
        (fn [value i]
          (return [:% n/View
                   {:key   value
                    :style {:flexDirection "row"
                            :alignItems "center"
                            :padding 2}}
                   [:% -/RadioBox
                    #{[design
                       variant
                       theme
                       style
                       :selected (== index i)
                       :onPress (fn []
                                  (when (not= i index)
                                    (setIndex i)
                                   (if onChange (onChange i))))
                       (:.. (or (. itemProps [i])
                                {}))]}]
                   [:% ui-static/Text
                    #{design
                      {:variant (j/assign
                                 {:fg {:key "primary"}}
                                 (k/get-in design ["variant" "text"]))
                       :style styleText}}
                    (format value i)]])))
   (return [:% n/View
            {:style styleContainer}
            (j/map items itemFn)])))

(defn.js RadioGroup
  "creates a group of radio boxes"
  {:added "0.1"}
  ([#{[data
       valueFn
       value
       setValue
       (:.. rprops)]}]
   (let [#{setIndex
           items
           index} (r/convertIndex #{data
                                      valueFn
                                      value
                                      setValue})]
     (return [:% -/RadioGroupIndexed
              #{[setIndex
                 items
                 index
                 (:.. rprops)]}]))))

(def.js MODULE (!:module))

(comment
  )
