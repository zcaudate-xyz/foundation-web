^{:no-test true}
(ns pune.project-web
  (:require [std.lib :as h]
            [lang.core :as l]
            [std.string :as str]))

(def expo-babel-preset
  (l/emit-as
   :js '[(:= module.exports
            (fn [api]
              (api.cache true)
              (return {:presets ["babel-preset-expo"]})))]))

(def expo-babel-preset-paper
  (l/emit-as
   :js '[(:= module.exports
             (fn [api]
               (api.cache true)
               (return {:presets ["babel-preset-expo"]
                        :plugins ["react-native-paper/babel"]})))]))

(def expo-babel-preset-reanimated
  (l/emit-as
   :js '[(:= module.exports
             (fn [api]
               (api.cache true)
               (return {:presets ["babel-preset-expo"]
                        :plugins ["react-native-reanimated/plugin"]})))]))

(defn expo-app-json
  "creates a expo app.json"
  {:added "4.0"}
  [title & [m slug]]
  {:type :json
   :file "app.json"
   :main (h/merge-nested
          {"expo"
           {"name" title
            "slug" slug
            "version" "1.0.0",
            "orientation" "portrait",
            "icon" "./assets/icon.png",
            "entryPoint" "./src/App.js",
            "splash"
            {"image" "./assets/splash.png",
             "resizeMode" "contain",
             "backgroundColor" "#ffffff"}
            "updates" {"fallbackToCacheTimeout" 0},
            "assetBundlePatterns" ["**/*"]
            "experiments" {"baseUrl" (clojure.core/str "/" slug)}
            "ios" {"supportsTablet" true},
            "android" {"adaptiveIcon" {"foregroundImage" "./assets/adaptive-icon.png",
                                       "backgroundColor" "#FFFFFF"}},
            "web" {"favicon" "./assets/favicon.png"}}}
          m)})

;;
;; Files
;;

(def +expo-babel+
  {:type :script
   :file "babel.config.js"
   :main expo-babel-preset})

(def +expo-makefile+
  {:type  :makefile
   :main  '[[:init
             [yarn install]]
            [:build-web
             [yarn install]
             [npx expo export --platform web]]
            [:dev
             [yarn install]
             [npx expo start --web --port 19007]]
            [:ios
             [yarn install]
             [npx expo start --ios]]
            [:android
             [yarn install]
             [npx expo start --android]]
            [:purge   [npx expo r -c]]]})

(def +expo-gitignore+
  {:type :gitignore
   :main '["node_modules/**/*"
           ".expo/*"
           "npm-debug.*"
           "*.jks"
           "*.p8"
           "*.p12"
           "*.key"
           "*.mobileprovision"
           "*.orig.*"
           "dist/"
           ".DS_Store"
           "yarn.lock"
           "yarn-error.log"]})

(defn expo-package-json
  "creates the expo package.json"
  {:added "4.0"}
  ([name & [m]]
   {:type :package.json
    :main (h/merge-nested
           {"main" "./src/App.js"
            "name" name
            "scripts"
            {"start" "expo start"
             "android" "expo start --android"
             "ios" "expo start --ios"
             "web" "expo start --web"
             "eject" "expo eject"}
            
            "dependencies"
            {"@expo/vector-icons" "^14.1.0"
             "@react-navigation/bottom-tabs" "^7.3.10"
             "@react-navigation/elements" "^2.3.8"
             "@react-navigation/native" "^7.1.6"
             "ethers" "^6.15.0"
             "expo" "~53.0.17"
             "expo-auth-session" "^6.2.1"
             "expo-asset" "~11.1.3"
             "expo-blur" "~14.1.5"
             "expo-constants" "~17.1.7"
             "expo-crypto" "^14.1.5"
             "expo-font" "~13.3.2"
             "expo-haptics" "~14.1.4"
             "expo-image" "~2.3.2"
             "expo-image-picker" "^16.1.4"
             "expo-linking" "~7.1.7"
             "expo-router" "~5.1.3"
             "expo-splash-screen" "~0.30.10"
             "expo-status-bar" "~2.2.3"
             "expo-symbols" "~0.4.5"
             "expo-system-ui" "~5.0.10"
             "expo-web-browser" "~14.2.0"
             "react" "19.0.0"
             "react-dom" "19.0.0"
             "react-native" "0.79.5"
             "react-native-base64" "^0.2.1"
             "react-native-gesture-handler" "~2.24.0"
             "react-native-get-random-values" "^1.11.0"
             "react-native-reanimated" "~3.17.4"
             "react-native-safe-area-context" "5.4.0"
             "react-native-screens" "~4.11.1"
             "react-native-svg" "~15.11.2"
             "react-native-vector-icons" "^10.2.0"
             "react-native-web" "~0.20.0"
             "react-native-webview" "13.13.5"
             "ua-parser-js" "^2.0.4"
             "url" "^0.11.4"
             "uuid" "^11.1.0"
             "react-color" "2.19.3"
             "base-64" "1.0.0"
             "dateformat" "^4"
             "javascript-time-ago" "2.3.11"
             "mustache" "4.2.0"
             "fuse.js" "6.4.6"
             "lightweight-charts" "3.8.0"
             "@metamask/onboarding" "1.0.1"
             "@metamask/detect-provider" "1.2.0"}
            "devDependencies"
            {"@babel/core" "^7.25.2"
             "@types/react" "~19.0.10"
             "eslint" "^9.25.0"
             "eslint-config-expo" "~9.2.0"
             "typescript" "~5.8.3"
             "@expo/metro-runtime" "^5.0.4"}
            "private" true}
           m)}))

