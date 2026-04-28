-- Add gender column to products table
ALTER TABLE products ADD COLUMN gender VARCHAR(20);

-- Set gender values for existing products
-- Shoes & sportswear: unisex
UPDATE products SET gender = 'unisex' WHERE slug IN ('nike-air-max-90', 'adidas-ultraboost-23', 'nike-air-force-1-07');
-- Men's clothing
UPDATE products SET gender = 'men' WHERE slug IN ('levis-501-original-jeans', 'nike-dri-fit-training-tee', 'adidas-essentials-hoodie');
-- Women's clothing
UPDATE products SET gender = 'women' WHERE slug IN ('zara-oversized-blazer', 'zara-satin-midi-dress');
-- Skincare: unisex
UPDATE products SET gender = 'unisex' WHERE slug IN ('the-ordinary-niacinamide-serum', 'the-ordinary-hyaluronic-acid');
-- Electronics & furniture: unisex
UPDATE products SET gender = 'unisex' WHERE slug IN ('samsung-galaxy-s24-ultra', 'apple-macbook-pro-16', 'ikea-kallax-shelf-unit', 'samsung-55-oled-4k-tv', 'apple-iphone-15-pro', 'ikea-malm-desk');
