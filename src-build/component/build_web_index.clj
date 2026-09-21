(ns component.build-web-index
  (:use code.test)
  (:require [lang.core :as l]
            [std.lib :as h]
            [std.string :as str]
            [std.make :as make :refer [def.make]]
            [pune.project-web :as project-web]
            [component.web-index :as web-index]
            #_#_#_
            [statsenv.docker.util :as util]
            [statsenv.docker.build-web-common :as common]
            [statsenv.main.xyz-components]))

(def +readme+
  {:type :readme.md
   :main ["* Foundation Web - Pune and Melbourne"]})

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

(def +github-workflows-build+
  {:type :yaml
   :file ".github/workflows/build.yml"
   :main [[:name "build gh-pages"]
          [:on ["push"]]
          [:jobs
           {:build
            {:runs-on "ubuntu-latest"
             :permissions {:contents "write"}
             :steps
             [{:name "Checkout repo"
               :uses "actions/checkout@v4"}
              {:name "Node Setup"
               :uses "actions/setup-node@v4"
               :with {:node-version "20.x"}}
              {:name "Deploy gh-pages"
               :run
               (str/|
                "make build-web"
                "touch dist/.nojekyll"
                "git config --global user.name github-actions"
                "git config --global user.email github-actions@github.com"
                "cd dist && git init && git add -A && git commit -m 'deploying to gh-pages'"
                "git remote add origin https://x-access-token:${{ github.token }}@github.com/zcaudate-xyz/demo.foundation-web.git"
                "git push origin HEAD:gh-pages --force")}]}}]]})

(def +metro-config+
  {:type :raw
   :file "metro.config.js"
   :main
   ["const { getDefaultConfig } = require('expo/metro-config');"
    ""
    "const config = getDefaultConfig(__dirname);"
    ""
    "// Extend asset and source extensions"
    "config.resolver.assetExts.push('db', 'ttf'); // Add 'ttf' for TrueType Fonts"
    "config.resolver.sourceExts.push('db'); // If you have custom '.db' files that need resolving"
    ""
    "module.exports = config;"]})
  
(def.make WEB-INDEX
  {:tag      "web-index"
   :build    ".build/web-index"
   :github   {:repo   "zcaudate-xyz/demo.foundation-web"
              :private true
              :description "Web Index"}
   :sections {:common [+readme+
                       +expo-makefile+
                       +github-workflows-build+
                       +metro-config+]
              :node   [{:type :gitignore,
                        :main
                        ["node_modules/**/*"
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
                         "yarn-error.log"]}
                       {:type :json
                        :file "app.json"
                        :main  {"expo"
                                {"name" "Web Index"
                                 "slug" "web index"
                                 "version" "1.0.0",
                                 "orientation" "portrait",
                                 "entryPoint" "./src/App.js",
                                 "splash"
                                 {"resizeMode" "contain",
                                  "backgroundColor" "#ffffff"}
                                 "updates" {"fallbackToCacheTimeout" 0},
                                 "assetBundlePatterns" ["**/*"]
                                 "experiments" {"baseUrl" "/demo.foundation-web"}
                                 "ios" {"supportsTablet" true},}}}
                       
                       {:type :package.json,
                        :main {"main" "./src/App.js"
                               "name" "web-index"
                               "scripts" {"start" "expo start"
                                          "android" "expo start --android"
                                          "ios" "expo start --ios"
                                          "web" "expo start --web"
                                          "eject" "expo eject"}
                               "private" true
                               "homepage" "/demo.foundation-web"
                               "dependencies" {"@expo/vector-icons" "^14.1.0"
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
                               "devDependencies" {"@babel/core" "^7.25.2"
                                                  "@types/react" "~19.0.10"
                                                  "eslint" "^9.25.0"
                                                  "eslint-config-expo" "~9.2.0"
                                                  "typescript" "~5.8.3"
                                                  "@expo/metro-runtime" "^5.0.4"}}}]}
   :default [{:type   :module.graph
              :lang   :js
              :main   'component.web-index
              :target "src"
              
              :emit   {:code   {:label true}}}]})

(def +init+
  (make/triggers-set
   WEB-INDEX
   #{"pune"
     "component.web"
     "melbourne"
     "js.react"
     "xt.event"}))

(comment
  (make/build WEB-INDEX)
  (do (make/build-all WEB-INDEX)
      (make/gh:setup WEB-INDEX))
  
  (do (make/build-all WEB-INDEX)
      (make/gh:dwim-push WEB-INDEX))
  
  (make/gh:commit WEB-INDEX "hello")
  (make/gh:push WEB-INDEX)
  (make/gh:setup WEB-INDEX)
  (make/run WEB-INDEX :container-build)
  (future
    (make/run-internal WEB-INDEX :init))
  
  (future
    (make/run WEB-INDEX :dev)))
