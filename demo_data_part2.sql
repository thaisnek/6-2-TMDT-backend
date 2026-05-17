-- ============================================================
-- DEMO DATA - WEB BAN DO CHOI & SAN PHAM TRE EM - PHAN 2
-- Database: tmdt | Chay SAU demo_data_part1.sql
-- ============================================================
USE tmdt;

-- ==================== 8. TYPE VOUCHERS (4 loai) ====================
INSERT INTO type_vouchers (id, type_voucher, value, max_value, min_value, created_at, updated_at) VALUES
(1, 'PERCENTAGE', 10.00, 50000.00, 200000.00, NOW(), NOW()),
(2, 'PERCENTAGE', 20.00, 100000.00, 500000.00, NOW(), NOW()),
(3, 'FIXED_AMOUNT', 30000.00, NULL, 150000.00, NOW(), NOW()),
(4, 'FIXED_AMOUNT', 50000.00, NULL, 300000.00, NOW(), NOW());

-- ==================== 9. VOUCHERS (5 ma) ====================
INSERT INTO vouchers (id, type_voucher_id, from_date, to_date, quantity, code_voucher, created_at, updated_at) VALUES
(1, 1, '2025-01-01 00:00:00', '2026-12-31 23:59:59', 100, 'BABY10', NOW(), NOW()),
(2, 2, '2025-06-01 00:00:00', '2026-12-31 23:59:59', 50, 'KIDVIP20', NOW(), NOW()),
(3, 3, '2025-01-01 00:00:00', '2026-12-31 23:59:59', 200, 'GIAM30K', NOW(), NOW()),
(4, 4, '2026-05-01 00:00:00', '2026-06-30 23:59:59', 80, 'HEQUA50K', NOW(), NOW()),
(5, 1, '2026-06-01 00:00:00', '2026-06-01 23:59:59', 500, 'TETTHIEUNHI', NOW(), NOW());

-- ==================== 10. LOYALTY POLICY ====================
INSERT INTO loyalty_policies (id, amount_per_point, point_value, enabled, created_at, updated_at) VALUES
(1, 10000.00, 1000.00, true, NOW(), NOW());

-- ==================== 11. LOYALTY ACCOUNTS ====================
INSERT INTO loyalty_accounts (user_id, current_points, lifetime_earned_points, lifetime_spent_points) VALUES
(5, 250, 300, 50),
(6, 180, 180, 0),
(7, 45, 45, 0),
(8, 0, 0, 0);

-- ==================== 12. CARTS ====================
INSERT INTO carts (id, customer_id, created_at, updated_at) VALUES
(1, 5, NOW(), NOW()),
(2, 6, NOW(), NOW()),
(3, 7, NOW(), NOW()),
(4, 8, NOW(), NOW());

-- ==================== 13. CART ITEMS ====================
INSERT INTO cart_items (id, cart_id, variant_id, quantity) VALUES
(1, 3, 11, 1),
(2, 3, 29, 2),
(3, 4, 2, 1),
(4, 4, 16, 3);

-- ==================== 14. ORDERS (8 don hang) ====================
INSERT INTO orders (id, order_code, customer_id, voucher_id, shipping_name, shipping_address, shipping_phone, order_status, payment_status, shipping_status, subtotal, discount_amount, shipping_fee, total_amount, cancel_reason, created_at, updated_at) VALUES
-- customer1: 3 don
(1, 'ORD20260501001', 5, 1, 'Lê Thị Hằng', '123 Nguyễn Trãi, Q.1, TP.HCM', '0912345678',
 'COMPLETED', 'PAID', 'DELIVERED', 1540000.00, 50000.00, 30000.00, 1520000.00, NULL, '2026-05-01 10:00:00', '2026-05-05 14:00:00'),
(2, 'ORD20260510002', 5, NULL, 'Lê Thị Hằng', '123 Nguyễn Trãi, Q.1, TP.HCM', '0912345678',
 'SHIPPING', 'PAID', 'SHIPPING', 450000.00, 0.00, 0.00, 450000.00, NULL, '2026-05-10 08:30:00', '2026-05-12 09:00:00'),
(3, 'ORD20260508003', 5, NULL, 'Lê Thị Hằng', '456 Lê Lợi, Q.3, TP.HCM', '0912345678',
 'CANCELLED', 'REFUNDED', 'PENDING', 890000.00, 0.00, 30000.00, 920000.00, 'Bé nhà mình đã có bộ LEGO này rồi', '2026-05-08 09:00:00', '2026-05-08 10:00:00'),
-- customer2: 3 don
(4, 'ORD20260502004', 6, 3, 'Phạm Minh Tuấn', '789 Trần Hưng Đạo, Q.5, TP.HCM', '0987654321',
 'COMPLETED', 'PAID', 'DELIVERED', 600000.00, 30000.00, 30000.00, 600000.00, NULL, '2026-05-02 16:00:00', '2026-05-06 10:00:00'),
(5, 'ORD20260514005', 6, NULL, 'Phạm Minh Tuấn', '789 Trần Hưng Đạo, Q.5, TP.HCM', '0987654321',
 'CONFIRMED', 'PAID', 'ASSIGNED', 700000.00, 0.00, 30000.00, 730000.00, NULL, '2026-05-14 11:00:00', '2026-05-14 15:00:00'),
(6, 'ORD20260515006', 6, NULL, 'Phạm Minh Tuấn', '789 Trần Hưng Đạo, Q.5, TP.HCM', '0987654321',
 'PENDING', 'PENDING', 'PENDING', 350000.00, 0.00, 30000.00, 380000.00, NULL, '2026-05-15 20:00:00', '2026-05-15 20:00:00'),
-- customer3: 1 don
(7, 'ORD20260503007', 7, NULL, 'Hoàng Thị Mai', '321 Hai Bà Trưng, Q.Bình Thạnh, TP.HCM', '0933445566',
 'COMPLETED', 'PAID', 'DELIVERED', 500000.00, 0.00, 0.00, 500000.00, NULL, '2026-05-03 14:00:00', '2026-05-07 16:00:00'),
-- customer4: 1 don
(8, 'ORD20260516008', 8, 4, 'Võ Đức Anh', '55 Phạm Văn Đồng, Q.Thủ Đức, TP.HCM', '0911223344',
 'PENDING', 'PENDING', 'PENDING', 405000.00, 50000.00, 30000.00, 385000.00, NULL, '2026-05-16 09:00:00', '2026-05-16 09:00:00');

-- ==================== 15. ORDER ITEMS (14 dong) ====================
INSERT INTO order_items (id, order_id, variant_id, product_name_snapshot, color_snapshot, size_snapshot, unit_price, quantity, line_total, created_at, updated_at) VALUES
-- Don 1: LEGO City + Gau bong nau
(1, 1, 1, 'Bộ xếp hình LEGO City 60316', NULL, NULL, 890000.00, 1, 890000.00, '2026-05-01 10:00:00', '2026-05-01 10:00:00'),
(2, 1, 11, 'Gấu bông Teddy Bear 50cm', 'Nâu', '50cm', 280000.00, 1, 280000.00, '2026-05-01 10:00:00', '2026-05-01 10:00:00'),
(3, 1, 22, 'Đầm công chúa bé gái', 'Hồng', 'M (4-5T)', 350000.00, 1, 350000.00, '2026-05-01 10:00:00', '2026-05-01 10:00:00'),
-- Don 2: Xe scooter do
(4, 2, 7, 'Xe scooter 3 bánh cho bé', 'Đỏ', NULL, 450000.00, 1, 450000.00, '2026-05-10 08:30:00', '2026-05-10 08:30:00'),
-- Don 3: LEGO City (da huy)
(5, 3, 1, 'Bộ xếp hình LEGO City 60316', NULL, NULL, 890000.00, 1, 890000.00, '2026-05-08 09:00:00', '2026-05-08 09:00:00'),
-- Don 4: Do choi bac si hong + Bang chu cai
(6, 4, 5, 'Bộ đồ chơi bác sĩ 15 món', 'Hồng', NULL, 250000.00, 1, 250000.00, '2026-05-02 16:00:00', '2026-05-02 16:00:00'),
(7, 4, 4, 'Bảng chữ cái nam châm', 'Nhiều màu', NULL, 180000.00, 1, 180000.00, '2026-05-02 16:00:00', '2026-05-02 16:00:00'),
(8, 4, 29, 'Sách tô màu Thế giới động vật', NULL, NULL, 55000.00, 2, 110000.00, '2026-05-02 16:00:00', '2026-05-02 16:00:00'),
-- Don 5: Dam cong chua tim + Ao khung long
(9, 5, 24, 'Đầm công chúa bé gái', 'Tím', 'L (6-7T)', 380000.00, 1, 380000.00, '2026-05-14 11:00:00', '2026-05-14 11:00:00'),
(10, 5, 17, 'Áo thun bé trai in hình khủng long', 'Xanh lá', 'M (4-5T)', 120000.00, 2, 240000.00, '2026-05-14 11:00:00', '2026-05-14 11:00:00'),
(11, 5, 25, 'Quần short bé trai thể thao', 'Xanh navy', 'S (2-3T)', 95000.00, 1, 95000.00, '2026-05-14 11:00:00', '2026-05-14 11:00:00'),
-- Don 6: Dam cong chua hong S
(12, 6, 21, 'Đầm công chúa bé gái', 'Hồng', 'S (2-3T)', 350000.00, 1, 350000.00, '2026-05-15 20:00:00', '2026-05-15 20:00:00'),
-- Don 7: Xep hinh go + Tho bong
(13, 7, 3, 'Bộ xếp hình gỗ 100 chi tiết', 'Nhiều màu', NULL, 320000.00, 1, 320000.00, '2026-05-03 14:00:00', '2026-05-03 14:00:00'),
(14, 7, 14, 'Thỏ bông tai dài 40cm', 'Trắng', '40cm', 220000.00, 1, 220000.00, '2026-05-03 14:00:00', '2026-05-03 14:00:00'),
-- Don 8: Binh sua + But sap mau
(15, 8, 28, 'Bình sữa Pigeon 240ml', 'Trong suốt', '240ml', 285000.00, 1, 285000.00, '2026-05-16 09:00:00', '2026-05-16 09:00:00'),
(16, 8, 30, 'Bộ bút sáp màu 24 cây Crayola', 'Nhiều màu', '24 cây', 85000.00, 1, 85000.00, '2026-05-16 09:00:00', '2026-05-16 09:00:00');

-- ==================== 16. PAYMENTS (8 thanh toan) ====================
INSERT INTO payments (id, order_id, payment_method, payment_status, amount, provider_transaction_id, provider_name, paid_at, failure_reason, created_at, updated_at) VALUES
(1, 1, 'MOMO', 'PAID', 1520000.00, 'MOMO_TXN_001', 'MoMo', '2026-05-01 10:05:00', NULL, '2026-05-01 10:00:00', '2026-05-01 10:05:00'),
(2, 2, 'MOMO', 'PAID', 450000.00, 'MOMO_TXN_002', 'MoMo', '2026-05-10 08:35:00', NULL, '2026-05-10 08:30:00', '2026-05-10 08:35:00'),
(3, 3, 'MOMO', 'REFUNDED', 920000.00, 'MOMO_TXN_003', 'MoMo', '2026-05-08 09:05:00', NULL, '2026-05-08 09:00:00', '2026-05-08 10:00:00'),
(4, 4, 'COD', 'PAID', 600000.00, NULL, NULL, '2026-05-06 10:00:00', NULL, '2026-05-02 16:00:00', '2026-05-06 10:00:00'),
(5, 5, 'MOMO', 'PAID', 730000.00, 'MOMO_TXN_004', 'MoMo', '2026-05-14 11:05:00', NULL, '2026-05-14 11:00:00', '2026-05-14 11:05:00'),
(6, 6, 'COD', 'PENDING', 380000.00, NULL, NULL, NULL, NULL, '2026-05-15 20:00:00', '2026-05-15 20:00:00'),
(7, 7, 'COD', 'PAID', 500000.00, NULL, NULL, '2026-05-07 16:00:00', NULL, '2026-05-03 14:00:00', '2026-05-07 16:00:00'),
(8, 8, 'MOMO', 'PENDING', 385000.00, NULL, NULL, NULL, NULL, '2026-05-16 09:00:00', '2026-05-16 09:00:00');

-- ==================== 17. SHIPMENTS (7 van chuyen) ====================
INSERT INTO shipments (id, order_id, delivery_staff_id, shipment_status, assigned_at, shipped_at, delivered_at, failure_reason, created_at, updated_at) VALUES
(1, 1, 3, 'DELIVERED', '2026-05-02 08:00:00', '2026-05-03 09:00:00', '2026-05-05 14:00:00', NULL, '2026-05-01 10:00:00', '2026-05-05 14:00:00'),
(2, 2, 3, 'SHIPPING', '2026-05-11 08:00:00', '2026-05-12 09:00:00', NULL, NULL, '2026-05-10 08:30:00', '2026-05-12 09:00:00'),
(3, 3, NULL, 'PENDING', NULL, NULL, NULL, NULL, '2026-05-08 09:00:00', '2026-05-08 10:00:00'),
(4, 4, 4, 'DELIVERED', '2026-05-03 08:00:00', '2026-05-04 10:00:00', '2026-05-06 10:00:00', NULL, '2026-05-02 16:00:00', '2026-05-06 10:00:00'),
(5, 5, 3, 'ASSIGNED', '2026-05-14 15:00:00', NULL, NULL, NULL, '2026-05-14 11:00:00', '2026-05-14 15:00:00'),
(6, 6, NULL, 'PENDING', NULL, NULL, NULL, NULL, '2026-05-15 20:00:00', '2026-05-15 20:00:00'),
(7, 7, 4, 'DELIVERED', '2026-05-04 08:00:00', '2026-05-05 09:00:00', '2026-05-07 16:00:00', NULL, '2026-05-03 14:00:00', '2026-05-07 16:00:00');

-- ==================== 18. REVIEWS (6 danh gia) ====================
INSERT INTO reviews (id, product_id, customer_id, order_item_id, rating, comment, approved, created_at, updated_at) VALUES
(1, 1, 5, 1, 5, 'Bé nhà mình thích mê luôn! LEGO chính hãng, chi tiết đẹp, hướng dẫn rõ ràng.', true, '2026-05-06 10:00:00', '2026-05-06 10:00:00'),
(2, 8, 5, 2, 4, 'Gấu bông mềm mịn, nhưng giao hàng hơi lâu. Bé rất thích!', true, '2026-05-06 11:00:00', '2026-05-06 11:00:00'),
(3, 11, 5, 3, 5, 'Đầm rất xinh, con gái mặc đi tiệc sinh nhật ai cũng khen.', true, '2026-05-06 12:00:00', '2026-05-06 12:00:00'),
(4, 5, 6, 6, 5, 'Bé gái 4 tuổi nhà mình chơi suốt ngày, đồ chơi bác sĩ rất đáng yêu!', true, '2026-05-07 08:00:00', '2026-05-07 08:00:00'),
(5, 4, 6, 7, 4, 'Bảng chữ cái giúp bé học nhanh hơn, nhưng nam châm hơi yếu.', true, '2026-05-07 09:00:00', '2026-05-07 09:00:00'),
(6, 3, 7, 13, 5, 'Xếp hình gỗ chất lượng tốt, sơn an toàn, bé 3 tuổi chơi được.', false, '2026-05-08 10:00:00', '2026-05-08 10:00:00');

-- ==================== 19. VOUCHER USED (3 da su dung) ====================
INSERT INTO voucher_useds (id, user_id, voucher_id, status, created_at, updated_at) VALUES
(1, 5, 1, 'USED', '2026-05-01 10:00:00', '2026-05-01 10:00:00'),
(2, 6, 3, 'USED', '2026-05-02 16:00:00', '2026-05-02 16:00:00'),
(3, 8, 4, 'USED', '2026-05-16 09:00:00', '2026-05-16 09:00:00');

-- ==================== 20. LOYALTY TRANSACTIONS (7 giao dich) ====================
INSERT INTO loyalty_transactions (id, user_id, order_id, points_change, balance_after, type, note, created_at, updated_at) VALUES
(1, 5, 1, 152, 152, 'EARN', 'Tích điểm đơn hàng ORD20260501001', '2026-05-05 14:00:00', '2026-05-05 14:00:00'),
(2, 5, 2, 45, 197, 'EARN', 'Tích điểm đơn hàng ORD20260510002', '2026-05-12 09:00:00', '2026-05-12 09:00:00'),
(3, 5, NULL, -50, 250, 'SPEND', 'Đổi điểm lấy voucher GIAM30K', '2026-05-13 08:00:00', '2026-05-13 08:00:00'),
(4, 5, NULL, 100, 300, 'ADJUST', 'Admin thưởng điểm khách hàng thân thiết', '2026-05-14 08:00:00', '2026-05-14 08:00:00'),
(5, 6, 4, 60, 60, 'EARN', 'Tích điểm đơn hàng ORD20260502004', '2026-05-06 10:00:00', '2026-05-06 10:00:00'),
(6, 6, 5, 73, 133, 'EARN', 'Tích điểm đơn hàng ORD20260514005', '2026-05-14 15:00:00', '2026-05-14 15:00:00'),
(7, 7, 7, 50, 50, 'EARN', 'Tích điểm đơn hàng ORD20260503007', '2026-05-07 16:00:00', '2026-05-07 16:00:00');

-- ==================== 21. CONVERSATIONS & MESSAGES ====================
INSERT INTO conversations (id, user_one, user_two, created_at, updated_at) VALUES
(1, 5, 2, '2026-05-01 09:00:00', '2026-05-01 09:30:00'),
(2, 6, 2, '2026-05-14 10:00:00', '2026-05-14 10:20:00'),
(3, 8, 2, '2026-05-16 08:00:00', '2026-05-16 08:15:00');

INSERT INTO messages (id, user_id, conversation_id, content, created_at, updated_at) VALUES
(1, 5, 1, 'Shop ơi, bộ LEGO City có phù hợp bé 5 tuổi không ạ?', '2026-05-01 09:00:00', '2026-05-01 09:00:00'),
(2, 2, 1, 'Chào chị! Bộ LEGO City phù hợp bé từ 6 tuổi ạ. Bé 5 tuổi chị nên chọn LEGO Duplo nhé!', '2026-05-01 09:10:00', '2026-05-01 09:10:00'),
(3, 5, 1, 'Vậy cho mình đặt LEGO City cho bé lớn nhé, cảm ơn shop!', '2026-05-01 09:20:00', '2026-05-01 09:20:00'),
(4, 2, 1, 'Dạ được ạ! Chị đặt hàng trên web nhé. Cảm ơn chị!', '2026-05-01 09:30:00', '2026-05-01 09:30:00'),
(5, 6, 2, 'Mình muốn mua đầm công chúa cho bé gái 6 tuổi, size nào phù hợp ạ?', '2026-05-14 10:00:00', '2026-05-14 10:00:00'),
(6, 2, 2, 'Chào anh! Bé 6 tuổi anh chọn size L (6-7T) nhé. Có màu Tím rất đẹp ạ!', '2026-05-14 10:10:00', '2026-05-14 10:10:00'),
(7, 6, 2, 'OK shop, mình đặt luôn nhé!', '2026-05-14 10:20:00', '2026-05-14 10:20:00'),
(8, 8, 3, 'Shop ơi có mã giảm giá nào không ạ?', '2026-05-16 08:00:00', '2026-05-16 08:00:00'),
(9, 2, 3, 'Chào anh! Hiện tại có mã HEQUA50K giảm 50k cho đơn từ 300k, và mã TETTHIEUNHI giảm 10% nhân dịp 1/6 ạ!', '2026-05-16 08:10:00', '2026-05-16 08:10:00'),
(10, 8, 3, 'Tuyệt vời, cảm ơn shop!', '2026-05-16 08:15:00', '2026-05-16 08:15:00');

SELECT 'PART 2 DONE - All demo data loaded!' AS STATUS;
