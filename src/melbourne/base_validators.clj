(ns melbourne.base-validators
  (:require [lang.core :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js is-true
  [message]
  (return ["is-true" {:message (or message "Always true.")
                      :check (fn:> true)}]))

(defn.js is-integer
  "creates an is-integer validator"
  {:added "0.1"}
  [message]
  (return ["is-integer" {:message (or message "Must be integer.")
                         :check (fn [v rec]
                                  (var n (k/to-number v))
                                  (return
                                   (and (k/is-number? n)
                                        (== n (k/round n)))))}]))

(defn.js is-number
  "creates an is-number validator"
  {:added "0.1"}
  [message]
  (return ["is-number" {:message (or message "Must be number.")
                        :check (fn [v rec]
                                 (var n (k/to-number v))
                                 (return (k/is-number? n)))}]))

(defn.js is-accepted
  "creates an is-accepted validator"
  {:added "0.1"}
  [message]
  (return ["is-accepted" {:message (or message "Must be accepted.")
                          :check (fn:> [v rec] (== true v))}]))

(defn.js is-required
  "creates an is-required validator"
  {:added "0.1"}
  [message]
  (return ["is-required" {:message (or message "Required field.")
                          :check (fn:> [v rec] (and (k/not-nil? v)
                                                    (< 0 (k/len v))))}]))

(defn.js is-not-empty
  "creates an is-not-empty validator"
  {:added "0.1"}
  [message]
  (return ["is-not-empty" {:message (or message "Must not be empty.")
                           :check (fn:> [v rec] (and (k/not-nil? v)
                                                     (< 0 (k/len v))))}]))

(defn.js is-at-most
  "creates an is-at-most validator"
  {:added "0.1"}
  [n message]
  (return ["is-at-most" {:message (or message (k/cat "Must have "
                                                     (k/to-string n)
                                                     " or less characters."))
                         :check (fn:> [v rec] (and (k/not-nil? v)
                                                   (>= n (k/len v))))}]))

(defn.js is-at-least
  "creates an is-at-least validator"
  {:added "0.1"}
  [n message]
  (return ["is-at-least" {:message (or message (k/cat "Must have "
                                                      (k/to-string n)
                                                      " or more characters."))
                          :check (fn:> [v rec] (and (k/not-nil? v)
                                                    (<= n (k/len v))))}]))

(defn.js is-length-n
  "creates an is-length-n validator"
  {:added "0.1"}
  [n message]
  (return ["is-length-n" {:message (or message (k/cat "Must be "
                                                      (k/to-string n)
                                                      " characters."))
                          :check (fn:> [v rec] (and (k/not-nil? v)
                                                    (== n (k/len v))))}]))

(defn.js is-same-as
  "creates an is-same-as validator"
  {:added "0.1"}
  [field message]
  (return ["is-same-as" {:message (or message (k/cat "Must be same as " field "."))
                         :check (fn:> [v rec] (== v (k/get-key rec field)))}]))

(def +email-regex+
  #"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9-]{2,}$")

(defn.js is-valid-email
  "creates an is-valid-email validator"
  {:added "0.1"}
  [message]
  (return ["is-valid-email"
           {:message (or message "Must be a valid email.")
            :check (fn [v rec]
                     (return (and (k/not-nil? v)
                                  (. (@! +email-regex+)
                                     (test v)))))}]))

(def.js MODULE (!:module))
