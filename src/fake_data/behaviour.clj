(ns fake-data.behaviour
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.spec.alpha :as s]
            [fake-data.specs :as specs]))

(defn tree
  "Create a tree with an arbitrary number of child branches per node"
  ([node]
   {:pre [(s/valid? (s/or :simple ::specs/name :full ::specs/child) node)]
    :post [(s/valid? ::specs/tree %)]}
   (if (coll? node)
     (let [[name left right] node]
       [name [(if (nil? left) true left) (if (nil? right) false right)]])
     (tree [node nil nil])))
  ([node & rest] (cons (tree node) (map tree rest))))

(defn solve-node
  "Solve a node, randomly picking either the left or right branch"
  [node chances]
  {:pre [(s/valid? ::specs/node node) (s/valid? ::specs/chances chances)]
   :post [(s/valid? ::specs/decision %)]}
  (let [[k [left right]] node
        choice (if (<= (rand) (get chances k 0.0)) true false)
        branch (if (true? choice) left right)]
    (cond
      ;; (boolean? branch) [k branch]
      (seq? branch) [k (map #(solve-node % chances) branch)]
      (vector? branch) [k (solve-node branch chances)]
      :else [k branch])))

(defn solve-nodes
  "Walk a behaviour tree, successively resolving choices weighted by <chances>"
  [tree chances]
  {:pre [(s/valid? ::specs/tree tree) (s/valid? ::specs/chances chances)]
   :post [(s/valid? ::specs/decisions %)]}
  (if (keyword? (first tree))
    (solve-node tree chances)
    (mapcat #(solve-node % chances) tree)))

(defn deduplicate-keys
  "Deduplicate keys in a solved tree"
  [input]
  {:pre [(s/valid? (s/coll-of ::specs/decision) input)]
   :post [(s/valid? (s/coll-of (s/coll-of ::specs/decision)) %)]}
  (let [freqs (frequencies (map first input))]
    (for [[key freq] freqs]
      (let [entries (map-indexed list (filter #(= (first %) key) input))]
        (for [[idx [k v]] entries]
          (if (> freq 1)
            [(keyword (str/replace-first (str k idx) ":" "")) v]
            [k v]))))))

(defn hash-solved-tree
  "Transform a solved tree into a list of hash-maps"
  [solved-tree-map]
  {:pre [(s/valid? ::specs/decisions-map solved-tree-map)]
   :post [(s/valid? (s/+ ::specs/decisions-map) %)]}
  (defn deep-hash-map [[k v]]
    (cond
      (vector? v) (hash-map k (deep-hash-map v))
      (seq? v) (->> v (deduplicate-keys) (apply concat) (map deep-hash-map) (apply merge) (hash-map k))
      :else (hash-map k v)))
  (map deep-hash-map solved-tree-map))

(defn solve-tree
  "Solve a behaviour tree by picking weighted choices for each node"
  [tree chances]
  {:pre [(s/valid? ::specs/tree tree) (s/valid? ::specs/chances chances)]
   :post [(s/valid? ::specs/decisions-map %)]}
  (->> (solve-nodes tree chances)
       (apply hash-map)
       (hash-solved-tree)
       (apply merge)))

