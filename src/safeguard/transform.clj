(ns safeguard.transform)

(defn integer
  "Parse value as an integer. Return zero on failure."
  [v]
	(try
		(Integer. v)
		(catch Exception e 0)))