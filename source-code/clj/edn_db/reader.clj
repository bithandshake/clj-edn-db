
(ns edn-db.reader
    (:require [edn-db.check  :as check]
              [edn-db.engine :as engine]
              [edn-db.utils  :as utils]
              [io.api        :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection
  ; @param (string) collection-name
  ;
  ; @example
  ; (get-collection "my_collection")
  ; =>
  ; [{...} {...} {...}]
  ;
  ; @return (vector)
  [collection-name]
  (if (check/collection-exists? collection-name)
      (-> collection-name utils/collection-name->filepath io/read-edn-file)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-documents
  ; @param (string) collection-name
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-documents "my_collection" #(= :value (:key %1)))
  ;
  ; @return (maps in vector)
  [collection-name filter-f]
  (let [collection (get-collection collection-name)]
       (engine/filter-documents collection filter-f)))

(defn filter-document
  ; @param (string) collection-name
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-document "my_collection" #(= :value (:key %1)))
  ;
  ; @return (map)
  [collection-name filter-f]
  (let [collection (get-collection collection-name)]
       (engine/filter-document collection filter-f)))

(defn match-documents
  ; @param (string) collection-name
  ; @param (map) pattern
  ;
  ; @example
  ; (match-documents "my_collection" {:foo "bar"})
  ; =>
  ; [{:foo "bar" :baz "boo"}]
  ;
  ; @return (maps in vector)
  [collection-name pattern]
  (let [collection (get-collection collection-name)]
       (engine/match-documents collection pattern)))

(defn match-document
  ; @param (string) collection-name
  ; @param (keyword) pattern
  ;
  ; @example
  ; (match-document "my_collection" {:foo "bar"})
  ; =>
  ; {:foo "bar" :baz "boo"}
  ;
  ; @return (map)
  [collection-name pattern]
  (let [collection (get-collection collection-name)]
       (engine/match-document collection pattern)))

(defn get-documents-kv
  ; @param (string) collection-name
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @example
  ; (get-documents-kv "my_collection" :foo "bar")
  ; =>
  ; [{:foo "bar"} {:foo "bar"}]
  ;
  ; @return (maps in vector)
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (engine/get-documents-kv collection item-key item-value)))

(defn get-document-kv
  ; @param (string) collection-name
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @example
  ; (get-document-kv "my_collection" :foo "bar")
  ; =>
  ; {:foo "bar"}
  ;
  ; @return (map)
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (engine/get-document-kv collection item-key item-value)))

(defn get-documents
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ;
  ; @usage
  ; (get-documents "my_collection" ["my-document" "another-document"])
  ;
  ; @return (maps in vector)
  [collection-name document-ids]
  (let [collection (get-collection collection-name)]
       (engine/get-documents collection document-ids)))

(defn get-document
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @usage
  ; (get-document "my_collection" "my-document")
  ;
  ; @return (map)
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (engine/get-document collection document-id)))

(defn get-document-item
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ; (get-document-item "my_collection" "my-document" :my-item)
  ;
  ; @return (*)
  [collection-name document-id item-key]
  (let [collection (get-collection collection-name)]
       (engine/get-document-item collection document-id item-key)))

(defn document-exists?
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @usage
  ; (document-exists? "my_collection" "my-document")
  ;
  ; @return (boolean)
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (engine/document-exists? collection document-id)))
