# Milestone 2 — Explicit State Machine

**Status:** Planned

## Objective

Make payment-session lifecycle rules explicit, framework-independent, and hard
to bypass.

## Proposed lifecycle

```text
RESERVED -> PROCESSING -> AUTHORIZED -> SETTLED
                    |             |
                    +-> FAILED    +-> UNKNOWN
```

The exact states and transitions require approval during this milestone; this
diagram is a working hypothesis rather than an implementation requirement.

## Scope

- Define legal transitions in domain language.
- Reject illegal transitions without mutating state.
- Prevent late events from regressing terminal state.
- Persist an append-only transition history.
- Map domain failures to stable API error responses.

## Acceptance criteria

- [ ] Every state has documented business meaning.
- [ ] Every legal transition has a domain unit test.
- [ ] Representative illegal transitions have unit tests.
- [ ] Domain workflow tests run without Spring or a database.
- [ ] Concurrent persistence uses guarded version/state updates.
- [ ] Invalid API transitions return `409 Conflict`.
- [ ] Transition history explains when and why state changed.

## Non-goals

- provider-specific states;
- webhook or polling endpoints;
- refunds, voids, and cancellation recovery;
- generic state-machine frameworks.
