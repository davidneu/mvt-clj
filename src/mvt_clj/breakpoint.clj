(ns mvt-clj.breakpoint
  (:require
   [clojure.main]))

(def break? (atom false))

(defn break-on []
  (reset! break? true))

(defn break-off []
  (reset! break? false))

(defn toggle-break []
  (swap! break? (fn [b] (not b))))

(defn contextual-eval [ctx expr]
  (eval
   `(let [~@(mapcat (fn [[k v]] [k `'~v]) ctx)]
      ~expr)))

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

(defmacro local-context []
  (let [symbols (keys &env)]
    (zipmap
     (map (fn [sym] `(quote ~sym)) symbols)
     symbols)))

(defmacro break []
  `(when @break?
    (clojure.main/repl
     :prompt #(print "debug=> ")
     :read readr
     :eval (partial contextual-eval (local-context)))))

