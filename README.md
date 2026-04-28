# X-Plaza Backend

<p align="center">
  <a href="https://github.com/x-plaza/xplaza-backend/actions/workflows/main.yml"><img src="https://github.com/x-plaza/xplaza-backend/actions/workflows/main.yml/badge.svg?branch=main" alt="Main Pipeline"></a>
  <a href="https://github.com/x-plaza/xplaza-backend/actions/workflows/pr.yml"><img src="https://github.com/x-plaza/xplaza-backend/actions/workflows/pr.yml/badge.svg" alt="PR Pipeline"></a>
  <a href="https://github.com/x-plaza/xplaza-backend/actions/workflows/security.yml"><img src="https://github.com/x-plaza/xplaza-backend/actions/workflows/security.yml/badge.svg" alt="Security Scan"></a>
  <br>
  <img src="https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white" alt="Java 25">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.5-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.0.5">
  <img src="https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql&logoColor=white" alt="PostgreSQL 17">
  <img src="https://img.shields.io/badge/Elasticsearch-8-005571?logo=elasticsearch&logoColor=white" alt="Elasticsearch 8">
  <img src="https://img.shields.io/badge/Redis-7-DC382D?logo=redis&logoColor=white" alt="Redis 7">
  <img src="https://img.shields.io/badge/License-Proprietary-blue" alt="License">
</p>

Backend service for the **X-Plaza** multi-vendor marketplace platform. Built as a modular monolith with domain-driven design, it powers multi-vendor catalogs, idempotent payments, full-text product search, loyalty & gift cards, GDPR compliance, MFA, OAuth2 social login, CMS, abandoned-cart recovery, invoice generation, and a domain-event-driven core.

## Architecture

The application is structured as a **Modular Monolith** following **Domain-Driven Design (DDD)** principles. Code is organized by business domain rather than technical layer, enforcing clear boundaries and simplifying long-term maintenance. Cross-module communication flows through a sealed `DomainEvent` hierarchy and a transactional outbox.

```
src/main/java/com/xplaza/backend/
├── catalog/        # Products, brands, categories, tags, SEO, CSV import/export
├── order/          # Order processing, checkout, invoice PDFs, abandoned-cart recovery
├── payment/        # Stripe + COD gateways, idempotent webhooks, refunds, saved cards
├── auth/           # JWT, MFA (TOTP), OAuth2, account lockout, email verification
├── customer/       # Profile, addresses, GDPR export/erase, loyalty balance
├── cart/           # Shopping session management with abandoned-cart detection
├── search/         # Elasticsearch-backed product search and autocomplete
├── recommendation/ # Related, frequently-bought-together, recently-viewed
├── tax/            # Region-aware tax engine (zones + compound rules)
├── giftcard/       # Stored-value programs
├── loyalty/        # Points-based rewards program
├── vendor/         # Marketplace commission and weekly payouts
├── cms/            # Pages, banners, FAQ
├── notification/   # Email (Spring Mail), SMS (Telesign), preferences
└── common/         # Idempotency, rate limiting, events/outbox, audit, i18n
```

## Technology Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Java 25 (SAP Machine) with virtual-thread executor |
| **Framework** | Spring Boot 4.0.5 + Spring Security + Spring Data JPA / Elasticsearch |
| **Database** | PostgreSQL 17 (production) · H2 (local) |
| **ORM & Migrations** | Hibernate 7 · Flyway |
| **Caching** | Redis 7 + Caffeine (L1/L2 strategy) |
| **Search** | Elasticsearch 8 |
| **Resilience** | Bucket4j rate limiting · Resilience4j retry/circuit-breaker |
| **Observability** | Micrometer + Prometheus + OpenTelemetry · Structured JSON logs |
| **Payments** | Stripe Java SDK · Idempotent COD gateway |
| **Documents** | OpenPDF invoices · Apache Commons CSV import/export |
| **Authentication** | JWT + OAuth2 social login + BCrypt (strength 12) · TOTP MFA |
| **Notifications** | Spring Mail (email) · Telesign (SMS) |

## Getting Started

### Prerequisites

- **JDK 25** ([SAP Machine](https://sap.github.io/SapMachine/) recommended)
- **Docker** (for the local infrastructure stack)

### Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/x-plaza/xplaza-backend.git
cd xplaza-backend

# 2. Start infrastructure (Postgres, Redis, Elasticsearch, MinIO, Mailhog)
docker compose up -d

# 3. Build
./mvnw clean install -DskipTests

# 4. Run with the local profile (H2 in-memory database)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The API is available at **http://localhost:8080**.

### Running with PostgreSQL

```bash
export DB_URL=jdbc:postgresql://localhost:5432/xplaza
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=cloud
```

### Profiles

| Profile | Database | Migrations | Elasticsearch | Usage |
|---------|----------|------------|---------------|-------|
| `local` | H2 (in-memory) | Disabled | Optional | Local development |
| `cloud` | PostgreSQL 17 | Flyway | Required | Production / CI |

## Testing & Code Quality

```bash
# Run tests
./mvnw test

# Check code formatting (Google Java Style via Spotless)
./mvnw spotless:check

# Auto-fix formatting
./mvnw spotless:apply

# Run Checkstyle
./mvnw checkstyle:check
```

## Deployment

### Docker

```bash
docker build -t xplaza-backend .
docker run -p 8080:10001 xplaza-backend
```

The CI pipeline automatically builds and pushes images to **GitHub Container Registry** on every merge to `main`.

### Infrastructure (docker-compose)

`docker-compose.yml` brings up the full local stack:

| Service | Port |
|---------|------|
| PostgreSQL 17 | 5432 |
| Redis 7 | 6379 |
| Elasticsearch 8 | 9200 |
| MinIO (S3-compatible) | 9000 |
| Mailhog | 8025 |
| Prometheus | 9090 |
| Jaeger | 16686 |

### API

| Endpoint | Description |
|----------|-------------|
| `GET /actuator/health` | Health check |
| `GET /actuator/prometheus` | Prometheus metrics |
| `GET /api/v1/products` | Product catalog |
| `GET /api/v1/search/products/faceted` | Full-text search with facets |

> **Idempotency**: `Idempotency-Key` header is required on all write endpoints under `/api/v1/payments/**`, `/api/v1/checkout/**`, and `/api/v1/orders/**`.

> **Internationalization**: `Accept-Language` header drives locale resolution. Bundled translations: `en`, `es`.

## CI/CD

| Workflow | Trigger | Description |
|----------|---------|-------------|
| [Main Pipeline](.github/workflows/main.yml) | Push to `main` | Build, test, Docker push to GHCR, GitHub Release, Coolify deploy |
| [PR Pipeline](.github/workflows/pr.yml) | Pull requests | Spotless, Checkstyle, Trivy scan, tests + JaCoCo coverage report |
| [Security Scan](.github/workflows/security.yml) | Push/PR to `main` + weekly | CodeQL analysis |

## Related

- [**xplaza-ui**](https://github.com/x-plaza/xplaza-ui) — Next.js frontend monorepo (storefront, vendor, admin)

## License

Proprietary. All rights reserved.
