
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.check
    (:require [io.api           :as io]
              [local-db.config  :as config]
              [local-db.helpers :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn max-filesize-reached?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/max-filesize-reached? filepath config/MAX-FILESIZE)))

(defn collection-exists?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/file-exists? filepath)))

(defn collection-writable?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (and (-> collection-name collection-exists?)
       (-> collection-name max-filesize-reached? not)))

(defn collection-readable?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/file-exists? filepath)))
