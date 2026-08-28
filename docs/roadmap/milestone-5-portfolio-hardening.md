# Milestone 5 — Portfolio Hardening

**Status:** Planned

## Objective

Turn implemented behaviour into credible, repeatable engineering evidence.

## Scope

- Run production-scenario integration tests against disposable PostgreSQL.
- Add metrics for transitions, idempotent reuse, conflicts, and unknown sessions.
- Document transaction boundaries, failure modes, and accepted risks.
- Provide sequence and state diagrams that match the implementation.
- Add repeatable demo commands for retry and concurrency scenarios.
- Document a safe inspection workflow for unresolved sessions.

## Acceptance criteria

- [ ] CI runs unit and PostgreSQL integration tests independently.
- [ ] The recovery scenario matrix is executable and documented.
- [ ] Metrics distinguish business latency from HTTP latency.
- [ ] README demonstrates duplicate and concurrent requests.
- [ ] Significant decisions record context, alternatives, and trade-offs.
- [ ] Known limitations are explicit and do not claim production guarantees.
- [ ] A clean checkout can build, test, package, and run from documentation.

## Non-goals

- Kubernetes or cloud deployment;
- formal production SLO claims without production evidence;
- an unauthenticated production operations console;
- adding features solely to expand a technology list.
