(ns safeguard.core
	(:use [ring.util.response :only [response?]]))

(defn policy
	"Combine a sequence of validation rules into a policy. A policy passes only if
all rules pass. The result of the first failure is returned."
	[ & validation-fns]
	(fn [request]
		(loop [req  request
			     sfns (seq validation-fns)]
			(if-not sfns
				request
				(let [result ((first sfns) req)]
					(if (response? result)
						result
						(recur result (next sfns))))))))

(defn wrap-policy
  "Wrap a handler in a policy."
	[pol handler]
	(fn [request]
		(let [result (pol request)]
			(if (response? result)
				result
				(handler result)))))

(defmacro with-policy
	"Declare a route body to have a policy."
	[pol bindings & body]
	`(wrap-policy ~pol
		 (fn [request#]
			 (let [~bindings request#]
				 ~@body))))