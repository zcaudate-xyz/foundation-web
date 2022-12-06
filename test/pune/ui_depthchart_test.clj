(ns pune.ui-depthchart-test
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {#_#_#_#_
   :runtime :basic
   :config  {:emit {:native {:suppress true}
                    :lang/jsx false}}
   :require [[js.core :as j]
             [js.core.style :as css]
             [js.react-native.helper-color :as c]
             [js.react :as r]
             [js.react-native :as n :include [:fn]]
             [js.react-native.animate :as a]
             [js.react-native.physical-base :as ui]
             [pune.ui-depthchart :as depthchart]
             #_[statslink.impl.base-market :as base-market]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(def.js CHART
  (@! (std.json/read
       (h/sys:resource-content
        "pune/test_order_depthchart.json"))))

^{:refer pune.ui-depthchart/get-depth-histogram :added "0.1"}
(fact "gets the histogram depth"
  ^:hidden

  (comment
    (!.js
     (var offers [[65 75] [64 60] [63 100]])
     (var buy-domain [63 70])
     (var buy-lu  (k/arr-juxt offers  k/first k/second))
     (depthchart/get-depth-histogram buy-domain
                                     buy-lu
                                     1
                                     k/lt))
    => [0 100 160 235 235 235 235 235]))

^{:refer pune.ui-depthchart/get-depth-data :added "0.1"}
(fact "gets the histogram data"
  ^:hidden

  (comment
    (!.js
     (depthchart/get-depth-data
      (base-market/live-offers-rate (. -/CHART market)
                                    (. -/CHART allotment)
                                    (. -/CHART prediction)
                                    20)))
    => {"buy_lu" {"67" 80, "66" 60},
        "buy_hist" [0 60 140 140 140 140 140 140 140 140 140 140],
        "buy_domain" [66 67],
        "sell_hist" [0 75 135 235 235 235 235 235 235 235 235 235],
        "max_depth" 235,
        "max_steps" 10,
        "sell_domain" [63 65],
        "sell_lu" {"63" 100, "65" 75, "64" 60}}))

^{:refer pune.ui-depthchart/MarketDepthChart :added "0.1"}
(fact "market ladder row"
  ^:hidden
  
  (defn.js MarketDepthChartDemo
    []
    (return
     [:% n/Enclosed
      {:label "pune.ui-depthchart/MarketDepthChart"
       :style {:height 500}}
      (r/% depthchart/MarketDepthChart -/CHART)
      [:% n/View {:style {:height 10}}]
      (r/% n/TextDisplay -/CHART)]))
  
  (def.js MODULE (!:module)))
