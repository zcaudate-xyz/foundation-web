(ns component.build-web-index
  (:use code.test)
  (:require [std.lang :as l]
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
             [npx expo build:web]]
            [:dev
             [yarn install]
             [npx expo start --web]]
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
             :steps
             [{:name "Checkout repo"
               :uses "actions/checkout@v3"}
              {:name "Node Setup"
               :uses "actions/setup-node@v3"
               :with {:node-version "16.x"}}
              {:name "SSH Init"
               :run (str/|
                     "install -m 600 -D /dev/null ~/.ssh/id_rsa"
                     "echo '${{ secrets.GH_SSH_PRIVATE_KEY }}' > ~/.ssh/id_rsa"
                     "ssh-keyscan -H www.github.com > ~/.ssh/known_hosts")}
              
              {:name "Deploy gh-pages"
               :run
               (str/|
                "make build-web"
                "git config --global user.name github-actions"
                "git config --global user.email github-actions@github.com"
                "cd web-build && git init && git add -A && git commit -m 'deploying to gh-pages'"
                "git remote add origin git@github.com:zcaudate-xyz/demo.foundation-web.git"
                "git push origin HEAD:gh-pages --force")}]}}]]})
  
(def.make WEB-INDEX
  {:tag      "web-index"
   :build    ".build/web-index"
   :github   {:repo   "zcaudate-xyz/demo.foundation-web"
              :private true
              :description "Web Index"}
   :sections {:common [+readme+
                       +expo-makefile+
                       +github-workflows-build+]
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
                         "web-build/"
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
                                 "ios" {"supportsTablet" true},}}}
                       
                       {:type :package.json,
                        :main {"main" "node_modules/expo/AppEntry.js",
                               "name" "component-native",
                               "private" true,
                               "homepage" "/demo.foundation-web"
                               "dependencies" {"react" "17.0.2"
                                               "react-dom" "17.0.2"
                                               "react-native" "0.68.1"
                                               "react-native-web" "0.17.7"
                                               "react-native-error-boundary" "1.1.10"
                                               "react-native-get-random-values" "1.8.0"
                                               "react-native-base64" "0.1.0"
                                               "react-native-svg" "12.3.0"
                                               "react-native-vector-icons" "9.1.0"
                                               
                                               "expo-image-picker"  "13.1.1"
                                               "expo-media-library" "14.1.0"
                                               "expo-web-browser"   "10.2.1"
                                               "expo-auth-session"  "3.6.1"
                                               "expo-random"        "12.2.0"
                                               "react-color"        "2.19.3"
                                               "base-64"            "1.0.0"
                                               
                                               "dateformat"  "^4"
                                               "javascript-time-ago" "2.3.11"
                                               "mustache" "4.2.0"
                                               "fuse.js"  "6.4.6"
                                               "uuid"     "8.3.2"
                                               "lightweight-charts" "3.8.0"
                                               
                                               "@metamask/onboarding" "1.0.1"
                                               "@metamask/detect-provider" "1.2.0"
                                               "ethers" "5.7.1"}
                               "devDependencies" {"@babel/core" "7.9.0"
                                                  "@babel/preset-env" "7.13.15"
                                                  "@types/react" "16.9.35",
                                                  "@types/react-native" "0.63.2",
                                                  "expo" "45.0.3",
                                                  "expo-cli" "6.0.8",
                                                  "typescript" "4.3.5"}
                               
                               "scripts"
                               {"start" "expo start",
                                "android" "expo start --android",
                                "ios" "expo start --ios",
                                "web" "expo start --web",
                                "eject" "expo eject"},
                               
                               "metro" {"watchFolders" ["assets"]}}}]}
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
     "js.cell"}))

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
