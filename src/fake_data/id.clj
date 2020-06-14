(ns fake-data.id
  (:import [java.util UUID]))

(defn uuid4
  "Generate a random UUID, i.e. UUID4"
  []
  {:post [(instance? UUID %)]}
  (UUID/randomUUID))

(defn uuid4-lazy-seq
  "Generate a lazy sequence of UUID4s of length <length>"
  [length]
  {:pre [(integer? length)]
   :post [(seq? %)]}
  (for [x (range 0 length)] (uuid4)))
