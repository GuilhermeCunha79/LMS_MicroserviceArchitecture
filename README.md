# 📚 LMS - Microservice Architecture

This project is a **Library Management System (LMS)** built with a **microservice architecture** as part of the **Software Architecture** course at **ISEP**. Each bounded context (AuthN, Book, Lending, Reader, Recommendation) is deployed as an independent Spring Boot service, communicating via **RabbitMQ** (AMQP) using a **CQRS** pattern (Command vs. Query).

---

## ✅ Features

- 🔀 **Microservices** per domain:
  - AuthNUserCommand & AuthNUserQuery  
  - BookCommand & BookQuery  
  - LendingCommand & LendingQuery  
  - ReaderCommand & ReaderQuery  
  - RecommendationCommand  
- 📝 **CQRS**: separate write (Command) and read (Query) models  
- 📨 **Event-driven** via RabbitMQ (AMQP)  
- 🧪 Automated tests and **Checkstyle** quality checks  
- ⚙️ CI/CD (Jenkins + SonarQube)  
- 🚀 Containerized with Docker / Docker Compose  
- 📄 Centralized API docs in `/Docs`  

---

## 🛠️ Technologies Used

- **Java 21**  
- **Spring Boot** (one service per module)  
- **Maven 3.8.7** with a multi-module parent POM  
- **RabbitMQ** (AMQP)  
- **Docker** & **Docker Compose**  
- **Jenkins** (CI/CD) + **SonarQube**  
- **Checkstyle** for code standards  
- **Postman** (manual testing)  

---

## ⚙️ Getting Started

### 🔧 1. Prerequisites
- Java 21 (JDK)  
- Maven 3.8.7  
- Docker & Docker Compose  
- RabbitMQ (or use `docker-compose up rabbitmq`)  
- IDE (IntelliJ / VS Code)  

### 📥 2. Clone the Repository
```bash
git clone https://github.com/GuilhermeCunha79/LMS_MicroserviceArchitecture.git
cd LMS_MicroserviceArchitecture
```

### ⚙️ 3. Configure Environment
Copy and edit `.env.template` at project root:
```dotenv
# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASS=guest

# Database (example for Mongo)
MONGO_URI=mongodb://localhost:27017/lms
MYSQL_URL=jdbc:mysql://localhost:3306/lms
MYSQL_USER=root
MYSQL_PASS=admin
```
Or set equivalent environment variables in your shell/CI.

### 📦 4. Build & Package All Services
```bash
./mvnw clean package
```

### 🐳 5. Run with Docker Compose
```bash
docker-compose up --build
```
- RabbitMQ will launch first  
- Each microservice will connect and start on its configured port  
- API docs served from `Docs/` (e.g. `http://localhost:8080/swagger-ui.html`)  

### 🔐 6. OAuth2 Authentication (Optional)
To secure endpoints with Google OAuth2, add to each `application-*.properties`:
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

### 🧪 7. Running Tests & Quality Checks

#### Unit & Integration Tests
```bash
./mvnw test
```

#### Checkstyle
```bash
./mvnw checkstyle:check
```

---

## 🤖 CI/CD with Jenkins
A multi-module `Jenkinsfile` is included at root:
1. 🔄 Code checkout  
2. 🧪 Run tests + Checkstyle  
3. 🔍 SonarQube analysis  
4. 📦 Package Docker images  
5. 🚀 Deploy to remote or default Docker registry  

---

## 🧩 Project Structure
```bash
├── .dockerignore
├── .gitignore
├── .env.template       # sample env for RabbitMQ & DB
├── HELP.md             # developer notes
├── mvnw, mvnw.cmd
├── pom.xml             # parent multi-module POM
├── sonar-project.properties
├── Docs/               # OpenAPI specs & markdown docs
├── AuthNUserCommand/   # write-side service
├── AuthNUserQuery/     # read-side service
├── BookCommand/
├── BookQuery/
├── LendingCommand/
├── LendingQuery/
├── ReaderCommand/
├── ReaderQuery/
├── RecommendationCommand/
├── amqp-rabbitmq/      # shared config/exchange setup
└── checkstyle/         # style rules
```

---

## 👥 Authors

- Guilherme Cunha – [@GuilhermeCunha79](https://github.com/GuilhermeCunha79)
