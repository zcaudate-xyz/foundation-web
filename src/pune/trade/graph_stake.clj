(ns pune.trade.graph-stake
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit   {:native {:suppress true}
                     :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [js.react-native.helper-color :as c]
             [js.lib.lw-charts :as lw]
             [xt.lang.base-lib :as k]
             [melbourne.base-palette :as base-palette]]
   :export [MODULE]})

(defn.js GraphStake
  [#{design
     dataPriceYes
     dataPriceNo
     height
     prediction
     decimal
     allotment
     fraction}]
  (var chartRef (r/ref))
  (var palette (base-palette/designPalette design))
  (r/watch [dataPriceYes
            dataPriceNo
            height
            design]
    (var chart
         (lw/createChart
          (r/curr chartRef)
          {:width (. (r/curr chartRef)
                     clientWidth)
           :height height}))
    (var handleResize
         (fn []
           (. chart (applyOptions
                     {:width (. (r/curr chartRef)
                                clientWidth)
                      :height (. (r/curr chartRef)
                                 clientHeight)}))))
    (. chart (timeScale) (fitContent))

    (var yesSeries (. chart (addLineSeries
                             {:color (c/toRGB
                                          (base-palette/getColorRaw
                                           palette
                                           "primary"
                                           "flatten"))
                              :title "Yes"
                              :priceScaleId "right"})))
    (. yesSeries (setData dataPriceYes))
    (. yesSeries (applyOptions
                    {:priceFormat
                     {:type "price"
                      :precision 1
                      :minMove 1}}))

    (var noSeries (. chart (addLineSeries
                             {:color (c/toRGB
                                          (base-palette/getColorRaw
                                           palette
                                           "error"
                                           "flatten"))
                              :title "No"
                                :priceScaleId "right"})))
    (. noSeries (setData dataPriceNo))
    (. noSeries (applyOptions
                    {:priceFormat
                     {:type "price"
                      :precision 1
                      :minMove 1}}))
    
    (. chart (applyOptions
              {:layout {:background {:color (base-palette/getColorRaw
                                             palette
                                             "background"
                                             "sharpen")}
                        :textColor (base-palette/getColorRaw
                                    palette
                                    "neutral"
                                    "sharpen")}
               :grid {:horzLines {:visible false}
                      :vertLines {:style 4
                                  :color (base-palette/getColorRaw
                                          palette
                                          "background"
                                          "flatten")}}
               :leftPriceScale
               {:visible false
                :scaleMargins {:top 0.3
                               :bottom 0.3}}
               
               :rightPriceScale
               {:visible false
                :scaleMargins {:top 0.02
                               :bottom 0}}
               :timeScale {:visible true
                           :timeVisible true
                           :secondsVisible true
                           :lockVisibleTimeRangeOnResize true}}))
    (. window (addEventListener "resize" handleResize))
    (return (fn []
              (. window (removeEventListener
                         "resize" handleResize))
              (. chart (remove)))))
  (return [:% n/View
           {:ref chartRef
            :style {:margin -5}}]))

(defn.js GraphStakePrice
  [#{design
     dataPrice
     height
     prediction
     decimal
     allotment
     fraction}]
  (var chartRef (r/ref))
  (var palette (base-palette/designPalette design))
  (r/watch [dataPrice
            height
            design]
    (var chart
         (lw/createChart
          (r/curr chartRef)
          {:width (. (r/curr chartRef)
                     clientWidth)
           :height height}))
    (var handleResize
         (fn []
           (. chart (applyOptions
                     {:width (. (r/curr chartRef)
                                clientWidth)
                      :height (. (r/curr chartRef)
                                 clientHeight)}))))
    (. chart (timeScale) (fitContent))
    (var priceSeries (. chart (addLineSeries
                               {:color (:? (== prediction "no")
                                           (c/toRGB
                                            (base-palette/getColorRaw
                                             palette
                                             "error"
                                             "flatten"))
                                           (c/toRGB
                                            (base-palette/getColorRaw
                                             palette
                                             "primary"
                                             "flatten")))
                                :title "Price"
                                :priceScaleId "right"
                                :autoscaleInfoProvider
                                (fn:>
                                  {:priceRange {:minValue (- (* 5 fraction))
                                                :maxValue (+ (* fraction allotment)
                                                             (* 5 fraction))}})})))
    (. priceSeries (setData dataPrice))
    (. priceSeries (applyOptions
                    {:priceFormat
                     {:type "price"
                      :precision decimal
                      :minMove fraction}}))
    
    (. chart (applyOptions
              {:layout {:background {:color (base-palette/getColorRaw
                                             palette
                                             "background"
                                             "sharpen")}
                        :textColor (base-palette/getColorRaw
                                    palette
                                    "neutral"
                                    "sharpen")}
               :grid {:horzLines {:visible false}
                      :vertLines {:style 4
                                  :color (base-palette/getColorRaw
                                          palette
                                          "background"
                                          "flatten")}}
               :leftPriceScale
               {:visible false
                :scaleMargins {:top 0.3
                               :bottom 0.3}}
               
               :rightPriceScale
               {:visible true
                :scaleMargins {:top 0.02
                               :bottom 0}}
               :timeScale {:visible true
                           :timeVisible true
                           :secondsVisible true
                           :lockVisibleTimeRangeOnResize true}}))
    (. window (addEventListener "resize" handleResize))
    (return (fn []
              (. window (removeEventListener
                         "resize" handleResize))
              (. chart (remove)))))
  (return #_[:% n/View
             {:style {:padding 10
                      :backgroundColor (base-palette/getColorRaw
                                        palette
                                        "background"
                                        "sharpen")}}]
          [:% n/View
           {:ref chartRef
            :style {:margin -5}}]))

(def.js MODULE (!:module))

(comment)
