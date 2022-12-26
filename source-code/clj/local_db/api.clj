
(ns local-db.api
    (:require [local-db.actions :as actions]
              [local-db.reader  :as reader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; local-db.actions
(def set-collection!    actions/set-collection!)
(def add-document!      actions/add-document!)
(def remove-documents!  actions/remove-documents!)
(def remove-document!   actions/remove-document!)
(def set-document!      actions/set-document!)
(def apply-on-document! actions/apply-on-document!)

; local-db.reader
(def get-collection    reader/get-collection)
(def filter-documents  reader/filter-documents)
(def filter-document   reader/filter-document)
(def match-documents   reader/match-documents)
(def match-document    reader/match-document)
(def get-documents-kv  reader/get-documents-kv)
(def get-document-kv   reader/get-document-kv)
(def get-documents     reader/get-documents)
(def get-document      reader/get-document)
(def get-document-item reader/get-document-item)
(def document-exists?  reader/document-exists?)
