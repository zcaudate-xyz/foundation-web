(ns pune.ui-metamask-manage-test
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]
            [rt.solidity :as s]
            [rt.solidity.compile-solc :as solc]
            [rt.solidity.compile-deploy :as deploy]
            [statspay.common.env-ganache :as env]
            [statspay.contract.gateway :as gateway]
            [statspay.contract.room-erc20 :as room-erc20]
            [statspay.contract.series-erc20 :as series-erc20]
            [js.lib.eth-bench :as eth-bench]
            [std.make]))

(l/script :js
  {:runtime :basic
   :require [[xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as xtl]
             [xt.lang.common-repl :as repl]
             [js.lib.eth-lib :as eth-lib :include [:fn]]
             [js.lib.eth-bench :as eth-bench]
             ]})

(fact:global
 {:setup    [(env/stop-ganache-server)
             (Thread/sleep 500)
             (env/start-ganache-server)
             (Thread/sleep 500)
             (l/rt:restart)
             (l/rt:scaffold :js)]
  :teardown [(l/rt:stop)]})

(defonce __init__
  (do (l/rt:restart)
      (l/rt:scaffold :js)))

(defonce +address+
  (atom {}))

(def +gateway+
  (solc/create-module-entry
   (l/rt 'pune.ui-metamask-manage-test :js)
   statspay.contract.gateway/+default-contract+))

(def +room-erc20+
  (solc/create-module-entry
   (l/rt 'pune.ui-metamask-manage-test :js)
   statspay.contract.room-erc20/+default-contract+))

(def +series-erc20+
  (solc/create-module-entry
   (l/rt 'pune.ui-metamask-manage-test :js)
   statspay.contract.series-erc20/+default-contract+
   true))

(defn deploy
  [url private-key contract key]
  (let [{:strs [contractAddress]}
        (solc/compile-rt-eval
         (l/rt 'pune.ui-metamask-manage-test :js)
         (h/$ (. (~`eth-bench/contract-deploy
                  ~url
                  ~private-key
                  ~(:abi contract)
                  ~(:bytecode contract)
                  ~(:args contract)
                  {})
                 (catch xtl/identity))))]
    (swap! +address+ assoc key contractAddress)
    contractAddress))

(def +gateway-deployed+
  (deploy (str "http://127.0.0.1:" env/+default-port+)
          (last env/+default-private-keys+)
          +gateway+
          :gateway))

(def +room-erc20-deployed+
  (deploy (str "http://127.0.0.1:" env/+default-port+)
          (last env/+default-private-keys+)
          +room-erc20+
          :room-erc20))

(def +series-erc20-deployed+
  (deploy (str "http://127.0.0.1:" env/+default-port+)
          (last env/+default-private-keys+)
          +series-erc20+
          :series-erc20))





(comment

  (j/<!
   (eth-bench/contract-run
    (@! (str "http://127.0.0.1:" env/+default-port+))
    (@! (last env/+default-private-keys+))
    (@! (:erc20-source-local (deref +address+)))
    (@! (:abi +erc20-contract-source+))
    "transfer"
    ["0xf4f4B19C7216a7b94f5136E2DC9fc553Ac280cFd"
     "1000000"]
    {:gasLimit 10000000}))
  
  (j/<!
   (eth-bench/contract-run
    (@! (str "http://127.0.0.1:" env/+default-port+))
    (@! (last env/+default-private-keys+))
    (@! (:erc20-source-local (deref +address+)))
    (@! (:abi +erc20-contract-source+))
    "balanceOf"
    [(nth env/+default-addresses-raw+ 9)]
    {}))

  (j/<!
   (eth-bench/contract-run
    (@! (str "http://127.0.0.1:" env/+default-port+))
    (@! (last env/+default-private-keys+))
    (@! (:erc20-source-local (deref +address+)))
    (@! (:abi +erc20-contract-source+))
    "balanceOf"
    ["0xf4f4B19C7216a7b94f5136E2DC9fc553Ac280cFd"]
    {}))
  
  
  
  
  (j/<! (eth-bench/send-wei
         (@! (str "http://127.0.0.1:" env/+default-port+))
         (@! (last env/+default-private-keys+))
         "0xf4f4B19C7216a7b94f5136E2DC9fc553Ac280cFd"
         "1000000000000000000"
         ))
  (eth-lib/send-wei
   )

  (j/<! (eth-lib/getBalance
         (eth-lib/new-rpc-provider
          (@! (str "http://127.0.0.1:" env/+default-port+)))
         "0xf4f4B19C7216a7b94f5136E2DC9fc553Ac280cFd"))
  
  (def +counter-deployed+
    (deploy "LOCAL"
            (last env/+default-private-keys+)
            +counter-contract+))
  
  (get +counter-deployed+ "contractAddress")
  
  "0xcCEbB4d3B78733a0a3e1d57b7FEfab2310964fa1"
  "0x73347ae3a68f1a6F3cc02ee15ed3554C3751c51F"
  "0x503705084619Cb3050C4CaD67E071f6fBA3d84e5"
  "0x191edd48E2D7524d56a48e092b78fA12c1420cEb"
  "0x08510c6ee24861484dF5F2cD891561C906EaA26E"
  
  
  
  
  (get +erc20-deployed+ "contractAddress")
  "0x39298d9b6f31709c459008ce7ceEF5eCB1153b5d"
  
  (def +source-deployed+
    (deploy "LOCAL"
            +erc20-contract-source+
            (last env/+default-private-keys+)))
  (get +source-deployed+ "contractAddress")
  "0x5dE7e27745fe19a9F5Ae803cB27cA6Cab870CD84"
  
  (def +counter-deployed-GOERLI+
    (deploy "GOERLI"
            +counter-contract+
            (System/getenv "TEST_GOERLI_KEY")))
  
  (j/<!
   (web3.eth.getBalance (@! (System/getenv "TEST_GOERLI_ADDRESS")
                         "0x1111c0AaB6941781d81B8EE493A1A0538455E966")))
  
  (j/<!
   (eth/send-wei web3
                 (System/getenv "TEST_GOERLI_ADDRESS")
                 1000000000000000000
                 0
                 (@! (last env/+default-private-keys+))))

  (j/<!
   (eth/send-wei LOCAL
                 "0x1111c0AaB6941781d81B8EE493A1A0538455E966"
                 1000000000000000000
                 0
                 (@! (last env/+default-private-keys+))))
  
  
  (j/<!
   (eth/send-wei LOCAL
                 "0xf4f4B19C7216a7b94f5136E2DC9fc553Ac280cFd"
                 1000000000000000000
                 0
                 (@! (last env/+default-private-keys+))))
  
  
  (j/<!
   (eth/send-wei LOCAL
                 "0xEB6e3a852b2Dfd80bCeDD109251A42C9F15DE5e4"
                 1000000000000
                 0
                 (@! (last env/+default-private-keys+))))
  
  (first env/+default-addresses+)
  (nth env/+default-addresses-raw+ 2)
  (nth env/+default-addresses-raw+ 2)
  
  
  
  (def +counter-deployed+
    (j/<!
     (. (eth/contract-deploy-single
         web3
         (@! (:abi +counter-contract+))
         (@! (:bytecode +counter-contract+))
         (@! (System/getenv "TEST_GOERLI_KEY"))
         [])
        (catch xtl/identity))))
  
  (def +counter-deployed+
    (j/<!
     (. (eth/contract-deploy-single
         web3
         (@! (:abi +counter-contract+))
         (@! (:bytecode +counter-contract+))
         (@! (last env/+default-private-keys+))
         [])
        (catch xtl/identity))))
  
  (eth/get-past-events)

  (get +counter-deployed+ "contractAddress")
  => "0x9e1Afda502f420F063f019c0ccDf3e9ce775F01f"
  
  (def +erc20-deployed+
    (j/<!
     (eth/contract-deploy-single
      web3
      (@! (:abi +erc20-contract+))
      (@! (:bytecode +erc20-contract+))
      (@! (last env/+default-private-keys+))
      [])))
  
  (get +erc20-deployed+ "contractAddress")
  "0x0C1E5F6f00A212F98986F5B52b9FA4f411ed512d"
  "0x5ddCf4AfD5E53c3144e24D4aDf103021166171c2"
  => "0x53493D041B313a87dE7bbf16D6ac74F994E06d12"

  (j/<!
   (. (new eth/Contract (@! (:abi +erc20-contract+)))
      methods
      (balanceOf "0x1111c0aab6941781d81b8ee493a1a0538455e966")
      (call {:from "0x1111c0aab6941781d81b8ee493a1a0538455e966"})
      (catch xtl/identity)))
  )
