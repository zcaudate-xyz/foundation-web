(ns melbourne.base-palette
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react-native.helper-color :as c]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(def.js PaletteRatio
  {:ionian     (/ 1 8)
   :dorian     (/ 2 8)
   :phrygian   (/ 3 8)
   :lydian     (/ 4 8)
   :mixolydian (/ 5 8)
   :aeolian    (/ 6 8)
   :locrian    (/ 7 8)})

(def.js PaletteBase
  {:lightNeutral   "#222"
   :lightError     "#9c1f1f"
   :lightWarn      "#ffffe0"
   :darkNeutral    "#eee"
   :darkError      "#f55151"
   :darkWarn       "#aaaae0" #_"#61604a"})

(def.js PalettePrimary
  {:purple   {:lightPrimary   "#5f4abd"
              :darkPrimary    "#aa97fc"}
   :indigo   {:lightPrimary   "#3F33BD"
              :darkPrimary    "#837AE6"}
   :blue     {:lightPrimary   "#1A5D9E"
              :darkPrimary    "#409EFF"}
   :default  {:lightPrimary   "#378E75" #_"#23C67E"  #_"#18785d"
              :darkPrimary    "#38e8b6" #_"#35DB92"}
   :green    {:lightPrimary   "#3D9735"
              :darkPrimary    "#5DDE51" #_"7fdb63"}})

(defn.js createPalette
  "gets the palette given type and color"
  {:added "4.0"}
  [type color]
  (cond (== type "dark")
        (return
         {:mainPrimary   (. (or (. -/PalettePrimary [color])
                                (. -/PalettePrimary default))
                            darkPrimary)
          :mainNeutral    (. -/PaletteBase darkNeutral)
          :mainError      (. -/PaletteBase darkError)
          :mainWarn       (. -/PaletteBase darkWarn)
          :mainBackground (. -/PaletteBase lightNeutral)
          :isDark true})
        
        :else
        (return
         {:mainPrimary    (. (or (. -/PalettePrimary [color])
                                 (. -/PalettePrimary default))
                             lightPrimary)
          :mainNeutral    (. -/PaletteBase lightNeutral)
          :mainError      (. -/PaletteBase lightError)
          :mainWarn       (. -/PaletteBase lightWarn)
          :mainBackground (. -/PaletteBase darkNeutral)
          :isDark false})))

(defn.js toneFlatten
  "flattens the color to the center"
  {:added "4.0"}
  [color isDark]
  (return
   (c/lighten color
              (:? isDark -0.8 0.8))))

(defn.js toneDiminish
  "diminishes the color to the center"
  {:added "4.0"}
  [color isDark]
  (return
   (c/lighten color
              (:? isDark -0.98 0.98))))

(defn.js toneAugment
  "augments the color to the edge"
  {:added "4.0"}
  [color isDark]
  (return
   (c/lighten color
              (:? isDark 0.9 -0.9))))

(defn.js toneSharpen
  "sharpens the color to the edge"
  {:added "4.0"}
  [color isDark]
  (return
   (c/lighten color
              (:? isDark 0.8 -0.6))))


(defn.js toneRatio
  "gets the tone ratio either for musical modes, ratios and whole tones"
  {:added "4.0"}
  [ratio]
  (cond (k/is-number? ratio)
        (cond (and (< 0 ratio)
                   (< ratio 1))
              (return ratio)

              :else
              (return (/ ratio 8)))

        :else
        (return (or (k/get-key -/PaletteRatio
                               ratio)
                    0))))

(defn.js getColorRaw
  "gets a color given palette, key, tone and additional parameters"
  {:added "4.0"}
  [palette colorKey tone mixKey ratio]
  (var #{isDark} palette)
  (var color (or (. palette [(+ "main" (k/capitalize (or colorKey
                                                         "primary")))])
                 colorKey))
  (cond (or mixKey
            (== tone "mix"))
        (do (var colorTo (or (. palette [(+ "main" (k/capitalize (or mixKey
                                                                     "background")))])
                             mixKey
                             (. palette mainPrimary)))
            (return (c/mix [color colorTo]
                           (-/toneRatio ratio))))
        
        (k/nil? tone)
        (return color)

        (== tone "sharpen")
        (return (-/toneSharpen color isDark))

        (== tone "flatten")
        (return (-/toneFlatten color isDark))

        (== tone "augment")
        (return (-/toneAugment color isDark))

        (== tone "diminish")
        (return (-/toneDiminish color isDark))
        
        (== tone "lighten")
        (return (c/lighten color (:? isDark
                                     (- 1 (-/toneRatio ratio))
                                     (- (-/toneRatio ratio) 1))))

        (== tone "darken")
        (return (c/lighten color (:? isDark
                                     (- (-/toneRatio ratio) 1)
                                     (- 1 (-/toneRatio ratio)))))
        
        (== tone "saturate")
        (return (c/saturate color (- (-/toneRatio ratio) 1)))
        
        (== tone "desaturate")
        (return (c/saturate color (- 1 (-/toneRatio ratio))))
        
        :else
        (return color)))

(defn.js getColor
  "gets the color given a map"
  {:added "4.0"}
  [palette m]
  (var #{raw} m)
  (cond (k/nil? raw)
        (return
         (-/getColorRaw palette
                        (. m key)
                        (. m tone)
                        (or (. m mix)
                            (. m mixKey))
                        (. m ratio)))

        :else (return raw)))

;;
;; DESIGN
;;

(defn.js invertDesign
  "inverts the design type"
  {:added "4.0"}
  [design]
  (var #{invert} design)
  (return (j/assign {} design {:invert (not invert)})))


(defn.js designPalette
  "gets the palette given design"
  {:added "4.0"}
  [design]
  (var #{override color type invert} (or design {}))
  (:= type (:? invert
               (:? (== type "dark")
                   "light"
                   "dark")
               (or type "light")))
  (return (j/assign (-/createPalette type color)
                    override)))

(defn.js getPalette
  "gets either the palette if exists or generates from design"
  {:added "4.0"}
  [design palette]
  (return (or palette (-/designPalette design palette))))

(def.js MODULE (!:module))
