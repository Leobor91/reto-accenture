# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por �ltimo el inicio y configuraci�n de la aplicaci�n.

Lee el art�culo [Clean Architecture � Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el m�dulo m�s interno de la arquitectura, pertenece a la capa del dominio y encapsula la l�gica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este m�dulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define l�gica de aplicaci�n y reacciona a las invocaciones desde el m�dulo de entry points, orquestando los flujos hacia el m�dulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no est�n arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
gen�ricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patr�n de dise�o [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicaci�n o el inicio de los flujos de negocio.

## Application

Este m�dulo es el m�s externo de la arquitectura, es el encargado de ensamblar los distintos m�dulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma autom�tica, inyectando en �stos instancias concretas de las dependencias declaradas. Adem�s inicia la aplicaci�n (es el �nico m�dulo del proyecto donde encontraremos la funci�n �public static void main(String[] args)�.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**


# Sistema de Administración de Franquicias

Sistema reactivo para la gestión centralizada de franquicias, sucursales e inventario de productos, construido con **Clean Architecture**, **Spring Boot WebFlux** y **PostgreSQL**.

## 🚀 Inicio Rápido

```bash
# Ejecutar aplicación
./gradlew :app-service:bootRun

# Acceder a Swagger UI
open http://localhost:8080/swagger-ui.html
```

## 📚 Documentación API

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api-docs
- **Base URL**: http://localhost:8080/api/v1

### Endpoints Principales

| Tag | Endpoints | Descripción |
|-----|-----------|-------------|
| **Franquicias** | `POST /franchises/create` | Crear franquicia |
| | `PUT /franchises/{id}/name` | Actualizar nombre |
| | `GET /franchises` | Listar todas |
| **Sucursales** | `POST /branches/create` | Crear sucursal |
| | `GET /franchises/{id}/branches` | Listar por franquicia |
| **Productos** | `POST /products/create` | Crear producto |
| | `PUT /products/{id}/stock` | Actualizar stock |
| | `DELETE /products/{id}` | Eliminar producto |
| **Reportes** | `GET /franchises/{id}/top-stock-products` | Top stock por sucursal |

## 🎯 Resultado Final

Al completar esta implementación, el proyecto contará con las siguientes capacidades de documentación:

### 🚀 Características de la API
* ✅ **Swagger UI Interactivo:** Disponible en [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
* ✅ **OpenAPI 3.0 Spec:** Especificación completa con ejemplos y validaciones dinámicas.
* ✅ **Documentación de Endpoints:** Detalle de todos los esquemas de *Request* y *Response*.
* ✅ **Agrupación por Tags:** Organización lógica (Franquicias, Sucursales, Productos, Reportes).
* ✅ **Ejemplos Reales:** Datos de prueba en los cuerpos de solicitud y respuestas.
* ✅ **Validaciones Documentadas:** Reglas de negocio visibles (ej. `stock >= 0`, nombres únicos).
* ✅ **Compatibilidad:** Soporte nativo para *WebFlux functional endpoints*.

---

### 📸 Vista Previa de la Interfaz
La interfaz de Swagger presentará la siguiente estructura:

| Elemento | Detalle |
| :--- | :--- |
| **Tags** | 4 grupos (Franquicias, Sucursales, Productos, Reportes) |
| **Operaciones** | 11 métodos HTTP documentados |
| **Interactividad** | Botón `Try it out` habilitado para pruebas en vivo |
| **Modelos** | Schemas detallados de DTOs y dominio |
| **Respuestas** | Códigos de estado con ejemplos (200, 400, 404, 500) |