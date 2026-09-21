(ns pune.ui-breadcrumb
  (:require [lang.core :as l]
            [std.lib :as h]
            [std.lib.link :as link]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [
             [melbourne.ui-static :as ui-static]
             [xt.lang.common-string :as base-text]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]]
   :export [MODULE]})

(defn.js Breadcrumb
    "Constructs a Breadcrumb"
    {:added "0.1"}
  [#{[design
      mini
      variant
      style
      root
      rootOnly
      branchOnly
      path
      text
      noBanner]}]
  (var routePath (xtd/arr-assign [(:.. (xtd/arrayify (:? branchOnly [] root)))]
                               (xtd/arrayify (:? rootOnly
                                               []
                                               path))))
  (var routeString (xtd/arr-map routePath
                          (fn:> [s] s (base-text/to-uppercase (base-text/tag-string s)))))
  (:= text (or text
              (base-text/join "   /   "
                              routeString)) )
  (return
   [:% ui-static/Text
    {:design design
     :variant (Object.assign
               {:font "h3"
                :fg (:? noBanner
                        {:key "primary"
                         :tone "flatten"}
                        {:key "background"
                         :tone "sharpen"})}
               variant)
     :numberOfLines 1
     :style [{:paddingVertical 5
              :fontWeight "900"
              #_#_:textAlign ""}
             (xtd/arrayify style)]}
    text]))

(def.js MODULE (!:module))
