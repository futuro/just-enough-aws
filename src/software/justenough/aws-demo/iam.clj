(ns software.justenough.aws-demo.iam
  (:require [clojure.edn :as edn]
            [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]))

(def region (-> "config.edn" slurp edn/read-string :aws :region))
(def demo-creds (-> "credentials.edn" slurp edn/read-string))

(def iam-orchestrator (-> demo-creds :aws :iam))
;; (def s3-creds (:s3 demo-creds))
;; (def iam-creds (:iam demo-creds))

(defn config
  "Given a region as a string, and an opts map with `:access-key-id` and
  `:secret-access-key` values defined, return a credentials-laden map for use
  with creating a client."
  [{:keys [region creds]}]
  {:region               region
   :credentials-provider (credentials/basic-credentials-provider creds)})

(defn iam-client
  [config]
  (aws/client
   (merge {:api :s3} config)))

;; (def s3 (s3-client))
