
(ns edn-db.check
    (:require [edn-db.config :as config]
              [edn-db.utils  :as utils]
              [io.api        :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn max-filesize-reached?
  ; @ignore
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ; (max-filesize-reached? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (-> collection-name utils/collection-name->filepath (io/max-filesize-reached? config/MAX-FILESIZE)))

(defn collection-exists?
  ; @ignore
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-exists? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (-> collection-name utils/collection-name->filepath io/file-exists?))

(defn collection-writable?
  ; @ignore
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-writable? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (-> collection-name max-filesize-reached? not))

(defn collection-readable?
  ; @ignore
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-readable? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (-> collection-name utils/collection-name->filepath io/file-exists?))
