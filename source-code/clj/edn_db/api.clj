
(ns edn-db.api
    (:require [edn-db.actions :as actions]
              [edn-db.reader  :as reader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (edn-db.actions/*)
(def set-collection!    actions/set-collection!)
(def insert-document!   actions/insert-document!)
(def remove-document!   actions/remove-document!)
(def apply-on-document! actions/apply-on-document!)

; @redirect (edn-db.reader/*)
(def get-collection   reader/get-collection)
(def filter-documents reader/filter-documents)
(def filter-document  reader/filter-document)
(def get-documents    reader/get-documents)
(def get-document     reader/get-document)
(def document-exists? reader/document-exists?)
