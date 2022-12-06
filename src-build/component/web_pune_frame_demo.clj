(ns component.web-pune-frame-demo
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
             [js.react-native :as n :include [:fn [:entypo :icon]]]
             [js.react-native.ui-notify :as ui-notify-events]
             [js.react-native.ui-tooltip :as ui-tooltip]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-button :as ui-button]
             [melbourne.ui-toggle-button :as ui-toggle-button]
             [melbourne.slim-submit :as slim-submit]
             [melbourne.slim-dialog :as slim-dialog]
             [pune.ui-console :as ui-console]
             [pune.ui-sidemenu :as ui-sidemenu]
             [pune.ui-menu-vert :as ui-menu-vert]
             [pune.ui-notify-base :as ui-topnotify]
             [statsweb.admin.index.base-layout :as base-layout]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(def.js SampleText
  (@! (std.string/|
       "Lorem ipsum dolor sit amet. Aut aliquam perspiciatis est atque temporibus At esse esse rem saepe temporibus est voluptatibus molestiae. Est tempora quasi 33 officiis totam est officia inventore. Est nihil quia qui internos nostrum est odit repellendus ea perspiciatis necessitatibus. Aut internos eaque ea omnis quibusdam id esse reprehenderit."
       ""
       "Eum dicta ipsam ut natus autem et recusandae ullam et laudantium deserunt. Ad provident officiis qui aperiam sequi qui laudantium nulla aut minima beatae! Qui nulla sunt ab consequatur galisum qui odit dolores et aliquid similique ut iure molestiae. Ut magnam consequuntur ut facere quam est obcaecati veritatis."
       ""
       "Sit eaque similique et vitae consequatur qui iure aliquid eum eveniet numquam qui internos nulla aut inventore repellendus et dolores laboriosam. Et consectetur dolorem ab unde voluptatem in Quis totam id deleniti provident. Et fuga quia in laboriosam autem qui repudiandae laborum ut quam assumenda. Et assumenda commodi qui nihil unde nam corrupti quasi.")))

(defn.js FrameHeader
  [#{[design
      setIsGuest]}]
  (return
   [:% ui-static/Div
    {:design design
     :style {:flex 1}}
    [:% n/Text "HEADER"]
    [:% n/Padding {:style {:flex 1}}]
    [:% ui-button/Button
     {:text "BACK"
      :onPress (fn:> (setIsGuest false))}]]))

(defn.js FrameConsole
  [#{[design
      routeKey setRouteKey
      (:.. rprops)]}]
  (var [current setCurrent]  [routeKey setRouteKey])
  (return
   [:% ui-console/Console
    #{[:design (j/assignNew design {:invert true})
       current setCurrent
       :screens {"one"   (fn:> [:% n/Text "ONE"])
                 "two"   (fn:> [:% n/Text "TWO"])
                 "three" (fn:> [:% n/Text "THREE"])
                 "four"  (fn:> [:% n/Text "FOUR"])}
       (:.. rprops)]}]))

(defn.js FrameMenu
  "Constructs the main menu"
  {:added "0.1"}
  [#{[design
      setDesign
      mini
      route
      frameConsole
      setFrameConsole
      isGuest
      setIsGuest
      routeKey setRouteKey
      showNotify
      setShowNotify
      inbox
      setInbox]}]
  (return
   #_[:% n/View
    {:style {:width 60
             :height "100%"}}]
   [:% ui-menu-vert/MainMenu
    #{design mini routeKey setRouteKey
      {:items [{:key  "one"
                :icon "home"
                :label "HOME"}
               {:key  "two"
                :icon "user"
                :label "ACCOUNT"}
               {:key   "three"
                :icon  "line-graph"
                :label "MARKET"}
               {:key   "four"
                :icon  "wallet"
                :label "MARKET"}
               {:component ui-menu-vert/MainMenuSeperator}
               {:component ui-menu-vert/MainMenuToggle
                :key   "h3"
                :icon  "browser"
                :label "CONSOLE"
                :design    design
                :selected (k/not-nil? frameConsole)
                :onPress  (fn:> (setFrameConsole
                                 (:? (k/nil? frameConsole)
                                     true
                                     nil)))}
               {:component ui-menu-vert/MainMenuToggle
                :key   "h3"
                :icon  "user"
                :label "IS GUEST"
                :design    design
                :selected isGuest
                :onPress  (fn:> (setIsGuest true))}
               {:component ui-menu-vert/MainMenuSeperator}
               {:component ui-menu-vert/MainMenuToggle
                :key   "h3"
                :icon  "tag"
                :label "DARK MODE"
                :design   design
                :selected (== "dark" (k/get-in design ["type"]))
                :onPress  (fn:> (setDesign
                                 {:type (:? (== "dark" (k/get-in design ["type"]))
                                            "light"
                                            "dark")}))}
               {:component ui-menu-vert/MainMenuToggle
                :key   "h3"
                :icon  "inbox"
                :label "SHOW MESSAGES"
                :design    design
                :selected showNotify
                :onPress  (fn:> (setShowNotify (not showNotify)))}
               {:component ui-menu-vert/MainMenuButton
                :key   "h3"
                :icon  "sound"
                :label "NOTIFY"
                :onPress  (fn []
                            (var id (j/randomId 6))
                            (var msg
                                 {:id    id
                                  :topic "user.account/notify"
                                  :title id
                                  :message (+ "Notify: " id)
                                  :time (k/now-ms)})
                            (setInbox (j/assign {id msg} inbox))
                            (setShowNotify true))}
               ]}}]))

(defn.js FrameNotify
  [#{[design
      showNotify
      setShowNotify
      inbox
      setInbox]}]
  (var isMounted (r/useIsMounted))
  (var refresh   (r/useRefresh))
  (var [index setIndex] (r/local 0))
  (var data (k/arr-sort (j/values inbox)
                        (k/key-fn "time")
                        k/gt))
  (r/watch [inbox refresh]
    (when (k/not-empty? inbox)
      (j/future-delayed [500]
        (when (isMounted)
          (refresh))))
    (var outdated (-> inbox
                      (j/values)
                      (j/filter (fn [e]
                                  (return
                                   (and (not (. e sticky))
                                        (< (+ 5000 (. e time))
                                           (k/now-ms))))))
                      (j/map k/id-fn)))
    (when (k/not-empty? outdated)
      (var out (k/obj-omit inbox outdated))
      (cond (k/is-empty? out)
            (do (setShowNotify false)
                (j/future-delayed [500]
                    (when (isMounted)
                      (setInbox out))))
            
            :else
            (setInbox out))))
  (return
   [:% ui-notify-events/Notify
    {:position "bottom_right"
     :visible showNotify
     :margin 10}
    [:% ui-topnotify/TopNotifyInner
     #{design
       index setIndex
       {:onClose (fn []
                   (var entry (. data [index]))
                   (when (k/nil? entry)
                     (setShowNotify false))
                   (var out (k/obj-omit inbox [(and entry
                                                    (. entry id))]))
                   (cond (k/is-empty? out)
                         (do (setShowNotify false)
                             (j/future-delayed [200]
                               (when (isMounted)
                                 (setInbox out))))
                         
                         :else
                         (setInbox out)))
        :data  data}}]]))

(defn.js FrameDeleteTooltip
  [#{design}]
  (var [visible setVisible] (r/local (fn:> false)))
  (var [waiting setWaiting] (r/local false))
  (var buttonRef (r/ref))
  (return
   [:<>
    [:% n/Row
     {:style {:marginVertical 3}}
     [:% ui-toggle-button/ToggleButton
      {:refLink buttonRef
       :design design
       :text (:? waiting
                 [[:% n/ActivityIndicator
                   {;;:color designColor
                    :size 14}]
                  "DELETE"
                  #_[:% n/Icon
                   {:name "trash"
                    :size 20}]])
       :selected visible
       :style {:width 100
               :alignItems "center"
               :justifyContent "center"
               :textAlign "center"}
       :styleText {}
       :onPress (fn:> (setVisible (not visible)))}]]
    #_[:% ui-tooltip/Tooltip
     #{visible setVisible
       {:alignment "center"
        :position "right"
        :hostRef buttonRef
        :arrow  {:placement "host"
                 :color designError
                 :backdrop true}}}
     [:% n/View
      {:style {:backgroundColor designError
               :justifyContent "center"
               :borderRadius 3}}
      [:% n/View
       {:style {:flexDirection "row-reverse"
                :padding 5}}
       [:% slim-submit/SubmitButton
        #{[design
           :text "CONFIRM"
           :theme {:bgNormal designError
                   :fgPressed designBackground
                   :bgPressed designError}
           :onPress (fn []
                     (setVisible false)
                     (setWaiting true)
                     (j/delayed [500]
                       (setWaiting false)))]}]
       #_[:% slim-submit/SubmitLineHelpers
        #{[design
           :cancelShow true
           :onCancel (fn:> (setVisible false))
           ]}]]]]]))

(defn.js FrameDeleteModal
  [#{design}]
  (var [visible setVisible] (r/local (fn:> false)))
  (var buttonRef (r/ref))
  (return
   [:<>
    [:% n/Row
     {:style {:marginVertical 3}}
     [:% ui-button/Button
      {:refLink buttonRef
       :design (j/assignNew design {:mode ["primary"] })
       :text "DELETE MODAL"
       :onPress (fn:> (setVisible true))}]]
    [:% slim-dialog/Dialog
     {:design {:type "light"}
      :title "Confirm Delete"
      :body [:<>
             "Are you sure you wish to delete?"]
      :modalProps {;;:hostRef buttonRef
                   :transition "none"
                   :effect {:fade 0.1
                            :zoom 0.1}}
      :onSubmit (fn:> (setVisible false))
      :onCancel (fn:> (setVisible false))
      :visible visible}]]))

(defn.js FrameBody
  [#{[design
      routeKey
      setRouteKey
      showNotify
      setShowNotify
      inbox
      setInbox
      mini
      setMini]}]
  (var dimensions (n/useWindowDimensions))
  (return
   [:% n/Isolation
    {:style {:position "absolute"
             :top 0
             :bottom 0
             :left 0
             :right 0
             :overflow "hidden"}}
    [:% ui-static/Div
     {:design design
      :style {:flexDirection "row-reverse"
              :flex 1}}
     [:% ui-sidemenu/SideMenu
      #{design routeKey setRouteKey
        {:data ["one" "two" "three" "four"]
         :narrowed (< (. dimensions width)
                      800)}}]
     [:% n/View
      {:style {:flex 1
               :paddingHorizontal 10}}
      #_[:% ui-static/Breadcrumb
       {:design design
        :root ["HOME"]
        :path (:? routeKey (j/toUpperCase (+ "" routeKey)))}]
      [:% n/Row
       {:style {:paddingVertical 10}}
       [:% ui-static/Text
        {:design design
         :style [{:width 500}]}
        -/SampleText]]

      [:% ui-button/Button
       {:text "MINI"
        :onPress (fn:> (setMini (not mini)))}]
      [:% -/FrameDeleteTooltip
       #{design}]
      [:% n/View
       {:style {:height 20}}]
      [:% -/FrameDeleteModal
       #{design}]
      [:% -/FrameNotify
       #{design
         showNotify
         setShowNotify
         inbox
         setInbox}]]]]))


(defn.js FrameMain
  []
  (var [design setDesign] (r/local {:type "dark"}))
  (var bkey (k/js-encode design))
  (var [frameConsole
        setFrameConsole] (r/local false))
  (var [isGuest
        setIsGuest] (r/local false))
  (var [routeKey setRouteKey] (r/local "one"))
  (var [showNotify setShowNotify] (r/local true))
  (var [mini setMini] (r/local true))
  (var [inbox setInbox] (r/local
                         {"02" {:id    "02"
                                :topic "user.account/place"
                                :title "Order Placed"
                                :message "NBA-MVP-2022/S.CURRY @ Y 1.34"
                                :sticky true
                                :detail {:id "001-order"}
                                :time (k/now-ms)}
                          "01" {:id    "01"
                                :topic "user.account/password-changed"
                                :title "Password Changed"
                                :message "user: test00001"
                                :time (k/now-ms)}}))
  (return
   [:% n/View
    {:style {:position "absolute"
             :top 0
             :bottom 0
             :left 0
             :right 0
             :backgroundColor "white"
             :padding 20
             :overflow "hidden"}}
    [:% n/Isolation
     {:style {:flex 1}}
     [:<>
      [:% base-layout/LayoutMain
       {:design   design
        :mini    mini
        :isGuest isGuest
        :header  -/FrameHeader
        :headerProps #{setIsGuest
                       {:key bkey}}
        :menu    -/FrameMenu
        :menuProps #{frameConsole
                     setFrameConsole
                     isGuest
                     setIsGuest
                     design
                     setDesign
                     routeKey
                     setRouteKey
                     showNotify
                     setShowNotify
                     mini
                     setMini
                     inbox
                     setInbox
                     {:key bkey}}
        :consoleView -/FrameConsole
        :consoleShow  frameConsole
        :consoleProps #{design routeKey setRouteKey}
        :body   -/FrameBody
        :bodyProps #{routeKey
                     setRouteKey
                     showNotify
                     setShowNotify
                     inbox
                     setInbox
                     mini
                     setMini
                     {:key bkey}}}]]]]))

(def.js MODULE (!:module))
