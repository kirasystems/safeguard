(ns safeguard.validate-test
  (:require [safeguard.validate :as v])
	(:use clojure.test))


(deftest test-must
	(testing "Testing single-key must."
		(is (= ((v/must :a #(= % 1)) {:a 1}) {}))
		(is (nil? ((v/must :a #(= % 1)) {:a 0}))))

	(testing "Testing nested-key must."
		(is (= ((v/must [:a] #(= % 1)) {:a 1}) {}))
		(is (= ((v/must [:a :b] #(= % 1)) {:a {:b 1}}) {}))
		(is (nil? ((v/must [:a] #(= % 1)) {:a 0})))
		(is (nil? ((v/must [:a :b] #(= % 1)) {:a {:b 0}}))))

	(testing "Testing general must."
		(is (= ((v/must #(= (:a %) 1)) {:a 1}) {}))
		(is (nil? ((v/must #(= (:a %) 1)) {:a 0})))))


(deftest test-declare-safe-params
	(testing "Testing basic declare-safe-params."
  	(is (=
		      ((v/declare-safe-params :a :b) {:params {:a 1 :b 2 :c 3}})
		      {:safe-params {:a 1 :b 2}}))))
