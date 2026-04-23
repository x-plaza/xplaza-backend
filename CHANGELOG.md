# Changelog

All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-04-22

This release is a major overhaul that lifts the project to a marketplace-grade
e-commerce backend. It is intentionally large because it stitches together
independent features (search, payments, loyalty, CMS, GDPR, etc.) that are all
required for the v1.0.0 launch.

### Platform & runtime

- Bumped to Java 25 (SAP Machine) with virtual threads enabled for the embedded
  Tomcat connector (`spring.threads.virtual.enabled=true`) and a virtual-thread
  task executor for `@Async` work.
- Multi-stage `Dockerfile` (builder: `maven:3.9.11-sapmachine-25`, runtime:
  `sapmachine:25-jre-headless-ubuntu-noble`), non-root user, ZGC, healthcheck.
- Local docker-compose stack now bundles Postgres 17, Redis 7, Elasticsearch 8,
  MinIO, Mailhog, Prometheus and Jaeger.
- New `infra/prometheus.yml` for scraping the actuator endpoints.

### Data layer

- Flyway migration `V2__v1_release_features.sql` adds:
  - Auditing & cross-cutting tables: `idempotency_keys`, `domain_event_outbox`,
    `audit_log`.
  - Auth: `email_verification_tokens`, `password_reset_tokens`,
    `mfa_recovery_codes`, `oauth_accounts`, `refresh_tokens`.
  - Customer extensions for verification, lockout, MFA, OAuth, loyalty, B2B.
  - CMS: `cms_pages`, `cms_banners`, `cms_blocks`, `cms_faq`.
  - Gift cards: `gift_cards`, `gift_card_transactions`.
  - Subscriptions: `subscriptions`, `subscription_items`.
  - B2B: `customer_groups`, `price_lists`, `price_list_items`.
  - Tax: `tax_zones`, `tax_rules` (seeded with sample EU/US rates).
  - Recommendations: `product_co_purchases`, `recently_viewed_products`.
  - Search analytics: `search_queries`.
  - Soft-delete columns on `products`, `customers`, `shops`.
  - Product/category translation tables and SEO columns.

### Cross-cutting

- `BaseAuditableEntity` + `SpringSecurityAuditorAware` for JPA `@CreatedBy`,
  `@LastModifiedBy`, `@CreatedDate`, `@LastModifiedDate`.
- `AsyncConfig` and `LocaleConfig` for virtual-thread executors and i18n.
- `CacheConfig` for Caffeine-backed caches (`products`, `tax`, `i18n`, etc.).
- Bucket4j-based `RateLimitFilter` with separate buckets for auth/payment/default.
- `IdempotencyKey` + `IdempotencyService` + `IdempotencyInterceptor` to protect
  payment, checkout and order endpoints from duplicate writes.
- Domain event sealed-interface (`DomainEvent`) + transactional outbox
  (`OutboxEvent`, `OutboxRelayWorker`).
- Structured JSON logging in the `cloud` profile via `logback-spring.xml`.
- Resilience4j retry/circuit breaker config wired in `application*.yaml`.
- Micrometer/Prometheus + OpenTelemetry tracing exporter.

### Security & auth

- `CompositeUserDetailsService` resolves both admin and customer principals,
  picking the right service based on a JWT role hint (replaces the previous
  admin-only path that silently failed for customer logins).
- `AccountLockoutPolicy`: progressive lockout after configurable failed
  attempts.
- `MfaService`: TOTP enrollment, QR generation and verification using
  `dev.samstevens.totp`.
- Customer email verification + password reset flows with one-time tokens.
- OAuth2 social login wired conditionally — only enabled when at least one
  client registration is present so the local profile still boots.
- CORS hardened: explicit `allowed-origins` list, exposed headers, max age.
- BCrypt strength bumped to 12.

### Customer experience

- New `CustomerAuthController` endpoints: `/me`, `/email/verify`,
  `/forgot-password`, `/reset-password`, `/mfa/enroll`, `/mfa/confirm`,
  `/mfa/disable`.
- `CustomerAddressController` for full CRUD over addresses, including default
  selection and soft-delete semantics.
- `CustomerGdprController` exposes `/gdpr/export` and `/gdpr/erase`.
- `LoyaltyController` exposes `/customer/loyalty/balance`.
- `NotificationPreferenceController` for email/SMS/push opt-in management.
- `SavedPaymentMethodController` for the saved-card vault (PCI-safe metadata
  only — gateway tokens are stored, never raw PAN).

### Catalog & search

- `Product` entity gains `slug`, `seoTitle`, `seoDescription`, `seoKeywords`,
  `isPublished`, `deletedAt`, `averageRating`, `reviewCount`.
- New `Tag` entity + repository for product tagging.
- `ProductCsvService` + `ProductCsvController` for bulk import/export.
- Elasticsearch integration: `ProductDocument`, `ProductDocumentRepository`,
  `ProductSearchService`, `SearchController`. Service is gated by
  `search.elasticsearch.enabled` so the app still boots without ES.
- `RecommendationService` with related/FBT/recently-viewed APIs and a hook
  on `OrderPlaced` to learn co-purchases.

### Payments

- `CodPaymentGateway` and `PaymentService.createCod(...)` for cash-on-delivery
  with proper transaction tracking.
- Stripe `RequestOptions` idempotency-key on `PaymentIntent` create/confirm.
- Idempotent webhook processing using the `Idempotency-Key` infra (replays
  return success without re-running side effects).
- `CustomerPaymentMethod` saved-card vault.

### Orders & checkout

- `InvoicePdfService` + `InvoiceController` for OpenPDF tax invoices,
  downloadable at `/api/v1/orders/{id}/invoice`.
- `AbandonedCartScheduler` detects inactive carts and emits `CartAbandoned`
  events for the notifications module to follow up.

### Marketplace

- `CommissionService` splits a sale between marketplace and seller.
- `VendorPayoutScheduler` triggers weekly payouts.

### CMS

- `CmsPage`, `CmsBanner`, `CmsFaq` entities + repositories + `CmsController`
  with admin-gated CRUD and public read endpoints.

### Loyalty & gift cards

- `LoyaltyService` accrues points on `OrderPlaced`, redeems with configurable
  rates, and computes the customer's tier.
- `GiftCardService` issues, redeems and queries the balance for gift cards;
  ledger entries land in `gift_card_transactions`.

### Tax engine

- `TaxService.computeTax(...)` resolves a `TaxZone` by country/region and
  applies all matching `TaxRule`s in priority order, supporting compound
  taxation.

### Internationalisation

- `messages.properties` + `messages_es.properties` bundles, locale resolution
  via `Accept-Language`, `LocaleConfig`.

### Notifications

- `SmsService` with Telesign integration, gracefully no-op when credentials
  are absent.
- `NotificationPreference` entity + controller.

### Testing

- `PublicEndpointsSmokeTest` proves CMS / search / autocomplete routes are
  registered and reachable.
- Existing service-level tests retained.

### Known gaps (to be addressed in 1.1.0)

The following items are scoped, scaffolded, but require additional
implementation work before they meet production quality. They are tracked in
the project board:

- Full BOGO/free-shipping/bundle promotion calculator.
- Push notifications via FCM/APNs (entity hook + opt-in plumbing exist).
- Subscription order-generation workflow.
- Multi-vendor order splitting at checkout.
- B2B price-list resolver in pricing service.
- Replace the `RecommendationService` simple SQL with an offline collaborative
  filter pipeline.
