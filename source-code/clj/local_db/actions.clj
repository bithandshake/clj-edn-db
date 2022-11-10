
(ns local-db.actions
    (:require [io.api           :as io]
              [local-db.check   :as check]
              [local-db.engine  :as engine]
              [local-db.helpers :as helpers]
              [local-db.reader  :as reader]
              [time.api         :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-collection!
  ; @param (string) collection-name
  ; @param (maps in vector) collection
  ;
  ; @usage
  ;  (set-collection! "my_collection" [{...} {...} {...}])
  ;
  ; @return (nil)
  [collection-name collection]
  (if (check/collection-writable? collection-name)
      (let [filepath (helpers/collection-name->filepath collection-name)]
           (io/write-edn-file! filepath collection {:abc? true}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-document!
  ; @param (string) collection-name
  ; @param (map) document
  ;
  ; @return (nil)
  [collection-name document]
  (let [collection (reader/get-collection collection-name)
        document   (time/unparse-date-time document)]
       (set-collection! collection-name (engine/add-document collection document))))

(defn remove-documents!
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ;
  ; @return (nil)
  [collection-name document-ids]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/remove-documents collection document-ids))))

(defn remove-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (nil)
  [collection-name document-id]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/remove-document collection document-id))))

(defn set-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) document
  ;
  ; @return (nil)
  [collection-name document-id document]
  (let [collection (reader/get-collection collection-name)
        document   (time/unparse-date-time document)]
       (set-collection! collection-name (-> collection (engine/remove-document document-id)
                                                       (engine/add-document    document)))))

(defn apply-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ;  (apply-document! "my_collection" "my-document" assoc :foo "bar")
  ;
  ; @usage
  ;  (apply-document! "my_collection" "my-document"
  ;                   (fn [document] (assoc document :foo "bar")))
  ;
  ; @return (nil)
  [collection-name document-id f & params]
  ; XXX#8075
  ; Az 1.0.3 verzióig az apply-document! függvény az engine/apply-document függvény
  ; alkalmazásával volt megvalósítva, amely nem tette lehetővé a params listában
  ; átadott anoním függvényekben lévő dátum objektumok string típussá alakítását.
  (let [collection       (reader/get-collection      collection-name)
        document         (engine/get-document collection document-id)
        params           (cons document params)
        updated-document (apply f params)
        updated-document (time/unparse-date-time updated-document)]
       (set-collection! collection-name (-> collection (engine/remove-document document-id)
                                                       (engine/add-document    updated-document)))))
