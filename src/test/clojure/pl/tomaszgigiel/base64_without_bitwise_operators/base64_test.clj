(ns pl.tomaszgigiel.base64-without-bitwise-operators.base64-test
  (:import java.util.Arrays)
  (:import java.util.Base64)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.base64-without-bitwise-operators.base64 :as my-base64])
  (:require [pl.tomaszgigiel.base64-without-bitwise-operators.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(tst/deftest to-binary-test
  (tst/is (= (my-base64/to-binary   1) '(0 0 0 0 0 0 0 1)))
  (tst/is (= (my-base64/to-binary   2) '(0 0 0 0 0 0 1 0)))
  (tst/is (= (my-base64/to-binary  10) '(0 0 0 0 1 0 1 0)))
  (tst/is (= (my-base64/to-binary  97) '(0 1 1 0 0 0 0 1)))
  (tst/is (= (my-base64/to-binary 128) '(1 0 0 0 0 0 0 0)))
  (tst/is (= (my-base64/to-binary 200) '(1 1 0 0 1 0 0 0)))
  (tst/is (= (my-base64/to-binary 240) '(1 1 1 1 0 0 0 0)))
  (tst/is (= (my-base64/to-binary 255) '(1 1 1 1 1 1 1 1)))
  (tst/is (= (my-base64/to-binary 256) (concat (my-base64/to-binary 1) (my-base64/to-binary 0)) '(0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0)))
  (tst/is (= (my-base64/to-binary \a) (my-base64/to-binary (char 97)) (my-base64/to-binary 97)))
  (tst/is (= (my-base64/to-binary \b) (my-base64/to-binary (char 98)) (my-base64/to-binary 98)))
  (tst/is (= (my-base64/to-binary \c) (my-base64/to-binary (char 99)) (my-base64/to-binary 99)))
  (tst/is (= (my-base64/to-binary \ð) (my-base64/to-binary (char 240)) (my-base64/to-binary 240)))
  (tst/is (= (my-base64/to-binary "abc") (concat (my-base64/to-binary \a) (my-base64/to-binary \b) (my-base64/to-binary \c)) '(0 1 1 0 0 0 0 1 0 1 1 0 0 0 1 0 0 1 1 0 0 0 1 1))))

(tst/deftest from-binary-test
  (tst/is (= (my-base64/from-binary '(0 0 0 0 0 0 0 1)) '(1)))
  (tst/is (= (my-base64/from-binary '(0 0 0 0 0 0 1 0)) '(2)))
  (tst/is (= (my-base64/from-binary '(0 0 0 0 1 0 1 0)) '(10)))
  (tst/is (= (my-base64/from-binary '(0 1 1 0 0 0 0 1)) '(97)))
  (tst/is (= (my-base64/from-binary '(1 0 0 0 0 0 0 0)) '(128)))
  (tst/is (= (my-base64/from-binary '(1 1 0 0 1 0 0 0)) '(200)))
  (tst/is (= (my-base64/from-binary '(1 1 1 1 0 0 0 0)) '(240)))
  (tst/is (= (my-base64/from-binary '(1 1 1 1 1 1 1 1)) '(255)))
  (tst/is (= (my-base64/from-binary '(0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0)) '(1 0)))
  (tst/is (= (my-base64/from-binary '(0 1 1 0 0 0 0 1 0 1 1 0 0 0 1 0 0 1 1 0 0 0 1 1)) '(97 98 99))))

(defn java-base64-encode [to-encode] (.encode (Base64/getEncoder) (.getBytes to-encode)))
(defn java-base64-encode-to-string [to-encode] (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))
(defn java-base64-decode [to-decode] (.decode (Base64/getDecoder) to-decode))

(tst/deftest java-test
  (tst/is (= (java-base64-encode-to-string "abc") "YWJj"))
  (tst/is (Arrays/equals (java-base64-encode "abc") (.getBytes "YWJj")))
  (tst/is (Arrays/equals (java-base64-decode "YWJj") (.getBytes "abc")))
  (tst/is (Arrays/equals (java-base64-decode (.getBytes "YWJj")) (.getBytes "abc"))))

(tst/deftest my-test
  (tst/is (= (apply str (my-base64/encode-to-chars "abc")) "YWJj"))
  (tst/is (Arrays/equals (byte-array (my-base64/encode-to-bytes "abc")) (.getBytes "YWJj")))
  (tst/is (Arrays/equals (byte-array (my-base64/decode "YWJj")) (.getBytes "abc")))
  (tst/is (Arrays/equals (byte-array (my-base64/decode (.getBytes "YWJj"))) (.getBytes "abc"))))
