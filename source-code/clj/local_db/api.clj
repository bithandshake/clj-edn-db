
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.api
    (:require [local-db.actions :as actions]
              [local-db.reader  :as reader]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; local-db.actions
(def set-collection!   side-effects/set-collection!)
(def add-document!     side-effects/add-document!)
(def remove-documents! side-effects/remove-documents!)
(def remove-document!  side-effects/remove-document!)
(def set-document!     side-effects/set-document!)
(def apply-document!   side-effects/apply-document!)

; local-db.reader
(def get-collection    side-effects/get-collection)
(def filter-documents  side-effects/filter-documents)
(def filter-document   side-effects/filter-document)
(def match-documents   side-effects/match-documents)
(def match-document    side-effects/match-document)
(def get-documents-kv  side-effects/get-documents-kv)
(def get-document-kv   side-effects/get-document-kv)
(def get-documents     side-effects/get-documents)
(def get-document      side-effects/get-document)
(def get-document-item side-effects/get-document-item)
(def document-exists?  side-effects/document-exists?)
