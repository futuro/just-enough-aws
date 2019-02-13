(ns software.justenough.aws-demo.s3
  (:require [clojure.edn :as edn]
            [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]))

(def region (-> "config.edn" slurp edn/read-string :aws :region))
(def demo-creds (-> "credentials.edn" slurp edn/read-string :cljmn-aws-demo))

(def s3-creds (:s3 demo-creds))
(def iam-creds (:iam demo-creds))

(def config
  {:region               region
   :credentials-provider (credentials/basic-credentials-provider
                          {:access-key-id     (:access-id creds)
                           :secret-access-key (:secret-key creds)})})

(defn s3-client
  [config]
  (aws/client
   (merge {:api :s3}
          config)))

;; (def s3 (s3-client))
