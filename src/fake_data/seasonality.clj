(ns fake-data.seasonality
  (:require [clojure.spec.alpha :as s]))

(def ^{:dynamic true} *default-offset* (java.time.ZoneOffset/ofHoursMinutes 0 0))
(def ^{:dynamic true} *default-date* (java.time.LocalDate/now))

(defn generate-day
  "Generate <base> timestamps per minute, for a whole day"
  ([base] (generate-day base *default-date* *default-offset*))
  ([base date] (generate-day base date *default-offset*))
  ([base date offset]
   {:pre [(s/valid? ::generate-day [base date offset])]
    :post [(seq? %) (every? #(instance? java.time.OffsetDateTime %) %)]}
   (->
    (for [hour (range 0 24)]
      (for [minute (range 0 60)]
        (for [x (range 0 base)]
          (-> (java.time.OffsetTime/of hour minute (rand-int 60) 0 offset) (.atDate date)))))
    (flatten))))

(s/def ::generate-day (s/cat :base pos-int? :date #(instance? java.time.LocalDate %) :offset #(instance? java.time.ZoneOffset %)))

(s/fdef generate-day
  :args ::generate-day
  :ret (s/every #(instance? java.time.OffsetDateTime %)))
