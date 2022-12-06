(ns melbourne.slim-table-search-test
  (:use code.test)
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :websearch :path "dev/notify"}}
   :require [[js.react :as r]
             [js.react.ext-view :as ext-view]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [melbourne.base-palette :as base-palette]
             [melbourne.ui-group :as ui-group]
             [melbourne.slim :as slim]
             [melbourne.slim-table-list :as slim-table-list]
             [melbourne.slim-entry :as slim-entry]
             [melbourne.ui-input :as ui-input]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [js.core :as j]
             [xt.lang.base-lib :as k]
             [xt.lang.event-route :as event-route]
             [xt.lang.event-view :as event-view]]
   :export [MODULE]})

^{:refer melbourne.slim-table-list/TableList :adopt true :added "0.1"}
(fact "creates the table list view"
  ^:hidden
  
  (defn.js TableListSearchDemo
    []
    (var views   (r/const {:list (ext-view/makeView
                                  {:handler
                                   (fn [args]
                                     (return
                                      (j/future-delayed [100]
                                        (return
                                         (-> (k/arr-range 40)
                                             (k/arr-map (fn:> [i]
                                                          {:id (+ "id-" i)
                                                           :balance (k/random)
                                                           :escrow  args})))))))})}))
    (var control (slim/useLocalControl))
    (var impl   {:type "card"
                 :body {:title  {:type "title"
                                 :template  ["currency_id"]}
                        :main   {:type "v"
                                 :body [{:type "h"
                                         :body [{:type "title"
                                                 :template "B"}
                                                {:template ["balance"]
                                                 :style {:marginLeft 10}
                                                 #_#_:format (fn:> [n] (j/toFixed n 2))}]}
                                        {:type "h"
                                         :body [{:type "title"
                                                 :template "E"}
                                                {:template ["escrow"]
                                                 :style {:marginLeft 10}}]}]}
                        :avatar {:type "image"
                                 :text  {:template  ["currency_id"]}
                                 :image {:template  ["picture"]}}}})
    (var EntryBrief
         (r/const
          (fn:> [props]
            (r/% slim-entry/Entry
                 (j/assignNew
                  props
                  {:impl impl})))))
    (var [example setExample] (r/local "A"))
    (var components {:entry-brief  EntryBrief})
    (r/watch [example]
      (when example
        (j/delayed [100]
          (ext-view/refresh-args (. views list)
                                 [example]))))
    
    (return
     [:% n/Isolation
      (n/EnclosedCode 
{:label "melbourne.slim-table-search/TableListSearch"} 
[:% ui-input/Input
        {:design {:type "light"}
         :value example
         :onChangeText setExample}] 
[:% n/Row
        {:style {:height 400}}
        [:% n/ScrollView
         [:% slim-table-list/TableList
          #{{:mini true
             :design  {:type "light"}
             :style   {:minWidth 200}
             :display {:brief {:card {:component "mini"}}
                       :list  {}}}
            views control components}]]])]))
  
  (def.js MODULE (!:module)))

(comment
  {:design {:type "light"}
   :highlighted (event-form/check-field-errored
                 form "first_name")
   :value (. data ["first_name"])
   :onBlur  (fn []
              (event-form/validate-field form "first_name"))
   :onChangeText (fn [v]
                   (event-form/set-field form "first_name" v)
                   (event-form/validate-field form "first_name"))
   :addons [(-/inputAsterix {:type "light"})]})


^{:refer melbourne.slim-table-search/TableListSearch :added "4.0"}
(fact "TODO")
