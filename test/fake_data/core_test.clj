(ns fake-data.core-test
  (:require [clojure.test :refer :all]
            [fake-data.core :refer :all]))

(deftest uuid4-test
  (let [uuid (uuid4)]
    (testing "Created UUID is a UUID4, in the correct format"
      (is (instance? java.util.UUID uuid))
      (is (= (.version uuid) 4))
      (is (= (.variant uuid) 2)))
    (testing "Created UUID4s are distinct"
      (is (not (= (uuid4) (uuid4) (uuid4)))))))

(deftest uuid4-lazy-seq-test
  (let* [length 11
         uuids (uuid4-lazy-seq length)]
    (testing "Type & length of generated lazy sequence of UUID4s"
      (is (instance? clojure.lang.LazySeq uuids))
      (is (= (count uuids) length)))
    (testing "Generated lazy sequence consists of UUID4s"
      (is (= (count uuids) (count (filter #(instance? java.util.UUID %) uuids))))
      (is (every? true? (map #(instance? java.util.UUID %) uuids)))
      (is (= '(1) (filter #(not (instance? java.util.UUID %)) (cons 1 uuids)))))))

(deftest add-weight-test
  (testing "Modifying a base integer with a weight modifier"
    (is (= (add-weight 10 1.5) 15))
    (is (= (add-weight 100 0.1) 10))
    (is (= (add-weight 100 0.01) 1))
    (is (= (add-weight 100 0.001) 0))))

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
