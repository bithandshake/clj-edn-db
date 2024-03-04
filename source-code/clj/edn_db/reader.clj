
(ns edn-db.reader
    (:require [edn-db.check  :as check]
              [edn-db.engine :as engine]
              [edn-db.utils  :as utils]
              [io.api        :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection
  ; @description
  ; Returns the documents of a specific collection file.
  ;
  ; @param (string) collection-name
  ;
  ; @example
  ; (get-collection "my-collection")
  ; =>
  ; [{:id "my-document" ...} {...} {...}]
  ;
  ; @return (maps in vector)
  [collection-name]
  (if (check/collection-exists? collection-name {:warn? false})
      (-> collection-name utils/collection-name->filepath io/read-edn-file)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-documents
  ; @description
  ; Returns the documents of a specific collection file, filtered with the given 'filter-f' function.
  ;
  ; @param (string) collection-name
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-documents "my-collection" :my-key)
  ; =>
  ; [{:id "my-document" :my-key "My value" ...} {:id "another-document" :my-key "Another value" ...}]
  ;
  ; @return (maps in vector)
  [collection-name filter-f]
  (if-let [collection (get-collection collection-name)]
          (engine/filter-documents collection filter-f)))

(defn filter-document
  ; @description
  ; Returns the first document of a specific collection file that matches the given 'filter-f' function.
  ;
  ; @param (string) collection-name
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-document "my-collection" :my-key)
  ; =>
  ; {:id "my-document" :my-key "My value" ...}
  ;
  ; @return (map)
  [collection-name filter-f]
  (if-let [collection (get-collection collection-name)]
          (engine/filter-document collection filter-f)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-documents
  ; @description
  ; Returns specific documents (identified by their document ID) from a specific collection file.
  ;
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ;
  ; @usage
  ; (get-documents "my-collection" ["my-document" "another-document"])
  ; =>
  ; [{:id "my-document" ...} {:id "another-document"}]
  ;
  ; @return (maps in vector)
  [collection-name document-ids]
  (if-let [collection (get-collection collection-name)]
          (engine/get-documents collection document-ids)))

(defn get-document
  ; @description
  ; Returns a specific document (identified by its document ID) from a specific collection file.
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @usage
  ; (get-document "my-collection" "my-document")
  ; =>
  ; {:id "my-collection"}
  ;
  ; @return (map)
  [collection-name document-id]
  (if-let [collection (get-collection collection-name)]
          (engine/get-document collection document-id)))

(defn document-exists?
  ; @description
  ; Returns TRUE if the document exists with the given document ID.
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @usage
  ; (document-exists? "my-collection" "my-document")
  ; =>
  ; true
  ;
  ; @return (boolean)
  [collection-name document-id]
  (if-let [collection (get-collection collection-name)]
          (engine/document-exists? collection document-id)))
