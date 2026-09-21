(ns pune.layout-toplevel
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :require [[xt.lang.spec-promise :as promise]
             
             [js.react :as r]
             [js.react-native :as n :include [:fn]]
             [js.react-native.ui-frame :as ui-frame]
             [melbourne.base-palette :as base-palette]]
   :export [MODULE]})

(defn.js LayoutMain
  "constructs the main layout"
  {:added "0.1"}
  [#{[design
      mini
      showAuth
      showGuest
      header
      headerProps
      consoleView
      consoleProps
      consoleShow
      body
      bodyProps
      menu
      menuProps
      (:.. rprops)]}]
  (var palette (base-palette/designPalette design))
  (var bodyView (r/% (or body n/View)
                                 (Object.assign #{design}
                                           bodyProps)))
  (var headerVisible (and showGuest
                          (or (not mini)
                              (not showAuth))))
  (var menuVisible (not showGuest))
  
  (var miniProps
       {:bottomSize 45
        :bottomComponent menu
        :bottomProps (Object.assign #{design mini} menuProps)
        :bottomVisible  menuVisible
        :bottomFade true
        :bottomStyle {:backgroundColor (base-palette/getColor
                                        palette
                                        {:key "background"
                                         :tone "sharpen"})}})
  (var normalProps
       {:leftComponent menu
        :leftProps (Object.assign #{design mini}
                            menuProps)
        :leftVisible  menuVisible
        :leftFade true
        :leftStyle {:backgroundColor (base-palette/getColor
                                      palette
                                      {:key "background"})}
        :bottomSize 400
        :bottomStyle {:backgroundColor (base-palette/getColor
                                        palette
                                        {:key "neutral"})}
        :bottomVisible (:? mini
                           menuVisible
                           consoleShow)
        :bottomComponent consoleView
        :bottomProps (Object.assign #{design}
                               consoleProps)})
  (var frameProps
       (:? mini
           (Object.assign miniProps rprops)
           (Object.assign normalProps rprops)))
  (return
   [:% ui-frame/Frame
    #{[:topComponent header
       :topProps (Object.assign #{design}
                           headerProps)
       :topStyle {:backgroundColor (base-palette/getColor
                                    palette
                                    {:key "primary"})}
       :topSize 60
       :topVisible   headerVisible
       (:.. frameProps)]}
    bodyView]))

(def.js MODULE (!:module))


(comment

  (var [topVisible setTopVisible]   (r/local showGuest))
  (var [leftVisible setLeftVisible] (r/local (not showGuest)))
  (r/watch [showGuest]
    (when showGuest
      (promise/x:with-delay 100 (fn []
        (setLeftVisible (not showGuest))))
      (promise/x:with-delay 300 (fn []
        (setTopVisible showGuest))))
    (when (not showGuest)
      (promise/x:with-delay 100 (fn []
          (setTopVisible showGuest)))
      (promise/x:with-delay 300 (fn []
        (setLeftVisible (not showGuest)))))))
