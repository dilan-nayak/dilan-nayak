# ShopLite Platform

This repository hosts the evolving ShopLite microservices platform. The first milestone introduces the **catalog-service**, a Spring Boot application that provides CRUD operations for managing product metadata.

## Modules

- `catalog-service` – exposes REST APIs for creating, reading, updating and deleting products. Uses Spring Data JPA with Flyway migrations and is ready for PostgreSQL deployments.

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.9+
- Optional: Docker (for running PostgreSQL locally)

### Run Tests

```bash
mvn test
```

> **Note:** The project depends on Maven Central to download Spring Boot artifacts.

### Run the Catalog Service

```bash
mvn -pl catalog-service spring-boot:run
```

The service listens on `http://localhost:8081` by default.

### Sample Requests

```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"pen","price":10.0,"stockQty":100}'
```

```bash
curl http://localhost:8081/api/products
```

## Database Configuration

- **Development (default)**: In-memory H2 database with Flyway migrations.
- **Production**: Configure the `application-prod.yml` profile to point at a PostgreSQL instance.

## Health Checks

The service exposes Actuator endpoints with `/actuator/health`, `/actuator/info`, and `/actuator/metrics` enabled.
