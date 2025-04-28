(ns pune.ui-market-live-test
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :playground/web-basic
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.core.style :as css]
             [js.react-native.helper-color :as c]
             [js.react :as r]
             [js.react-native :as n :include [:fn]]
             [js.react-native.animate :as a]
             [js.react-native.physical-base :as ui]
             [pune.ui-market-live :as market-live]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(def.js CHART
  (@! (std.json/read
       (h/sys:resource-content
        "pune/test_order_live.json"))))

(def.js CHART_ROW
  (@! (std.json/read
       (h/sys:resource-content
        "pune/test_order_live_row.json"))))

^{:refer pune.ui-market-live/live-priority-rate :added "0.1"}
(fact "gets the priority rate")

^{:refer pune.ui-market-live/MarketLiveOrder :added "0.1"}
(fact "market live order")

^{:refer pune.ui-market-live/MarketLiveRow :added "0.1"}
(fact "market live row"
  ^:hidden
  
  (defn.js MarketLiveRowDemo
    []
    (return
     (n/EnclosedCode 
{:label "pune.ui-market-live/MarketLiveRow"} 
[:% n/Row
       (r/% market-live/MarketLiveRow -/CHART_ROW)]))))

^{:refer pune.ui-market-live/MarketLive :added "0.1"}
(fact "market live"
  ^:hidden
  
  (defn.js MarketLiveDemo
    []
    (return
     (n/EnclosedCode 
{:label "pune.ui-market-live/MarketLive"} 
[:% n/Row
       {:style {:height 350}}
       (r/% market-live/MarketLive
            (j/assign {:orderFn
                       (fn [orderId orderLookup]
                         (return
                          (. (j/future-delayed [200]
                               (return (. orderLookup [orderId])))
                             (then (fn [data]
                                     (alert (k/json-encode data)))))))}
                      -/CHART))] 
[:% n/Row
       {:style {:height 400}}
       (r/% n/TextDisplay -/CHART)])))

  (def.js MODULE (!:module)))
