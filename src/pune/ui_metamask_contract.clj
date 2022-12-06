(ns pune.ui-metamask-contract
  (:use code.test)
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [pune.ui-metamask-basic :as mm]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-section :as ui-section]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js MetamaskContract
  []
  (return [:% n/View]))

(def.js MODULE (!:module))
