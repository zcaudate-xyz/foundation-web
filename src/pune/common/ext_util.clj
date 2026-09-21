(ns pune.common.ext-util
  (:use code.test)
    (:require [lang.core :as  l]
              [std.lib :as h]))

(l/script :js
  {:require [[js.react :as r :include [:fn]]
             [js.core :as j]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js useResendDelay
  "creates a resend delay"
  {:added "0.1"}
  [resend
   opts]
  (var #{[(:= delay 45)]} (or opts {}))
  (var [t] (r/useNow 1000))
  (var #{updated} (or resend {}))
  (return {:disabled (and (k/is-number? updated)
                          (< (- t updated)
                             (* delay 1000)))
           :seconds  (j/max (j/ceil (- delay (/ (- t updated) 1000)))
                            0)
           :updated  updated}))

(defn.js useGroupKey
  [#{groupLu
     routeKey}]
  (var groupKey (. groupLu [routeKey]))
  (var groupRef (r/useFollowRef groupKey))
  (var [groupPrev setGroupPrev] (r/local groupKey))
  (var onComplete
       (fn []
         (setGroupPrev (r/curr groupRef))))
  (return #{groupKey
            onComplete}))

(def.js MODULE (!:module))
