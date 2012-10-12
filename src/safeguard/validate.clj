(ns safeguard.validate
	(:use clojure.stacktrace))


(defn validation-fn
  "Return a validation function from a set of rules."
	[& rules]
  (fn [m]
	  (loop [m       m
	         rules   (seq rules)]
	    (if-not rules
		    (dissoc m ::on-failure)
	      (if-let [result ((first rules) m)]
		      (recur (merge m result) (next rules))
		      (::on-failure m))))))

(defn validate
	"Validate a map with rules."
	[m & rules]
	((apply validation-fn rules) m))

(defmacro defvalidation
  "Create a validation function and bind it to a symbol."
	[sym & rules]
	`(def ~sym (validation-fn ~@rules)))

(defn on-failure
  "Set the value to return on short-circuit failure."
	[failure-value]
	(fn [request] (assoc request ::on-failure failure-value)))

(defn must
	"Check a validation predicate and stop on failure."
	([param-key validation-fn]
	 (let [pkey (if (vector? param-key) param-key [param-key])]
     (fn [data]
	     (if (validation-fn (get-in data pkey)) {}))))
  ([validation-fn]
	  (fn [request] (if (validation-fn request) {}))))

(defn is
  "Check a validation predicate and collect error on failure."
	([param-key validation-fn error-msg]
		(fn [request]
			(if (validation-fn (get (:params request) param-key))
				{}
				{::errors (update-in (::errors request) [param-key] conj error-msg)})))
	([validation-fn error-msg]
		(fn [request]
			(or
				(validation-fn request)
				{::perrors (conj (::perrors request) error-msg)}))))

(defn to
	[param-key transformation-fn]
	(fn [request]
	  (update-in request [:params param-key] transformation-fn)))

(defn declare-safe-params
  "Copy a collection of ring ':params' keys to a map under the key :safe-params."
	[& param-keys]
  (fn [request]
    (assert (empty? (select-keys (:safe-params request) param-keys)))
	  {:safe-params (merge (:safe-params request) (select-keys (:params request) param-keys))}))
