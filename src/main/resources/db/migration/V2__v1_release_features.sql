-- =====================================================
-- X-Plaza v1.0.0 release features
-- Adds: idempotency, transactional outbox, audit log,
-- auth flows (verify/reset/MFA/OAuth), CMS, gift cards,
-- subscriptions, B2B groups + price lists, tax engine,
-- product translations, recommendations, full-text search.
-- =====================================================

-- ---------- Customers/Admins extras ----------
ALTER TABLE customers ADD COLUMN IF NOT EXISTS oauth_provider VARCHAR(30);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS oauth_subject VARCHAR(200);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS mfa_secret VARCHAR(200);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS store_credit DECIMAL(14,2) DEFAULT 0;
ALTER TABLE customers ADD COLUMN IF NOT EXISTS customer_group_id BIGINT;
ALTER TABLE customers ADD COLUMN IF NOT EXISTS tax_id VARCHAR(50);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS verified_email_at TIMESTAMP;

ALTER TABLE admin_users ADD COLUMN IF NOT EXISTS failed_login_attempts INTEGER DEFAULT 0;
ALTER TABLE admin_users ADD COLUMN IF NOT EXISTS locked_until TIMESTAMP;
ALTER TABLE admin_users ADD COLUMN IF NOT EXISTS mfa_enabled BOOLEAN DEFAULT FALSE;
ALTER TABLE admin_users ADD COLUMN IF NOT EXISTS mfa_secret VARCHAR(200);

-- ---------- Idempotency ----------
CREATE TABLE IF NOT EXISTS idempotency_keys (
    key_value VARCHAR(200) PRIMARY KEY,
    endpoint VARCHAR(200) NOT NULL,
    request_hash VARCHAR(128),
    response_status INTEGER,
    response_body TEXT,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_idem_expires ON idempotency_keys(expires_at);

-- ---------- Domain event outbox ----------
CREATE TABLE IF NOT EXISTS domain_event_outbox (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(36) NOT NULL UNIQUE,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    published_at TIMESTAMP,
    retries INTEGER NOT NULL DEFAULT 0,
    last_error VARCHAR(1000)
);
CREATE INDEX IF NOT EXISTS idx_outbox_unpublished ON domain_event_outbox(published_at);
CREATE INDEX IF NOT EXISTS idx_outbox_event_type ON domain_event_outbox(event_type);

-- ---------- Generic audit log ----------
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(20) NOT NULL,
    actor VARCHAR(100),
    diff TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_audit_entity ON audit_log(entity_type, entity_id);

-- ---------- Auth: email verification & password reset ----------
CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    token VARCHAR(120) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    consumed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    token VARCHAR(120) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    consumed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mfa_recovery_codes (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    code_hash VARCHAR(120) NOT NULL,
    used_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS oauth_accounts (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    provider VARCHAR(30) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(provider, subject)
);

-- ---------- Refresh token revocation ----------
CREATE TABLE IF NOT EXISTS refresh_tokens (
    token_hash VARCHAR(128) PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_refresh_expires ON refresh_tokens(expires_at);

-- ---------- CMS ----------
CREATE TABLE IF NOT EXISTS cms_pages (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(150) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    body TEXT,
    seo_title VARCHAR(255),
    seo_description VARCHAR(500),
    locale VARCHAR(10) DEFAULT 'en',
    published BOOLEAN DEFAULT FALSE,
    published_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS cms_banners (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    link_url VARCHAR(500),
    placement VARCHAR(50) NOT NULL,
    sort_order INTEGER DEFAULT 0,
    starts_at TIMESTAMP,
    ends_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    locale VARCHAR(10) DEFAULT 'en',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cms_blocks (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    title VARCHAR(255),
    body TEXT,
    locale VARCHAR(10) DEFAULT 'en',
    is_active BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cms_faq (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(100),
    question VARCHAR(500) NOT NULL,
    answer TEXT NOT NULL,
    sort_order INTEGER DEFAULT 0,
    locale VARCHAR(10) DEFAULT 'en',
    is_active BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------- Gift cards ----------
CREATE TABLE IF NOT EXISTS gift_cards (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(40) NOT NULL UNIQUE,
    initial_amount DECIMAL(14,2) NOT NULL,
    balance DECIMAL(14,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    purchaser_customer_id BIGINT REFERENCES customers(customer_id),
    recipient_email VARCHAR(255),
    recipient_name VARCHAR(255),
    message TEXT,
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS gift_card_transactions (
    id BIGSERIAL PRIMARY KEY,
    gift_card_id BIGINT NOT NULL REFERENCES gift_cards(id) ON DELETE CASCADE,
    order_id UUID,
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(14,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------- Subscriptions ----------
CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    interval_unit VARCHAR(20) NOT NULL,
    interval_count INTEGER NOT NULL DEFAULT 1,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    total_amount DECIMAL(14,2),
    next_renewal_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subscription_items (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL REFERENCES subscriptions(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(product_id),
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(14,2) NOT NULL
);

-- ---------- B2B: customer groups & price lists ----------
CREATE TABLE IF NOT EXISTS customer_groups (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    discount_percent DECIMAL(5,2) DEFAULT 0,
    tax_exempt BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS price_lists (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    customer_group_id BIGINT REFERENCES customer_groups(id),
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    starts_at TIMESTAMP,
    ends_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS price_list_items (
    id BIGSERIAL PRIMARY KEY,
    price_list_id BIGINT NOT NULL REFERENCES price_lists(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    min_quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(14,2) NOT NULL,
    UNIQUE(price_list_id, product_id, min_quantity)
);

-- ---------- Tax engine ----------
CREATE TABLE IF NOT EXISTS tax_zones (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    state VARCHAR(100),
    postal_code_prefix VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS tax_rules (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    tax_zone_id BIGINT REFERENCES tax_zones(id) ON DELETE CASCADE,
    rate DECIMAL(7,4) NOT NULL,
    inclusive BOOLEAN DEFAULT FALSE,
    category_filter VARCHAR(100),
    priority INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE
);

INSERT INTO tax_zones (code, name, country_code) VALUES
    ('US-DEFAULT', 'United States Default', 'US'),
    ('EU-DE',      'Germany',                'DE'),
    ('EU-FR',      'France',                 'FR'),
    ('UK',         'United Kingdom',         'GB')
ON CONFLICT (code) DO NOTHING;

INSERT INTO tax_rules (name, tax_zone_id, rate, inclusive)
SELECT 'US Sales Tax', id, 0.0875, FALSE FROM tax_zones WHERE code = 'US-DEFAULT'
ON CONFLICT DO NOTHING;

INSERT INTO tax_rules (name, tax_zone_id, rate, inclusive)
SELECT 'DE VAT', id, 0.19, TRUE FROM tax_zones WHERE code = 'EU-DE'
ON CONFLICT DO NOTHING;

INSERT INTO tax_rules (name, tax_zone_id, rate, inclusive)
SELECT 'FR VAT', id, 0.20, TRUE FROM tax_zones WHERE code = 'EU-FR'
ON CONFLICT DO NOTHING;

INSERT INTO tax_rules (name, tax_zone_id, rate, inclusive)
SELECT 'UK VAT', id, 0.20, TRUE FROM tax_zones WHERE code = 'UK'
ON CONFLICT DO NOTHING;

-- ---------- Product translations ----------
CREATE TABLE IF NOT EXISTS product_translations (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    locale VARCHAR(10) NOT NULL,
    name VARCHAR(255),
    description TEXT,
    seo_title VARCHAR(255),
    seo_description VARCHAR(500),
    UNIQUE(product_id, locale)
);

CREATE TABLE IF NOT EXISTS category_translations (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES categories(category_id) ON DELETE CASCADE,
    locale VARCHAR(10) NOT NULL,
    name VARCHAR(255),
    description VARCHAR(500),
    UNIQUE(category_id, locale)
);

-- Product SEO columns (V1 doesn't have them)
ALTER TABLE products ADD COLUMN IF NOT EXISTS slug VARCHAR(255);
ALTER TABLE products ADD COLUMN IF NOT EXISTS seo_title VARCHAR(255);
ALTER TABLE products ADD COLUMN IF NOT EXISTS seo_description VARCHAR(500);
CREATE INDEX IF NOT EXISTS idx_products_slug ON products(slug);

-- ---------- Recommendations ----------
CREATE TABLE IF NOT EXISTS product_co_purchases (
    product_id BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    co_product_id BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    score DOUBLE PRECISION NOT NULL DEFAULT 0,
    last_computed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id, co_product_id)
);
CREATE INDEX IF NOT EXISTS idx_copurchases_score ON product_co_purchases(product_id, score DESC);

CREATE TABLE IF NOT EXISTS recently_viewed_products (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    session_id VARCHAR(100),
    product_id BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    viewed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_recent_views ON recently_viewed_products(customer_id, viewed_at DESC);
CREATE INDEX IF NOT EXISTS idx_recent_views_session ON recently_viewed_products(session_id, viewed_at DESC);

-- ---------- Search analytics ----------
CREATE TABLE IF NOT EXISTS search_queries (
    id BIGSERIAL PRIMARY KEY,
    query VARCHAR(500) NOT NULL,
    customer_id BIGINT REFERENCES customers(customer_id),
    results_count INTEGER NOT NULL DEFAULT 0,
    locale VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_search_query_time ON search_queries(created_at DESC);

-- ---------- Soft-delete columns on long-lived entities ----------
ALTER TABLE products  ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE customers ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE shops     ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- ---------- Indices for common reads ----------
CREATE INDEX IF NOT EXISTS idx_products_shop_active   ON products(fk_shop_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer_status ON customer_orders(customer_id, status);
CREATE INDEX IF NOT EXISTS idx_orders_shop_status     ON customer_orders(shop_id, status);
CREATE INDEX IF NOT EXISTS idx_carts_customer         ON carts(customer_id);
CREATE INDEX IF NOT EXISTS idx_carts_session          ON carts(session_id);
CREATE INDEX IF NOT EXISTS idx_carts_status_updated   ON carts(status, updated_at);
