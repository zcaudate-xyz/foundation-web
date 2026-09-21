(ns xt.lang.base-lib
  "Compatibility helpers for foundation-base 4.1.

  foundation-base split the old base-lib module into common-lib,
  common-data, common-string, common-math and spec-base.  Melbourne and Pune
  still use the compact base-lib vocabulary, so keep that vocabulary local to
  foundation-web while routing it to the current modules."
  (:require [lang.core :as l]))

(l/script :xtalk
  {:require [[xt.lang.spec-base :as xt]
             [xt.lang.common-lib :as lib]
             [xt.lang.common-data :as data]
             [xt.lang.common-string :as string]
             [xt.lang.common-math :as math]
             [xt.lang.common-tree :as tree]
             [xt.lang.common-sort-by :as sort-by]
             [xt.lang.common-trace :as trace]]
   :export [MODULE]})

;; Basic predicates and data helpers retain their current implementations.
(def.xt F lib/F)
(def.xt T lib/T)
(def.xt identity lib/identity)
(def.xt fn? lib/is-function?)
(def.xt arr? lib/is-array?)
(def.xt obj? lib/is-object?)
(def.xt nil? lib/nil?)
(def.xt not-nil? lib/not-nil?)
(def.xt is-number? lib/is-number?)
(def.xt is-string? lib/is-string?)
(def.xt is-empty? data/is-empty?)
(def.xt not-empty? data/not-empty?)
(def.xt first data/first)
(def.xt second data/second)
(def.xt last data/last)
(def.xt id-fn data/id-fn)
(def.xt key-fn data/key-fn)
(def.xt arrayify data/arrayify)
(def.xt get-in data/get-in)
(def.xt obj-assign data/obj-assign)
(def.xt obj-assign-nested data/obj-assign-nested)
(def.xt obj-filter data/obj-filter)
(def.xt obj-from-pairs data/obj-from-pairs)
(def.xt obj-keys data/obj-keys)
(def.xt obj-map data/obj-map)
(def.xt obj-omit data/obj-omit)
(def.xt obj-pairs data/obj-pairs)
(def.xt obj-pick data/obj-pick)
(def.xt arr-filter data/arr-filter)
(def.xt arr-foldl data/arr-foldl)
(def.xt arr-group-by data/arr-group-by)
(def.xt arr-map data/arr-map)
(def.xt arr-mapcat data/arr-mapcat)
(def.xt arr-juxt data/arr-juxt)
(def.xt arr-omit data/arr-omit)
(def.xt arr-range data/arr-range)
(def.xt arr-repeat data/arr-repeat)
(def.xt arr-reverse data/arr-reverse)
(def.xt arr-rslice data/arr-rslice)
(def.xt arr-slice data/arr-slice)
(def.xt arr-some data/arr-some)
(def.xt arr-append data/arr-assign)
(def.xt arr-sort data/arr-sort)
(def.xt template-entry data/template-entry)
(def.xt template-fn data/template-fn)
(def.xt eq-nested tree/eq-nested)
(def.xt sort-by sort-by/sort-by)
(def.xt capitalize string/capitalize)
(def.xt join string/join)
(def.xt split-long string/split-long)
(def.xt to-fixed string/to-fixed)
(def.xt to-number lib/to-number)
(def.xt to-string lib/to-string)
(def.xt trim string/trim)
(def.xt div lib/div)
(def.xt gt lib/gt)
(def.xt gte lib/gte)
(def.xt lt lib/lt)
(def.xt lte lib/lte)
(def.xt lcm math/lcm)
(def.xt mix math/mix)
(def.xt round math/round)

;; The old arr-join argument order was [array separator].
(defn.xt arr-join
  [arr separator]
  (return (string/join separator arr)))

(defn.xt sort
  [arr]
  (return (data/arr-sort arr lib/identity lib/lt)))

;; Primitive operations are macros in spec-base.  Re-export them under the
;; names used by the existing Melbourne/Pune modules.
(defmacro.xt cat
  [x y & more]
  (apply list (quote x:cat) x y more))

(defmacro.xt len
  [value]
  (list (quote x:len) value))

(defmacro.xt get-key
  ([obj key]
   (list (quote x:get-key) obj key))
  ([obj key default]
   (list (quote x:get-key) obj key default)))

(defmacro.xt err
  [message]
  (list (quote x:err) message))

(defmacro.xt random
  []
  (list (quote x:random)))

(defmacro.xt now-ms
  []
  (list (quote x:now-ms)))

(defmacro.xt json-encode
  [value]
  (list (quote x:json-encode) value))

(defmacro.xt json-decode
  [value]
  (list (quote x:json-decode) value))

(defmacro.xt unpack
  [value]
  (list (quote x:unpack) value))

(defmacro.xt floor
  [value]
  (list (quote x:m-floor) value))

(defmacro.xt log10
  [value]
  (list (quote x:m-log10) value))

(defmacro.xt mod
  [x y]
  (list (quote x:m-mod) x y))

(defmacro.xt pow
  [x y]
  (list (quote x:m-pow) x y))

(defmacro.xt max
  [x y & more]
  (apply list (quote x:m-max) x y more))

(defmacro.xt min
  [x y & more]
  (apply list (quote x:m-min) x y more))

(defmacro.xt for:array
  ([[entry collection] & body]
   (apply list (quote for:array) [entry collection] body)))

(defmacro.xt for:index
  ([[entry bounds] & body]
   (apply list (quote for:index) [entry bounds] body)))

;; Keep metadata/debug calls source-compatible with the current trace module.
(defmacro.xt meta:info
  [& args]
  (apply list (quote xt.lang.common-trace/meta:info) args))

(defmacro.xt LOG!
  [& args]
  (apply list (quote xt.lang.common-trace/LOG!) args))

(defn.xt walk
  [obj pre-fn post-fn]
  (:= obj (pre-fn obj))
  (cond (nil? obj)
        (return (post-fn obj))

        (obj? obj)
        (do (var out {})
            (for:object [[key value] obj]
              (x:set-key out key (walk value pre-fn post-fn)))
            (return (post-fn out)))

        (arr? obj)
        (do (var out [])
            (for:array [value obj]
              (x:arr-push out (walk value pre-fn post-fn)))
            (return (post-fn out)))

        :else
        (return (post-fn obj))))

(def.js MODULE (!:module))
