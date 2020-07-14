(ns fake-data.distributions
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]))

(defn triangular
  "Triangular distribution"
  [a b c]
  {:pre [(s/valid? ::triangular [a b c])]
   :post [(and (number? %) (<= a % b))]}
  (let [F (/ (- c a) (- b a))
        U (Math/random)]
    (if (< 0 U F)
      (+ a (Math/sqrt (* U (- b a) (- c a))))
      (- b (Math/sqrt (* (- 1 U) (- b a) (- b c)))))))

(s/def ::triangular (s/and (s/cat :a number? :b number? :c number?)
                           #(<= (:a %) (:c %))
                           #(>= (:b %) (:c %))
                           #(< (:a %) (:b %))))

(s/fdef triangular
  :args ::triangular
  :ret number?
  :fn #(<= (-> % :args :a) (-> % :args :c) (-> % :args :b)))
