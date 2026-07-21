# cloud-itonami-assoc-6612-jpn-jsda

Industry self-regulatory rule catalog for the **Japan Securities Dealers
Association** (日本証券業協会 / JSDA) — a third industry-association-level
source alongside
[`cloud-itonami-assoc-6419-jpn-zenginkyo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-jpn-zenginkyo)
and
[`cloud-itonami-assoc-6512-jpn-sonpo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6512-jpn-sonpo).
Part of the [`cloud-itonami`](https://github.com/cloud-itonami)
compliance-fact family (ADR-2607141700,
`cloud-itonami-compliance-fact-federation`, in `com-junkawasaki/root`).

Aligned to **ISIC 6612** (securities brokerage), one of the 12 verticals
already wired into `cloud-itonami-isic-8291`'s compliance-intelligence
links (ADR-2607110400).

## Scope

A **read-only reference/archive** catalog — not an Advisor⊣Governor
actuation actor. It proposes or executes nothing on the association's
behalf.

Coverage is reported honestly by the fail-closed exported Kotoba ABI: an
association not explicitly admitted has **no spec-basis**, full stop — never
fabricate one.

## Data

- `src/association_facts.kotoba` — the sole production catalog authority.
- `schema/association-rule.edn` — DataScript schema.
- `data/datascript-tx.edn` — derived DataScript tx-data (query this
  alongside other `cloud-itonami`/`etzhayyim` compliance-fact sources via
  `com-junkawasaki/root`'s `scripts/compliance-fact-query.cljs`).

Both entries cite an official [jsda.or.jp](https://www.jsda.or.jp/)
document, independently WebFetch-verified against the live page
(2026-07-14) — titles and enactment/revision dates read back from the
actual document listing, not guessed.

The catalog compiles through `kotoba-lang/compiler` to the reference evaluator,
restricted JavaScript, and typed WebAssembly. Clojure/JVM and Node are test and
compiler hosts only; neither is production authority. Compatibility is checked
by observable values, typed ABI, empty effects, bounds, and fail-closed
rejections—not compiler-output byte identity.

## License

AGPL-3.0-or-later (matches the `cloud-itonami-iso3166-*` /
`-municipality-*` / `-assoc-*` / `-lei-*` convention). Rule text itself
remains the association's; this repo stores only citation metadata
(id/title/url/dates), not full rule text.
