## 🔗 Swagger URL

- Visit: `http://localhost:8080/swagger-ui/index.html`
- 📘 All APIs documented with request/response bodies.

### 🪪 Sample JWT Token

```

Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluQGVtYWlsLmNvbSIsImlzcyI6ImNsYWltLXN5c3RlbSIsImV4cCI6MTY5OTk5OTk5OX0.abcd1234xyz

```

> Replace it with a real token generated via `/api/auth/login`.

---

## 🛠️ Build & Run Instructions

### 1. Clone Repository

```bash
git clone https://github.com/shankarbhola/claim-property-system.git

cd claim-property-system
```

### 2. Configure Application

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/claim_property_db
spring.datasource.username=root
spring.datasource.password=yourpassword

jwt.secretKey=asdf-asdf-asdf
jwt.issuer=shankar
jwt.expityTime=86400000

file.upload-dir=ADD-YOUR-PATH/uploads
pdf.output-dir=pdfs
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

### 5. Access Swagger UI

Visit: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧪 Test Coverage

Run:

```bash
mvn test
```

![Test Coverage](https://i.ibb.co/bMSX55r6/image.png)

> 97% coverage on controller/service layers ensured using JUnit 4 + Mockito.