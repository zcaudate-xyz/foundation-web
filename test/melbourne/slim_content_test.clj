(ns melbourne.slim-content-test
  (:use code.test)
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:runtime :websocket
   :config {:id :test/web-main
            :bench false
            :emit {:native {:suppress true}
                   :lang/jsx false}
            :notify {:type :webpage :path "dev/notify"}}
   :require [[js.react :as r]
             [js.react-native :as n :include [:fn]]
             [melbourne.slim-content :as slim-content]
             [js.core :as j]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(def.js base64Icon
  (k/join
   ""
   ["data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAAGXRFWHRTb2"
    "Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAwBQTFRF7c5J78kt+/Xm78lQ6stH5LI36bQh6rcf7s"
    "Qp671G89ZZ8c9V8c5U9+u27MhJ/Pjv9txf8uCx57c937Ay5L1n58Nb67si8tVZ5sA68tJX/Pfr7dF58t"
    "BG9d5e8+Gc6chN6LM+7spN1pos6rYs6L8+47hE7cNG6bQc9uFj7sMn4rc17cMx3atG8duj+O7B686H7c"
    "Al7cEm7sRM26cq/vz5/v767NFY7tJM78Yq8s8y3agt9dte6sVD/vz15bY59Nlb8txY9+y86LpA5LxL67"
    "pE7L5H05Ai2Z4m58Vz89RI7dKr+/XY8Ms68dx/6sZE7sRCzIEN0YwZ67wi6rk27L4k9NZB4rAz7L0j5r"
    "M66bMb682a5sJG6LEm3asy3q0w3q026sqC8cxJ6bYd685U5a457cIn7MBJ8tZW7c1I7c5K7cQ18Msu/v"
    "3678tQ3aMq7tNe6chu6rgg79VN8tNH8c0w57Q83akq7dBb9Nld9d5g6cdC8dyb675F/v327NB6////AA"
    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/LvB3QAAAMFJREFUeNpiqIcAbz0ogwFKm7GgCjgyZMihCLCkc0"
    "nkIAnIMVRw2UhDBGp5fcurGOyLfbhVtJwLdJkY8oscZCsFPBk5spiNaoTC4hnqk801Qi2zLQyD2NlcWW"
    "P5GepN5TOtSxg1QwrV01itpECG2kaLy3AYiCWxcRozQWyp9pNMDWePDI4QgVpbx5eo7a+mHFOqAxUQVe"
    "RhdrLjdFFQggqo5tqVeSS456UEQgWE4/RBboxyC4AKCEI9Wu9lUl8PEGAAV7NY4hyx8voAAAAASUVORK"
    "5CYII="]))

^{:refer melbourne.slim-content/ContentTitle :added "4.0"}
(fact "creates the content title"
  ^:hidden

  (defn.js ContentTitleDemo
    []
    (return
     [:% n/Enclosed
      {:label "melbourne.slim-content/ContentTitle"}
      [:% n/Row
       [:% slim-content/ContentTitle
        {:style {:margin 10}
         :entry {:picture {:uri -/base64Icon}
                 :first "Sarah"
                 :last "Conner"}
         :textField (fn [#{first last}]
                   (return (+ (k/first first)
                              (k/first last))))}]
       [:% slim-content/ContentTitle
        {:style {:margin 10}
         :entry {:picture {:uri -/base64Icon}
                 :first "Sarah"
                 :last "Conner"}
         :imageField "picture"}]]])))

^{:refer melbourne.slim-content/ContentAvatar :added "4.0"}
(fact "creates the content avatar"
  ^:hidden

  (defn.js ContentAvatarDemo
    []
    (return
     [:% n/Enclosed
      {:label "melbourne.slim-content/ContentAvatar"}
      [:% n/Row
       [:% slim-content/ContentAvatar
        {:style {:margin 10}
         :entry {:picture {:uri -/base64Icon}
                 :first "Sarah"
                 :last "Conner"}
         :textField (fn [#{first last}]
                   (return (+ (k/first first)
                              (k/first last))))}]
       [:% slim-content/ContentAvatar
        {:style {:margin 10}
         :entry {:picture {:uri -/base64Icon}
                 :first "Sarah"
                 :last "Conner"}
         :imageField "picture"}]]])))

^{:refer melbourne.slim-content/ContentCard :added "4.0"}
(fact  "creates the content card"
  ^:hidden
  
  (defn.js ContentCardDemo
    []
    (var entry {:picture {:uri -/base64Icon}
                :first "Sarah"
                :last "Conner"})
    
    (return
     [:% n/Enclosed
      {:label "melbourne.slim-content/ContentCard"}
      [:% slim-content/ContentCard
       {:style {:width 300
                #_#_:backgroundColor "red"}
        :styleRight {:padding 10}
        :styleLeft {:padding 10}
        :entry {:picture {:uri -/base64Icon}
                :first "Sarah"
                :last "Conner"}
        :leftComponent slim-content/ContentAvatar
        :leftProps
        {:imageField "picture"}
        :rightComponent slim-content/ContentAvatar
        :rightProps
        (fn:>
          {:textField (fn [#{first last}]
                   (return (+ (k/first first)
                              (k/first last))))})}]])))

^{:refer melbourne.slim-content/HeroCard :added "4.0"}
(fact  "creates the hero card"
  ^:hidden
  
  (defn.js HeroCardDemo
    []
    (return
     [:% n/Enclosed
      {:label "melbourne.slim-content/HeroCard"}
      [:% slim-content/HeroCard
       {:style {:width 300
                #_#_:backgroundColor "red"}
        :styleContent {:padding 10}
        :styleHeader {:padding 10}
        :entry {:picture {:uri -/base64Icon}
                :first "Sarah"
                :last "Conner"}
        :headerComponent slim-content/ContentAvatar
        :headerProps
        {:imageField "picture"}
        :contentComponent slim-content/ContentAvatar
        :contentProps
        (fn:>
          {:textField (fn [#{first last}]
                        (return (+ (k/first first)
                                   (k/first last))))})}]]))
  
  (def.js MODULE (!:module)))
