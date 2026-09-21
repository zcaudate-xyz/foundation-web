(ns pune.ui-depthchart
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react-native :as n :include [:fn :svg]]
             [js.react :as r :include [:fn]]
             [js.core :as j]
             [melbourne.base-palette :as base-palette]
             [pune.ui-sparkline :as ui-sparkline]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as xtl]
             [xt.lang.common-data :as xtd]]
   :export [MODULE]})

(defn.js get-depth-histogram
  "gets the histogram depth"
  {:added "0.1"}
  [domain lu step cmp]
  (var out [0])
  (var i (xtd/first domain))
  (while (cmp i (xtd/last domain))
    (var x (+ (xtd/last out)
              (or (. lu [i]) 0)))
    (xt/x:arr-push out x)
    (:= i (+ i step)))
  (return out))

(defn.js get-depth-data
  "gets the histogram data"
  {:added "0.1"}
  [offers]
  (var #{buy sell} offers)
  (var buy-domain  (:? (xtd/is-empty? buy)
                         []
                         [(xtd/first (xtd/last buy))
                          (xtd/first (xtd/first buy))]))
  (var sell-domain (:? (xtd/is-empty? sell) []
                       [(xtd/first (xtd/last sell))
                        (xtd/first (xtd/first sell))]))
  (var max-steps  (j/max (:? (xtd/is-empty? buy-domain)
                             0
                             (- (xtd/second buy-domain)
                                (xtd/first buy-domain)))
                         (:? (xtd/is-empty? sell-domain)
                             0
                             (- (xtd/second sell-domain)
                                (xtd/first sell-domain)))
                         10))
  (var max-depth  (j/max (xtd/arr-foldl buy
                                      (fn:> [acc [_ vol]]
                                        (+ acc vol))
                                      0)
                         (xtd/arr-foldl sell
                                      (fn:> [acc [_ vol]]
                                        (+ acc vol))
                                      0)
                         100))
  
  (var buy-lu  (xtd/arr-juxt buy  xtd/first xtd/second))
  (var sell-lu (xtd/arr-juxt sell xtd/first xtd/second))
  (var buy-hist
       (:? (xtd/is-empty? buy)
           (xtd/arr-repeat 0 max-steps)
           (-/get-depth-histogram [(xtd/first buy-domain)
                                   (+ (xtd/first buy-domain)
                                      max-steps)]
                                  buy-lu 1 xtl/lte)))
  (var sell-hist
       (:? (xtd/is-empty? sell)
           (xtd/arr-repeat 0 max-steps)
           (-/get-depth-histogram [(xtd/last sell-domain)
                                   (- (xtd/last sell-domain)
                                      max-steps)]
                                  sell-lu -1 xtl/gte)))
  
  (return #{buy-domain sell-domain max-depth max-steps buy-lu sell-lu
            buy-hist sell-hist}))

(defn.js MarketDepthChart
  "market ladder row"
  {:added "0.1"}
  [#{[design
      offers
      #_market
      control]}]
  (var #{[(:= allotment 100)
          (:= decimal 0)
          (:= trade "buy")
          (:= prediction "yes")
          fraction]} control)
  
  #_
  (:= offers (or offers (base-market/live-offers-rate market
                                                      allotment
                                                      prediction
                                                      20)))
  (var m (-/get-depth-data offers))
  (var #{max-depth buy-hist sell-hist} m)
  (return
   [:% n/Row
    [:% ui-sparkline/Sparkline
     #{[:design design
        :variant {:bg {:key (:? (== prediction "yes") "primary" "error")
                       :mix "background"
                       :ratio 3}
                  :fg {:key "neutral"}}
        :style {:paddingVertical 4
                  :paddingLeft 2}
        :pathStyle {:strokeWidth 1}
        :height 12
        :width  55
        :maxValue max-depth
        :minValue 1
        :values (xtd/arr-reverse sell-hist)]}]
    [:% ui-sparkline/Sparkline
     #{[:design design
        :variant {:bg {:key "neutral"
                       :mix "background"
                       :ratio 3}
                  :fg {:key "neutral"}}
        :style {:paddingVertical 4
                :paddingRight 2}
        :pathStyle {:strokeWidth 1}
        :height 12
        :width  50
        :maxValue max-depth
        :minValue 1
        :values buy-hist]}]]))

(def.js MODULE (!:module))
