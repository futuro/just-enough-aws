(ns software.justenough.aws-demo.orchestrator
  (:require [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]
            [clojure.edn :as edn]
            [software.justenough.aws-demo.iam :as iam]
            [software.justenough.aws-demo.s3 :as s3]))

(comment

  ;; First, make an IAM client to work with. This will pull our user
  ;; credentials from `~/.aws`
  (def iam-client (aws/client {:api :iam}))
  (aws/validate-requests iam-client true)

  ;; Second, see what ops we can call
  (aws/ops iam-client)

  ;; Third, show the docs for a specific operation.
  (aws/doc iam-client :ListGroups)

  ;; Let's make our S3 user!
  (def s3-username "STEVE")
  (def demo-s3-admin (iam/create-user! iam-client s3-username))

  ;; Buuuut...we don't have any access keys, so let's make some!
  (def s3-keys (iam/create-access-key! iam-client s3-username))

  ;; And now build our credentials so we can authenticate as STEVE to S3
  (def s3-creds (credentials/basic-credentials-provider
                 {:access-key-id     (get-in s3-keys [:AccessKey :AccessKeyId])
                  :secret-access-key (get-in s3-keys [:AccessKey :SecretAccessKey])}))

  ;; Finally creating our S3 client with our new user
  (def s3-client (aws/client {:api                  :s3
                              :region               "us-east-1"
                              :credentials-provider s3-creds}))

  ;; What buckets do we have already?
  (s3/list-buckets s3-client)

  ;; Oh yeah, we need the right perms! Let's use the IAM client to see what
  ;; groups we can put STEVE into so he has the proper permissions.
  (iam/list-group-names iam-client)

  ;; I guess `s3-admins` sounds right
  (iam/join-group! iam-client s3-username "s3-admins")

  ;; Let's see if that worked
  (iam/users-groups iam-client s3-username)

  ;; Primo! Let's try listing those buckets again!
  (s3/list-buckets s3-client)

  ;; Let's make a bucket!
  (def bucket-name "TODO YOUR FAVORITE BUCKET NAME") ; We used "chum-bucket.justenough.software"
  (s3/create-bucket s3-client bucket-name)

  ;; Let's add something to it, something from the web!
  (aws/invoke s3-client {:op      :PutObject
                         :request {:Bucket bucket-name
                                   :Key    "crabby-patty.html"
                                   :Body   (byte-array (map int (slurp "https://wikipedia.org")))}})

  ;; Let's get it back!
  (def secret-recipe
    (aws/invoke s3-client {:op      :GetObject
                           :request {:Bucket bucket-name
                                     :Key    "crabby-patty.html"}}))

  ;; Let's take a look at what came back, and then put it into a file.
  secret-recipe
  (spit "crabby-patty.html" (slurp (:Body secret-recipe)))

  ;; And there you have it, the secret crabby patty recipe!
  )
