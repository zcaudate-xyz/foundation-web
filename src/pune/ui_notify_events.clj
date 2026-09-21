(ns pune.ui-notify-events
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.cell :as cl]
             [js.react :as r :include [:fn]]
             [js.react.ext-box :as ext-box]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-text :as ui-text]
             [pune.ui-notify-base :as base]
             [xt.lang.base-lib :as k]
             [xt.lang.common-string :as text]]
   :export [MODULE]})

(defn.js parseEvent
  [e pred]
  (var #{status body} e)
  (when (not= status "ok")
    (return [false]))
  (when (not (pred body))
    (return [false]))
(var #{msg-id
       op-tag
       op-level
       op-time
       message} body)
  (var t (k/now-ms))
  (var msg {:id (or msg-id (j/randomId 6))
            :title  (j/toUpperCase (text/tag-string (or op-tag "")))
            :message (or message (+ "" (new Date t)))
            ;;:sticky (== op-level "N")
            :time t})
  (return [true msg]))


(defn.js useUserEvents
  [handler event-key event-types]
  (r/init []
    (var callbackKey (+ event-key "/" (j/randomId 4)))
    (cl/add-raw-callback event-key
                         event-types
                         handler)
    (return (fn []
              (cl/remove-raw-callback event-key)))))

(defn.js NotifyEvents
  "Exported App"
  {:added "0.1"}
  [props]
  (var #{design mini notify} props)
  (var [events
        setEvents] (ext-box/useBox
                    (. notify source)
                    []
                    (k/meta:info)))
  (var eventsRef (r/useFollowRef events))
  (-/useUserEvents (fn [e]
                     
                     (var [ok msg] (-/parseEvent e (. notify filter)))
                     (when ok
                       (var #{id} msg)
                       (setEvents (j/assign {id msg} (r/curr eventsRef)))))
                   (. notify key)
                   (. notify types))
  (base/useOutdated
   #{events setEvents
     {:duration 5000}})
  (return
   [:% base/TopNotify
    #{design mini
      {:data    (j/values events)
       :onClose (fn:> (setEvents {}))}}]))

(def.js MODULE (!:module))
