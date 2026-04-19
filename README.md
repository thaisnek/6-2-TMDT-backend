# 🛒 WebTMDT – Backend Thương Mại Điện Tử

Dự án backend cho hệ thống thương mại điện tử, xây dựng bằng Spring Boot, sử dụng MySQL làm cơ sở dữ liệu.

---

## 📋 Mục lục

- [Công nghệ sử dụng]
- [Cấu trúc dự án]
- [Cài đặt môi trường]
- [Cấu hình database]
- [Chạy ứng dụng]
- [Quy tắc làm việc nhóm]

---

## 🛠 Công nghệ sử dụng

| Thành phần        | Công nghệ                         |
|-------------------|-----------------------------------|
| Ngôn ngữ          | Java 21                           |
| Framework         | Spring Boot 4.0.5                 |
| ORM               | Spring Data JPA (Hibernate)       |
| Bảo mật           | Spring Security + JWT (JJWT 0.11.5) |
| Cơ sở dữ liệu     | MySQL                             |
| Build tool        | Maven                             |
| Mapping DTO       | MapStruct 1.5.5                   |
| Tiện ích          | Lombok 1.18.30                    |

---

## 📁 Cấu trúc dự án

```bash
WebTMDT/
├── src/
│   └── main/
│       ├── java/com/example/webtmdt/
│       │   ├── configuration/      # Cấu hình Security, JWT, CORS
│       │   ├── controller/         # REST API Controllers
│       │   │   ├── AuthController.java
│       │   │   ├── ProductController.java
│       │   │   └── CartController.java
│       │   ├── dto/                # Data Transfer Objects (request & response)
│       │   ├── entity/             # JPA Entities (ánh xạ bảng database)
│       │   ├── enums/              # Enum dùng chung (Role, OrderStatus, ...)
│       │   ├── repository/         # Spring Data JPA Repositories
│       │   └── service/            # Business Logic
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

---

## ⚙️ Cài đặt môi trường
### Yêu cầu
- JDK 21 trở lên
- MySQL 8.0 trở lên
- Maven 3.9+
- Postman (để kiểm tra API)

## 🗄 Cấu hình database
1. Tạo database trong MySQL:
```
CREATE DATABASE webtmdt CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
2. Mở file `src/main/resources/application.yaml` và chỉnh sửa thông tin kết nối:
```bash
server:
  port: 8080
  servlet:
    context-path: /httmdt
spring:
  datasource:
    url: "jdbc:mysql://localhost:3306/tmdt"
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

> ⚠️ Thay `username & password` bằng tài khoản MySQL của bạn.

---

## ▶️ Chạy ứng dụng

```bash
# Clone repository
git clone https://github.com/thaisnek/6-2-TMDT-backend.git
cd 6-2-TMDT-backend.git

# Chạy trên Windows
mvnw.cmd spring-boot:run
```
Ứng dụng sẽ khởi động tại: `http://localhost:8080`
> ⚠️ **Lưu ý**: Toàn bộ API đều có prefix `/httmdt`. Ví dụ:
> - Đăng nhập: `POST http://localhost:8080/httmdt/api/auth/login`


## 🌐 API Endpoints
### Auth
| Method | Endpoint              | Mô tả              | Auth |
|--------|-----------------------|--------------------|------|
| POST   | `/api/auth/register`  | Đăng ký tài khoản  | ❌   |
| POST   | `/api/auth/login`     | Đăng nhập, lấy token | ❌ |

### Products (Admin)
| Method | Endpoint                   | Mô tả              | Auth    |
|--------|----------------------------|--------------------|---------|
| GET    | `/api/products`            | Danh sách sản phẩm | ❌      |
| GET    | `/api/products/{id}`       | Chi tiết sản phẩm  | ❌      |
| POST   | `/api/products`            | Thêm sản phẩm      | 🔐 ADMIN |
| PUT    | `/api/products/{id}`       | Sửa sản phẩm       | 🔐 ADMIN |
| DELETE | `/api/products/{id}`       | Xoá sản phẩm       | 🔐 ADMIN |

### Cart (User)
| Method | Endpoint                        | Mô tả                  | Auth     |
|--------|---------------------------------|------------------------|----------|
| GET    | `/api/cart`                     | Xem giỏ hàng           | 🔐 USER  |
| POST   | `/api/cart/items`               | Thêm vào giỏ           | 🔐 USER  |
| PUT    | `/api/cart/items/{itemId}`      | Cập nhật số lượng      | 🔐 USER  |
| DELETE | `/api/cart/items/{itemId}`      | Xoá item khỏi giỏ      | 🔐 USER  |
| DELETE | `/api/cart`                     | Xoá toàn bộ giỏ hàng  | 🔐 USER  |

> **Cách dùng token**: Thêm header `Authorization: Bearer <your_jwt_token>` vào mỗi request cần xác thực.


## 👥 Quy tắc làm việc nhóm
### Nhánh (Branch)
| Nhánh       | Mục đích |
|-------------|----------|
| `main`      | Production – chỉ merge khi hoàn chỉnh |
| `develop`   | Nhánh phát triển chính |
| `feature/*` | Tính năng mới (VD: `feature/cart-api`) |

### Quy trình
```
feature/* → develop → main
```
1. Tạo nhánh `feature/ten-tinh-nang` từ `develop`
2. Code xong → tạo **Pull Request** vào `develop`
3. Ít nhất 1 thành viên review trước khi merge
4. Không commit thẳng vào `main` hoặc `develop`
