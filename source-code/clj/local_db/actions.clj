
(ns local-db.actions
    (:require [candy.api        :refer [return]]
              [io.api           :as io]
              [local-db.check   :as check]
              [local-db.engine  :as engine]
              [local-db.helpers :as helpers]
              [local-db.reader  :as reader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-collection!
  ; @param (string) collection-name
  ; @param (maps in vector) collection
  ;
  ; @usage
  ; (set-collection! "my_collection" [{...} {...} {...}])
  ;
  ; @return (boolean)
  [collection-name collection]
  (boolean (if (check/collection-writable? collection-name)
               (let [filepath (helpers/collection-name->filepath collection-name)]
                    (io/write-edn-file! filepath collection {:abc? true :create? true}))
               (println "local-db.actions:" collection-name "collection is not writable!"))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-document!
  ; @param (string) collection-name
  ; @param (map) document
  ;
  ; @usage
  ; (add-document! "my_collection" {...})
  ;
  ; @example
  ; (add-document! "my_collection" {...})
  ; =>
  ; {...}
  ;
  ; @return (map)
  [collection-name document]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/add-document collection document))
       (return document)))

(defn remove-documents!
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ;
  ; @usage
  ; (remove-documents! "my_collection" ["my-document"])
  ;
  ; @example
  ; (remove-documents! "my_collection" ["my-document"])
  ; =>
  ; ["my-document"]
  ;
  ; @return (strings in vector)
  [collection-name document-ids]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/remove-documents collection document-ids))
       (return document-ids)))

(defn remove-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @usage
  ; (remove-document! "my_collection" "my-document")
  ;
  ; @example
  ; (remove-document! "my_collection" "my-document")
  ; =>
  ; "my-document"
  ;
  ; @return (string)
  [collection-name document-id]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/remove-document collection document-id))
       (return document-id)))

(defn set-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) document
  ;
  ; @usage
  ; (set-document! "my_collection" "my-document" {...})
  ;
  ; @example
  ; (set-document! "my_collection" "my-document" {...})
  ; =>
  ; "my-document"
  ;
  ; @return (string)
  [collection-name document-id document]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (-> collection (engine/remove-document document-id)
                                                       (engine/add-document    document)))
       (return document-id)))

(defn apply-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ; (apply-document! "my_collection" "my-document" assoc :foo "bar")
  ;
  ; @usage
  ; (apply-document! "my_collection" "my-document"
  ;                  (fn [document] (assoc document :foo "bar")))
  ;
  ; @example
  ; (apply-document! "my_collection" "my-document" (fn [%] %))
  ; =>
  ; "my-document"
  ;
  ; @return (string)
  [collection-name document-id f & params]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/apply-document collection document-id f params))
       (return document-id)))
