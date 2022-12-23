
# local-db-api

### Overview

The <strong>local-db-api</strong> is a lightweight Clojure document-collection
database stored in EDN files.

### deps.edn

```
{:deps {bithandshake/local-db-api {:git/url "https://github.com/bithandshake/local-db-api"
                                   :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/local-db-api/tree/release).

### Documentation

The <strong>local-db-api</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>local-db-api</strong> library [here](CHANGES.md).

### Index

- [How to add a document?](#how-to-add-a-document)

- [How to remove a document?](#how-to-remove-a-document)

- [How to remove more than one document?](#how-to-remove-more than one-document)

- [How to apply a function on a document?](#how-to-apply-a-function-on-a-document)

- [How to get all documents from a collection?](#how-to-get-all-documents-from-a-collection)

- [How to get a document from a collection found by its ID?](#how-to-get-a-document-from-a-collection-found-by-its-id)

- [How to get more than one document from a collection found by their ID-s?](#how-to-get-more-than-one-document-from-a-collection-found-by-their-id-s)

- [How to get documents from a collection filtered by a function?](#how-to-get-documents-from-a-collection-filtered-by-a-function)

- [How to get the first document from a collection filtered by a function?](#how-to-get-the-first-document-from-a-collection-filtered-by-a-function)

- [How to get documents from a collection filtered by a query?](#how-to-get-documents-from-a-collection-filtered-by-a-query)

- [How to get the first document from a collection filtered by a query?](#how-to-get-the-first-document-from-a-collection-filtered-by-a-query)

- [How to get documents from a collection filtered by a key/value pair?](#how-to-get-documents-from-a-collection-filtered-by-a-key-value-pair)

- [How to get the first document from a collection filtered by a key/value pair?](#how-to-get-the-first-document-from-a-collection-filtered-by-a-key-value-pair)

- [How to get a specific value from a document found by its ID?](#how-to-get-a-specific-value-from-a-document-found-by-its-id)

- [How to check whether a document exists?](#how-to-check-whether-a-document-exists)

# Usage

> The `local-db.api` manages both namespaced and non namespaced documents and collections.

### How to add a document?

The [`local-db.api/add-document!`](documentation/clj/local-db/API.md/#add-document)
function adds a document to a collection.

- If the document doesn't have an ID, the function will generate one for it.
- The document will get the collection's namespace automatically.
- This function doesn't handle document ID conflicts, in case of you don't sure
  the document has an individual ID, use the `set-document!` function instead.
- The date/time objects will be unparsed to strings in the stored document.

```
(defn add-my-document!
  []
  (add-document! "my_collection" {:my-string "My value"}))
```

```
(defn add-my-document!
  []
  (add-document! "my_collection" {:my-string "My value" :id "my-document"}))
```

```
(defn add-my-document!
  []
  (add-document! "my_collection" {:namespace/my-string "My value"}))
```

The [`local-db.api/set-document!`](documentation/clj/local-db/API.md/#set-document)
function adds a document to a collection.

- If the document doesn't have an ID, the function will generate one for it.
- The document will get the collection's namespace automatically.
- This function handles document ID conflicts, in case of the document ID is in
  use, the existing document will be replaced by the given document.
- The date/time objects will be unparsed to strings in the stored document.

```
(defn set-my-document!
  []
  (set-document! "my_collection" {:my-string "My value"}))
```

```
(defn set-my-document!
  []
  (set-document! "my_collection" {:my-string "My value" :id "my-document"}))
```

```
(defn set-my-document!
  []
  (set-document! "my_collection" {:namespace/my-string "My value"}))
```

### How to remove a document?

The [`local-db.api/remove-document!`](documentation/clj/local-db/API.md/#remove-document)
function removes a document from a collection found by its ID.

```
(defn remove-my-document!
  []
  (remove-document! "my_collection" "my-document"))
```

### How to remove more than one document?

The [`local-db.api/remove-documents!`](documentation/clj/local-db/API.md/#remove-documents)
function removes multiple documents from a collection found by their ID-s.

```
(defn remove-my-documents!
  []
  (remove-documents! "my_collection" ["my-document" "your-document"]))
```

### How to apply a function on a document?

The [`local-db.api/apply-on-document!`](documentation/clj/local-db/API.md/#apply-on-document)
function applies the given function on a document found by its ID.

```
(defn my-function
  [document a b]
  (assoc document :a a :b b))

(defn apply-on-my-document!
  []
  (apply-on-document! "my_collection" "my-document" my-function "Param #1" "Param #2"))
```

```
(defn apply-on-my-document!
  []
  (apply-on-document! "my_collection" "my-document" assoc :my-string "My value"))
```

### How to get all documents from a collection?

The [`local-db.api/get-collection`](documentation/clj/local-db/API.md/#get-collection)
function returns with the documents in a collection.

```
(defn get-my-collection
  []
  (get-collection "my_collection"))
```

### How to get a document from a collection found by its ID?

The [`local-db.api/get-document`](documentation/clj/local-db/API.md/#get-document)
function returns with a document from a collection found by its ID.

```
(defn get-my-document
  []
  (get-document "my_collection" "my-document"))
```

### How to get more than one document from a collection found by their ID-s?

The [`local-db.api/get-documents`](documentation/clj/local-db/API.md/#get-documents)
function returns with documents from a collection found by their ID-s.

```
(defn get-my-documents
  []
  (get-documents "my_collection" ["my-document" "your-document"]))
```

### How to get documents from a collection filtered by a function?

The [`local-db.api/filter-documents`](documentation/clj/local-db/API.md/#filter-documents)
function returns with the documents in a collection filtered by the given function.

```
(defn filter-my-documents
  []
  (filter-documents "my_collection" (fn [%] (string? :my-string))))
```

### How to get the first document from a collection filtered by a function?

The [`local-db.api/filter-document`](documentation/clj/local-db/API.md/#filter-document)
function returns with the first document in a collection filtered by the given function.

```
(defn filter-my-document
  []
  (filter-document "my_collection" (fn [%] (string? :my-string))))
```

### How to get documents from a collection filtered by a query?

The [`local-db.api/match-documents`](documentation/clj/local-db/API.md/#match-documents)
function returns with the documents in a collection filtered by the given pattern.

```
(defn match-my-documents
  []
  (match-documents "my_collection" {:my-string "My value"}))
```

### How to get the first document from a collection filtered by a query?

The [`local-db.api/match-document`](documentation/clj/local-db/API.md/#match-document)
function returns with the first document in a collection filtered by the given pattern.

```
(defn match-my-document
  []
  (match-document "my_collection" {:my-string "My value"}))
```

### How to get documents from a collection filtered by a key/value pair?

The [`local-db.api/get-documents-kv`](documentation/clj/local-db/API.md/#get-documents-kv)
function returns with the documents in a collection filtered by the key/value pair.

```
(defn get-my-documents-kv
  []
  (get-documents-kv "my_collection" :my-string "My value"))
```

### How to get the first document from a collection filtered by a key/value pair?

The [`local-db.api/get-document-kv`](documentation/clj/local-db/API.md/#get-document-kv)
function returns with the first document in a collection filtered by the given key/value pair.

```
(defn get-my-document-kv
  []
  (get-document-kv "my_collection" :my-string "My value"))
```

### How to get a specific value from a document found by its ID?

The [`local-db.api/get-document-item`](documentation/clj/local-db/API.md/#get-document-item)
function returns with a specific value in document found by its ID.

```
(defn get-my-document-item
  []
  (get-document-item "my_collection" "my-document" :my-string))
```

### How to check whether a document exists?

The [`local-db.api/document-exists?`](documentation/clj/local-db/API.md/#document-exists)
function returns TRUE if a document found in the collection with the given ID.

```
(defn my-document-exists?
  []
  (document-exists? "my_collection" "my-document"))
```
