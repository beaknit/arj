(ns arj.core
  (:gen-class)
  (:use [amazonica.aws.sqs]
        [clojure.pprint])
  (:require [clojure.tools.cli :refer [parse-opts]]
            [amazonica.aws.s3 :as s3]
            [amazonica.aws.s3transfer :as s3transfer]))

(def cli-options
  [["-s" "--seed SEED" "Seed Value"
    :default "blerg"]
  ["-h" "--help"]])

(defn my-strip [s]
  (clojure.string/replace s "\"" ""))

(defn get-payload [q]
  (-> (receive-message q) :messages (get 0) :body))

(defn get-seed [a]
  (-> (parse-opts a cli-options) :options :seed))

(def prefix "568697")

(def upload-file   (java.io.File. "upload.txt"))
(def download-file (java.io.File. "download.txt"))

(defn -main [& args]
  (let [x (str prefix "-" (get-seed args))]
    (println "Prefixed Input:" x)
    (create-queue x)
    (let [q (find-queue x)]
      (send-message q x)
      (let [m (get-payload q)]
        (println "Output:" (my-strip m))
        (spit upload-file m)
        (s3/create-bucket x)
        (s3/put-object :bucket-name x
                    :key m
                    :metadata {:server-side-encryption "AES256"}
                    :file upload-file)
        )))
)
