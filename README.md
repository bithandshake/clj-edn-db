
# clj-edn-db

### Overview

The <strong>clj-edn-db</strong> is a lightweight Clojure collection/document database stored in EDN files.

### deps.edn

```
{:deps {bithandshake/clj-edn-db {:git/url "https://github.com/bithandshake/clj-edn-db"
                                 :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/clj-edn-db/tree/release).

### Documentation

The <strong>clj-edn-db</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>clj-edn-db</strong> library [here](CHANGES.md).

# Usage

> Some parameters of the following functions and some further functions are not discussed in this file.
  To learn more about the available functionality, check out the [functional documentation](documentation/COVER.md)!

> The `edn-db.api` manages both namespaced and non-namespaced documents and collections.

### Index

- [How to add a document?](#how-to-add-a-document)

- [How to remove a document?](#how-to-remove-a-document)

- [How to remove more than one document?](#how-to-remove-more-than-one-document)

- [How to apply a function on a document?](#how-to-apply-a-function-on-a-document)

- [How to get all documents from a collection?](#how-to-get-all-documents-from-a-collection)

- [How to get a document from a collection found by its ID?](#how-to-get-a-document-from-a-collection-found-by-its-id)

- [How to get more than one document from a collection found by their IDs?](#how-to-get-more-than-one-document-from-a-collection-found-by-their-ids)

- [How to get documents from a collection filtered by a function?](#how-to-get-documents-from-a-collection-filtered-by-a-function)

- [How to get the first document from a collection filtered by a function?](#how-to-get-the-first-document-from-a-collection-filtered-by-a-function)

- [How to get documents from a collection filtered by a query?](#how-to-get-documents-from-a-collection-filtered-by-a-query)

- [How to get the first document from a collection filtered by a query?](#how-to-get-the-first-document-from-a-collection-filtered-by-a-query)

- [How to get documents from a collection filtered by a key/value pair?](#how-to-get-documents-from-a-collection-filtered-by-a-key-value-pair)

- [How to get the first document from a collection filtered by a key/value pair?](#how-to-get-the-first-document-from-a-collection-filtered-by-a-key-value-pair)

- [How to get a specific value from a document found by its ID?](#how-to-get-a-specific-value-from-a-document-found-by-its-id)

- [How to check whether a document exists?](#how-to-check-whether-a-document-exists)

### How to add a document?

The [`edn-db.api/add-document!`](documentation/clj/edn-db/API.md/#add-document)
function adds a document to a collection.

- If the document doesn't have an ID, the function will generate one for it.
- The document will get the collection's namespace automatically.
- This function doesn't handle document ID conflicts, in case of you don't sure
  the document has an individual ID, use the `set-document!` function instead.
- The date/time objects will be unparsed to strings in the stored document.

```
(add-document! "my_collection" {:my-string "My value"})
```

```
(add-document! "my_collection" {:my-string "My value" :id "my-document"})
```

```
(add-document! "my_collection" {:namespace/my-string "My value"})
```

The [`edn-db.api/set-document!`](documentation/clj/edn-db/API.md/#set-document)
function adds a document to a collection.

- If the document doesn't have an ID, the function will generate one for it.
- The document will get the collection's namespace automatically.
- This function handles document ID conflicts, in case of the document ID is in
  use, the existing document will be replaced by the given document.
- The date/time objects will be unparsed to strings in the stored document.

```
(set-document! "my_collection" {:my-string "My value"})
```

```
(set-document! "my_collection" {:my-string "My value" :id "my-document"})
```

```
(set-document! "my_collection" {:namespace/my-string "My value"})
```

### How to remove a document?

The [`edn-db.api/remove-document!`](documentation/clj/edn-db/API.md/#remove-document)
function removes a document from a collection found by its ID.

```
(remove-document! "my_collection" "my-document")
```

### How to remove more than one document?

The [`edn-db.api/remove-documents!`](documentation/clj/edn-db/API.md/#remove-documents)
function removes multiple documents from a collection found by their IDs.

```
(remove-documents! "my_collection" ["my-document" "your-document"])
```

### How to apply a function on a document?

The [`edn-db.api/apply-on-document!`](documentation/clj/edn-db/API.md/#apply-on-document)
function applies the given function on a document found by its ID.

```
(defn my-function
  [document a b]
  (assoc document :a a :b b))

(apply-on-document! "my_collection" "my-document" my-function "Param #1" "Param #2")
```

```
(apply-on-document! "my_collection" "my-document" assoc :my-string "My value")
```

### How to get all documents from a collection?

The [`edn-db.api/get-collection`](documentation/clj/edn-db/API.md/#get-collection)
function returns the documents in a collection.

```
(get-collection "my_collection")
```

### How to get a document from a collection found by its ID?

The [`edn-db.api/get-document`](documentation/clj/edn-db/API.md/#get-document)
function returns a document from a collection found by its ID.

```
(get-document "my_collection" "my-document")
```

### How to get more than one document from a collection found by their IDs?

The [`edn-db.api/get-documents`](documentation/clj/edn-db/API.md/#get-documents)
function returns documents from a collection found by their IDs.

```
(get-documents "my_collection" ["my-document" "your-document"])
```

### How to get documents from a collection filtered by a function?

The [`edn-db.api/filter-documents`](documentation/clj/edn-db/API.md/#filter-documents)
function returns the documents in a collection filtered by the given function.

```
(filter-documents "my_collection" (fn [%] (string? :my-string)))
```

### How to get the first document from a collection filtered by a function?

The [`edn-db.api/filter-document`](documentation/clj/edn-db/API.md/#filter-document)
function returns the first document in a collection filtered by the given function.

```
(filter-document "my_collection" (fn [%] (string? :my-string)))
```

### How to get documents from a collection filtered by a query?

The [`edn-db.api/match-documents`](documentation/clj/edn-db/API.md/#match-documents)
function returns the documents in a collection filtered by the given pattern.

```
(match-documents "my_collection" {:my-string "My value"})
```

### How to get the first document from a collection filtered by a query?

The [`edn-db.api/match-document`](documentation/clj/edn-db/API.md/#match-document)
function returns the first document in a collection filtered by the given pattern.

```
(match-document "my_collection" {:my-string "My value"})
```

### How to get documents from a collection filtered by a key/value pair?

The [`edn-db.api/get-documents-kv`](documentation/clj/edn-db/API.md/#get-documents-kv)
function returns the documents in a collection filtered by the key/value pair.

```
(get-documents-kv "my_collection" :my-string "My value")
```

### How to get the first document from a collection filtered by a key/value pair?

The [`edn-db.api/get-document-kv`](documentation/clj/edn-db/API.md/#get-document-kv)
function returns the first document in a collection filtered by the given key/value pair.

```
(get-document-kv "my_collection" :my-string "My value")
```

### How to get a specific value from a document found by its ID?

The [`edn-db.api/get-document-item`](documentation/clj/edn-db/API.md/#get-document-item)
function returns a specific value in document found by its ID.

```
(get-document-item "my_collection" "my-document" :my-string)
```

### How to check whether a document exists?

The [`edn-db.api/document-exists?`](documentation/clj/edn-db/API.md/#document-exists)
function returns TRUE if a document found in the collection with the given ID.

```
(document-exists? "my_collection" "my-document")
```
