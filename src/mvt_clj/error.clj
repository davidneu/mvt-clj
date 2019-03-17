(ns mvt-clj.error
  (:require
   [clojure.main]
   [clojure.pprint :as pprint]
   [clojure.string :as str]
   [prone.middleware :as prone]))

 ;; TO DO: Try using working_dir: "${PWD}" in docker-compose.yml or in Dockerfile

(defn format-resource [r]
  (-> r
      (str/replace "jar:file:" "")
      (str/replace "file:" "")
      (str/replace "jar!/" "jar:")))

(defn print-non-execution-error [e]
  (print
   (str
    "\n"
    (clojure.main/ex-str (clojure.main/ex-triage (Throwable->map e)))
    "Location: "
    (str/join
     "\n"
     (map
      (fn [x]
        (str
         (let [source (:clojure.error/source (:data x))]
           (if (str/starts-with? source "/")
             source
             (format-resource (clojure.java.io/resource source))))
         ":"
         (:clojure.error/line (:data x))))
      (filter (fn [x] (:clojure.error/source (:data x))) (:via (Throwable->map e)))))
    "\n")))

(defn print-execution-error [e n]
  (let [{:keys [message type class-name frames]} (prone.stacks/normalize-exception e)]
    (print (str "\n" type ": " message))
    (pprint/print-table
     ["Location" "Function"]
     (map
      (fn [{:keys [file-name line-number class-path-url loaded-from method-name class-name package lang]}]
        {"Location"
         (str
          (if (clojure.java.io/resource class-path-url) "" "?/")
          (if-let [resource (clojure.java.io/resource class-path-url)]
            (format-resource resource)
            class-path-url)
          ":"
          line-number)
         "Function" (str package (if (= lang :java) "$" "/") method-name)})
      (take n frames)))))

(defn print-error
  ([e]
   (print-error e 20))
  ([e n]
   (if (not (nil? (:phase (Throwable->map e))))
     (print-non-execution-error e)
     (print-execution-error e n))))
