(ns melbourne.slim-table
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react.ext-view :as ext-view]
             [js.react.ext-route :as ext-route]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-util :as ui-util]
             [js.react-native.ui-router :as ui-router]
             [melbourne.ui-toolbar :as ui-toolbar]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-swiper :as ui-swiper]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-section :as ui-section]
             [melbourne.slim-entry :as slim-entry]
             [melbourne.slim-table-common :as slim-table-common]
             [melbourne.slim-table-list :as slim-table-list]
             [melbourne.slim-sheet :as slim-sheet]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

;;
;; VIEWS
;;


(defn.js TableModifyView
  [props]
  (return
   (slim-table-common/tablePageView props "modify")))

(defn.js TableDetailView
  [props]
  
  (return
   (slim-table-common/tablePageView props "detail")))

(defn.js TableCreateView
  [props]
  (return
   (slim-table-common/tablePageView props "create")))

(def.js TableRouterLu
  {:detail -/TableDetailView
   :create -/TableCreateView
   :modify -/TableModifyView})

(defn.js TableRouterView
  "creates a single router view"
  {:added "0.1"}
  [props]
  (var #{[design
          control
          (:= components {})
          (:= displayKey "list")
          (:= display {})
          (:= custom {})
          (:= views {})
          entries]} props)
  (var #{routeKey} control)
  (var routeComponent slim-table-common/TableDefaultNotFound)
  (cond (. components [routeKey])
        (:= routeComponent (. components [routeKey]))

        (and (== routeKey "list")
             (or entries
                 (. views [displayKey])))
        (:= routeComponent slim-table-list/TableList)

        (. display [routeKey])
        (:= routeComponent
            (or (. -/TableRouterLu [routeKey])
                slim-table-common/TableDefaultNotFound)))

  (var routeElem
       [:% r/Suspense
        {:fallback [:% slim-table-common/TableDefaultIsLoading #{design}]}
        (r/% routeComponent
             (j/assignNew props (. custom [routeKey])))])
  #_(r/watch [(k/json-encode display)
            routeKey
            displayKey]
     (k/LOG! {:display display
              :routeKey routeKey
              :displayKey displayKey
              :scroll (and (== routeKey "list")
                           (not= false (k/get-in display ["list" "scroll"])))}))
  (return
   (:? (and (== routeKey "list")
            (not= false (k/get-in display ["list" "scroll"])))
       [:% ui-static/ScrollView
        #{design}
        routeElem]
       routeElem)))

(defn.js TableRouter
  "creates the router for elements"
  {:added "0.1"}
  [props]
  (var #{control} props)
  (var routeComponentFn (fn:> -/TableRouterView))
  (var routePropsFn (fn:> [routeKey]
                      (j/assign #{routeKey} props)))
  (var transitionMap {:list   {:detail "from_right"
                               :create "from_left"}
                      :detail {:list   "from_left"
                               :modify "flip_horizontal"}
                      :modify {:detail "flip_horizontal"}
                      :create {:list   "from_right"}})
  (return
   [:% ui-router/Router
    #{routeComponentFn
      routePropsFn
      transitionMap
      {:routeKey (. control routeKey)
       :style {:flex 1}
       :noTransition true}}]))

(defn.js Table
  "creates the table"
  {:added "0.1"}
  [props]
  (var #{[design
          mini
          control
          components
          views
          (:= custom {})]} props)
  (return
   (r/% -/TableRouter props)))

(defn.js TableStandard
  [props]
  (var #{[design
          control
          impl
          views
          display
          (:= displayKey "list")]} props)
  (var entries (ext-view/listenView
                (. views [displayKey])
                "success"))
  (var embedded (or (k/get-in display ["list" "embedded"])
                    {}))
  (return
   [:% n/View
    {:style {:flex 1}}
    (:? (and (k/is-empty? entries)
             (. control showList))
        [:% n/View
         {:style {:flex 1
                  :justifyContent "center"
                  :alignItems "center"}}
         [:% ui-section/EmptyButton
          #{design
            {:textButton (or (. embedded emptyText)
                             "ADD")
             :onPress (fn:> (. control (setShowCreate true)))}}]])
    (:? (or (k/not-empty? entries)
            (not (. control showList)))
        (r/% -/Table props))]))

(defn.js TableEmbedded
  [props]
  (var #{[design
          control
          impl
          views
          display
          (:= displayKey "list")]} props)
  (var entries (or (. props entries)
                   (ext-view/listenView
                    (. views [displayKey])
                    "success")))
  (var embedded (or (k/get-in display ["list" "embedded"])
                    {}))
  (return
   [:% n/Row
    {:style {:flex 1}}
    (:? (and (k/not-empty? entries)
             (. control showList))
        [:% n/View
         {:style {:marginTop 8}}
         [:% ui-text/ButtonMinor
          #{design
            {:text [:% n/Icon
                    {:key "plus"
                     :name "plus"}]
             :onPress (fn:> (. control (setShowCreate true)))}}]])
    [:% n/View
     {:style {:flex 1}}
     (:? (and (k/is-empty? entries)
              (. control showList))
         [:% n/View
          {:style {:flex 1
                   :justifyContent "center"
                   :alignItems "center"}}
          [:% ui-section/EmptyButton
           #{design
             {:textButton (or (. embedded emptyText)
                              "ADD")
              :onPress (fn:> (. control (setShowCreate true)))}}]])
     (:? (or (k/not-empty? entries)
             (not (. control showList)))
         (r/% -/Table props))]]))

(def.js MODULE (!:module))
