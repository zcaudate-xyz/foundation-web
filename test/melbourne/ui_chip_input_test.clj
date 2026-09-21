(ns melbourne.ui-chip-input-test
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react-native :as n :include [:fn]]
             [js.react :as r]
             [melbourne.ui-static :as ui-static]
             [melbourne.base-palette :as base-palette]
             [melbourne.ui-chip-input :as ui-chip-input]
             ]
   :export [MODULE]})

^{:refer melbourne.ui-chip-input/ChipInput :added "4.0"}
(fact "creates a static div"
  ^:hidden
  
  (defn.js ChipInputDemo
    []
    (var [values setValues] (r/local ["hello" "world"]))
    (return
     (n/EnclosedCode 
{:label "melbourne.ui-chip-input/ChipInput"} 
[:% n/Row
       [:% ui-chip-input/ChipInput
        #{values setValues}]])))

  (def.js MODULE (!:module)))
