(ns software.justenough.aws-demo.orchestrator
  (:require [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]
            [clojure.edn :as edn]
            [software.justenough.aws-demo.iam :as iam]
            [software.justenough.aws-demo.s3 :as s3]))

(def region (-> "config.edn" slurp edn/read-string :aws :region))
(def demo-creds (-> "credentials.edn" slurp edn/read-string :aws))

(def iam-creds (:iam demo-creds))
(def s3-creds (:s3 demo-creds))

(defn config
  "Given a region as a string, and an opts map with `:access-key-id` and
  `:secret-access-key` values defined, return a credentials-laden map for use
  with creating a client."
  [{:keys [region creds]}]
  {:region               region
   :credentials-provider (credentials/basic-credentials-provider creds)})

(defn client
  [config]
  (aws/client config))

(comment

  ;; First, make a client to work with. This will pull our user level details
  ;; from `~/.aws`
  (def iam-client (client {:api :iam}))
  (aws/validate-requests iam-client true)

  ;; Second, see what ops we can call (pull up the REBL for this)
  (aws/ops iam-client)

  ;; Third, show the docs for a specific operation. How does this differ from
  ;; what the REBL can show?
  (aws/doc iam-client :ListGroups)

  ;; Let's make our S3 user!
  (def s3-username "test-s3-admin")
  (def demo-s3-admin (iam/create-user! iam-client s3-username))

  ;; Buuuut...we don't have any access keys!
  (def s3-keys (iam/create-access-key! iam-client s3-username))
  ;; And now build our credentials
  (def s3-creds (credentials/basic-credentials-provider
                 {:access-key-id     (get-in s3-keys [:AccessKey :AccessKeyId])
                  :secret-access-key (get-in s3-keys [:AccessKey :SecretAccessKey])}))
  ;; Finally creating our S3 client with our new user
  (def s3-client (client {:api                 :s3
                          :region               region
                          :credentials-provider s3-creds}))

  ;; Let's make a bucket!
  (s3/list-buckets s3-client)

  ;; Oh yeah, we need the right perms!
  (iam/list-group-names iam-client)

  ;; I guess `s3-admins` sounds right
  (iam/join-group! iam-client s3-username "s3-admins")
  ;; Let's see if that worked
  (iam/users-groups iam-client s3-username)

  ;; Primo! Let's try listing those buckets again!
  (s3/list-buckets s3-client)
  )
