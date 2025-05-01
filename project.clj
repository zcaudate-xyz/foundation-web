(require 'cemerick.pomegranate.aether)
(cemerick.pomegranate.aether/register-wagon-factory!
 "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))
 
(defproject xyz.zcaudate/foundation-web "4.0.10"
  :description "web libraries for foundation"
  :url "https://www.github.com/zcaudate/foundation-web"
  :license  {:name "MIT License"
             :url  "http://opensource.org/licenses/MIT"}
  :aliases
  {"publish"     ["exec" "-ep" "(use 'code.doc)     (deploy-template :all) (publish :all)"]
   "incomplete"  ["exec" "-ep" "(use 'code.manage)  (incomplete :all) (System/exit 0)"]
   "install"     ["exec" "-ep" "(use 'code.maven)   (install :all {:tag :all}) (System/exit 0)"]
   "deploy"      ["exec" "-ep" "(use 'code.maven)   (deploy :all {:tag :all}) (System/exit 0)"]
   "deploy-lein" ["exec" "-ep" "(use 'code.maven)   (deploy-lein :all {:tag :all}) (System/exit 0)"]
   "push-web-code"  ["run" "-m" "component.task-web-index"]}
  
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [xyz.zcaudate/code.test           "4.0.10"]
                 [xyz.zcaudate/code.manage         "4.0.10"]
                 [xyz.zcaudate/code.java           "4.0.10"]
                 [xyz.zcaudate/code.maven          "4.0.10"]
                 [xyz.zcaudate/code.doc            "4.0.10"]
                 [xyz.zcaudate/code.dev            "4.0.10"]
                 
                 [xyz.zcaudate/js.core             "4.0.10"]
                 [xyz.zcaudate/js.cell             "4.0.10"]
                 [xyz.zcaudate/js.lib.datetime     "4.0.10"]
                 [xyz.zcaudate/js.lib.ethereum     "4.0.10"]
                 [xyz.zcaudate/js.lib.rn           "4.0.10"]
                 [xyz.zcaudate/js.lib.lw-charts    "4.0.10"]
                 [xyz.zcaudate/js.lib.valtio       "4.0.10"]
                 [xyz.zcaudate/js.react            "4.0.10"]
                 [xyz.zcaudate/js.react-ext        "4.0.10"]
                 [xyz.zcaudate/js.react-native     "4.0.10"]
                 
                 [xyz.zcaudate/jvm                 "4.0.10"]
                 [xyz.zcaudate/net.http            "4.0.10"]

                 [xyz.zcaudate/rt.basic            "4.0.10"]
                 
                 [xyz.zcaudate/script.css          "4.0.10"]
                 [xyz.zcaudate/script.sql          "4.0.10"]
                 [xyz.zcaudate/std.lib             "4.0.10"]
                 [xyz.zcaudate/std.log             "4.0.10"]
                 [xyz.zcaudate/std.lang            "4.0.10"]
                 [xyz.zcaudate/std.text            "4.0.10"]
                 [xyz.zcaudate/xtalk.lang          "4.0.10"]]
  :profiles {:dev {:plugins [[lein-ancient "0.6.15"]
                             [lein-exec "0.3.7"]
                             [lein-cljfmt "0.7.0"]
                             [cider/cider-nrepl "0.25.11"]]}
             :repl {:injections [(do 
                                   (require '[std.lib :as h])
                                   (require '[std.lib.link :as link])
                                   (create-ns '.))
                                 (require 'jvm.tool)]}}
  :resource-paths    ["resources" "src-build" "test-data"]
  :repl-options {:host "0.0.0.0" :port 10234}
  :jvm-opts
  ["-Xms2048m"
   "-Xmx2048m"
   "-XX:MaxMetaspaceSize=1048m"
   "-XX:-OmitStackTraceInFastThrow"
   
   ;;
   ;; GC FLAGS
   ;;
   "-XX:+UseAdaptiveSizePolicy"
   "-XX:+AggressiveHeap"
   "-XX:+ExplicitGCInvokesConcurrent"
   ;;"-XX:+UseCMSInitiatingOccupancyOnly"
   ;;"-XX:+CMSClassUnloadingEnabled"
   ;;"-XX:+CMSParallelRemarkEnabled"

   ;;
   ;; GC TUNING
   ;;   
   "-XX:MaxNewSize=256m"
   "-XX:NewSize=256m"
   ;;"-XX:CMSInitiatingOccupancyFraction=60"
   "-XX:MaxTenuringThreshold=8"
   "-XX:SurvivorRatio=4"

   ;;
   ;; Truffle
   ;;
   "-Dpolyglot.engine.WarnInterpreterOnly=false"
   
   ;;
   ;; JVM
   ;;
   "-Djdk.tls.client.protocols=\"TLSv1,TLSv1.1,TLSv1.2\""
   "-Djdk.attach.allowAttachSelf=true"
   "--enable-native-access=ALL-UNNAMED"
   "--add-opens" "java.base/java.io=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.annotation=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.invoke=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.module=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.ref=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.reflect=ALL-UNNAMED"
   "--add-opens" "java.base/java.math=ALL-UNNAMED"
   "--add-opens" "java.base/java.net=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.channels=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.charset=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.file=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.file.attribute=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.file.spi=ALL-UNNAMED"
   "--add-opens" "java.base/java.security=ALL-UNNAMED"
   "--add-opens" "java.base/java.security.cert=ALL-UNNAMED"
   "--add-opens" "java.base/java.security.interfaces=ALL-UNNAMED"
   "--add-opens" "java.base/java.security.spec=ALL-UNNAMED"
   "--add-opens" "java.base/java.text=ALL-UNNAMED"
   "--add-opens" "java.base/java.time=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.chrono=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.format=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.temporal=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.zone=ALL-UNNAMED"
   "--add-opens" "java.base/java.util=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.concurrent=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.concurrent.atomic=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.concurrent.locks=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.function=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.jar=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.regex=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.spi=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.stream=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.zip=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.loader=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.misc=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.module=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.org.xml.sax=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.perf=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.reflect=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.util=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.vm=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.vm.annotation=ALL-UNNAMED"

   "--add-opens" "java.net.http/java.net.http=ALL-UNNAMED"
   "--add-opens" "java.net.http/jdk.internal.net.http=ALL-UNNAMED"
   "--add-opens" "java.management/java.lang.management=ALL-UNNAMED"
   "--add-opens" "java.management/sun.management=ALL-UNNAMED"
   
   "--add-opens" "java.desktop/java.applet=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.color=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.dnd=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.event=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.font=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.geom=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.im=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.im.spi=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.image=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.image.renderable=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.awt.print=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.beans=ALL-UNNAMED"
   "--add-opens" "java.desktop/java.beans.beancontext=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.accessibility=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.imageio=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.imageio.event=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.imageio.metadata=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.imageio.plugins.bmp=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.imageio.plugins.jpeg=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.imageio.spi=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.imageio.stream=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.print=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.print.attribute=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.print.attribute.standard=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.print.event=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.sound.midi=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.sound.midi.spi=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.sound.sampled=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.sound.sampled.spi=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.border=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.colorchooser=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.event=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.filechooser=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.plaf=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.plaf.basic=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.plaf.metal=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.plaf.multi=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.plaf.nimbus=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.plaf.synth=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.table=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.text=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.text.html=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.text.html.parser=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.text.rtf=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.tree=ALL-UNNAMED"
   "--add-opens" "java.desktop/javax.swing.undo=ALL-UNNAMED"])
