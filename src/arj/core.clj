(ns arj.core
  (:gen-class)
  (:use [amazonica.aws.sqs]
        [clojure.pprint])
  (:require [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-s" "--seed SEED" "Seed Value"
    :default "blerg"]
  ["-h" "--help"]])

(defn my-strip [s]
  (clojure.string/replace s "\"" ""))

(defn get-payload [q]
  (:body (get (:messages (receive-message q)) 0)))

(defn -main [& args]
  (let [x (:seed (:options (parse-opts args cli-options)))]
    (println "Input:" x)
    (create-queue x)
    (let [q (find-queue x)]
      (send-message q x)
      (let [m (get-payload q)]
        (println "Output:" (my-strip m)))))
;;    (pprint (:queue-urls(list-queues))))
)


