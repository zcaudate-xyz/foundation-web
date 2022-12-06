(ns melbourne.ui-input-test
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
   :require [[js.core :as j]
             [js.react-native :as n :include [:fn]]
             [melbourne.base-palette :as base-palette]
             [melbourne.ui-button :as ui-button]
             [melbourne.ui-input :as ui-input]
             [js.react.ext-form :as ext-form]
             [xt.lang.base-lib :as k]
             [xt.lang.event-form :as event-form]]
   :export [MODULE]})

(comment
  (l/rt:module :js))

^{:refer melbourne.ui-input/InputTheme :adopt true :added "0.1"}
(fact "constructs input theme"
  ^:hidden
  
  (def.js RegistraionValidators
    {:first-name    [["is-not-empty" {:message "Must not be empty"
                                      :check (fn:> [v rec] (and (k/not-nil? v)
                                                                (< 0 (k/len v))))}]]
     :last-name     [["is-not-empty" {:message "Must not be empty"
                                      :check (fn:> [v rec]
                                               (j/future-delayed [100]
                                                 (return (and (k/not-nil? v)
                                                              (< 0 (k/len v))))))}]]})
  
  (defn.js inputAsterix
    [design]
    (var #{mainColor} (base-palette/designPalette design))
    (return
     {:component n/Text
      :children ["*"]
      :style {:position "absolute"
              :right -5
              :color mainColor
              :fontSize 16
              :fontWeight "800"}
      :transformations
      (fn:> [#{highlighted
               focusing}]
            {:style {:opacity (* highlighted
                                 (- 1 focusing))}})}))
  
  (defn.js styleInputLabel
    [#{mainNeutral} m]
    (return (j/assign {:fontFamily "Helvetica"
                       :fontSize 12
                       :fontWeight "800"
                       :padding 8
                       :color mainNeutral
                       :width 80}
                      m))))

^{:refer melbourne.ui-input/Input :added "0.1"}
(fact "constructs a themed input"
  ^:hidden
  
  (defn.js InputLightExamples
    []
    (var form (ext-form/makeForm
               (fn:> {:first-name ""
                      :last-name ""})
               -/RegistraionValidators))
    (var #{data result} (ext-form/listenForm form))
    (return
     (n/EnclosedCode 
{:label "melbourne.ui-input/Input.Light"} 
[:% n/View
       {:style {:borderRadius 10
                :width 350
                :overflow "hidden"}}
       
       [:% n/Row
        {:style {:backgroundColor "#eee"
                 :width 350
                 :padding 10}}]
       [:% n/Row
        [:% n/View
         {:style {:backgroundColor "#eee"
                  :width 350
                  :padding 10}}
         [:% n/Row
          {:style {:padding 5}}
          [:% n/Text
           {:style (-/styleInputLabel {:mainNeutral "#333"})}
           "First Name"]
          [:% ui-input/Input
           {:design {:type "light"}
            :highlighted (event-form/check-field-errored
                          form "first_name")
            :value (. data ["first_name"])
            :onBlur  (fn []
                       (event-form/validate-field form "first_name"))
            :onChangeText (fn [v]
                            (event-form/set-field form "first_name" v)
                            (event-form/validate-field form "first_name"))
            :addons [(-/inputAsterix {:type "light"})]}]]
         [:% n/Row
          {:style {:padding 5}}
          [:% n/Text
           {:style (-/styleInputLabel {:mainNeutral "#333"})}
           "Last Name"]
          [:% ui-input/Input
           {:design {:type "light"}
            :highlighted (event-form/check-field-errored
                          form "last_name")
            :value (. data ["last_name"])
            :onBlur  (fn []
                       (event-form/validate-field form "last_name"))
            :onChangeText (fn [v]
                            (event-form/set-field form "last_name" v)
                            (event-form/validate-field form "last_name"))
            :addons [(-/inputAsterix {:type "light"})]}]]
         
         [:% n/Row
          {:style {:marginTop 10
                   :padding 5
                   :flexDirection "row-reverse"}}
          [:% ui-button/Button
           {:disabled (not (event-form/check-all-passed form))
            :design {:type "light"}
            :style {:fontSize 12
                    :fontWeight "800"}
            :text  "Register"}]
          [:% ui-button/Button
           {:design {:type "light"
                     :theme {:bg {:key "neutral"}}}
            :style {:fontSize 12
                    :fontWeight "800"
                    :marginHorizontal 10}
            :onPress (fn:> (event-form/reset-all form))
            :text  "Cancel"}]]]]])))

  (defn.js InputDarkExamples
    []
    (var form (ext-form/makeForm
               (fn:> {:first-name ""
                      :last-name ""})
               -/RegistraionValidators))
    (var #{data result} (ext-form/listenForm form))
    (return
     (n/EnclosedCode 
{:label "melbourne.ui-input/Input.Dark"} 
[:% n/View
       {:style {:borderRadius 10
                :width 350
                :overflow "hidden"}}
       
       [:% n/Row
        {:style {:backgroundColor "#333"
                 :width 350
                 :padding 10}}]
       [:% n/Row
        [:% n/View
         {:style {:backgroundColor "#333"
                  :width 350
                  :padding 10}}
         [:% n/Row
          {:style {:padding 5}}
          [:% n/Text
           {:style (-/styleInputLabel {:mainNeutral "#eee"})}
           "First Name"]
          [:% ui-input/Input
           {:design {:type "dark"}
            :highlighted (event-form/check-field-errored
                          form "first_name")
            :value (. data ["first_name"])
            :onBlur  (fn []
                       (event-form/validate-field form "first_name"))
            :onChangeText (fn [v]
                            (event-form/set-field form "first_name" v)
                            (event-form/validate-field form "first_name"))
            :addons [(-/inputAsterix {:type "dark"})]}]]
         [:% n/Row
          {:style {:padding 5}}
          [:% n/Text
           {:style (-/styleInputLabel {:mainNeutral "#eee"})}
           "Last Name"]
          [:% ui-input/Input
           {:design {:type "dark"}
            :highlighted (event-form/check-field-errored
                          form "last_name")
            :value (. data ["last_name"])
            :onBlur  (fn []
                       (event-form/validate-field form "last_name"))
            :onChangeText (fn [v]
                            (event-form/set-field form "last_name" v)
                            (event-form/validate-field form "last_name"))
            :addons [(-/inputAsterix {:type "dark"})]}]]
         
         [:% n/Row
          {:style {:marginTop 10
                   :padding 5
                   :flexDirection "row-reverse"}}
          [:% ui-button/Button
           {:disabled (not (event-form/check-all-passed form))
            :design {:type "dark"}
            :style {:fontSize 12
                    :fontWeight "800"}
            :text  "Register"}]
          [:% ui-button/Button
           {:design {:type "dark"
                     :theme {:bg {:key "neutral"}}}
            :style {:fontSize 12
                    :fontWeight "800"
                    :marginHorizontal 10}
            :onPress (fn:> (event-form/reset-all form))
            :text  "Cancel"}]]]]])))

  (def.js MODULE (!:module))
  )
