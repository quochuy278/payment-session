# Payment Session Roadmap

This directory is the source of truth for project scope and milestone progress.
It tracks learning outcomes and executable evidence rather than feature count.

## Product statement

Build a backend that keeps a checkout financially correct when clients retry,
multiple devices act concurrently, and payment-provider results are delayed,
duplicated, or reordered.

The project is a greenfield exploration informed by production lessons. It does
not reproduce a previous production system or its provider-specific behaviour.

## Milestone status

| Milestone | Status | Outcome |
| --- | --- | --- |
| [1. Business skeleton](milestone-1-business-skeleton.md) | In progress | Persist and retrieve the minimum domain model |
| [2. Explicit state machine](milestone-2-state-machine.md) | Planned | Make legal and illegal transitions explicit |
| [3. Idempotency and concurrency](milestone-3-idempotency-and-concurrency.md) | Planned | Prevent duplicate sessions and over-reservation |
| [4. Unreliable provider signals](milestone-4-unreliable-provider-signals.md) | Planned | Converge duplicate and reordered results safely |
| [5. Portfolio hardening](milestone-5-portfolio-hardening.md) | Planned | Add evidence, observability, and operational clarity |

## Scope boundaries

Portfolio v1 intentionally excludes:

- real payment providers;
- authentication;
- refunds and automatic voids;
- realtime delivery;
- microservices and distributed infrastructure;
- a full order, menu, tax, or discount domain;
- frontend and administrative UI.

## Progress rules

- A milestone is complete only when every acceptance criterion has executable
  evidence.
- Do not implement work from a future milestone early.
- New scope must state which milestone owns it and why it improves learning.
- Applied Flyway migrations are immutable; schema changes use a new migration.
- Architecture decisions and known limitations must be documented with the
  milestone that introduces them.
