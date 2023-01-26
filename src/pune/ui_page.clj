(ns pune.ui-page
  (:require [std.lang :as l]
            [std.lib :as h]
            [std.lib.link :as link]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn]]
             [melbourne.ui-static :as ui-static]
             [pune.ui-sidemenu :as ui-sidemenu]
             [pune.ui-breadcrumb :as ui-breadcrumb]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js PageLayoutHeader
  "constructs the page header"
  {:added "0.1"}
  [#{[design
      mini
      actions
      headerId
      routeKey
      routeRoot
      singleRoute
      noBanner
      noBreadcrumb
      overrideBreadcrumb
      (:= overrideToolbar {})
      children]}]
  (var [toolbar setToolbar] (r/local))
  (var bprops
       (j/assign
        #{design mini actions noBanner}
        (:? singleRoute
            {:root []
             :path (k/first routeRoot)}
            {:root routeRoot
             :path (:? routeKey (j/toUpperCase (+ "" routeKey)))})
        overrideBreadcrumb))
  (var tprops
       (j/assign
        {:design design
         #_#_:variant {:bg (:? noBanner
                           {:key "background"
                            :tone "diminish"}
                           {:key "primary"})}}
        overrideToolbar
        {:style [{;;:paddingTop 1
                  #_#_:height (:? (k/not-empty? toolbar)
                              70
                              35)
                  :zIndex 100
                  :overflow "hidden"}
                 (:.. (k/arrayify (. overrideToolbar style)))]}))
  
  (return
   (r/% ui-static/Div
        tprops
        (:? (not noBreadcrumb)
            (r/% ui-breadcrumb/Breadcrumb
                 (j/assign bprops {:variant {:bg {:key "primary"}}
                                   :style
                                   {:padding 10}}))
            children)
        [:% n/Row
         [:% n/PortalSink
          {:name headerId
           :onSource setToolbar
           :style {:flex 1}}]])))

(defn.js PageLayoutMenu
  "constructs the page menu"
  {:added "0.1"}
  [#{design
     mini
     actions
     routeKey
     setRouteKey
     overrideSideMenu
     routeRoot
     sections
     appendId}]
  (var dimensions (n/useWindowDimensions))
  (var override (:? overrideSideMenu (overrideSideMenu routeKey mini)))
  (var menuProps (j/assign
                  #{design
                    mini
                    actions
                    routeKey
                    setRouteKey
                    {:miniTitle (and routeRoot (j/toUpperCase (or (k/first routeRoot) ""))) 
                     :data sections
                     :narrowed (< (. dimensions width)
                                  720)}}
                  override))
  (return
   [:<>
    [:% ui-sidemenu/SideMenu
     #{(:.. menuProps)}]
    [:% n/View
     {:style {:bottom 10
              :right 10
              :zIndex 100
              :position "absolute"}}
     [:% n/PortalSink
      {:name appendId}]]]))

(defn.js PageLayout
  "constructs the page layout"
  {:added "0.1"}
  ([#{[design
       mini
       actions
       sections
       sectionRoutes
       (:= sectionPropsFn (fn:>))
       noBanner
       noSideMenu
       route
       routeRoot
       routeKey
       setRouteKey
       children
       overrideToolbar
       overrideSideMenu
       overrideBreadcrumb
       noBreadcrumb]}]
   (var headerId (r/id))
   (var appendId (r/id))
   (var Component  (k/get-key sectionRoutes routeKey))
   (var cprops (j/assign #{design mini route actions
                           headerId
                           appendId}
                         (sectionPropsFn routeKey)))
   (return
    [:% n/View
     {:style {:flexDirection "row-reverse" :flex 1}}
     (:? (not noSideMenu)
         [:% -/PageLayoutMenu
          #{routeKey
            sections
            mini
            actions
            route
            setRouteKey
            routeRoot
            appendId
            design
            overrideSideMenu}])
     [:% n/View
      {:style {:flex 1}}
      [:% -/PageLayoutHeader
       #{design
         mini
         actions
         headerId
         routeKey
         routeRoot
         noBanner
         overrideToolbar
         overrideBreadcrumb
         noBreadcrumb}]
      children
      (:? Component [:% Component #{(:.. cprops)}])]])))

(defn.js PageLayoutSingle
  "constructs the page layout"
  {:added "0.1"}
  ([#{[design
       mini
       actions
       (:= sectionPropsFn (fn:>))
       noBanner
       noSideMenu
       route
       routeRoot
       children]}]
   (var headerId (r/id))
   (return
    [:% n/View
     {:style {:flexDirection "row-reverse" :flex 1}}
     [:% n/View
      {:style {:flex 1}}
      [:% -/PageLayoutHeader
       #{design
         mini
         actions
         headerId
         routeRoot
         noBanner
         {:singleRoute true}}]
      children]])))

(comment
  (defn.js F02AccountMainBody
  [#{design
     route
     routeKey
     sectionList
     sectionRoutes}]
    
    (return )))

(def.js MODULE (!:module))
