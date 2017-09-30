(ns arj.core
  (:gen-class)
  (:use [amazonica.aws.sqs]
        [clojure.pprint])
  (:require [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-s" "--seed SEED" "Seed Value"
    :default "blerg"]
  ["-h" "--help"]])

(defn my_strip [s]
  (clojure.string/replace s "\"" ""))

(defn -main [& args]
  (let [x (:seed (:options (parse-opts args cli-options)))]
    (println x)
    (create-queue x)
    (let [q (find-queue x)]
      (send-message q x)
      (let [m (:body (get (:messages (receive-message q)) 0))]
        (println (my_strip m)))))
;;    (pprint (:queue-urls(list-queues))))
)


