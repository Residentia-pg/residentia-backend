# Residentia - PG Finder Platform (Owner Panel)

A complete React + Spring Boot application for managing PG (Paying Guest) properties and bookings. This project includes full owner functionality with JWT authentication, Swagger API documentation, and MySQL database integration.

## Project Structure

### Backend (Spring Boot)
```
residentia-backend/
├── src/main/java/com/residentia/
│   ├── entity/              # JPA Entities
│   │   ├── Owner.java
│   │   ├── Property.java
│   │   ├── Booking.java
│   │   └── Review.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── OwnerRegistrationDTO
│   │   ├── OwnerLoginDTO
│   │   ├── OwnerDTO
│   │   ├── PropertyDTO
│   │   ├── BookingDTO
│   │   └── AuthResponseDTO
│   ├── controller/          # REST Controllers
│   │   ├── OwnerController
│   │   ├── PropertyController
│   │   └── BookingController
│   ├── service/             # Business Logic
│   │   ├── OwnerService
│   │   ├── PropertyService
│   │   └── BookingService
│   ├── repository/          # Database Access Layer
│   │   ├── OwnerRepository
│   │   ├── PropertyRepository
│   │   ├── BookingRepository
│   │   └── ReviewRepository
│   ├── exception/           # Exception Handling
│   │   ├── GlobalExceptionHandler
│   │   ├── ResourceNotFoundException
│   │   ├── DuplicateResourceException
│   │   └── UnauthorizedException
│   ├── config/              # Spring Configuration
│   │   ├── SecurityConfig
│   │   ├── JwtAuthenticationFilter
│   │   ├── JwtAuthenticationEntryPoint
│   │   └── SwaggerConfig
│   ├── security/            # JWT Token Management
│   │   └── JwtTokenProvider
│   └── ResidentialiaApplication.java
├── src/main/resources/
│   ├── application.yml      # Configuration
│   └── schema.sql          # Database Schema
└── pom.xml                 # Maven Dependencies
```

### Frontend (React)
```
residentia-frontend/
├── src/
│   ├── api/                # API Integration
│   │   ├── axios.js        # Axios Configuration with JWT
│   │   ├── ownerApi.js     # Owner APIs
│   │   ├── propertyApi.js  # Property APIs
│   │   └── bookingApi.js   # Booking APIs
│   ├── components/
│   │   ├── OwnerPanel/     # Owner Dashboard Components
│   │   │   ├── Owner.jsx
│   │   │   ├── DashboardContent.jsx
│   │   │   ├── MyPropertiesContent.jsx
│   │   │   ├── AddPropertyContent.jsx
│   │   │   ├── BookingsContent.jsx
│   │   │   ├── ProfileContent.jsx
│   │   │   └── Owner.module.css
│   │   └── ...
│   ├── pages/
│   │   ├── Auth/
│   │   │   ├── Owner/
│   │   │   │   ├── OwnerRegister.jsx
│   │   │   │   ├── OwnerLogin.jsx
│   │   │   │   └── OwnerRegister.css
│   │   │   └── Client/
│   │   └── Landing/
│   └── utils/
│       └── ownerfrontAuth.js
└── package.json
```

## Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Node.js 16+ and npm/yarn**
- **MySQL 8.0+**
- **Git**

## Backend Setup

### 1. Install Dependencies
```bash
cd residentia-backend
mvn clean install
```

### 2. Database Setup
```bash
# Create database using MySQL
mysql -u root -p < src/main/resources/schema.sql
```

Or manually:
```sql
CREATE DATABASE residentia_db;
USE residentia_db;
-- Run the schema.sql file contents
```

### 3. Configure Database Connection
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/residentia_db
    username: root
    password: your_mysql_password
```

### 4. Run Backend Server
```bash
mvn spring-boot:run
```

Server will start on `http://localhost:8080`

## Frontend Setup

### 1. Install Dependencies
```bash
cd residentia-frontend
npm install
```

### 2. Run Development Server
```bash
npm run dev
```

Frontend will be available on `http://localhost:5173`

## API Endpoints

### Authentication
- **POST** `/api/owner/register` - Register new owner
- **POST** `/api/owner/login` - Login owner
- **GET** `/api/owner/profile` - Get logged-in owner profile
- **PUT** `/api/owner/profile` - Update owner profile
- **GET** `/api/owner/{ownerId}` - Get owner by ID

### Properties
- **POST** `/api/owner/pgs` - Create new property
- **GET** `/api/owner/pgs` - Get all owner properties
- **GET** `/api/owner/pgs/{propertyId}` - Get property details
- **PUT** `/api/owner/pgs/{propertyId}` - Update property
- **DELETE** `/api/owner/pgs/{propertyId}` - Delete property

### Bookings
- **GET** `/api/owner/bookings` - Get all owner bookings
- **GET** `/api/owner/bookings/{bookingId}` - Get booking details
- **GET** `/api/owner/property/{propertyId}/bookings` - Get property bookings
- **PUT** `/api/owner/bookings/{bookingId}` - Update booking
- **DELETE** `/api/owner/bookings/{bookingId}` - Delete booking

## Swagger API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

Features:
- Interactive API testing
- Request/Response examples
- Authentication with JWT tokens
- Complete API documentation

## Authentication Flow

1. **Register**: User provides details and creates account
   - Password is hashed with BCrypt
   - JWT token is generated on success

2. **Login**: User authenticates with email and password
   - Server validates credentials
   - JWT token is issued for subsequent requests

3. **API Requests**: All protected endpoints require JWT token
   - Token sent in `Authorization: Bearer <token>` header
   - Token validated using `JwtAuthenticationFilter`

4. **Token Validation**:
   - Token signature verified
   - Expiration checked
   - User context set in `SecurityContext`

## Key Features

### Owner Management
- ✅ Owner registration with full details
- ✅ Profile management and updates
- ✅ Account deletion
- ✅ JWT-based authentication

### Property Management
- ✅ Create new properties/PG listings
- ✅ Update property details
- ✅ Delete properties
- ✅ View all properties owned
- ✅ Property status management (ACTIVE, INACTIVE, MAINTENANCE)

### Booking Management
- ✅ View all bookings for owned properties
- ✅ Update booking status
- ✅ Manage check-in/check-out dates
- ✅ Track tenant information

### Exception Handling
- ✅ Global exception handler with consistent error responses
- ✅ Custom exceptions for different error scenarios
- ✅ Detailed error messages and HTTP status codes

### API Documentation
- ✅ Swagger/OpenAPI integration
- ✅ Complete endpoint documentation
- ✅ Interactive API testing
- ✅ Schema documentation

## Sample API Requests

### Register Owner
```json
POST /api/owner/register
{
  "name": "John Doe",
  "email": "john@example.com",
  "mobile": "9876543210",
  "managementCompany": "My PGs Pvt Ltd",
  "address": "123 Main St",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": "400001",
  "password": "secure_password",
  "aadharOrPan": "12345678901234",
  "bankAccount": "1234567890",
  "ifsc": "SBIN0001234"
}
```

### Login Owner
```json
POST /api/owner/login
{
  "email": "john@example.com",
  "passwordHash": "secure_password"
}
```

### Create Property
```json
POST /api/owner/pgs
Authorization: Bearer <token>
{
  "propertyName": "Cozy PG in Mumbai",
  "address": "456 Park Ave",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": "400002",
  "rentAmount": 15000,
  "sharingType": "Double",
  "maxCapacity": 4,
  "availableBeds": 2,
  "foodIncluded": true,
  "description": "Well-maintained PG with all facilities",
  "status": "ACTIVE"
}
```

## Technologies Used

### Backend
- Spring Boot 3.2
- Spring Data JPA/Hibernate
- Spring Security
- JWT (JSON Web Token)
- MySQL 8.0
- Maven
- Lombok
- Springdoc OpenAPI (Swagger)

### Frontend
- React 19
- Vite
- React Router v7
- Axios
- React Toastify
- CSS Modules

## Default Configuration

- **Backend Port**: 8080
- **Frontend Port**: 5173
- **Database**: MySQL (residentia_db)
- **JWT Expiration**: 24 hours
- **CORS Origins**: localhost:5173, localhost:3000

## Troubleshooting

### Database Connection Error
- Ensure MySQL is running
- Check database credentials in `application.yml`
- Verify database `residentia_db` exists

### Port Already in Use
- Backend: Change `server.port` in `application.yml`
- Frontend: Use `npm run dev -- --port 3000`

### CORS Issues
- Check CORS configuration in `SecurityConfig.java`
- Verify frontend origin is in allowed list

### JWT Token Expired
- Token expires after 24 hours
- User needs to re-login
- New token will be issued on successful login

## Running Both Applications

### Terminal 1 - Backend
```bash
cd residentia-backend
mvn spring-boot:run
```

### Terminal 2 - Frontend
```bash
cd residentia-frontend
npm run dev
```

## Testing the Application

1. **Access Frontend**: `http://localhost:5173`
2. **Register as Owner**: Fill in all required details
3. **Login**: Use registered email and password
4. **Dashboard**: View profile, properties, bookings
5. **Add Property**: Create new property listing
6. **Manage Bookings**: View and update booking status
7. **Update Profile**: Edit owner information

## API Testing with Swagger

1. Navigate to `http://localhost:8080/swagger-ui/index.html`
2. Click "Authorize" button
3. Paste JWT token from login response
4. Test endpoints directly from Swagger UI

## Future Enhancements

- Image upload functionality
- Email notifications
- Advanced search and filters
- Analytics dashboard
- Payment integration
- Review and rating system
- Chat functionality
- Mobile app (React Native)

## License

This project is part of the Residentia PG Finder platform.

## Support

For issues and support, contact the development team.

---

**Happy Coding! 🏠**
