(ns fake-data.seasonality
  (:require [clojure.spec.alpha :as s]
            [fake-data.generation :as gen]))

(def ^{:dynamic true} *default-offset* (java.time.ZoneOffset/ofHoursMinutes 0 0))
(def ^{:dynamic true} *default-date* (java.time.LocalDate/now))

(defn generate-day
  "Generate <base> timestamps per minute, for a whole day"
  [base & {:keys [date offset weights]
           :or {date *default-date* offset *default-offset* weights nil}}]
   {:pre [(s/valid? ::generate-day [base date offset weights])]
    :post [(seq? %) (every? #(instance? java.time.OffsetDateTime %) %)]}
   (->
    (for [hour (range 0 24)]
      (for [minute (range 0 60)]
        (let [weight (or (nth weights hour) 1.0)]
          (for [x (range 0 (gen/add-weight base weight))]
            (-> (java.time.OffsetTime/of hour minute (rand-int 60) 0 offset) (.atDate date))))))
    (flatten)))

(s/def ::generate-day (s/cat :base pos-int?
                             :date #(instance? java.time.LocalDate %)
                             :offset #(instance? java.time.ZoneOffset %)
                             :weights (s/nilable (s/and (s/coll-of float) #(= (count %) 24)))))

(s/fdef generate-day
  :args ::generate-day
  :ret (s/every #(instance? java.time.OffsetDateTime %)))

