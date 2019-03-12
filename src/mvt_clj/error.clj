(ns mvt-clj.error
  (:require
   [clojure.main]
   [clojure.pprint :as pprint]
   [clojure.string :as str]
   [prone.middleware :as prone]))

(defn print-non-execution-error [e]
  (print
   (str
    "\n"
    (clojure.main/ex-str (clojure.main/ex-triage (Throwable->map e)))
    "Location: "
    (str/join
     "\n"
     (map
      (fn [x] (str (:clojure.error/source (:data x)) ":" (:clojure.error/line (:data x))))
      (filter (fn [x] (:clojure.error/source (:data x))) (:via (Throwable->map e)))))
    "\n")))

(defn print-execution-error [e n]
  (let [{:keys [message type class-name frames]} (prone.stacks/normalize-exception e)]
    (print (str "\n" type ": " message))
    (pprint/print-table
     ["Location" "Function" "Found?"]
     (map
      (fn [{:keys [file-name line-number class-path-url loaded-from method-name class-name package lang]}]
        {"Location"
         (str
          (if-let [resource (clojure.java.io/resource class-path-url)]
            (let [project-directory (clojure.string/replace (-> (java.io.File. ".") .getAbsolutePath) #"/.$" "/")
                  location
                  (cond (str/includes? (str resource) (str "jar:file:" project-directory)) (str/replace (str resource) (str "jar:file:"  project-directory) "")
                        (str/includes? (str resource) (str "file:"  project-directory) ) (str/replace (str resource) (str "file:"  project-directory) "")
                        :else (str resource))]
              (if (str/includes? location "jar!/")
                (str/replace location "jar!/" "jar:")
                location))
            class-path-url)
          ":"
          line-number)
         "Function" (str package (if (= lang :java) "$" "/") method-name)
         "Found?" (if (clojure.java.io/resource class-path-url) "Yes" "No")})
      (take n frames)))))

(defn print-error
  ([e]
   (print-error e 20))
  ([e n]
   (if (not (nil? (:phase (Throwable->map e))))
     (print-non-execution-error e)
     (print-execution-error e n))))

