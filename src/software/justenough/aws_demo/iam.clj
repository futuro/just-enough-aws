(ns software.justenough.aws-demo.iam
  (:require [clojure.edn :as edn]
            [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]))

;; Exploration

(defn list-groups
  [client]
  (aws/invoke client {:op :ListGroups}))

(defn list-group-names
  [client]
  (->> (list-groups client)
       :Groups
       (map :GroupName)))

(defn list-users
  [client]
  (aws/invoke client {:op :ListUsers}))

(defn users-groups
  [client username]
  (aws/invoke client {:op      :ListGroupsForUser
                      :request {:UserName username}}))

(defn users-keys
  [client username]
  (aws/invoke client {:op      :ListAccessKeys
                      :request {:UserName username}}))

;; Creation

(defn create-access-key!
  [client username]
  (aws/invoke client {:op      :CreateAccessKey
                      :request {:UserName username}}))

(defn create-user!
  [client username]
  ;; XXX all AWS props are PascalCased, but the Clojure library specific
  ;; keywords are lowercased
  (aws/invoke client {:op      :CreateUser
                      :request {:UserName username}}))


(defn join-group!
  [client username groupname]
  (aws/invoke client {:op      :AddUserToGroup
                      :request {:UserName  username
                                :GroupName groupname}}))
