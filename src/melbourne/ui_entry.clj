(ns melbourne.ui-entry
  (:use code.test)
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
             [js.react :as r]
             [js.react-native :as n :include [:fn]]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-static :as ui-static]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js get-entry-data
  "gets the entry data"
  {:added "4.0"}
  [entry getter]
  (cond (k/fn? getter)
        (return (getter entry))

        (k/nil? getter)
        (return entry)
        
        :else
        (return (k/get-in entry (j/arrayify getter)))))

;;
;; Entry Row
;;

(defn.js EntryRowHeaderText
  "creates the entry header"
  {:added "4.0"}
  [#{[design
      impl
      column
      style
      (:.. rprops)]}]
  (var format (or (k/get-in impl ["header"
                                  "format"])
                  k/identity))
  (var #{[name]} column)
  (return
   (r/% ui-text/H6
        (j/assign
         #{design
           {:style [{:flex 1}
                    (:.. (k/arrayify style))]}}
         rprops)
        (format name column))))

(defn.js EntryRowHeader
  "creates the entry row header"
  {:added "4.0"}
  [#{design
     variant
     impl
     props
     style}]
  (:= variant (or variant
                  {:bg {:key "primary"}
                   :fg {:key "background"
                        :tone "sharpen"}}))
  (var #{[columns
          (:= header {})]} impl)
  (var Component (or (. header component)
                     -/EntryRowHeaderText))
  (var columnFn
       (fn [column]
         (var #{name} column)
         (var #{[style
                 (:.. rprops)]} (or (k/get-in props [name])
                                    {}))
         (return
          (r/% Component
               (j/assign
                #{[design
                   variant
                   impl
                   column
                   :style [{:paddingVertical 5
                            :paddingHorizontal 10}
                           (:.. (k/arrayify (. header style)))
                           (:.. (k/arrayify style))]
                   :key (. column name)]}
                rprops)))))
  (return
   [:% ui-static/Div
    #{[design variant
       :style [{:flexDirection "row"
                :margin 5
                :maxWidth 500}
               (:.. (k/arrayify style))]]}
    (j/map columns columnFn)]))

;;
;; Entry Row
;;

(defn.js EntryRowText
  "creates the entry row text"
  {:added "4.0"}
  [props]
  (var #{design
         entry
         column
         style} props)
  (var #{[(:= format k/identity)]} column)
  (var text (-/get-entry-data entry (. column data)))
  (return
   (r/% ui-text/P
        (j/assignNew
         props
         {:style [{:flex 1}
                  (:.. (k/arrayify style))]})
        (format text props))))

(defn.js EntryRow
  "creates the entry row"
  {:added "4.0"}
  [#{design
     variant
     entry
     impl
     props
     style}]
  (var #{[columns
          (:= header {})]} impl)
  (var columnFn
       (fn [column]
         (var #{name} column)
         (var #{[style
                 (:.. rprops)]} (or (k/get-in props [name])
                                    {}))
         (var Component (or (. column component)
                            -/EntryRowText))
         (return
          (r/% Component
               (j/assign
                #{[design
                   entry
                   :style [(:.. (k/arrayify (. header style)))
                           (:.. (k/arrayify style))]
                   column
                   :key (. column name)]}
                (k/get-in props [name]))))))
  (return
   [:% ui-static/Div
    {:design design
     :variant (or variant
                  {:bg {:key "background"
                        :tone "sharpen"}})
     :style [{:flexDirection "row"
              :alignContent "space-between"
              :justifyContent "space-between"
              :maxWidth 500}
             (:.. (k/arrayify style))]}
    (j/map columns columnFn)]))

;;
;; Entry Card
;;

(defn.js EntryCardTitle
  "creates entry card title"
  {:added "4.0"}
  [props]
  (var #{design
         entry
         section
         style} props)
  (var #{[(:= format k/identity)]} section)
  (var text (-/get-entry-data entry (. section data)))
  (return
   [:% n/Row
    (r/% ui-text/H5
         props
         (format (or text "")
                 props))]))

(defn.js EntryCardAvatar
  "creates entry card avatar"
  {:added "4.0"}
  [props]
  (var #{design
         entry
         section
         style} props)
  (var #{[(:= format k/identity)]} section)
  (var text (-/get-entry-data entry (. section data)))
  (return
   (r/% ui-text/Avatar
        {:design design
         :text  (format text props)
         :style [{:margin 10}
                 (:.. (k/arrayify style))]})))

(defn.js EntryCardBodyPair
  "creates entry body pair"
  {:added "4.0"}
  [props]
  (var #{design
         entry
         column
         style
         styleTitle
         styleText} props)
  (var #{[(:= format k/identity)
          name]} column)
  (var titleProps (j/assignNew
                   props
                   #{design {:style [{:fontWeight "500"}
                                     (:.. (k/arrayify styleTitle))]}}))
  
  (var text (-/get-entry-data entry (. column data)))
  (var textProps  (j/assignNew
                   props
                   #{design {:style [{:marginLeft 10}
                                     (:.. (k/arrayify styleText))]}}))
  (return
   [:% n/Row
    {:style {:justifyContent "space-between"}}
    (r/% ui-text/P titleProps (+ name ":"))
    (r/% ui-text/P textProps (format text props))]))

(defn.js EntryCardBodyGroup
  "creates entry body group"
  {:added "4.0"}
  [#{[design
      entry
      group
      (:.. rprops)]}]
  (return
   [:% n/View
    #{design
      {:style [{:marginRight 20}
               (. group style)]}}
    (j/map (. group columns)
           (fn [column]
             (return
              (r/% -/EntryCardBodyPair
                   (j/assign #{{:key (. column name)}
                               design
                               entry
                               column}
                             rprops)))))]))

(defn.js EntryCardBody
  "creates entry card body"
  {:added "4.0"}
  [props]
  (var #{design
         entry
         section
         style} props)
  (cond (k/arr? section)
        (return
         [:% n/Row
          (-> section 
              (j/map (fn:> [group i]
                       [:% -/EntryCardBodyGroup
                        #{design
                          entry
                          group
                          {:key i}}])))])
        
        :else
        (do (var #{[(:= format k/identity)]} section)
            (var text (-/get-entry-data entry (. section data)))
            (return
             (r/% ui-text/P
                  (j/assignNew
                   props
                   #{design
                     {:style [{:flex 1}
                              (:.. (k/arrayify style))]}})
                  (format text props))))))

(defn.js EntryCard
  "creates the entry row"
  {:added "4.0"}
  [props]
  (var #{design
         variant
         entry
         impl
         style
         noAvatar
         noTitle
         noBody} props)
  (var #{sections} impl)
  (var #{title
         body
         avatar} sections)
  (var sectionFn
       (fn [name section defaultComponent]
         (var Component (or (. section component)
                            defaultComponent))
         (return
          (r/% Component
               (j/assign
                #{[design
                   entry
                   section]}
                props
                (k/get-in props ["props" name]))))))
  (return
   [:% ui-static/Div
    {:design design
     :variant (or variant
                  {:bg {:key "background"
                        :tone "sharpen"}})
     :style [{:flex 1
              :padding 5}
             (:.. (k/arrayify style))]}
    [:% n/Row
     (:? (and avatar
              (not noAvatar))
         [:<>
          (sectionFn "avatar" avatar -/EntryCardAvatar)
          [:% n/Padding {:style {:width 5}}]]
         [:% n/Padding {:style {:width 5}}])
     [:% n/View
      {:style {:flex 1
               :marginTop 10}}
      (:? (and title
               (not noTitle))
          [:<>
           (sectionFn "title" title -/EntryCardTitle)
           [:% n/Padding {:style {:height 5}}]])
      (:? (and body
               (not noBody))
          (sectionFn "body" body -/EntryCardBody))]]]))

;;
;; Entry
;;

(defn.js Entry
  "creates an entry"
  {:added "4.0"}
  [props]
  (var #{impl} props)
  (cond (== "card" (. impl type))
        (return
         (r/% -/EntryCard props))

        (== "row" (. impl type))
        (return
         (r/% -/EntryRow props))))

(defn.js EntryTable
  [props]
  (var #{design
         entries
         impl
         style} props)
  (return
   [:<> 
    [:% -/EntryRowHeader
     #{design
       {:impl impl}}]
    [:% ui-static/ScrollView
     {:design design}
     [:% n/View
      {:style {:flex 1
               :margin 10}}
      (j/map entries
             (fn:> [entry]
               [:% -/EntryRow
                #{design entry
                  {:key  (. entry id)
                   :impl impl
                   :style style}}]))]]]))

(def.js MODULE (!:module))
