(ns pune.ui-market-ladder
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[xt.lang.spec-promise :as promise]
             [xt.lang.common-data :as xtd]
             [xt.lang.common-math :as xtm]
             [xt.lang.common-string :as xts]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-section :as ui-section]
             [pune.common.data-market :as base-market]]
   :export [MODULE]})

(defn.js MarketLadderText
  "market ladder text"
  {:added "0.1"}
  [#{[market
      allotment
      decimal
      prediction]}]
  (var frac (xtm/pow 10 (- decimal)))
  (var offers   (base-market/live-offers-rate market
                                              allotment
                                              prediction 6))
  (return
   [:% ui-static/ScrollView
    [:% n/Row
     [:% n/TextDisplay
      #{market}]
     [:% n/TextDisplay
      #{offers}]]]))

(defn.js MarketLadderRow
  "market ladder row"
  {:added "0.1"}
  [#{design
     control
     amount
     rate}]
  (var #{[(:= fraction 1)
          (:= prediction "yes")
          (:= decimal 0)]} control)
  (var [prevAmount setPrevAmount] (r/local amount))
  (var isMounted (r/useIsMounted))
  (r/watch [amount]
    (setTimeout (fn []
      (when (isMounted)
        (setPrevAmount amount))) 1000))
  
  (return
   [:% n/Row
    {:style {:marginHorizontal 5}}
    [:% ui-static/Text
     {:design design
      :variant (:? (not= amount prevAmount)
                   {:font "h6"
                    :fg {:key "background"}
                    :bg {:key (:? (== "no" prediction)
                                  "error"
                                  "primary")}}
                   {:font "h6"})}
     (xts/to-fixed (* rate fraction) decimal)]
    [:% n/Fill]
    [:% ui-static/Text
     {:design design}
     amount]]))

(defn.js MarketLadder
  "market ladder row"
  {:added "0.1"}
  [#{[design
      market
      control
      (:= steps 15)]}]
  (var #{[(:= allotment 100)
          (:= decimal 0)
          (:= trade "buy")
          (:= prediction "yes")
          (:= fraction 1)
          rate
          setRate]} control)
  (var offers   (base-market/live-offers-rate market
                                              allotment
                                              prediction
                                              steps))
  (var segment   (base-market/segment-price rate offers))
  (var lineFn
       (fn [[rate amount] i]
         (return
          [:% -/MarketLadderRow
           #{{:key rate}
             amount
             rate
             design
             control}])))
  (return
   [:% n/View
    {:style {:padding 3
             :flex 1}}
    [:% n/View
     {:style {:minHeight 60
              :flex 1
              :flexDirection "column-reverse"
              :overflow "hidden"}}
     (xtd/arr-map (xtd/arr-reverse [(:.. (. offers buy))])
            lineFn)]
    [:% ui-section/SectionSeparator
     {:design design
      :variant {:fg {:key "neutral"}}
      :style {:marginVertical 3}}]
    [:% n/View
     {:style {:minHeight 60
              :flex 1
              :flexDirection "column"
              :overflow "hidden"}}
     (xtd/arr-map (. offers sell)
            lineFn)]]))


(def.js MODULE (!:module))

(comment
  #_
  (defn.js MarketLadder
    "market ladder row"
    {:added "0.1"}
    [#{[design
        market
        rate
        (:= allotment 100)
        (:= decimal 0)
        (:= trade "buy")
        (:= prediction "yes")]}]
    (return
     [:% n/TextDisplay
      #{design
        market
        rate
        allotment
        decimal
        trade
        prediction}])))
