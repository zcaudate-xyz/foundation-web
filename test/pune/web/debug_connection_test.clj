(ns pune.web.debug-connection-test
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
   :require [[js.react.ext-cell :as cr]
             [js.react-native :as n :include [:fn]]
             [statslink.app.model-constant :as mc]
             [pune.web.debug-connection :as debug-connection]
             [xt.lang.base-lib :as k]
             ]
   :export [MODULE]})

^{:refer pune.web.debug-connection/wrap-waiting :added "0.1"}
(fact "wraps a waiting setter")

^{:refer pune.web.debug-connection/PingControl :added "0.1"}
(fact "creates a ping debug panel"
  ^:hidden
  
  (defn.js PingControlDemo
    []
    (var out (cr/listenCellOutput mc/C_DEBUG_PING ["pending"] {}))
    (return (n/EnclosedCode 
{:label "pune.web.debug-connection/PingControl"
              :style {:flex 1}} 
[:% debug-connection/PingControl] 
[:% n/TextDisplay
              {:content (n/format-obj out)}]))))

^{:refer pune.web.debug-connection/LoginControl :added "0.1"}
(fact "creates a login panel"
  ^:hidden
  
  (defn.js LoginControlDemo
    []
    (var token    (cr/listenCell mc/C_AUTH_TOKEN "success" {}))
    (var summary  (cr/listenCell mc/C_UTIL_SUMMARY "success" {}))
    (var dash     (cr/listenCell mc/C_AUTH_DASHBOARD "success" {}))
    (return (n/EnclosedCode 
{:label "pune.web.debug-connection/LoginControl"
              :style {:flex 1}} 
[:% debug-connection/LoginControl
              {:login "test00001"
               :password "hello"}] 
[:% n/TextDisplay
              {:content [(n/format-obj summary)
                         "\n\n"
                         (k/arr-join (or (k/split-long (or token ""))
                                         [])
                                     "\n")
                         "\n\n"
                         (n/format-obj dash)]}] 
#_))))

^{:refer pune.web.debug-connection/WSTickerControl :added "0.1"}
(fact "creates a ws ticker control"
  ^:hidden

  (defn.js WSTickerControlDemo
    []
    (var summary  (cr/listenCell mc/C_UTIL_SUMMARY "success" {}))
    (return (n/EnclosedCode 
{:label "pune.web.debug-connection/WSTickerControl"
              :style {:flex 1}} 
[:% debug-connection/WSTickerControl] 
[:% n/TextDisplay
              {:content (n/format-obj summary)}]))))

^{:refer pune.web.debug-connection/WSUserControl :added "0.1"}
(fact "creates a ws user control"
  ^:hidden
  
  (defn.js WSUserControlDemo
    []
    (var summary  (cr/listenCell mc/C_UTIL_SUMMARY "success" {}))
    (return (n/EnclosedCode 
{:label "pune.web.debug-connection/WSUserControl"
              :style {:flex 1}} 
[:% debug-connection/WSUserControl] 
[:% n/TextDisplay
              {:content (n/format-obj summary)}]))))

^{:refer pune.web.debug-connection/WSDeltaControl :added "0.1"}
(fact "creates a ws delta control"
  ^:hidden

  (defn.js WSDeltaControlDemo
    []
    (var summary  (cr/listenCell mc/C_UTIL_SUMMARY "success" {}))
    (return (n/EnclosedCode 
{:label "pune.web.debug-connection/WSDeltaControl"
              :style {:flex 1}} 
[:% debug-connection/WSDeltaControl
              {:book "TEST-00"}] 
[:% debug-connection/WSDeltaControl
              {:book "TEST-01"}] 
[:% n/TextDisplay
              {:content (n/format-obj summary)}]))))

^{:refer pune.web.debug-connection/WSHarnessControl :added "0.1"}
(fact "creates a ws harness control"
  ^:hidden
  
  (defn.js WSHarnessControlDemo
    []
    (var summary  (cr/listenCell mc/C_UTIL_SUMMARY "success" {}))
    (return (n/EnclosedCode 
{:label "pune.web.debug-harness/WSHarnessControl"
              :style {:flex 1}} 
[:% debug-connection/WSHarnessControl
              {:accountIds ["00000000-0000-0000-0000-000000000000"]}] 
[:% n/TextDisplay
              {:content (n/format-obj summary)}])))
  
  (def.js MODULE (!:module)))
