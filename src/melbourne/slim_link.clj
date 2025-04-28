(ns melbourne.slim-link
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-util :as ui-util]
             [js.react.ext-view :as ext-view]
             [js.react.ext-form :as ext-form]
             [xt.lang.event-form :as event-form]
             [melbourne.ui-static :as ui-static]
             [melbourne.slim-common :as slim-common]
             [melbourne.slim-select :as slim-select]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js useViewLink
  [props]
  (var #{views
         form
         field
         viewKey
         viewArgs
         viewOpts} props)
  (var links (ext-view/listenView 
              (. views [viewKey])
              "success"))
  (var #{results
         lookup} links)
  (var args nil)
  (var link-id (ext-form/listenFieldValue form field))
  (r/watch [results]
    (when (and (k/not-empty? results)
               (k/nil? link-id))
      (event-form/set-field form field (k/id-fn (k/first results)))))

  (when viewArgs
    (var data (ext-form/listenFormData form))
    (:= args (viewArgs data props))
    (ext-view/useRefreshArgs (. views [viewKey])
                             args
                             (or viewOpts {:remote "none"})))
  (return #{links
            args}))

(defn.js FormLinkDropdown
  "creates a Dropdown"
  {:added "0.1"}
  [props]
  (var aprops (j/assignNew props (. props fieldProps)))
  (var #{views viewKey viewArgs viewOpts
         viewTemplate viewValueFn
         form field} aprops)

  (var #{links
         args} (-/useViewLink aprops))
  (return
   (r/% slim-select/FormDropdown
        (j/assignNew {}
                     props
                     {:key (k/json-encode args)
                      :data     (. links results)
                      :fieldProps
                      {:valueFn  k/id-fn
                       :format (fn [id]
                                 (return (k/template-entry
                                          (k/get-in links ["lookup" id])
                                          viewTemplate)))}}))))

(defn.js FormLinkReadOnly
  "creates a Dropdown"
  {:added "0.1"}
  [props]
  (var #{views viewKey viewArgs viewOpts
         viewTemplate
         form field} (j/assignNew props (. props fieldProps)))
  (var #{args
         links} (-/useViewLink #{views
                                  form
                                  field
                                  viewKey
                                  viewArgs
                                 viewOpts}))
  (return
   (r/% slim-common/FormReadOnly
        (j/assignNew props
                     {:template (fn [e]
                                  (return
                                   (k/template-entry
                                    (k/get-in links ["lookup" (. e [field])])
                                    viewTemplate)))}))))

(defn.js useViewLinkEntry
  [#{views
     viewKey
     viewArgs
     viewOpts}
   entry
   field]
  (var links (ext-view/listenView 
              (. views [viewKey])
              "success"))
  (var #{results
         lookup} links)

  (var args nil)
  (when viewArgs
    (:= args (viewArgs entry))
    (ext-view/useRefreshArgs (. views [viewKey])
                             args
                             (or viewOpts {})))
  (return #{links
            args}))

(defn.js FormLinkEntryReadOnly
  "creates a Dropdown"
  {:added "0.1"}
  [props]
  (var #{views viewKey viewArgs viewOpts
         viewTemplate
         entry field} (j/assignNew props (. props fieldProps)))
  (var #{args
         links} (-/useViewLinkEntry #{views
                                       viewKey
                                       viewArgs
                                       viewOpts}
                                     entry field))
  (return
   (r/% slim-common/FormReadOnly
        (j/assignNew props
                     {:template (fn [e]
                                  (return
                                   (k/template-entry
                                    (k/get-in links ["lookup" (. e [field])])
                                    viewTemplate)))}))))

(def.js MODULE (!:module))
