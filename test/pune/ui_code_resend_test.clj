(ns pune.ui-code-resend-test
  (:use code.test)
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
             [js.react.ext-form :as ext-form]
             [js.react-native :as n :include [:fn]]
             [melbourne.ui-static :as ui-static]
             [pune.ui-code-resend :as ui-code-resend]
             [melbourne.base-validators :as validators]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

^{:refer pune.ui-code-resend/CodeResendButton :added "0.1"}
(fact "creates the submit resend form"
  ^:hidden
  
  (defn.js  CodeResendButtonDemo
    []
    (var [result0 setResult0] (r/local (fn:>)))
    (var [result1 setResult1] (r/local (fn:>)))
    (var sink0Id (r/id))
    (var sink1Id (r/id))
    (var [key setKey] (r/local (j/random)))
    (var form (ext-form/makeForm (fn:> {:email "test00001@statstrade.io"
                                        :code  "1234"})
                                 {:email []
                                  :code  [(validators/is-length-n 6)]}))
    (return
     [:% n/Enclosed
      {:label "pune.ui-code-resend/CodeResendButton"}
      [:% n/Row
       [:% n/Button {:title "R"
                     :onPress (fn:> (setKey (j/random)))}]]
      [:% n/View
       [:% n/View
        {:style {:backgroundColor "#eee"
                 :padding 20}}
        [:% ui-code-resend/CodeResendButton
         #{form 
           {:key key
            :design {:type "light"}
            :sinkId sink0Id
            :submitProps {:onResult setResult0
                          :onSubmit (fn []
                                      (return (j/future-delayed [300]
                                               (return
                                                {:status "ok"
                                                 :updated (k/now-ms)}))))}}}]
        [:% n/PortalSink
         {:name sink0Id
          :style {:height 80}}]]
       [:% n/View
        {:style {:backgroundColor "#eee"
                 :padding 20}}
        [:% ui-code-resend/CodeResendButton
         #{form
           {:key key
            :design {:type "dark"}
            :sinkId sink1Id
            :submitProps {:onResult setResult1
                          :onSubmit (fn []
                                      (return (j/future-delayed [300]
                                               (return
                                                {:status "error"}))))}}}]
        [:% n/PortalSink
         {:name sink1Id
          :style {:height 80}}]]]
      [:% n/TextDisplay
       {:content (n/format-entry
                  #{result0 result1
                    {:data form.data}})}]]))

  (def.js MODULE (!:module)))
