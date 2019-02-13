(ns software.justenough.aws-demo.s3
  (:require [cognitect.aws.client.api :as aws]))

(defn list-buckets
  [client]
  (aws/invoke client {:op :ListBuckets}))

(defn create-bucket
  [client bucket-name]
  (aws/invoke client {:op      :CreateBucket
                      :request {:Bucket bucket-name}}))
