(ns mvt-clj.repl
  (:require
   [mvt-clj.error :refer [print-error]]))

(defn repl
  "REPL with customized :init and :caught options."
  []
  (clojure.main/repl
   :init
   (fn []
     (require 'complete.core))
   :caught
   (fn [e]
     (print-error e))))

(defn socket-repl
  "REPL with predefined hooks for attachable socket server and
  customized :init and :caught options."
  []
  (clojure.main/repl
   :init
   (fn []
     (clojure.core.server/repl-init)
     (require 'complete.core))
   :read clojure.core.server/repl-read
   :caught
   (fn [e]
     (print-error e))))

