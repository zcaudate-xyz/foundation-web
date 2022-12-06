(ns pune.ui-notify-base-test
  (:use code.test)
  (:require [std.lang :as l]
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
             [pune.ui-notify-base :as ui-topnotify]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

^{:refer pune.ui-notify-base/getOutdated :added "0.1"}
(fact "gets outdated events")

^{:refer pune.ui-notify-base/useOutdated :added "0.1"}
(fact "evicts outdated events")

^{:refer pune.ui-notify-base/TopNotifyInner :added "0.1"}
(fact "creates a TopNotifyInner Component"
  ^:hidden
  
  (defn.js TopNotifyInnerDemo
    []
    (var [index setIndex] (r/local 0))
    (var [data setData] (r/local [{:id    "02"
                                   :topic "user.account/place"
                                   :title "Order Placed"
                                   :message "NBA-MVP-2022/S.CURRY @ Y 1.34"
                                   :detail {:id "001-order"}
                                   :time (k/now-ms)}
                                  {:id    "01"
                                   :topic "user.account/password-changed"
                                   :title "Password Changed"
                                   :message "user: test00001"
                                   :time (k/now-ms)}
                                  ]))
    (return
     (n/EnclosedCode 
{:label "pune.ui-notify-base/TopNotifyInner"
       :style {:backgroundColor "#eee"
               :padding 20}} 
[:% n/View
       [:% ui-topnotify/TopNotifyInner
        {:data []
         :design {:type "light"}}]] 
[:% n/View
       [:% n/Row
        {:style {:marginVertical 5}}]
       [:% ui-topnotify/TopNotifyInner
        {:data [{:id    "01"
                 :topic "user.account/password-changed"
                 :title "Password Changed"
                 :message "user: test00001"
                 :time (k/now-ms)}]
         :design {:type "light"}}]] 
[:% n/View
       [:% n/Row
        {:style {:marginVertical 5}}
        ]
       [:% ui-topnotify/TopNotifyInner
        #{index setIndex
          {:data  data
           :design {:type "light"}}}]]))))

^{:refer pune.ui-notify-base/TopNotify :added "0.1"}
(fact "creates the top notify"
  ^:hidden
  
  (defn.js TopNotifyDemo
    []
    (var [index setIndex] (r/local 0))
    (var [data setData] (r/local [{:id    "02"
                                   :topic "user.account/place"
                                   :title "Order Placed"
                                   :message "NBA-MVP-2022/S.CURRY @ Y 1.34"
                                   :detail {:id "001-order"}
                                   :time (k/now-ms)}
                                  {:id    "01"
                                   :topic "user.account/password-changed"
                                   :title "Password Changed"
                                   :message "user: test00001"
                                   :time (k/now-ms)}
                                  ]))
    (return
     (n/EnclosedCode 
{:label "pune.ui-notify-base/TopNotify"
       :style {:backgroundColor "#333"
               :padding 20}} 
[:% n/Isolation
       [:% n/Row
        {:style {:marginVertical 5
                 :width 300
                 :height 80}}
        
        [:% ui-topnotify/TopNotify
         {:mini true
          :data [{:id    "01"
                  :topic "user.account/password-changed"
                  :title "Password Changed"
                  :message "user: test00001"
                  :time (k/now-ms)}]
          :design {:type "dark"}}]]] 
[:% n/Isolation
       [:% n/Row
        {:style {:marginVertical 5
                 :width 300
                 :height 80}}
        
        [:% ui-topnotify/TopNotify
         #{index setIndex
           {:mini true
            :data  data
            :design {:type "dark"}}}]]])))
  
  (def.js MODULE (!:module)))
