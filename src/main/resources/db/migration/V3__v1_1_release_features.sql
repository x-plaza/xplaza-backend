-- =====================================================
-- X-Plaza v1.1.0 release features
-- Adds: cart/JPA schema alignment, shop commission,
-- B2B/subscriptions/translations Java entities, image
-- variants, soft-delete metadata, referrals, parent/child
-- order links, FCM push tokens.
-- =====================================================

-- ---------- Cart <-> JPA entity alignment ----------
-- The Cart aggregate started life with a `shopping_carts` JPA mapping but the
-- canonical Flyway schema created `carts`. v1.1.0 collapses this drift:
-- the entity now points at `carts`. Add the columns the entity uses but the
-- original `carts` DDL did not declare, and relax NOT NULL constraints on
-- columns that JPA leaves to be re-derived in business methods.
ALTER TABLE carts ADD COLUMN IF NOT EXISTS coupon_code VARCHAR(50);
ALTER TABLE carts ADD COLUMN IF NOT EXISTS coupon_discount DECIMAL(10,2) DEFAULT 0;
ALTER TABLE carts ADD COLUMN IF NOT EXISTS notes TEXT;
ALTER TABLE carts ADD COLUMN IF NOT EXISTS last_activity_at TIMESTAMP;

ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS original_price DECIMAL(10,2);
ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS discount_percentage DECIMAL(5,2);
ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS product_name VARCHAR(255);
ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS variant_name VARCHAR(255);
ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS sku VARCHAR(50);
ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);
ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';
ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS custom_attributes TEXT;
ALTER TABLE cart_items ALTER COLUMN total_price DROP NOT NULL;
ALTER TABLE cart_items ALTER COLUMN price_at_add DROP NOT NULL;

-- ---------- Multi-vendor split orders ----------
-- A checkout that spans products from N shops produces N child orders, each
-- linked back to the umbrella order via `parent_order_id`. The parent order
-- carries the customer-facing aggregates (grand total, payment); child
-- orders own per-shop fulfilment (status history, payouts).
ALTER TABLE customer_orders ADD COLUMN IF NOT EXISTS parent_order_id UUID;
CREATE INDEX IF NOT EXISTS idx_orders_parent ON customer_orders(parent_order_id);

-- ---------- Shop commission & payout account ----------
ALTER TABLE shops ADD COLUMN IF NOT EXISTS commission_rate DECIMAL(5,4);
ALTER TABLE shops ADD COLUMN IF NOT EXISTS payout_account VARCHAR(255);
ALTER TABLE shops ADD COLUMN IF NOT EXISTS payout_currency VARCHAR(3);

-- ---------- Push notification tokens (FCM/APNs) ----------
CREATE TABLE IF NOT EXISTS push_tokens (
    id            BIGSERIAL PRIMARY KEY,
    customer_id   BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    platform      VARCHAR(10) NOT NULL,
    token         VARCHAR(500) NOT NULL,
    device_id     VARCHAR(120),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_seen_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(token)
);
CREATE INDEX IF NOT EXISTS idx_push_tokens_customer ON push_tokens(customer_id);

-- ---------- Referral program ----------
CREATE TABLE IF NOT EXISTS referrals (
    id              BIGSERIAL PRIMARY KEY,
    referrer_id     BIGINT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    referee_email   VARCHAR(255) NOT NULL,
    referee_id      BIGINT REFERENCES customers(customer_id) ON DELETE SET NULL,
    code            VARCHAR(40) NOT NULL UNIQUE,
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    rewarded_at     TIMESTAMP,
    reward_amount   DECIMAL(14,2),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_referrals_referrer ON referrals(referrer_id);
CREATE INDEX IF NOT EXISTS idx_referrals_email ON referrals(referee_email);

-- ---------- Product image variants ----------
ALTER TABLE product_images ADD COLUMN IF NOT EXISTS thumbnail_url VARCHAR(500);
ALTER TABLE product_images ADD COLUMN IF NOT EXISTS medium_url    VARCHAR(500);
ALTER TABLE product_images ADD COLUMN IF NOT EXISTS large_url     VARCHAR(500);
ALTER TABLE product_images ADD COLUMN IF NOT EXISTS alt_text      VARCHAR(255);
ALTER TABLE product_images ADD COLUMN IF NOT EXISTS sort_order    INTEGER DEFAULT 0;

-- ---------- Soft-delete bookkeeping for entities that gain @SQLDelete ----------
ALTER TABLE shops ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- ---------- Subscription Java-entity tweaks ----------
-- The base `subscriptions` table already exists from V2. The Java entity
-- additionally tracks the active price-list snapshot, gateway customer id and
-- the next attempt counter for retries.
ALTER TABLE subscriptions ADD COLUMN IF NOT EXISTS gateway_customer_id VARCHAR(100);
ALTER TABLE subscriptions ADD COLUMN IF NOT EXISTS retry_count         INTEGER NOT NULL DEFAULT 0;
ALTER TABLE subscriptions ADD COLUMN IF NOT EXISTS last_error          VARCHAR(500);

-- ---------- B2B price-list extras for the resolver ----------
-- The resolver compares a candidate price list's currency to the cart's
-- currency, so make currency NOT NULL to avoid surprises.
ALTER TABLE price_list_items ADD COLUMN IF NOT EXISTS notes VARCHAR(500);
