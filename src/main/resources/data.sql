-- ============================================================
-- Seed data for local development (H2 in-memory, MODE=PostgreSQL)
-- Runs after Hibernate ddl-auto creates the schema.
-- ============================================================

SET SCHEMA xplaza;

-- ============================================================
-- 1. Currencies
-- ============================================================
INSERT INTO currencies (currency_name, currency_sign) VALUES ('US Dollar', '$');
INSERT INTO currencies (currency_name, currency_sign) VALUES ('Euro', '€');

-- ============================================================
-- 2. Product Variation Types
-- ============================================================
INSERT INTO product_variation_types (var_type_name, var_type_description) VALUES ('Size', 'Product size variations');
INSERT INTO product_variation_types (var_type_name, var_type_description) VALUES ('Color', 'Product color variations');

-- ============================================================
-- 3. Categories  (parent → child)
-- ============================================================
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Fashion', 'Clothing, shoes and accessories', NULL);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Electronics', 'Tech gadgets and devices', NULL);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Home & Living', 'Furniture and home decor', NULL);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Sports', 'Sportswear and equipment', NULL);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Beauty', 'Skincare and cosmetics', NULL);

-- Sub-categories (parent_category = id of parent)
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Men''s Clothing', 'Clothing for men', 1);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Women''s Clothing', 'Clothing for women', 1);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Shoes', 'Footwear for all', 1);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Smartphones', 'Mobile phones', 2);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Laptops', 'Notebook computers', 2);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Furniture', 'Home and office furniture', 3);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Running', 'Running gear', 4);
INSERT INTO categories (category_name, category_description, parent_category) VALUES ('Skincare', 'Skin care products', 5);

-- ============================================================
-- 4. Brands
-- ============================================================
INSERT INTO brands (brand_name, brand_description) VALUES ('Nike', 'Just Do It — Global sportswear leader');
INSERT INTO brands (brand_name, brand_description) VALUES ('Adidas', 'Impossible Is Nothing — German sportswear brand');
INSERT INTO brands (brand_name, brand_description) VALUES ('Samsung', 'South Korean electronics giant');
INSERT INTO brands (brand_name, brand_description) VALUES ('Apple', 'Think Different — Premium tech products');
INSERT INTO brands (brand_name, brand_description) VALUES ('IKEA', 'Swedish furniture and home furnishings');
INSERT INTO brands (brand_name, brand_description) VALUES ('Zara', 'Spanish fast-fashion retailer');
INSERT INTO brands (brand_name, brand_description) VALUES ('The Ordinary', 'Science-backed affordable skincare');
INSERT INTO brands (brand_name, brand_description) VALUES ('Levi''s', 'Iconic American denim brand');

-- ============================================================
-- 5. Shops (vendors)
-- ============================================================
INSERT INTO shops (shop_name, shop_description, shop_address, shop_owner, commission_rate) VALUES ('SportZone', 'Premium sports equipment and apparel', '123 Sport Ave, New York, NY', 'John Smith', 0.10);
INSERT INTO shops (shop_name, shop_description, shop_address, shop_owner, commission_rate) VALUES ('TechHub', 'Latest electronics and gadgets', '456 Tech Blvd, San Francisco, CA', 'Jane Doe', 0.08);
INSERT INTO shops (shop_name, shop_description, shop_address, shop_owner, commission_rate) VALUES ('StyleHouse', 'Trending fashion for everyone', '789 Fashion St, Los Angeles, CA', 'Emily Chen', 0.12);
INSERT INTO shops (shop_name, shop_description, shop_address, shop_owner, commission_rate) VALUES ('HomeNest', 'Everything for your home', '321 Home Lane, Chicago, IL', 'Michael Brown', 0.09);
INSERT INTO shops (shop_name, shop_description, shop_address, shop_owner, commission_rate) VALUES ('GlowUp', 'Beauty and skincare essentials', '555 Beauty Rd, Miami, FL', 'Sarah Kim', 0.11);

-- ============================================================
-- 6. Products
-- ============================================================
-- Nike Air Max 90  (brand=1/Nike, category=8/Shoes, shop=1/SportZone, currency=1/USD)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Nike Air Max 90', 'Iconic running shoes with visible Air cushioning. A classic silhouette reimagined for modern comfort.', 1, 8, 1, 10, 89.99, 129.99, 1, 1, 50, 1, CURRENT_TIMESTAMP, 'nike-air-max-90', TRUE, 4.5, 128, 'unisex');

-- Adidas Ultraboost 23  (brand=2/Adidas, category=12/Running, shop=1/SportZone)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Adidas Ultraboost 23', 'Premium running shoes with responsive BOOST midsole. Engineered Primeknit+ upper for a precision fit.', 2, 12, 1, 11, 120.00, 189.99, 1, 1, 35, 1, CURRENT_TIMESTAMP, 'adidas-ultraboost-23', TRUE, 4.7, 95, 'unisex');

-- Samsung Galaxy S24 Ultra  (brand=3/Samsung, category=9/Smartphones, shop=2/TechHub)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Samsung Galaxy S24 Ultra', 'Flagship smartphone with Galaxy AI, 200MP camera, S Pen, and titanium frame. The most powerful Galaxy yet.', 3, 9, NULL, NULL, 899.00, 1299.99, 1, 2, 25, 1, CURRENT_TIMESTAMP, 'samsung-galaxy-s24-ultra', TRUE, 4.6, 234, 'unisex');

-- Apple MacBook Pro 16"  (brand=4/Apple, category=10/Laptops, shop=2/TechHub)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Apple MacBook Pro 16"', 'Professional laptop with M3 Pro chip, 18GB unified memory, and stunning Liquid Retina XDR display.', 4, 10, NULL, NULL, 1800.00, 2499.99, 1, 2, 15, 1, CURRENT_TIMESTAMP, 'apple-macbook-pro-16', TRUE, 4.8, 312, 'unisex');

-- Zara Oversized Blazer  (brand=6/Zara, category=7/Women's Clothing, shop=3/StyleHouse)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Zara Oversized Blazer', 'Relaxed-fit blazer in premium wool blend. Features padded shoulders and flap pockets. Perfect for layering.', 6, 7, 1, 4, 45.00, 89.99, 1, 3, 60, 1, CURRENT_TIMESTAMP, 'zara-oversized-blazer', TRUE, 4.3, 67, 'women');

-- Levi's 501 Original Jeans  (brand=8/Levi's, category=6/Men's Clothing, shop=3/StyleHouse)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Levi''s 501 Original Jeans', 'The original straight fit jean since 1873. Button fly, sits at waist, regular through thigh. 100% cotton denim.', 8, 6, 1, 6, 40.00, 79.99, 1, 3, 80, 1, CURRENT_TIMESTAMP, 'levis-501-original-jeans', TRUE, 4.4, 189, 'men');

-- IKEA KALLAX Shelf Unit  (brand=5/IKEA, category=11/Furniture, shop=4/HomeNest)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('IKEA KALLAX Shelf Unit', 'Versatile shelving unit with 8 compartments. Can be used as room divider, bookshelf or storage. White finish.', 5, 11, NULL, NULL, 49.00, 99.99, 1, 4, 40, 1, CURRENT_TIMESTAMP, 'ikea-kallax-shelf-unit', TRUE, 4.2, 156, 'unisex');

-- Nike Dri-FIT Training Tee  (brand=1/Nike, category=6/Men's Clothing, shop=1/SportZone)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Nike Dri-FIT Training Tee', 'Moisture-wicking training t-shirt with Dri-FIT technology. Lightweight and breathable for intense workouts.', 1, 6, 1, 5, 15.00, 34.99, 1, 1, 120, 1, CURRENT_TIMESTAMP, 'nike-dri-fit-training-tee', TRUE, 4.1, 78, 'men');

-- The Ordinary Niacinamide Serum  (brand=7/The Ordinary, category=13/Skincare, shop=5/GlowUp)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('The Ordinary Niacinamide 10% + Zinc 1%', 'High-strength vitamin and mineral formula for blemish-prone skin. Reduces appearance of pores and oil.', 7, 13, NULL, NULL, 3.50, 12.99, 1, 5, 200, 1, CURRENT_TIMESTAMP, 'the-ordinary-niacinamide-serum', TRUE, 4.6, 423, 'unisex');

-- Samsung 55" OLED TV  (brand=3/Samsung, category=2/Electronics, shop=2/TechHub)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Samsung 55" OLED 4K Smart TV', 'Stunning OLED display with self-lit pixels, Dolby Atmos, and AI-powered 4K upscaling. Smart Hub built-in.', 3, 2, NULL, NULL, 900.00, 1499.99, 1, 2, 20, 1, CURRENT_TIMESTAMP, 'samsung-55-oled-4k-tv', TRUE, 4.5, 87, 'unisex');

-- Apple iPhone 15 Pro  (brand=4/Apple, category=9/Smartphones, shop=2/TechHub)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Apple iPhone 15 Pro', 'Pro-grade smartphone with A17 Pro chip, 48MP camera system, titanium design, and USB-C.', 4, 9, NULL, NULL, 799.00, 1099.99, 1, 2, 30, 1, CURRENT_TIMESTAMP, 'apple-iphone-15-pro', TRUE, 4.7, 567, 'unisex');

-- Adidas Essentials Hoodie  (brand=2/Adidas, category=6/Men's Clothing, shop=1/SportZone)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Adidas Essentials Hoodie', 'Comfortable fleece hoodie with kangaroo pocket. Ribbed cuffs and hem. Iconic 3-stripes on sleeve.', 2, 6, 1, 4, 30.00, 59.99, 1, 1, 70, 1, CURRENT_TIMESTAMP, 'adidas-essentials-hoodie', TRUE, 4.3, 142, 'men');

-- Zara Midi Dress  (brand=6/Zara, category=7/Women's Clothing, shop=3/StyleHouse)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Zara Satin Midi Dress', 'Elegant satin midi dress with cowl neckline and adjustable straps. Side slit detail. Machine washable.', 6, 7, 1, 4, 35.00, 69.99, 1, 3, 45, 1, CURRENT_TIMESTAMP, 'zara-satin-midi-dress', TRUE, 4.5, 93, 'women');

-- IKEA MALM Desk  (brand=5/IKEA, category=11/Furniture, shop=4/HomeNest)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('IKEA MALM Desk', 'Modern desk with pull-out panel for laptop. Cable management shelf on back. Oak veneer finish, 140x65 cm.', 5, 11, NULL, NULL, 89.00, 179.99, 1, 4, 30, 1, CURRENT_TIMESTAMP, 'ikea-malm-desk', TRUE, 4.0, 78, 'unisex');

-- The Ordinary Hyaluronic Acid  (brand=7/The Ordinary, category=13/Skincare, shop=5/GlowUp)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('The Ordinary Hyaluronic Acid 2% + B5', 'Multi-weight hyaluronic acid serum with vitamin B5. Hydrates skin surface and deeper layers.', 7, 13, NULL, NULL, 4.00, 13.99, 1, 5, 180, 1, CURRENT_TIMESTAMP, 'the-ordinary-hyaluronic-acid', TRUE, 4.4, 356, 'unisex');

-- Nike Air Force 1  (brand=1/Nike, category=8/Shoes, shop=1/SportZone)
INSERT INTO products (product_name, product_description, fk_brand_id, fk_category_id, fk_product_var_type_id, product_var_type_value, product_buying_price, product_selling_price, fk_currency_id, fk_shop_id, quantity, created_by, created_at, slug, is_published, average_rating, review_count, gender) VALUES
('Nike Air Force 1 ''07', 'The radiance lives on in the Nike Air Force 1. Stitched overlays on the upper add heritage style and durability.', 1, 8, 1, 12, 65.00, 109.99, 1, 1, 90, 1, CURRENT_TIMESTAMP, 'nike-air-force-1-07', TRUE, 4.6, 892, 'unisex');

-- ============================================================
-- 7. Product Images (using placeholder URLs from picsum.photos)
-- ============================================================
-- Nike Air Max 90 (product_id=1)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(1, 'nike-air-max-90-1.jpg', 'https://images.unsplash.com/photo-1605348532760-6753d2c43329?w=600', 'Nike Air Max 90 - Side View', 1, 1, CURRENT_TIMESTAMP);
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(1, 'nike-air-max-90-2.jpg', 'https://images.unsplash.com/photo-1605348532760-6753d2c43329?w=600', 'Nike Air Max 90 - Top View', 2, 1, CURRENT_TIMESTAMP);

-- Adidas Ultraboost 23 (product_id=2)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(2, 'adidas-ultraboost-1.jpg', 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=600', 'Adidas Ultraboost 23 - Side View', 1, 1, CURRENT_TIMESTAMP);

-- Samsung Galaxy S24 Ultra (product_id=3)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(3, 'galaxy-s24-ultra-1.jpg', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600', 'Samsung Galaxy S24 Ultra - Front', 1, 1, CURRENT_TIMESTAMP);

-- Apple MacBook Pro 16" (product_id=4)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(4, 'macbook-pro-16-1.jpg', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600', 'Apple MacBook Pro 16 inch - Open', 1, 1, CURRENT_TIMESTAMP);

-- Zara Oversized Blazer (product_id=5)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(5, 'zara-blazer-1.jpg', 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=600', 'Zara Oversized Blazer - Front', 1, 1, CURRENT_TIMESTAMP);

-- Levi's 501 (product_id=6)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(6, 'levis-501-1.jpg', 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=600', 'Levi''s 501 Original Jeans', 1, 1, CURRENT_TIMESTAMP);

-- IKEA KALLAX (product_id=7)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(7, 'ikea-kallax-1.jpg', 'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=600', 'IKEA KALLAX Shelf Unit', 1, 1, CURRENT_TIMESTAMP);

-- Nike Dri-FIT (product_id=8)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(8, 'nike-drifit-1.jpg', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600', 'Nike Dri-FIT Training Tee', 1, 1, CURRENT_TIMESTAMP);

-- The Ordinary Niacinamide (product_id=9)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(9, 'ordinary-niacinamide-1.jpg', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600', 'The Ordinary Niacinamide Serum', 1, 1, CURRENT_TIMESTAMP);

-- Samsung 55" OLED (product_id=10)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(10, 'samsung-oled-tv-1.jpg', 'https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=600', 'Samsung 55 inch OLED 4K TV', 1, 1, CURRENT_TIMESTAMP);

-- Apple iPhone 15 Pro (product_id=11)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(11, 'iphone-15-pro-1.jpg', 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600', 'Apple iPhone 15 Pro', 1, 1, CURRENT_TIMESTAMP);

-- Adidas Essentials Hoodie (product_id=12)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(12, 'adidas-hoodie-1.jpg', 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600', 'Adidas Essentials Hoodie', 1, 1, CURRENT_TIMESTAMP);

-- Zara Satin Midi Dress (product_id=13)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(13, 'zara-dress-1.jpg', 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=600', 'Zara Satin Midi Dress', 1, 1, CURRENT_TIMESTAMP);

-- IKEA MALM Desk (product_id=14)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(14, 'ikea-malm-desk-1.jpg', 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=600', 'IKEA MALM Desk', 1, 1, CURRENT_TIMESTAMP);

-- The Ordinary Hyaluronic Acid (product_id=15)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(15, 'ordinary-ha-1.jpg', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600', 'The Ordinary Hyaluronic Acid', 1, 1, CURRENT_TIMESTAMP);

-- Nike Air Force 1 (product_id=16)
INSERT INTO product_images (fk_product_id, product_image_name, product_image_path, alt_text, sort_order, created_by, created_at) VALUES
(16, 'nike-af1-1.jpg', 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=600', 'Nike Air Force 1 07', 1, 1, CURRENT_TIMESTAMP);

-- ============================================================
-- 8. Customers (password is BCrypt hash of "password123")
-- ============================================================
INSERT INTO customers (first_name, last_name, email, password, phone_number, role, enabled, verified_email, verified_email_at, failed_login_attempts, mfa_enabled, loyalty_points, loyalty_tier, store_credit, created_at) VALUES
('Demo', 'User', 'demo@xplaza.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '+1234567890', 'CUSTOMER', TRUE, TRUE, CURRENT_TIMESTAMP, 0, FALSE, 0, 'BRONZE', 0.00, CURRENT_TIMESTAMP);

INSERT INTO customers (first_name, last_name, email, password, phone_number, role, enabled, verified_email, verified_email_at, failed_login_attempts, mfa_enabled, loyalty_points, loyalty_tier, store_credit, created_at) VALUES
('Alice', 'Johnson', 'alice@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '+1987654321', 'CUSTOMER', TRUE, TRUE, CURRENT_TIMESTAMP, 0, FALSE, 0, 'BRONZE', 0.00, CURRENT_TIMESTAMP);

-- ============================================================
-- 9. Customer Addresses
-- ============================================================
INSERT INTO customer_addresses (customer_id, type, is_default, label, first_name, last_name, address_line1, city, state, postal_code, country_code, phone, is_verified, created_at, updated_at) VALUES
(1, 'BOTH', TRUE, 'Home', 'Demo', 'User', '123 Main Street', 'New York', 'NY', '10001', 'US', '+1234567890', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO customer_addresses (customer_id, type, is_default, label, first_name, last_name, address_line1, address_line2, city, state, postal_code, country_code, phone, is_verified, created_at, updated_at) VALUES
(1, 'SHIPPING', FALSE, 'Office', 'Demo', 'User', '456 Business Ave', 'Suite 200', 'New York', 'NY', '10002', 'US', '+1234567890', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 10. Attributes (if not created by V1 migration — for H2 local)
-- ============================================================
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position) VALUES ('Color', 'color', 'SELECT', TRUE, TRUE, TRUE, 1);
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position) VALUES ('Size', 'size', 'SELECT', TRUE, TRUE, TRUE, 2);
INSERT INTO attributes (name, code, type, is_variant_attribute, is_filterable, is_searchable, position) VALUES ('Material', 'material', 'SELECT', FALSE, TRUE, FALSE, 3);

-- Attribute values for Color (attribute_id=1)
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (1, 'Black', 'black', 1);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (1, 'White', 'white', 2);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (1, 'Red', 'red', 3);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (1, 'Blue', 'blue', 4);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (1, 'Grey', 'grey', 5);

-- Attribute values for Size (attribute_id=2)
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (2, 'XS', 'xs', 1);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (2, 'S', 's', 2);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (2, 'M', 'm', 3);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (2, 'L', 'l', 4);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (2, 'XL', 'xl', 5);
INSERT INTO attribute_values (attribute_id, display_value, code, position) VALUES (2, 'XXL', 'xxl', 6);

-- ============================================================
-- 11. Reviews (a few sample reviews)
-- ============================================================
INSERT INTO reviews (review_id, product_id, customer_id, shop_id, status, rating_overall, title, body, is_verified_purchase, published_at, created_at, updated_at) VALUES
(RANDOM_UUID(), 1, 1, 1, 'APPROVED', 5, 'Best sneakers ever!', 'Super comfortable and stylish. The Air cushioning makes a huge difference for all-day wear.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reviews (review_id, product_id, customer_id, shop_id, status, rating_overall, title, body, is_verified_purchase, published_at, created_at, updated_at) VALUES
(RANDOM_UUID(), 1, 2, 1, 'APPROVED', 4, 'Classic design, great comfort', 'Love the retro look. Runs a bit narrow though, size up half a size if you have wide feet.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reviews (review_id, product_id, customer_id, shop_id, status, rating_overall, title, body, is_verified_purchase, published_at, created_at, updated_at) VALUES
(RANDOM_UUID(), 3, 1, 2, 'APPROVED', 5, 'Galaxy AI is a game changer', 'The AI features alone are worth the upgrade. Camera quality is insane, especially in low light.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reviews (review_id, product_id, customer_id, shop_id, status, rating_overall, title, body, is_verified_purchase, published_at, created_at, updated_at) VALUES
(RANDOM_UUID(), 4, 2, 2, 'APPROVED', 5, 'Worth every penny', 'The M3 Pro chip handles everything I throw at it. Battery life is exceptional.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reviews (review_id, product_id, customer_id, shop_id, status, rating_overall, title, body, is_verified_purchase, published_at, created_at, updated_at) VALUES
(RANDOM_UUID(), 9, 1, 5, 'APPROVED', 5, 'Holy grail product', 'This serum transformed my skin. Pores visibly smaller within 2 weeks. Amazing value for the price.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reviews (review_id, product_id, customer_id, shop_id, status, rating_overall, title, body, is_verified_purchase, published_at, created_at, updated_at) VALUES
(RANDOM_UUID(), 16, 2, 1, 'APPROVED', 5, 'Timeless classic', 'You can never go wrong with AF1s. Clean, versatile, and comfortable. A wardrobe staple.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
