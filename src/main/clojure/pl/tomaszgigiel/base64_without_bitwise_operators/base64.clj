(ns pl.tomaszgigiel.base64-without-bitwise-operators.base64
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:gen-class))

(defn exp [x n]
  "(= (exp 2 0) 1)"
  (loop [acc 1 n n]
    (if (zero? n) acc
      (recur (* x acc) (dec n)))))

(defn- to-bits [x]
  "(= (to-bits 6) '(1 1 0))"
  (loop [acc () x x]
    (cond
      (= x 0) (conj acc 0)
      (= x 1) (conj acc 1)
      :default (let [x2 (quot x 2)
                     even? (= x (* x2 2))
                     digit (if even? 0 1)
                     acc2 (conj acc digit)]
                 (recur acc2 x2)))))

(defn- to-bytes [x]
  "(= (to-bytes '(1 0 1)) '(0 0 0 0 0 1 0 1))"
  (let [size (count x)
        size-8 (* (quot (+ size 7) 8) 8)
        pre (repeat (- size-8 size) 0)]
    (into x pre)))

(defmulti to-binary (fn [x] [(class x)]))
(defmethod to-binary [String] [x] (flatten (map #(-> % int to-bits to-bytes) x)))
(defmethod to-binary [Character] [x] (->  x int to-bits to-bytes))
(defmethod to-binary [Number] [x] (-> x to-bits to-bytes))

(defn- from-binary-byte [x]
  "(= (from-binary-byte '(1 1)) 3)"
  (let [size (count x)
        pos (filter (fn [x] (pos? (:val x))) (map-indexed (fn [i val] {:i (- size i 1) :val val}) x))
        factors (map (fn [x] (exp 2 (:i x))) pos)]
    (reduce + factors)))

(defn from-binary [x] (->> x to-bytes (partition 8) (map from-binary-byte)))

(defn- to-base64-character [x]
  {:pre [(= 6 (count x))]}
  (let [base64chars "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        i (from-binary-byte x)]
    (get base64chars i)))

(defn encode-to-chars [x]
  (let [source (partition-all 3 x)
        octets (map #(.getBytes %) source);TODO:not used
        bits (to-binary x) ;TODO 
        padding-count 0
        padding (apply str (repeat padding-count \=))
        sextets (partition 6 6 0 bits)
        characters (map to-base64-character sextets)]
    (lazy-cat characters padding)))

(defn encode-to-bytes [x] (map byte (encode-to-chars x)))

(defn- base64-bits [x]
  (let [base64chars "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        i (string/index-of base64chars x)]
    (to-binary i)))

(defn decode-partition [x]
  (let [bits (map base64-bits x)
        sextets (map #(drop 2 %) bits)
        octets (partition 8 8 0 (flatten sextets))
        bytes (map from-binary-byte octets)]
    bytes))

(defmulti decode (fn [x] [(class x)]))

(defmethod decode [String] [x]
  (let [source (partition-all 4 x)
        bytes (flatten (map decode-partition source))]
    bytes))

(defmethod decode [(Class/forName "[B")] [x]
  (let [source (map #(apply str (map char %)) (partition-all 4 x))
        bytes (flatten (map decode-partition source))]
    bytes))
