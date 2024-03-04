
(ns edn-db.actions
    (:require [edn-db.check  :as check]
              [edn-db.engine :as engine]
              [edn-db.reader :as reader]
              [edn-db.utils  :as utils]
              [io.api        :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-collection!
  ; @description
  ; Stores the given documents in a specific collection file, ereasing its previous content.
  ;
  ; @param (string) collection-name
  ; @param (maps in vector) documents
  ;
  ; @usage
  ; (set-collection! "my-collection" [{:id "my-document" ...} {...} {...}])
  ; =>
  ; true
  ;
  ; @return (boolean)
  [collection-name documents]
  (boolean (if (check/collection-writable? collection-name {:warn? true})
               (let [filepath (utils/collection-name->filepath collection-name)]
                    (io/write-edn-file! filepath documents {:abc? true :create? true})))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-document!
  ; @description
  ; Inserts the given document to the end of a specific collection file.
  ;
  ; @param (string) collection-name
  ; @param (map) document
  ; {:id (string)(opt)
  ;  :namespace/id (string)(opt)
  ;  ...}
  ;
  ; @example
  ; (insert-document! "my-collection" {:id "my-document" ...})
  ; =>
  ; {:id "my-document" ...}
  ;
  ; @return (map)
  [collection-name document]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/insert-document collection document))
       (-> document)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-document!
  ; @description
  ; Removes a document (identified by its document ID) from a specific collection file.
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @example
  ; (remove-document! "my-collection" "my-document")
  ; =>
  ; "my-document"
  ;
  ; @return (string)
  [collection-name document-id]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/remove-document collection document-id))
       (-> document-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-on-document!
  ; @description
  ; Updates a document (identified by its document ID) in a specific collection file.
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ; (apply-on-document! "my-collection" "my-document" assoc :my-key "My value")
  ; =>
  ; "my-document"
  ;
  ; @return (string)
  [collection-name document-id f & params]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/apply-on-document collection document-id f params))
       (-> document-id)))
