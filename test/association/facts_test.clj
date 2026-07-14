(ns association.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest jsda-has-spec-basis
  (let [sb (facts/spec-basis "jsda")]
    (is (= 2 (count sb)))
    (is (every? #(str/starts-with? (:association-rule/url %) "https://www.jsda.or.jp/") sb))
    (is (every? #(= "6612" (:association-rule/isic %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "keidanren")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["jsda" "keidanren"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["keidanren"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= ["jsda.investment-solicitation-customer-management"]
         (mapv :association-rule/id (facts/by-topic "jsda" :consumer-protection))))
  (is (empty? (facts/by-topic "jsda" :labor)))
  (is (empty? (facts/by-topic "keidanren" :governance))))
