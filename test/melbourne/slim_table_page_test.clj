(ns melbourne.slim-table-page-test
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
             [js.react.ext-view :as ext-view]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [melbourne.base-palette :as base-palette]
             [melbourne.ui-group :as ui-group]
             [melbourne.slim :as slim]
             [melbourne.slim-table-list :as slim-table-list]
             [melbourne.slim-entry :as slim-entry]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [js.core :as j]
             [xt.lang.base-lib :as k]
             [xt.lang.event-route :as event-route]]
   :export [MODULE]})

^{:refer melbourne.slim-table-list/TableList :adopt true :added "0.1"}
(fact "creates the table list view"
  ^:hidden
  
  (defn.js TableListPagedDemo
    []
    (var views   (r/const {:list (ext-view/makeView
                                  {:defaultArgs []
                                   :handler
                                   (fn:>
                                     (j/future-delayed [100]
                                       (return
                                        (-> (k/arr-range 200)
                                            (k/arr-map (fn:> [i]
                                                         {:id (+ "id-" i)
                                                          :balance (k/random)
                                                          :escrow  (k/random)}))))))})}))
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
                                                 :format (fn:> [n] (:? (k/is-number? n) (j/toFixed n 2)))}]}
                                        {:type "h"
                                         :body [{:type "title"
                                                 :template "E"}
                                                {:template ["escrow"]
                                                 :style {:marginLeft 10}
                                                 :format (fn:> [n] (:? (k/is-number? n) (j/toFixed n 2)))}]}]}
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
    (var components {:entry-brief  EntryBrief})
    (return
     [:% n/Isolation
      (n/EnclosedCode 
{:label "melbourne.slim-table-page/TableListPaged"} 
[:% n/Row
        {:style {:height 400}}
        [:% n/ScrollView
         [:% slim-table-list/TableList
          #{{:mini true
             :design {:type "light"}
             :style   {:minWidth 200}
             :display {:brief {:card {:component "mini"}}
                       :list  {:page {:display 5}}}}
            views control components}]]])])))

^{:refer melbourne.slim-table-list/TableList :adopt true :added "0.1"}
(fact "creates the table list view"
  ^:hidden

  (defn.js TableListRemotePagedDemo
    []
    (var views
         (r/const {:list (ext-view/makeView
                          {:defaultArgs []
                           :handler
                           (fn:> [showPage display]
                             (j/future-delayed [200]
                                               (return
                                                (k/arr-map (k/arr-range display)
                                                           (fn:> [i]
                                                             {:id   (+ "id-" (+ (* (- showPage 2) display)
                                                                                display
                                                                                i))
                                                              :balance (k/random)
                                                              :escrow  (k/random)})))))})}))
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
                                                 :format (fn:> [n] (:? (k/is-number? n) (j/toFixed n 2)))}]}
                                        {:type "h"
                                         :body [{:type "title"
                                                 :template "E"}
                                                {:template ["escrow"]
                                                 :style {:marginLeft 10}
                                                 :format (fn:> [n] (:? (k/is-number? n) (j/toFixed n 2)))}]}]}
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
    (var components {:entry-brief  EntryBrief})
    (return
     [:% n/Isolation
      (n/EnclosedCode 
{:label "melbourne.slim-table-page/TableListRemotePaged"} 
[:% n/Row
        {:style {:height 400}}
        [:% n/ScrollView
         [:% slim-table-list/TableList
          #{{:mini true
             :design {:type "light"}
             :style   {:minWidth 200}
             :display {:brief {:card {:component "mini"}}
                       :list  {:page {:remote true
                                      :total 200
                                      :display 5}}}}
            views control components}]]])]))
  
  (def.js MODULE (!:module)))
