(ns melbourne.ui-checkbox-test
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
             [melbourne.ui-checkbox :as ui-checkbox]
             [xt.lang.base-lib :as k]
             ]
   :export [MODULE]})

^{:refer melbourne.ui-checkbox/CheckBox :added "0.1"}
(fact "creates a checkbox"
  ^:hidden
  
  (defn.js CheckBoxDemo
    []
    (var [s0 setS0] (r/local false))
    (var [s1 setS1] (r/local true))
    (var [s2 setS2] (r/local true))
    (return
     [:% n/Enclosed
      {:label " melbourne.ui-checkbox/CheckBox"}
      [:% n/Row
       [:% n/Row
        {:style {:backgroundColor "#eee"
                 :flex 1
                 :padding 30}}
        [:% ui-checkbox/CheckBox
         #{[:design {:type "light"
                    }
            :style {:margin 2}
            :selected s0
            :onPress (fn:> (setS0 (not s0)))]}]
        [:% ui-checkbox/CheckBox
         #{[:design {:type "light"
                    :mode "secondary"}
            :style {:margin 2}
            :selected s1
            :onPress (fn:> (setS1 (not s1)))]}]
        [:% ui-checkbox/CheckBox
         #{[:design {:type "light"
                    }
            :style {:margin 2}
            :selected s2
            :onPress (fn:> (setS2 (not s2)))]}]
        [:% ui-checkbox/CheckBox
         #{[:design {:type "light"
                    }
            :style {:margin 2}
            :selected s2
            :onPress (fn:> (setS2 (not s2)))]}]]
       [:% n/Row
        {:style {:backgroundColor "#333"
                 :flex 1
                 :padding 30}}
        [:% ui-checkbox/CheckBox
         #{[:design {:type "dark"
                    }
            :style {:margin 2}
            :selected s0
            :onPress (fn:> (setS0 (not s0)))]}]
        [:% ui-checkbox/CheckBox
         #{[:design {:type "dark"
                    :mode "secondary"}
            :style {:margin 2}
            :selected s1
            :onPress (fn:> (setS1 (not s1)))]}]
        [:% ui-checkbox/CheckBox
         #{[:design {:type "dark"
                    }
            :style {:margin 2}
            :selected s2
            :onPress (fn:> (setS2 (not s2)))]}]]]])))


^{:refer melbourne.ui-checkbox/CheckGroupIndexed :added "0.1"}
(fact "creates a group of check boxes"
  ^:hidden

  (defn.js CheckGroupIndexedDemo
    []
    (var [indices setIndices] (r/local [false true false]))
    (return
     [:% n/Enclosed
      {:label " melbourne.ui-checkbox/CheckGroupIndexed"}
      [:% n/Row
       [:% n/Row
        {:style {:backgroundColor "#eee"
                 :flex 1
                 :padding 30}}
        [:% ui-checkbox/CheckGroupIndexed
         {:design {:type "light"
                  }
          :items [" STATS " " XLM " " USD "]
          :indices indices
          :setIndices setIndices}]]
       [:% n/Row
        {:style {:backgroundColor "#333"
                 :flex 1
                 :padding 30}}
        [:% ui-checkbox/CheckGroupIndexed
         {:design {:type "dark"
                  }
          :items [" STATS " " XLM " " USD "]
          :indices indices
          :setIndices setIndices}]]]
      [:% n/TextDisplay
       {:content (k/js-encode indices)}]])))

^{:refer melbourne.ui-checkbox/CheckGroup :added "0.1"}
(fact "creates a group of check boxes"
  ^:hidden

  (defn.js CheckGroupDemo
    []
    (var [values setValues] (r/local ["USD"]))
    (return
     [:% n/Enclosed
      {:label " melbourne.ui-checkbox/CheckGroup"}
      [:% n/Row
       [:% n/Row
        {:style {:backgroundColor "#eee"
                 :flex 1
                 :padding 30}}
        [:% ui-checkbox/CheckGroup
         {:design {:type "light"
                  }
          :data ["STATS" "XLM" "USD"]
          :values values
          :setValues setValues
          :format (fn:> [s] (+ "  " s))}]]
       [:% n/Row
        {:style {:backgroundColor "#333"
                 :flex 1
                 :padding 30}}
        [:% ui-checkbox/CheckGroup
         {:design {:type "dark"
                  }
          :data ["STATS" "XLM" "USD"]
          :values values
          :setValues setValues
          :format (fn:> [s] (+ "  " s))}]]]
      [:% n/TextDisplay
       {:content (k/js-encode values)}]]))

  (def.js MODULE (!:module)))
