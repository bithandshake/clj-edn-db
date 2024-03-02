
(ns edn-db.check
    (:require [edn-db.config :as config]
              [edn-db.utils  :as utils]
              [io.api        :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn max-filesize-exceeded?
  ; @ignore
  ;
  ; @param (string) collection-name
  ; @param (map)(opt) options
  ; {:warn? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; (max-filesize-exceeded? "my-collection")
  ; =>
  ; true
  ;
  ; @return (boolean)
  ([collection-name]
   (max-filesize-exceeded? collection-name options))

  ([collection-name {:keys [warn?]}]
   (if-not warn? (-> collection-name utils/collection-name->filepath (io/max-filesize-exceeded? config/MAX-FILESIZE))
                 (-> collection-name utils/collection-name->filepath (io/max-filesize-exceeded? config/MAX-FILESIZE)
                     (and (throw (Exception. (str "Collection '" collection-name "' max filesize exceeded!"))))))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-exists?
  ; @ignore
  ;
  ; @param (string) collection-name
  ; @param (map)(opt) options
  ; {:warn? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; (collection-exists? "my-collection")
  ; =>
  ; true
  ;
  ; @return (boolean)
  ([collection-name]
   (collection-exists? collection-name options))

  ([collection-name {:keys [warn?]}]
   (if-not warn? (-> collection-name utils/collection-name->filepath io/file-exists?)
                 (-> collection-name utils/collection-name->filepath io/file-exists?
                     (or (throw (Exception. (str "Collection '" collection-name "' does not exist!"))))))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-writable?
  ; @ignore
  ;
  ; @param (string) collection-name
  ; @param (map)(opt) options
  ; {:warn? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; (collection-writable? "my-collection")
  ; =>
  ; true
  ;
  ; @return (boolean)
  ([collection-name]
   (collection-writable? collection-name options))

  ([collection-name options]
   (-> collection-name (max-filesize-exceeded? options) not)))

(defn collection-readable?
  ; @ignore
  ;
  ; @param (string) collection-name
  ; @param (map)(opt) options
  ; {:warn? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; (collection-readable? "my-collection")
  ; =>
  ; true
  ;
  ; @return (boolean)
  ([collection-name]
   (collection-readable? collection-name options))

  ([collection-name options]
   (-> collection-name (collection-exists? options))))
