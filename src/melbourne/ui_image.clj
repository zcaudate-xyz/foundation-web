(ns melbourne.ui-image
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :playground/web-basic
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[xt.lang.spec-promise :as promise]
             [js.core.style :as css]
             [js.react-native :as n :include [:fn]]
             [js.lib.rn-expo :as x :include [:image-picker]]
             [melbourne.ui-swiper :as ui-swiper]
             [melbourne.ui-button :as ui-button]
             [melbourne.base-palette :as base-palette]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]
             [xt.lang.common-math :as xtm]]
   :export [MODULE]})

(defn.js selectImage
  "select image from media library"
  {:added "0.1"}
  [props]
  (var #{[onPhoto
          (:= setPhoto (fn:>))
          (:= setBlob  (fn:>))]} props)
  (return
   (new Promise
    (fn [resolve reject]
      (-> (x/imageLibraryLaunch)
          (promise/x:promise-then
           (fn [res]
             (if (not res.cancelled)
               (do (setPhoto res)
                   (-> (fetch res.uri)
                       (promise/x:promise-then (fn [res]
                                 (return (res.blob))))
                       (promise/x:promise-then (fn [blob]
                                 (var reader (new FileReader))
                                 (:= reader.onload
                                     (fn []
                                       (setBlob reader.result)
                                       (when onPhoto
                                         (onPhoto {:blob reader.result
                                                   :photo res}
                                                  props))
                                       (resolve {:blob reader.result
                                                 :photo res})))
                                 (reader.readAsArrayBuffer blob)))
                       (return)))
               (resolve nil)))))))))

(comment
  "https://picsum.photos/200/300"
  reader.readAsArrayBuffer)

(defn.js ImagePicker
  "picks an image"
  {:added "0.1"}
  [props]
  (var #{[(:= design {})
          setData
          data
          waiting
          onPhoto
          setPhoto
          photo
          setBlob
          blob
          textEmpty
          onClear
          (:= size 130)
          (:= border 0)
          inner
          (:.. rprops)]} props)
  (var subSize (- size (* 2 border)))
  (var #{fgNormal
         bgNormal} (base-palette/designPalette design))
  (var uri (or (and (xtd/not-empty? photo)
                    (. photo ["uri"]))
               (and (xtd/not-empty? data)
                    (or (. data  ["url"])
                        (. data  ["thumbnailUrl"])))))
  (var swipeElem
       [:% ui-swiper/Swiper
        #{[:design design
           :variant {:bg {:key "background" :mix "primary" :ratio 1}
                     :fg {:key "neutral" :mix "primary" :ratio 4}}
           :styleContainer {:backgroundColor bgNormal
                            :borderRadius 4 :height subSize
                            :overflow "hidden"
                            :width subSize}

           :negEnabled true
           :negThreshold (* -0.5 subSize)
           :negFull (* -2 subSize)
           :posEnabled true
           :posThreshold (* 0.5 subSize)
           :posFull (* 2 subSize)
           :onOpened (fn [res]
                       (setPhoto nil)
                       (setBlob nil)
                       (setData nil)
                       (when onClear (onClear))
                       (return true))
           :style {:cursor "grab"
                   :height subSize
                   :width subSize}
           :inner [{:component n/Image
                    :source {:uri uri}
                    :style  [{:borderRadius 4 :height subSize :width subSize}]
                    :transformations
                    (fn:> [#{position pressing}]
                      {:style {:opacity (* (xtm/mix 1 0.8 pressing)
                                           (xtm/mix 1 0 (/ (xtm/abs position)
                                                         (* 2 subSize))))
                               :transform [{:scale (xtm/mix 1 2 (/ (xtm/abs position) subSize))}]}})}
                   (:.. (xtd/arrayify inner))]
           (:.. rprops)]}])
  (return
   [:% n/View
    {:style [{:height size
              :width  size
              :borderRadius 4}
             (css/centered)
             {:backgroundColor bgNormal}]}
    (:? (not uri)
        [:% ui-button/Button
         {:design design
          :onPress (fn:> (-/selectImage props))
          :style [{:height subSize :width subSize
                   :padding 0
                   :paddingHorizontal 0}]
          :text  [:% n/View {:key "label"
                             :style [{:height subSize :width subSize}
                                     (css/centered)]}
                  [:% n/Text
                   {:style {:fontWeight 400
                            :fontSize 13}}
                   (or textEmpty "SELECT")]]
          :transformations
          {:bg (fn [#{pressing}]
                 (return {:style {:transform [{:scale (- 1 (* 0.15 pressing))}]}}))}
          :variant {:bg {:key "background" :mix "primary" :ratio 1}
                    :fg {:key "neutral"}}}]
        (:? waiting
            [:% n/View
             {:style {:opacity 0.3}}
             swipeElem
             [:% n/View
              {:style {:position "absolute"
                       :height "100%"
                       :width "100%"
                       :justifyContent "center"
                       :alignItems "center"}}
              [:% n/ActivityIndicator]]]
            swipeElem))]))

(def.js MODULE (!:module))


(comment
  (defn.js uploadImage
  "uploads image to imagekit"
  {:added "4.0"}
  [#{blob
     photo
     setData
     setUploading
     setUploaded}]
  (let [form (new FormData)]
    (doto form
      (. (append "file" blob))
      (. (append "fileName" (. photo.uri
                               (split "/")
                               (pop))))
      (. (append "folder" "upload"))
      (. (append "publicKey" (j/! imagekit/+public+))))
    (setUploading true)
    (return
     (-> (base-imagekit/imagekit-upload
          (state/token)
          form)
         (j/toJson)
         (promise/x:promise-then (fn [res]
                   (setData (Object.assign res {:type "imagekit"}))))
         (promise/x:promise-finally (fn []
                      (setUploading false)
                       (setUploaded true)))))))
  )
