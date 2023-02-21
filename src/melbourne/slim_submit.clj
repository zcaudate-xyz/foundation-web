(ns melbourne.slim-submit
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
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [melbourne.ui-button :as ui-button]
             [melbourne.ui-text :as ui-text]
             [melbourne.slim-error :as slim-error]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-font :as base-font]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js SubmitButton
  "constructs a submit button"
  {:added "0.1"}
  [#{[design
      variant
      component
      disabled
      reset
      resetText
      waiting
      waitingText
      text
      style
      (:.. rprops)]}]
  (var #{mainBackground
         mainPrimary} (base-palette/designPalette design))
  (return
   (r/% (or component
            ui-text/ButtonMinor)
        #{[design variant
           :disabled (or disabled waiting)
           :style  [#_base-font/fontH6
                    {:textAlign "center"
                     :minWidth 100}
                    (:.. (j/arrayify style))]
           :text   (:? reset
                       (or resetText "BACK")
                       (:? waiting
                           (or waitingText
                               [:% n/ActivityIndicator
                                {:key "busy"
                                 :color mainPrimary
                                 :size 12}])
                           (or text "SUBMIT")))
           (:.. rprops)]})))

(defn.js SubmitLine
  "creates a submit line (with button and error)"
  {:added "4.0"}
  [props]
  (var #{[design
          mini
          variant
          form
          styleContainer
          styleSubmit
          submitText
          
          (:= errorProps {})
          result
          errored
          onAction
          onActionPress
          onActionReset
          (:= onActionCheck (fn:> true))
          children
          disabled
          (:.. rprops)]} props)
  (var #{row} errorProps)
  (var errorElem (:? errored
                     [:% slim-error/ErrorInfo
                      #{[:design design
                         :style (:? (or (not row)
                                        mini)
                                    {}
                                    {:flex 1})
                         :result result
                         :onClose onActionReset
                         (:.. errorProps)]}]))
  (:= (!:G CHECK) onActionCheck)
  (return
   [:% n/View
    {:style {:marginTop 3}}
    (:? (or (not row)
            mini)
        errorElem)
    [:% n/Row
     {:style [(:? mini
                  {:flexDirection "column"}
                  {:flexDirection "row-reverse"})
              (:.. (j/arrayify styleContainer))]}
     (:? (not errored)
         [:% -/SubmitButton
          #{[design
             variant
             :disabled (or disabled
                           (not (onActionCheck)))
             :reset errored
             :style styleSubmit
             :onPress onActionPress
             :text submitText
             (:.. rprops)]}])
     children
     (:? (and errored
              (and row (not mini)))
         [:<>
          [:% n/Padding {:style {:width 10}}]
          errorElem])]]))

(defn.js SubmitLineHelpers
  "creates the helper (cancel and clear) buttons"
  {:added "4.0"}
  [#{[design
      errored
      clearText
      clearStyle
      onClear
      (:= clearShow  false)
      (:= clearProps {})
      
      cancelText
      cancelStyle
      onCancel
      (:= cancelShow false)
      (:= cancelProps {})]}]
  (return
   [:<>
    (:? (and clearShow (not errored))
        [:<>
         [:% n/Padding {:style {:width 10}}]
         [:% ui-text/ButtonMinor
          #{[design
             :variant (j/assign
                       {:bg {:key "background"}, :fg {:key "neutral"}}
                       (k/get-in design ["variant" "clear"]))
             :style [base-font/fontH6
                     (:.. (j/arrayify clearStyle))]
             :text (or clearText "Clear")
             :onPress onClear
             (:.. clearProps)]}]])
    (:? (and cancelShow (not errored))
        [:<>
         [:% n/Padding {:style {:width 10}}]
         [:% ui-text/ButtonAccent
          #{[design
             :variant (k/get-in
                        design
                        ["variant" "cancel"])
             :style [base-font/fontH6
                     (:.. (j/arrayify cancelStyle))]
             :text (or cancelText "Cancel")
             :onPress onCancel
             (:.. cancelProps)]}]])]))

(defn.js SubmitLineActions
  "creates a submit line (with clear, cancel button and errors)"
  {:added "4.0"}
  [#{[design
      mini
      variant
      form
      errored
      onActionReset
      
      clearText
      clearStyle
      onClear
      clearShow
      clearProps
      cancelText
      cancelStyle
      onCancel
      cancelShow
      cancelProps
      
      children
      (:.. rprops)]}]
  (return
   [:% -/SubmitLine
    #{[design
       variant
       errored
       onActionReset
       (:.. rprops)]}
    (:? (not mini)
        [:% -/SubmitLineHelpers
         #{design
           errored
           clearText
           clearStyle
           {:onClear (or onClear onActionReset)}
           clearShow
           clearProps
           cancelText
           cancelStyle
           onCancel
           cancelShow
           cancelProps}])
    children]))

(defn.js useSubmitField
  "use submit form compatible with the submit controls"
  {:added "4.0"}
  [#{[form
      meta
      field
      explicit
      (:= keep true)
      onCheck
      onResult
      onSubmit
      onError
      onSuccess
      (:.. rprops)]}]  
  (var #{waiting
         setWaiting
         onAction
         errored
         isMounted
         result
         setResult} (r/useSubmitResult #{[onResult
                                          onSubmit
                                          onError
                                          onSuccess
                                          (:.. rprops)]}))
  (var #{onActionReset
         onActionCheck} (ext-form/useSubmitField
                         #{form
                           field
                           setResult
                           explicit
                           keep
                           isMounted
                           onCheck
                           {:meta (j/assign {:slim/type "submit_field"
                                             :fn/type   "submit"
                                             :validation/changes true}
                                            meta)}}))
  (var onActionPress (:? errored onActionReset onAction))
  (return #{onAction
            onActionReset
            onActionCheck
            onActionPress
            errored
            waiting
            setWaiting
            result
            setResult}))

(defn.js useSubmitForm
  "use submit field compatible with the submit controls"
  {:added "4.0"}
  [#{[form
      meta
      explicit
      (:= keep true)
      onCheck
      onResult
      onSubmit
      onSuccess
      onError
      (:.. rprops)]}]
  (var #{waiting
         setWaiting
         onAction
         errored
         isMounted
         result
         setResult} (r/useSubmitResult #{[onResult
                                          onSubmit
                                          onError
                                          onSuccess
                                          (:.. rprops)]}))
  (var #{onActionReset
         onActionCheck} (ext-form/useSubmitForm
                         #{form
                           setResult
                           explicit
                           keep
                           onCheck
                           isMounted
                           {:meta (j/assign {:slim/type "submit_form"
                                             :fn/type   "submit"}
                                            meta)}}))
  (var onActionPress (:? errored
                         onActionReset
                         (:? (onActionCheck)
                             onAction)))
  (return #{onAction
            onActionReset
            onActionCheck
            onActionPress
            errored
            waiting
            setWaiting
            result
            setResult}))

(def.js MODULE (!:module))
