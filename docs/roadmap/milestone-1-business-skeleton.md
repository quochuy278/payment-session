# Milestone 1 — Business Skeleton

**Status:** In progress

## Objective

Establish the smallest backend-owned model that can persist and retrieve a
checkout and its payment session.

## Learning focus

- Kotlin immutable domain models and null safety;
- Spring constructor injection and configuration;
- Gradle Kotlin DSL dependency management;
- Flyway-owned PostgreSQL schema;
- jOOQ records mapped without leaking into the domain;
- explicit application transaction boundaries.

## Scope

- Define the requirements and invariants for `Checkout` and `PaymentSession`.
- Represent money as integer minor units with an explicit currency.
- Create the first Flyway migration after the model is approved.
- Create one checkout.
- Create one payment session for a checkout.
- Retrieve a payment session by ID.
- Return explicit API errors for invalid input and missing resources.

## Acceptance criteria

- [x] Spring Boot application runs with the development profile.
- [x] PostgreSQL, Flyway, and jOOQ dependencies are configured.
- [x] Local Supabase credentials remain outside Git.
- [x] Flyway connects through the Supabase session pooler.
- [x] Domain requirements and invariants are documented in
  [`ADR-001`](../decisions/ADR-001-payment-correctness-boundary.md).
- [ ] The initial migration is applied to the `payment` schema.
- [ ] jOOQ generated types compile from the migrated schema.
- [ ] Create and get use cases work through the HTTP API.
- [ ] Domain code has no dependency on Spring, jOOQ, or HTTP types.
- [ ] The application can be packaged and started from its executable JAR.

## Non-goals

- payment lifecycle transitions;
- idempotency and concurrency control;
- provider calls;
- generic CRUD repositories;
- update and hard-delete endpoints.

## Completion evidence

- Domain tests for money and construction invariants.
- One repository integration test against PostgreSQL.
- Curl examples for create and get operations.
- A short schema and transaction-boundary explanation in project docs.
