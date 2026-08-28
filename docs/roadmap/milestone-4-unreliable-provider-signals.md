# Milestone 4 — Unreliable Provider Signals

**Status:** Planned

## Objective

Produce one financial effect when external results are delayed, duplicated,
missing, or reordered.

## Scope

- Introduce one fake provider adapter with controllable outcomes.
- Give provider events a stable identity.
- Converge webhook-like and polling-like results on one finalization use case.
- Preserve uncertain outcomes as `UNKNOWN`.
- Reconcile `UNKNOWN` into a confirmed outcome without state regression.
- Make duplicate finalization a safe no-op.

## Acceptance criteria

- [ ] Duplicate provider events produce one effect.
- [ ] Webhook before polling and polling before webhook converge identically.
- [ ] A late event cannot regress a terminal state.
- [ ] Timeout remains visible as `UNKNOWN`.
- [ ] Reconciliation can resolve `UNKNOWN` safely.
- [ ] Application restart does not lose recoverable state.

## Non-goals

- integration with a real payment provider;
- automatic refund or void;
- scheduled cloud jobs;
- a provider factory or hierarchy without a second real variation.
