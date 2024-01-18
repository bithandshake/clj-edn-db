
(ns edn-db.utils
    (:require [edn-db.config     :as config]
              [fruits.string.api :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-name-valid?
  ; @ignore
  ;
  ; @param (*) collection-name
  ;
  ; @usage
  ; (collection-name-valid? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (string/not-empty? collection-name))

(defn collection-name->filepath
  ; @ignore
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-name->filepath "my_collection")
  ;
  ; @return (string)
  [collection-name]
  (str config/EDN-DB-PATH collection-name ".edn"))
