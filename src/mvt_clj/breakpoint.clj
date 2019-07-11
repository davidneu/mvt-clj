(ns mvt-clj.breakpoint
  (:require
   [clojure.main]))

(def ^:dynamic *level* 0)

(def ^:dynamic *locals*)

(def break? (atom false))

(defn break-on []
  (reset! break? true))

(defn break-off []
  (reset! break? false))

(defn toggle-break []
  (swap! break? (fn [b] (not b))))

(defmacro local-bindings []
  "Produces a map of the names of local bindings to their values."
  (let [symbols (keys &env)]
    (zipmap (map (fn [sym] `(quote ~sym)) symbols) symbols)))

(defn eval-with-locals [locals form]
  "Evals a form with given locals. The locals should be a map of symbols to values."
  (binding [*locals* locals]
    (eval
     `(let ~(vec (mapcat #(list % `(*locals* '~%)) (keys locals)))
        ~form))))

(defn readr [prompt exit-code]
  (let [input (clojure.main/repl-read prompt exit-code)]
    (cond (= input :exit)
          exit-code
          (= input :quit)
          (do
            (reset! break? false)
            exit-code)
          :else
          input)))

(defmacro break []
  `(when @break?
     (let [eval-fn# (partial eval-with-locals (local-bindings))]
       (binding
           [*level* (inc *level*)]
           (clojure.main/repl
            ;; :prompt #(print (format "debug-%d=> " *level*))
            :prompt #(print
                      (if (= *level* 1)
                        "debug=> "
                        (format "debug-%d=> " *level*)))
            :read readr
            :eval eval-fn#)))))

