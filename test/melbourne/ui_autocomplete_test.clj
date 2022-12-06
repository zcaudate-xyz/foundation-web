(ns melbourne.ui-autocomplete-test
  (:use code.test)
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react :as r]
             [js.react-native :as n :include [:fn]]
             [js.react.ext-form :as ext-form]
             [js.react.ext-view :as ext-view]
             [js.core :as j]
             [melbourne.ui-autocomplete :as ui-autocomplete]
             [melbourne.slim-sheet :as slim-sheet]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(def.js NAMES
  (@! (->> (h/sys:resource-content "melbourne/girl-names.json")
           (std.json/read)
           (mapv std.string/upper-case))))

(defn.js get-names
  [filt]
  (var output [])
  (k/for:array [n -/NAMES]
    (when (j/startsWith n (j/toUpperCase filt))
      (x:arr-push output {:name n}))
    (when (< 15 (k/len output))
      (return output)))
  (return output))
  

^{:refer melbourne.ui-autocomplete/SelectComponentEmpty :added "4.0"}
(fact "default empty component")

^{:refer melbourne.ui-autocomplete/SelectComponentBusy :added "4.0"}
(fact "default busy component")

^{:refer melbourne.ui-autocomplete/SelectComponentEntry :added "4.0"}
(fact "default entry component")

^{:refer melbourne.ui-autocomplete/SelectSingle :added "4.0"}
(fact "creates a single select autocomplete"
  ^:hidden
  
  (defn.js SelectSingleDemo
    []
    (var view    (ext-view/makeView
                  {:handler (fn:> [filt]
                              (j/future-delayed [300]
                                (return (-/get-names filt))))
                   :defaultOutput []}))
    (var [selected
          setSelected] (r/local))
    (return
     [:% n/Enclosed
      {:label "melbourne.ui-autocomplete/SelectSingle"}
      [:% n/Isolation
       [:% ui-autocomplete/SelectSingle
        #{selected
          setSelected
          {:source {:key-fn k/id-fn
                    :val-fn k/identity
                    :view view}}}]]]))

  (def.js MODULE (!:module)))
