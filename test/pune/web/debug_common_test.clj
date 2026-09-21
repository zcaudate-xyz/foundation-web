(ns pune.web.debug-common-test
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :dev/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react-native :as n :include [:fn]]
             [js.lib.valtio :as v]
             [pune.web.debug-common :as debug-common]]
   :export [MODULE]})

(defglobal.js State
  (v/make {}))

^{:refer pune.web.debug-common/displayInfo :added "0.1"}
(fact "displays a brief info")

^{:refer pune.web.debug-common/SchemaPanel :added "0.1"}
(fact "Constructs a schema panel"
  ^:hidden
  
  (defn.js SchemaPanelDemo
    []
    (var [table setTable]   (v/useProxyField -/State "pune.web.debug-common/SchemaPanel.table"))
    (var [column setColumn] (v/useProxyField -/State "pune.web.debug-common/SchemaPanel.column"))
    (return
     (n/EnclosedCode 
{:label "pune.web.debug-common/SchemaPanel"
       :style {:flex 1}} 
[:% debug-common/SchemaPanel
       #{[:style {:backgroundColor "black"}
          table setTable
          column setColumn]}]))))

^{:refer pune.web.debug-common/ViewsPanel :added "0.1"}
(fact "Constructs a views panel"
  ^:hidden

  (defn.js ViewsPanelDemo
    []
    (var [table setTable]  (v/useProxyField -/State "pune.web.debug-common/ViewsPanel.table"))
    (var [view setView]    (v/useProxyField -/State "pune.web.debug-common/ViewsPanel.view"))
    (return
     (n/EnclosedCode 
{:label "pune.web.debug-common/ViewsPanel"
       :style {:flex 1}} 
[:% debug-common/ViewsPanel
       #{[table setTable
          view setView]}]))))

^{:refer pune.web.debug-common/RoutesPanel :added "0.1"}
(fact "Constructs a routes panel"
  ^:hidden

  (defn.js RoutesPanelDemo
    []
    (var [group setGroup]  (v/useProxyField -/State "pune.web.debug-common/RoutesPanel.group"))
    (var [route setRoute]  (v/useProxyField -/State "pune.web.debug-common/RoutesPanel.route"))
    (return
     (n/EnclosedCode 
{:label "pune.web.debug-common/RoutesPanel"
       :style {:flex 1}} 
[:% debug-common/RoutesPanel
       #{[group setGroup
          route setRoute]}]))))

^{:refer pune.web.debug-common/EventsPanelLog :added "0.1"}
(fact "log view for the `xt.event.base-log` object")

^{:refer pune.web.debug-common/EventsPanel :added "0.1"}
(fact "Constructs an events panel"
  ^:hidden

  (defn.js EventsPanelDemo
    []
    (var [eventType setEventType] (v/useProxyField -/State "pune.web.debug-common/EventsPanel.eventType"))
    (return
     (n/EnclosedCode 
{:label "pune.web.debug-common/EventsPanel"
       :style {:flex 1}} 
[:% debug-common/EventsPanel
       #{[eventType setEventType]}]))))

^{:refer pune.web.debug-common/ModelsPanel :added "0.1"}
(fact "Constructs a models panel"
  ^:hidden

  (defn.js ModelsPanelDemo
    []
    (var [model setModel]  (v/useProxyField -/State "pune.web.debug-common/ModelsPanel.model"))
    (var [view setView]    (v/useProxyField -/State "pune.web.debug-common/ModelsPanel.view"))
    (return
     (n/EnclosedCode 
{:label "pune.web.debug-common/ModelsPanel"
       :style {:flex 1}} 
[:% debug-common/ModelsPanel
       #{[:style {:backgroundColor "black"}
          model setModel
          view setView]}])))

  (def.js MODULE (!:module)))
