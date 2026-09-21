(ns melbourne.slim-table-common
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react.ext-model :as ext-view]
             [js.react.ext-form :as ext-form]
             [js.react-native.ui-util :as ui-util]
             [xt.event.base-form :as event-form]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [melbourne.slim-entry :as slim-entry]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js useTableEntry
  "uses the table entry"
  {:added "4.0"}
  [props]
  (var #{views displayKey control} props)
  #_(k/LOG! {:out (k/get-in views ["topic_archive"
                                 "output"
                                 "current"])
           :remote (k/get-in views ["topic_archive"
                                 "remote"
                                 "current"])
           :in  (k/get-in views ["topic_archive"
                                 "input"
                                 "current"
                                 "data"])}
          #_#{views displayKey control})
  (var entryId (or (. control showDetail)
                   (. control showModify)))
  (var entries (or (. props entries)
                   (ext-view/listenView (. views [(or displayKey "list")]) "success")
                   []))
  (var remote-entries (or (k/get-in views [displayKey
                                           "remote"
                                           "current"])
                          []))
  (when (k/obj? remote-entries)
    (:= remote-entries []))
  (var entry   (or (j/find entries (fn:> [e] (== entryId (. e id))))
                   (j/find remote-entries (fn:> [e] (== entryId (. e id))))
                   {}))
  (return entry))

(defn.js TableDefaultNotFound
  "default not found placeholder"
  {:added "4.0"}
  [#{design}]
  (return [:% ui-text/H4 #{design} "NOT FOUND"]))

(defn.js TableDefaultIsLoading
  "default is loading placeholder"
  {:added "4.0"}
  [#{design}]
  (return [:% ui-text/H4 #{design} "LOADING..."]))

(defn.js TableBackButton
  "creates a back button"
  {:added "4.0"}
  [#{design
     control}]
  (return
   [:% ui-text/ButtonMinor
    #{design
      {:text  [:% n/Icon
               {:key "back",
                :name "chevron-thin-left"
                :style {:padding 0}}]
       :style {:width 30
               :height 27
               :paddingHorizontal 0
               :textAlign "center"}
       :onPress (fn []
                  (cond (. control showModify)
                        (. control (setShowModify false))

                        :else
                        (do (. control (setShowCreate false))
                            (. control (setShowDetail nil)))))}}]))

(defn.js tablePageHooks
  [props hooks]
  (cond (k/fn? hooks)
        (return (hooks props))

        (k/arr? hooks)
        (return
         (k/arr-foldl hooks
                      (fn [init f]
                        (var out (f init))
                        (return
                         (j/assign init out)))
                      (j/assign {} props)))))

(defn.js TableProgressBack
  [props]
  (var #{control
         entry} props)
  (r/useCountdown 1
                  (fn []
                    (when (k/is-empty? entry)
                      (. control (setShowDetail nil)))))
  (return
   [:% n/View
    {:style {:flex 1
             :alignItems "center"
             :justifyContent "center"}}
    [:% n/ActivityIndicator]]))

(defn.js tablePageView
  "displays the detail view"
  {:added "0.1"}
  [props page]
  (var #{[(:= display {})
          (:= displayKey "list")
          design
          views
          entries
          control
          components]} props)
  (var variant {:bg {:key "background"
                     :tone "sharpen"}})
  (var entry (or (and views 
                      (-/useTableEntry #{views
                                         entries
                                         displayKey
                                         control}))
                 (. props entry)))
  (var impl (. display [page]))
  (var implForm  (k/get-in impl ["form"]))
  (var form (or (. props form)
                implForm))
  (cond (k/arr? form)
        (do 
          (:= form (ext-form/makeForm
                    (fn:>
                      ((k/first implForm) entry props))
                    ((k/second implForm) entry props)))
          (ext-form/listenFormData form)))
  
  (var hooks (. components [(+ "hooks_" page)]))
  (var hprops (-/tablePageHooks (j/assignNew
                                 props
                                 #{entry})
                                hooks))
  
  ;;
  ;; Short Circuit
  ;;
  
  (if (and (k/is-empty? entry)
           (== page "detail"))
    (return
     (r/% -/TableProgressBack (j/assignNew props #{entry}))))


  ;;
  ;; Components
  ;;
  
  (var HeaderComponent
       (or (. components [(+ "header_" page)])
           (and impl
                (. impl header)
                slim-entry/Entry)))
  (var PageComponent
       (or (. components [(+ "entry_" page)])
           (and impl
                slim-entry/Entry)
           -/TableDefaultNotFound))

  (var implHeader (or (. impl header)
                      {}))
  (:= implHeader
      {:main  {:type "v"
               #_#_:variant {:bg {:key "primary"
                              :mix "background"
                              :ratio 7}}
               :style {:paddingTop 10
                       :paddingBottom 5
                       :minHeight 30
                       :maxWidth 500}
               :body (k/arrayify (. implHeader main))}})
  
  (var implHeaderActions (. impl header-actions))
  (var headerElem
       (:? HeaderComponent
           (r/% HeaderComponent
                (j/assignNew
                 props
                 hprops
                 {:entry entry
                  :form form
                  :impl (:? (k/get-in impl ["header" "link"])
                            (j/assign {:type "link"}
                                      (. impl header))

                            implHeader
                            {:type "card"
                             :body (j/assign {:isHeader true}
                                             implHeader)}

                            :else nil)}))))

  (var headerActionsElem
       (:? (and (. control showHeader)
                implHeaderActions)
           [:% n/View
            {:style {}}
            (r/% slim-entry/Entry
                 (j/assignNew
                  props
                  hprops
                  {:entry entry
                   :form form
                   :impl implHeaderActions}))]))
  
  (var pageElem
       (r/% PageComponent
            (j/assignNew
             props
             hprops
             {:entry entry
              :form form
              :impl (. display [page])})))
  (return
   [:% ui-static/Div
    {:style {:flex 1
             #_#_:backgroundColor (:? (== page "detail")
                                  "red")}
     :design design}
    
    (:? (. control showHeader)
        [:% n/Row headerElem] )
    
    headerActionsElem
    (:? (and (. control showScroll)
             (not= false (. impl scroll)))
        (r/% ui-static/ScrollView
             #{design}
             pageElem)
        pageElem)]))

(comment

  #_(:? 
       [:% n/View
      {}
         #_[:% n/View
          {:style {:marginVertical 10
                   :marginHorizontal 4}}
          [:% -/TableBackButton
           #{design control}]]
      ])
  
  #_(:? (== false (k/get-in display [routeKey "scroll"]))
        
        (r/% ui-static/ScrollView
             #{design}
             routeElem)))

(def.js MODULE (!:module))
