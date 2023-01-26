(ns melbourne.slim-time
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
             [js.react.ext-form :as ext-form]
             [js.react-native :as n :include [:fn]]
             [xt.lang.base-lib :as k]
             [xt.lang.event-form :as event-form]
             [melbourne.base-font :as base-font]
             [melbourne.base-palette :as base-palette]
             [melbourne.slim-common :as slim-common]
             [melbourne.ui-input :as ui-input]
             [melbourne.ui-datetime :as ui-datetime]
             [melbourne.addon-validation :as addon-validation]]
   :export [MODULE]})

(defn.js FormTime
  "creates an Input"
  {:added "0.1"}
  [#{[design
      mini
      variant
      form
      meta
      label
      labelHide labelNone
      styleLabel
      field
      fieldProps
      minWidth
      hideValidation]}]
  (var #{value result} (ext-form/listenField form field
                                             (j/assign {:slim/type "time"
                                                        :fn/type   "field"}
                                                       meta)))
  (return
   [:% slim-common/FormEnclosed
    #{design mini
      {:variant (k/get-in design ["variant" "label"])}
      styleLabel
      label
      labelHide labelNone
      minWidth}
    [:% ui-input/Input
     #{[design variant
        :indicatorParams {:focusing {:default {:duration 100}}}
        :highlighted (== (. result ["status"])
                         "errored")
        :value (j/toString (:? (k/nil? value) "" value))
        :onFocus (fn []
                   (event-form/validate-field form field))
        :onChangeText (fn [v]
                        (event-form/set-field form field v)
                        (event-form/validate-field form field))
        :addons [(:? (not hideValidation)
                     (addon-validation/addonValidation
                      #{[result design
                         :style {:position "absolute"
                                 :right 0
                                 :top -5}]}))]
        :outlined false
        (:.. fieldProps)]}]]))

(defn.js FormDate
  "creates an Input"
  {:added "0.1"}
  [#{[design
      mini
      variant
      form
      meta
      label
      labelHide labelNone
      styleLabel
      field
      fieldProps
      minWidth
      hideValidation]}]
  (var #{value result} (ext-form/listenField form field
                                             (j/assign {:slim/type "time"
                                                        :fn/type   "field"}
                                                       meta)))
  (return
   [:% slim-common/FormEnclosed
    #{design mini
      {:variant (k/get-in design ["variant" "label"])}
      styleLabel
      label
      labelHide labelNone
      minWidth}
    [:% ui-input/Input
     #{[design variant
        :indicatorParams {:focusing {:default {:duration 100}}}
        :highlighted (== (. result ["status"])
                         "errored")
        :value (j/toString (:? (k/nil? value) "" value))
        :onFocus (fn []
                   (event-form/validate-field form field))
        :onChangeText (fn [v]
                        (event-form/set-field form field v)
                        (event-form/validate-field form field))
        :addons [(:? (not hideValidation)
                     (addon-validation/addonValidation
                      #{[result design
                         :style {:position "absolute"
                                 :right 0
                                 :top -5}]}))]
        :outlined false
        (:.. fieldProps)]}]]))

(defn.js FormDatetime
  "creates an Input"
  {:added "0.1"}
  [#{[design
      mini
      variant
      form
      meta
      label
      labelHide labelNone
      styleLabel
      field
      fieldProps
      minWidth
      hideValidation]}]
  (var #{value result} (ext-form/listenField form field
                                             (j/assign {:slim/type "time"
                                                        :fn/type   "field"}
                                                       meta)))
  (return
   [:% slim-common/FormEnclosed
    #{design mini
      {:variant (k/get-in design ["variant" "label"])}
      styleLabel
      label
      labelHide labelNone
      minWidth}
    [:% ui-input/Input
     #{[design variant
        :indicatorParams {:focusing {:default {:duration 100}}}
        :highlighted (== (. result ["status"])
                         "errored")
        :value (j/toString (:? (k/nil? value) "" value))
        :onFocus (fn []
                   (event-form/validate-field form field))
        :onChangeText (fn [v]
                        (event-form/set-field form field v)
                        (event-form/validate-field form field))
        :addons [(:? (not hideValidation)
                     (addon-validation/addonValidation
                      #{[result design
                         :style {:position "absolute"
                                 :right 0
                                 :top -5}]}))]
        :outlined false
        (:.. fieldProps)]}]]))

(def.js MODULE (!:module))
