(ns pune.web.debug-asset-test
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react-native :as n :include [:fn]]
             [js.react.ext-cell :as cr]
             [statslink.app.model-constant :as mc]
             #_[statslink.app.model-trade :as model-trade]
             [pune.web.debug-asset :as debug-asset]]
   :export [MODULE]})

(comment
  ^{:refer pune.web.debug-asset/AssetRawLabel :added "0.1"}
  (fact "constructs an asset raw label"
    ^:hidden
    
    (defn.js AssetRawLabelDemo
      []
      (return
       (n/EnclosedCode 
{:label "pune.web.debug-asset/AssetRawLabel"} 
[:% n/Row
           [:% n/Row
            {:style {:flex 1
                     :backgroundColor "#eee"
                     :padding 10}}
            [:% debug-asset/AssetRawLabel
             {:design {:type "light"}
              :currencyId "STC"
              :currency {:balance 100 :escrow 10}
              :activeEscrow nil
              :decimal 2}]]
           [:% n/Row
            {:style {:flex 1
                     :backgroundColor "#333"
                     :padding 10}}
            [:% debug-asset/AssetRawLabel
             {:design {:type "dark"}
              :currencyId "STC"
              :currency {:balance 100 :escrow 10}
              :activeEscrow nil
              :decimal 2}]]] 
#_))))

  ^{:refer pune.web.debug-asset/AssetControlLabel :added "0.1"}
  (fact "constructs an asset control label"
    ^:hidden
    
    (defn.js AssetControlLabelDemo
      []
      (var active {"DOGE" 20 "STC" 10})
      (var assets {"DOGE"   {:balance 200
                            :escrow  30}
                   "STC" {:balance 100
                            :escrow  30}})
      (return
       (n/EnclosedCode 
{:label "pune.web.debug-asset/AssetControlLabel"} 
[:% n/Row
           [:% n/View
            {:style {:flex 1
                     :backgroundColor "#eee"
                     :padding 10}}
            [:% debug-asset/AssetControlLabel
             {:design {:type "light"}
              :currencyId "STC"
              :assets assets
              :active active}]
            [:% debug-asset/AssetControlLabel
             {:design {:type "light"}
              :currencyId "DOGE"
              :assets assets
              :active active}]]
           [:% n/View
            {:style {:flex 1
                     :backgroundColor "#333"
                     :padding 10}}
            [:% debug-asset/AssetControlLabel
             {:design {:type "dark"}
              :currencyId "STC"
              :assets assets
              :active active}]
            [:% debug-asset/AssetControlLabel
             {:design {:type "dark"}
              :currencyId "DOGE"
              :assets assets
              :active active}]]] 
#_))))

  ^{:refer pune.web.debug-asset/AssetControl :added "0.1"}
  (fact "creates an asset control"
    ^:hidden
    
    (defn.js AssetControlDemo
      []
      (var orders  (cr/listenCell mc/T_ACTIVE_ORDER "success" {}))
      (var assets  (cr/listenCell mc/C_ASSET_MAIN "success" {}))
      (var active  (model-trade/get-active-asset-escrow))
      (return
       (n/EnclosedCode 
{:label "debug-asset/AssetControl"} 
[:% n/Row
           [:% n/View
            {:style {:flex 1
                     :backgroundColor "#eee"
                     :padding 10}}
            [:% debug-asset/AssetControl
             {:design {:type "light"}
              :currencies ["STC" "USD" "DOGE" "BUSD" "SOL"]}]]
           [:% n/View
            {:style {:flex 1
                     :backgroundColor "#333"
                     :padding 10}}
            [:% debug-asset/AssetControl
             {:design {:type "dark"}
              :currencies ["STC" "USD" "DOGE" "BUSD" "SOL"]}]]] 
[:% n/TextDisplay
         {:content (+ (n/format-entry #{assets active})
                      "\n\n"
                      (n/format-entry orders))}] 
#_)))

    
    )
  )

(def.js MODULE (!:module))
