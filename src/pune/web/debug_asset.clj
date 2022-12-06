(ns pune.web.debug-asset
  (:require [std.lang :as l]
            [std.lib :as h]))

(l/script :js
  {:require [[js.core :as j]]
   :export [MODULE]})

(comment
  (defn.js AssetRawLabel
    "constructs an asset raw label"
    {:added "0.1"}
    [#{design currencyId currency activeEscrow decimal onPress
       noCurrencyLabel notFound}]
    (var #{balance escrow} currency)
    (return
     [:% n/View
      {:style {:flex 1
               :flexDirection "row"}}
      [:% n/Text
       {:style [slim-common/StyleInfo
                #_{:color designNeutral}]}
       (:? noCurrencyLabel
           ""
           (+ (j/padEnd currencyId 6 " ")
              " "))
       (:? notFound
           "NOT FOUND"
           (+ "(" (j/toFixed
                   (or activeEscrow
                       escrow)
                   decimal) ")"))]
      [:% n/Padding {:style {:flex 1}}]
      [:% n/Text
       {:style [slim-common/StyleInfo
                #_{:color designNeutral}]}
       (:? notFound
           "-"
           (j/toFixed balance decimal))]]))
  
  (defn.js AssetControlLabel
    "constructs an asset control label"
    {:added "0.1"}
    [#{[currencyId
        active
        assets
        (:.. rprops)]}]
    (var activeEscrow (and active (k/get-key active currencyId)))
    (var currency  (k/get-key assets currencyId))
    (return [:% -/AssetRawLabel
             #{[:currency (or currency {})
                :currencyId currencyId
                :activeEscrow activeEscrow
                :decimal 2
                :notFound (k/nil? currency)
                (:.. rprops)]}]))

  (defn.js AssetControl
    "creates an asset control"
    {:added "0.1"}
    [#{design context currencies}]
    (cr/listenCell mc/T_ACTIVE_ORDER "success" {} context)
    (var assets  (cr/listenCell mc/C_ASSET_MAIN "success" {} context))
    (var active  (model-trade/get-active-asset-escrow context))
    (:= currencies (or currencies ["STC" "USD" "DOGE"]))
    (when (k/is-empty? assets)
      (return nil))
    (var currencyFn
         (fn [currencyId i]
           (return [:% -/AssetControlLabel
                    #{[:key currencyId
                       design
                       assets
                       active
                       currencyId
                       :onPress (fn []
                                  (cl/view-update mc/C_ASSET_MAIN context))]}])))
    (return
     [:% n/View
      (j/map currencies currencyFn)])))


(def.js MODULE (!:module))



