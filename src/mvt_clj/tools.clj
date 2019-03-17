(ns mvt-clj.tools
  (:require
  [clojure.tools.namespace.repl]      
   [mvt-clj.error :refer [print-error]]))

(defn reset []
  (let [r (clojure.tools.namespace.repl/refresh)]
    (if (instance? Exception r)
      (print-error r)
      r)))
