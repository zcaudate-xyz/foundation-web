(ns melbourne.base-font
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[xt.lang.base-lib :as k]
             [js.react-native :as n]
             [js.core :as j]]
   :export [MODULE]})

(def.js fontH1
  {:fontFamily "Lato-Bold"
   :fontSize 30
   :fontWeight "900"})

(def.js fontH2
  {:fontFamily "Lato-Bold"
   :fontSize 24
   :fontWeight "900"})

(def.js fontH3
  {:fontFamily "Lato-Bold"
   :fontSize 20
   :fontWeight "900"})

(def.js fontH4
  {:fontFamily "Lato-Bold"
   :fontSize 16
   :fontWeight "900"})

(def.js fontH5
  {:fontFamily "Lato-Bold"
   :fontSize 14
   :fontWeight "700"})

(def.js fontH6
  {:fontFamily "Lato-Bold"
   :fontSize 12
   :fontWeight "700"})

(def.js fontP
  {:fontFamily "Lato"
   :fontSize 12
   :fontWeight "400"})

(def.js fontText
  {:fontFamily "Lato"
   :fontSize 11
   :fontWeight "400"})

(def.js fontItalic
  {:fontFamily "Lato"
   :fontSize 11
   :fontWeight "400"
   :fontVariant "italic"})

(def.js fontBold
  {:fontFamily "Lato-Bold"
   :fontSize 11
   :fontWeight "800"})

(def.js fontCaption
  (j/assign
   {:fontSize 11
    :fontWeight "500"}
   (n/PlatformSelect {:ios     {:fontFamily "Courier"}
                      :default {:fontFamily "monospace"}})))

(def.js FontStyle
  {:h1 -/fontH1
   :h2 -/fontH2
   :h3 -/fontH3
   :h4 -/fontH4
   :h5 -/fontH5
   :h6 -/fontH6
   :p  -/fontP
   :bold -/fontBold
   :italic -/fontItalic
   :caption -/fontCaption})

(defn.js getFontStyle
  "gets font style gives size"
  {:added "4.0"}
  [font]
  (cond (k/obj? font)
        (return font)

        (k/is-number? font)
        (return {:fontSize font})

        :else
        (return (or (k/get-key -/FontStyle font)
                    -/fontText))))

(def.js MODULE (!:module))
