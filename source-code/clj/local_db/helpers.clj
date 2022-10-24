
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.helpers
    (:require [local-db.config   :as config]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-name-valid?
  ; @param (*) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (string/nonempty? collection-name))

(defn collection-name->filepath
  ; @param (string) collection-name
  ;
  ; @return (string)
  [collection-name]
  (str config/LOCAL-DB-PATH collection-name ".edn"))
