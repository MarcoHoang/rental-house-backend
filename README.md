# Rental House Backend

A comprehensive Spring Boot backend application for a rental house management system. This application provides RESTful APIs for managing house rentals, user authentication, and administrative functions.

## 🚀 Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **House Management**: CRUD operations for rental properties with image support
- **Rental System**: Booking and management of house rentals
- **Review System**: User reviews and ratings for properties
- **Notification System**: Real-time notifications for users
- **Admin Dashboard**: Administrative functions for system management
- **Email Integration**: Email notifications and password reset functionality
- **Geocoding**: Location services using Google Maps API
- **Internationalization**: Multi-language support

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.1.4**
- **Spring Security** with JWT
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven**
- **Lombok**
- **MapStruct**
- **OpenAPI/Swagger**

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- Gmail account (for email functionality)
- Google Maps API key (optional, for geocoding)

## 🔧 Installation & Setup

### 1. Clone the repository
```bash
git clone <repository-url>
cd rental-house-backend
```

### 2. Configure environment variables
Copy the `env.example` file to `.env` and configure your environment variables:

```bash
cp env.example .env
```

Edit the `.env` file with your configuration:
```env
# Server Configuration
SERVER_PORT=8080

# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/rental_house?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# JWT Configuration
JWT_SECRET_KEY=your_jwt_secret_key_here_make_it_long_and_secure_at_least_256_bits

# Email Configuration (Gmail)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Optional: Google API Configuration (for geocoding)
GOOGLE_API_KEY=your_google_api_key_here
```

### 3. Database Setup
Create a MySQL database and update the connection details in your environment variables.

### 4. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, you can access the API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🏗️ Project Structure

```
src/main/java/com/codegym/
├── config/                 # Configuration classes
├── controller/             # REST controllers
│   ├── api/               # Public API endpoints
│   └── admin/             # Admin endpoints
├── dto/                   # Data Transfer Objects
│   ├── request/           # Request DTOs
│   └── response/          # Response DTOs
├── entity/                # JPA entities
├── exception/             # Custom exceptions
├── infrastructure/        # Infrastructure components
├── mapper/                # MapStruct mappers
├── repository/            # Data access layer
├── seed/                  # Database seeding
├── service/               # Business logic layer
│   └── impl/              # Service implementations
└── utils/                 # Utility classes
```

## 🔐 Authentication & Authorization

The application uses JWT-based authentication with three user roles:

- **USER**: Regular users who can rent houses and leave reviews
- **HOST**: Property owners who can manage their listings
- **ADMIN**: System administrators with full access

### API Endpoints

#### Public Endpoints (No authentication required)
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/houses` - Get all houses
- `GET /api/houses/{id}` - Get house details

#### User Endpoints (ROLE_USER required)
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `POST /api/rentals` - Create rental request
- `GET /api/rentals` - Get user's rentals

#### Host Endpoints (ROLE_HOST required)
- `POST /api/renters/{id}/houses` - Add house listing
- `PUT /api/houses/{id}/status` - Update house status
- `GET /api/renters/{id}/rentals` - Get host's rentals

#### Admin Endpoints (ROLE_ADMIN required)
- `GET /api/admin/users` - Get all users
- `PUT /api/admin/users/{id}` - Update user
- `GET /api/admin/dashboard` - Admin dashboard

## 🗄️ Database Schema

The application includes the following main entities:

- **users**: User accounts and profiles
- **houses**: Rental properties
- **rentals**: Rental bookings
- **reviews**: User reviews and ratings
- **notifications**: System notifications
- **house_renters**: Property owner profiles
- **house_renter_requests**: Host registration requests

## 📧 Email Configuration

To enable email functionality:

1. Create a Gmail account or use an existing one
2. Enable 2-factor authentication
3. Generate an App Password
4. Update the `MAIL_USERNAME` and `MAIL_PASSWORD` in your environment variables

## 🗺️ Geocoding (Optional)

To enable location services:

1. Get a Google Maps API key from Google Cloud Console
2. Enable the Geocoding API
3. Add the API key to your environment variables as `GOOGLE_API_KEY`

## 🧪 Testing

The application includes basic integration tests. To run tests:

```bash
mvn test
```

## 📝 Logging

The application uses structured logging with the following configuration:

- **Console logging**: Formatted output for development
- **File logging**: Rotated log files in `logs/rental-house.log`
- **Log levels**: Configurable per package

## 🚀 Deployment

### Docker Deployment
```bash
# Build Docker image
docker build -t rental-house-backend .

# Run container
docker run -p 8080:8080 --env-file .env rental-house-backend
```

### Traditional Deployment
1. Build the JAR file: `mvn clean package`
2. Run the JAR: `java -jar target/rentalhouse-0.0.1-SNAPSHOT.jar`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **CodeGym Team** - Initial work

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- The open-source community for various libraries and tools




