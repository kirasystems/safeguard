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
  	(is (= ((v/declare-safe-params :a :b) {:params {:a 1 :b 2 :c 3}})
		       {:safe-params {:a 1 :b 2}}))
		; TODO: We should also test the assertion here.
		))

(deftest test-validation-fn
	(testing "Testing basic must validation-fn."
	  (let [vfn (v/validation-fn
		            (v/must [:params :id] number?)
		            (v/must [:params :text] string?)
		            (v/must [:params :key] keyword?))]
		  (is (= (vfn {:params {:id 1 :text "foo" :key :a}}) {:params {:id 1 :text "foo" :key :a}}))
		  (is (nil? (vfn {:params {:id "1" :text "foo" :key :a}})))
		  (is (nil? (vfn {:params {:id 1 :text 1 :key :a}})))
		  (is (nil? (vfn {:params {:id 1 :text "foo" :key 1}})))))

	(testing "Testing basic validation-fn custom failure."
		(let [vfn (v/validation-fn
		            (v/on-failure :bad-id-text)
		          	(v/must [:params :id] number?)
	    	       	(v/must [:params :text] string?)
	          		(v/on-failure :bad-key)
			(v/must [:params :key] keyword?))]
			(is (= (vfn {:params {:id 1 :text "foo" :key :a}}) {:params {:id 1 :text "foo" :key :a}}))
			(is (= :bad-id-text (vfn {:params {:id "1" :text "foo" :key :a}})))
			(is (= :bad-id-text (vfn {:params {:id 1 :text 1 :key :a}})))
			(is (= :bad-key (vfn {:params {:id 1 :text "foo" :key 1}}))))))

(testing "Testing basic validation-fn mutation."
	(let [vfn (v/validation-fn
		           (v/must [:params :id] number?)
		           (v/must [:params :text] string?)
							 (v/declare-safe-params :id :text))]
		(is (= (vfn {:params {:id 1 :text "foo" :key :a}})
			     {:params {:id 1 :text "foo" :key :a} :safe-params {:id 1 :text "foo"}}))))