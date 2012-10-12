(ns safeguard.transform)

(defn integer
  "Parse value as an integer. Return zero on failure."
  [v]
	(try
		(Integer. v)
		(catch Exception e 0)))

(defn to
  "Modify a map value."
	[param-key transformation-fn]
	(fn [request]
		(update-in request [:params param-key] transformation-fn)))

(defn declare-safe-params
	"Copy a collection of ring ':params' keys to a map under the key :safe-params."
	[& param-keys]
	(fn [request]
		{:safe-params (merge (:safe-params request) (select-keys (:params request) param-keys))}))