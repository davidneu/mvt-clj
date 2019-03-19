(ns mvt-clj.tools
  (:require
  [clojure.tools.namespace.repl]      
   [mvt-clj.error :refer [print-error]]))

(defn refresh [& options]
  (let [r (apply clojure.tools.namespace.repl/refresh options)]
    (if (instance? Exception r)
      (print-error r)
      r)))
