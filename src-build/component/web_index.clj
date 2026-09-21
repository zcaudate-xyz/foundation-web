(ns component.web-index
  (:require [lang.core :as l]
            [std.lib :as h]
            [std.string :as str]
            [net.http :as http]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit   {:native {:suppress true}
                     :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [
             [js.module :as jm]
             [js.react.ext-box :as ext-box]
             [js.react :as r]
             [js.react-native :as n :include [:fn]]
             [js.lib.rn-expo :as x :include [:lib]]
             [xt.lang.spec-base :as xt]
             [xt.event.base-box :as base-box]
             [component.web-melbourne :as web-melbourne]
             [component.web-pune-frame :as web-pune-frame]]
   :export [MODULE]
   :file   "App.js"})

(defrun.js __import__
  (jm/import-missing)
  (jm/import-set-global))

(defglobal.js Global
  (base-box/make-box
   {:init false
    :l0 "01-melbourne"
    :l1 "207a-spinner-basic"}))

(defglobal.js Screens
  (base-box/make-box {}))

(defrun.js ^{:rt/init true}
  __screen__
  (base-box/set-data
   -/Screens
   []
   (tab #_["00-native"      (web-native/raw-controls)]
        ["01-melbourne"   (web-melbourne/melbourne-controls)]
        ["02-slim"        (web-melbourne/slim-controls)]
        ["03-pune-frame"  (web-pune-frame/pune-frame-controls)])))

(defn.js AppMain
  []
  (var [l0 setL0] (ext-box/useBox -/Global ["l0"]))
  (var [l1 setL1] (ext-box/useBox -/Global ["l1"]))
  (var tree (ext-box/listenBox -/Screens []))
  (return
   [:% n/View
    {:style {:position "absolute", :top 0, :bottom 0, :width "100%"}}
    [:% n/TreePane
     {:tree tree,
      :levels
      [{:type "tabs", :initial l0, :setInitial setL0}
       {:type "list",
        :initial l1,
        :setInitial setL1,
        :listWidth 120,
        :displayFn n/displayTarget}]}]]))

(defn.js AppScratch
  []
  (return [:% n/View]))

(defrun.js ^{:rt/init true}
  __main__
  (base-box/set-data -/Global ["Main"] -/AppMain))

(defn.js App
  []
  (var #{Main} (ext-box/listenBox -/Global []))
  (return [:% Main]))

(def.js MODULE
  (x/registerRootComponent -/App))
