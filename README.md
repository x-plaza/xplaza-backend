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

## Environment Variables

**Required** (no defaults):

| Variable | Description |
|----------|-------------|
| `DB_URL` | JDBC connection string, e.g. `jdbc:postgresql://host:5432/xplaza` |
| `DB_USERNAME` | Database user |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret for signing JWTs |
| `MINIO_ACCESS_KEY` | S3/MinIO access key |
| `MINIO_SECRET_KEY` | S3/MinIO secret key |

**Optional** (have sensible defaults):

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `10001` | Server port |
| `ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:5173` | CORS origins |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `REDIS_PASSWORD` | _(empty)_ | Redis password |
| `ELASTICSEARCH_URIS` | `http://localhost:9200` | Elasticsearch URL |
| `ELASTICSEARCH_USERNAME` | _(empty)_ | ES username |
| `ELASTICSEARCH_PASSWORD` | _(empty)_ | ES password |
| `MINIO_URL` | `http://localhost:9000` | MinIO/S3 endpoint |
| `MINIO_BUCKET` | `xplaza-products` | S3 bucket name |
| `MAIL_HOST` | `smtp.gmail.com` | SMTP host |
| `MAIL_PORT` | `587` | SMTP port |
| `EMAIL_USERNAME` | _(empty)_ | SMTP user |
| `EMAIL_PASSWORD` | _(empty)_ | SMTP password |
| `STRIPE_API_KEY` | _(empty)_ | Stripe secret key |
| `STRIPE_WEBHOOK_SECRET` | _(empty)_ | Stripe webhook signing secret |
| `GOOGLE_CLIENT_ID` | _(empty)_ | Google OAuth client ID |
| `GOOGLE_CLIENT_SECRET` | _(empty)_ | Google OAuth client secret |
| `FACEBOOK_CLIENT_ID` | _(empty)_ | Facebook OAuth client ID |
| `FACEBOOK_CLIENT_SECRET` | _(empty)_ | Facebook OAuth client secret |
| `TELESIGN_CUSTOMER_ID` | _(empty)_ | Telesign SMS customer ID |
| `TELESIGN_API_KEY` | _(empty)_ | Telesign SMS API key |
| `TELESIGN_ENABLED` | `false` | Enable SMS notifications |
| `JWT_EXPIRATION_MS` | `900000` | Access token TTL (15 min) |
| `JWT_REFRESH_EXPIRATION_MS` | `604800000` | Refresh token TTL (7 days) |
| `LOG_LEVEL` | `INFO` | App log level |
| `OTLP_ENDPOINT` | `http://localhost:4318/v1/traces` | OpenTelemetry collector |
| `TRACING_SAMPLE_RATE` | `0.1` | Trace sampling rate |

## CI/CD

Push to `main` → build → Docker push to GHCR → GitHub Release → Coolify deploy.
PRs get Spotless + Checkstyle + Trivy + tests + JaCoCo coverage. Weekly CodeQL scan.

## Related

- [xplaza-ui](https://github.com/x-plaza/xplaza-ui) — Frontend (Next.js)
