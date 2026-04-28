# X-Plaza Backend

[![CI](https://github.com/x-plaza/xplaza-backend/actions/workflows/main.yml/badge.svg)](https://github.com/x-plaza/xplaza-backend/actions/workflows/main.yml)

Multi-vendor marketplace backend. Java 25, Spring Boot 4.0.5, PostgreSQL 17, Elasticsearch 8, Redis 7.

## Quick Start

```bash
git clone https://github.com/x-plaza/xplaza-backend.git && cd xplaza-backend

# Local dev (H2 in-memory, no external deps needed)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# With full stack (Postgres, Redis, ES, MinIO, Mailhog)
docker compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=cloud
```

API at http://localhost:8080.

## Project Structure

Modular monolith organized by business domain:

```
src/main/java/com/xplaza/backend/
├── catalog/        # Products, brands, categories, tags, CSV import/export
├── order/          # Checkout, invoices, abandoned-cart recovery
├── payment/        # Stripe + COD, idempotent webhooks, refunds
├── auth/           # JWT, MFA (TOTP), OAuth2, email verification
├── customer/       # Profiles, addresses, GDPR export/erase
├── cart/           # Shopping sessions, abandoned-cart detection
├── search/         # Elasticsearch search + autocomplete
├── recommendation/ # Related & frequently-bought-together
├── tax/            # Region-aware tax zones
├── giftcard/       # Stored-value programs
├── loyalty/        # Points rewards
├── vendor/         # Commission & payouts
├── cms/            # Pages, banners, FAQ
├── notification/   # Email + SMS
└── common/         # Idempotency, rate limiting, events, audit, i18n
```

## Commands

```bash
./mvnw test                  # Run tests
./mvnw spotless:apply        # Fix formatting
./mvnw checkstyle:check      # Check style
docker build -t xplaza-backend .  # Build Docker image
```

## Profiles

| Profile | Database | Migrations | Usage |
|---------|----------|------------|-------|
| `local` | H2 | Off | Dev |
| `cloud` | PostgreSQL | Flyway | Prod |

## CI/CD

Push to `main` → build → Docker push to GHCR → GitHub Release → Coolify deploy.
PRs get Spotless + Checkstyle + Trivy + tests + JaCoCo coverage. Weekly CodeQL scan.

## Related

- [xplaza-ui](https://github.com/x-plaza/xplaza-ui) — Frontend (Next.js)
