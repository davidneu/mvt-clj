(ns mvt-clj.core-test
  (:require
   [clojure.test :refer :all]
   [mvt-clj.error :refer [print-error]]
   [mvt-clj.breakpoint :refer [break]]))

(defn break-test []
  (let [x 1 y 0]
    (break)
    (/ x y)))

(defn break-loop-test []
  (dotimes [x 5]
    (let [y 0]
      (break)
      (/ x y))))

;; (defn unable-to-resolve-symbol-test []
;;   (let [x 1
;;         y z]
;;     (println x)))

;; (defn even-number-of-forms-test []
;;   (let [x 1
;;         y]
;;     (println x)))

