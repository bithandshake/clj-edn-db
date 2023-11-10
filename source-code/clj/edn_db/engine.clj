
(ns edn-db.engine
    (:require [keyword.api :as keyword]
              [map.api     :as map]
              [random.api  :as random]
              [time.api    :as time]
              [vector.api  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection->namespace
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ;
  ; @usage
  ; (collection->namespace [{...} {...} {...}])
  ;
  ; @return (keyword)
  [collection]
  (if-let [first-document (first collection)]
          (map/namespace first-document)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document<-document-id
  ; @ignore
  ;
  ; @param (map) document
  ;
  ; @example
  ; (document<-document-id {:bar "baz" :id "my-document"})
  ; =>
  ; {:bar "baz" :id "my-document"}
  ;
  ; @example
  ; (document<-document-id {:foo/bar "baz" :foo/id "my-document"})
  ; =>
  ; {:foo/bar "baz" :foo/id "my-document"}
  ;
  ; @example
  ; (document<-document-id {:bar "baz"})
  ; =>
  ; {:bar "baz" :id "0ce14671-e916-43ab-b057-0939329d4c1b"}
  ;
  ; @example
  ; (document<-document-id {:foo/bar "baz"})
  ; =>
  ; {:foo/bar "baz" :foo/id "0ce14671-e916-43ab-b057-0939329d4c1b"}
  ;
  ; @return (map)
  [document]
  (if (map/namespaced? document)
      (let [namespace (map/namespace document)]
           (if (get   document (keyword/add-namespace :id namespace))
               (->    document)
               (assoc document (keyword/add-namespace :id namespace)
                               (random/generate-string))))
      (if (get   document :id)
          (->    document)
          (assoc document :id (random/generate-string)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn trim-collection
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (integer) max-count
  ; @param (integer)(opt) skip
  ; Default: 0
  ;
  ; @example
  ; (trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2)
  ; =>
  ; [{:id "1"} {:id "2"}]
  ;
  ; @example
  ; (trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2 1)
  ; =>
  ; [{:id "2"} {:id "3"} {:id "4"}]
  ;
  ; @example
  ; (trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2 4)
  ; =>
  ; [{:id "5"}]
  ;
  ; @return (maps in vector)
  ([collection max-count]
   (trim-collection collection max-count 0))

  ([collection max-count skip]
   (take max-count (drop skip collection))))

(defn filter-documents
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-documents [{...} {...} {...}] #(= :value (:key %1)))
  ;
  ; @return (maps in vector)
  [collection filter-f]
  (letfn [(f [result document]
             (if-not (filter-f document)
                     ; If document is NOT matches ...
                     (-> result)
                     ; If document is matches ...
                     (conj result document)))]
         (reduce f [] collection)))

(defn filter-document
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-document [{...} {...} {...}] #(= :value (:key %1)))
  ;
  ; @return (map)
  [collection filter-f]
  (vector/first-filtered collection filter-f))

(defn match-documents
  ; @ignore
  ;
  ; @description
  ; Returns documents found by the given pattern
  ;
  ; @param (maps in vector) collection
  ; @param (map) pattern
  ;
  ; @example
  ; (match-documents [{:foo "bar"} {...} {:foo "bar"}] {:foo "bar"})
  ; =>
  ; [{:foo "bar"} {:foo "bar"}]
  ;
  ; @return (maps in vector)
  [collection pattern]
  (letfn [(f [result document]
             (if-not (map/matches-pattern? document pattern)
                     ; If document is NOT matches ...
                     (-> result)
                     ; If document is matches ...
                     (conj result document)))]
         (reduce f [] collection)))

(defn match-document
  ; @ignore
  ;
  ; @description
  ; Returns the first document found by the given pattern
  ;
  ; @param (maps in vector) collection
  ; @param (map) pattern
  ;
  ; @example
  ; (match-document [{:foo "bar"} {...} {:foo "bar"}] {:foo "bar"})
  ; =>
  ; {:foo "bar"}
  ;
  ; @return (map)
  [collection pattern]
  (vector/first-filtered collection #(map/matches-pattern? % pattern)))

(defn get-documents-kv
  ; @ignore
  ;
  ; @description
  ; Returns documents found by the given value
  ;
  ; @param (maps in vector) collection
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @example
  ; (get-documents-kv [{:foo "bar"} {...} {:foo "bar"}] :foo "bar")
  ; =>
  ; [{:foo "bar"} {:foo "bar"}]
  ;
  ; @return (maps in vector)
  [collection item-key item-value]
  (letfn [(f [result document]
             (if-not (= item-value (get document item-key))
                     ; If document is NOT matches ...
                     (-> result)
                     ; If document is matches ...
                     (conj result document)))]
         (reduce f [] collection)))

(defn get-document-kv
  ; @ignore
  ;
  ; @description
  ; Returns the first document found by the given value
  ;
  ; @param (maps in vector) collection
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @example
  ; (get-document-kv [{...} {...} {:foo "bar"}] :foo "bar")
  ; =>
  ; {:foo "bar"}
  ;
  ; @return (map)
  [collection item-key item-value]
  (vector/first-filtered collection #(= item-value (get % item-key))))

(defn get-document
  ; @ignore
  ;
  ; @description
  ; Returns (first) document found by the given ID
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @usage
  ; (get-document [{...} {...} {...}] "my-document")
  ;
  ; @return (map)
  [collection document-id]
  (if-let [namespace (collection->namespace collection)]
          (get-document-kv collection (keyword/add-namespace :id namespace)
                           document-id)
          (get-document-kv collection :id document-id)))

(defn get-document-item
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (keyword) item-key
  ;
  ; @example
  ; (get-document-item [{:id "my-document" :label "My document"} {...} {...}]
  ;                    "my-document"
  ;                    :label)
  ; =>
  ; "My document"
  ;
  ; @return (*)
  [collection document-id item-key]
  (let [document (get-document collection document-id)]
       (get document item-key)))

(defn get-documents
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (strings in vector) document-ids
  ;
  ; @usage
  ; (get-documents [{...} {...}] ["my-document" ...])
  ;
  ; @return (maps in vector)
  [collection document-ids]
  (letfn [(f [result document]
             (if-not (vector/contains-item? document-ids (:id document))
                     ; If result is NOT matches ...
                     (-> result)
                     ; If document is matches ...
                     (conj result document)))]
         (reduce f [] collection)))

(defn add-document
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (keyword) document
  ;
  ; @example
  ; (add-document [{:foo "bar"}] {:baz "boo"})
  ; =>
  ; [{:foo "bar"} {:baz "boo"}]
  ;
  ; @example
  ; (add-document [{:foo "bar"}] {:bam/baz "boo"})
  ; =>
  ; [{:foo "bar"} {:baz "boo"}]
  ;
  ; @example
  ; (add-document [{:bam/foo "bar"}] {:baz "boo"})
  ; =>
  ; [{:bam/foo "bar"} {:bam/baz "boo"}]
  ;
  ; @return (maps in vector)
  [collection document]
  (let [document (document<-document-id   document)
        document (time/unparse-timestamps document)]
       (if (empty? collection)
           (vector/conj-item collection document)
           (if-let [namespace (collection->namespace collection)]
                   (let [document (map/add-namespace document namespace)]
                        (vector/conj-item collection document))
                   (let [document (map/remove-namespace document)]
                        (vector/conj-item collection document))))))

(defn remove-document
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @example
  ; (remove-document [{:id "1"} {:id "2"}] "2")
  ; =>
  ; [{:id "1"}]
  ;
  ; @return (maps in vector)
  [collection document-id]
  (letfn [(f [result document]
             (if (= document-id (map/get-ns document :id))
                 (->   result)
                 (conj result document)))]
         (reduce f [] collection)))

(defn remove-documents
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (strings in vector) document-ids
  ;
  ; @example
  ; (remove-documents [{:id "1"} {:id "2"} {:id "3"}] ["1" "3"])
  ; =>
  ; [{:id "2"}]
  ;
  ; @return (maps in vector)
  [collection document-ids]
  (letfn [(f [result document]
             (if (vector/contains-item? document-ids (map/get-ns document :id))
                 (->   result)
                 (conj result document)))]
         (reduce f [] collection)))

(defn apply-on-document
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ; (apply-on-document [{:id "my-document" :label "My document"}]
  ;                    "my-document" assoc :foo "bar")
  ;
  ; @usage
  ; (apply-on-document [{:id "my-document" :label "My document"}]
  ;                    "my-document" (fn [document] (assoc document :foo "bar")))
  ;
  ; @return (maps in vector)
  [collection document-id f & [params]]
  (let [document         (get-document collection document-id)
        params           (cons document params)
        updated-document (apply f params)
        updated-document (time/unparse-timestamps updated-document)]
       (-> collection (remove-document document-id)
                      (add-document    updated-document))))

(defn document-exists?
  ; @ignore
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @usage
  ; (document-exists? [{...} {...}] "my-document")
  ;
  ; @return (boolean)
  [collection document-id]
  (boolean (get-document collection document-id)))
