(ns pune.ui-notify-alerts
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [
             [js.react :as r :include [:fn]]
             [js.react.ext-box :as ext-box]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-text :as ui-text]
             [melbourne.slim-dialog :as slim-dialog]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as xtl]
             [xt.lang.common-data :as xtd]
             [xt.lang.common-trace :as trace]
             [xt.lang.common-string :as text]]
   :export [MODULE]})

(defn.js NotifyAlerts
  "Exported App"
  {:added "0.1"}
  [props]
  (var #{design notify} props)
  (var [events
        setEvents] (ext-box/useBox
                    (. notify source)
                    (or (. notify path)
                        ["alert"])
                    (trace/meta:info)))
  (var [visible
        setVisible] (r/local))
  (var current (or (xtd/first events) {}))
  (r/watch [current]
    (when (xtd/not-empty? current)
      (j/future-delayed [100]
        (setVisible true))))
  (var #{title body submitText cancelText action} current)
  
  (return
   [:% slim-dialog/Dialog
    {:design (Object.assign {} design {:invert true})
     :title  title
     :body   body
     :submitProps {:text (or submitText "OK")}
     :helperProps {:cancelText (or cancelText "Cancel")
                   :cancelShow (xtl/not-nil? cancelText)}
     :modalProps  {:transition "none"
                   :effect {:fade 0.1
                            :zoom 0.1}}
     :onSubmit (fn []
                 (setVisible false)
                 (when action (action))
                 (setEvents (j/splice events 1)))
     :onCancel (fn []
                 (setVisible false)
                 (setEvents (j/splice events 1)))
     :visible visible}]))

(def.js MODULE (!:module))
