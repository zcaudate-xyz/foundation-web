(ns pune.ui-market-delta-test
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
             [pune.ui-market-delta :as market-delta]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

^{:refer pune.ui-market-delta/Delta :added "4.0"}
(fact "prepare highlighted indicators and chord"

  (defn.js DeltaDemo
    []
    (var [value setValue] (r/local 100))
    (return
     (n/EnclosedCode 
{:label "pune.ui-market-delta/Delta"} 
[:% n/Row
       [:% market-delta/Delta
        {:value value}]
       [:% n/Fill]
       [:% n/Row
        [:% n/Button
         {:title "-1"
          :style {:fontSize 10}
          :onPress (fn [] (setValue (- value 1 )))}]
        [:% n/Padding {:style {:width 10}}]
        [:% n/Button
         {:title "+1"
          :onPress (fn [] (setValue (+ value 1)))}]]] 
[:% n/Caption
       {:text (k/json-encode #{value})
        :style {:marginTop 10
                :zIndex -100}}])))

  (def.js MODULE (!:module)))


