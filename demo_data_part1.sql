-- ============================================================
-- DEMO DATA - WEB BAN DO CHOI & SAN PHAM TRE EM - PHAN 1
-- Database: tmdt | Password tat ca user: 123456
-- ============================================================
USE tmdt;

-- ==================== 1. USERS (8 users) ====================
INSERT INTO users (id, full_name, user_name, email, phone, password_hash, status, role, created_at, updated_at) VALUES
(1, 'Admin Hệ Thống', 'admin', 'admin@kidshop.com', '0901000001', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'ADMIN', NOW(), NOW()),
(2, 'Nguyễn Thị Lan', 'salesman', 'sales@kidshop.com', '0901000002', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'SALES_STAFF', NOW(), NOW()),
(3, 'Trần Văn Giao', 'shipper1', 'shipper1@kidshop.com', '0901000003', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'DELIVERY_STAFF', NOW(), NOW()),
(4, 'Phạm Văn Tài', 'shipper2', 'shipper2@kidshop.com', '0901000004', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'DELIVERY_STAFF', NOW(), NOW()),
(5, 'Lê Thị Hằng', 'customer1', 'hang@gmail.com', '0912345678', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'CUSTOMER', NOW(), NOW()),
(6, 'Phạm Minh Tuấn', 'customer2', 'tuan@gmail.com', '0987654321', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'CUSTOMER', NOW(), NOW()),
(7, 'Hoàng Thị Mai', 'customer3', 'mai@gmail.com', '0933445566', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'CUSTOMER', NOW(), NOW()),
(8, 'Võ Đức Anh', 'customer4', 'ducanh@gmail.com', '0911223344', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', 'CUSTOMER', NOW(), NOW());

-- ==================== 2. ADDRESS USERS (6 dia chi) ====================
INSERT INTO address_users (id, user_id, ship_name, ship_address, ship_email, ship_phone, is_default, created_at, updated_at) VALUES
(1, 5, 'Lê Thị Hằng', '123 Nguyễn Trãi, Q.1, TP.HCM', 'hang@gmail.com', '0912345678', true, NOW(), NOW()),
(2, 5, 'Lê Thị Hằng', '456 Lê Lợi, Q.3, TP.HCM', 'hang@gmail.com', '0912345678', false, NOW(), NOW()),
(3, 6, 'Phạm Minh Tuấn', '789 Trần Hưng Đạo, Q.5, TP.HCM', 'tuan@gmail.com', '0987654321', true, NOW(), NOW()),
(4, 7, 'Hoàng Thị Mai', '321 Hai Bà Trưng, Q.Bình Thạnh, TP.HCM', 'mai@gmail.com', '0933445566', true, NOW(), NOW()),
(5, 8, 'Võ Đức Anh', '55 Phạm Văn Đồng, Q.Thủ Đức, TP.HCM', 'ducanh@gmail.com', '0911223344', true, NOW(), NOW()),
(6, 8, 'Võ Đức Anh', '100 Lý Thường Kiệt, Q.10, TP.HCM', 'ducanh@gmail.com', '0911223344', false, NOW(), NOW());

-- ==================== 3. SUPPLIERS (5 nha cung cap) ====================
INSERT INTO suppliers (id, name, contact_person, phone, email, address, contract_info, active, created_at, updated_at) VALUES
(1, 'Công ty TNHH Đồ Chơi Sáng Tạo', 'Nguyễn Văn An', '0281111111', 'sangtao@gmail.com', '100 CMT8, Q.3, TP.HCM', 'HĐ 2024-2026, đồ chơi giáo dục', true, NOW(), NOW()),
(2, 'Nhà phân phối LEGO Việt Nam', 'Trần Bảo', '0282222222', 'legovn@gmail.com', '200 Nguyễn Huệ, Q.1, TP.HCM', 'HĐ 2025-2027, nhà phân phối chính hãng', true, NOW(), NOW()),
(3, 'Xưởng sản xuất Gỗ An Toàn', 'Lê Thị Cẩm', '0283333333', 'goantoan@gmail.com', '50 Quang Trung, Q.Gò Vấp, TP.HCM', 'HĐ 2025-2026, đồ chơi gỗ', true, NOW(), NOW()),
(4, 'Công ty Mẹ và Bé Việt', 'Phạm Duy', '0284444444', 'mevabe@gmail.com', '80 Lê Văn Sỹ, Q.Phú Nhuận, TP.HCM', 'HĐ 2024-2026, sản phẩm chăm sóc bé', true, NOW(), NOW()),
(5, 'Nhà phân phối Thời Trang Nhí', 'Hoàng Vy', '0285555555', 'thoitrangnhi@gmail.com', '30 Pasteur, Q.1, TP.HCM', 'HĐ 2025-2027, quần áo trẻ em', true, NOW(), NOW());

-- ==================== 4. CATEGORIES (14 danh muc) ====================
INSERT INTO categories (id, name, image_url, parent_id, active, created_at, updated_at) VALUES
-- Danh muc cha
(1, 'Đồ chơi', 'https://placehold.co/200x200?text=DoChoi', NULL, true, NOW(), NOW()),
(2, 'Quần áo trẻ em', 'https://placehold.co/200x200?text=QuanAo', NULL, true, NOW(), NOW()),
(3, 'Đồ dùng cho bé', 'https://placehold.co/200x200?text=DoDung', NULL, true, NOW(), NOW()),
(4, 'Sách & học liệu', 'https://placehold.co/200x200?text=Sach', NULL, true, NOW(), NOW()),
-- Danh muc con - Do choi
(5, 'Đồ chơi xếp hình', 'https://placehold.co/200x200?text=XepHinh', 1, true, NOW(), NOW()),
(6, 'Đồ chơi giáo dục', 'https://placehold.co/200x200?text=GiaoDuc', 1, true, NOW(), NOW()),
(7, 'Đồ chơi vận động', 'https://placehold.co/200x200?text=VanDong', 1, true, NOW(), NOW()),
(8, 'Thú nhồi bông', 'https://placehold.co/200x200?text=NhoiBong', 1, true, NOW(), NOW()),
-- Danh muc con - Quan ao
(9, 'Áo trẻ em', 'https://placehold.co/200x200?text=AoBe', 2, true, NOW(), NOW()),
(10, 'Quần trẻ em', 'https://placehold.co/200x200?text=QuanBe', 2, true, NOW(), NOW()),
-- Danh muc con - Do dung
(11, 'Bình sữa & Phụ kiện', 'https://placehold.co/200x200?text=BinhSua', 3, true, NOW(), NOW()),
(12, 'Xe đẩy & Ghế ngồi', 'https://placehold.co/200x200?text=XeDay', 3, true, NOW(), NOW()),
-- Danh muc con - Sach
(13, 'Sách tô màu', 'https://placehold.co/200x200?text=ToMau', 4, true, NOW(), NOW()),
(14, 'Đồ dùng học tập', 'https://placehold.co/200x200?text=HocTap', 4, true, NOW(), NOW());

-- ==================== 5. PRODUCTS (15 san pham) ====================
INSERT INTO products (id, name, description, brand, material, base_price, status, category_id, supplier_id, created_at, updated_at) VALUES
(1, 'Bộ xếp hình LEGO City 60316', 'Bộ LEGO đồn cảnh sát 468 chi tiết, phù hợp trẻ từ 6 tuổi.', 'LEGO', 'Nhựa ABS cao cấp', 890000.00, 'ACTIVE', 5, 2, NOW(), NOW()),
(2, 'Bộ xếp hình LEGO Duplo trang trại', 'Bộ LEGO Duplo 97 chi tiết, phù hợp trẻ từ 2 tuổi.', 'LEGO', 'Nhựa ABS cao cấp', 650000.00, 'ACTIVE', 5, 2, NOW(), NOW()),
(3, 'Bộ xếp hình gỗ 100 chi tiết', 'Bộ xếp hình gỗ tự nhiên đa dạng hình khối, sơn an toàn.', 'GoAn', 'Gỗ tự nhiên', 320000.00, 'ACTIVE', 5, 3, NOW(), NOW()),
(4, 'Bảng chữ cái nam châm', 'Bảng chữ cái tiếng Việt và tiếng Anh nam châm gắn tủ lạnh.', 'EduKids', 'Nhựa + nam châm', 180000.00, 'ACTIVE', 6, 1, NOW(), NOW()),
(5, 'Bộ đồ chơi bác sĩ 15 món', 'Bộ đồ chơi bác sĩ nhập vai với 15 dụng cụ y tế giả lập.', 'PlayFun', 'Nhựa an toàn', 250000.00, 'ACTIVE', 6, 1, NOW(), NOW()),
(6, 'Xe scooter 3 bánh cho bé', 'Xe scooter 3 bánh có đèn LED, chịu tải 50kg, cho bé 3-8 tuổi.', 'BabyRide', 'Hợp kim nhôm + nhựa', 450000.00, 'ACTIVE', 7, 1, NOW(), NOW()),
(7, 'Bóng rổ mini treo tường', 'Bộ bóng rổ mini treo tường kèm bóng, lắp đặt dễ dàng.', 'SportKid', 'Nhựa + lưới', 195000.00, 'ACTIVE', 7, 1, NOW(), NOW()),
(8, 'Gấu bông Teddy Bear 50cm', 'Gấu bông Teddy siêu mềm mịn, chất liệu lông nhung cao cấp.', 'CuddlePet', 'Bông + lông nhung', 280000.00, 'ACTIVE', 8, 1, NOW(), NOW()),
(9, 'Thỏ bông tai dài 40cm', 'Thỏ nhồi bông tai dài đáng yêu, an toàn cho bé sơ sinh.', 'CuddlePet', 'Bông organic', 220000.00, 'ACTIVE', 8, 1, NOW(), NOW()),
(10, 'Áo thun bé trai in hình khủng long', 'Áo thun cotton 100% in hình khủng long ngộ nghĩnh.', 'KidFashion', 'Cotton 100%', 120000.00, 'ACTIVE', 9, 5, NOW(), NOW()),
(11, 'Đầm công chúa bé gái', 'Đầm công chúa vải tulle phồng, phù hợp dự tiệc sinh nhật.', 'PrincessKid', 'Tulle + satin', 350000.00, 'ACTIVE', 9, 5, NOW(), NOW()),
(12, 'Quần short bé trai thể thao', 'Quần short thể thao thoáng mát, co giãn tốt.', 'KidFashion', 'Polyester', 95000.00, 'ACTIVE', 10, 5, NOW(), NOW()),
(13, 'Bình sữa Pigeon 240ml', 'Bình sữa cổ rộng Pigeon, núm ti mềm, chống sặc.', 'Pigeon', 'Nhựa PPSU', 285000.00, 'ACTIVE', 11, 4, NOW(), NOW()),
(14, 'Sách tô màu Thế giới động vật', 'Sách tô màu 48 trang với hình động vật dễ thương, kèm bút sáp.', 'EduBook', 'Giấy couché', 55000.00, 'ACTIVE', 13, 1, NOW(), NOW()),
(15, 'Bộ bút sáp màu 24 cây Crayola', 'Bút sáp màu Crayola an toàn, không độc hại, màu tươi sáng.', 'Crayola', 'Sáp an toàn', 85000.00, 'ACTIVE', 14, 1, NOW(), NOW());

-- ==================== 6. PRODUCT VARIANTS (35 bien the) ====================
INSERT INTO product_variants (id, product_id, color, size, price_override, stock_quantity, active, created_at, updated_at) VALUES
-- LEGO City 60316
(1, 1, NULL, NULL, NULL, 30, true, NOW(), NOW()),
-- LEGO Duplo trang trai
(2, 2, NULL, NULL, NULL, 25, true, NOW(), NOW()),
-- Xep hinh go 100 chi tiet
(3, 3, 'Nhiều màu', NULL, NULL, 50, true, NOW(), NOW()),
-- Bang chu cai nam cham
(4, 4, 'Nhiều màu', NULL, NULL, 100, true, NOW(), NOW()),
-- Do choi bac si
(5, 5, 'Hồng', NULL, NULL, 40, true, NOW(), NOW()),
(6, 5, 'Xanh dương', NULL, NULL, 35, true, NOW(), NOW()),
-- Xe scooter
(7, 6, 'Đỏ', NULL, NULL, 20, true, NOW(), NOW()),
(8, 6, 'Xanh lá', NULL, NULL, 18, true, NOW(), NOW()),
(9, 6, 'Hồng', NULL, 470000.00, 15, true, NOW(), NOW()),
-- Bong ro mini
(10, 7, 'Cam', NULL, NULL, 30, true, NOW(), NOW()),
-- Gau bong Teddy
(11, 8, 'Nâu', '50cm', NULL, 40, true, NOW(), NOW()),
(12, 8, 'Trắng', '50cm', NULL, 35, true, NOW(), NOW()),
(13, 8, 'Hồng', '70cm', 350000.00, 20, true, NOW(), NOW()),
-- Tho bong tai dai
(14, 9, 'Trắng', '40cm', NULL, 30, true, NOW(), NOW()),
(15, 9, 'Hồng', '40cm', NULL, 25, true, NOW(), NOW()),
-- Ao thun be trai khung long
(16, 10, 'Xanh lá', 'S (2-3T)', NULL, 60, true, NOW(), NOW()),
(17, 10, 'Xanh lá', 'M (4-5T)', NULL, 55, true, NOW(), NOW()),
(18, 10, 'Trắng', 'S (2-3T)', NULL, 50, true, NOW(), NOW()),
(19, 10, 'Trắng', 'M (4-5T)', NULL, 45, true, NOW(), NOW()),
(20, 10, 'Vàng', 'L (6-7T)', NULL, 40, true, NOW(), NOW()),
-- Dam cong chua
(21, 11, 'Hồng', 'S (2-3T)', NULL, 20, true, NOW(), NOW()),
(22, 11, 'Hồng', 'M (4-5T)', NULL, 18, true, NOW(), NOW()),
(23, 11, 'Trắng', 'M (4-5T)', NULL, 15, true, NOW(), NOW()),
(24, 11, 'Tím', 'L (6-7T)', 380000.00, 12, true, NOW(), NOW()),
-- Quan short be trai
(25, 12, 'Xanh navy', 'S (2-3T)', NULL, 70, true, NOW(), NOW()),
(26, 12, 'Xanh navy', 'M (4-5T)', NULL, 65, true, NOW(), NOW()),
(27, 12, 'Đen', 'M (4-5T)', NULL, 55, true, NOW(), NOW()),
-- Binh sua Pigeon
(28, 13, 'Trong suốt', '240ml', NULL, 80, true, NOW(), NOW()),
-- Sach to mau
(29, 14, NULL, NULL, NULL, 200, true, NOW(), NOW()),
-- But sap mau Crayola
(30, 15, 'Nhiều màu', '24 cây', NULL, 150, true, NOW(), NOW());

-- ==================== 7. PRODUCT IMAGES (15 anh) ====================
INSERT INTO product_images (id, product_id, image_url, thumbnail, created_at, updated_at) VALUES
(1, 1, 'https://placehold.co/600x600?text=LEGO+City', true, NOW(), NOW()),
(2, 2, 'https://placehold.co/600x600?text=LEGO+Duplo', true, NOW(), NOW()),
(3, 3, 'https://placehold.co/600x600?text=Xep+Hinh+Go', true, NOW(), NOW()),
(4, 4, 'https://placehold.co/600x600?text=Bang+Chu+Cai', true, NOW(), NOW()),
(5, 5, 'https://placehold.co/600x600?text=Do+Choi+Bac+Si', true, NOW(), NOW()),
(6, 6, 'https://placehold.co/600x600?text=Xe+Scooter', true, NOW(), NOW()),
(7, 7, 'https://placehold.co/600x600?text=Bong+Ro+Mini', true, NOW(), NOW()),
(8, 8, 'https://placehold.co/600x600?text=Gau+Bong+Teddy', true, NOW(), NOW()),
(9, 9, 'https://placehold.co/600x600?text=Tho+Bong', true, NOW(), NOW()),
(10, 10, 'https://placehold.co/600x600?text=Ao+Khung+Long', true, NOW(), NOW()),
(11, 11, 'https://placehold.co/600x600?text=Dam+Cong+Chua', true, NOW(), NOW()),
(12, 12, 'https://placehold.co/600x600?text=Quan+Short', true, NOW(), NOW()),
(13, 13, 'https://placehold.co/600x600?text=Binh+Sua', true, NOW(), NOW()),
(14, 14, 'https://placehold.co/600x600?text=Sach+To+Mau', true, NOW(), NOW()),
(15, 15, 'https://placehold.co/600x600?text=But+Sap+Mau', true, NOW(), NOW());

SELECT 'PART 1 DONE!' AS STATUS;
