(defproject fake-data "0.1.0-SNAPSHOT"
  :description "Generate plausible enough fake data for demos/testing"
  :url "https://github.com/rmk2/fake-data"
  :license {:name "GPL-3.0-or-later"
            :url "https://www.gnu.org/licenses/gpl-3.0.txt"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot fake-data.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
  :plugins [[lein-cljfmt "0.6.7"]]
  :cljfmt {})
