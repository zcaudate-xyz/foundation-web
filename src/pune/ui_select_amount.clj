(ns pune.ui-select-amount
  (:require [std.lang :as l]
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
             [js.react-native :as n :include [:fn]]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-input :as ui-input]
             [melbourne.ui-spinner-basic :as ui-spinner-basic]
             [pune.common.data-swap :as data-swap]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js SelectAmount
  [props]
  (var #{design
         variant
         theme
         value
         setValue
         setValueEdit
         max
         min
         step
         decimal} props)
  (var [editShow  setEditShow] (r/local))
  (var [editText  setEditText] (r/local
                                (j/toFixed (/ value
                                              (j/pow 10 decimal))
                                           (j/max 0 decimal))))
  (var calcValue
       (fn [num]
         (var val (k/round
                   (* num 
                      (j/pow 10 decimal))))
         (:= val (:? (k/not-nil? min)
                     (k/max min val)
                     val))
         (:= val (:? (k/not-nil? max)
                     (k/min max val)
                     val))
         (return val)))
  
  (var setEditTextNumber
       (fn [v]
         (var isEnding (k/first (or (j/match v #"\.0+$")
                                    [])))
         (var isStarting (k/first (or (j/match v #"^\.")
                                    [])))
         (var hasDot (== "." (k/last v)))
         (var isZero (or (k/nil? v)
                         (k/not-empty? )))
         (var num (j/parseFloat v))
         (cond (k/is-empty? v)
               (setEditText "0")

               (or isEnding isStarting)
               (setEditText v)

               (k/nil? num)
               (setEditText editText)
               
               (k/not-nil? num)
               (do (setEditText
                    (+ (j/toString num)
                       (:? hasDot "." "")))
                   (setValue (calcValue num)))
               
               :else
               (setEditText
                (+ editText
                   (:? hasDot "." ""))))))
  (r/watch [editShow]
    (cond setValueEdit
          (do (var out (k/to-number editText))
              (when (not (j/isNaN out))
                (setValueEdit out editShow)))

          editShow
          (setEditText
           (j/toFixed (/ value
                         (j/pow 10 decimal))
                      (j/max 0 decimal)))

          :else
          (do (var out (k/to-number editText))
              (when (not (j/isNaN out))
                (setValue
                 (calcValue out))))))
  (return
   [:% n/Row
    {:style {:alignItems "center"
             :height 40}}
    (:? (not editShow)
        [:% ui-spinner-basic/SpinnerBasic
         #{value setValue step max min decimal
           design
           variant
           theme
           {:styleDigitText {:fontSize 24
                             :marginRight 5}
            :style {:borderWidth 0
                    :borderRadius 3
                    :justifyContent "flex-end"
                    :paddingVertical 0
                    :alignContent "center"
                    :marginTop  2
                    :marginLeft 2
                    :marginRight 2
                    :height 40
                    :marginVertical 0
                    :width 130}}}]
        [:% ui-input/Input
         #{design
           variant
           theme
           {;;:refLink refInput
            :value   editText
            :onChangeText setEditTextNumber
            :onBlur  (fn []
                       (setEditShow false))
            :design {:type "light"}
            :variant {:bg   {:key  "background"
                             :tone "darken"
                             :ratio 1}}
            :autoFocus true
            :styleContainer {#_#_:flex nil
                             :borderWidth 0
                             :width  130
                             :height 40}
            :style {:textAlign "right"
                    :fontSize 24
                    #_#_:fontWeight 600
                    :marginTop 0
                    :marginBottom 0}}}])
    [:% ui-text/ButtonAccent
     {:key editShow
      :style {:marginBottom 2
              :paddingHorizontal 5
              :paddingVertical 4}
      :variant (:? editShow
                   {:bg {:key "background"
                         :mix "neutral"
                         :ratio 3}}
                   {:bg {:key "neutral"}})
      :onPress (fn []
                 (setEditShow (not editShow)))
      :icon {:name "edit"}}]]))

(defn.js SelectPosition
  [props]
  (var #{design
         variant
         theme
         position
         setPosition
         onPress} props)
  (var [editShow  setEditShow] (r/local))
  (var [editText  setEditText] (r/local
                                (data-swap/position-to-fstr position)))
  (r/watch [position]
    (var posText (data-swap/position-to-fstr position))
    (when (not= editText posText)
      (setEditText posText)))
  (var setEditTextNumber
       (fn []
         (var num (j/parseFloat editText))
         (cond (or (k/nil? num)
                   (== 0 num)
                   (j/isNaN num)
                   (k/is-empty? editText))
               (setEditText (data-swap/position-to-fstr position))

               :else
               (do (var p (data-swap/fprice-to-position num))
                   (setPosition p)
                   (setEditText (data-swap/position-to-fstr p))))))
  (return
   [:% n/Row
    {:style {:alignItems "center"
             :height 40}}
    [:% ui-input/Input
     #{design
       variant
       theme
       {:value   editText
        :onChangeText setEditText
        :onBlur setEditTextNumber
        :variant {:bg   {:key  "background"
                         :tone "darken"
                         :ratio 1}}
        #_#_:autoFocus true
        :styleContainer {#_#_:flex nil
                         :borderWidth 0
                         :width  130
                         :height 40}
        :style {:textAlign "right"
                :fontSize 24
                #_#_:fontWeight 600
                :marginTop 0
                :marginBottom 0}}}]
    [:% ui-text/ButtonAccent
     {:style {:marginBottom 2
              :paddingHorizontal 5
              :paddingVertical 4}
      :variant {:bg {:key "neutral"}}
      :onPress onPress
      :icon {:name "cycle"}}]]))

(def.js MODULE (!:module))
