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

(defn get-seed [a]
  (:seed (:options (parse-opts a cli-options))))

(defn -main [& args]
  (let [x (get-seed args)]
    (println "Input:" x)
    (create-queue x)
    (let [q (find-queue x)]
      (send-message q x)
      (let [m (get-payload q)]
        (println "Output:" (my-strip m)))))
)


