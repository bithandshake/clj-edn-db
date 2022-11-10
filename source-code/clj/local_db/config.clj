
(ns local-db.config
    (:require [io.api :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (B)
(def MAX-FILESIZE (io/MB->B 10))

; @constant (string)
(def LOCAL-DB-PATH "environment/db/")
