
# <strong>local-db.api</strong> namespace

<strong>[README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > </strong>source-code/clj/local_db/api.clj

### add-document!

```
@param (string) collection-name
@param (map) document
```

```
@usage
(add-document! "my_collection" {...})
```

```
@example
(add-document! "my_collection" {...})
=>
{...}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn add-document!
  [collection-name document]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/add-document collection document))
       (return document)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [add-document!]]))

(local-db.api/add-document! ...)
(add-document!              ...)
```

</details>

---

### apply-document!

```
@param (string) collection-name
@param (string) document-id
@param (function) f
@param (list of *)(opt) params
```

```
@usage
(apply-document! "my_collection" "my-document" assoc :foo "bar")
```

```
@usage
(apply-document! "my_collection" "my-document"
                 (fn [document] (assoc document :foo "bar")))
```

```
@example
(apply-document! "my_collection" "my-document" (fn [%] %))
=>
"my-document"
```

```
@return (string)
```

<details>
<summary>Source code</summary>

```
(defn apply-document!
  [collection-name document-id f & params]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/apply-document collection document-id f params))
       (return document-id)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [apply-document!]]))

(local-db.api/apply-document! ...)
(apply-document!              ...)
```

</details>

---

### document-exists?

```
@param (string) collection-name
@param (string) document-id
```

```
@usage
(document-exists? "my_collection" "my-document")
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn document-exists?
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (engine/document-exists? collection document-id)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [document-exists?]]))

(local-db.api/document-exists? ...)
(document-exists?              ...)
```

</details>

---

### filter-document

```
@param (string) collection-name
@param (function) filter-f
```

```
@usage
(filter-document "my_collection" #(= :value (:key %1)))
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn filter-document
  [collection-name filter-f]
  (let [collection (get-collection collection-name)]
       (engine/filter-document collection filter-f)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [filter-document]]))

(local-db.api/filter-document ...)
(filter-document              ...)
```

</details>

---

### filter-documents

```
@param (string) collection-name
@param (function) filter-f
```

```
@usage
(filter-documents "my_collection" #(= :value (:key %1)))
```

```
@return (maps in vector)
```

<details>
<summary>Source code</summary>

```
(defn filter-documents
  [collection-name filter-f]
  (let [collection (get-collection collection-name)]
       (engine/filter-documents collection filter-f)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [filter-documents]]))

(local-db.api/filter-documents ...)
(filter-documents              ...)
```

</details>

---

### get-collection

```
@param (string) collection-name
```

```
@example
(get-collection "my_collection")
=>
[{...} {...} {...}]
```

```
@return (vector)
```

<details>
<summary>Source code</summary>

```
(defn get-collection
  [collection-name]
  (if (check/collection-exists? collection-name)
      (-> collection-name helpers/collection-name->filepath io/read-edn-file)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [get-collection]]))

(local-db.api/get-collection ...)
(get-collection              ...)
```

</details>

---

### get-document

```
@param (string) collection-name
@param (string) document-id
```

```
@usage
(get-document "my_collection" "my-document")
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn get-document
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (engine/get-document collection document-id)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [get-document]]))

(local-db.api/get-document ...)
(get-document              ...)
```

</details>

---

### get-document-item

```
@param (string) collection-name
@param (string) document-id
@param (keyword) item-key
```

```
@usage
(get-document-item "my_collection" "my-document" :my-item)
```

```
@return (*)
```

<details>
<summary>Source code</summary>

```
(defn get-document-item
  [collection-name document-id item-key]
  (let [collection (get-collection collection-name)]
       (engine/get-document-item collection document-id item-key)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [get-document-item]]))

(local-db.api/get-document-item ...)
(get-document-item              ...)
```

</details>

---

### get-document-kv

```
@param (string) collection-name
@param (keyword) item-key
@param (*) item-value
```

```
@example
(get-document-kv "my_collection" :foo "bar")
=>
{:foo "bar"}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn get-document-kv
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (engine/get-document-kv collection item-key item-value)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [get-document-kv]]))

(local-db.api/get-document-kv ...)
(get-document-kv              ...)
```

</details>

---

### get-documents

```
@param (string) collection-name
@param (strings in vector) document-ids
```

```
@usage
(get-documents "my_collection" ["my-document" "your-document"])
```

```
@return (maps in vector)
```

<details>
<summary>Source code</summary>

```
(defn get-documents
  [collection-name document-ids]
  (let [collection (get-collection collection-name)]
       (engine/get-documents collection document-ids)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [get-documents]]))

(local-db.api/get-documents ...)
(get-documents              ...)
```

</details>

---

### get-documents-kv

```
@param (string) collection-name
@param (keyword) item-key
@param (*) item-value
```

```
@example
(get-documents-kv "my_collection" :foo "bar")
=>
[{:foo "bar"} {:foo "bar"}]
```

```
@return (maps in vector)
```

<details>
<summary>Source code</summary>

```
(defn get-documents-kv
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (engine/get-documents-kv collection item-key item-value)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [get-documents-kv]]))

(local-db.api/get-documents-kv ...)
(get-documents-kv              ...)
```

</details>

---

### match-document

```
@param (string) collection-name
@param (keyword) pattern
```

```
@example
(match-document "my_collection" {:foo "bar"})
=>
{:foo "bar" :baz "boo"}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn match-document
  [collection-name pattern]
  (let [collection (get-collection collection-name)]
       (engine/match-document collection pattern)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [match-document]]))

(local-db.api/match-document ...)
(match-document              ...)
```

</details>

---

### match-documents

```
@param (string) collection-name
@param (map) pattern
```

```
@example
(match-documents "my_collection" {:foo "bar"})
=>
[{:foo "bar" :baz "boo"}]
```

```
@return (maps in vector)
```

<details>
<summary>Source code</summary>

```
(defn match-documents
  [collection-name pattern]
  (let [collection (get-collection collection-name)]
       (engine/match-documents collection pattern)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [match-documents]]))

(local-db.api/match-documents ...)
(match-documents              ...)
```

</details>

---

### remove-document!

```
@param (string) collection-name
@param (string) document-id
```

```
@usage
(remove-document! "my_collection" "my-document")
```

```
@example
(remove-document! "my_collection" "my-document")
=>
"my-document"
```

```
@return (string)
```

<details>
<summary>Source code</summary>

```
(defn remove-document!
  [collection-name document-id]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/remove-document collection document-id))
       (return document-id)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [remove-document!]]))

(local-db.api/remove-document! ...)
(remove-document!              ...)
```

</details>

---

### remove-documents!

```
@param (string) collection-name
@param (strings in vector) document-ids
```

```
@usage
(remove-documents! "my_collection" ["my-document"])
```

```
@example
(remove-documents! "my_collection" ["my-document"])
=>
["my-document"]
```

```
@return (strings in vector)
```

<details>
<summary>Source code</summary>

```
(defn remove-documents!
  [collection-name document-ids]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (engine/remove-documents collection document-ids))
       (return document-ids)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [remove-documents!]]))

(local-db.api/remove-documents! ...)
(remove-documents!              ...)
```

</details>

---

### set-collection!

```
@param (string) collection-name
@param (maps in vector) collection
```

```
@usage
(set-collection! "my_collection" [{...} {...} {...}])
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn set-collection!
  [collection-name collection]
  (boolean (if (check/collection-writable? collection-name)
               (let [filepath (helpers/collection-name->filepath collection-name)]
                    (io/write-edn-file! filepath collection {:abc? true :create? true}))
               (println "local-db.actions:" collection-name "collection is not writable!"))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [set-collection!]]))

(local-db.api/set-collection! ...)
(set-collection!              ...)
```

</details>

---

### set-document!

```
@param (string) collection-name
@param (string) document-id
@param (map) document
```

```
@usage
(set-document! "my_collection" "my-document" {...})
```

```
@example
(set-document! "my_collection" "my-document" {...})
=>
"my-document"
```

```
@return (string)
```

<details>
<summary>Source code</summary>

```
(defn set-document!
  [collection-name document-id document]
  (let [collection (reader/get-collection collection-name)]
       (set-collection! collection-name (-> collection (engine/remove-document document-id)
                                                       (engine/add-document    document)))
       (return document-id)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [local-db.api :refer [set-document!]]))

(local-db.api/set-document! ...)
(set-document!              ...)
```

</details>
