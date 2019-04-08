(ns mvt-clj.tools
  (:require
   [clojure.tools.namespace.repl :as ctnr]
   [clojure.java.io :as io]
   [java-time :as jt]
   [eftest.runner :as eftest]
   [mvt-clj.error]))

;; (clojure.tools.namespace.repl/disable-unload!)

(defn refresh [& options]
  (let [r (apply clojure.tools.namespace.repl/refresh options)]
    (if (instance? Exception r)
      (mvt-clj.error/print-error r)
      r)))

(defn testit []
  (binding [clojure.test/*test-out* *out*]
    (eftest/run-tests (eftest/find-tests "test"))))

(defn touch [fs]
  (doseq [f fs]
    (let [af (clojure.java.io/file f)]
      (if (.exists af)
        (.setLastModified
         af
         (jt/to-millis-from-epoch (jt/instant)))
        (throw (Exception. (format "File %s not found" (.getCanonicalPath af))))))))

