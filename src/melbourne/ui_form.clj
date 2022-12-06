(ns melbourne.ui-form
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react.ext-view :as ext-view]
             [js.react.ext-route :as ext-route]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [melbourne.slim-common :as slim-common]
             [melbourne.slim-number :as slim-number]
             [melbourne.slim-select :as slim-select]
             [melbourne.slim-submit :as slim-submit]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})


(def.js MODULE (!:module))
