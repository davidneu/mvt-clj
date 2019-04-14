(ns dev
  (:require
   [clojure.tools.namespace.repl :refer [refresh refresh-all clear]]   
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer [javadoc]]
   [clojure.pprint :refer [pprint]]
   [clojure.repl :refer [apropos dir doc find-doc pst source]]
   [clojure.set :as set]
   [clojure.string :as str]
   [mvt-clj.tools]
   [mvt-clj.breakpoint :refer [break-on break-off]]))

(defn init []
  (println "running dev/init"))

(defn reset []
  (mvt-clj.tools/refresh :after 'dev/init))

