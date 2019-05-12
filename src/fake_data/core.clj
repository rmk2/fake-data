(ns fake-data.core
  (:gen-class)
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
      
(defn add-weight
  "Modify a base number by a weight, i.e. <weight>% of <base>"
  [base weight]
  {:pre [(integer? base) (float? weight)]
   :post [(integer? %)]}
  (int (* base weight)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

