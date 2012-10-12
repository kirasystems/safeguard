(ns safeguard.transform-test
	(:require
		[safeguard.validate :as v]
		[safeguard.transform :as t])
	(:use clojure.test))

(deftest test-declare-safe-params
	(testing "Testing basic declare-safe-params."
		(is (= ((t/declare-safe-params :a :b) {:params {:a 1 :b 2 :c 3}})
			    {:safe-params {:a 1 :b 2}}))
		; TODO: We should also test the assertion here.
		))

(testing "Testing basic validation-fn mutation."
	(let [vfn (v/validation-fn
		(v/must [:params :id] number?)
		(v/must [:params :text] string?)
		(t/declare-safe-params :id :text))]
		(is (= (vfn {:params {:id 1 :text "foo" :key :a}})
			    {:params {:id 1 :text "foo" :key :a} :safe-params {:id 1 :text "foo"}}))))
