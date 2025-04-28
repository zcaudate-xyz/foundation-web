(ns melbourne.slim-table-list
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
             [melbourne.slim-sheet :as slim-sheet]
             [xt.lang.base-lib :as k]
             [xt.lang.event-view :as event-view]]
   :export [MODULE]})

;;
;; CARDS
;;

(defn.js TableListCardBrief
  "creates the mini row"
  {:added "0.1"}
  [props]
  (var #{[design
          entry
          style
          children]} props)
  (return
   [:% ui-section/CardBoundary
    #{style}
    [:% n/Row
     [:% n/View
      {:style {:flex 1}}
      children]]]))

(defn.js TableListCardNav
  "creates the mini row"
  {:added "4.0"}
  [props]
  (var #{[design
          entry
          control
          style
          (:= list {})
          children]} props)
  (var #{routeKey} control)
  (var isDetail (== (. entry id)
                    (. control showDetail)))
  (var #{[(:= showDetail "none")]} list)
  (var detailFn
       (fn []
         (cond (not isDetail)
               (. control (setShowDetail (. entry id)))
               
               :else
               (. control (setShowList true)))))
  (var buttonElem [:% ui-text/ButtonMinor
                   #{design
                     {:text  [:% n/Icon
                              {:key "detail"
                               :name "chevron-right"}]
                      :style {:width 30
                              :height 27
                              :paddingHorizontal 0
                              :textAlign "center"}
                      :onPress detailFn}}])
  (return
   [:% ui-section/CardBoundary
    #{style}
    [:% n/Row
     [:% n/View
      {:style {:flex 1}}
      children]
     (:? (== showDetail "none")
         nil

         (== showDetail "float")
         [:% n/View
          {:style {:position "absolute"
                   :right 0
                   :top 10}}
          buttonElem]
         
         (== showDetail "right")
         [:% n/View
          {:style {:top 10}}
          buttonElem]

         :else nil)]]))

(defn.js TableListCardSwipe
  "creates the table swipe row element (mini)"
  {:added "0.1"}
  [#{[children
      style
      display
      (:.. rprops)]}]
  (var #{design
         actions
         entry
         control} rprops)
  (var #{[direction
          (:= textDetail "DETAIL")
          (:= textDelete "DELETE")
          (:= showDetail true)
          (:= showDelete true)]} (or (k/get-in display ["swipe"])
                                     {}))
  (var swipeView
       [:% n/View
        {:key "detail"
         :style {:flex 1
                 :justifyContent "space-around"
                 :alignItems "center"}}
        (:? showDetail
            (r/% ui-text/ButtonAccent
                 #{design
                   {:text textDetail
                    :onPress (fn:> (. control (setShowDetail (. entry id))))}}))
        (:? showDelete
            (r/% ui-text/ConfirmTooltip
                 #{design
                   {:text textDelete
                    :onPress (fn:> (. actions (delete (. entry id))))}}))])
  (var swipeProps
       (:? (== direction "left")
           {:negEnabled (or showDetail
                            showDelete)
            :negThreshold -50
            :negFull -80
            :negView swipeView}
           {:posEnabled (or showDetail
                            showDelete)
            :posThreshold 50
            :posFull 80
            :posView swipeView}))
  (return
   [:% ui-section/CardBoundary
    {:style style}
    (r/% ui-swiper/Swiper
         (j/assign #{design} swipeProps)
         children)]))

(defn.js TableListCardFold
  "displays a standard row"
  {:added "0.1"}
  [props]
  (var #{[(:= display {})
          (:= components {})
          design
          entry
          control
          
          scroll
          children]} props)
  (var DetailComponent
       (or (. components ["entry_detail"])
           (and (. display ["detail"])
                slim-entry/Entry)
           slim-table-common/TableDefaultNotFound))
  
  (var detailElem
       (r/% DetailComponent
            (j/assign {:impl (. display ["detail"])}
                      props)))
  
  (return
   [:% n/View
    (r/% -/TableListCardNav props)
    [:% ui-section/CardBoundary
     [:% ui-util/Fold
      {:visible (== (. entry id)
                    (. control showDetail))
       :noTransition true}
      (:? scroll
          [:% n/View
           {:style {:height 400}}
           detailElem]
          detailElem)]]]))

(defn.js TableListCard
  "creates the table card"
  {:added "0.1"}
  [props]
  (var #{[(:= display {})
          (:= custom  {})
          (:= components {})]} props)
  (var BriefComponent
       (or (. components ["entry_brief"])
           (and (. display ["brief"])
                slim-entry/Entry)
           slim-table-common/TableDefaultNotFound))
  (var briefElem
       (r/% BriefComponent
            (j/assign {:impl (. display ["brief"])}
                      props
                      (. custom ["brief"]))))
  (var #{[(:= card {})]}  (or (. display ["brief"])
                              {}))
  (var Component (:? (== (. card component) "swipe")
                     -/TableListCardSwipe
                     
                     (== (. card component) "fold")
                     -/TableListCardFold

                     (k/fn? (. card component))
                     (. card component)
                     
                     :else
                     -/TableListCardBrief))
  (return
   (r/% Component props briefElem)))

(defn.js TableListViewEntries
  "creates a group row of sheets"
  {:added "4.0"}
  [#{[entries
      impl
      (:.. rprops)]}]
  (var join (k/get-in impl ["groups" "join"]))
  (return
   [:% n/FlatList
    {:data entries
     :keyExtractor k/id-fn
     :renderItem
     (fn [iprops i]
       (var entry (. iprops item))
       (return
        [:% n/View
         {:key (. entry id)}
         (r/% -/TableListCard
              (j/assignNew rprops
                           {:entry entry
                            :list  impl}))
         (:? (and join
                  (< (+ 1 i) (k/len entries)))
             (r/% slim-entry/Entry (j/assignNew rprops {:impl join})))]))}]))

(defn.js TableListViewGroup
  "creates a group row of sheets"
  {:added "4.0"}
  [props]
  (var #{[group
          impl
          (:.. rprops)]} props)
  (var component (k/get-in impl ["groups" "header"]))
  (var #{entries} group)
  (return
   [:% n/View
    {:style {:marginBottom 5}}
    [:% n/View
     {:style {:marginHorizontal 10}}
     (r/% (or component
              slim-sheet/SheetGroupHeader) props)]
    (r/% -/TableListViewEntries (j/assign rprops #{entries impl}))]))

(defn.js TableListViewBase
  [props]
  (var #{impl
         entries} props)
  (var itemsImpl   (j/assign {:reverse false
                              :sort k/identity
                              :filter k/identity}
                             (. impl items)))
  (var isGrouped   (k/not-nil? (. impl groups)))
  (cond isGrouped
        (do (var groups (slim-sheet/groupEntries entries impl))
            (return
             [:% n/FlatList
              {:data groups
               :keyExtractor k/first
               :renderItem
               (fn [iprops]
                 (var [name entries] (. iprops item))
                 (return
                  (r/% -/TableListViewGroup
                       (j/assignNew props
                                    {:key name
                                     :group #{name entries
                                              {:format (. impl groups format)}}}))))}]))
        
        :else
        (return
         (r/% -/TableListViewEntries props))))

(defn.js usePageEntries
  [props]
  (var #{[control
          views
          impl
          (:= displayKey "list")]} props)
  (var #{[(:= filterFn k/identity)
          (:= sortFn k/identity)]} impl)
  (var page (j/assign {:display 20}
                      (k/get-in impl ["page"])))
  (var [showPage setShowPage] (:? (k/get-in control ["setShowPage"])
                                  [(. control showPage) (. control setShowPage)]
                                  (r/local 1)))
  (var entriesAll (-> (or (. props entries)
                          (ext-view/listenView (. views [displayKey]) "success")
                          [])
                      (sortFn (. control orderBy))))
  (var entries (-> entriesAll
                   (k/arr-slice (* (- showPage 1)
                                   (. page display))
                                (* showPage
                                   (. page display)))
                   (k/arr-filter k/identity)))
  (return #{page
            showPage setShowPage
            entriesAll
            entries}))

(defn.js TableListViewPagedScaffold
  [props]
  (var #{design
         ListComponent} props)
  (var #{page
         showPage setShowPage
         entriesAll
         entries} (-/usePageEntries props))
  (return
   [:% n/View
    (r/% ListComponent (j/assignNew props #{entries}))
    (:? (> (k/len entriesAll)
           (. page display))
        [:% ui-static/Div
         {:design design
          :style {:alignItems "center"
                  :marginHorizontal 10
                  :marginTop 10
                  :marginBottom 20
                  :maxWidth 550}}
         [:% slim-sheet/SheetPagination
          {:design design
           :control #{showPage setShowPage}
           :impl #{page}
           :entries entriesAll}]])]))

(defn.js TableListViewPaged
  [props]
  (var ListComponent -/TableListViewBase)
  (return
   (r/% -/TableListViewPagedScaffold
        (j/assignNew props #{ListComponent}))))

;;
;; REMOTE
;;

(defn.js useRemotePagedEntries
  [props]
  (var #{[control
          views
          impl
          (:= displayKey "list")]} props)
  (var #{[(:= filterFn k/identity)
          (:= sortFn k/identity)]} impl)
  (var page (j/assign {:display 20
                       :total 0
                       :argsFn k/identity}
                      (k/get-in impl ["page"])))
  (var [showPage setShowPage]
       (:? (k/get-in control ["setShowPage"])
           [(. control showPage) (. control setShowPage)]
           (r/local 1)))
  (var args ((. page argsFn) [showPage (. page display)] props))
  (var entriesUpdatedRef (r/ref))
  (var refresh-fn
       (fn []
         (r/curr:set entriesUpdatedRef (k/now-ms))
         (ext-view/refreshArgsFn
          (. views [displayKey])
          args
          {:remote "always"
           ;;:with-pending true
           :meta #{displayKey}})))
  (var entries
       (ext-view/listenView
        (. views [displayKey])
        "success"
        {:resultFn
         (fn [event]
           (when (== "view.output" (. event type))
             (when (== "main" (. event data tag))
               (when (> (- (k/now-ms)
                           (r/curr entriesUpdatedRef))
                        5000)
                 (refresh-fn)))))}
        nil
        "remote"))
  
  (var output  (ext-view/listenViewOutput (. views [displayKey])
                                          ["pending"]
                                          {}
                                          nil
                                          "remote"))
  (r/watch [(k/json-encode args)]
    (refresh-fn))
  (r/init []
    (refresh-fn)
    (return (fn []
              (event-view/set-output (. views [displayKey])
                                     nil))))
  (return #{page
            showPage setShowPage
            entries
            output}))

(defn.js TableListViewRemotePagedScaffold
  [props]
  (var #{design
         variant
         ListComponent} props)
  (var #{page
         showPage setShowPage
         entries
         output} (-/useRemotePagedEntries props))
  (return
   [:% n/View
    {:style {:flex 1}}
    (:? (. output pending)
        [:% n/View
         {:style {:flex 1
                  :alignItems "center"
                  :justifyContent "center"}}
         [:% n/ActivityIndicator]]

        (k/is-empty? entries)
        [:% n/View
         {:style {:flex 1
                  :alignItems "center"
                  :justifyContent "center"}}
         [:% ui-static/Text
          #{design
            {:style {:fontSize 13}}}
          "No Entries"]]

        :else
        [:% ui-static/ScrollView
         #{design}
         (r/% ListComponent
              (j/assignNew props #{entries}))])
    
    (:? (> (. page total)
           (. page display))
        [:% ui-static/Div
         {:design design
          :variant variant
          :style {:alignItems "center"
                  :marginHorizontal 10
                  :marginTop 5
                  :marginBottom 5
                  :maxWidth 550}}
         [:% slim-sheet/SheetPagination
          {:design design
           :control #{showPage setShowPage}
           :impl #{page}
           :entries []}]
         
         #_[:% ui-text/H6
            #{design
              {:style {:marginTop 2}
               :variant {:fg {:key "neutral"}}}}
            (k/cat (+ 1 (* (. page display)
                           (- showPage 1)))
                   "-"
                   (* (. page display)
                      showPage)
                   " of "
                   (. page total))]])]))

(defn.js TableListViewRemotePaged
  [props]
  (var ListComponent -/TableListViewBase)
  (return
   (r/% -/TableListViewRemotePagedScaffold
        (j/assignNew props #{ListComponent}))))

(defn.js TableListView
  "creates a table list view"
  {:added "4.0"}
  [props]
  (var #{impl
         entries} props)
  (cond (k/get-in impl ["page" "remote"])
        (return
         (r/% -/TableListViewRemotePaged props))
        
        (k/get-in impl ["page"])
        (return
         (r/% -/TableListViewPaged props))

        :else
        (return
         (r/% -/TableListViewBase props))))

(defn.js TableList
  "creates the table list view"
  {:added "0.1"}
  [props]
  (var #{[entries
          style
          (:.. rprops)]} props)
  (var #{[design
          mini
          actions
          views
          display
          components
          control
          (:= displayKey "list")]} rprops)
  (var impl (or (k/get-in display ["list"])
                {}))
  (:= impl (:? (. impl props)
               (j/assignNew impl ((. impl props) impl props))
               impl))
  (var #{[top
          bottom
          (:= filterFn k/identity)
          (:= sortFn k/identity)]} impl)
  (:= entries (-> (or entries
                      (ext-view/listenView (. views [displayKey]) "success")
                      [])
                  (sortFn (. control orderBy))))
(var topElem
       (:? top
         (r/% slim-entry/Entry (j/assignNew props {:impl top}))))
  (var bottomElem
       (:? bottom
           (r/% slim-entry/Entry (j/assignNew props {:impl bottom}))))
  (var centerElem
       (:? (== "row" (. impl type))
           (r/% slim-sheet/Sheet (j/assignNew props #{impl entries}))

           :else
           (r/% -/TableListView (j/assignNew props #{impl entries}))))
  (return
   [:% n/View
    {:style style}
    topElem
    centerElem
    bottomElem]))

(def.js MODULE (!:module))
