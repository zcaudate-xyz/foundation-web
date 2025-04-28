(ns pune.web.debug-common
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[xt.lang.base-lib :as k]
             [xt.lang.event-log :as event-log]
             [js.core :as j]
             [js.cell :as cl]
             [js.react.ext-box :as ext-box]
             [js.react.ext-log :as ext-log]
             [js.react.ext-cell :as cr]
             [js.react-native :as n :include [:fn]]
             [statslink.app.model-constant :as mc]
             [statslink.app.model-system :as model-system]
             [statslink.app.runtime :as rt]]
   :export [MODULE]})

;;
;; SCHEMA
;;

(defn.js displayInfo
  "displays a brief info"
  {:added "0.1"}
  [v branch parents]
  (var header (k/arr-join [(:.. parents) branch]
                          ", "))
  (return
   [:% n/View
    {:style {:flex 1
             :overflow "auto"}}
    [:% n/Text
     {:style {:color "#888"
              :fontFamily "monospace"
              :fontSize 11}}
     (+ header
        "\n\n"
        (n/format-entry v))]]))

(defn.js SchemaPanel
  "Constructs a schema panel"
  {:added "0.1"}
  [#{[context
      setTable
      table
      setColumn
      column
      (:.. rprops)]}]
  (var schema  (cr/listenCell mc/S_DB_SCHEMA "success" {} context))
  (return
   [:% n/TreePane
    #{[:tree schema
       :levels [{:type "list"
                 :initial table
                 :setInitial setTable
                 :listWidth 150}
                {:type "list"
                 :branchesFn (fn:> [_tree parents schema]
                               (model-system/schema-columns-fn schema (k/first parents))) 
                 :initial column
                 :setInitial setColumn
                 :width 150
                 :displayFn -/displayInfo}]
       (:.. rprops)]}]))

(defn.js ViewsPanel
  "Constructs a views panel"
  {:added "0.1"}
  [#{[context
      setTable
      table
      setView
      view
      (:.. rprops)]}]
  (var views   (cr/listenCell mc/S_DB_VIEWS "success" {} context))
  (return
   [:% n/TreePane
    #{[:tree views
       :levels [{:type "list"
                 :initial table
                 :setInitial setTable}
                {:type "list"
                 :branchesFn (fn:> [_tree parents view]
                               (model-system/view-entries-fn view (k/first parents))) 
                 :initial view
                 :setInitial setView
                 :targetFn
                 (fn:> [_tree branch parents views]
                   (k/get-in views [(k/first parents)
                                    (k/unpack (k/json-decode (or branch "[null, null]")))]))
                 :displayFn -/displayInfo}]
       (:.. rprops)]}]))

(defn.js RoutesPanel
  "Constructs a routes panel"
  {:added "0.1"}
    [#{[context
      setGroup
      group
      setRoute
      route
      (:.. rprops)]}]
  (var locals   (cr/listenCell mc/S_ROUTE_LOCAL "success" {} context))
  (var remotes  (cr/listenCell mc/S_ROUTE_REMOTE "success" {} context))
  (var grouped  (model-system/routes-build-grouped remotes locals))
  (:= (!:G GROUPED) grouped)
  (return
   [:% n/TreePane
    #{[:tree grouped
       :levels [{:type "list"
                 :width 15
                 :initial group
                 :setInitial setGroup
                 :tabsFormat (fn:> [s] (+ " " s " "))
                 :formatFn k/json-encode}
                {:type "list"
                 :initial route
                 :setInitial setRoute
                 :width 20
                 :displayFn -/displayInfo}]
       (:.. rprops)]}]))


(defn.js EventsPanelLog
  "displays a brief info"
  {:added "0.1"}
  [#{log}]
  (when (not log)
    (return [:% n/View]))
  (var last (ext-log/listenLogLatest log))
  (return
   [:% n/View
    {:style {:flex 1
             :overflow "auto"}}
    [:% n/Text
     {:style {:color "#888"
              :fontFamily "monospace"
              :fontSize 11}}
     (n/format-entry (event-log/get-tail log 20))]]))

(defn.js EventsPanel
  "Constructs an events panel"
  {:added "0.1"}
  [#{[setEventType
      eventType
      (:.. rprops)]}]
  (var events  (ext-box/listenBox (rt/EVENTS) []))
  (return
   [:% n/TreePane
    #{[:tree events
       :levels [{:type "list"
                 :initial eventType
                 :setInitial setEventType
                 :tabsFormat (fn:> [s] (+ " " s " "))
                 :displayFn (fn [log]
                              (return [:% -/EventsPanelLog #{log}]))}]
       (:.. rprops)]}]))

(defn.js ModelsPanel
  "Constructs a models panel"
  {:added "0.1"}
  [#{[context
      setModel
      model
      setView
      view
      (:.. rprops)]}]
  (var cell (cl/get-cell context))
  (return
   [:% n/TreePane
    #{[:tree cell
       :levels [{:type "list"
                 :initial model
                 :setInitial setModel
                 :branchesFn
                 (fn:> [cell]
                   (k/sort (cl/list-models cell)))
                 :targetFn
                 (fn [cell model]
                   (return (cl/get-model model cell)))}
                {:type "tabs"
                 :initial view
                 :setInitial setView
                 :tabsFormat (fn:> [s] (+ " " s " "))
                 :branchesFn 
                 (fn [model parents cell]
                   (when (and (k/first parents)
                              (cl/get-model (k/first parents) cell))
                     (return (cl/list-views (k/first parents) cell)))
                   (return []))
                 :targetFn
                 (fn [model modelKey parents cell]
                   (var #{output input} (or (cl/get-view
                                             [(:.. (j/arrayify parents))
                                              modelKey]
                                             cell)
                                            {}))
                   (return (j/assign #{input} output)))
                 :displayFn -/displayInfo}]
       (:.. rprops)]}]))

(def.js MODULE (!:module))

(comment

  
  (defn.js LivePanel
    "views model group data"
    {:added "0.1"}
    []
    (var live    (v/val (live-market/LIVE)))
    (return [:% ui/KeyListPane
             {:tree live
              :keysFn (fn:> [live] (k/sort (k/arr-filter (k/obj-keys live)
                                                         (fn:> [k] (not= k "__reset__")))))}]))

  )
