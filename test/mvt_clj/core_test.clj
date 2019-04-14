(ns mvt-clj.core-test
  (:require
   [clojure.test :refer :all]
   [mvt-clj.breakpoint :refer [break]]))

(deftest pass-test
  (testing "I passed"
    (is (= 1 1))))

(deftest fail-test
  (testing "I failed"
    (is (= 0 1))))

(defn division-by-zero-test []
    (/ 1 0))

(defn break-test []
  ;; d is randomly set to 0 or 1.
  (let [n 5
        d (rand-int 2)]
    (break)
    (/ n d)))

(defn break-loop-test []
  (dotimes [n 5]
    ;; d is randomly set to 0 or 1.
    (let [d (rand-int 2)]
      (break)
      (/ n d))))

(comment
  (defn unable-to-resolve-symbol-test []
    (let [x 1
          y z]
      (println x)))

  (defn even-number-of-forms-test []
    (let [x 1
          y]
      (println x))))

