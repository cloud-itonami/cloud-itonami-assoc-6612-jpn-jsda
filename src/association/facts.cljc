(ns association.facts
  "Industry self-regulatory rule catalog for the Japan Securities Dealers
  Association (日本証券業協会 / JSDA) -- a third industry-association-level
  source (see cloud-itonami-assoc-6419-jpn-zenginkyo and
  cloud-itonami-assoc-6512-jpn-sonpo for the first two) per ADR-2607141700
  (cloud-itonami-compliance-fact-federation). Aligned to ISIC 6612
  (securities brokerage), one of the 12 ISIC-6419-sibling verticals
  already wired into cloud-itonami-isic-8291's compliance-intelligence
  links. Every entry cites an OFFICIAL jsda.or.jp URL -- never fabricated.
  A rule not in this table has NO spec-basis, full stop; extend `catalog`,
  do not invent an id/url.

  Both entries below were independently WebFetch-verified against the
  live jsda.or.jp page on 2026-07-14 (title + enactment/revision dates
  read back from the actual document listing, not guessed).")

(def catalog
  "assoc-slug -> vector of self-regulatory rule entries."
  {"jsda"
   [{:association-rule/id "jsda.articles-of-incorporation"
     :association-rule/title "定款 (Articles of Incorporation)"
     :association-rule/association "jsda"
     :association-rule/isic "6612"
     :association-rule/country "JPN"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://www.jsda.or.jp/about/kisoku/files/260401_teikann.pdf"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:governance}}
    {:association-rule/id "jsda.investment-solicitation-customer-management"
     :association-rule/title "協会員の投資勧誘、顧客管理等に関する規則 (Rules on Investment Solicitation and Customer Management for Member Firms)"
     :association-rule/association "jsda"
     :association-rule/isic "6612"
     :association-rule/country "JPN"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://www.jsda.or.jp/shijyo/seido/jishukisei/web-handbook/101_kanri/files/260616_toushikanyuu.pdf"
     :association-rule/url-provenance :official-association-site
     :association-rule/established-date "1975-02-19"
     :association-rule/last-revised-date "2026-06-16"
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:consumer-protection :fair-transaction :suitability}}]})

(defn spec-basis [assoc-slug] (get catalog assoc-slug))

(defn coverage
  ([] (coverage (keys catalog)))
  ([slugs]
   (let [have (filter catalog slugs)
         missing (remove catalog slugs)]
     {:requested (count slugs)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-6612-jpn-jsda Wave 0 (ADR-2607141700): "
                 (count (get catalog "jsda")) " jsda rules seeded with an "
                 "official jsda.or.jp citation. Extend "
                 "`association.facts/catalog`, never fabricate a rule id/url.")})))

(defn by-topic [assoc-slug topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis assoc-slug)))
