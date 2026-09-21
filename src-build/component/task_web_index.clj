^{:no-test true}
(ns component.task-web-index
  (:require
   [lang.core :as l]
   [std.lib :as h]
   [std.string :as str]
   [std.make :as make]
   [std.lib.link :as link]
   [component.build-web-index :as build-web-index]))

(defn task-gh-push
  [& [message]]
  (make/build-all build-web-index/WEB-INDEX)
  (make/gh:dwim-push build-web-index/WEB-INDEX message))

(defn task-gh-init
  [& [message]]
  (make/build-all build-web-index/WEB-INDEX)
  (make/gh:dwim-init build-web-index/WEB-INDEX message))

(defn -main
  []
  (try
    (h/p "")
    (h/p "********************************************************************")
    (h/p "********************************************************************")
    (h/p "")
    (h/p "GITHUB PUSH")
    (h/p "")
    (h/p "********************************************************************")
    (h/p "********************************************************************")
    (task-gh-push)
    (catch Throwable t
      (prn t)
      (System/exit 1)))
  
  (System/exit 0))

(comment)
