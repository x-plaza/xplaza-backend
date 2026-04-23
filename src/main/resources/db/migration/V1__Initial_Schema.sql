-- =====================================================
-- X-Plaza Enterprise E-Commerce Schema
-- Version: 1.0.0 (consolidated)
--
-- This is a single, clean-slate baseline that replaces the previous
-- V1/V2/V3 chain. The chain accumulated drift between Hibernate
-- `ddl-auto=update` (pre-Flyway era) and the intended canonical schema
-- (the `shopping_carts` -> `carts` rename being the most painful one).
-- We have no data to preserve, so the history is consolidated here.
--
-- Runs idempotently (IF NOT EXISTS everywhere) against a completely
-- empty PostgreSQL 17 `public` schema.
-- =====================================================

-- =====================================================
-- EXTENSIONS / SEQUENCES
-- =====================================================

CREATE SEQUENCE IF NOT EXISTS order_number_seq START WITH 1 INCREMENT BY 1;

-- =====================================================
-- CATALOG CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS categories (
    category_id           BIGSERIAL PRIMARY KEY,
    category_name         VARCHAR(255),
    category_description  VARCHAR(255),
    parent_category       BIGINT REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS brands (
    brand_id           BIGSERIAL PRIMARY KEY,
    brand_name         VARCHAR(255),
    brand_description  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS shops (
    shop_id              BIGSERIAL PRIMARY KEY,
    shop_name            VARCHAR(255),
    shop_description    VARCHAR(255),
    shop_address         VARCHAR(255),
    shop_owner           VARCHAR(255),
    fk_location_id       BIGINT,
    slug                 VARCHAR(100),
    status               VARCHAR(20) DEFAULT 'PENDING',
    legal_name           VARCHAR(255),
    business_type        VARCHAR(30),
    tax_id               VARCHAR(50),
    registration_number  VARCHAR(100),
    established_year     INTEGER,
    logo                 VARCHAR(500),
    banner               VARCHAR(500),
    email                VARCHAR(255),
    phone                VARCHAR(20),
    website              VARCHAR(500),
    support_email        VARCHAR(255),
    return_window_days   INTEGER DEFAULT 30,
    shipping_policy      TEXT,
    return_policy        TEXT,
    verified_at          TIMESTAMP,
    metadata             TEXT,
    commission_rate      DECIMAL(5,4),
    payout_account       VARCHAR(255),
    payout_currency      VARCHAR(3),
    deleted_at           TIMESTAMP
);

CREATE TABLE IF NOT EXISTS currencies (
    currency_id     BIGSERIAL PRIMARY KEY,
    currency_name   VARCHAR(255),
    currency_sign   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS product_variation_types (
    product_var_type_id    BIGSERIAL PRIMARY KEY,
    var_type_name          VARCHAR(255),
    var_type_description   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS products (
    product_id                BIGSERIAL PRIMARY KEY,
    product_name              VARCHAR(255),
    product_description       VARCHAR(255),
    product_buying_price      DOUBLE PRECISION,
    product_selling_price     DOUBLE PRECISION,
    quantity                  INTEGER,
    product_var_type_value    INTEGER,
    created_at                TIMESTAMP,
    created_by                INTEGER,
    last_updated_at           TIMESTAMP,
    last_updated_by           INTEGER,
    fk_brand_id               BIGINT REFERENCES brands(brand_id),
    fk_category_id            BIGINT REFERENCES categories(category_id),
    fk_currency_id            BIGINT REFERENCES currencies(currency_id),
    fk_product_var_type_id    BIGINT REFERENCES product_variation_types(product_var_type_id),
    fk_shop_id                BIGINT REFERENCES shops(shop_id),
    slug                      VARCHAR(255),
    short_description         TEXT,
    status                    VARCHAR(20) DEFAULT 'DRAFT',
    visibility                VARCHAR(20) DEFAULT 'VISIBLE',
    seo_title                 VARCHAR(255),
    seo_description           TEXT,
    seo_keywords              TEXT,
    metadata                  TEXT,
    published_at              TIMESTAMP,
    deleted_at                TIMESTAMP
);

CREATE TABLE IF NOT EXISTS attributes (
    attribute_id          BIGSERIAL PRIMARY KEY,
    name                  VARCHAR(100) NOT NULL,
    code                  VARCHAR(50)  NOT NULL UNIQUE,
    type                  VARCHAR(20)  NOT NULL DEFAULT 'SELECT',
    is_variant_attribute  BOOLEAN DEFAULT FALSE,
    is_filterable         BOOLEAN DEFAULT TRUE,
    is_searchable         BOOLEAN DEFAULT TRUE,
    position              INTEGER DEFAULT 0,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS attribute_values (
    value_id       BIGSERIAL PRIMARY KEY,
    attribute_id   BIGINT NOT NULL REFERENCES attributes(attribute_id) ON DELETE CASCADE,
    display_value  VARCHAR(255) NOT NULL,
    code           VARCHAR(100) NOT NULL,
    metadata       TEXT,
    position       INTEGER DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(attribute_id, code)
);

CREATE TABLE IF NOT EXISTS category_attributes (
    category_id   BIGINT NOT NULL REFERENCES categories(category_id) ON DELETE CASCADE,
    attribute_id  BIGINT NOT NULL REFERENCES attributes(attribute_id) ON DELETE CASCADE,
    is_required   BOOLEAN DEFAULT FALSE,
    position      INTEGER DEFAULT 0,
    PRIMARY KEY (category_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS product_variants (
    variant_id        UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_id        BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    sku               VARCHAR(100) NOT NULL UNIQUE,
    name              VARCHAR(255),
    price             DECIMAL(15, 2) NOT NULL,
    compare_at_price  DECIMAL(15, 2),
    cost_price        DECIMAL(15, 2),
    currency_id       BIGINT REFERENCES currencies(currency_id),
    barcode           VARCHAR(50),
    weight_grams      DECIMAL(10, 2),
    length_cm         DECIMAL(10, 2),
    width_cm          DECIMAL(10, 2),
    height_cm         DECIMAL(10, 2),
    is_default        BOOLEAN DEFAULT FALSE,
    position          INTEGER DEFAULT 0,
    status            VARCHAR(20) DEFAULT 'ACTIVE',
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS variant_attributes (
    variant_id    UUID   NOT NULL REFERENCES product_variants(variant_id) ON DELETE CASCADE,
    attribute_id  BIGINT NOT NULL REFERENCES attributes(attribute_id),
    value_id      BIGINT NOT NULL REFERENCES attribute_values(value_id),
    PRIMARY KEY (variant_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS variant_images (
    image_id    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    variant_id  UUID NOT NULL REFERENCES product_variants(variant_id) ON DELETE CASCADE,
    url         VARCHAR(500) NOT NULL,
    alt_text    VARCHAR(255),
    position    INTEGER DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_attributes (
    product_id    BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    attribute_id  BIGINT NOT NULL REFERENCES attributes(attribute_id),
    value_id      BIGINT REFERENCES attribute_values(value_id),
    text_value    VARCHAR(500),
    PRIMARY KEY (product_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS product_tags (
    product_id  BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    tag         VARCHAR(100) NOT NULL,
    PRIMARY KEY (product_id, tag)
);

CREATE TABLE IF NOT EXISTS product_images (
    product_images_id    BIGSERIAL PRIMARY KEY,
    fk_product_id        BIGINT REFERENCES products(product_id) ON DELETE CASCADE,
    product_image_name   VARCHAR(255),
    product_image_path   VARCHAR(500),
    thumbnail_url        VARCHAR(500),
    medium_url           VARCHAR(500),
    large_url            VARCHAR(500),
    alt_text             VARCHAR(255),
    sort_order           INTEGER DEFAULT 0,
    created_by           INTEGER,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated_by      INTEGER,
    last_updated_at      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_translations (
    id               BIGSERIAL PRIMARY KEY,
    product_id       BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    locale           VARCHAR(10) NOT NULL,
    name             VARCHAR(255),
    description      TEXT,
    seo_title        VARCHAR(255),
    seo_description  VARCHAR(500),
    UNIQUE(product_id, locale)
);

CREATE TABLE IF NOT EXISTS category_translations (
    id           BIGSERIAL PRIMARY KEY,
    category_id  BIGINT NOT NULL REFERENCES categories(category_id) ON DELETE CASCADE,
    locale       VARCHAR(10) NOT NULL,
    name         VARCHAR(255),
    description  VARCHAR(500),
    UNIQUE(category_id, locale)
);

-- =====================================================
-- CUSTOMER / IDENTITY CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS admin_users (
    id                      BIGSERIAL PRIMARY KEY,
    username                VARCHAR(255) NOT NULL UNIQUE,
    email                   VARCHAR(255) NOT NULL UNIQUE,
    password                VARCHAR(255) NOT NULL,
    role                    VARCHAR(255) NOT NULL,
    enabled                 BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP,
    last_login_at           TIMESTAMP,
    failed_login_attempts   INTEGER DEFAULT 0,
    locked_until            TIMESTAMP,
    mfa_enabled             BOOLEAN DEFAULT FALSE,
    mfa_secret              VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS customers (
    customer_id              BIGSERIAL PRIMARY KEY,
    first_name               VARCHAR(255) NOT NULL,
    last_name                VARCHAR(255) NOT NULL,
    email                    VARCHAR(255) NOT NULL UNIQUE,
    password                 VARCHAR(255) NOT NULL,
    phone_number             VARCHAR(255),
    role                     VARCHAR(255) NOT NULL DEFAULT 'CUSTOMER',
    enabled                  BOOLEAN NOT NULL DEFAULT TRUE,
    created_at               TIMESTAMP,
    last_login_at            TIMESTAMP,
    status                   VARCHAR(20) DEFAULT 'ACTIVE',
    display_name             VARCHAR(255),
    avatar                   VARCHAR(500),
    gender                   VARCHAR(20),
    locale                   VARCHAR(10) DEFAULT 'en-US',
    verified_email           BOOLEAN DEFAULT FALSE,
    verified_email_at        TIMESTAMP,
    verified_phone           BOOLEAN DEFAULT FALSE,
    mfa_enabled              BOOLEAN DEFAULT FALSE,
    mfa_secret               VARCHAR(200),
    oauth_provider           VARCHAR(30),
    oauth_subject            VARCHAR(200),
    failed_login_attempts    INTEGER DEFAULT 0,
    locked_until             TIMESTAMP,
    last_order_at            TIMESTAMP,
    marketing_email          BOOLEAN DEFAULT TRUE,
    marketing_sms            BOOLEAN DEFAULT FALSE,
    loyalty_points           BIGINT NOT NULL DEFAULT 0,
    loyalty_tier             VARCHAR(20) DEFAULT 'BRONZE',
    lifetime_spend           DECIMAL(15, 2) DEFAULT 0,
    store_credit             DECIMAL(14, 2) DEFAULT 0,
    customer_group_id        BIGINT,
    tax_id                   VARCHAR(50),
    deleted_at               TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_addresses (
    address_id       BIGSERIAL PRIMARY KEY,
    customer_id      BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    type             VARCHAR(20) DEFAULT 'BOTH',
    is_default       BOOLEAN DEFAULT FALSE,
    label            VARCHAR(50),
    first_name       VARCHAR(100) NOT NULL,
    last_name        VARCHAR(100) NOT NULL,
    company          VARCHAR(255),
    address_line1    VARCHAR(255) NOT NULL,
    address_line2    VARCHAR(255),
    city             VARCHAR(100) NOT NULL,
    state            VARCHAR(100),
    postal_code      VARCHAR(20)  NOT NULL,
    country_code     VARCHAR(2)   NOT NULL,
    phone            VARCHAR(20),
    instructions     TEXT,
    latitude         DECIMAL(10, 8),
    longitude        DECIMAL(11, 8),
    is_verified      BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_payment_methods (
    payment_method_id    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id          BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    type                 VARCHAR(20) NOT NULL,
    is_default           BOOLEAN DEFAULT FALSE,
    nickname             VARCHAR(50),
    last4                VARCHAR(4),
    brand                VARCHAR(20),
    expiry_month         INTEGER,
    expiry_year          INTEGER,
    cardholder_name      VARCHAR(255),
    gateway              VARCHAR(50),
    gateway_token        VARCHAR(255),
    billing_address_id   BIGINT REFERENCES customer_addresses(address_id),
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wishlists (
    wishlist_id    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id    BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    name           VARCHAR(100) DEFAULT 'My Wishlist',
    visibility     VARCHAR(20) DEFAULT 'PRIVATE',
    share_token    VARCHAR(64),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wishlist_items (
    wishlist_item_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    wishlist_id            UUID   NOT NULL REFERENCES wishlists(wishlist_id) ON DELETE CASCADE,
    product_id             BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    variant_id             UUID   REFERENCES product_variants(variant_id) ON DELETE SET NULL,
    note                   TEXT,
    price_at_add           DECIMAL(15, 2),
    notify_price_drop      BOOLEAN DEFAULT FALSE,
    notify_back_in_stock   BOOLEAN DEFAULT FALSE,
    added_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(wishlist_id, product_id, variant_id)
);

-- =====================================================
-- AUTH: verification, reset, MFA, OAuth, refresh tokens
-- =====================================================

CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id             BIGSERIAL PRIMARY KEY,
    customer_id    BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id  BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    token          VARCHAR(120) NOT NULL UNIQUE,
    expires_at     TIMESTAMP NOT NULL,
    consumed_at    TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id             BIGSERIAL PRIMARY KEY,
    customer_id    BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id  BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    token          VARCHAR(120) NOT NULL UNIQUE,
    expires_at     TIMESTAMP NOT NULL,
    consumed_at    TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mfa_recovery_codes (
    id             BIGSERIAL PRIMARY KEY,
    customer_id    BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id  BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    code_hash      VARCHAR(120) NOT NULL,
    used_at        TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS oauth_accounts (
    id           BIGSERIAL PRIMARY KEY,
    customer_id  BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    provider     VARCHAR(30) NOT NULL,
    subject      VARCHAR(200) NOT NULL,
    email        VARCHAR(255),
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(provider, subject)
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    token_hash     VARCHAR(128) PRIMARY KEY,
    customer_id    BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    admin_user_id  BIGINT REFERENCES admin_users(id) ON DELETE CASCADE,
    expires_at     TIMESTAMP NOT NULL,
    revoked_at     TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PROMOTION CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS discount_types (
    discount_type_id      BIGSERIAL PRIMARY KEY,
    discount_type_name    VARCHAR(255) NOT NULL,
    description           VARCHAR(255),
    created_by            INTEGER,
    created_at            TIMESTAMP,
    last_updated_by       INTEGER,
    last_updated_at       TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coupons (
    coupon_id                  BIGSERIAL PRIMARY KEY,
    coupon_code                VARCHAR(255) NOT NULL UNIQUE,
    coupon_description         VARCHAR(255),
    fk_discount_type_id        BIGINT REFERENCES discount_types(discount_type_id),
    discount_value             DOUBLE PRECISION,
    minimum_order_amount       DOUBLE PRECISION,
    maximum_discount_amount    DOUBLE PRECISION,
    start_date                 TIMESTAMP,
    end_date                   TIMESTAMP,
    usage_limit                INTEGER DEFAULT 0,
    used_count                 INTEGER DEFAULT 0,
    is_active                  BOOLEAN DEFAULT TRUE,
    created_by                 INTEGER,
    created_at                 TIMESTAMP,
    last_updated_by            INTEGER,
    last_updated_at            TIMESTAMP
);

CREATE TABLE IF NOT EXISTS campaigns (
    campaign_id                UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name                       VARCHAR(255) NOT NULL,
    description                TEXT,
    type                       VARCHAR(30) NOT NULL,
    status                     VARCHAR(20) DEFAULT 'DRAFT',
    start_date                 TIMESTAMP NOT NULL,
    end_date                   TIMESTAMP NOT NULL,
    timezone                   VARCHAR(50) DEFAULT 'UTC',
    customer_segments          VARCHAR ARRAY,
    min_order_amount           DECIMAL(15, 2),
    max_uses_total             INTEGER,
    max_uses_per_customer      INTEGER DEFAULT 1,
    first_time_customer_only   BOOLEAN DEFAULT FALSE,
    discount_type              VARCHAR(20),
    discount_value             DECIMAL(15, 2),
    max_discount_amount        DECIMAL(15, 2),
    applies_to                 VARCHAR(20),
    target_type                VARCHAR(30),
    target_product_ids         BIGINT ARRAY,
    target_category_ids        BIGINT ARRAY,
    target_brand_ids           BIGINT ARRAY,
    target_shop_ids            BIGINT ARRAY,
    total_uses                 INTEGER DEFAULT 0,
    total_revenue              DECIMAL(15, 2) DEFAULT 0,
    total_discount_given       DECIMAL(15, 2) DEFAULT 0,
    created_by                 BIGINT REFERENCES admin_users(id),
    created_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS loyalty_transactions (
    transaction_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id      BIGINT NOT NULL REFERENCES customers(customer_id),
    type             VARCHAR(30) NOT NULL,
    points           INTEGER NOT NULL,
    balance_after    INTEGER NOT NULL,
    reference_type   VARCHAR(50),
    reference_id     VARCHAR(100),
    description      VARCHAR(255),
    expires_at       DATE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- CART CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS carts (
    cart_id              UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id          BIGINT REFERENCES customers(customer_id) ON DELETE SET NULL,
    session_id           VARCHAR(100),
    status               VARCHAR(20) DEFAULT 'ACTIVE',
    currency             VARCHAR(3) DEFAULT 'USD',
    subtotal             DECIMAL(15, 2) DEFAULT 0,
    discount_total       DECIMAL(15, 2) DEFAULT 0,
    shipping_estimate    DECIMAL(15, 2) DEFAULT 0,
    tax_estimate         DECIMAL(15, 2) DEFAULT 0,
    total_estimate       DECIMAL(15, 2) DEFAULT 0,
    item_count           INTEGER DEFAULT 0,
    converted_order_id   UUID,
    expires_at           TIMESTAMP,
    coupon_code          VARCHAR(50),
    coupon_discount      DECIMAL(10, 2) DEFAULT 0,
    notes                TEXT,
    last_activity_at     TIMESTAMP,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    cart_item_id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    cart_id              UUID   NOT NULL REFERENCES carts(cart_id) ON DELETE CASCADE,
    product_id           BIGINT NOT NULL REFERENCES products(product_id),
    variant_id           UUID   REFERENCES product_variants(variant_id),
    shop_id              BIGINT NOT NULL REFERENCES shops(shop_id),
    quantity             INTEGER NOT NULL DEFAULT 1,
    unit_price           DECIMAL(15, 2) NOT NULL,
    price_at_add         DECIMAL(15, 2),
    original_price       DECIMAL(10, 2),
    discount_percentage  DECIMAL(5, 2),
    discount_amount      DECIMAL(15, 2) DEFAULT 0,
    total_price          DECIMAL(15, 2),
    product_name         VARCHAR(255),
    variant_name         VARCHAR(255),
    sku                  VARCHAR(50),
    image_url            VARCHAR(500),
    status               VARCHAR(20) DEFAULT 'ACTIVE',
    custom_attributes    TEXT,
    saved_for_later      BOOLEAN DEFAULT FALSE,
    added_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(cart_id, variant_id)
);

CREATE TABLE IF NOT EXISTS cart_coupons (
    cart_id           UUID   NOT NULL REFERENCES carts(cart_id) ON DELETE CASCADE,
    coupon_id         BIGINT NOT NULL REFERENCES coupons(coupon_id),
    code              VARCHAR(50) NOT NULL,
    discount_amount   DECIMAL(15, 2) DEFAULT 0,
    applied_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id, coupon_id)
);

-- =====================================================
-- ORDER CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS customer_orders (
    order_id                    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id                 BIGINT NOT NULL REFERENCES customers(customer_id),
    shop_id                     BIGINT NOT NULL REFERENCES shops(shop_id),
    cart_id                     UUID,
    parent_order_id             UUID,
    status                      VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    subtotal                    DECIMAL(15, 2) NOT NULL,
    discount_amount             DECIMAL(15, 2) DEFAULT 0,
    shipping_cost               DECIMAL(15, 2) DEFAULT 0,
    tax_amount                  DECIMAL(15, 2) DEFAULT 0,
    tax_total                   DECIMAL(15, 2),
    shipping_total              DECIMAL(15, 2),
    grand_total                 DECIMAL(15, 2) NOT NULL,
    paid_amount                 DECIMAL(15, 2) DEFAULT 0,
    currency                    VARCHAR(3) DEFAULT 'USD',
    shipping_address_id         BIGINT,
    shipping_first_name         VARCHAR(100),
    shipping_last_name          VARCHAR(100),
    shipping_phone              VARCHAR(20),
    shipping_address_line1      VARCHAR(255),
    shipping_address_line2      VARCHAR(255),
    shipping_city               VARCHAR(100),
    shipping_state              VARCHAR(100),
    shipping_postal_code        VARCHAR(20),
    shipping_country_code       VARCHAR(2),
    shipping_instructions       TEXT,
    billing_address_id          BIGINT,
    billing_same_as_shipping    BOOLEAN DEFAULT TRUE,
    billing_first_name          VARCHAR(100),
    billing_last_name           VARCHAR(100),
    billing_company             VARCHAR(255),
    billing_address_line1       VARCHAR(255),
    billing_address_line2       VARCHAR(255),
    billing_city                VARCHAR(100),
    billing_state               VARCHAR(100),
    billing_postal_code         VARCHAR(20),
    billing_country             VARCHAR(2),
    billing_phone               VARCHAR(20),
    requested_delivery_date     DATE,
    delivery_slot_start         TIME,
    delivery_slot_end           TIME,
    estimated_delivery_date     DATE,
    actual_delivery_date        DATE,
    payment_type_id             BIGINT,
    payment_method              VARCHAR(50),
    payment_status              VARCHAR(30),
    payment_transaction_id      UUID,
    order_number                VARCHAR(50) NOT NULL UNIQUE,
    customer_notes              TEXT,
    internal_notes              TEXT,
    cancellation_reason         VARCHAR(255),
    source                      VARCHAR(20) DEFAULT 'WEB',
    ip_address                  VARCHAR(45),
    user_agent                  TEXT,
    coupon_code                 VARCHAR(50),
    coupon_id                   BIGINT,
    coupon_discount_amount      DECIMAL(15, 2),
    placed_at                   TIMESTAMP,
    confirmed_at                TIMESTAMP,
    shipped_at                  TIMESTAMP,
    delivered_at                TIMESTAMP,
    cancelled_at                TIMESTAMP,
    completed_at                TIMESTAMP,
    created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_order_items (
    order_item_id        UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id             UUID   NOT NULL REFERENCES customer_orders(order_id) ON DELETE CASCADE,
    product_id           BIGINT NOT NULL REFERENCES products(product_id),
    variant_id           UUID   REFERENCES product_variants(variant_id),
    shop_id              BIGINT NOT NULL REFERENCES shops(shop_id),
    sku                  VARCHAR(100),
    product_name         VARCHAR(255) NOT NULL,
    variant_name         VARCHAR(255),
    product_image_url    VARCHAR(500),
    quantity             INTEGER NOT NULL,
    unit_price           DECIMAL(15, 2) NOT NULL,
    cost_price           DECIMAL(15, 2),
    total_price          DECIMAL(15, 2) NOT NULL,
    discount_amount      DECIMAL(15, 2) DEFAULT 0,
    tax_amount           DECIMAL(15, 2) DEFAULT 0,
    status               VARCHAR(30) DEFAULT 'PENDING',
    fulfillment_status   VARCHAR(30) DEFAULT 'PENDING',
    quantity_shipped     INTEGER DEFAULT 0,
    quantity_returned    INTEGER DEFAULT 0,
    quantity_refunded    INTEGER DEFAULT 0,
    notes                VARCHAR(500),
    metadata             TEXT,
    category_name        VARCHAR(255),
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_history (
    history_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id     UUID NOT NULL REFERENCES customer_orders(order_id) ON DELETE CASCADE,
    status       VARCHAR(30) NOT NULL,
    note         TEXT,
    actor_id     BIGINT,
    actor_type   VARCHAR(20),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- REVIEW CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS reviews (
    review_id              UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_id             BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    variant_id             UUID   REFERENCES product_variants(variant_id),
    order_id               UUID   REFERENCES customer_orders(order_id),
    order_item_id          UUID,
    customer_id            BIGINT NOT NULL REFERENCES customers(customer_id),
    shop_id                BIGINT NOT NULL REFERENCES shops(shop_id),
    status                 VARCHAR(20) DEFAULT 'PENDING',
    rating_overall         INTEGER NOT NULL CHECK (rating_overall BETWEEN 1 AND 5),
    rating_quality         INTEGER CHECK (rating_quality BETWEEN 1 AND 5),
    rating_value           INTEGER CHECK (rating_value BETWEEN 1 AND 5),
    rating_shipping        INTEGER CHECK (rating_shipping BETWEEN 1 AND 5),
    title                  VARCHAR(255),
    body                   TEXT,
    pros                   VARCHAR ARRAY,
    cons                   VARCHAR ARRAY,
    helpful_votes          INTEGER DEFAULT 0,
    unhelpful_votes        INTEGER DEFAULT 0,
    is_verified_purchase   BOOLEAN DEFAULT FALSE,
    is_anonymous           BOOLEAN DEFAULT FALSE,
    moderated_by           BIGINT REFERENCES admin_users(id),
    moderated_at           TIMESTAMP,
    reject_reason          TEXT,
    flag_reason            TEXT,
    published_at           TIMESTAMP,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review_images (
    image_id    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    review_id   UUID NOT NULL REFERENCES reviews(review_id) ON DELETE CASCADE,
    url         VARCHAR(500) NOT NULL,
    alt_text    VARCHAR(255),
    position    INTEGER DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review_videos (
    video_id           UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    review_id          UUID NOT NULL REFERENCES reviews(review_id) ON DELETE CASCADE,
    url                VARCHAR(500) NOT NULL,
    thumbnail_url      VARCHAR(500),
    duration_seconds   INTEGER,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review_responses (
    response_id    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    review_id      UUID NOT NULL UNIQUE REFERENCES reviews(review_id) ON DELETE CASCADE,
    body           TEXT NOT NULL,
    responded_by   BIGINT NOT NULL REFERENCES admin_users(id),
    responded_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review_votes (
    vote_id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    review_id    UUID   NOT NULL REFERENCES reviews(review_id) ON DELETE CASCADE,
    customer_id  BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    is_helpful   BOOLEAN NOT NULL,
    voted_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(review_id, customer_id)
);

CREATE TABLE IF NOT EXISTS product_ratings (
    product_id       BIGINT PRIMARY KEY REFERENCES products(product_id) ON DELETE CASCADE,
    average_rating   DECIMAL(3, 2) DEFAULT 0,
    total_reviews    INTEGER DEFAULT 0,
    rating_1_count   INTEGER DEFAULT 0,
    rating_2_count   INTEGER DEFAULT 0,
    rating_3_count   INTEGER DEFAULT 0,
    rating_4_count   INTEGER DEFAULT 0,
    rating_5_count   INTEGER DEFAULT 0,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shop_ratings (
    shop_id          BIGINT PRIMARY KEY REFERENCES shops(shop_id) ON DELETE CASCADE,
    average_rating   DECIMAL(3, 2) DEFAULT 0,
    total_reviews    INTEGER DEFAULT 0,
    rating_1_count   INTEGER DEFAULT 0,
    rating_2_count   INTEGER DEFAULT 0,
    rating_3_count   INTEGER DEFAULT 0,
    rating_4_count   INTEGER DEFAULT 0,
    rating_5_count   INTEGER DEFAULT 0,
    response_rate    DECIMAL(5, 2) DEFAULT 0,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PAYMENT CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS payment_transactions (
    transaction_id           UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id                 UUID REFERENCES customer_orders(order_id),
    customer_id              BIGINT REFERENCES customers(customer_id),
    type                     VARCHAR(20) NOT NULL,
    status                   VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    amount                   DECIMAL(15, 2) NOT NULL,
    currency                 VARCHAR(3) NOT NULL,
    amount_in_cents          BIGINT NOT NULL,
    gateway                  VARCHAR(50) NOT NULL,
    gateway_transaction_id   VARCHAR(255),
    authorization_code       VARCHAR(100),
    response_code            VARCHAR(50),
    response_message         TEXT,
    payment_method_type      VARCHAR(20),
    card_brand               VARCHAR(20),
    card_last4               VARCHAR(4),
    card_expiry_month        INTEGER,
    card_expiry_year         INTEGER,
    cardholder_name          VARCHAR(255),
    billing_address_line1    VARCHAR(255),
    billing_address_line2    VARCHAR(255),
    billing_city             VARCHAR(100),
    billing_state            VARCHAR(100),
    billing_postal_code      VARCHAR(20),
    billing_country          VARCHAR(2),
    risk_score               INTEGER,
    risk_level               VARCHAR(20),
    risk_factors             VARCHAR ARRAY,
    ip_address               VARCHAR(45),
    user_agent               TEXT,
    device_fingerprint       VARCHAR(255),
    metadata                 TEXT,
    parent_transaction_id    UUID REFERENCES payment_transactions(transaction_id),
    processed_at             TIMESTAMP,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS refunds (
    refund_id            UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id             UUID NOT NULL REFERENCES customer_orders(order_id),
    transaction_id       UUID REFERENCES payment_transactions(transaction_id),
    status               VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    type                 VARCHAR(20) NOT NULL DEFAULT 'FULL',
    items_amount         DECIMAL(15, 2) DEFAULT 0,
    shipping_amount      DECIMAL(15, 2) DEFAULT 0,
    tax_amount           DECIMAL(15, 2) DEFAULT 0,
    total_amount         DECIMAL(15, 2) NOT NULL,
    currency             VARCHAR(3) NOT NULL,
    reason               VARCHAR(50) NOT NULL,
    reason_detail        TEXT,
    internal_note        TEXT,
    requested_by         BIGINT NOT NULL,
    requested_by_type    VARCHAR(20) NOT NULL,
    approved_by          BIGINT REFERENCES admin_users(id),
    gateway_refund_id    VARCHAR(255),
    approved_at          TIMESTAMP,
    processed_at         TIMESTAMP,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS refund_items (
    refund_item_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    refund_id        UUID NOT NULL REFERENCES refunds(refund_id) ON DELETE CASCADE,
    order_item_id    UUID NOT NULL,
    quantity         INTEGER NOT NULL,
    reason           VARCHAR(50),
    amount           DECIMAL(15, 2) NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- FULFILLMENT CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS carriers (
    carrier_id              BIGSERIAL PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL,
    code                    VARCHAR(50) NOT NULL UNIQUE,
    tracking_url_template   VARCHAR(500),
    logo_url                VARCHAR(500),
    is_active               BOOLEAN DEFAULT TRUE,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shipments (
    shipment_id               UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id                  UUID   NOT NULL REFERENCES customer_orders(order_id),
    shop_id                   BIGINT NOT NULL REFERENCES shops(shop_id),
    status                    VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    carrier_id                BIGINT REFERENCES carriers(carrier_id),
    carrier_name              VARCHAR(100),
    service_name              VARCHAR(100),
    tracking_number           VARCHAR(100),
    tracking_url              VARCHAR(500),
    label_url                 VARCHAR(500),
    shipping_cost             DECIMAL(15, 2),
    currency                  VARCHAR(3),
    weight_grams              DECIMAL(10, 2),
    length_cm                 DECIMAL(10, 2),
    width_cm                  DECIMAL(10, 2),
    height_cm                 DECIMAL(10, 2),
    ship_to_name              VARCHAR(200),
    ship_to_address_line1     VARCHAR(255),
    ship_to_address_line2     VARCHAR(255),
    ship_to_city              VARCHAR(100),
    ship_to_state             VARCHAR(100),
    ship_to_postal_code       VARCHAR(20),
    ship_to_country           VARCHAR(2),
    ship_to_phone             VARCHAR(20),
    estimated_delivery_date   DATE,
    actual_delivery_date      DATE,
    shipped_at                TIMESTAMP,
    delivered_at              TIMESTAMP,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shipment_items (
    shipment_item_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    shipment_id        UUID NOT NULL REFERENCES shipments(shipment_id) ON DELETE CASCADE,
    order_item_id      UUID NOT NULL,
    quantity           INTEGER NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shipment_tracking_events (
    event_id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    shipment_id   UUID NOT NULL REFERENCES shipments(shipment_id) ON DELETE CASCADE,
    status        VARCHAR(50) NOT NULL,
    description   TEXT,
    location      VARCHAR(255),
    city          VARCHAR(100),
    state         VARCHAR(100),
    country       VARCHAR(2),
    postal_code   VARCHAR(20),
    occurred_at   TIMESTAMP NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS returns (
    return_id                 UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id                  UUID   NOT NULL REFERENCES customer_orders(order_id),
    shop_id                   BIGINT NOT NULL REFERENCES shops(shop_id),
    customer_id               BIGINT NOT NULL REFERENCES customers(customer_id),
    status                    VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
    return_shipping_method    VARCHAR(30),
    return_carrier            VARCHAR(100),
    return_tracking_number    VARCHAR(100),
    return_label_url          VARCHAR(500),
    resolution_type           VARCHAR(30),
    resolution_amount         DECIMAL(15, 2),
    exchange_variant_id       UUID REFERENCES product_variants(variant_id),
    store_credit_id           UUID,
    inspected_by              BIGINT REFERENCES admin_users(id),
    inspected_at              TIMESTAMP,
    inspection_condition      VARCHAR(30),
    inspection_notes          TEXT,
    requested_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at               TIMESTAMP,
    received_at               TIMESTAMP,
    completed_at              TIMESTAMP,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS return_items (
    return_item_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    return_id        UUID NOT NULL REFERENCES returns(return_id) ON DELETE CASCADE,
    order_item_id    UUID NOT NULL,
    variant_id       UUID REFERENCES product_variants(variant_id),
    quantity         INTEGER NOT NULL,
    reason           VARCHAR(50) NOT NULL,
    condition        VARCHAR(30),
    note             TEXT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- INVENTORY CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS warehouses (
    warehouse_id          BIGSERIAL PRIMARY KEY,
    shop_id               BIGINT REFERENCES shops(shop_id),
    name                  VARCHAR(100) NOT NULL,
    code                  VARCHAR(50) NOT NULL UNIQUE,
    address_line1         VARCHAR(255),
    address_line2         VARCHAR(255),
    city                  VARCHAR(100),
    state                 VARCHAR(100),
    postal_code           VARCHAR(20),
    country_code          VARCHAR(2),
    latitude              DECIMAL(10, 8),
    longitude             DECIMAL(11, 8),
    is_active             BOOLEAN DEFAULT TRUE,
    priority              INTEGER DEFAULT 0,
    contact_name          VARCHAR(100),
    contact_email         VARCHAR(255),
    contact_phone         VARCHAR(50),
    operating_hours       TEXT,
    type                  VARCHAR(50) DEFAULT 'FULFILLMENT_CENTER',
    capacity_units        INTEGER,
    current_utilization   DECIMAL(5, 2),
    accepts_returns       BOOLEAN DEFAULT TRUE,
    accepts_inbound       BOOLEAN DEFAULT TRUE,
    supported_carriers    TEXT,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inventory_items (
    inventory_item_id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    variant_id             UUID   NOT NULL REFERENCES product_variants(variant_id) ON DELETE CASCADE,
    warehouse_id           BIGINT NOT NULL REFERENCES warehouses(warehouse_id) ON DELETE CASCADE,
    sku                    VARCHAR(100) NOT NULL,
    on_hand                INTEGER NOT NULL DEFAULT 0,
    reserved               INTEGER NOT NULL DEFAULT 0,
    available              INTEGER GENERATED ALWAYS AS (on_hand - reserved) STORED,
    incoming               INTEGER DEFAULT 0,
    damaged                INTEGER DEFAULT 0,
    low_stock_threshold    INTEGER DEFAULT 10,
    reorder_point          INTEGER DEFAULT 5,
    reorder_quantity       INTEGER DEFAULT 50,
    max_stock              INTEGER,
    tracking_type          VARCHAR(20) DEFAULT 'NONE',
    location_code          VARCHAR(50),
    last_counted_at        TIMESTAMP,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(variant_id, warehouse_id)
);

CREATE TABLE IF NOT EXISTS inventory_movements (
    movement_id          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    inventory_item_id    UUID NOT NULL REFERENCES inventory_items(inventory_item_id),
    type                 VARCHAR(30) NOT NULL,
    quantity             INTEGER NOT NULL,
    quantity_before      INTEGER NOT NULL,
    quantity_after       INTEGER NOT NULL,
    reference_type       VARCHAR(50),
    reference_id         VARCHAR(100),
    reason               VARCHAR(255),
    note                 TEXT,
    performed_by         BIGINT,
    performed_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stock_reservations (
    reservation_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    inventory_item_id    UUID NOT NULL REFERENCES inventory_items(inventory_item_id),
    reference_type       VARCHAR(30) NOT NULL,
    reference_id         UUID NOT NULL,
    quantity             INTEGER NOT NULL,
    expires_at           TIMESTAMP NOT NULL,
    released_at          TIMESTAMP,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- VENDOR FINANCE CONTEXT
-- =====================================================

CREATE TABLE IF NOT EXISTS vendor_bank_accounts (
    bank_account_id             UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    shop_id                     BIGINT NOT NULL REFERENCES shops(shop_id) ON DELETE CASCADE,
    account_name                VARCHAR(255) NOT NULL,
    account_number_encrypted    VARCHAR(500) NOT NULL,
    bank_name                   VARCHAR(255) NOT NULL,
    routing_number              VARCHAR(50),
    swift_code                  VARCHAR(20),
    iban                        VARCHAR(50),
    currency                    VARCHAR(3) DEFAULT 'USD',
    is_primary                  BOOLEAN DEFAULT FALSE,
    verified_at                 TIMESTAMP,
    created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS commission_rates (
    commission_rate_id   BIGSERIAL PRIMARY KEY,
    shop_id              BIGINT REFERENCES shops(shop_id) ON DELETE CASCADE,
    category_id          BIGINT REFERENCES categories(category_id) ON DELETE CASCADE,
    rate                 DECIMAL(5, 2) NOT NULL,
    effective_from       DATE NOT NULL,
    effective_to         DATE,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(shop_id, category_id, effective_from)
);

CREATE TABLE IF NOT EXISTS vendor_payouts (
    payout_id                UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    shop_id                  BIGINT NOT NULL REFERENCES shops(shop_id),
    status                   VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    period_start             DATE NOT NULL,
    period_end               DATE NOT NULL,
    gross_sales              DECIMAL(15, 2) NOT NULL,
    refunds                  DECIMAL(15, 2) DEFAULT 0,
    commission               DECIMAL(15, 2) NOT NULL,
    fees                     DECIMAL(15, 2) DEFAULT 0,
    adjustments              DECIMAL(15, 2) DEFAULT 0,
    net_payout               DECIMAL(15, 2) NOT NULL,
    currency                 VARCHAR(3) DEFAULT 'USD',
    bank_account_id          UUID REFERENCES vendor_bank_accounts(bank_account_id),
    transaction_reference    VARCHAR(255),
    scheduled_for            DATE,
    processed_at             TIMESTAMP,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payout_items (
    payout_item_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    payout_id            UUID NOT NULL REFERENCES vendor_payouts(payout_id) ON DELETE CASCADE,
    order_id             UUID NOT NULL REFERENCES customer_orders(order_id),
    order_number         VARCHAR(50),
    order_date           DATE,
    gross_amount         DECIMAL(15, 2) NOT NULL,
    commission_amount    DECIMAL(15, 2) NOT NULL,
    net_amount           DECIMAL(15, 2) NOT NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PLATFORM: idempotency, outbox, audit
-- =====================================================

CREATE TABLE IF NOT EXISTS idempotency_keys (
    key_value         VARCHAR(200) PRIMARY KEY,
    endpoint          VARCHAR(200) NOT NULL,
    request_hash      VARCHAR(128),
    response_status   INTEGER,
    response_body     TEXT,
    created_at        TIMESTAMP NOT NULL,
    expires_at        TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS domain_event_outbox (
    id             BIGSERIAL PRIMARY KEY,
    event_id       VARCHAR(36) NOT NULL UNIQUE,
    event_type     VARCHAR(100) NOT NULL,
    payload        TEXT NOT NULL,
    occurred_at    TIMESTAMP NOT NULL,
    published_at   TIMESTAMP,
    retries        INTEGER NOT NULL DEFAULT 0,
    last_error     VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS audit_log (
    id            BIGSERIAL PRIMARY KEY,
    entity_type   VARCHAR(100) NOT NULL,
    entity_id     VARCHAR(100) NOT NULL,
    action        VARCHAR(20) NOT NULL,
    actor         VARCHAR(100),
    diff          TEXT,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- CMS
-- =====================================================

CREATE TABLE IF NOT EXISTS cms_pages (
    id                BIGSERIAL PRIMARY KEY,
    slug              VARCHAR(150) NOT NULL UNIQUE,
    title             VARCHAR(255) NOT NULL,
    body              TEXT,
    seo_title         VARCHAR(255),
    seo_description   VARCHAR(500),
    locale            VARCHAR(10) DEFAULT 'en',
    published         BOOLEAN DEFAULT FALSE,
    published_at      TIMESTAMP,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS cms_banners (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(150) NOT NULL,
    image_url     VARCHAR(500) NOT NULL,
    link_url      VARCHAR(500),
    placement     VARCHAR(50) NOT NULL,
    sort_order    INTEGER DEFAULT 0,
    starts_at     TIMESTAMP,
    ends_at       TIMESTAMP,
    is_active     BOOLEAN DEFAULT TRUE,
    locale        VARCHAR(10) DEFAULT 'en',
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- cms_blocks uses (code, locale) as the natural key so the same block
-- can have a localised variant per supported locale.
CREATE TABLE IF NOT EXISTS cms_blocks (
    id           BIGSERIAL PRIMARY KEY,
    code         VARCHAR(100) NOT NULL,
    title        VARCHAR(255),
    body         TEXT,
    locale       VARCHAR(10)  NOT NULL DEFAULT 'en',
    is_active    BOOLEAN DEFAULT TRUE,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_cms_blocks_code_locale UNIQUE (code, locale)
);

CREATE TABLE IF NOT EXISTS cms_faq (
    id           BIGSERIAL PRIMARY KEY,
    category     VARCHAR(100),
    question     VARCHAR(500) NOT NULL,
    answer       TEXT NOT NULL,
    sort_order   INTEGER DEFAULT 0,
    locale       VARCHAR(10) DEFAULT 'en',
    is_active    BOOLEAN DEFAULT TRUE,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- GIFT CARDS
-- =====================================================

CREATE TABLE IF NOT EXISTS gift_cards (
    id                      BIGSERIAL PRIMARY KEY,
    code                    VARCHAR(40) NOT NULL UNIQUE,
    initial_amount          DECIMAL(14, 2) NOT NULL,
    balance                 DECIMAL(14, 2) NOT NULL,
    currency                VARCHAR(3) NOT NULL DEFAULT 'USD',
    purchaser_customer_id   BIGINT REFERENCES customers(customer_id),
    recipient_email         VARCHAR(255),
    recipient_name          VARCHAR(255),
    message                 TEXT,
    issued_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at              TIMESTAMP,
    status                  VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS gift_card_transactions (
    id             BIGSERIAL PRIMARY KEY,
    gift_card_id   BIGINT NOT NULL REFERENCES gift_cards(id) ON DELETE CASCADE,
    order_id       UUID,
    type           VARCHAR(20) NOT NULL,
    amount         DECIMAL(14, 2) NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- SUBSCRIPTIONS
-- =====================================================

CREATE TABLE IF NOT EXISTS subscriptions (
    id                    BIGSERIAL PRIMARY KEY,
    customer_id           BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    status                VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    interval_unit         VARCHAR(20) NOT NULL,
    interval_count        INTEGER NOT NULL DEFAULT 1,
    currency              VARCHAR(3) NOT NULL DEFAULT 'USD',
    total_amount          DECIMAL(14, 2),
    next_renewal_at       TIMESTAMP,
    cancelled_at          TIMESTAMP,
    gateway_customer_id   VARCHAR(100),
    retry_count           INTEGER NOT NULL DEFAULT 0,
    last_error            VARCHAR(500),
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subscription_items (
    id                BIGSERIAL PRIMARY KEY,
    subscription_id   BIGINT NOT NULL REFERENCES subscriptions(id) ON DELETE CASCADE,
    product_id        BIGINT NOT NULL REFERENCES products(product_id),
    quantity          INTEGER NOT NULL DEFAULT 1,
    unit_price        DECIMAL(14, 2) NOT NULL
);

-- =====================================================
-- B2B: customer groups & price lists
-- =====================================================

CREATE TABLE IF NOT EXISTS customer_groups (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(50) NOT NULL UNIQUE,
    name                VARCHAR(150) NOT NULL,
    description         VARCHAR(500),
    discount_percent    DECIMAL(5, 2) DEFAULT 0,
    tax_exempt          BOOLEAN DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS price_lists (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(50) NOT NULL UNIQUE,
    name                VARCHAR(150) NOT NULL,
    customer_group_id   BIGINT REFERENCES customer_groups(id),
    currency            VARCHAR(3) NOT NULL DEFAULT 'USD',
    starts_at           TIMESTAMP,
    ends_at             TIMESTAMP,
    is_active           BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS price_list_items (
    id              BIGSERIAL PRIMARY KEY,
    price_list_id   BIGINT NOT NULL REFERENCES price_lists(id) ON DELETE CASCADE,
    product_id      BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    min_quantity    INTEGER NOT NULL DEFAULT 1,
    unit_price      DECIMAL(14, 2) NOT NULL,
    notes           VARCHAR(500),
    UNIQUE(price_list_id, product_id, min_quantity)
);

-- =====================================================
-- TAX ENGINE
-- =====================================================

CREATE TABLE IF NOT EXISTS tax_zones (
    tax_zone_id           BIGSERIAL PRIMARY KEY,
    code                  VARCHAR(50) NOT NULL UNIQUE,
    name                  VARCHAR(150) NOT NULL,
    country_code          VARCHAR(2)   NOT NULL,
    region                VARCHAR(100),
    postal_code_pattern   VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tax_rules (
    tax_rule_id    BIGSERIAL PRIMARY KEY,
    name           VARCHAR(150) NOT NULL,
    tax_zone_id    BIGINT NOT NULL REFERENCES tax_zones(tax_zone_id) ON DELETE CASCADE,
    rate           DECIMAL(6, 4) NOT NULL,
    category       VARCHAR(100),
    priority       INTEGER DEFAULT 0,
    compound       BOOLEAN DEFAULT FALSE,
    active         BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- RECOMMENDATIONS
-- =====================================================

CREATE TABLE IF NOT EXISTS product_co_purchases (
    product_id          BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    co_product_id       BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    score               DOUBLE PRECISION NOT NULL DEFAULT 0,
    co_purchase_count   BIGINT NOT NULL DEFAULT 0,
    last_computed_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id, co_product_id)
);

CREATE TABLE IF NOT EXISTS recently_viewed_products (
    id            BIGSERIAL PRIMARY KEY,
    customer_id   BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    session_id    VARCHAR(100),
    product_id    BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    viewed_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- SEARCH
-- =====================================================

CREATE TABLE IF NOT EXISTS search_queries (
    id              BIGSERIAL PRIMARY KEY,
    query           VARCHAR(500) NOT NULL,
    customer_id     BIGINT REFERENCES customers(customer_id),
    results_count   INTEGER NOT NULL DEFAULT 0,
    locale          VARCHAR(10),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- NOTIFICATIONS
-- =====================================================

CREATE TABLE IF NOT EXISTS notification_templates (
    template_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code          VARCHAR(100) NOT NULL,
    channel       VARCHAR(20) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    subject       VARCHAR(500),
    body          TEXT NOT NULL,
    locale        VARCHAR(10) DEFAULT 'en-US',
    is_active     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(code, channel, locale)
);

CREATE TABLE IF NOT EXISTS notifications (
    notification_id     UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    template_id         UUID REFERENCES notification_templates(template_id),
    recipient_id        BIGINT NOT NULL,
    recipient_type      VARCHAR(20) NOT NULL,
    recipient_contact   VARCHAR(255) NOT NULL,
    channel             VARCHAR(20) NOT NULL,
    status              VARCHAR(20) DEFAULT 'PENDING',
    subject             VARCHAR(500),
    body                TEXT,
    metadata            TEXT,
    sent_at             TIMESTAMP,
    delivered_at        TIMESTAMP,
    read_at             TIMESTAMP,
    failure_reason      TEXT,
    retry_count         INTEGER DEFAULT 0,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notification_preferences (
    preference_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    user_type           VARCHAR(20) NOT NULL,
    channel             VARCHAR(20) NOT NULL,
    notification_type   VARCHAR(50) NOT NULL,
    is_enabled          BOOLEAN DEFAULT TRUE,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, user_type, channel, notification_type)
);

CREATE TABLE IF NOT EXISTS push_tokens (
    id             BIGSERIAL PRIMARY KEY,
    customer_id    BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    platform       VARCHAR(10) NOT NULL,
    token          VARCHAR(500) NOT NULL UNIQUE,
    device_id      VARCHAR(120),
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_seen_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- REFERRAL PROGRAM
-- =====================================================

CREATE TABLE IF NOT EXISTS referrals (
    id               BIGSERIAL PRIMARY KEY,
    referrer_id      BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    referee_email    VARCHAR(255) NOT NULL,
    referee_id       BIGINT REFERENCES customers(customer_id) ON DELETE SET NULL,
    code             VARCHAR(40) NOT NULL UNIQUE,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    rewarded_at      TIMESTAMP,
    reward_amount    DECIMAL(14, 2),
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- INDEXES
-- =====================================================

-- Products
CREATE INDEX IF NOT EXISTS idx_products_slug           ON products(slug);
CREATE INDEX IF NOT EXISTS idx_products_status         ON products(status);
CREATE INDEX IF NOT EXISTS idx_products_shop_category  ON products(fk_shop_id, fk_category_id);
CREATE INDEX IF NOT EXISTS idx_products_shop_active    ON products(fk_shop_id);
CREATE INDEX IF NOT EXISTS idx_products_deleted_at     ON products(deleted_at);

-- Product images
CREATE INDEX IF NOT EXISTS idx_product_images_product  ON product_images(fk_product_id);

-- Product Variants
CREATE INDEX IF NOT EXISTS idx_variants_product        ON product_variants(product_id);
CREATE INDEX IF NOT EXISTS idx_variants_sku            ON product_variants(sku);
CREATE INDEX IF NOT EXISTS idx_variants_status         ON product_variants(status);

-- Variant Attributes
CREATE INDEX IF NOT EXISTS idx_variant_attrs_variant   ON variant_attributes(variant_id);
CREATE INDEX IF NOT EXISTS idx_variant_attrs_value     ON variant_attributes(value_id);

-- Customers
CREATE INDEX IF NOT EXISTS idx_customers_deleted_at        ON customers(deleted_at);
CREATE INDEX IF NOT EXISTS idx_customer_addresses_customer ON customer_addresses(customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_addresses_default  ON customer_addresses(customer_id, is_default);

-- Carts
CREATE INDEX IF NOT EXISTS idx_carts_customer           ON carts(customer_id);
CREATE INDEX IF NOT EXISTS idx_carts_session            ON carts(session_id);
CREATE INDEX IF NOT EXISTS idx_carts_status             ON carts(status);
CREATE INDEX IF NOT EXISTS idx_carts_status_updated     ON carts(status, updated_at);
CREATE INDEX IF NOT EXISTS idx_cart_items_cart          ON cart_items(cart_id);
CREATE INDEX IF NOT EXISTS idx_cart_items_variant       ON cart_items(variant_id);

-- Orders
CREATE INDEX IF NOT EXISTS idx_orders_number            ON customer_orders(order_number);
CREATE INDEX IF NOT EXISTS idx_orders_customer_status   ON customer_orders(customer_id, status);
CREATE INDEX IF NOT EXISTS idx_orders_shop_status       ON customer_orders(shop_id, status);
CREATE INDEX IF NOT EXISTS idx_orders_parent            ON customer_orders(parent_order_id);

-- Wishlists
CREATE INDEX IF NOT EXISTS idx_wishlists_customer       ON wishlists(customer_id);
CREATE INDEX IF NOT EXISTS idx_wishlist_items_wishlist  ON wishlist_items(wishlist_id);
CREATE INDEX IF NOT EXISTS idx_wishlist_items_product   ON wishlist_items(product_id);

-- Reviews
CREATE INDEX IF NOT EXISTS idx_reviews_product          ON reviews(product_id);
CREATE INDEX IF NOT EXISTS idx_reviews_customer         ON reviews(customer_id);
CREATE INDEX IF NOT EXISTS idx_reviews_shop             ON reviews(shop_id);
CREATE INDEX IF NOT EXISTS idx_reviews_status           ON reviews(status);
CREATE INDEX IF NOT EXISTS idx_reviews_rating           ON reviews(rating_overall);

-- Payment Transactions
CREATE INDEX IF NOT EXISTS idx_payment_trans_order     ON payment_transactions(order_id);
CREATE INDEX IF NOT EXISTS idx_payment_trans_customer  ON payment_transactions(customer_id);
CREATE INDEX IF NOT EXISTS idx_payment_trans_status    ON payment_transactions(status);
CREATE INDEX IF NOT EXISTS idx_payment_trans_gateway   ON payment_transactions(gateway, gateway_transaction_id);

-- Refunds
CREATE INDEX IF NOT EXISTS idx_refunds_order           ON refunds(order_id);
CREATE INDEX IF NOT EXISTS idx_refunds_status          ON refunds(status);

-- Shipments
CREATE INDEX IF NOT EXISTS idx_shipments_order         ON shipments(order_id);
CREATE INDEX IF NOT EXISTS idx_shipments_shop          ON shipments(shop_id);
CREATE INDEX IF NOT EXISTS idx_shipments_status        ON shipments(status);
CREATE INDEX IF NOT EXISTS idx_shipments_tracking      ON shipments(tracking_number);

-- Returns
CREATE INDEX IF NOT EXISTS idx_returns_order           ON returns(order_id);
CREATE INDEX IF NOT EXISTS idx_returns_customer        ON returns(customer_id);
CREATE INDEX IF NOT EXISTS idx_returns_status          ON returns(status);

-- Inventory
CREATE INDEX IF NOT EXISTS idx_inventory_variant       ON inventory_items(variant_id);
CREATE INDEX IF NOT EXISTS idx_inventory_warehouse     ON inventory_items(warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inventory_sku           ON inventory_items(sku);
CREATE INDEX IF NOT EXISTS idx_inv_movements_item      ON inventory_movements(inventory_item_id);
CREATE INDEX IF NOT EXISTS idx_inv_movements_type      ON inventory_movements(type);
CREATE INDEX IF NOT EXISTS idx_inv_movements_reference ON inventory_movements(reference_type, reference_id);

-- Vendor Payouts
CREATE INDEX IF NOT EXISTS idx_payouts_shop            ON vendor_payouts(shop_id);
CREATE INDEX IF NOT EXISTS idx_payouts_status          ON vendor_payouts(status);
CREATE INDEX IF NOT EXISTS idx_payouts_period          ON vendor_payouts(period_start, period_end);

-- Platform
CREATE INDEX IF NOT EXISTS idx_idem_expires            ON idempotency_keys(expires_at);
CREATE INDEX IF NOT EXISTS idx_outbox_unpublished      ON domain_event_outbox(published_at);
CREATE INDEX IF NOT EXISTS idx_outbox_event_type       ON domain_event_outbox(event_type);
CREATE INDEX IF NOT EXISTS idx_audit_entity            ON audit_log(entity_type, entity_id);

-- Auth / refresh tokens
CREATE INDEX IF NOT EXISTS idx_refresh_expires         ON refresh_tokens(expires_at);

-- Recommendations / search
CREATE INDEX IF NOT EXISTS idx_copurchases_score       ON product_co_purchases(product_id, score DESC);
CREATE INDEX IF NOT EXISTS idx_copurchases_count       ON product_co_purchases(product_id, co_purchase_count DESC);
CREATE INDEX IF NOT EXISTS idx_recent_views            ON recently_viewed_products(customer_id, viewed_at DESC);
CREATE INDEX IF NOT EXISTS idx_recent_views_session    ON recently_viewed_products(session_id, viewed_at DESC);
CREATE INDEX IF NOT EXISTS idx_search_query_time       ON search_queries(created_at DESC);

-- Notifications / push / referrals
CREATE INDEX IF NOT EXISTS idx_notifications_recipient       ON notifications(recipient_id, recipient_type);
CREATE INDEX IF NOT EXISTS idx_notifications_status          ON notifications(status);
CREATE INDEX IF NOT EXISTS idx_push_tokens_customer          ON push_tokens(customer_id);
CREATE INDEX IF NOT EXISTS idx_referrals_referrer            ON referrals(referrer_id);
CREATE INDEX IF NOT EXISTS idx_referrals_email               ON referrals(referee_email);
CREATE INDEX IF NOT EXISTS idx_referrals_referee_status      ON referrals(referee_id, status);

-- CMS
CREATE INDEX IF NOT EXISTS idx_cms_blocks_code         ON cms_blocks(code);

-- =====================================================
-- SEED DATA
-- =====================================================

-- Default attributes
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position)
SELECT 'Color',    'color',    'SELECT', true,  true, true,  1 WHERE NOT EXISTS (SELECT 1 FROM attributes WHERE code = 'color');
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position)
SELECT 'Size',     'size',     'SELECT', true,  true, true,  2 WHERE NOT EXISTS (SELECT 1 FROM attributes WHERE code = 'size');
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position)
SELECT 'Material', 'material', 'SELECT', false, true, true,  3 WHERE NOT EXISTS (SELECT 1 FROM attributes WHERE code = 'material');
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position)
SELECT 'Weight',   'weight',   'TEXT',   false, true, false, 4 WHERE NOT EXISTS (SELECT 1 FROM attributes WHERE code = 'weight');
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position)
SELECT 'Pattern',  'pattern',  'SELECT', false, true, true,  5 WHERE NOT EXISTS (SELECT 1 FROM attributes WHERE code = 'pattern');

-- Common color values
INSERT INTO attribute_values (attribute_id, display_value, code, metadata, position)
SELECT a.attribute_id, v.display_val, v.code, v.metadata, v.position
FROM attributes a
CROSS JOIN (VALUES
    ('Red',    'red',    '{"hex": "#FF0000"}',  1),
    ('Blue',   'blue',   '{"hex": "#0000FF"}',  2),
    ('Green',  'green',  '{"hex": "#00FF00"}',  3),
    ('Black',  'black',  '{"hex": "#000000"}',  4),
    ('White',  'white',  '{"hex": "#FFFFFF"}',  5),
    ('Yellow', 'yellow', '{"hex": "#FFFF00"}',  6),
    ('Orange', 'orange', '{"hex": "#FFA500"}',  7),
    ('Purple', 'purple', '{"hex": "#800080"}',  8),
    ('Pink',   'pink',   '{"hex": "#FFC0CB"}',  9),
    ('Brown',  'brown',  '{"hex": "#A52A2A"}', 10),
    ('Gray',   'gray',   '{"hex": "#808080"}', 11),
    ('Navy',   'navy',   '{"hex": "#000080"}', 12)
) AS v(display_val, code, metadata, position)
WHERE a.code = 'color'
  AND NOT EXISTS (SELECT 1 FROM attribute_values av WHERE av.attribute_id = a.attribute_id AND av.code = v.code);

-- Common size values
INSERT INTO attribute_values (attribute_id, display_value, code, position)
SELECT a.attribute_id, v.display_val, v.code, v.position
FROM attributes a
CROSS JOIN (VALUES
    ('XXS', 'xxs', 1),
    ('XS',  'xs',  2),
    ('S',   's',   3),
    ('M',   'm',   4),
    ('L',   'l',   5),
    ('XL',  'xl',  6),
    ('XXL', 'xxl', 7),
    ('XXXL','xxxl',8)
) AS v(display_val, code, position)
WHERE a.code = 'size'
  AND NOT EXISTS (SELECT 1 FROM attribute_values av WHERE av.attribute_id = a.attribute_id AND av.code = v.code);

-- Common material values
INSERT INTO attribute_values (attribute_id, display_value, code, position)
SELECT a.attribute_id, v.display_val, v.code, v.position
FROM attributes a
CROSS JOIN (VALUES
    ('Cotton',    'cotton',    1),
    ('Polyester', 'polyester', 2),
    ('Wool',      'wool',      3),
    ('Silk',      'silk',      4),
    ('Leather',   'leather',   5),
    ('Denim',     'denim',     6),
    ('Linen',     'linen',     7),
    ('Nylon',     'nylon',     8),
    ('Velvet',    'velvet',    9),
    ('Cashmere',  'cashmere', 10)
) AS v(display_val, code, position)
WHERE a.code = 'material'
  AND NOT EXISTS (SELECT 1 FROM attribute_values av WHERE av.attribute_id = a.attribute_id AND av.code = v.code);

-- Default carriers
INSERT INTO carriers (name, code, tracking_url_template)
SELECT v.name, v.code, v.tracking_url_template
FROM (VALUES
    ('DHL',            'dhl',   'https://www.dhl.com/en/express/tracking.html?AWB={tracking_number}'),
    ('FedEx',          'fedex', 'https://www.fedex.com/fedextrack/?trknbr={tracking_number}'),
    ('UPS',            'ups',   'https://www.ups.com/track?tracknum={tracking_number}'),
    ('USPS',           'usps',  'https://tools.usps.com/go/TrackConfirmAction?tLabels={tracking_number}'),
    ('Local Delivery', 'local',  NULL)
) AS v(name, code, tracking_url_template)
WHERE NOT EXISTS (SELECT 1 FROM carriers c WHERE c.code = v.code);

-- Default notification templates
INSERT INTO notification_templates (code, channel, name, subject, body)
SELECT v.code, v.channel, v.name, v.subject, v.body
FROM (VALUES
('ORDER_PLACED', 'EMAIL', 'Order Confirmation', 'Your order #{{order_number}} has been placed',
'Dear {{customer_name}},

Thank you for your order!

Order Number: {{order_number}}
Order Date: {{order_date}}
Total: {{currency}}{{total}}

We will notify you when your order ships.

Thank you for shopping with us!'),

('ORDER_SHIPPED', 'EMAIL', 'Order Shipped', 'Your order #{{order_number}} has been shipped',
'Dear {{customer_name}},

Great news! Your order has been shipped.

Order Number: {{order_number}}
Tracking Number: {{tracking_number}}
Carrier: {{carrier_name}}

Track your package: {{tracking_url}}

Thank you for shopping with us!'),

('ORDER_DELIVERED', 'EMAIL', 'Order Delivered', 'Your order #{{order_number}} has been delivered',
'Dear {{customer_name}},

Your order has been delivered!

Order Number: {{order_number}}
Delivered: {{delivery_date}}

We hope you enjoy your purchase. If you have any questions, please contact us.

Thank you for shopping with us!')
) AS v(code, channel, name, subject, body)
WHERE NOT EXISTS (
    SELECT 1 FROM notification_templates nt
    WHERE nt.code = v.code AND nt.channel = v.channel AND nt.locale = 'en-US'
);

-- Default warehouse
INSERT INTO warehouses (name, code, city, country_code, is_active, priority)
SELECT v.name, v.code, v.city, v.country, v.is_active, v.priority
FROM (VALUES ('Main Warehouse', 'MAIN', 'City', 'US', true, 1)) AS v(name, code, city, country, is_active, priority)
WHERE NOT EXISTS (SELECT 1 FROM warehouses w WHERE w.code = v.code);

-- Default tax zones
INSERT INTO tax_zones (code, name, country_code) VALUES
    ('US-DEFAULT', 'United States Default', 'US'),
    ('EU-DE',      'Germany',                'DE'),
    ('EU-FR',      'France',                 'FR'),
    ('UK',         'United Kingdom',         'GB')
ON CONFLICT (code) DO NOTHING;

-- Default tax rules
INSERT INTO tax_rules (name, tax_zone_id, rate, compound)
SELECT 'US Sales Tax', tax_zone_id, 0.0875, FALSE FROM tax_zones WHERE code = 'US-DEFAULT'
ON CONFLICT DO NOTHING;

INSERT INTO tax_rules (name, tax_zone_id, rate, compound)
SELECT 'DE VAT', tax_zone_id, 0.19, FALSE FROM tax_zones WHERE code = 'EU-DE'
ON CONFLICT DO NOTHING;

INSERT INTO tax_rules (name, tax_zone_id, rate, compound)
SELECT 'FR VAT', tax_zone_id, 0.20, FALSE FROM tax_zones WHERE code = 'EU-FR'
ON CONFLICT DO NOTHING;

INSERT INTO tax_rules (name, tax_zone_id, rate, compound)
SELECT 'UK VAT', tax_zone_id, 0.20, FALSE FROM tax_zones WHERE code = 'UK'
ON CONFLICT DO NOTHING;
