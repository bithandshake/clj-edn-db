
(ns edn-db.check
    (:require [edn-db.config  :as config]
              [edn-db.helpers :as helpers]
              [io.api         :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn max-filesize-reached?
  ; @param (string) collection-name
  ;
  ; @usage
  ; (max-filesize-reached? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/max-filesize-reached? filepath config/MAX-FILESIZE)))

(defn collection-exists?
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-exists? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/file-exists? filepath)))

(defn collection-writable?
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-writable? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (let [max-filesize-reached? (max-filesize-reached? collection-name)]
       (not max-filesize-reached?)))

(defn collection-readable?
  ; @param (string) collection-name
  ;
  ; @usage
  ; (collection-readable? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/file-exists? filepath)))
