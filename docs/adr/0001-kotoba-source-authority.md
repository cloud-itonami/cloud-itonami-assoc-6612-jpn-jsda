# ADR 0001: Kotoba is the JSDA catalog source authority

- Status: Accepted
- Date: 2026-07-21

`src/association_facts.kotoba` is the sole production source. It preserves the
first entry's absent establishment and revision dates, the complete 1975-02-19
establishment and 2026-06-16 revision dates for the second entry, Japanese UTF-8
titles, official citations, and asymmetric ordered topic sets. Unknown
associations, aliases, fields, topics, and indexes fail closed; no effects are
declared.

Conformance is observable semantics across the reference evaluator, restricted
JavaScript, and instantiated typed WebAssembly, including typed ABI, bounds,
effects, and rejection behavior. Compiler-output byte identity is not a language
gate. Clojure and the JVM are compiler/test hosts only.
