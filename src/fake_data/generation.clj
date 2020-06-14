(ns fake-data.generation)

(defn add-weight
  "Modify a base number by a weight, i.e. <weight>% of <base>"
  [base weight]
  {:pre [(integer? base) (float? weight)]
   :post [(integer? %)]}
  (int (* base weight)))
