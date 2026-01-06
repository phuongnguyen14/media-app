# Media Application Backend

Backend service cho Media Application - Nền tảng quản lý và tìm kiếm nội dung đa phương tiện.

## 🚀 Công nghệ sử dụng

- **Java 17** - LTS version
- **Spring Boot 3.5.0** - Framework chính
- **PostgreSQL** - Database chính
- **Elasticsearch 8.11** - Search engine
- **Flyway** - Database migration
- **HikariCP** - Connection pooling
- **Lombok** - Giảm boilerplate code
- **MapStruct** - DTO mapping
- **SpringDoc OpenAPI** - API documentation

## 📋 Yêu cầu hệ thống

- Java 17 hoặc cao hơn
- Maven 3.8+
- PostgreSQL 14+
- Elasticsearch 8.x
- 4GB RAM (recommended)

## 🔧 Cài đặt

### 1. Clone repository

```bash
git clone <repository-url>
cd media-app/backend
```

### 2. Cài đặt PostgreSQL

#### Windows (sử dụng Chocolatey):
```powershell
choco install postgresql
```

#### Hoặc download từ: https://www.postgresql.org/download/

Sau khi cài đặt, tạo database:
```sql
CREATE DATABASE media_app_db;
```

### 3. Cài đặt Elasticsearch

#### Windows (sử dụng Chocolatey):
```powershell
choco install elasticsearch
```

#### Hoặc download từ: https://www.elastic.co/downloads/elasticsearch

Start Elasticsearch:
```powershell
# Navigate to Elasticsearch directory
cd C:\ProgramData\chocolatey\lib\elasticsearch\tools\elasticsearch-<version>
.\bin\elasticsearch.bat
```

Verify Elasticsearch đang chạy:
```powershell
curl http://localhost:9200
```

### 4. Cấu hình application

Tạo file `application-local.yml` trong `src/main/resources/` (file này sẽ không được commit):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/media_app_db
    username: postgres
    password: your_password
    
  elasticsearch:
    uris: http://localhost:9200
```

## 🏃 Chạy ứng dụng

### Sử dụng Maven

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

### Sử dụng IDE

Import project vào IntelliJ IDEA hoặc Eclipse và run `MediaAppApplication.java`

### Chạy với profile cụ thể

```bash
# Development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 📊 Database Migration

Flyway sẽ tự động chạy migrations khi application start. Để chạy migration thủ công:

```bash
mvn flyway:migrate
```

Các lệnh Flyway khác:
```bash
mvn flyway:info      # Xem trạng thái migrations
mvn flyway:validate  # Validate migrations
mvn flyway:clean     # Clean database (chỉ dùng trong dev!)
```

## 🔍 API Documentation

Sau khi start application, truy cập:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api/api-docs

## 🏥 Health Check

Kiểm tra health của application:

```bash
curl http://localhost:8080/api/actuator/health
```

Các endpoints khác:
- Metrics: http://localhost:8080/api/actuator/metrics
- Prometheus: http://localhost:8080/api/actuator/prometheus
- Info: http://localhost:8080/api/actuator/info

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=UserServiceTest

# Run integration tests
mvn verify
```

## 📦 Build Production

```bash
# Build JAR file
mvn clean package -DskipTests

# JAR file sẽ được tạo tại: target/media-app-backend-1.0.0-SNAPSHOT.jar

# Run JAR file
java -jar target/media-app-backend-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

## 🐳 Docker (Coming Soon)

```bash
# Build Docker image
docker build -t media-app-backend .

# Run container
docker-compose up -d
```

## 📁 Cấu trúc thư mục

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/mediaapp/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── service/          # Business logic
│   │   │   ├── repository/       # Data access
│   │   │   │   ├── jpa/          # JPA repositories
│   │   │   │   └── elasticsearch/ # ES repositories
│   │   │   ├── model/            # Domain entities
│   │   │   │   ├── entity/       # JPA entities
│   │   │   │   └── document/     # ES documents
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── mapper/           # MapStruct mappers
│   │   │   ├── exception/        # Custom exceptions
│   │   │   └── util/             # Utility classes
│   │   └── resources/
│   │       ├── db/migration/     # Flyway migrations
│   │       ├── application.yml   # Main config
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── logback-spring.xml
│   └── test/                     # Test files
├── pom.xml
└── README.md
```

## 🔐 Security (Coming Soon)

Application hiện tại chưa có authentication/authorization. Sẽ được implement trong các version tiếp theo với:
- Spring Security
- JWT tokens
- Role-based access control (RBAC)

## 🐛 Troubleshooting

### PostgreSQL connection refused
```bash
# Kiểm tra PostgreSQL đang chạy
pg_isready

# Start PostgreSQL service
# Windows:
net start postgresql-x64-14

# Linux/Mac:
sudo systemctl start postgresql
```

### Elasticsearch connection timeout
```bash
# Kiểm tra Elasticsearch đang chạy
curl http://localhost:9200

# Kiểm tra logs
tail -f logs/media-app.log
```

### Flyway migration failed
```bash
# Xem chi tiết lỗi
mvn flyway:info

# Repair nếu cần (cẩn thận!)
mvn flyway:repair
```

## 📝 Environment Variables

Có thể override configuration bằng environment variables:

```bash
# Database
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=media_app_db
export DB_USERNAME=postgres
export DB_PASSWORD=your_password

# Elasticsearch
export ES_URIS=http://localhost:9200
export ES_USERNAME=
export ES_PASSWORD=

# Application
export SPRING_PROFILE=dev
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Contact

Development Team - dev@mediaapp.com

Project Link: [https://github.com/yourorg/media-app](https://github.com/yourorg/media-app)
