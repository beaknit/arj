(ns arj.core
  (:gen-class)
  (:use [amazonica.aws.sqs]
        [clojure.pprint])
  (:require [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-s" "--seed SEED" "Seed Value"
    :default "blerg"]
  ["-h" "--help"]])

(defn -main [& args]
  (let [x (:seed (:options (parse-opts args cli-options)))]
    (println x)
    (create-queue x)
    (pprint (:queue-urls(list-queues))))
)


