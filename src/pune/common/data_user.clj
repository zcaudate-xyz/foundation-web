(ns pune.common.data-user
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[melbourne.base-validators :as validators]
             [js.cell :as cl]
             [statslink.full.link-remote :as link-remote]
             [xt.lang.spec-base :as xt]
             [xt.lang.common-data :as xtd]]
   :export [MODULE]})

(defn.js is-email-available
  "checks that email is available"
  {:added "0.1"}
  [message context]
  (return ["is-email-available"
           {:message (or message "Email already taken")
            :check (fn [v rec]
                     (return (. (link-remote/check-email-available
                                 (cl/get-cell context)
                                 v)
                                (then (xtd/key-fn "data")))))}]))

(defn.js is-nickname-available
  "checks that nickname is available"
  {:added "0.1"}
  [message context]
  (return ["is-nickname-available"
           {:message (or message "Name already taken")
            :check (fn [v rec]
                     (return (. (link-remote/check-nickname-available
                                 (cl/get-cell context)
                                 v)
                                (then (xtd/key-fn "data")))))}]))

(defn.js account-new-validators
  "creates new account validators"
  {:added "0.1"}
  [context]
  (return
   {:email    [(validators/is-valid-email)
               (-/is-email-available nil context)]
    :nickname [(validators/is-at-least 5)
               (-/is-nickname-available nil context)]}))

(def.js MODULE (!:module))
