
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.reader
    (:require [io.api           :as io]
              [local-db.check   :as check]
              [local-db.engine  :as engine]
              [local-db.helpers :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection
  ; @param (string) collection-name
  ;
  ; @example
  ;  (get-collection "my_collection")
  ;  =>
  ;  [{...} {...} {...}]
  ;
  ; @return (vector)
  [collection-name]
  (if (check/collection-exists? collection-name)
      (-> collection-name helpers/collection-name->filepath io/read-edn-file)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-documents
  ; @param (string) collection-name
  ; @param (function) filter-f
  ;
  ; @usage
  ;  (filter-documents "my_collection" #(= :value (:key %1)))
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
  ;  (filter-document "my_collection" #(= :value (:key %1)))
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
  ;  (match-documents "my_collection" {:foo "bar"})
  ;  =>
  ;  [{:foo "bar" :baz "boo"}]
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
  ;  (match-document "my_collection" {:foo "bar"})
  ;  =>
  ;  {:foo "bar" :baz "boo"}
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
  ; @return (maps in vector)
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (engine/get-documents-kv collection item-key item-value)))

(defn get-document-kv
  ; @param (string) collection-name
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @return (map)
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (engine/get-document-kv collection item-key item-value)))

(defn get-documents
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ;
  ; @return (maps in vector)
  [collection-name document-ids]
  (let [collection (get-collection collection-name)]
       (engine/get-documents collection document-ids)))

(defn get-document
  ; @param (string) collection-name
  ; @param (string) document-id
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
  ; @return (*)
  ([collection-name document-id item-key]
   (let [collection (get-collection collection-name)]
        (engine/get-document-item collection document-id item-key))))

(defn document-exists?
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (boolean)
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (engine/document-exists? collection document-id)))
