
(ns local-db.helpers
    (:require [local-db.config   :as config]
              [mid-fruits.string :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-name-valid?
  ; @param (*) collection-name
  ;
  ; @usage
  ; (collection-name-valid? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (string/nonempty? collection-name))

(defn collection-name->filepath
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-name->filepath "my_collection")
  ;
  ; @return (string)
  [collection-name]
  (str config/LOCAL-DB-PATH collection-name ".edn"))
