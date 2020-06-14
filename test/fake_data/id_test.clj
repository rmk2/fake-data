(ns fake-data.id-test
  (:require [clojure.test :refer :all]
            [fake-data.id :refer :all]))

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
