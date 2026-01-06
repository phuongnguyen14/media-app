# Apidog Integration Guide

## Giới Thiệu

Hướng dẫn sử dụng Apidog để test API của Media Application Backend.

## Phương Pháp 1: Import Trực Tiếp Từ URL (Khuyến Nghị) ⭐

### Bước 1: Khởi Động Backend

```bash
cd d:\CODE\media-app\backend
mvn spring-boot:run
```

Đợi cho đến khi thấy log:
```
Started MediaAppApplication in X.XXX seconds
```

### Bước 2: Import Vào Apidog

1. Mở **Apidog**
2. Tạo project mới hoặc mở project hiện có
3. Click **Import** (góc trên bên trái)
4. Chọn **URL** 
5. Nhập URL: `http://localhost:8080/api/api-docs`
6. Click **Import** hoặc **Confirm**

✅ **Tất cả API endpoints sẽ được tự động import với đầy đủ thông tin!**

### Bước 3: Sync Thường Xuyên

Mỗi khi bạn thêm/sửa API, chỉ cần:
- Click **Import** lại
- Chọn **Sync** để cập nhật thay đổi

---

## Phương Pháp 2: Export File OpenAPI Spec

Nếu bạn muốn chia sẻ API spec với team hoặc làm việc offline:

### Trên Windows:

```powershell
cd d:\CODE\media-app\backend\scripts
.\export-openapi.ps1
```

### Trên Linux/Mac:

```bash
cd d:\CODE\media-app\backend/scripts
chmod +x export-openapi.sh
./export-openapi.sh
```

File `openapi.json` sẽ được tạo ra. Bạn có thể:
- Import vào Apidog: **Import** → **OpenAPI** → Chọn file
- Chia sẻ với team qua Git
- Import vào Postman, Insomnia, hoặc công cụ khác

---

## Phương Pháp 3: Sử dụng Swagger UI Để Xuất

1. Truy cập: `http://localhost:8080/api/swagger-ui.html`
2. Click vào link `/api/api-docs` ở đầu trang
3. Copy toàn bộ JSON
4. Trong Apidog: **Import** → **OpenAPI** → Paste JSON

---

## Lợi Ích Khi Dùng Apidog

✅ **Auto-generated documentation** từ code  
✅ **Test API** trực tiếp với UI đẹp  
✅ **Mock Server** để test frontend trước khi backend hoàn thành  
✅ **Collaboration** với team  
✅ **Auto-sync** khi code thay đổi  
✅ **Test Scenarios** và automation testing  

---

## Environment Variables (Tùy Chọn)

Trong Apidog, bạn có thể tạo environments:

### Local Development
- **Base URL**: `http://localhost:8080/api`

### Staging (nếu có)
- **Base URL**: `https://staging.mediaapp.com/api`

### Production (nếu có)
- **Base URL**: `https://api.mediaapp.com/api`

---

## Troubleshooting

### Backend không running?
```bash
# Kiểm tra port 8080
netstat -an | Select-String ":8080"

# Start backend
mvn spring-boot:run
```

### Import lỗi?
- Đảm bảo backend đang chạy
- Kiểm tra firewall không block port 8080
- Thử truy cập `http://localhost:8080/api/api-docs` trong browser trước

### API không đầy đủ?
- Rebuild backend: `mvn clean install`
- Restart application
- Refresh import trong Apidog

---

## Liên Kết Hữu Ích

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/api-docs
- **Health Check**: http://localhost:8080/api/actuator/health

---

**Happy Testing! 🚀**
