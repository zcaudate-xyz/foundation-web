(ns melbourne.slim-image
  (:use code.test)
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :playground/web-basic
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react :as r]
             [js.react.ext-form :as ext-form]
             [js.react-native :as n]
             [melbourne.slim-common :as slim-common]
             [melbourne.ui-image :as ui-image]
             [melbourne.base-palette :as base-palette]
             [xt.lang.base-lib :as k]
             [xt.lang.event-form :as event-form]]
   :export [MODULE]})

(defn.js FormImage
  "constructs a image info demo"
  {:added "0.1"}
  [#{[design
      mini
      palette
      form
      meta
      label
      styleLabel
      labelHide labelNone
      field
      fieldProps
      minWidth
      (:= options [])]}]
  (var #{value result} (ext-form/listenField form field
                                             (j/assign {:slim/type "image"
                                                        :fn/type   "field"}
                                                       meta)))
  (when (k/is-empty? value)
    (:= value {}))
  (when (k/is-string? value)
    (:= value (k/json-decode value)))
  
  
  (:= palette (base-palette/getPalette design palette))
  (var #{mainNeutral} palette)
  (var [data setData]   (r/local value))
  (var [photo setPhoto] (r/local))
  (var [blob setBlob]   (r/local))
  (var [waiting setWaiting] (r/local))
  #_(var uri (or (and photo (. photo ["uri"]))
               (and (k/not-empty? data)
                    (or (. data  ["url"])
                        (. data  ["thumbnailUrl"])))))
  (return 
   [:% slim-common/FormEnclosed
    #{design
      mini
      styleLabel
      labelHide labelNone
      label
      minWidth}
    [:% n/View
     {:style {:marginLeft 1
              :marginVertical 3}}
     [:% ui-image/ImagePicker
      #{[design
         photo setPhoto
         blob setBlob
         data setData
         waiting setWaiting
         :size (:? mini 120 140)
         (:.. fieldProps)]}]]]))

(def.js MODULE (!:module))

