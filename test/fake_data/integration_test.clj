(ns fake-data.integration-test
  (:require [clojure.test :refer :all]
            [fake-data.generation :refer :all]
            [fake-data.id :refer :all]))

(deftest generate-weighted-sequence-test
  (let* [base 10
         seasonality [0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0 1.1 1.2 1.3 1.4 1.5]
         weights (map #(add-weight base %) seasonality)
         uuids (map uuid4-lazy-seq weights)]
    (testing "Weights are a sequence of integers"
      (is (instance? clojure.lang.LazySeq weights))
      (is (= (count seasonality) (count weights)))
      (is (every? true? (map integer? weights))))
    (testing "Weights meet expectations"
      (is (= '(1 2 3 4 5 6 7 8 9 10 11 12 13 14 15) weights)))
    (testing "Generated lazy sequence lengths correspond to weights"
      (is (= (count uuids) (count weights)))
      (is (every? true? (map #(= (count %1) %2) uuids weights))))))

