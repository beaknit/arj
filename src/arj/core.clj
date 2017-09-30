(ns arj.core
  (:gen-class)
  (:use [amazonica.aws.dynamodbv2])
  (:require [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-s" "--seed SEED" "Seed Value"
    :default "blerg"]
  ["-h" "--help"]])

(defn -main [& args]
  (println (:seed (:options (parse-opts args cli-options))))
)

(list-tables)
