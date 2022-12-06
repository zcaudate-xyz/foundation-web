(ns melbourne.slim
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react.ext-view :as ext-view]
             [js.react.ext-route :as ext-route]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [xt.lang.base-lib :as k]
             [melbourne.ui-toolbar :as ui-toolbar]
             [melbourne.ui-static :as ui-static]
             [melbourne.slim-table-list :as slim-table-list]
             [melbourne.slim-table-toolbar :as slim-table-toolbar]
             [melbourne.slim-table :as slim-table]
             [melbourne.slim-entry :as slim-entry]
             [melbourne.slim-sheet :as slim-sheet]]
   :export [MODULE]})

(def.js TableToolbar slim-table-toolbar/TableToolbar)

(def.js Entry slim-entry/Entry)

(def.js Table slim-table/Table)

(def.js TableList slim-table-list/TableList)

(def.js TableStandard slim-table/TableStandard)

(def.js TableEmbedded slim-table/TableEmbedded)

(def.js Sheet slim-sheet/Sheet)

(def.js SheetHeader slim-sheet/SheetHeader)

(def.js SheetRow slim-sheet/SheetRow)

(def.js SheetBasic slim-sheet/SheetBasic)

(defn.js createEntry
  [props ...args]
  (return
   (r/% slim-entry/Entry props ...args)))

(defn.js entry
  [props impl opts]
  (return
   (r/% slim-entry/Entry (j/assignNew props #{impl} opts))))

(defn.js useLocalPrimitives
  "creates crud control primitives"
  {:added "4.0"}
  [override]
  (:= override (or override {}))
  (var [showCreate
        setShowCreate] (or (. override create)
                           (r/local false)))
  (var [showModify
        setShowModify] (or (. override modify)
                           (r/local false)))
  (var [showDetail
        setShowDetail] (or (. override detail)
                           (r/local)))
  (var [orderBy
        setOrderBy]    (or (. override orderBy)
                           (r/local true)))
  (var [showScroll
        setShowScroll] (or (. override scroll)
                           (r/local true)))
  (var [showHeader
        setShowHeader] (or (. override header)
                           (r/local true)))
  (return #{orderBy
            setOrderBy
            showHeader
            setShowHeader
            showScroll
            setShowScroll
            showDetail
            setShowDetail
            showModify
            setShowModify
            showCreate
            setShowCreate}))

(defn.js useRoutePrimitives
  "creates crud control on route"
  {:added "4.0"}
  [route override]
  (:= override (or override {}))
  (var [showCreate
        setShowCreate] (or (. override create)
                           (ext-route/useRouteParamFlag
                            route
                            "section"
                            "create")))
  (var [showModify
        setShowModify] (or (. override modify)
                           (ext-route/useRouteParam
                            route
                            "modify")))
  
  (var [showDetail
        setShowDetail] (or (. override detail)
                           (ext-route/useRouteParam
                            route
                            "detail")))
  (var [showHeader
        setShowHeader] (or (. override header)
                           (r/local true)))
  (var [showScroll
        setShowScroll] (or (. override scroll)
                           (r/local true)))
  
  (var [orderBy
        setOrderBy]    (or (. override orderBy)
                           (ext-route/useRouteParam
                            route
                            "orderBy")))
  (return #{orderBy
            setOrderBy
            showDetail
            setShowDetail
            showModify
            setShowModify
            showCreate
            showHeader
            setShowHeader
            showScroll
            setShowScroll
            setShowCreate}))

(defn.js useListControl
  "creates show list control based on show"
  {:added "4.0"}
  [#{orderBy
     setOrderBy
     showDetail
     setShowDetail
     showModify
     setShowModify
     showCreate
     setShowCreate}]
  (var routeKey (:? showModify
                    "modify"

                    showDetail
                    "detail"

                    showCreate
                    "create"

                    :else "list"))
  (var [backAction
        setBackAction] (r/local nil))
  (var [showList
        setShowList] [(== routeKey "list")
                      (fn []
                        (when (and showDetail
                                   setShowDetail)
                          (setShowDetail nil))
                        (when (and showModify
                                   setShowModify)
                          (setShowModify nil))
                        (when (and showCreate
                                   setShowCreate)
                          (setShowCreate false)))])
  (return #{routeKey
            showList
            setShowList
            backAction
            setBackAction}))

(defn.js useRouteControl
  "uses the basic route controls"
  {:added "4.0"}
  [route override m]
  (var control (-/useRoutePrimitives route override))
  (return (j/assign (-/useListControl control)
                    control)))

(defn.js useLocalControl
  "uses the local controls"
  {:added "4.0"}
  [m]
  (var control (-/useLocalPrimitives))
  (return (j/assign (-/useListControl control)
                    control
                    m)))

(defn.js getParentProps
  [props]
  (var parentKeys ["entry"
                   "data"
                   "parent"
                   "display"
                   "control"
                   "actions"])
  (return (k/obj-pick props parentKeys)))

(defn.js useParentControl
  "packages parent props"
  {:added "4.0"}
  [props control opts]
  (var #{showDetail
         showCreate
         showModify
         showList} control)
  (var parent (-/getParentProps props))
  (r/init []
    (. parent control (setShowScroll false))
    (return (fn:> (. parent control (setShowScroll true)))))
  (r/watch [showDetail
            showCreate
            showModify]
    (cond (or (and showCreate
                   (not= "disable" (k/get-in opts ["create"])))
              (and showModify
                   (not= "disable" (k/get-in opts ["modify"])))
              (and showDetail
                   (not= "disable" (k/get-in opts ["detail"]))))
          (do (. parent control (setShowHeader false)))

          :else
          (. parent control (setShowHeader true)))
    (return (fn:> (. parent control (setShowHeader true)))))
  (return parent))

(def.js MODULE (!:module))
