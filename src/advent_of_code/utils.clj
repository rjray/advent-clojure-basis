(ns advent-of-code.utils
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import (java.security MessageDigest)
           (java.math BigInteger)))

(defn read-input
  "Read in the content of the given day-file and return as a blob"
  [day]
  (slurp (if (str/starts-with? day "/") day (io/resource day))))

(defn to-blocks
  "Turn a blob (probably from `slurp`) into a seq of blocks"
  [input]
  (str/split input #"\n\n"))

(defn to-lines
  "Turn a blob or block into a seq of lines"
  [input]
  (str/split-lines input))

(defn to-matrix
  "Turn a blob (or block) into a vector of vectors"
  [input]
  (->> input
       to-lines
       (mapv vec)))

(defn parse-out-longs
  "Parse out all numbers in `line` that are integers (longs)"
  [line]
  (map parse-long (re-seq #"[-+]?\d+" line)))

(defn parse-ranges
  "Parse each string of input as a range in the form 'M-N'"
  [ranges]
  (->> ranges
       (map #(str/replace % "-" " "))
       (map parse-out-longs)))

(defn manhattan-dist
  "Calculate the Manhattan Distance between two points."
  [p1 p2]
  (+ (abs (- (first p1) (first p2)))
     (abs (- (last  p1) (last  p2)))))

(defn first-duplicate
  "Find first element of collection that is a duplicate"
  [coll]
  (reduce (fn [acc elt]
            (if (get acc elt)
              (reduced elt)
              (assoc acc elt true)))
          {} coll))

;; Like the core time macro, but rather than printing the elapsed time it
;; returns a list of (result, time). Returned value is in milliseconds.
(defmacro time-it [expr]
  `(let [start# (. System (nanoTime))
         ret#   ~expr
         end#   (/ (double (- (. System (nanoTime)) start#)) 1000000.0)]
     (list ret# end#)))

(defn tee
  "Like 'tap' or 'tee', show the value of expr before returning it"
  [expr]
  (print expr "\n")
  expr)

;; Taken from https://stackoverflow.com/a/3266877/6421
;;
;; Get matches for a given regexp *and* their position within the string.
(defn re-pos
  "Return a list of pairs of (index, string) for all matches of `re` in `s`"
  [re s]
  (loop [m (re-matcher re s), res ()]
    (if (.find m)
      (recur m (cons (list (.start m) (.group m)) res))
      (reverse res))))

;; Lazy sequence of primes, taken from Project Euler code repo
(def primes
  (concat
   [2 3 5 7]
   (lazy-seq
    (let [primes-from
          (fn primes-from [n [f & r]]
            (if (some #(zero? (rem n %))
                      (take-while #(<= (* % %) n) primes))
              (recur (+ n f) r)
              (lazy-seq (cons n (primes-from (+ n f) r)))))
          wheel (cycle [2 4 2 4 6 2 6 4 2 4 6 6 2 6  4  2
                        6 4 6 8 4 2 4 2 4 8 6 4 6 2  4  6
                        2 6 6 4 2 4 6 2 6 4 2 4 2 10 2 10])]
      (primes-from 11 wheel)))))

(defn factorize
  "Determine all prime factors of n"
  [n]
  (loop [x n, [p & ps] primes, factors []]
    (cond
      (= 1 x)           factors
      (zero? (mod x p)) (recur (/ x p) primes (conj factors p))
      :else             (recur x ps factors))))

;; https://stackoverflow.com/questions/10347315/matrix-transposition-in-clojure
(defn transpose [m]
  (apply mapv vector m))

;; Taken from https://github.com/narimiran/AdventOfCode2023/blob/main/clojure/aoc.clj
(defn gcd
  ([] 1)
  ([x] x)
  ([a b] (if (zero? b) a
             (recur b (mod a b)))))

(defn lcm
  ([] 1)
  ([x] x)
  ([a b] (/ (* a b) (gcd a b))))

;; Taken from https://gist.github.com/jizhang/4325757?permalink_comment_id=2196746#gistcomment-2196746
(defn md5 [^String s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        raw (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(defn create-field
  "Create a NxM field as a matrix (vector of vectors). Fill with `with` or nil"
  [N M & [with]]
  (if (or (seq? with) (vector? with))
    ;; Ignore N/M and treat each element of `with` as a row in the field
    (mapv vec with)
    ;; Otherwise, use the value of `with` itself (which may be nil)
    (vec (repeat M (vec (repeat N with))))))

(defn display
  "Display a matrix of characters, as if on a terminal or similar"
  [lines]
  (println (str/join "\n" (map #(str/join %) lines))))
