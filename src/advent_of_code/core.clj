(ns advent-of-code.core
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

;; Command-line options spec, in vector-of-vectors form.
(def ^:private options
  [["-b" "--bis" :id :bis]
   ["-f" "--file FILE" "Data file name"
    :id :file
    :default "day%02d.txt"]])

;; Create a single string from the vector of error messages when needed.
(defn- error-msg [errors]
  (str "The following errors occurred while parsing the command:\n\n"
       (str/join \newline errors)))

;; Validate the arguments. Call `parse-opts` and handle any errors. Check that
;; there are the right number of arguments.
(defn- validate [args]
  (let [{:keys [options arguments errors]} (parse-opts args options)]
    (cond
      errors                     (do
                                   (.println *err* (error-msg errors))
                                   (System/exit 1))
      (not= 2 (count arguments)) (do
                                   (.println *err* "Wrong number of arguments")
                                   (System/exit 1))
      :else                      {:options options, :arguments arguments})))

;; Main entry point. Take the command-line arguments and validate them, then
;; use the day/part arguments to derive the file to read for input and the
;; actual function to invoke.
(defn -main
  "Used to dispatch tasks from the command line.

  Usage: lein run [ -b ] [ -f FILE ] DAY PART

  where DAY and PART are integers selecting the day and part, respectively.

  If -f is given, specifies the data file to load in place of 'dayNN.txt'.

  If -b is given, load the 'bis' version of the day's code."
  [& args]
  (let [{:keys [options arguments]} (validate args)
        bis   (:bis options)
        day   (parse-long (first arguments))
        part  (parse-long (last arguments))
        input (format (:file options) day)
        sub   (try (requiring-resolve
                    (symbol (format "advent-of-code.day%02d%s/part-%d"
                                    day (if bis "bis" "") part)))
                   (catch Exception _
                     (format "No%s fn found for day %d part %d."
                             (if bis " bis" "") day part)))]
    (cond
      (or (< day 1)
          (> day 25)) (.println *err* "Day out of range.")
      (or (< part 1)
          (> part 2)) (.println *err* "Part out of range.")
      (string? sub)   (.println *err* sub)
      :else           (let [[r t] (u/time-it (sub (u/read-input input)))]
                        (println r)
                        (println (format "\nTime: %.3fms" t))))))
