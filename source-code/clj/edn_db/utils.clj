
(ns edn-db.utils
    (:require [edn-db.config :as config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-name->filepath
  ; @ignore
  ;
  ; @description
  ; Converts the given collection name into collection filepath.
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-name->filepath "my-collection")
  ; =>
  ; "environment/edn-db/my-collection.edn"
  ;
  ; @return (string)
  [collection-name]
  (str config/EDN-DB-PATH collection-name ".edn"))
