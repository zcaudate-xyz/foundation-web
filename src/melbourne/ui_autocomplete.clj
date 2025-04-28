(ns melbourne.ui-autocomplete
  (:require [std.lang :as  l]
            [std.lib :as h]
            [std.string :as str]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [js.react.ext-view :as ext-view]
             [js.react-native.ui-autocomplete :as ui-autocomplete]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-input :as ui-input]
             [melbourne.base-font :as base-font]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js SelectComponentEmpty
  [#{design}]
  (return
   [:% ui-text/H5
    #{design}
    "Not Found"]))

(defn.js SelectComponentBusy
  [#{design}]
  (return
   [:% ui-text/H5
    #{design}
    "Fetching Data..."]))

(defn.js SelectComponentEntry
  [#{design
     entry
     source
     selected
     setSelected}]
  (return
   [:% ui-text/ButtonAccent
    #{design
      {:onPress (fn:> (setSelected entry))
       :text (k/json-encode entry)}}]))

(defn.js SelectSingle
  [#{[design
      selected
      setSelected
      source
      (:.. rprops)]}]
  (var hostRef (r/ref))
  (var [value setValue] (r/local ""))
  (var [visible setVisible] (r/local))
  (return
   (:? selected
       [:% ui-text/ButtonAccent
        #{design
          {:onPress (fn:> (setSelected nil))
           :text (k/json-encode selected)}}]
       [:<>
        [:% ui-input/Input
         {:design {:type "dark"}
          :refLink hostRef
          :value value
          :onChangeText setValue
          :onFocus (fn:> (setVisible true))
          :onBlur  (fn:> (setVisible false))}]
        [:% ui-autocomplete/Autocomplete
         #{[hostRef
            visible setVisible 
            selected setSelected
            :sourceView (. source view)
            :sourceInput [value]
            :component -/SelectComponentEntry
            :componentBusy -/SelectComponentBusy
            :componentEmpty -/SelectComponentEmpty]}]])))

(def.js MODULE (!:module))



(comment

  (defn.js AutocompleteDemo
    []
  (var inputRef (r/ref))
  (var [value setValue] (r/local ""))
  (var [visible setVisible] (r/local true))
  (var view    (ext-view/makeView
                {:handler (fn:> [filt]
                            (j/future-delayed [300]
                              (return (-/get-names filt))))
                 :defaultOutput []}))
  (var component (r/const
                    (fn [#{entry}]
                      (return
                       [:% n/Text
                        {:style {:padding 5}}
                        (k/json-encode entry)]))))
    (return
     [:% n/Enclosed
      {:label "js.react-native.ui-autocomplete/Autocomplete"}
      [:% n/Isolation
       [:% n/View
        {:style {:width 300
                 :height 150}}
        [:% n/View
         [:% ui-input/Input
          {:design {:type "dark"}
           :refLink inputRef
           :value value
           :onChangeText setValue
           :onFocus (fn:> (setVisible true))
           :onBlur  (fn:> (setVisible false))}]]]
       [:% ui-autocomplete/Autocomplete
        #{[visible setVisible component
           :hostRef inputRef
           :sourceView view
           :sourceInput [value]
           :styleContainer {:backgroundColor "red"}]}]]]))

  (defn.js SelectMulti
  [#{[design
      selected
      setSelected
      (:.. rprops)]}]
  (return
   (:? selected
       [:% n/View])))
  )
