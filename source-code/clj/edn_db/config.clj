
(ns edn-db.config
    (:require [io.api :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @constant (B)
(def MAX-FILESIZE (io/MB->B 10))

; @ignore
;
; @constant (string)
(def EDN-DB-PATH "environment/edn-db/")
