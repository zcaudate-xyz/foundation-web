(ns pune.ui-notify-alerts-test
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react :as r]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [pune.ui-notify-alerts :as ui-notify-alerts]
             [melbourne.ui-text :as ui-text]
             [xt.event.base-box :as event-box]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

^{:refer pune.ui-notify-alerts/NotifyAlerts :added "0.1"}
(fact "notify alerts"

  (defglobal.js NotifySource
    (event-box/make-box {:alert []}))
  
  (defn.js NotifyAlertsDemo
    []
    (var [current setCurrent] (r/local true))
    (return
     (n/EnclosedCode 
{:label "pune.ui-notify-alerts/NotifyAlerts"} 
[:% n/Row
       {:style {:marginTop 30}}
       [:% ui-text/ButtonAccent
        {:text "Make Alert"
         :onPress (fn []
                    (event-box/append-data -/NotifySource
                                           ["alert"]
                                           {:title "Hello"
                                            :body "Hello World"}))}]
       [:% ui-notify-alerts/NotifyAlerts
        #{[:notify {:source -/NotifySource}]}]])))
  
  (def.js MODULE (!:module)))
