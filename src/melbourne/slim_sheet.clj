(ns melbourne.slim-sheet
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [melbourne.base-font :as base-font]
             [melbourne.slim-entry :as slim-entry]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js SheetPagination
  "creates a sheet pagination"
  {:added "4.0"}
  [props]
  (var #{entries
         impl
         design
         control} props)
  (var #{[(:= page {})]} impl)
  (var #{[(:= display 20)
          (:= total (:? entries
                        (k/len entries)
                        0))]} page)
  (var setShowPage (k/get-in control ["setShowPage"]))
  (var showPage    (or (k/get-in control ["showPage"])
                       1))
  (var pageCount (+ (k/floor (/ (- total 1) display))
                    1))

  (var isMini (< pageCount 7))
  (var isLeftEdge  (< showPage 4))
  (var isRightEdge (> showPage (- pageCount 3)))
  (var toggleFn
       (fn:> [idx ellipsis]
         (r/% (or (. page component)
                  ui-text/ToggleTabMinor)
              {:design design
               :key (+ idx 1)
               :style {:marginHorizontal 0}
               :selected (== showPage (+ idx 1))
               :text (:? ellipsis
                         [:% n/Icon
                          {:key  (+ idx 1)
                           :name "dots-three-horizontal"}]
                         (j/toString (+ idx 1)))
               :onPress (fn:> (setShowPage (+ idx 1)))})))
  
  (return
   [:% n/View
    {:key pageCount}
    (:? isMini
        [:% n/Row
         (k/arr-map (k/arr-range pageCount) toggleFn)]
        
        isLeftEdge
        [:% n/Row
         (k/arr-map (k/arr-range 4) toggleFn)
         (toggleFn (+ showPage 1) true)
         (toggleFn (- pageCount 1))]

        isRightEdge
        [:% n/Row
         (toggleFn 0)
         (toggleFn (- showPage 3) true)
         (k/arr-map (k/arr-range [(- pageCount 4) pageCount])
                    toggleFn)]
        
        :else
        [:% n/Row
         (toggleFn 0)
         (toggleFn (- showPage 3) true)
         (k/arr-map (k/arr-range [(- showPage 2) (+ showPage 1)])
                    toggleFn)
         (toggleFn (+ showPage 1) true)
         (toggleFn (- pageCount 1))])]))

(defn.js SheetGroupHeader
  "creates a sheet group header"
  {:added "4.0"}
  [#{[design
      variant
      group
      style
      styleContainer
      (:.. iprops)]}]
  (var #{[name
          (:= format k/identity)]} group)
  (return
   [:% n/View
    {:style {:marginTop 10}}
    [:% n/Row
     {:style styleContainer}
     [:% ui-text/H6
      #{design 
        {#_#_:variant {:fg {:key "background"}
                   :bg {:key "neutral"}}
         :style [{:fontWeight 900
                  :borderRadius 2
                  :paddingVertical 3}
                 (:.. (k/arrayify style))]}}
      (format name)]]
    [:% ui-static/Separator
     #{design
       {:variant {:fg {:key "background"
                       :mix "primary"
                       :ratio 1}}
        :style {:marginBottom 10}}}]]))

;;
;; Entry Row
;;

(defn.js SheetHeader
  "creates a sheet header"
  {:added "4.0"}
  [#{[design
      impl
      (:= variant {:fg {:key "background"}
                   :bg {:key "primary"}})
      (:= custom {})
      style
      (:.. iprops)]}]
  
  (var #{[columns
          (:= header {})]} impl)
  (var columnFn
       (fn [column i]
         (var #{key} column)
         (var #{[style
                 (:.. rprops)]} (or (k/get-in custom [key])
                                    {}))
         (var Component slim-entry/EntryContentTitleH5)
         (var oprops (j/assignNew
                      #{[design
                         :impl  (j/assignNew
                                 column
                                 header
                                 {:variant variant
                                  :template (. column name)}
                                 (. column header))
                         :style [{:paddingVertical 5
                                  :paddingHorizontal 10}
                                 (:.. (k/arrayify style))]]}
                      rprops))
         (return
          [:% n/View
           {:key i
            :style [{:flex 1}
                    (. column style)]}
           (r/% Component oprops)])))
  (return
   [:% ui-static/Div
    #{[design variant
       :style [{:flexDirection "row"
                :maxWidth 500}
               (:.. (k/arrayify style))]]}
    (j/map columns columnFn)
    (r/% slim-entry/EntryContentSeparator
         #{design})]))

(defn.js SheetRow
  "creates a sheet row"
  {:added "4.0"}
  [#{[design
      variant
      impl
      style
      (:= custom {})
      (:.. iprops)]}]
  (var #{[columns]} impl)
  (var columnFn
       (fn [column i]
         (var #{key} column)
         (var #{[style
                 (:.. rprops)]} (or (k/get-in custom [key])
                                    {}))
         (var Component (or (and (. column type)
                                 slim-entry/Entry)
                            slim-entry/EntryContentParagraph))
         (var oprops (j/assignNew
                      iprops
                      #{[design
                         variant
                         :impl  column
                         :style [{:paddingHorizontal 10}
                                 (:.. (k/arrayify style))]
                         ]}
                      rprops))
         (return
          [:% n/View
           {:key i
            :style [{:flex 1}
                    (. column style)]}
           (r/% Component oprops)])))
  (return
   [:% ui-static/Div
    #{[design variant
       :style [{:flexDirection "row"
                :alignItems "center"
                :maxWidth 500
                :marginBottom 3}
               (:.. (k/arrayify style))]]}
    (j/map columns columnFn)]))

(defn.js SheetBasicRows
  "creates a basic sheet"
  {:added "4.0"}
  [props]
  (var #{design
         entries
         impl
         style} props)
  (return
   [:% n/View
    {:style {:flex 1}}
    (j/map entries
           (fn:> [entry i]
             (r/% -/SheetRow
                  (j/assignNew
                   props
                   {:key (+ (or (. entry id)
                                "")
                            i)
                    :entry entry}))))]))

(defn.js SheetBasic
  "creates a basic sheet"
  {:added "4.0"}
  [props]
  (var #{design
         entries
         impl
         style} props)
  (return
   [:<>
    (r/% -/SheetHeader props)
    [:% ui-static/ScrollView
     {:design design
      :style {:marginTop 10}}
     (r/% -/SheetBasicRows props)]]))

(defn.js SheetGroupRows
  [props]
  (var #{[group
          (:.. rprops)]} props)
  (var #{entries} group)
  (return
   [:% n/View
    (r/% -/SheetGroupHeader props)
    (r/% -/SheetBasicRows (j/assign rprops #{entries}))]))

(defn.js groupEntries
  [entries impl]
  (var itemsImpl   (j/assign {:reverse false
                              :sort k/identity
                              :filter k/identity}
                             (. impl items)))
  (var groupsImpl  (j/assign {:reverse false
                              :split  k/id-fn
                              :sort   k/identity
                              :filter k/T}
                             (. impl groups)))
  (var groups (-> entries
                  (k/arr-group-by (k/template-fn (. groupsImpl split))
                                  k/identity)
                  (k/obj-map (. itemsImpl sort))
                  (k/obj-pairs)
                  ((. groupsImpl sort))))
  (return groups))

(defn.js Sheet
  "creates a sheet"
  {:added "4.0"}
  [props]
  (var #{impl
         entries} props)
  (var itemsImpl   (j/assign {:reverse false
                              :sort k/identity
                              :filter k/identity}
                             (. impl items)))
  (var isGrouped   (k/not-nil? (. impl groups)))
  (var isPaged     (k/not-nil? (. impl page)))

  (cond isGrouped
        (do (var groups (-/groupEntries entries impl))
            (return [:% n/View
                     (r/% -/SheetHeader props)
                     (j/map groups
                            (fn:> [[name entries]]
                              (r/% -/SheetGroupRows
                                   (j/assignNew props
                                                {:group #{name entries
                                                          {:format (. impl groups format)}}}
                                                ))))]))
        
        isPaged
        (do (return [:% n/View]))
        
        :else
        (do (return [:% n/View
                     (r/% -/SheetHeader props)
                     (r/% -/SheetBasicRows (j/assignNew props {:entries (. itemsImpl (sort entries))}))])))
  
  
  (return
   (r/% -/SheetBasicRows props)))

(def.js MODULE (!:module))
