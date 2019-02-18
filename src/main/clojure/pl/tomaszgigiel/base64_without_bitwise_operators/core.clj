(ns pl.tomaszgigiel.base64-without-bitwise-operators.core
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.base64-without-bitwise-operators.cmd :as cmd])
  (:gen-class))

(defn -main [& args]
  "base64-without-bitwise-operators: base64 without bitwise operators in Clojure/SQL"
  (let [{:keys [uri options exit-message ok?]} (cmd/validate-args args)]
    (if exit-message
      (cmd/exit (if ok? 0 1) exit-message)
      (println "ok")))
  (log/info "ok")
  (shutdown-agents))
