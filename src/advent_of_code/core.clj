(ns advent-of-code.core
  (:require [advent-of-code.utils :as u]))

(defn -main
  "Used to dispatch tasks from the command line."
  [day part]
  (let [day   (Integer/parseInt day)
        part  (Integer/parseInt part)
        input (format "day%02d.txt" day)
        sub   (requiring-resolve
               (symbol (format "advent-of-code.day%02d/part-%d" day part)))]
    (cond
      (or (< day 1)
          (> day 25)) (throw (AssertionError. "Day out of range."))
      (or (< part 1)
          (> part 2)) (throw (AssertionError. "Part out of range."))
      (nil? sub)      (throw (Exception. "Matching fn not found."))
      :else           (let [[r t] (u/time-it (sub (u/read-input input)))]
                        (println r)
                        (println (format "\nTime: %.4fms" t))))))
