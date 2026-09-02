# ADR-002: Generate jOOQ types from PostgreSQL

## Status

Accepted

## Problem

jOOQ needs database metadata to generate type-safe Kotlin table and record
classes. Generated types must reflect the PostgreSQL schema produced by the
project's Flyway migrations without requiring a second, manually maintained
schema declaration.

## Options considered

### Generate from Flyway SQL with `DDLDatabase`

This avoids a live database connection, but jOOQ interprets the migrations with
a parser and an H2-backed metadata model rather than PostgreSQL itself. The
current migrations already require special transformations for PostgreSQL
`TEXT` primary keys and a multi-action `ALTER TABLE` statement. More
PostgreSQL-specific syntax would create more case-specific build logic.

### Generate directly from PostgreSQL

jOOQ reads PostgreSQL's real catalog after Flyway applies the migrations. This
requires the database to be reachable and current during generation, but avoids
SQL emulation and produces metadata matching the runtime database engine.

## Decision

Generate jOOQ Kotlin sources directly from PostgreSQL with
`PostgresDatabase`.

- Flyway migrations remain the source of truth for changing the schema.
- PostgreSQL's catalog is the source read by code generation.
- No SQL rewriting or migration-specific compatibility rules are used.
- `compileKotlin` depends on `jooqCodegen`, preventing compilation against stale
  generated types.
- `jooqCodegen` is never treated as up to date because Gradle cannot fingerprint
  external PostgreSQL metadata. Each invocation reads the current catalog.
- Generated files live under `build/generated-src/jooq/main` and are not
  committed.
- Only jOOQ records and table metadata are generated. Domain models and
  repositories remain explicit application code.

## Configuration

For local development, code generation reads the datasource from the
gitignored `config/application.yml`, using a YAML parser rather than custom
line-based parsing. `DATABASE_URL`, `DATABASE_USERNAME`, and
`DATABASE_PASSWORD` override those local values when present. They are the same
environment variables used by Spring Boot at runtime, avoiding a second
connection convention while keeping local startup persistent across terminals.

Before generation, the target database must have the checked-out Flyway
migrations applied. Code generation can then be run with:

```shell
./gradlew jooqCodegen
```

## Trade-offs and consequences

- PostgreSQL must be reachable during `jooqCodegen` and clean compilation.
- Code generation runs on each compilation and is slower than reusing generated
  output, in exchange for not silently trusting stale external metadata.
- If the database has not been migrated, generated types describe its older
  schema. If it contains migrations from another branch, generated types may
  describe a newer or incompatible schema.
- A dedicated local development database is safer than a shared database,
  because its migration state belongs to the checked-out project.
- Docker is optional. Native PostgreSQL or a remote PostgreSQL instance works
  as long as it is dedicated, reachable, and correctly migrated.
- Because build and runtime share connection variables, a production deployment
  must run the already-built JAR rather than invoking Gradle code generation
  with production credentials.
- A later PostgreSQL integration test can verify that Flyway successfully
  creates the expected schema from an empty database. It serves a different
  purpose from jOOQ's compile-time types.
