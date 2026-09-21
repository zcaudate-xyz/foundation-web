(ns pune.ui-metamask-contract-test
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]
            [pune.ui-metamask-manage-test :as manage-test]))

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
             [pune.ui-metamask-contract :as metamask-contract]
             [xt.lang.base-lib :as k]
             [xt.event.base-box :as event-box]]
   :import  [["@statstrade/interface/StatstradeGateway.json" :as GatewaySpec]]
   :export [MODULE]})

(defglobal.js METAMASK
  (event-box/make-box
   (fn:> {:accounts []
          :exists   false
          :chain-id nil
          :provider nil})))

(defglobal.js COIN_ADDRESS
  {1337 (@! (:gateway  (deref manage-test/+address+)))})

(defglobal.js COIN
  (event-box/make-box
   (fn:> {:amount nil})))

(defglobal.js COUNTER_ADDRESS
  {1337 (@! (or (:counter-local  (deref manage-test/+address+))
                "0x6e1E10d22C7221909eb94Fdb0A3dEEd9933F2e5F"))
   5    (@! (or (:counter-goerli (deref manage-test/+address+))
                "0x98E16C00fF8977A0E8Cab7Ea97D1b339673927F7"))})

(defglobal.js COUNTER
  (event-box/make-box
   (fn:> {:counter0 nil
          :counter1 nil})))

^{:refer pune.ui-metamask-contract/MetamaskContract :added "0.1"}
(fact "creates a metamask contract"

  (defn.js MetamaskContractDemo
    []
    (return
     (n/EnclosedCode 
{:label "pune.ui-metamask-contract/MetamaskContract"} 
[:% n/TextDisplay
       {:spec GatewaySpec}])))
 
  (def.js MODULE (!:module)))
