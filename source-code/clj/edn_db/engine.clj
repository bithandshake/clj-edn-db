
(ns edn-db.engine
    (:require [fruits.keyword.api :as keyword]
              [fruits.map.api     :as map]
              [fruits.random.api  :as random]
              [fruits.vector.api  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection->namespace
  ; @ignore
  ;
  ; @description
  ; Returns the document namespace derived from the first document of the given collection.
  ;
  ; @param (maps in vector) collection
  ;
  ; @usage
  ; (collection->namespace [{:my-namespace/id "my-document" ...} {...} {...}])
  ; =>
  ; :my-namespace
  ;
  ; @return (keyword)
  [collection]
  (if-let [first-document (first collection)]
          (map/namespace first-document)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document->document-id
  ; @ignore
  ;
  ; @description
  ; Returns the ID of the given document.
  ;
  ; @param (map) document
  ;
  ; @example
  ; (document->document-id {:id "my-document" ...})
  ; =>
  ; "my-document"
  ;
  ; @example
  ; (document->document-id {...})
  ; =>
  ; "my-document"
  ;
  ; @example
  ; (document->document-id {:my-key "My value" ...})
  ; =>
  ; nil
  ;
  ; @example
  ; (document->document-id {...})
  ; =>
  ; nil
  ;
  ; @return (map)
  [document]
  (map/get-ns document :id))

(defn document<-document-id
  ; @ignore
  ;
  ; @description
  ; Ensures that the given document contains a document ID.
  ;
  ; @param (map) document
  ;
  ; @example
  ; (document<-document-id {:my-key "My value" :id "my-document" ...})
  ; =>
  ; {:my-key "My value"
  ;  :id     "my-document"
  ;  ...}
  ;
  ; @example
  ; (document<-document-id {:my-namespace/my-value "My value" :my-namespace/id "my-document" ...})
  ; =>
  ; {:my-namespace/my-value "My value"
  ;  :my-namespace/id       "my-document"
  ;  ...}
  ;
  ; @example
  ; (document<-document-id {:my-key "My value" ...})
  ; =>
  ; {:my-key "My value"
  ;  :id     "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
  ;  ...}
  ;
  ; @example
  ; (document<-document-id {:my-namespace/my-value "My value" ...})
  ; =>
  ; {:my-namespace/my-value "My value"
  ;  :my-namespace/id       "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
  ;  ...}
  ;
  ; @return (map)
  [document]
  (if-let [document-id (document->document-id document)]
          (-> document)
          (-> document (map/assoc-ns :id (random/generate-string)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-documents
  ; @ignore
  ;
  ; @description
  ; Returns the documents of the given collection, filtered with the given 'filter-f' function.
  ;
  ; @param (maps in vector) collection
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-documents [{:id "my-document" :my-key "My value" ...} {:id "another-document" :my-key "Another value" ...} {...} {...}] :my-key)
  ; =>
  ; [{:id "my-document" :my-key "My value" ...} {:id "another-document" :my-key "Another value" ...}]
  ;
  ; @return (maps in vector)
  [collection filter-f]
  (vector/all-matches collection filter-f))

(defn filter-document
  ; @ignore
  ;
  ; @description
  ; Returns the first document of the given collection that matches the the given 'filter-f' function.
  ;
  ; @param (maps in vector) collection
  ; @param (function) filter-f
  ;
  ; @usage
  ; (filter-document [{:id "my-document" :my-key "My value" ...} {:id "another-document" :my-key "Another value" ...} {...} {...}] :my-key)
  ; =>
  ; {:id "my-document" :my-key "My value" ...}
  ;
  ; @return (map)
  [collection filter-f]
  (vector/first-match collection filter-f))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-dex
  ; @ignore
  ;
  ; @description
  ; Returns the index of a specific document (identified by its document ID) in the given collection.
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @usage
  ; (get-document [{:id "my-document" ...} {...} {...}] "my-document")
  ; =>
  ; 0
  ;
  ; @return (integer)
  [collection document-id]
  (letfn [(f0 [%] (-> % document->document-id (= document-id)))]
         (vector/first-dex-by collection f0)))

(defn get-documents
  ; @ignore
  ;
  ; @description
  ; Returns specific documents (identified by their document ID) from the given collection.
  ;
  ; @param (maps in vector) collection
  ; @param (strings in vector) document-ids
  ;
  ; @usage
  ; (get-documents [{:id "my-document" ...} {:id "another-document" ...} {...} {...}] ["my-document" "another-document"])
  ; =>
  ; [{:id "my-document" ...} {:id "another-document" ...} {...} {...}]
  ;
  ; @return (maps in vector)
  [collection document-ids]
  (letfn [(f0 [%] (let [document-id (document->document-id %)] (vector/contains-item? document-ids document-id)))]
         (vector/all-matches collection f0)))

(defn get-document
  ; @ignore
  ;
  ; @description
  ; Returns a specific document (identified by its document ID) from the given collection.
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @usage
  ; (get-document [{:id "my-document" ...} {...} {...}] "my-document")
  ; =>
  ; {:id "my-document" ...}
  ;
  ; @return (map)
  [collection document-id]
  (letfn [(f0 [%] (-> % document->document-id (= document-id)))]
         (vector/first-match collection f0)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-exists?
  ; @ignore
  ;
  ; @description
  ; Returns TRUE if the document exists with the given document ID in the given collection.
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @usage
  ; (document-exists? [{:id "my-document" ...} {...}] "my-document")
  ; =>
  ; true
  ;
  ; @return (boolean)
  [collection document-id]
  (let [document (get-document collection document-id)]
       (boolean document)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-document
  ; @ignore
  ;
  ; @description
  ; Inserts the given document to the end of the given collection.
  ;
  ; @param (maps in vector) collection
  ; @param (keyword) document
  ;
  ; @example
  ; (insert-document [{:id "my-document"}] {:id "another-document"})
  ; =>
  ; [{:id "my-document"} {:id "another-document"}]
  ;
  ; @example
  ; (insert-document [{:id "my-document"}] {:my-namespace/id "another-document"})
  ; =>
  ; [{:id "my-document"} {:id "another-document"}]
  ;
  ; @example
  ; (insert-document [{:my-namespace/id "my-document"}] {:id "another-document"})
  ; =>
  ; [{:my-namespace/id "my-document"} {:my-namespace/id "another-document"}]
  ;
  ; @return (maps in vector)
  [collection document]
  (let [document (document<-document-id document)]
       (if (empty? collection)
           (vector/conj-item collection document)
           (if-let [namespace (collection->namespace collection)]
                   (let [document (map/add-namespace document namespace)]
                        (vector/conj-item collection document))
                   (let [document (map/remove-namespace document)]
                        (vector/conj-item collection document))))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-document
  ; @ignore
  ;
  ; @description
  ; Removes a document (identified by its document ID) from the given collection.
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @example
  ; (remove-document [{:id "my-document" ...} {:id "another-document" ...}] "my-document")
  ; =>
  ; [{:id "another-document" ...}]
  ;
  ; @return (maps in vector)
  [collection document-id]
  (println collection)
  (letfn [(f0 [%] (println % (-> % document->document-id)
                             (-> % document->document-id (= document-id)))
                  (-> % document->document-id (= document-id)))]

        (println (vector/remove-items-by collection f0))
        (vector/remove-items-by collection f0)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-on-document
  ; @ignore
  ;
  ; @description
  ; Updates a document (identified by its document ID) in the given collection.
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (function) f
  ; @param (vector) params
  ;
  ; @usage
  ; (apply-on-document [{:id "my-document" :label "My document"}]
  ;                    "my-document" assoc [:foo "bar"])
  ;
  ; @return (maps in vector)
  [collection document-id f params]
  (letfn [(f0 [%] (apply f % params))]
         (if-let [document-dex (get-document-dex collection document-id)]
                 (-> collection (vector/update-nth-item document-dex f0))
                 (-> collection))))
