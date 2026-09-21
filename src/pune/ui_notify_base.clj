(ns pune.ui-notify-base
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn [:entypo :icon]]]
             [js.react-native.ui-notify :as ui-notify-events]
             [js.react-native.ui-util :as ui-util]
             [xt.lang.base-lib :as k]
             [melbourne.ui-spinner :as ui-spinner]
             [melbourne.ui-picker :as ui-picker]
             [melbourne.ui-button :as ui-button]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-text :as ui-text]]
   :export [MODULE]})

(defn.js getOutdated
  [events duration]
  (return
   (-> events
       (j/values)
       (j/filter (fn [e]
                   (return
                    (and (not (. e sticky))
                         (< (+ duration (. e time))
                            (k/now-ms))))))
       (j/map k/id-fn))))

(defn.js useOutdated
  [#{events
     setEvents
     duration}]
  (var isMounted (r/useIsMounted))
  (var refresh   (r/useRefresh))
  (var evictFn
       (fn [ids]
         (when (k/not-empty? ids)
           (setEvents (k/obj-omit events ids)))))
  (r/watch [duration events refresh]
    (when (< 0 duration)
      (when (and (k/not-empty? events))
        (j/future-delayed [500]
          (when (isMounted)
            (refresh))))
      (var outdated (-/getOutdated events duration))
      (when (k/not-empty? outdated)
        (evictFn outdated))))
  (return evictFn))

(defn.js TopNotifyInner
  "creates a TopNotify Component"
  {:added "0.1"}
  [#{[style
      mini
      design
      variant
      (:= data [])
      (:= onClose (fn:>))
      (:= index 0)
      (:= setIndex (fn:>))]}]
  (var fgMix {:key "background"})
  (var bgMix {:key  "neutral"
              :mix  "primary"
              :ratio 1})
  (var __variant (j/assign
                  {:bg bgMix
                   :fg fgMix
                   :hovered  {:fg {:raw 1}
                              :bg {:raw 1}}
                   :pressed  {:fg {:raw 1}
                              :bg {:raw 1}}
                   :disabled {:bg bgMix
                              :fg bgMix}}
                  variant))
  (return
   [:% ui-static/Div
    {:design design
     :variant __variant
     :style [{:height 60
              :padding 5}
             (:? mini
                 {:borderRadius 0}
                 {:borderRadius 3
                  :width 350})
             (:.. (j/arrayify style))]}
    [:% n/Row
     [:% ui-button/Button
      {:design design
       :variant __variant
       :style {:paddingVertical 3
               :paddingHorizontal 3
               :borderRadius 0
               :marginHorizontal 3}
       :text [:% ui-text/Icon
              {:key "close"
               :design design
               :variant __variant
               :name "minus"
               :size 15}]
       :onPress (fn []
                  (onClose))}]
     [:% n/View
      {:style {:flex 1}}
      [:% ui-picker/PickerValues
       {:key (k/len data)
        :design design
        :variant __variant
        :items (:? (k/not-empty? data)
                   (j/map data (k/key-fn "title"))
                   ["NO NOTIFICATIONS"])
        :style {:width 300}
        :styleText {:width 300
                    :fontWeight "800"
                    :fontSize 13}
        :index index
        :setIndex setIndex}]]
     [:% n/View
      (:? (< 1 (k/len data))
          [:% n/Row {:style {:alignItems "center"}}
           [:% ui-spinner/SpinnerControls
            {:min 0,
             :iconProps {:size 10},
             :key (k/len data),
             :variant __variant,
             :value index,
             :setValue setIndex,
             :style {:borderRadius 0,
                     :paddingHorizontal 3,
                     :paddingVertical 3},
             :max (- (k/len data) 1),
             :decimal 0,
             :design design,
             :step 1}
            [:% ui-static/Text
             {:design design,
              :style {:fontSize 11, :margin 5},
              :variant __variant}
             (+ (+ index 1) " of " (k/len data))]]])]]
    [:% n/Row
     {:style {:flex 1
              :paddingLeft 5}}
     [:% ui-static/Text
      {:design design
       :variant __variant
       :style [{:position "absolute"
                :top 5
                :fontSize 11}]}
      (k/get-in data [index "message"])]]]))

(defn.js TopNotify
  [#{design
     variant
     mini
     data
     onClose}]
  (var [index setIndex] (r/local 0))
  (var visible (k/not-empty? data))
  (var notifyElem
       (r/% -/TopNotifyInner
            #{design
              variant
              mini
              data
              onClose
              index
              setIndex}))
  (return
   (:? mini
       (r/% ui-util/Fold
            #{visible}
            notifyElem)
       (r/% ui-notify-events/Notify
            #{visible
              {:position "bottom_left"
               :transition "from_bottom"
               :margin 10}}
            notifyElem))))

(def.js MODULE (!:module))


