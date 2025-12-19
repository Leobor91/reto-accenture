# Sistema de Administración de Franquicias - Clean Architecture

Sistema reactivo para la gestión centralizada de franquicias, sucursales e inventario de productos, construido con **Clean Architecture**, **Spring Boot WebFlux** y **PostgreSQL**.

## 📋 Tabla de Contenidos

- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Inicio Rápido](#-inicio-rápido)
- [Documentación API](#-documentación-api)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Patrones de Diseño](#-patrones-de-diseño)
- [Base de Datos](#-base-de-datos)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Desarrollo](#-desarrollo)

---

## 🏛️ Arquitectura

Este proyecto implementa **Clean Architecture** con una clara separación de responsabilidades en capas concéntricas.

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

### Principios Arquitectónicos

1. **Independencia de Frameworks**: La lógica de negocio no depende de Spring, PostgreSQL ni ninguna tecnología externa.
2. **Testeable**: Los casos de uso pueden probarse sin UI, DB o servidor web.
3. **Independencia de UI**: La UI puede cambiar sin afectar el dominio.
4. **Independencia de BD**: Puedes cambiar PostgreSQL por MongoDB sin tocar las reglas de negocio.
5. **Regla de Dependencia**: Las dependencias apuntan hacia adentro (dominio).

### Flujo de Petición

```
HTTP Request → RouterConfig → Handler → UseCase → Gateway (Interface) → Adapter → Database
                  ↓              ↓         ↓           ↓                    ↓
            Entry Point    Validation  Business    Domain Layer      Infrastructure
```

**Ejemplo Real del Código:**

```java
// 1. RouterConfig define rutas funcionales
POST("/api/v1/franchises/create") → FranchiseHandler::create

// 2. Handler valida y delega
public Mono<ServerResponse> create(ServerRequest request) {
    return request.bodyToMono(FranchiseRequest.class)
        .flatMap(req -> createUseCase.execute(req.getName()))  // 3. UseCase ejecuta lógica
        .flatMap(franchise -> ok().bodyValue(ApiResponseDto.success(franchise)))
        .onErrorResume(this::handleError);
}

// 3. UseCase orquesta (domain/usecase)
public Mono<Franchise> execute(String name) {
    return franchiseGateway.findByName(name)  // 4. Gateway (interface en dominio)
        .flatMap(existing -> Mono.error(new IllegalArgumentException("Ya existe")))
        .switchIfEmpty(franchiseGateway.save(Franchise.builder().name(name).build()));
}

// 4. Adapter implementa Gateway (infrastructure)
@Override
public Mono<Franchise> save(Franchise franchise) {
    return Mono.fromCallable(() -> 
        mapper.toModel(repository.save(mapper.toEntity(franchise))))
        .subscribeOn(Schedulers.boundedElastic());  // 🔑 Crítico: Offload blocking JDBC
}
```

---

## 🛠️ Tecnologías

### Core
- **Java 17** - LTS con features modernos (records, pattern matching)
- **Spring Boot 3.2.0** - Framework base
- **Spring WebFlux** - Programación reactiva no bloqueante
- **Project Reactor** - Reactive Streams (Mono/Flux)

### Persistencia
- **Spring Data JDBC** - Acceso a base de datos (sin ORM pesado)
- **PostgreSQL 15** - Base de datos relacional
- **HikariCP** - Connection pool de alto rendimiento
- **Flyway** - Migraciones de base de datos

### Utilities
- **Lombok** - Reducción de boilerplate
- **MapStruct** - Mapeo entity ↔ domain model
- **Validation API** - Validaciones declarativas

### Testing
- **JUnit 5** - Testing framework
- **Mockito** - Mocking
- **Reactor Test** - StepVerifier para flujos reactivos
- **JaCoCo** - Cobertura de código

### Documentación
- **SpringDoc OpenAPI** - Generación automática de Swagger UI
- **Swagger UI** - Interfaz interactiva de API

### DevOps
- **Gradle 8.5** - Build tool con multi-módulo
- **Docker** - Containerización
- **Render.com** - Platform as a Service (deployment)

---

## 🚀 Inicio Rápido

### Prerrequisitos

```bash
# Versiones requeridas
Java 17+
Gradle 8.5+
PostgreSQL 15+ (o usar Neon.tech)
Docker (opcional para deployment)
```

### Instalación Local

```bash
# 1. Clonar repositorio
git clone https://github.com/tu-usuario/reto-accenture.git
cd reto-accenture

# 2. Configurar base de datos (opción A: Local)
# Crear base de datos PostgreSQL
createdb franquicia

# Opción B: Usar Neon.tech (PostgreSQL serverless)
# Copiar DATABASE_URL desde https://neon.tech

# 3. Configurar variables de entorno
export DATABASE_URL="jdbc:postgresql://localhost:5432/franquicia"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="tu_password"

# 4. Ejecutar aplicación
./gradlew :app-service:bootRun

# 5. Verificar health
curl http://localhost:8080/actuator/health

# 6. Acceder a Swagger UI
open http://localhost:8080/swagger-ui.html

# 7. Servicio Desplegado en render
open https://reto-accenture.onrender.com
```



### Usando Docker

```bash
# Build imagen
docker build -t franquicia-api:latest .

# Run contenedor
docker run -d \
  -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/franquicia" \
  -e DATABASE_USERNAME="postgres" \
  -e DATABASE_PASSWORD="postgres" \
  --name franquicia-api \
  franquicia-api:latest

# Ver logs
docker logs -f franquicia-api
```

---

## 📚 Documentación API

### Base URL
| Recurso        | URL                   | 
|----------------|-----------------------|
| **Local**      | http://localhost:8080 |
| **Producción** | https://reto-accenture.onrender.com |



### Acceso Rápido

| Recurso | URL | Descripción |
|---------|-----|-------------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Interfaz interactiva |
| **OpenAPI Spec** | http://localhost:8080/v3/api-docs | JSON/YAML de la API |
| **Health Check** | http://localhost:8080/actuator/health | Estado del servicio |
| **Metrics** | http://localhost:8080/actuator/metrics | Métricas de runtime |

### Endpoints Principales Local

#### 🏢 Franquicias

```bash
# Crear franquicia
curl --location 'http://localhost:8080/api/v1/franchises/create' \
--header 'Content-Type: application/json' \
--data '{
    "nombre_franquicia": "Franquicia Colombia"
}'

# Actualizar nombre
curl --location --request PUT 'http://localhost:8080/api/v1/franchises/1/name' \
--header 'Content-Type: application/json' \
--data '{
    "nombre_franquicia": "Franquicia Colombia Actualizada"
  }'

# Listar todas

curl --location 'http://localhost:8080/api/v1/franchises'
```

#### 🏪 Sucursales

```bash
# Crear sucursal
curl --location 'http://localhost:8080/api/v1/branches/create' \
--header 'Content-Type: application/json' \
--data '{
    "franquicia_id": 1,
    "nombre_sucursal": "Sucursal Bogotá Centro"
}'

# Actualizar nombre

curl --location --request PUT 'http://localhost:8080/api/v1/branches/1/name' \
--header 'Content-Type: application/json' \
--data '{
    "nombre_sucursal": "Sucursal Bogotá Norte"
}'

# Listar por franquicia
curl http://localhost:8080/api/v1/franchises/1/branches
```

#### 📦 Productos

```bash
# Crear producto
curl --location 'http://localhost:8080/api/v1/products/create' \
--header 'Content-Type: application/json' \
--data '{
    "sucursal_id": 1,
    "nombre_producto": "Laptop Dell XPS 15",
    "stock_producto": 25
  }'

# Actualizar stock
curl --location --request PUT 'http://localhost:8080/api/v1/products/1/stock' \
--header 'Content-Type: application/json' \
--data '{
    "stock_producto": 500
  }'
  
# Actualizar nombre
curl --location --request PUT 'http://localhost:8080/api/v1/products/4/name' \
--header 'Content-Type: application/json' \
--data '{
    "nombre_producto": "Laptop Dell XPS 15 (Actualizado)"
  }'
  
# Eliminar producto
curl --location --request DELETE 'http://localhost:8080/api/v1/products/1'

```

#### 📊 Reportes

```bash
# Top productos con mayor stock por sucursal
curl --location 'http://localhost:8080/api/v1/franchises/1/top-stock-products'
```

### Endpoints Principales Desplegado

#### 🏢 Franquicias

```bash
# Crear franquicia
curl --location 'https://reto-accenture.onrender.com/api/v1/franchises/create' \
--header 'Content-Type: application/json' \
--data '{
    "nombre_franquicia": "Franquicia Peru"
}'

# Actualizar nombre
curl --location --request PUT 'https://reto-accenture.onrender.com/api/v1/franchises/1/name' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Franquicia Colombia Actualizada"
  }'

# Listar todas

curl --location 'https://reto-accenture.onrender.com/api/v1/franchises'
```

#### 🏪 Sucursales

```bash
# Crear sucursal
curl --location 'https://reto-accenture.onrender.com/api/v1/branches/create' \
--header 'Content-Type: application/json' \
--data '{
    "franchiseId": 1,
    "name": "Sucursal Bogotá Centro"
  }'

# Actualizar nombre

curl --location --request PUT 'https://reto-accenture.onrender.com/api/v1/branches/4/name' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Sucursal Bogotá Norte"
  }'

# Listar por franquicia
curl --location 'https://reto-accenture.onrender.com/api/v1/franchises/1/branches'
```

#### 📦 Productos

```bash
# Crear producto
curl --location 'https://reto-accenture.onrender.com/api/v1/products/create' \
--header 'Content-Type: application/json' \
--data '{
    "branchId": 1,
    "name": "Laptop Dell XPS 15",
    "stock": 25
  }'

# Actualizar stock
curl --location --request PUT 'https://reto-accenture.onrender.com/api/v1/products/1/stock' \
--header 'Content-Type: application/json' \
--data '{
    "stock": 50
  }'
  
# Actualizar nombre
curl --location --request PUT 'https://reto-accenture.onrender.com/api/v1/products/4/name' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Laptop Dell XPS 15 (Actualizado)"
  }'
  
# Eliminar producto
curl --location --request DELETE 'https://reto-accenture.onrender.com/api/v1/products/1'

```

#### 📊 Reportes

```bash
# Top productos con mayor stock por sucursal
curl --location 'https://reto-accenture.onrender.com/api/v1/franchises/1/top-stock-products'
```

### Respuestas Estándar

```json
// Success (200 OK)
{
  "success": true,
  "message": "Operación exitosa",
  "data": { "id": 1, "name": "Franquicia Central" },
  "timestamp": "2024-12-19T10:30:00Z"
}

// Error de Validación (400 Bad Request)
{
  "success": false,
  "message": "El nombre de la franquicia ya existe",
  "data": null,
  "timestamp": "2024-12-19T10:30:00Z"
}

// Recurso No Encontrado (404 Not Found)
{
  "success": false,
  "message": "Franquicia con ID 999 no encontrada",
  "data": null,
  "timestamp": "2024-12-19T10:30:00Z"
}
```

---

## 📁 Estructura del Proyecto

```
reto-accenture/
├── 📁 domain/                          # Capa de Dominio (Core Business Logic)
│   ├── 📁 model/                       # Modelos de dominio + Interfaces Gateway
│   │   └── src/main/java/
│   │       └── co/com/franquicia/
│   │           ├── 📁 franchise/       # Aggregate Franquicia
│   │           │   ├── Franchise.java  # Domain Model
│   │           │   └── gateways/
│   │           │       └── FranchiseGateway.java  # Port (interface)
│   │           ├── 📁 branch/          # Aggregate Sucursal
│   │           └── 📁 product/         # Aggregate Producto
│   │
│   └── 📁 usecase/                     # Casos de Uso (Application Logic)
│       └── src/main/java/
│           └── co/com/franquicia/usecase/
│               ├── 📁 franchise/
│               │   ├── CreateFranchiseUseCase.java
│               │   ├── UpdateFranchiseNameUseCase.java
│               │   └── GetAllFranchisesUseCase.java
│               ├── 📁 branch/
│               ├── 📁 product/
│               └── 📁 report/
│
├── 📁 infrastructure/                  # Capa de Infraestructura
│   ├── 📁 driven-adapters/            # Implementaciones de Gateways (Salida)
│   │   └── 📁 jpa-repository/
│   │       └── src/main/java/
│   │           └── co/com/franquicia/jpa/
│   │               ├── 📁 adapter/
│   │               │   └── FranchiseRepositoryAdapter.java  # Implementa Gateway
│   │               ├── 📁 entity/
│   │               │   └── FranchiseEntity.java             # JPA Entity
│   │               ├── 📁 mapper/
│   │               │   └── FranchiseEntityMapper.java       # MapStruct
│   │               └── 📁 repository/
│   │                   └── FranchiseJpaRepository.java      # Spring Data
│   │
│   └── 📁 entry-points/               # Puntos de Entrada (Controllers)
│       └── 📁 rest-consumer/
│           └── src/main/java/
│               └── co/com/franquicia/restconsumer/
│                   ├── 📁 config/
│                   │   ├── OpenAPIConfig.java              # Swagger setup
│                   │   └── RouterConfig.java               # Functional routes
│                   ├── 📁 handler/
│                   │   ├── FranchiseHandler.java           # Request handler
│                   │   ├── BranchHandler.java
│                   │   └── ProductHandler.java
│                   └── 📁 dto/
│                       ├── FranchiseRequest.java           # Input DTO
│                       └── ApiResponseDto.java             # Wrapper response
│
├── 📁 applications/                    # Capa de Aplicación
│   └── 📁 app-service/
│       └── src/main/
│           ├── java/co/com/franquicia/
│           │   ├── MainApplication.java                    # @SpringBootApplication
│           │   └── config/
│           │       └── UseCasesConfig.java                 # Auto-register UseCases
│           └── resources/
│               ├── application.yaml                        # Configuración principal
│               ├── schema.sql                              # DDL (tablas)
│               └── import.sql                              # Datos iniciales
│
├── 📁 deployment/                      # Deployment artifacts
│   └── Dockerfile                      # Multi-stage build optimizado
│   
│
├── 📄 build.gradle                     # Root build script
├── 📄 settings.gradle                  # Multi-module setup
├── 📄 main.gradle                      # Clean Architecture plugin config
└── 📄 README.md                        # Este archivo
```

### Dependencias entre Módulos

```
app-service
    ├─→ model (domain models + gateway interfaces)
    ├─→ usecase (business logic)
    ├─→ jpa-repository (database adapter)
    └─→ rest-consumer (HTTP entry point)

rest-consumer
    ├─→ model (DTOs use domain models)
    └─→ usecase (handlers invoke use cases)

jpa-repository
    └─→ model (implements gateway interfaces)

usecase
    └─→ model (uses gateways and domain models)

model
    └─→ (no dependencies - pure domain)
```

---

## 🎨 Patrones de Diseño

### 1. Hybrid Reactive Architecture (CRÍTICO)

**Problema:** Spring WebFlux es no bloqueante pero JDBC es bloqueante.

**Solución:** Usar `Schedulers.boundedElastic()` para offload de operaciones bloqueantes.

```java
// ❌ MAL: Bloquea el event loop de Netty
public Mono<Franchise> save(Franchise franchise) {
    return Mono.just(repository.save(mapper.toEntity(franchise)))
        .map(mapper::toModel);  // Ejecuta JDBC en el thread reactivo
}

// ✅ BIEN: Offload a boundedElastic scheduler
public Mono<Franchise> save(Franchise franchise) {
    return Mono.fromCallable(() -> 
        mapper.toModel(repository.save(mapper.toEntity(franchise))))
        .subscribeOn(Schedulers.boundedElastic());  // Ejecuta en thread pool dedicado
}
```

**Características de `boundedElastic`:**
- Thread pool de 10x CPU cores (default: 10 threads en máquina de 1 core)
- Máximo 100,000 tasks encolados
- Threads con TTL de 60 segundos
- Ideal para operaciones I/O bloqueantes (JDBC, file I/O)

### 2. Gateway Pattern (Ports & Adapters)

**Dominio define interfaces (Ports):**

```java
// domain/model/src/.../gateways/FranchiseGateway.java
public interface FranchiseGateway {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);
    Flux<Franchise> findAll();
    Mono<Franchise> findByName(String name);
}
```

**Infraestructura implementa (Adapters):**

```java
// infrastructure/driven-adapters/jpa-repository/.../FranchiseRepositoryAdapter.java
@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseGateway {
    private final FranchiseJpaRepository repository;
    private final FranchiseEntityMapper mapper;

    @Override
    @Transactional
    public Mono<Franchise> save(Franchise franchise) {
        return Mono.fromCallable(() -> 
            mapper.toModel(repository.save(mapper.toEntity(franchise))))
            .subscribeOn(Schedulers.boundedElastic());
    }
}
```

### 3. Functional Endpoints (Spring WebFlux)

**Ventajas sobre `@RestController`:**
- Más performantes (menos overhead de reflection)
- Mejor composición de rutas
- Testing más simple (sin contexto de Spring)

```java
@Configuration
public class RouterConfig {
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/franchises/create",
            method = RequestMethod.POST,
            beanClass = FranchiseHandler.class,
            beanMethod = "create"
        )
    })
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions
            .route(POST("/api/v1/franchises/create").and(accept(APPLICATION_JSON)),
                handler::create)
            .andRoute(PUT("/api/v1/franchises/{id}/name").and(accept(APPLICATION_JSON)),
                handler::updateName)
            .andRoute(GET("/api/v1/franchises"),
                handler::getAll);
    }
}
```

### 4. Entity-Model Separation

**Entity (Infrastructure):**

```java
@Table(name = "franchises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "franchise")
    private List<BranchEntity> branches;
}
```

**Model (Domain):**

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franchise {
    private Long id;
    private String name;
    // No anotaciones de JPA - dominio puro
}
```

**Mapper (Infrastructure):**

```java
@Mapper(componentModel = "spring")
public interface FranchiseEntityMapper {
    Franchise toModel(FranchiseEntity entity);
    FranchiseEntity toEntity(Franchise model);
}
```

### 5. Use Case Auto-Registration

```java
@Configuration
@ComponentScan(
    basePackages = "co.com.franquicia.usecase",
    includeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX, 
        pattern = "^.+UseCase$"
    ),
    useDefaultFilters = false
)
public class UseCasesConfig {}
```

**Consecuencia:** Todas las clases que terminen en `UseCase` se registran automáticamente como beans de Spring.

---

## 🗄️ Base de Datos

### Esquema

```sql
-- Franquicias
CREATE TABLE franchises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Sucursales
CREATE TABLE branches (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    franchise_id BIGINT NOT NULL,
    FOREIGN KEY (franchise_id) REFERENCES franchises(id) ON DELETE CASCADE,
    UNIQUE (name, franchise_id)
);

-- Productos
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL CHECK (stock >= 0),
    branch_id BIGINT NOT NULL,
    FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE CASCADE,
    UNIQUE (name, branch_id)
);
```

### Reglas de Negocio Implementadas

| Regla | Implementación |
|-------|----------------|
| Nombres únicos de franquicias | `UNIQUE CONSTRAINT` + validación en UseCase |
| Nombres únicos de sucursales por franquicia | `UNIQUE (name, franchise_id)` |
| Nombres únicos de productos por sucursal | `UNIQUE (name, branch_id)` |
| Stock no negativo | `CHECK (stock >= 0)` + validación en UseCase |
| Cascade delete | `ON DELETE CASCADE` en FKs |

### Configuración de Connection Pool

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20          # Máximo 20 conexiones
      minimum-idle: 5                # Mínimo 5 idle
      connection-timeout: 30000      # 30s timeout
      idle-timeout: 600000           # 10min idle antes de cerrar
      max-lifetime: 1800000          # 30min lifetime máximo
      pool-name: FranquiciaHikariCP
      connection-test-query: SELECT 1
```

**Para Render.com Free Tier (512MB RAM):**

```yaml
hikari:
  maximum-pool-size: 10   # Reducido para bajo RAM
  minimum-idle: 2
```

---

## 🧪 Testing

### Estrategia de Testing

```
Unit Tests (domain/usecase) → Integration Tests (adapters) → E2E Tests (API)
        99%                           90%                          80%
```

### Unit Testing de Use Cases

```java
@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {
    @Mock
    private FranchiseGateway franchiseGateway;
    
    @InjectMocks
    private CreateFranchiseUseCase useCase;

    @Test
    void shouldCreateFranchise_WhenNameIsUnique() {
        // Given
        String name = "Nueva Franquicia";
        Franchise expected = Franchise.builder().id(1L).name(name).build();
        
        when(franchiseGateway.findByName(name)).thenReturn(Mono.empty());
        when(franchiseGateway.save(any())).thenReturn(Mono.just(expected));

        // When & Then
        StepVerifier.create(useCase.execute(name))
            .expectNext(expected)
            .verifyComplete();
    }

    @Test
    void shouldThrowException_WhenNameAlreadyExists() {
        // Given
        String name = "Existente";
        Franchise existing = Franchise.builder().id(1L).name(name).build();
        
        when(franchiseGateway.findByName(name)).thenReturn(Mono.just(existing));

        // When & Then
        StepVerifier.create(useCase.execute(name))
            .expectErrorMatches(ex -> 
                ex instanceof IllegalArgumentException &&
                ex.getMessage().contains("ya existe"))
            .verify();
    }
}
```

### Integration Testing de Adapters

```java
@DataJdbcTest
@Import({FranchiseRepositoryAdapter.class, FranchiseEntityMapperImpl.class})
class FranchiseRepositoryAdapterTest {
    @Autowired
    private FranchiseRepositoryAdapter adapter;

    @Test
    void shouldSaveFranchise() {
        // Given
        Franchise franchise = Franchise.builder().name("Test").build();

        // When
        Franchise saved = adapter.save(franchise).block();

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test");
    }
}
```

### Comandos de Testing

```bash
# Ejecutar todos los tests
./gradlew test

# Test con cobertura
./gradlew test jacocoMergedReport

# Ver reporte de cobertura
open build/reports/jacocoMergedReport/html/index.html

# Test de un módulo específico
./gradlew :usecase:test

# Test con logs detallados
./gradlew test --info

# Test en modo continuo
./gradlew test --continuous
```

### Cobertura de Código

```bash
# Ubicación de reportes
build/reports/
├── jacocoMergedReport/
│   ├── html/index.html         # Reporte HTML interactivo
│   └── jacocoMergedReport.xml  # Para CI/CD (SonarQube, etc.)
└── tests/
    └── test/index.html          # Resultados de tests
```

**Configuración de JaCoCo:**

```gradle
jacocoTestReport {
    reports {
        xml.required = true   // Para CI/CD
        html.required = true  // Para desarrolladores
    }
    
    // Excluir de cobertura
    classDirectories.setFrom(files(classDirectories.files.collect {
        fileTree(dir: it, exclude: [
            '**/MainApplication.class',
            '**/config/**',
            '**/*Entity.class'
        ])
    }))
}
```

---

## 🚢 Deployment

### Render.com (Recomendado para Free Tier)

#### Configuración en `render.yaml`

```yaml
services:
  - type: web
    name: franquicia-api
    runtime: docker
    plan: free
    healthCheckPath: /actuator/health
    
    envVars:
      - key: DATABASE_URL
        fromDatabase:
          name: franquicia-postgres
          property: connectionString
      - key: DATABASE_USERNAME
        fromDatabase:
          name: franquicia-postgres
          property: user
      - key: DATABASE_PASSWORD
        fromDatabase:
          name: franquicia-postgres
          property: password

databases:
  - name: franquicia-postgres
    databaseName: franquicia
    user: franquicia_user
    plan: free
```

#### Deploy Automático

```bash
# 1. Conectar repositorio en Render Dashboard
# 2. Render detecta render.yaml automáticamente
# 3. Deploy se ejecuta en cada push a main

# Forzar re-deploy manual
git commit --allow-empty -m "trigger deploy"
git push origin main
```

#### Troubleshooting de Deploy

**Error: `validateStructure` fails en Docker**

```dockerfile
# ❌ Falla en Render
RUN gradle :app-service:bootJar --no-daemon

# ✅ Solución: Skip validateStructure
RUN gradle :app-service:bootJar --no-daemon -x validateStructure
```

**Error: `Could not read script '/app/main.gradle'`**

```dockerfile
# ❌ Falta copiar main.gradle
COPY build.gradle settings.gradle ./

# ✅ Incluir main.gradle explícitamente
COPY build.gradle settings.gradle main.gradle ./
```

### Docker Local

```bash
# Build optimizado (multi-stage)
docker build -t franquicia:latest .

# Run con variables de entorno
docker run -d \
  -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://..." \
  -e DATABASE_USERNAME="user" \
  -e DATABASE_PASSWORD="pass" \
  -e JAVA_OPTS="-Xmx512m" \
  franquicia:latest

# Ver logs
docker logs -f <container_id>

# Inspect recursos
docker stats <container_id>
```

### Variables de Entorno Requeridas

```bash
# Database
DATABASE_URL=jdbc:postgresql://host:5432/dbname
DATABASE_USERNAME=user
DATABASE_PASSWORD=password

# JVM (opcional - optimizado para Render Free Tier)
JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_CO_COM_FRANQUICIA=DEBUG
```

---

## 💻 Desarrollo

### Comandos Gradle Útiles

```bash
# Build completo
./gradlew build

# Limpiar build artifacts
./gradlew clean

# Ejecutar aplicación
./gradlew :app-service:bootRun

# Build sin tests
./gradlew build -x test

# Build sin validateStructure (si hay issues con Clean Architecture plugin)
./gradlew :app-service:bootJar -x validateStructure

# Ver dependencias
./gradlew :app-service:dependencies

# Ver tasks disponibles
./gradlew tasks

# Formatear código (si tienes plugin)
./gradlew spotlessApply

# Análisis estático (si tienes plugin)
./gradlew sonarqube
```

### Estructura de Branches

```
main                    # Producción (protegida)
├── develop             # Integración (default)
│   ├── feature/*       # Nuevas funcionalidades
│   ├── fix/*           # Correcciones de bugs
│   └── refactor/*      # Mejoras de código
└── hotfix/*            # Fixes urgentes en producción
```

### Convenciones de Código

#### Nombres de Clases

```java
// Use Cases
CreateFranchiseUseCase.java
UpdateBranchNameUseCase.java

// Handlers
FranchiseHandler.java
ProductHandler.java

// Adapters
FranchiseRepositoryAdapter.java

// Entities
FranchiseEntity.java

// Gateways (interfaces)
FranchiseGateway.java
```

#### Estructura de Packages

```
co.com.franquicia
├── franchise           # Por Aggregate
│   ├── Franchise.java
│   └── gateways/
│       └── FranchiseGateway.java
├── branch
├── product
└── common             # Utilities compartidas
```


```

### Hot Reload en Desarrollo

```yaml
# application-dev.yaml
spring:
  devtools:
    restart:
      enabled: true
    livereload:
      enabled: true
```

```bash
# Ejecutar con devtools
./gradlew :app-service:bootRun --args='--spring.profiles.active=dev'
```

---

## 📖 Recursos Adicionales

### Artículos Recomendados

- [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)
- [Spring WebFlux - Reactive Programming](https://spring.io/reactive)
- [Project Reactor - Reference Guide](https://projectreactor.io/docs/core/release/reference/)
- [Schedulers in Reactor](https://projectreactor.io/docs/core/release/reference/#schedulers)

### Documentación Oficial

- **Spring Boot:** https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
- **Spring WebFlux:** https://docs.spring.io/spring-framework/reference/web/webflux.html
- **Spring Data JDBC:** https://spring.io/projects/spring-data-jdbc
- **SpringDoc OpenAPI:** https://springdoc.org/
- **Render.com:** https://render.com/docs

### Herramientas de Desarrollo

- **Postman Collection:** `docs/postman/Franquicias.postman_collection.json`
- **Database Migrations:** `applications/app-service/src/main/resources/db/migration/`
- **OpenAPI Spec Export:** http://localhost:8080/v3/api-docs.yaml

---

## 🤝 Contribuir

1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/nueva-funcionalidad`)
3. Commit con mensaje semántico (`git commit -m 'feat: agregar nueva funcionalidad'`)
4. Push a la branch (`git push origin feature/nueva-funcionalidad`)
5. Abrir Pull Request

### Checklist de PR

- [ ] Tests unitarios agregados/actualizados
- [ ] Documentación actualizada (README, Swagger)
- [ ] Build pasa localmente (`./gradlew build`)
- [ ] Cobertura de código > 80% (`./gradlew jacocoMergedReport`)
- [ ] Sin warnings de compilación
- [ ] Commit messages siguen convención semántica
- [ ] PR description explica el "por qué" del cambio

---

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

---

## 👥 Autores

- **Equipo de Desarrollo** - [GitHub](https://github.com/Leobor91/reto-accenture)
- **Equipo de Desarrollo** - [LinkedIn - Leonel Borja](https://www.linkedin.com/in/leonel-borja-vargas-92a97b215/)

---

## 🙏 Agradecimientos

- Clean Architecture - Robert C. Martin (Uncle Bob)
- Spring Team por el excelente framework
- Bancolombia Tech por el plugin de Clean Architecture
- Comunidad de Project Reactor

---

**Última Actualización:** Diciembre 2024  
**Versión:** 1.0.0  
**Estado:** ✅ En Producción (Render.com)

---

## 📞 Soporte

- **Issues:** https://github.com/Leobor91/reto-accenture/issues
- **Discussions:** https://github.com/Leobor91/reto-accenture/discussions
- **Email:** leonel.borja9119@gmail.com

---

## 🎯 Roadmap

- [ ] Agregar autenticación JWT
- [ ] Implementar caché con Redis
- [ ] Agregar eventos asíncronos (Kafka/RabbitMQ)
- [ ] Implementar rate limiting
- [ ] Agregar métricas con Prometheus
- [ ] Implementar tracing distribuido (Zipkin/Jaeger)
- [ ] Migrar a R2DBC (full reactive stack)
- [ ] Agregar GraphQL endpoint