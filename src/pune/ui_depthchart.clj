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
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js get-depth-histogram
  "gets the histogram depth"
  {:added "0.1"}
  [domain lu step cmp]
  (var out [0])
  (var i (k/first domain))
  (while (cmp i (k/last domain))
    (var x (+ (k/last out)
              (or (. lu [i]) 0)))
    (x:arr-push out x)
    (:= i (+ i step)))
  (return out))

(defn.js get-depth-data
  "gets the histogram data"
  {:added "0.1"}
  [offers]
  (var #{buy sell} offers)
  (var buy-domain  (:? (k/is-empty? buy)
                         []
                         [(k/first (k/last buy))
                          (k/first (k/first buy))]))
  (var sell-domain (:? (k/is-empty? sell) []
                       [(k/first (k/last sell))
                        (k/first (k/first sell))]))
  (var max-steps  (k/max (:? (k/is-empty? buy-domain)
                             0
                             (- (k/second buy-domain)
                                (k/first buy-domain)))
                         (:? (k/is-empty? sell-domain)
                             0
                             (- (k/second sell-domain)
                                (k/first sell-domain)))
                         10))
  (var max-depth  (k/max (k/arr-foldl buy
                                      (fn:> [acc [_ vol]]
                                        (+ acc vol))
                                      0)
                         (k/arr-foldl sell
                                      (fn:> [acc [_ vol]]
                                        (+ acc vol))
                                      0)
                         100))
  
  (var buy-lu  (k/arr-juxt buy  k/first k/second))
  (var sell-lu (k/arr-juxt sell k/first k/second))
  (var buy-hist
       (:? (k/is-empty? buy)
           (k/arr-repeat 0 max-steps)
           (-/get-depth-histogram [(k/first buy-domain)
                                   (+ (k/first buy-domain)
                                      max-steps)]
                                  buy-lu 1 k/lte)))
  (var sell-hist
       (:? (k/is-empty? sell)
           (k/arr-repeat 0 max-steps)
           (-/get-depth-histogram [(k/last sell-domain)
                                   (- (k/last sell-domain)
                                      max-steps)]
                                  sell-lu -1 k/gte)))
  
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
        :values (k/arr-reverse sell-hist)]}]
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
