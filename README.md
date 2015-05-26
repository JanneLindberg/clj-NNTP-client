# clj-NNTP-client
Clojure wrapper that supports a subset of the Apache Commons Net NNTPClient library.
The main focus is on message retrieval, and thus posting is currently not supported at all.

## Usage

Some initial examples

```clojure
(use '[clj-NNTP-client.core :as nntp])
```



```clojure

;; Get a list all groups that contains the pattern

(let [nntp-client (nntp/connect {:hostname "news.gmane.org"} )
      groups (nntp/list-groups nntp-client "*clojure*")]

  (doseq [group groups]
    (println (select-keys group [:newsgroup :firstArticle :lastArticle :articleCount])))

  (nntp/disconnect nntp-client)
)
```

## License
Copyright (c) 2015 JanneLindberg

Distributed under the The MIT License (MIT)
