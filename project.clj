(defproject base64-without-bitwise-operators "1.0.0.0-SNAPSHOT"
  :description "base64-without-bitwise-operators: base64 without bitwise operators in Clojure/SQL"
  :url "http://tomaszgigiel.pl"
  :license {:name "Apache License"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/tools.logging "0.4.1"]
                 ;; otherwise log4j.properties has no effect
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [commons-codec/commons-codec "1.11"]
                 [commons-io/commons-io "2.6"]]

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :resource-paths ["src/main/resources"]
  :target-path "target/%s"
  :jar-name "base64-without-bitwise-operators.jar"
  :uberjar-name "base64-without-bitwise-operators-uberjar.jar"
  :uberjar {:aot :all}
  :main ^:skip-aot pl.tomaszgigiel.base64-without-bitwise-operators.core
  :aot [pl.tomaszgigiel.base64-without-bitwise-operators.core]
  :profiles {:test {:resource-paths ["src/test/resources"]}
             :dev {:resource-paths ["src/test/resources"]}})
