(ns association-facts-test
  (:require [clojure.java.io :as io] [clojure.java.shell :as shell]
            [clojure.test :refer [deftest is testing]]
            [kotoba.compiler.core :as compiler] [kotoba.kir :as ir]))
(def source (slurp "src/association_facts.kotoba"))
(defn call [kir f & xs] (ir/execute kir f (vec xs)))
(defn present [x] (when (second x) (nth x 2)))
(def fields ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
             "established-date" "last-revised-date" "retrieved-at"])
(def expected
  [{"id" "jsda.articles-of-incorporation" "title" "定款 (Articles of Incorporation)"
    "association" "jsda" "isic" "6612" "country" "JPN" "kind" "self-regulatory-code"
    "url" "https://www.jsda.or.jp/about/kisoku/files/260401_teikann.pdf"
    "url-provenance" "official-association-site" "established-date" nil
    "last-revised-date" nil "retrieved-at" "2026-07-14"}
   {"id" "jsda.investment-solicitation-customer-management"
    "title" "協会員の投資勧誘、顧客管理等に関する規則 (Rules on Investment Solicitation and Customer Management for Member Firms)"
    "association" "jsda" "isic" "6612" "country" "JPN" "kind" "self-regulatory-code"
    "url" "https://www.jsda.or.jp/shijyo/seido/jishukisei/web-handbook/101_kanri/files/260616_toushikanyuu.pdf"
    "url-provenance" "official-association-site" "established-date" "1975-02-19"
    "last-revised-date" "2026-06-16" "retrieved-at" "2026-07-14"}])
(deftest reference-preserves-authority
  (let [kir (:kir (compiler/compile-source source :js-kotoba-v1))
        observed (mapv (fn [i] (into {} (map (fn [f] [f (present (call kir 'entry-field "jsda" i f))]) fields))) [0 1])]
    (is (= expected observed))
    (is (= [[nil nil] ["1975-02-19" "2026-06-16"]]
           (mapv (fn [i] (mapv #(present (call kir 'entry-field "jsda" i %)) ["established-date" "last-revised-date"])) [0 1])))
    (is (= [["governance"] ["consumer-protection" "fair-transaction" "suitability"]]
           (mapv (fn [i] (mapv #(present (call kir 'topic "jsda" i %)) (range (call kir 'topic-count "jsda" i)))) [0 1])))
    (is (= "jsda.articles-of-incorporation" (present (call kir 'by-topic-id "jsda" "governance" 0))))
    (is (= "jsda.investment-solicitation-customer-management" (present (call kir 'by-topic-id "jsda" "suitability" 0))))
    (is (= #{} (set (:effects kir))))
    (testing "fail closed"
      (is (zero? (call kir 'entry-count "japan-securities-dealers-association")))
      (is (zero? (call kir 'entry-count "finra")))
      (is (nil? (present (call kir 'entry-field "jsda" 2 "id"))))
      (is (nil? (present (call kir 'entry-field "jsda" 0 "established-date"))))
      (is (nil? (present (call kir 'topic "jsda" 0 1))))
      (is (zero? (call kir 'by-topic-count "jsda" "labor")))
      (is (nil? (present (call kir 'by-topic-id "jsda" "governance" 1)))))))
(defn compiler-root [] (nth (iterate #(.getParent ^java.nio.file.Path %)
  (java.nio.file.Path/of (.toURI (io/resource "kotoba/compiler/core.clj")))) 4))
(defn base64 [x] (.encodeToString (java.util.Base64/getEncoder) x))
(deftest restricted-js-and-wasm-conform-semantically
  (let [js (compiler/compile-source source :js-kotoba-v1) wasm (compiler/compile-source source :wasm32-browser-kotoba-v1)
        js64 (base64 (.getBytes ^String (:source js) "UTF-8")) wasm64 (base64 ^bytes (:bytes wasm))
        p (shell/sh "node" "--input-type=module" "-e"
            (str "import(process.argv[1]).then(async h=>{const j=await import('data:text/javascript;base64," js64 "');const w=await h.instantiateKotoba(Buffer.from(process.argv[2],'base64'));const r=x=>{if(x['entry-field']('jsda',0n,'established-date')[1]!==false||x['entry-field']('jsda',0n,'last-revised-date')[1]!==false||x['entry-field']('jsda',1n,'established-date')[2]!=='1975-02-19'||x['entry-field']('jsda',1n,'last-revised-date')[2]!=='2026-06-16')throw Error('dates');if(x['topic-count']('jsda',1n)!==3n||x['by-topic-id']('jsda','suitability',0n)[2]!=='jsda.investment-solicitation-customer-management'||x['entry-count']('japan-securities-dealers-association')!==0n)throw Error('authority');};r(j.instantiateKotoba({}));r(w.instance.exports)}).catch(e=>{console.error(e);process.exit(99)})")
            (.toString (.toUri (.resolve (compiler-root) "runtime/browser-host.mjs"))) wasm64)]
    (is (zero? (:exit p)) (str (:out p) (:err p)))))
(deftest production-source-authority
  (is (= ["src/association_facts.kotoba"] (->> (file-seq (io/file "src")) (filter #(.isFile %)) (map str) sort vec))))
