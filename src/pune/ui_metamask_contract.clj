(ns pune.ui-metamask-contract
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [pune.ui-metamask-basic :as mm]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-section :as ui-section]
             [xt.lang.spec-base :as xt]]
   :export [MODULE]})

(defn.js MetamaskContract
  []
  (return [:% n/View]))

(def.js MODULE (!:module))
