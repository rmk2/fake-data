(ns fake-data.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]))

;; fake-data.behaviour

(s/def ::name (s/or :keyword keyword? :string string?))
(s/def ::branch (s/nilable (s/or :keyword keyword?
                                 :string string?
                                 :number number?
                                 :boolean boolean?
                                 :coll coll?)))
(s/def ::child (s/or :left (s/tuple ::name ::branch)
                     :full (s/tuple ::name ::branch ::branch)))

(s/def ::node (s/tuple ::name (s/tuple ::branch ::branch)))
(s/def ::tree (s/or :multi (s/every ::node) :single ::node))

(s/def ::chances (s/map-of ::name (s/double-in :min 0.0 :max 1.0)))
(s/def ::decision (s/cat :id ::name :choice ::branch))
(s/def ::decisions (s/+ ::decision))

(s/fdef fake-data.behaviour/tree
  :args (s/+ (s/or :simple ::name :full ::child))
  :ret ::tree)

(s/fdef fake-data.behaviour/solve-node
  :args (s/cat :node ::node :chances ::chances)
  :ret ::decision)

(s/fdef fake-data.behaviour/solve-nodes
  :args (s/cat :tree ::tree :chances ::chances)
  :ret ::decisions)

(s/fdef fake-data.behaviour/deduplicate-keys
  :args ::decisions
  :ret ::decisions)
