(ns pune.ui-market-live
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [js.react-native.animate :as a]
             [js.react-native.physical-base :as ui-base]
             [melbourne.ui-text :as ui-text]
             [melbourne.slim :as slim]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-section :as ui-section]
             [pune.common.data-market :as base-market]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js live-priority-rate
  "gets the priority rate"
  {:added "0.1"}
  [market
   allotment 
   prediction]
  (var rate-fn
       (fn [pair]
         (var [pos vol] pair)
         (return [(base-market/position-to-rate prediction allotment pos)
                  vol])))
  (var #{ask bid} (j/assign {:ask []
                             :bid []}
                            market))
  (var [buy sell] (:? (== prediction "yes")
                      [bid ask]
                      [ask bid]))
  (return {:buy  (k/arr-reverse (k/arr-map sell rate-fn))
           :sell (k/arr-reverse (k/arr-map buy rate-fn))}))

(def.js ORDER_IMPL
  {:type "v"
   :body [{:type "h"
           :body [{:type "p"
                   :variant {:fg {:key "neutral"}}
                   :template ["id"]}]}]})

(defn.js MarketLiveOrder
  [#{design
     orderId
     orderFn
     orderLookup
     amount}]
  (var [changed setChanged] (r/local false))
  (var [prev  setPrev]      (r/local amount))
  (var changing (a/useBinaryIndicator changed))
  (r/watch [amount]
    (when (not= prev amount)
      (setChanged true)
      (j/setTimeout (fn []
                      (setChanged false))
                    600)))
  (return
   [:% ui-base/Box
    {:indicators #{changing}
     :transformations
     {:changing (fn [v]
                  (return
                   {:style {:opacity (- 1 (* 0.7 v))}}))}}
    [:% ui-text/ButtonAccent
     {:design design
      :text  (+ "" amount)
      :onPress  (fn []
                  (when orderFn
                    (orderFn orderId
                             orderLookup)))
      :style {:paddingVertical 0
              :paddingHorizontal 0
              :marginHorizontal 2
              :marginVertical 2
              :borderWidth 0
              :width 40
              :textAlign "center"}}]]))

(defn.js MarketLiveRow
  "market live row"
  {:added "0.1"}
  [#{design
     control
     priority
     lookup
     rate
     orderFn}]
  (var #{fraction
         prediction
         decimal} control)
  (return
   [:% n/Row
    {:style {:alignItems "center"
             :marginHorizontal 5
             :marginVertical 5}}
    [:% ui-static/Text
     {:design design
      :variant {:font "h6"}
      :style {:width 50}}
     (j/toFixed (* rate fraction) decimal)]
    [:% n/View
     {:style {:flex 1}}
     [:% n/Row
      {:style {:flexWrap "wrap"}}
      (j/map priority
             (fn [[order-id amount]]
               (return
                [:% -/MarketLiveOrder
                 {:design design
                  :amount amount
                  :orderId order-id
                  :orderFn orderFn
                  :orderLookup lookup
                  :key order-id}])))]]]))

(defn.js MarketLive
  "market live"
  {:added "0.1"}
  [#{[design
      market
      control
      orderFn
      published]}]
  (var #{[(:= allotment 100)
          (:= decimal 2)
          (:= trade "buy")
          (:= prediction "yes")
          rate
          setRate]} control)
  (var lookup   (k/arr-juxt published k/id-fn k/identity))
  (var fraction (j/pow 10 (- decimal)))
  (var priorities    (-/live-priority-rate market
                                           allotment
                                           prediction))
  (var lineFn
       (fn [[rate priority] i]
         (return
          [:% -/MarketLiveRow
           #{{:key rate}
             design
             control
             rate
             priority
             lookup
             orderFn}])))
  (return
   [:% n/View
    {:style {:padding 3
             :flex 1
             :overflow "auto"}}
    [:% n/View
     {:style {:minHeight 60
              :flexDirection "column-reverse"}}
     (j/map (j/reverse [(:.. (. priorities buy))])
            lineFn)]
    [:% ui-section/SectionSeparator
     {:design design
      :variant {:fg {:key "neutral"}}
      :style {:marginVertical 3}}]
    [:% n/View
     {:style {:minHeight 60
              :flexDirection "column"}}
     (j/map (. priorities sell)
            lineFn)]]))

(def.js MODULE (!:module))
