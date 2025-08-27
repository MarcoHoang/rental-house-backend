# Hướng dẫn Deploy Backend lên Railway

## Tổng quan
Backend Spring Boot với MySQL database, deploy lên Railway.

## Các bước Deploy

### Bước 1: Chuẩn bị Repository
- Đảm bảo code đã được push lên GitHub
- Repository phải chứa các file:
  - `Dockerfile`
  - `pom.xml`
  - `railway.json`
  - `Procfile`

### Bước 2: Tạo Railway Account
1. Truy cập [railway.app](https://railway.app)
2. Sign up với GitHub
3. Authorize Railway truy cập repositories

### Bước 3: Tạo Project
1. Click "New Project"
2. Chọn "Deploy from GitHub repo"
3. Chọn repository chứa backend
4. Cấu hình:
   - **Project Name**: `rental-house-backend`
   - **Root Directory**: `rental-house-backend` (nếu repo chứa cả frontend/backend)

### Bước 4: Thêm Database
1. Click "New" → "Database" → "MySQL"
2. Railway sẽ tự động tạo MySQL database
3. Copy connection string từ database settings

### Bước 5: Cấu hình Environment Variables
Thêm các biến môi trường sau:

```bash
# Database (từ Railway MySQL)
SPRING_DATASOURCE_URL=jdbc:mysql://your-railway-mysql-url:3306/rentalhouse
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your-password

# JWT
JWT_SECRET=your-super-secret-jwt-key-here-make-it-long-and-secure

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# CORS (cho frontend)
CORS_ALLOWED_ORIGINS=https://rental-house.vercel.app

# Email (tùy chọn)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### Bước 6: Deploy
1. Click "Deploy"
2. Railway sẽ build từ Dockerfile
3. Chờ build hoàn thành (5-10 phút)

## Kiểm tra Deployment

### Health Check
- URL: `https://your-app.railway.app/actuator/health`
- Phải trả về status "UP"

### API Endpoints
- Swagger UI: `https://your-app.railway.app/swagger-ui.html`
- API Base: `https://your-app.railway.app/api`

## Troubleshooting

### Build Errors
- Kiểm tra Dockerfile syntax
- Đảm bảo Maven build thành công locally
- Kiểm tra Java version (17)

### Database Connection
- Kiểm tra connection string
- Đảm bảo database đã được tạo
- Kiểm tra credentials

### CORS Issues
- Cấu hình đúng CORS_ALLOWED_ORIGINS
- Bao gồm frontend domain

## Kết nối với Frontend

Sau khi deploy thành công:
1. Copy backend URL từ Railway
2. Cấu hình trong Vercel (frontend):
   - Environment Variable: `VITE_API_URL=https://your-backend.railway.app`

## Monitoring

### Logs
- Railway dashboard → Project → Deployments → View logs

### Metrics
- Railway cung cấp basic metrics
- Health check endpoint để monitor

## Cost
- Railway có free tier
- MySQL database có thể tính phí
- Monitor usage trong dashboard
