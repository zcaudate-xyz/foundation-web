(ns pune.f04-market.form-market-test
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(comment
  (l/script :js
    {:runtime :websocket
     :config {:id :dev/web-main
              :bench false
              :emit {:native {:suppress true}
                     :lang/jsx false}
              :notify {:type :webpage :path "dev/notify"}}
     :require [[js.core :as j]
               [js.core.style :as css]
               [js.react :as r]
               [js.react-native :as n :include [:fn]]
               [statslink.app.data-summary :as data-summary]
               [statsnet.interface.db-history :as db-history]
               [melbourne.ui-group :as ui-group]
               [melbourne.ui-toggle-button :as ui-toggle-button]
               [pune.f04-market.form-market :as form-market]
               [xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as xtl]
             [xt.lang.common-data :as xtd]]
     :export [MODULE]})

  (def.js HISTORY
    (@! (read-string
         (h/sys:resource-content "pune/sample_history.edn"))))

  (defn.js Table
    [#{data
       columns}]
    (var itemFn
         (fn [e]
           (var #{item index} e)
           (return
            [:% n/Row
             (j/map columns
                    (fn:> [#{key}]
                      [:% n/Text
                       {:style {:width 80}}
                       (+ "" (. item [key]))]))])))
    (return [:% n/View
             [:% n/Row
              (j/map columns
                     (fn:> [#{title}]
                       [:% n/Text
                        {:style {:width 80}}
                        title]))]
             [:% n/FlatList
              {:style {}
               :data  data
               :keyExtractor xtl/identity
               :renderItem itemFn}]]))

  ^{:refer pune.f04-market.form-market/MarketGraph :adopt true :added "0.1"}
  (fact "market graph"
    ^:hidden
    
    (defn.js MarketGraphDemo
      []
      (var linear (db-history/get-linear -/HISTORY
                                         "30sec"
                                         nil
                                         (. (xtd/last (. -/HISTORY ["exit"]))
                                            ["t_end"])))
      (return
       (n/EnclosedCode 
{:label "pune.f04-market.form-market/MarketGraph"} 
[:% n/Row
         [:% n/View
          {:style {:backgroundColor "#eee"
                   :flex 1
                   :padding 20}}
          [:% -/Table
           {:data linear
            :columns [{:key "index"
                       :title "Index"}
                      {:key "p_low"
                       :title "Low"}
                      {:key "p_high"
                       :title "High"}
                      {:key "n_total"
                       :title "Total"}]}]]
         [:% n/View
          {:style {:backgroundColor "#333"
                   :flex 1
                   :padding 20}}]] 
[:% n/View
         {:style {:height 300}}
         [:% n/TextDisplay
          {:content (n/format-entry linear)}]])))
    
    (def.js MODULE (!:module))
    )
  )
