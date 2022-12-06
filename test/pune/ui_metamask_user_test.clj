(ns pune.ui-metamask-user-test
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
             [pune.ui-metamask-user :as metamask-user]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

^{:refer pune.ui-metamask-user/MetamaskUser :added "0.1"}
(fact "creates a metamask user"

  (defn.js MetamaskUserDemo
    []
    (return
     [:% n/Enclosed
      {:label "pune.ui-metamask-user/MetamaskUser"}]))
 
  (def.js MODULE (!:module)))
