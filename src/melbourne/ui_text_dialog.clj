(ns melbourne.ui-text-dialog
  (:use code.test)
  (:require [std.lang :as  l]
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
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-tooltip :as ui-tooltip]
             [melbourne.base-palette :as base-palette]
             [melbourne.base-theme :as base-theme]
             [melbourne.ui-text :as ui-text]
             [melbourne.slim-dialog :as slim-dialog]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js ConfirmDialog
  "creates a button with dialog confirmation"
  {:added "4.0"}
  [props]
  (var #{[(:= component "minor")
          (:= confirm {})
          design
          variant
          text
          
          onPressIn
          onPress
          style
          (:.. rprops)]} props)
  (var [visible setVisible] (r/local (fn:> false)))
  (var hostRef (r/ref))
  (var ButtonComponent (or (. {:minor  ui-text/ButtonMinor
                               :accent ui-text/ButtonAccent}
                              [component])
                           component))
  (return
   [:<>
    (r/% ButtonComponent
     #{[:refLink hostRef
        design variant text style
        :onPress (fn []
                   (when onPressIn (onPressIn))
                   (setVisible (not visible)))
        (:.. rprops)]})
    [:% slim-dialog/Dialog
     {:design (j/assignNew design {:invert true})
      :title (or (. confirm title)
                 "CONFIRM")
      :body  (or (. confirm body)
                 "Do you wish to proceed?")
      :modalProps {:transition "none"
                   :effect {:fade 0.1
                            :zoom 0.1}}
      :onSubmit (fn []
                  (setVisible false)
                  (onPress))
      :onCancel (fn:> (setVisible false))
      :visible visible}]]))

(def.js MODULE (!:module))

(comment
  (:= confirmTitle "CONFIRM")
  (:= confirmText "Do you wish to proceed?"))
