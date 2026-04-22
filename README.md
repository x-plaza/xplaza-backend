# X-Plaza Backend

[![Build Status](https://github.com/x-plaza/xplaza-backend/actions/workflows/main.yml/badge.svg?branch=main)](https://github.com/x-plaza/xplaza-backend/actions/workflows/main.yml)
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-green)
![Version](https://img.shields.io/badge/version-1.0.0-blue)

Backend service for the X-Plaza marketplace e-commerce platform. Production
features include multi-vendor catalogs, idempotent payments, Elasticsearch
product search, loyalty/gift cards, GDPR endpoints, MFA, OAuth2 social login,
CMS, abandoned-cart recovery, invoice PDFs and a domain-event-driven core.

See [`CHANGELOG.md`](CHANGELOG.md) for the full v1.0.0 feature list.

## Architecture

The application is structured as a **Modular Monolith** following **Domain-Driven Design (DDD)** principles. Code is organized by business domain rather than technical layer to enforce boundaries and simplify maintenance. Cross-module communication goes through a sealed `DomainEvent` hierarchy and a transactional outbox.

### Domains

- **`catalog`**: Products, brands, categories, tags, SEO, CSV import/export.
- **`order`**: Order processing, checkout, invoice PDFs, abandoned-cart recovery.
- **`payment`**: Stripe + COD gateways, idempotent webhooks, refunds, saved cards.
- **`auth`**: JWT, MFA (TOTP), OAuth2, account lockout, email verification.
- **`customer`**: Profile, addresses, GDPR export/erase, loyalty balance.
- **`cart`**: Shopping session management with abandoned-cart detection.
- **`search`**: Elasticsearch-backed product search and autocomplete.
- **`recommendation`**: Related, frequently-bought-together, recently-viewed.
- **`tax`**: Region-aware tax engine (zones + compound rules).
- **`giftcard`** / **`loyalty`**: Stored-value and points programs.
- **`vendor`**: Marketplace commission and weekly payouts.
- **`cms`**: Pages, banners, FAQ.
- **`notification`**: Email (Spring Mail), SMS (Telesign), preferences.
- **`common`**: Idempotency, rate limiting, events/outbox, audit, i18n.

## Technology Stack

- **Java 25** (SAP Machine) with virtual-thread executor for `@Async` work
- **Spring Boot 4.0.1** + Spring Security + Spring Data JPA / Elasticsearch
- **PostgreSQL 17** (production) / **H2** (local)
- **Hibernate 7** / **Flyway** for migrations (V1 + V2 included)
- **Redis 7** + **Caffeine** caching
- **Elasticsearch 8** for product search
- **Bucket4j** rate limiting and **Resilience4j** retry/circuit-breaker
- **Micrometer + Prometheus + OpenTelemetry** observability with structured JSON logs (`cloud` profile)
- **Stripe Java SDK** + idempotent COD gateway
- **OpenPDF** invoices, **Apache Commons CSV** import/export
- **dev.samstevens.totp** TOTP MFA
- **Telesign** SMS
- **JWT** + OAuth2 social login + BCrypt strength 12

## Development Setup

### Prerequisites

- JDK 25
- Docker (optional, for local PostgreSQL)

### Build & Run

#### 1. Build

```bash
./mvnw clean install -DskipTests
```

#### 2. Run (Local Profile)

Uses in-memory H2 database.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

#### 3. Run (Cloud Profile)

Requires a running PostgreSQL instance.

```bash
export DB_URL=jdbc:postgresql://localhost:5432/xplaza
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=cloud
```

## Testing & Code Quality

The project uses **Spotless** to enforce Google Java Style and **JaCoCo** for coverage.

```bash
# Run tests
./mvnw test

# Apply code formatting
./mvnw spotless:apply
```

## Deployment

The application is containerized using Docker.

```bash
docker build -t xplaza-backend .
```

### Profiles

| Profile | Database | Migrations | Usage |
|---------|----------|------------|-------|
| `local` | H2 | Disabled | Local development |
| `cloud` | PostgreSQL | Enabled | Production / CI |

### Local stack

`docker-compose.yml` brings up Postgres, Redis, Elasticsearch, MinIO, Mailhog, Prometheus and Jaeger:

```bash
docker compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### Observability endpoints

- Prometheus scrape: `GET /actuator/prometheus`
- Health: `GET /actuator/health`
- Tracing: OTLP exporter to `${OTEL_EXPORTER_OTLP_ENDPOINT}` when configured

### Operational headers

- `Idempotency-Key`: required header on `/api/v1/payments/**`,
  `/api/v1/checkout/**`, `/api/v1/orders/**` for safe at-most-once writes.
- `Accept-Language`: drives `LocaleResolver`; bundled translations: `en`, `es`.
