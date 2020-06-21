(ns fake-data.behaviour-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [fake-data.specs :as specs]
            [fake-data.behaviour :refer :all]))

(deftest create-tree-test
  (testing "Creating a decision tree"
    (is (s/valid? ::specs/tree (tree :a :b [:c :d :e])))
    (is (s/valid? ::specs/tree (tree [:a (tree :b :b :b)] [:c (tree [:d 1 0] :e)])))
    (is (= (tree :a) (tree [:a true]) (tree [:a true false])))))

(deftest solve-node-test
  (testing "Solving a single node"
    (is (= (solve-node (tree :a) {:a 0.0}) [:a false]))
    (is (= (solve-node (tree :a) {:a 1.0}) [:a true]))))

(deftest solve-nodes-test
  (testing "Solving tree nodes"
    (is (= (solve-nodes (tree [:a (tree :b)] :c) {:a 0.0 :b 0.0 :c 0.0}) [:a false :c false]))
    (is (= (solve-nodes (tree [:a (tree :b)] :c) {:a 1.0 :b 0.0 :c 1.0}) [:a [:b false] :c true]))
    (is (= (solve-nodes (tree [:a (tree :b)] :c) {:a 1.0 :b 1.0 :c 1.0}) [:a [:b true] :c true]))))

(deftest deduplicate-keys-test
  (testing "Deduplicating keys"
    (is (= (deduplicate-keys [[:ax true] [:ax true] [:ax true]]) '(([:ax0 true] [:ax1 true] [:ax2 true]))))
    (is (= (deduplicate-keys [[:a true] [:a true] [:b true]]) '(([:a0 true] [:a1 true]) ([:b true]))))))

(deftest hash-solved-tree-test
  (testing "Transforming solved tree into hash"
    (is (= (hash-solved-tree {:a true :b '([:c true] [:c true])}) '({:a true} {:b {:c0 true :c1 true}})))))

(deftest solve-tree-test
  (testing "Solving a full behaviour tree"
    (is (= (solve-tree (tree [:a (tree :b)] :c) {:a 0.0 :b 0.0 :c 0.0}) {:a false :c false}))
    (is (= (solve-tree (tree [:a (tree :b)] :c) {:a 1.0 :b 0.0 :c 1.0}) {:a {:b false} :c true}))
    (is (= (solve-tree (tree [:a (tree :b)] :c) {:a 1.0 :b 1.0 :c 1.0}) {:a {:b true} :c true}))))

