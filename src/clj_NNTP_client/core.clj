(ns clj-NNTP-client.core
  "Clojure wrapper that supports a subset of the Apache Commons Net NNTPClient library."
  (:import (org.apache.commons.net.nntp NNTPClient
                                        NewsgroupInfo
                                        ArticleInfo)))


(defn connect
  [config]
  (let [client ^NNTPClient (NNTPClient.)
        hostname (:hostname config)
        username (:user config)
        password (:pwd config)]

    (if-let [port (:port config)]
      (.connect client hostname port)
      (.connect client hostname))

    (if-not (or (empty? username) (empty? password))
      (.authenticate client username password))

    client
    ))


(defn disconnect
  "Logout and disconnect from the current nntp server session"
  [^NNTPClient client]
  (when (.isConnected client)
    (.logout client)
    (.disconnect client)
    ))


(defn is-connected?
  [^NNTPClient client]
  (.isConnected client))


(defn reply-code
  "Return the reply code from the server"
  [^NNTPClient client]
  (.getReplyCode client))


(defn reply-string
  "Return the reply string from the server"
  [^NNTPClient client]
  (.getReplyString client))


(defn list-groups
  "List groups"
  ([^NNTPClient client]
  (let [info (map  #(dissoc (bean %) :class) ^NewsgroupInfo (.listNewsgroups client))]
    info))
  ([^NNTPClient client ^String pattern]
  (let [info (map #(dissoc (bean %) :class) ^NewsgroupInfo (.listNewsgroups client pattern))]
    info))
  )


(defn select-newsgroup
  "Select a newsgroup and return NewsGroupInfo as a map"
  [^NNTPClient client group]
  (let [info (NewsgroupInfo.)
        _ (.selectNewsgroup client group info)]
    (dissoc (bean info) :class)))


(defn retrieve-article-message-ids
  "Retrieve the Message-ID header for the articles in the current group specified by the start and end range"
  [^NNTPClient client start end]
  (line-seq (.retrieveHeader client "Message-ID" start end)))


(defn get-message-header
  "Retrieve the message header"
  [^NNTPClient client msg-id]
  (slurp (.retrieveArticleHeader client msg-id)))


(defn get-message-body
  "Retrieve the message body"
  [^NNTPClient client msg-id]
  (slurp (.retrieveArticleBody client msg-id)))


(defn get-message
  "Retrieve the message header and body and return the content as a map"
  [^NNTPClient client msg-id]
  (let [header (get-message-header client msg-id)
        body (get-message-body client msg-id)]
    { :header header
      :body body
      }
    ))
