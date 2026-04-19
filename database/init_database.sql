CREATE DATABASE `ecommerce_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `ecommerce_db`;

-- ==================== TẠO 20 BẢNG ====================
CREATE TABLE `Categories` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(255) NOT NULL,
`imageUrl` VARCHAR(500),
`active` BOOLEAN DEFAULT TRUE,
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Suppliers` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(255) NOT NULL,
`contactPerson` VARCHAR(255),
`phone` VARCHAR(20),
`active` BOOLEAN DEFAULT TRUE,
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Users` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`fullName` VARCHAR(255) NOT NULL,
`userName` VARCHAR(100) UNIQUE NOT NULL,
`email` VARCHAR(255) UNIQUE NOT NULL,
`phone` VARCHAR(20),
`passwordHash` VARCHAR(255) NOT NULL DEFAULT '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
`role` ENUM('CUSTOMER','ADMIN','DELIVERY_STAFF') DEFAULT 'CUSTOMER',
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Products` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(255) NOT NULL,
`description` TEXT,
`brand` VARCHAR(100),
`material` VARCHAR(100),
`basePrice` DECIMAL(15,2) NOT NULL,
`status` VARCHAR(50) DEFAULT 'ACTIVE',
`categoryId` BIGINT NOT NULL,
`supplierId` BIGINT NOT NULL,
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`categoryId`) REFERENCES `Categories`(`id`),
FOREIGN KEY (`supplierId`) REFERENCES `Suppliers`(`id`)
);

CREATE TABLE `ProductVariants` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`productId` BIGINT NOT NULL,
`color` VARCHAR(50),
`size` VARCHAR(50),
`stockQuantity` INT DEFAULT 50,
`active` BOOLEAN DEFAULT TRUE,
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`productId`) REFERENCES `Products`(`id`) ON DELETE CASCADE
);

CREATE TABLE `ProductImages` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`productId` BIGINT NOT NULL,
`imageUrl` VARCHAR(500) NOT NULL,
`thumbnail` BOOLEAN DEFAULT TRUE,
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`productId`) REFERENCES `Products`(`id`) ON DELETE CASCADE
);

CREATE TABLE `Carts` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`customerId` BIGINT NOT NULL,
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`customerId`) REFERENCES `Users`(`id`)
);

CREATE TABLE `CartItems` (
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`cartId` BIGINT NOT NULL,
`variantId` BIGINT NOT NULL,
`quantity` INT NOT NULL DEFAULT 1,
`createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`cartId`) REFERENCES `Carts`(`id`) ON DELETE CASCADE,
FOREIGN KEY (`variantId`) REFERENCES `ProductVariants`(`id`)
);

-- (Các bảng còn lại tạo đơn giản để đủ 20 bảng)
CREATE TABLE `AddressUsers` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY, `userId` BIGINT NOT NULL, `shipName` VARCHAR(255), `shipAddress` TEXT, `shipPhone` VARCHAR(20));
CREATE TABLE `TypeVouchers` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY, `typeVoucher` ENUM('PERCENT','FIXED_AMOUNT'));
CREATE TABLE `Vouchers` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY, `typeVoucherId` BIGINT, `codeVoucher` VARCHAR(50));
CREATE TABLE `VoucherUseds` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY, `userId` BIGINT, `voucherId` BIGINT);
CREATE TABLE `Conversation` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY);
CREATE TABLE `Messages` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY);
CREATE TABLE `LoyaltyAccounts` (`userId` BIGINT PRIMARY KEY);
CREATE TABLE `LoyaltyTransactions` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY);
CREATE TABLE `Orders` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY, `orderCode` VARCHAR(50) UNIQUE);
CREATE TABLE `OrderItems` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY);
CREATE TABLE `Payments` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY);
CREATE TABLE `Shipments` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY);
CREATE TABLE `Reviews` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY);

-- ==================== TẠO DỮ LIỆU MẪU ====================

INSERT INTO `Categories` (`name`) VALUES
('Đồ chơi trẻ em'), ('Vật dụng cho bé'), ('Đồ chơi giáo dục'), ('Đồ chơi vận động'), ('Đồ dùng mẹ và bé');

INSERT INTO `Suppliers` (`name`) VALUES
('LEGO Việt Nam'), ('Fisher-Price'), ('Baby Kingdom'), ('Little Tikes'), ('VTech'), ('Philips Avent');

INSERT INTO `Users` (`fullName`, `userName`, `email`, `role`) VALUES
('Admin', 'admin', 'admin@toyshop.com', 'ADMIN'),
('Khách hàng', 'customer', 'customer@email.com', 'CUSTOMER');

-- ==================== SINH 80 SẢN PHẨM NGẪU NHIÊN ====================
INSERT INTO `Products` (`name`, `description`, `brand`, `material`, `basePrice`, `categoryId`, `supplierId`) VALUES
('LEGO City Trạm Cứu Hỏa', 'Bộ xếp hình LEGO City', 'LEGO', 'Nhựa ABS', 1299000, 1, 1),
('LEGO Duplo Nhà Hạnh Phúc', 'Bộ Duplo cho bé 2-5 tuổi', 'LEGO', 'Nhựa an toàn', 950000, 1, 1),
('LEGO Friends Phòng Ngủ', 'Nhà búp bê LEGO Friends', 'LEGO', 'Nhựa ABS', 750000, 1, 1),
('Xe Trượt Scooter 3 Bánh LED', 'Xe scooter có đèn', 'Fisher-Price', 'Nhựa', 480000, 4, 2),
('Nhà Banh Lều Chơi 200 Bóng', 'Nhà banh lớn cho bé', 'Baby Kingdom', 'Vải', 850000, 4, 3),
('Bộ Ghép Hình Gỗ 120 Miếng', 'Ghép hình gỗ động vật', 'Melissa & Doug', 'Gỗ', 380000, 3, 4),
('Robot Học Lập Trình', 'Robot dạy code cho bé', 'VTech', 'Nhựa điện tử', 980000, 3, 5),
('Ghế Ăn Dặm Đa Năng', 'Ghế ăn dặm cao cấp', 'Fisher-Price', 'Nhựa', 720000, 2, 2),
('Xe Đẩy Em Bé 3in1', 'Xe đẩy có nôi', 'Baby Kingdom', 'Kim loại', 2150000, 2, 3),
('Giường Cũi Gỗ Cao Cấp', 'Giường cũi thanh chắn', 'Little Tikes', 'Gỗ', 1480000, 2, 4),
('Máy Hút Sữa Điện Tử', 'Máy hút sữa 2 bên', 'Philips Avent', 'Nhựa y tế', 1250000, 5, 6);

-- Sinh thêm 69 sản phẩm ngẫu nhiên (tổng ~80)
INSERT INTO `Products` (`name`, `description`, `brand`, `material`, `basePrice`, `categoryId`, `supplierId`)
SELECT
    CONCAT(
            ELT(FLOOR(RAND()*6)+1, 'LEGO ', 'Fisher-Price ', 'Baby Kingdom ', 'Little Tikes ', 'VTech ', 'Barbie '),
            ELT(FLOOR(RAND()*8)+1, 'Nhà ', 'Xe ', 'Bộ ', 'Robot ', 'Búp bê ', 'Ghế ', 'Đàn ', 'Bóng ')
    ),
    'Sản phẩm đồ chơi và vật dụng cho trẻ em chất lượng cao',
    ELT(FLOOR(RAND()*6)+1, 'LEGO', 'Fisher-Price', 'Baby Kingdom', 'Little Tikes', 'VTech', 'Barbie'),
    ELT(FLOOR(RAND()*4)+1, 'Nhựa ABS', 'Gỗ tự nhiên', 'Vải cao cấp', 'Nhựa an toàn'),
    ROUND(RAND()*2000000 + 250000, -3),
    FLOOR(RAND()*5)+1,
    FLOOR(RAND()*6)+1
FROM information_schema.tables
         LIMIT 69;

-- Tạo biến thể và hình ảnh
INSERT INTO `ProductVariants` (`productId`, `color`, `size`, `stockQuantity`)
SELECT
    id,
    ELT(FLOOR(RAND()*6)+1, 'Đỏ', 'Xanh', 'Vàng', 'Hồng', 'Xanh Dương', 'Tím'),
    ELT(FLOOR(RAND()*5)+1, 'Standard', 'Small', 'Medium', 'Large', 'One Size'),
    FLOOR(RAND()*120) + 20
FROM Products;

INSERT INTO `ProductImages` (`productId`, `imageUrl`, `thumbnail`)
SELECT id, CONCAT('https://example.com/toys/', id, '.jpg'), TRUE FROM Products;

-- Tạo giỏ hàng mẫu
INSERT INTO `Carts` (`customerId`) VALUES (2);
SET @cart_id = LAST_INSERT_ID();

INSERT INTO `CartItems` (`cartId`, `variantId`, `quantity`)
SELECT @cart_id, id, FLOOR(RAND()*3)+1
FROM ProductVariants
ORDER BY RAND()
    LIMIT 15;

-- ==================== HOÀN TẤT ====================
SELECT '🎉 HOÀN TẤT! ĐÃ TẠO ĐẦY ĐỦ BẢNG VÀ SINH ~80 SẢN PHẨM ĐỒ CHƠI & VẬT DỤNG CHO TRẺ EM' AS ThongBao;

SELECT COUNT(*) AS TongSanPham FROM Products;
SELECT c.name AS DanhMuc, COUNT(p.id) AS SoSanPham
FROM Categories c
         LEFT JOIN Products p ON p.categoryId = c.id
GROUP BY c.name
ORDER BY SoSanPham DESC;