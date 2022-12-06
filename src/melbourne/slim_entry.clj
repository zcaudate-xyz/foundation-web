(ns melbourne.slim-entry
  (:require [std.lang :as  l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]
             [js.react :as r :include [:fn]]
             [js.react-native :as n :include [:fn [:icon :entypo]]]
             [js.react-native.ui-util :as ui-util]
             [js.react.ext-form :as ext-form]
             [xt.lang.event-route :as event-route]
             [melbourne.ui-text :as ui-text]
             [melbourne.ui-text-dialog :as ui-text-dialog]
             [melbourne.ui-static :as ui-static]
             [melbourne.ui-dropdown :as ui-dropdown]
             [melbourne.slim-common :as slim-common]
             [melbourne.slim-image :as slim-image]
             [melbourne.slim-number :as slim-number]
             [melbourne.slim-select :as slim-select]
             [melbourne.slim-submit :as slim-submit]
             [melbourne.slim-link :as slim-link]
             [melbourne.base-font :as base-font]
             [melbourne.base-palette :as base-palette]
             [xt.lang.base-lib :as k]]
   :export [MODULE]})

(defn.js EntryImplNotFound
  "creates a not found entry"
  {:added "4.0"}
  [#{design}]
  (return [:% ui-text/H4 #{design} "IMPL TYPE NOT FOUND"]))


;;
;; BASELINE
;;

(defn.js EntryFree
  "creates a free component"
  {:added "4.0"}
  [props]
  (var #{[impl
          (:.. rprops)]} props)
  (var #{[component
          (:.. rimpl)]} impl)
  (return
   (r/% component (j/assign rprops
                            rimpl
                            {:impl rimpl}))))

(defn.js EntryContentRaw
  "creates a raw content view"
  {:added "4.0"}
  [props]
  (var #{design
         entry
         impl} props)
  (return
   (r/% ui-static/Text
        (j/assign
         #{design
           {:style {:margin 10}}}
         impl)
        (n/format-entry entry))))

(defn.js EntryContentRawForm
  "creates a raw content view"
  {:added "4.0"}
  [props]
  (var #{design
         form} props)
  (var data (ext-form/listenFormData form))
  (return
   (r/% ui-static/Text
        #{design
          {:style {:margin 10}}}
        (n/format-entry data))))

(defn.js EntryContentFill
  "adds space filler for layouts"
  {:added "4.0"}
  [props]
  (return
   [:% n/Fill]))

(defn.js entryLayoutDiv
  "creates a layout component"
  {:added "4.0"}
  [props defaultComponent defaultStyle]
  (var #{[impl
          display
          (:.. rprops)]} props)
  (var #{[key
          component
          body
          debug
          style
          variant
          (:.. iprops)]} impl)
  (var #{[;;style
          (:.. cprops)]} (or (k/get-in props ["custom" key])
                             {}))
  (return
   (r/% (or component
            defaultComponent)
        (j/assign rprops
                  #{[:style [{:marginVertical 2}
                             defaultStyle
                             style]
                     :variant (or variant {:bg nil
                                           :fg nil})]}
                  iprops
                  cprops)
        body)))

(defn.js EntryLayoutHorizontal
  "creates a horizontal layout"
  {:added "4.0"}
  [props]
  (return (-/entryLayoutDiv props ui-static/Div {:flexDirection "row"})))

(defn.js EntryLayoutVertical
  "creates a vertical layout"
  {:added "4.0"}
  [props]
  (return (-/entryLayoutDiv props ui-static/Div {})))

(defn.js EntryLayoutEnclosed
  "creates an enclosed layout"
  {:added "4.0"}
  [#{[impl
      (:.. rprops)]}]
  (var #{[body
          (:.. iprops)]} impl)
  (return (r/% slim-common/FormEnclosed (j/assign rprops iprops)
               [:% n/Row
                {:style {:flexDirection "row-reverse"}}
                body])))

(defn.js EntryLayoutPortal
  "creates an portal layout"
  {:added "4.0"}
  [#{[impl
      (:.. rprops)]}]
  (var #{[body
          (:.. iprops)]} impl)
  (return (r/% n/Portal
               (j/assign rprops iprops)
               body)))

(defn.js EntryLayoutPortalSink
  "creates an portal layout"
  {:added "4.0"}
  [#{[impl
      (:.. rprops)]}]
  (var #{[body
          (:.. iprops)]} impl)
  (return (r/% n/PortalSink
               (j/assign rprops iprops)
               body)))

(defn.js EntryLayoutScroll
  "creates a scrollview"
  {:added "4.0"}
  [#{[impl
      (:.. rprops)]}]
  (var #{[body
          (:.. iprops)]} impl)
  (return (r/% ui-static/ScrollView
               (j/assign rprops iprops)
               body)))

(defn.js EntryLayoutPopup
  "creates a popup view"
  {:added "4.0"}
  [#{[(:= impl {})
      (:.. rprops)]}]
  (var #{[style
          (:.. iprops)]} impl)
  (return (r/% ui-text/ButtonTooltip
               (j/assign rprops
                         iprops
                         {:style [{:padding 5}
                                  (:.. (k/arrayify style))]}))))

(defn.js EntryLayoutDebug
  "creates a debug view"
  {:added "4.0"}
  [#{[(:= impl {})
      entry
      (:.. rprops)]}]
  (var #{[body
          (:.. iprops)]} impl)
  (return (r/% ui-text/TextAlt
               (j/assign rprops iprops
                         {:value entry})
               body)))

(defn.js EntryContentSeparator
  "creates a separator"
  {:added "4.0"}
  [props]
  (var #{[(:= impl {})
          (:.. rprops)]} props)
  (var #{[key
          header
          (:= variant {:fg {:key  "primary"
                            #_#_#_#_
                            :mix  "background"
                            :ratio 4}})
          (:.. iprops)]} impl)
  (return
   [:% n/View
    {:style {:marginHorizontal (:? header 0 5)}}
    (r/% ui-static/Separator
         (j/assign rprops
                   iprops
                   #{variant}
                   (k/get-in props ["custom" key])))]))

(defn.js entryContentText
  "creates either a title or context component"
  {:added "4.0"}
  [props defaultComponent]
  (var #{[impl
          entry
          design
          (:.. rprops)]} props)
  (var #{[key
          component
          variant
          style
          (:.. iprops)]} impl)
  (var #{[template
          (:= format k/identity)]} impl)
  (var data (or (k/template-entry entry template props)
                ""))
  (var children "")
  (try
    (:= children (format data props))
    (catch e))
  (when (and (not (r/isValidElement children))
             (not (k/is-string? children)))
    (:= children (k/js-encode children)))
  (var oprops (j/assign rprops
                        #{entry}
                        iprops
                        (k/get-in props ["custom" key])))
  (return
   (r/% (or component
            defaultComponent)
        #{design variant style}
        children)))

(defn.js EntryContentTitleH1
  "creates a h4 title"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/H1)))

(defn.js EntryContentTitleH2
  "creates a h4 title"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/H2)))

(defn.js EntryContentTitleH3
  "creates a h4 title"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/H3)))

(defn.js EntryContentTitleH4
  "creates a h4 title"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/H4)))

(defn.js EntryContentTitleH5
  "creates a h5 title"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/H5)))

(defn.js EntryContentTitle
  "creates a h6 title"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/H6)))

(defn.js EntryContentBold
  "creates a bold text"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/Bold)))

(defn.js EntryContentParagraph
  "creates a paragraph"
  {:added "4.0"}
  [props]
  (return (-/entryContentText props ui-text/P)))

(defn.js EntryContentIcon
  "creates a paragraph"
  {:added "4.0"}
  [props]
  (var #{impl
         display
         entry} props)
  (var #{[key
          template
          (:= format k/identity)
          (:.. iprops)]} impl)
  (var #{[style
          (:.. cprops)]} (or (k/get-in props ["custom" key])
                             {}))
  (var name (format (k/template-entry entry template props)))
  (return
   (r/% ui-text/Icon
        (j/assignNew
         props
         iprops
         #{name}
         cprops))))

(defn.js EntryContentImage
  "creates entry card avatar"
  {:added "4.0"}
  [props]
  (var #{impl
         entry} props)
  (var #{[key
          text
          style
          image
          color
          (:= format k/identity)
          (:.. iprops)]} impl)
  (var #{[
          (:.. cprops)]} (or (k/get-in props ["custom" key])
                             {}))
  (var textInput  (:? text
                      (k/template-entry entry (. text template) props)
                      ""))
  (var colorInput (:? (k/not-nil? color)
                      (k/template-entry entry (. color template) props)
                      ""))
  (var imageInput (:? image
                      (k/template-entry entry (. image template) props)))
  (return
   (r/% ui-text/Avatar
        (j/assignNew
         props
         {:color  (k/not-nil? color)
          :text   (:? textInput (format textInput))
          :image  imageInput
          :styleText  (:? text  (k/get-in props ["custom" (. text key)]))
          :styleImage (:? image (k/get-in props ["custom" (. image key)]))
          :style  [{:margin 5}
                   (:? (and (k/not-nil? color)
                            (k/nil? imageInput))
                       {:backgroundColor colorInput})
                   (:.. (k/arrayify style))
                   (k/arrayify cprops.style)]}
         iprops
         cprops))))

(defn.js EntryContentPair
  "creates entry content pair"
  {:added "4.0"}
  [props]
  (var #{design
         impl
         entry
         mini} props)
  (var #{[key
          style
          title
          text
          body]} impl)
  (return
   [:% n/Row
    {:style [{:maxWidth (:? (not mini) 280)}
             style]}
    [:% n/View
     {:style {:width (:? (not mini) 80)
              :marginRight 5}}
     (:? title
         (r/% -/EntryContentTitle
              (j/assignNew props
                           {:impl (j/assign
                                   {:variant {:fg {:key "neutral"}}}
                                   title)})))]
    (:? (k/not-empty? body)
        body
        
        text
        (r/% -/EntryContentParagraph
             (j/assignNew props {:impl text}))

        :else nil)]))

(def.js FIELD_COMPONENTS
  {:toggle      slim-common/FormToggleButton
   :switch      slim-common/FormToggleSwitch
   :input       slim-common/FormInput
   :input-xl    slim-common/FormInputXL
   :text        slim-common/FormTextArea
   :readonly    slim-common/FormReadOnly
   :enum-single slim-common/FormEnumSingle
   :enum-multi  slim-common/FormEnumMulti
   :color       slim-common/FormColorInput
   :chip        slim-common/FormChipInput
   
   :image       slim-image/FormImage
   :dropdown    slim-select/FormDropdown
   :picker      slim-select/FormPicker
   :slider      slim-number/FormSlider
   :spinner     slim-number/FormSpinner
   
   :link-readonly slim-link/FormLinkEntryReadOnly
   :link-dropdown slim-link/FormLinkDropdown})

(defn.js EntryContentField
  "creates an entry field"
  {:added "4.0"}
  [props]
  (var #{[impl
          display
          (:.. rprops)]} props)
  (var #{[component
          key
          field
          fieldProps
          (:.. iprops)]} impl)
  (var FieldComponent (:? (k/is-string? component)
                          (k/get-key -/FIELD_COMPONENTS
                                     component)
                          (or component
                              slim-common/FormInput)))
  (when (k/fn? fieldProps)
    (:= fieldProps (fieldProps props)))
  (var aprops (j/assign rprops
                        #{field fieldProps}
                        {:className (+ "field-" field)}
                        iprops
                        (k/get-in props ["custom" key])))  
  (return
   (r/% FieldComponent aprops)))

(defn.js EntryLayoutFormFade
  "creates a debug view"
  {:added "4.0"}
  [props]
  (var #{[impl
          form]} props)
  (var #{[body
          watch
          template
          (:.. iprops)]} impl)
  (var #{data} (ext-form/listenFieldsData form watch))
  (return (r/% ui-util/Fade
               (j/assign iprops
                         {:visible (k/template-entry data template props)})
               body)))

(defn.js EntryLayoutFormFold
  "creates a debug view"
  {:added "4.0"}
  [props]
  (var #{[impl
          form]} props)
  (var #{[body
          watch
          template
          (:.. iprops)]} impl)
  (var #{data} (ext-form/listenFieldsData form watch))
  (return (r/% ui-util/Fold
               (j/assign iprops
                         {:visible (k/template-entry data template props)})
               [:% n/View body])))

(defn.js entrySubmitType
  "picks submit type based on submit key"
  {:added "4.0"}
  [submit submitType]
  (return (or (:? (k/fn? submit)
                  "custom")
              
              submitType
              (:? (== submit "create")
                  "form"
                  
                  (== submit "detail")
                  "id"
                  
                  (== submit "modify")
                  "modify"
                  
                  :else "entry"))))

(defn.js entryOnSubmit
  "creates onSubmit function based on submitType"
  {:added "4.0"}
  [submitFn submitType entry form props args]
  (:= args (or args []))
  (return
   (:? (== submitType "custom")
       (fn:> (submitFn entry props (:.. args)))
       
       (== submitType "none")
       (fn:> (submitFn (:.. args)))
       
       (== submitType "id")
       (fn:> (submitFn (. entry id) (:.. args)))

       (== submitType "entry")
       (fn:> (submitFn entry (:.. args)))

       (== submitType "form")
       (fn:> (submitFn (. form data) (:.. args)))
       
       (== submitType "modify")
       (fn:> (submitFn (. entry id) (. form data) (:.. args)))

       (k/fn? submitType)
       (fn:> (submitType submitFn entry form))
       
       :else
       (fn:> (submitFn (and entry
                            (. entry id))
                       (:.. args))))))

(defn.js entryControlFn
  "creates the control function"
  {:added "4.0"}
  [control type props]
  (return (:? (== type "list")
              (. control setShowList)

              (== type "modify")
              (. control setShowModify)

              (== type "detail")
              (. control setShowDetail)

              (== type "create")
              (. control setShowCreate)

              (k/fn? type)
              (fn []
                (type control props))
              
              :else (. control [type]))))

(defn.js entryOnControl
  "creates the control onPress/onSuccess lambda"
  {:added "4.0"}
  [control controlType entry props]
  (when (not controlType)
    (return (fn:>)))
  
  #_(when (k/fn? controlType)
    (return controlType))
  
  (when (== controlType "back")
    (return
     (fn []
       (cond (. control backAction)
             (do (. control (backAction true))
                 (. control (setBackAction nil)))


             (. control showModify)
             (. control (setShowModify false))
             
             :else
             (do (. control (setShowCreate false))
                 (. control (setShowDetail nil)))))))
  (var onControl   (-/entryControlFn control controlType props))
  (return (-/entryOnSubmit onControl nil entry props)))

(defn.js EntryContentControl
  "creates an control button"
  {:added "4.0"}
  [props]
  (var #{[impl
          entry
          control
          (:.. rprops)]} props)
  (var #{[(:= component "minor")
          key
          submitType
          submit
          popup
          (:.. iprops)]} impl)
  

  
  (var onSubmit (-/entryOnControl control submit entry props))
  (var fprops (or (k/get-in props ["custom" key])
                  {}))
  
  (var ControlComponent (or (. {:minor  ui-text/ButtonMinor
                                :accent ui-text/ButtonAccent}
                               [component])
                            component))
  (var onPress onSubmit)
  (when popup
    (:= onPress (fn []
                  (onSubmit)
                  (. props (setVisible false)))))
  (return
   (r/% ControlComponent
        (j/assign rprops
                  {:style [{:width nil
                            :padding 5}
                           base-font/fontP] 
                   :onPress onPress}
                  iprops
                  fprops))))

(defn.js EntryLayoutControl
  [props]
  (var #{[impl
          entry
          control
          (:.. rprops)]} props)
  (var #{[submit
          body
          (:.. iprops)]} impl)
  (var onSubmit (-/entryOnControl control submit entry props))
  (return (r/% n/TouchableOpacity
               (j/assign iprops {:onPress onSubmit})
               body)))

(defn.js EntryContentLink
  "creates a link button"
  {:added "4.0"}
  [props]
  (var #{[impl
          form
          entry
          route
          (:.. rprops)]} props)
  (var #{[(:= component "minor")
          key
          template
          (:.. iprops)]} impl)
  (var fprops (or (k/get-in props ["custom" key])
                  {}))
  (var ControlComponent (or (. {:minor  ui-text/ButtonMinor
                                :accent ui-text/ButtonAccent}
                               [component])
                            component))
  (var url (k/template-entry entry template props))
  (var onPress
       (fn []
         (event-route/set-url route url true)))
  (return
   (r/% ControlComponent
        (j/assign rprops
                  {:style [{:width nil
                            :padding 5}
                           base-font/fontP] 
                   :onPress onPress}
                  iprops
                  fprops))))

(defn.js EntryLayoutLink
  [props]
  (var #{[impl
          form
          entry
          route
          (:.. rprops)]} props)
  (var #{[key
          template
          body
          (:.. iprops)]} impl)
  (var url     (k/template-entry entry template props))
  (var onPress 
       (fn []
         (event-route/set-url route url true)))
  (return (r/% n/TouchableOpacity
               (j/assign iprops {:onPress onPress})
               body)))

(defn.js EntryContentRoute
  "creates a content route"
  {:added "4.0"}
  [props]
  (var #{[impl
          mini
          form
          entry
          control
          (:.. rprops)]} props)
  
  (var #{[(:= component "minor")
          key
          submit
          (:.. iprops)]} impl)
  (var [route setRoute] [(. control [submit])
                         (. control [(+ "set" (k/capitalize submit))])])
  
  (var fprops (or (k/get-in props ["custom" key])
                  {}))
  
  (cond (or (not mini)
            (>= 3 (or (k/get-in route ["length"])
                      0)))
        (do 
          (var ControlComponent (or (. {:minor  ui-text/TabsMinor
                                        :accent ui-text/TabsAccent}
                                       [component])
                                    component))
          (return
           (r/% ControlComponent
                (j/assign rprops
                          {:style [{:width nil
                                    :padding 5}] 
                           :value route
                           :setValue setRoute}
                          iprops
                          fprops))))

        :else
        (return
         (r/% ui-dropdown/Dropdown
              (j/assign rprops
                        {:style [{:width 130
                                  :padding 7}]
                         :styleMenuItem {:width 150}
                         :value route
                         :setValue setRoute}
                        iprops
                        fprops)))))

(defn.js EntryContentRouteToggle
  "creates a content route"
  {:added "4.0"}
  [props]
  (var #{[impl
          mini
          form
          entry
          control
          (:.. rprops)]} props)
  
  (var #{[(:= component "minor")
          key
          submit
          valueOn
          valueOff
          (:.. iprops)]} impl)
  (var [route setRoute] [(. control [submit])
                         (. control [(+ "set" (k/capitalize submit))])])
  
  
  (var fprops (or (k/get-in props ["custom" key])
                  {}))
  (var ControlComponent (or (. {:minor  ui-text/ToggleMinor
                                        :accent ui-text/ToggleAccent}
                                       [component])
                            component))
  (return
   (r/% ControlComponent
        (j/assign rprops
                  {:style [{:width nil
                            :padding 5}] 
                   :selected (== route valueOn)
                   :onPress  (fn:> (setRoute
                                    (:? (== route valueOn)
                                        valueOff
                                        valueOn)))}
                  iprops
                  fprops))))

(defn.js EntryContentAction
  "creates an action"
  {:added "4.0"}
  [props]
  (var #{[impl
          form
          entry
          actions
          (:.. rprops)]} props)
  (var #{[key
          custom
          submitType
          submit
          args
          confirm
          control
          style
          popup
          onSuccess
          onError
          (:.. iprops)]} impl)
  (var submitFn  (:? (k/fn? submit)
                     submit
                     (. actions [submit])))
  (:= submitType (-/entrySubmitType submit submitType))
  (var onSubmit  (-/entryOnSubmit submitFn submitType entry form props args))
  (:=  onSuccess (or onSuccess
                     (-/entryOnControl (. props control)
                                       (k/get-in control ["success"])
                                       entry
                                       props)))
  
  (var sprops (or (k/get-in props ["custom" submit])
                  {}))
  (var submitProps (r/useSubmitResult
                    (j/assign #{onSubmit
                                onError
                                onSuccess}
                              sprops)))
  (var #{onActionPress} submitProps)
  (var onPress onActionPress)
  (when popup
    (:= onPress (fn []
                  (. props (setVisible false))
                  (onActionPress))))
  (var ActionComponent (:? (== confirm "tooltip")
                           ui-text/ConfirmTooltip
                           
                           (== confirm "dialog")
                           ui-text-dialog/ConfirmDialog
                           
                           :else
                           (:? (== (. impl component)
                                   "accent")
                               ui-text/ButtonAccent
                               ui-text/ButtonMinor)))
  (var output (j/assign rprops
                        {:style [{:width nil
                                  :padding 5}
                                 base-font/fontP
                                 style]
                         :actions actions
                         :onPress onPress}
                        iprops
                        submitProps
                        custom))
  (return
   (r/% ActionComponent output)))




(defn.js EntryContentSubmit
  "creates a layout form"
  {:added "4.0"}
  [props]
  (var #{[impl
          form
          (:= actions {})
          entry
          (:.. rprops)]} props)
  (var #{[key
          control
          explicit
          keep
          args
          field
          onSuccess
          onError
          submit
          submitType
          submitModify
          submitField
          (:.. iprops)]} impl)
  (var sprops (or (k/get-in props ["custom" submit])
                  {}))
  (var fprops (or (k/get-in props ["custom" key])
                  {}))
  (var submitFn  (. actions [submit]))
  (:= submitType (-/entrySubmitType submit submitType))
  (var onSubmit  (-/entryOnSubmit submitFn submitType entry form props args))
  (:=  onSuccess (or onSuccess
                     (-/entryOnControl (. props control)
                                       (k/get-in control ["success"])
                                       entry
                                       props)))
  (var useSubmit   (:? submitField
                       slim-submit/useSubmitField
                       slim-submit/useSubmitForm))
  
  (var submitProps (useSubmit
                    (j/assign #{field
                                form
                                onSubmit
                                onSuccess
                                onError
                                explicit
                                keep}
                              sprops)))

  ;;
  ;;
  ;;
  (var disabled false)
  (when submitModify
    (:= disabled (:? (k/arr? submitModify)
                     (k/eq-nested (k/obj-pick (. form data) submitModify)
                                  (k/obj-pick (or entry {}) submitModify))
                     (k/eq-nested (. form data)
                                  entry))))
  (return
   (r/% slim-submit/SubmitLineActions
        (j/assign rprops
                  #{form disabled}
                  iprops
                  fprops
                  submitProps))))

;;
;;
;;

(defn.js EntryLayoutCard
  "creates a card entry"
  {:added "4.0"}
  [props]
  (var #{[design
          impl
          (:.. rprops)]} props)
  (var #{[body
          style
          variant
          (:.. rimpl)]} impl)
  (var #{isHeader
         avatar
         right
         title
         main
         footer} body)
  (return
   [:% ui-static/Div
    #{[design
       variant
       :style [{:flex 1}
               (:.. (k/arrayify style))]
       #_(:.. rprops)]}
    [:% n/Row
     avatar
     (:? avatar
         [:% n/Padding {:style {:width 5}}])
     [:% n/View
      {:style [{:flex 1}
               (:? (not isHeader)
                   {:marginTop 10})]}
      (:? title
          [:<>
           title
           [:% n/Padding {:style {:height 5}}]])
      main
      (:? (and body footer)
          [:% n/Padding {:style {:height 5}}])
      footer]
     (:? right
         [:<>
          [:% n/Padding {:style {:width 5}}]
          right])]]))

(defn.js EntryLayoutForm
  "creates a layout form"
  {:added "4.0"}
  [props]
  (var #{[impl
          form
          (:= actions {})
          entry
          (:.. rprops)]} props)
  (var #{body
         style} impl)
  (return
   [:% n/View
    {:style [{:display "block"
              :margin 5}
             style]}
    body
    [:% n/Padding {:style {:height 10}}]]))

(def.js ENTRY_PROPS
  ["design"
   "variant"
   "mini"
   "entry"
   "display"
   "custom"
   "form"
   "hooks"
   "route"
   "actions"
   "views"
   "control"
   "data"
   "parent"
   "visible"
   "setVisible"])

(def.js ENTRY_COMPONENTS
  {:card     -/EntryLayoutCard
   :h        -/EntryLayoutHorizontal
   :v        -/EntryLayoutVertical
   :form     -/EntryLayoutForm
   :form-fade  -/EntryLayoutFormFade
   :form-fold  -/EntryLayoutFormFold
   :enclosed -/EntryLayoutEnclosed
   :portal   -/EntryLayoutPortal
   :portal-sink   -/EntryLayoutPortalSink
   :scroll   -/EntryLayoutScroll
   :popup    -/EntryLayoutPopup
   :debug    -/EntryLayoutDebug

   :control-layout   -/EntryLayoutControl
   :link-layout   -/EntryLayoutLink
   
   :free      -/EntryFree
   :raw       -/EntryContentRaw
   :raw-form  -/EntryContentRawForm
   :fill      -/EntryContentFill
   :pair      -/EntryContentPair
   :submit    -/EntryContentSubmit
   :action    -/EntryContentAction
   :control   -/EntryContentControl
   
   :route     -/EntryContentRoute
   :route-toggle  -/EntryContentRouteToggle
   :link      -/EntryContentLink
   :title     -/EntryContentTitle
   :title-h5  -/EntryContentTitleH5
   :title-h4  -/EntryContentTitleH4
   :title-h3  -/EntryContentTitleH3
   :title-h2  -/EntryContentTitleH2
   :title-h1  -/EntryContentTitleH1
   :separator -/EntryContentSeparator
   :bold  -/EntryContentBold
   :p     -/EntryContentParagraph
   :icon  -/EntryContentIcon
   :image -/EntryContentImage
   :field -/EntryContentField})

(def.js ENTRY_LAYOUT
  {:card           true
   :pair           true
   :form           true
   :form-fade      true
   :form-fold      true
   :enclosed       true
   :portal         true
   :portal-sink    true
   :h              true
   :v              true
   :scroll         true
   :popup          true
   :debug          true
   :control-layout true
   :link-layout    true})

(defn.js compileEntryPopup
  "compiles the Entry Popup"
  {:added "4.0"}
  [props compileFn]
  (var #{impl} props)
  (var eprops (k/obj-pick props -/ENTRY_PROPS))
  (var body (k/walk (. impl body)
                    k/identity
                    (fn [e]
                      (cond (and (k/obj? e)
                                 (or (== (. e type) "action")
                                     (== (. e type) "control")))
                            (return (j/assign {:popup true}
                                              e))
                            
                            :else (return e)))))
  (var mainComponent
       (r/const
        (fn [mprops]
          (var entryFn (fn [impl i]
                         (return (compileFn (j/assignNew mprops
                                                         {:key (or (. impl key)
                                                                   i)}
                                                         #{impl})))))
          (return [:% n/View
                   (j/map (or body []) entryFn)]))))
  (return
   (r/% -/EntryLayoutPopup (j/assign #{mainComponent} props))))

(defn.js compileEntry
  "compiles the entry"
  {:added "4.0"}
  [props EntryComponent]
  (var #{[entry
          display
          components
          key
          views]} props)
  (var eprops (k/obj-pick props -/ENTRY_PROPS))
  (var impl  (or (k/get-in props ["impl"])
                 {}))
  (when (r/isValidElement impl)
    (return impl))
  
  (while (k/fn? (. impl props))
    (:= impl (or ((. impl props) entry props)
                 {})))
  
  (var #{[(:= type "p")]} impl)
  (var component (or (k/get-key -/ENTRY_COMPONENTS type)
                     -/EntryImplNotFound))
  (var isLayout   (k/get-key -/ENTRY_LAYOUT type))
  (var isAlias     (== type "alias"))
  (var isPopup     (== type "popup"))
  (var showFn  (fn [impl i]
                 (cond (not impl)
                       (return false)
                       
                       (. impl show)
                       (return ((. impl show) entry props))
                       
                       (. impl hide)
                       (return (not ((. impl hide) entry props)))
                       
                       :else
                       (return true))))
  
  (var entryFn (fn:> [impl i]
                 (-/compileEntry (j/assignNew eprops
                                              {:key (or (. impl key)
                                                        i)}
                                              #{impl}))))
  (cond isAlias
        (do (var aliasComponent (or (. components [(. impl alias)])
                                    EntryComponent))
            (return
             (r/% aliasComponent (j/assign {:impl (. display [(. impl alias)])}
                                           eprops))))

        isPopup
        (return (-/compileEntryPopup props -/compileEntry))
        
        isLayout
        (do  (var body (:? (k/arr? (. impl body))
                           (-> (. impl body)
                               (j/filter showFn)
                               (j/map entryFn))
                           
                           (k/obj? (. impl body))
                           (-> (. impl body)
                               (k/obj-filter showFn)
                               (k/obj-map entryFn))
                           
                           :else []))
             (return (r/% component (j/assign {:key key
                                               :impl (j/assignNew impl #{body})}
                                           eprops))))
        
        :else
        (do 
          (var oprops (j/assign #{impl
                                  key
                                  {:views (. props views)}}
                                eprops))
          (return
           (r/% component oprops)))))

(defn.js Entry
  "creates the entry"
  {:added "4.0"}
  [props]
  (return (-/compileEntry props -/Entry)))

(def.js MODULE (!:module))

(comment
  {:layout    {:card  true
               :row   true
               :form  true
               :h     true
               :v     true}
   :content   {:free   true
               :title  true
               :p      true
               :pair   true
               :image  true
               :field  true
               :action true
               :separator true}})

