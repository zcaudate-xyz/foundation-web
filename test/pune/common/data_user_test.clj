(ns pune.common.data-user-test
  (:use code.test)
  (:require [lang.core :as  l]
            [std.lib :as h]
            [xt.lang.common-notify :as notify]
            [statslink.test.base-scaffold :as scaffold]))

(l/script- :js
  {:runtime :basic
   :require [[pune.common.data-user :as data-user]
             [js.core :as j]
             [statslink.full.link-local :as link-local]
             [statslink.full.link-remote :as link-remote]
             [statslink.test.setup-node-debug :as setup-debug]
             [xt.lang.base-lib :as k]
             [xt.lang.common-repl :as repl]]
   :export [MODULE]})

(fact:global
 {:setup     [(do (scaffold/pg-restart)
                  (scaffold/pg-setup))
              (do (do (scaffold/redis-flush)
                      (scaffold/lua-reboot)
                      (scaffold/lua-start-services))
                  (do (l/rt:restart :js)
                      (scaffold/setup-js (l/rt :js))))
              (notify/wait-on :js
                (setup-debug/GD-init (@! (scaffold/lua-port))
                                     (repl/>notify)))]
  :teardown  [(scaffold/lua-stop)
              (scaffold/pg-stop)
              (l/rt:stop)]})

^{:refer pune.common.data-user/is-email-available :added "0.1"
  :setup [(j/<! ((. (data-user/is-email-available "hello" nil)
                    [1]
                    ["check"])
                 "a@a.com"))]}
(fact "checks that email is available"
  ^:hidden
  
  (j/<! ((. (data-user/is-email-available "hello" nil)
            [1]
            ["check"])
         "a@a.com"))
  => boolean?)

^{:refer pune.common.data-user/is-nickname-available :added "0.1"}
(fact "checks that nickname is available"
  ^:hidden
  
  (j/<! ((. (data-user/is-nickname-available "hello" nil)
            [1]
            ["check"])
         "a@a.com"))
  => boolean?)

^{:refer pune.common.data-user/account-new-validators :added "0.1"}
(fact "creates new account validators")
