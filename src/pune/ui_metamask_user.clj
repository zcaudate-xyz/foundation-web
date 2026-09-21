(ns pune.ui-metamask-user
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [pune.ui-metamask-basic :as mm
              :include [:onboarding :provider]]
             [js.lib.eth-lib :as eth-lib :include [:fn]]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-section :as ui-section]
             [xt.lang.spec-base :as xt]]
   :export [MODULE]})

(defn.js MetamaskUser
  []
  (return [:% n/View]))

(def.js MODULE (!:module))
