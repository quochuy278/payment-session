# Milestone 3 — Idempotency and Concurrency

**Status:** Planned

## Objective

Guarantee that retries converge on one logical payment session and concurrent
devices cannot reserve more than a checkout can pay.

## Scope

- Accept a stable idempotency key when initiating a payment session.
- Enforce idempotency with a PostgreSQL unique constraint.
- Return the existing result for a repeated logical request.
- Lock the checkout while calculating available payable value.
- Derive active reservation value from authoritative payment-session state.
- Create the reservation and session in one transaction.

## Acceptance criteria

- [ ] Sequential duplicate requests return the same session ID.
- [ ] Concurrent duplicate requests produce one database row.
- [ ] Two independent requests cannot over-reserve one checkout.
- [ ] Database constraints protect invariants when application checks race.
- [ ] Transaction boundaries and lock ordering are documented.
- [ ] Conflict and retry behaviour is deterministic.

## Non-goals

- distributed locks;
- PostgreSQL advisory locks unless row locking proves insufficient;
- multiple databases or read replicas;
- checkout line items, discounts, tax, or split-allocation rules.
