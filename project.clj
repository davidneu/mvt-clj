(defproject mvt-clj "0.1.9"
  :description "A library in the mvt, minimum viable toolset, for repl driven Clojure development."
  :url "http://github.com/davidneu/mvt-clj"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1-beta2"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [clojure-complete "0.2.5"]
                 [prone "1.6.1"]
                 [clojure.java-time "0.3.2"]
                 [eftest "0.5.7"]]
  :local-repo ".m2"
  :jvm-opts ["-Dclojure.server.repl={:port 5555 :accept mvt-clj.repl/socket-repl}"
             "-Xms2g"
             "-Xmx2g"
             "-server"])

