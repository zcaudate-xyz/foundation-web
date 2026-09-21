(ns pune.layout-toplevel
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :require [[js.core :as j]
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
                                 (j/assign #{design}
                                           bodyProps)))
  (var headerVisible (and showGuest
                          (or (not mini)
                              (not showAuth))))
  (var menuVisible (not showGuest))
  
  (var miniProps
       {:bottomSize 45
        :bottomComponent menu
        :bottomProps (j/assign #{design mini} menuProps)
        :bottomVisible  menuVisible
        :bottomFade true
        :bottomStyle {:backgroundColor (base-palette/getColor
                                        palette
                                        {:key "background"
                                         :tone "sharpen"})}})
  (var normalProps
       {:leftComponent menu
        :leftProps (j/assign #{design mini}
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
        :bottomProps (j/assign #{design}
                               consoleProps)})
  (var frameProps
       (:? mini
           (j/assign miniProps rprops)
           (j/assign normalProps rprops)))
  (return
   [:% ui-frame/Frame
    #{[:topComponent header
       :topProps (j/assign #{design}
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
      (j/future-delayed [100]
        (setLeftVisible (not showGuest)))
      (j/future-delayed [300]
        (setTopVisible showGuest)))
    (when (not showGuest)
      (j/future-delayed [100]
          (setTopVisible showGuest))
      (j/future-delayed [300]
        (setLeftVisible (not showGuest))))))
