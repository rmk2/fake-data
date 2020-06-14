(ns fake-data.generation-test
  (:require [clojure.test :refer :all]
            [fake-data.generation :refer :all]))

(deftest add-weight-test
  (testing "Modifying a base integer with a weight modifier"
    (is (= (add-weight 10 1.5) 15))
    (is (= (add-weight 100 0.1) 10))
    (is (= (add-weight 100 0.01) 1))
    (is (= (add-weight 100 0.001) 0))))

