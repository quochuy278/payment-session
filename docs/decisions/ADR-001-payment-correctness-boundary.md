# ADR-001 — Define the Payment Correctness Boundary and Business Invariants

- **Status:** Accepted
- **Date:** 2026-09-01
- **Applies to:** Milestones 1–4

## Problem

Payment implementation must begin from explicit business guarantees rather
than from a database schema or framework model.

The system needs to remain financially correct when:

- a client retries payment initiation;
- multiple devices act on the same payable balance;
- a provider result is delayed, duplicated, reordered, or missing;
- provider authorization succeeds but downstream completion fails;
- the system cannot yet determine whether money moved.

Without a defined correctness boundary, these cases can produce duplicate
payment attempts, over-reservation, lost successful payments, premature
refunds, or contradictory state.

## Context

The project is a greenfield learning system. It is informed by recurring
production lessons, but it does not reproduce a previous commerce platform or
provider-specific implementation.

Portfolio v1 models only the minimum concepts needed to reason about payment
correctness:

- `Checkout`: an immutable payable obligation;
- `PaymentSession`: one logical payment attempt;
- `Reservation`: payable value held by an active payment session;
- `Authorization`: evidence that a provider approved the payment;
- `Settlement`: the durable application of an authorized payment;
- `Unknown`: an explicit state where the financial outcome is not yet known;
- `IdempotencyKey`: the stable identity of one logical initiation request.

A cart, product catalogue, full order lifecycle, refunds, and real provider
integrations are outside the v1 boundary.

## Options Considered

### Option A — Let the client own payment outcome

The client creates payment records and reports whether a payment succeeded.

This is simple on the happy path but cannot safely resolve retries, lost
callbacks, concurrent clients, or disagreement with provider state.

### Option B — Treat timeout as failure and release value immediately

This improves apparent availability because the client can retry at once.

It is rejected because a timed-out provider operation may still have charged
the customer. Releasing the reserved value can permit a second charge.

### Option C — Make checkout mutable during payment

Products and total value remain editable after payment sessions exist.

This is rejected for v1 because it creates competing definitions of the amount
being paid. A future cart may remain mutable, but conversion from cart to
checkout must create an immutable payment snapshot.

### Option D — Use a backend-owned checkout and explicit payment sessions

The backend owns durable payment state, reserves payable value before external
processing, preserves uncertainty, and applies confirmed financial effects
idempotently.

This option is selected.

## Decision

### Backend ownership

The backend and PostgreSQL are authoritative for payment-session state.

- A client may request a payment attempt but cannot declare it successful.
- A provider response is evidence used to evaluate a legal state transition.
- A transient callback or realtime signal is not a second source of truth.

### Immutable checkout boundary

A checkout is an immutable snapshot once created.

- Its total amount and currency do not change.
- Product mutation is not part of the checkout boundary.
- If a cart is introduced later, it remains separate and is converted into an
  immutable checkout before payment begins.

### Money invariants

All monetary values use integer minor units and an explicit currency.

For a checkout:

```text
availableAmount = checkoutTotal - settledAmount - reservedAmount

checkoutTotal > 0
settledAmount >= 0
reservedAmount >= 0
settledAmount + reservedAmount <= checkoutTotal
availableAmount >= 0
```

A payment session must satisfy:

- `amountMinor > 0`;
- its currency equals the checkout currency;
- its amount does not exceed the available amount when reserved;
- amount and currency are immutable after creation.

Reserved and settled amounts are derived from authoritative payment-session
state rather than maintained as independently mutable checkout fields.

### Idempotent initiation

A client supplies an idempotency key for one logical payment attempt.

- The pair `(checkoutId, idempotencyKey)` identifies one Payment Session.
- Repeating the same logical request returns the existing session.
- A PostgreSQL unique constraint is the final concurrency guard.
- Query-before-insert alone is not considered sufficient protection.

### Reservation and concurrency

Creating a payment session reserves its amount before external payment work
begins.

- The checkout is locked while available value is calculated.
- Validation and session creation occur in one database transaction.
- Concurrent requests cannot reserve more than the checkout total.
- Multiple partial payments are allowed when their combined value remains
  within the available amount.

### Explicit uncertainty

A timeout is not proof of failure.

- An uncertain session remains represented as `UNKNOWN`.
- `UNKNOWN` continues to hold its reservation.
- Only confirmed failure releases the reservation.
- Reconciliation may later resolve `UNKNOWN` into a confirmed outcome.
- A late event cannot regress a newer terminal state.

This intentionally favors financial safety over immediate retry availability.

### Reconciliation before compensation

The system does not refund or void merely because the result is uncertain.

- A valid payment for an outstanding obligation is reconciled.
- A confirmed duplicate or overpayment may require compensation in a future
  milestone.
- Insufficient evidence remains `UNKNOWN` or requires manual review.

The objective is financial correctness and customer intent, not maximizing
retained revenue. Automatic refund and void behaviour remain outside v1.

### Downstream completion failure

A provider-confirmed payment is not lost or converted to failure because a
downstream checkout or order-completion step fails.

- The session preserves the confirmed authorization.
- Downstream completion is retried or reconciled.
- Repeating completion for the same session produces one business effect.
- The customer is not asked to pay again while a confirmed or unknown payment
  still holds the payable value.

The system guarantees recoverability and durable evidence. It does not claim
that an external downstream operation can always complete successfully.

### State and event integrity

- State changes occur only through legal domain transitions.
- A session is settled at most once.
- Only an authorized session can be settled.
- Duplicate provider events converge without duplicate financial effects.
- Provider transaction references and event identities remain stable.
- Important transitions retain source, time, and reason information for audit.

The exact state set and transition graph are deferred to Milestone 2.

## Enforcement Boundaries

| Invariant | Primary enforcement |
| --- | --- |
| Positive amount | Domain validation and PostgreSQL `CHECK` |
| Checkout existence | PostgreSQL foreign key |
| Idempotent session identity | PostgreSQL unique constraint |
| Matching currency | Application transaction |
| No over-reservation | Checkout row lock and application transaction |
| Legal transition | Domain model |
| No state regression | Guarded database update |
| Settle at most once | State guard and database transaction |
| Duplicate provider event | Stable event identity and unique constraint |

No single layer is expected to enforce every invariant. Each rule is placed at
the strongest boundary that has enough information to protect it.

## Guarantees

Portfolio v1 aims to demonstrate these guarantees:

1. One logical initiation request creates at most one Payment Session.
2. A checkout is never reserved or settled beyond its total amount.
3. Duplicate and reordered signals do not create duplicate financial effects.
4. An unknown outcome is not falsely converted into success or failure.
5. A confirmed payment remains durable and recoverable when downstream
   completion fails.

The system does not claim exactly-once delivery. It aims for exactly-once
business effects under at-least-once delivery.

## Trade-offs

- Holding reservations for unknown outcomes can temporarily block a legitimate
  retry.
- Checkout row locking reduces concurrency for one checkout but simplifies the
  financial invariant.
- An immutable checkout requires a separate cart model if editable products are
  introduced later.
- Transition history and stable event identities add storage and mapping work.
- Separating authorization from settlement adds states but preserves recovery
  information when downstream completion fails.

## Consequences

- Migration design must encode positive amounts, foreign keys, and uniqueness
  constraints structurally.
- Domain models must not depend on Spring, jOOQ, or HTTP types.
- Payment-session creation requires an application-level transaction boundary.
- Milestone 2 must define the exact legal transition graph.
- Milestone 3 must prove idempotency and reservation behaviour under concurrent
  PostgreSQL transactions.
- Milestone 4 must test delayed, duplicate, reordered, and missing provider
  results without implementing real terminal hardware.
- Refund, void, cart mutation, and full order placement remain explicitly out of
  scope until evidence justifies expanding the model.
