(ns pune.web.debug-connection
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[xt.lang.base-lib :as k]
             [js.core :as j]
             [js.react :as r :include [:fn]]
             [js.cell :as cl]
             [js.react.ext-cell :as cr]
             [js.react-native :as n :include [:fn]]
             [statslink.full.link-remote :as link-remote]
             [statslink.full.link-local :as link-local]
             [statslink.app.model-constant :as mc]
             ]
   :export [MODULE]})

(defn.js wrap-waiting
  "wraps a call with set waiting"
  {:added "0.1"}
  [f args setWaiting]
  (setWaiting true)
  (return (. (f (:.. args))
             (then  (fn [] (setWaiting false)))
             (catch (fn [] (setWaiting false))))))

(defn.js PingControl
  "creates a ping debug panel"
  {:added "0.1"}
  [#{context}]
  (var [current] (r/useNow 1000))
  (var out (cr/listenCellOutput mc/C_DEBUG_PING ["pending"] {} context))
  (var #{pending updated} out)
  (return
   [:% n/BaseIndicator
    {:label "PING"
     :waiting pending
     :onPress (fn []
                (cl/view-update mc/C_DEBUG_PING context))
     :styleText {:textAlign "center"
                 :width 150}
     :content   (:? (k/nil? updated)
                    "initialising"
                    (+ "" (j/floor (/ (- (k/now-ms) updated)
                                      1000))
                       " - "
                       (j/floor (/ updated 1000))))}]))

(defn.js LoginControl
  "creates a login panel"
  {:added "0.1"}
  [#{context login password}]
  (var [waiting setWaiting] (r/local false))
  (var cell  (cl/get-cell context))
  (var token (cr/listenCell mc/C_AUTH_TOKEN "success" {} context))
  (var dash  (cr/listenCell mc/C_AUTH_DASHBOARD "success" {} context))
  (var active (k/is-string? token))
  (var nickname (:? (k/is-empty? dash)
                    " - "
                    (. dash ["nickname"])))
  
  (return
   [:% n/BaseIndicator
    {:active active
     :label "USERNAME"
     :waiting waiting
     :content  (+ "" nickname)
     :onPress (fn []
                (when (not waiting)
                  (if active
                    (-/wrap-waiting link-remote/logout [cell token] setWaiting)
                    (. (-/wrap-waiting link-remote/login
                                       [cell {:login (or login "test00001")
                                              :password (or password "hello")}]
                                       setWaiting)
                       (then (fn:> []
                                   (link-local/connect-user   cell)
                                   (link-local/connect-ticker cell)))))))
     :styleText [{:textAlign "center"
                  :width 120
                  :backgroundColor "#555"}]}]))

(defn.js WSTickerControl
  "creates a ws ticker control"
  {:added "0.1"}
  [#{context}]
  (var [waiting setWaiting] (r/local false))
  (var cell (cl/get-cell context))
  (var summary (cr/listenCell mc/C_UTIL_SUMMARY "success" {} context))
  (var active (:? summary
                  (k/get-in summary ["streams" "ticker"])
                  false))
  (var [change setChange] (r/local nil))
  (var stable (and (or (k/nil? change)
                       (== active change))
                   (not waiting)))
  (return
   [:% n/ToggleIndicator
    {:label "WS TICKER"
     :active (:? stable active change)
     :waiting waiting 
     :onPress (fn []
                (when (not waiting)
                  (setChange (not active))
                  (if active
                    (-/wrap-waiting link-local/disconnect-ticker [cell] setWaiting)
                    (-/wrap-waiting link-local/connect-ticker [cell] setWaiting))))}]))

(defn.js WSUserControl
  "creates a ws user control"
  {:added "0.1"}
  [#{context}]
  (var [waiting setWaiting] (r/local false))
  (var cell (cl/get-cell context))
  (var summary (cr/listenCell mc/C_UTIL_SUMMARY "success" {} context))
  (var active (:? summary
                  (k/get-in summary ["streams" "user"])
                  false))
  (var [change setChange] (r/local nil))
  (var stable (and (or (k/nil? change)
                       (== active change))
                   (not waiting)))
  (return
   [:% n/ToggleIndicator
    {:label "WS USER"
     :active  (:? stable active change)
     :waiting waiting 
     :onPress (fn []
                (when (not waiting)
                  (setChange (not active))
                  (if active
                    (-/wrap-waiting link-local/disconnect-user [cell] setWaiting)
                    (-/wrap-waiting link-local/connect-user [cell] setWaiting))))}]))

(defn.js WSDeltaControl
  "creates a ws delta control"
  {:added "0.1"}
  [#{book context}]
  (var [waiting setWaiting] (r/local false))
  (var cell (cl/get-cell context))
  (var summary (cr/listenCell mc/C_UTIL_SUMMARY "success" {} context))
  (var active (:? summary
                  (k/get-in summary ["streams" "delta" book])
                  false))
  (var [change setChange] (r/local nil))
  (var stable (and (or (k/nil? change)
                       (== active change))
                   (not waiting)))
  (return
   [:% n/ToggleIndicator
    {:label   (+ "WS DELTA - " book)
     :active  (:? stable active change)
     :waiting waiting 
     :onPress (fn []
                (when (not waiting)
                  (setChange (not active))
                  (if active
                    (-/wrap-waiting link-local/disconnect-delta [cell book] setWaiting)
                    (-/wrap-waiting link-local/connect-delta [cell book] setWaiting))))}]))

(defn.js WSHarnessControl
  "creates a ws harness control"
  {:added "0.1"}
  [#{context accountIds}]
  (var [waiting setWaiting] (r/local false))
  (var cell (cl/get-cell context))
  (var summary (cr/listenCell mc/C_UTIL_SUMMARY "success" {} context))
  (var active (:? summary
                  (k/get-in summary ["streams" "harness"])
                  false))
  (var [change setChange] (r/local nil))
  (var stable (and (or (k/nil? change)
                       (== active change))
                   (not waiting)))
  (return
   [:% n/ToggleIndicator
    {:label "WS HARNESS"
     :active  (:? stable active change)
     :waiting waiting 
     :onPress (fn []
                (when (not waiting)
                  (setChange (not active))
                  (if active
                    (-/wrap-waiting link-local/disconnect-harness [cell] setWaiting)
                    (-/wrap-waiting link-local/connect-harness [cell accountIds] setWaiting))))}]))

(def.js MODULE (!:module))

