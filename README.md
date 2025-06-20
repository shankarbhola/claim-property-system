# 🛡️ Claim & Property Management System

A secure Spring Boot backend application to manage **insurance claims** and **property listings** with:
- JWT Authentication
- Role-based Access (ADMIN, AGENT, CUSTOMER)
- File Uploads
- OTP Recovery (mocked)
- PDF Generation (iText)
- Swagger Docs
- Logging and Testing

---

## 🚀 Features

- ✅ JWT-based Authentication & Authorization
- 👥 Role-based Access Control (ADMIN, AGENT, CUSTOMER)
- 🔁 OTP-based password recovery (mocked)
- 🧾 Claim Module: CRUD + Filter by status/date
- 🏠 Property Module: Upload up to 200 images/property
- 📄 Auto-generate Claim PDF on approval using iText
- 📨 Simulate Email/SMS (logs)
- 📘 API Documentation using Swagger/OpenAPI
- 📊 Log all requests & exceptions
- 🧪 JUnit + Mockito for Unit Testing

---

## 🧱 Tech Stack

| Layer        | Technology                      |
|--------------|----------------------------------|
| Language     | Java 17                         |
| Framework    | Spring Boot 3.x                 |
| Security     | Spring Security + JWT           |
| Build Tool   | Maven                           |
| DB           | MySQL / H2                      |
| ORM          | Spring Data JPA (Hibernate)     |
| PDF          | iText 5.5.13.3                  |
| Docs         | Springdoc Swagger/OpenAPI       |
| Testing      | JUnit 5, Mockito                |

---

## ⚙️ Build & Run Instructions

### 1. Clone and Build

```bash
git clone https://github.com/your-username/claim-property-system.git
cd claim-property-system
mvn clean install
```
### 2. Configure Application Properties
#### Edit `src/main/resources/application.properties:`

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/claim_property_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 3. Run the App