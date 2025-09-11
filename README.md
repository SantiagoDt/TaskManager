# 📋 TaskManager API

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-green?style=for-the-badge&logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-Database-green?style=for-the-badge&logo=mongodb)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge&logo=docker)
![JWT](https://img.shields.io/badge/JWT-Authentication-red?style=for-the-badge&logo=jsonwebtokens)

*API REST de microservicios para gestión básica de tareas con autenticación JWT*

[ API](#-api-endpoints) • [ Instalación](#-instalación-rápida) • [ Configuración](#️-configuración) • [ Roadmap](#-roadmap)

</div>

---

## ✨ Funcionalidades

### 🔑 **Auth Service (auth-svc)** 
- **Registro de usuarios** (username, email y contraseña)
- **Login con bloqueo** - 5 intentos fallidos → bloqueo 15 minutos
- **JWT HS256** - Emisión con `sub` (userId), `iss`, `iat`, `exp`
- **BCrypt hashing** - Contraseñas hasheadas de forma segura
- **Validaciones robustas** - Spring Validation integrado
- **Dockerizado** - Multi-stage build optimizado

### 📌 **Tasks Service (tasks-svc)** 
- **CRUD completo** - Crear, leer, actualizar y eliminar tareas
- **Filtrado básico** - Por estado, prioridad, etiquetas y fechas
- **Paginación** - En listados de tareas
- **Multi-usuario** - Separación por usuario mediante header `X-USER-ID`
- **Gestión de estados** - PENDING, IN_PROGRESS, DONE
- **Sistema de etiquetas** - Para categorizar tareas
- **Fechas de vencimiento** - Con validaciones temporales
- **Estimación de tiempo** - En minutos

>  
> [!NOTE]  
> **En desarrollo**: Migración de `X-USER-ID` a `Authorization: Bearer <token>.`
---

##  Arquitectura

```
TaskManager/
├── auth-svc/              #  Microservicio de autenticación
│   ├── src/main/java/     # Código fuente Java
│   ├── Dockerfile         # Multi-stage build 
│   └── .env.example       # Variables de entorno
├── tasks-svc/             #  Microservicio de tareas  
│   ├── src/main/java/     # Código fuente Java
│   ├── Dockerfile         # Multi-stage build 
│   └── .env.example       # Variables de entorno
├── compose.yaml           # Orquestación Docker 
├── .env.example           # Variables globales (BBDD)
└── .github/workflows/     # CI/CD con GitHub Actions
```

**Stack Tecnológico:**
- **Java 21** - Lenguaje principal
- **Spring Boot 3.5.4** - Framework web y microservicios
- **Spring Data MongoDB** - Persistencia NoSQL
- **Spring Security** - Base para JWT
- **JWT + BCrypt** - Autenticación y hashing seguro
- **Lombok** - Reducción de boilerplate
- **Docker + Docker Compose** - Contenedorización
- **OpenAPI 3** - Documentación automática

---

##  Instalación Rápida

###  Con Docker Compose 

```bash
# 1. Clonar el repositorio
git clone https://github.com/SantiagoDt/TaskManager.git
cd TaskManager

# 2. Configurar variables de entorno
cp .env.example .env                   
cp auth-svc/.env.example auth-svc/.env  
cp tasks-svc/.env.example tasks-svc/.env 

# 3. Levantar todos los servicios
docker compose up -d

# 4. Verificar estado
docker compose ps
```

**Servicios disponibles:**
- **Auth Service** → http://localhost:8083
- **Tasks Service** → http://localhost:8080  
- **Mongo Express** → http://localhost:8081

---

##  Configuración

###  Variables de Entorno

#### `.env` (raíz del proyecto - BBDD)
```env
# === MONGODB GLOBAL ===
MONGO_USER=admin
MONGO_PASS=securepass2024
ME_USER=dbadmin
ME_PASS=dbadmin2024
```

#### `auth-svc/.env` (Auth Service)
```env
# === SERVICE CONFIG ===
AUTH_PORT=8083

# === MONGODB CONNECTION ===
MONGO_HOST=mongo
MONGO_PORT=27017
MONGO_DB=userdb
MONGO_USER=admin
MONGO_PASS=securepass2024
MONGO_AUTHDB=admin

# === JWT CONFIGURATION ===
JWT_SECRET=9v2i8u1x4z7b0e3g6j9m2p5s8w1a4d7g0j3m6p9s2v5y8b
JWT_EXPIRATION=2
JWT_ISSUER=auth-svc
JWT_AUDIENCES=auth-svc
```

#### `tasks-svc/.env` (Tasks Service)
```env
# === SERVICE CONFIG ===
SPRING_APPLICATION_NAME=tasks-svc
MONGO_HOST=mongo
MONGO_PORT=27017
MONGO_DB=tasksdb
MONGO_USER=admin
MONGO_PASS=securepass2024
MONGO_AUTHDB=admin
SPRING_PROFILES_ACTIVE=local
PORT=8080

# === MONGO EXPRESS CONFIG ===
ME_USER=dbadmin
ME_PASS=dbadmin2024
```
> [!IMPORTANT]  
> Estas variables de entorno son **ejemplos**.  
> Debes crear y configurar los archivos `.env` en el directorio correspondiente de cada servicio:  
> - `.env` en la raíz (configuración global: Mongo y Mongo Express)  
> - `auth-svc/.env` (configuración del microservicio de autenticación)  
> - `tasks-svc/.env` (configuración del microservicio de tareas)  
> 
> Asegúrate de ajustar puertos, credenciales y secretos JWT antes de arrancar los servicios con `docker compose up -d`.
---

##  API Endpoints

###  Auth Service (puerto 8083)

| Método | Endpoint         | Descripción             | Headers          |
|--------|------------------|-------------------------|------------------|
| `POST` | `/auth/register` | Registrar nuevo usuario | `Content-Type`   |
| `POST` | `/auth/login`    | Login y obtener JWT     | `Content-Type`   |

####  Registro de Usuario
```bash
curl -X POST http://localhost:8083/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "santiago",
    "email": "santi@example.com",
    "password": "supersecret123"
  }'
```

####  Login y JWT
```bash
curl -X POST http://localhost:8083/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "santi@example.com",
    "password": "supersecret123"
  }'
```

**Respuesta de Login:**
```json
{
  "message": "Login successful",
  "username": "santi",
  "email": "santi@example.com",
  "lastLoginAt": "2025-09-11T11:18:30.569Z",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2NmUxY..."
}
```

---

###  Tasks Service (puerto 8080)

| Método   | Endpoint      | Descripción               | Headers Actuales     |
|----------|---------------|---------------------------|---------------------|
| `POST`   | `/tasks`      | Crear nueva tarea         | `X-USER-ID`         |
| `GET`    | `/tasks/{id}` | Obtener tarea específica  | `X-USER-ID`         |
| `GET`    | `/tasks`      | Listar tareas con filtros | `X-USER-ID`         |
| `PATCH`  | `/tasks/{id}` | Actualizar tarea          | `X-USER-ID`         |
| `DELETE` | `/tasks/{id}` | Eliminar tarea            | `X-USER-ID`         |

####  Crear Tarea
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -H "X-USER-ID: user123" \
  -d '{
    "title": "Revisar código",
    "description": "Code review del PR #42",
    "tags": ["code-review"],
    "priority": "HIGH",
    "dueAt": "2024-01-15T17:00:00Z",
    "estimatedMinutes": 60
  }'
```

#### 🔍 Filtrar Tareas
```bash
curl "http://localhost:8080/tasks?status=PENDING&priority=HIGH&size=5" \
  -H "X-USER-ID: user123"
```

---

##  Modelo de Datos

###  Usuario (auth-svc)
```json
{
  "id": "66e1c...",
  "username": "santiago",
  "email": "santi@example.com",
  "passwordHash": "bcrypt-hashed-password",
  "role": "USER",
  "isEmailVerified": false,
  "failedLoginAttempts": 0,
  "accountLockedUntil": null,
  "createdAt": "2025-09-11T10:00:00Z",
  "updatedAt": "2025-09-11T10:00:00Z"
}
```

###  Tarea (tasks-svc)
```json
{
  "id": "66e1d...",
  "title": "Completar documentación",
  "description": "Escribir README del proyecto",
  "tags": ["documentation"],
  "priority": "MEDIUM",
  "status": "PENDING",
  "dueAt": "2024-01-15T18:00:00Z",
  "estimatedMinutes": 60,
  "createdAt": "2024-01-10T10:00:00Z",
  "updatedAt": "2024-01-10T10:00:00Z",
  "finishedAt": null,
  "userId": "user123"
}
```

---

##  Documentación Interactiva

### 🌐 URLs Disponibles
- **Tasks Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Auth Swagger UI**: [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)
- **Tasks OpenAPI**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Auth OpenAPI**: [http://localhost:8083/v3/api-docs](http://localhost:8083/v3/api-docs)
- **Mongo Express**: [http://localhost:8081](http://localhost:8081) 

---

## ✅ Validaciones Implementadas

### 🔑 Auth Service
- **Username**: 3-20 caracteres, obligatorio
- **Email**: Formato válido, obligatorio, único
- **Password**: Mínimo 10 caracteres, obligatorio
- **Login**: Máximo 5 intentos (bloqueo 15 min)

### 📌 Tasks Service
- **Título**: Obligatorio, máximo 20 caracteres
- **Descripción**: Opcional, máximo 200 caracteres
- **Fecha vencimiento**: Presente o futura
- **Minutos estimados**: Entre 1 y 1440 (24 horas)
- **Estados**: PENDING, IN_PROGRESS, DONE
- **Prioridades**: LOW, MEDIUM, HIGH

---

##  Roadmap

###  **Completado**
- [x] **Auth Service** - Registro, login, JWT con BCrypt
- [x] **Tasks CRUD** - Operaciones completas de tareas
- [x] **Docker Compose** - Orquestación de microservicios
- [x] **Validaciones** - Spring Validation en ambos servicios
- [x] **MongoDB** - Persistencia con Mongo Express UI
- [x] **OpenAPI Docs** - Swagger UI automático

###  **En Desarrollo**
- [ ] **JWT Integration** - Reemplazar `X-USER-ID` por `Authorization: Bearer <token>`
- [ ] **JWT Filter** - Middleware de autenticación en tasks-svc
- [ ] **Error Handling** - Manejo centralizado de errores



---

##  Testing

###  Ejecutar Tests
```bash
# Tests unitarios auth-svc
cd auth-svc && mvn test

# Tests unitarios tasks-svc  
cd tasks-svc && mvn test

```
 Tests Incluidos
- **Unit Tests** - JUnit 5 + Spring Boot Test
- **Repository Tests** - @DataMongoTest
- **Controller Tests** - @WebMvcTest con MockMvc
- **Validation Tests** - DTO y constraint validation

---

## 🤝 Contribuir

1. **Fork** el repositorio
2. **Crea** tu feature branch (`git checkout -b feature/NuevaFeature`)
3. **Commit** tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. **Push** al branch (`git push origin feature/NuevaFeature`)
5. **Abre** un Pull Request

---

<div align="center">

**Hecho con ❤️ por [Santiago](https://github.com/SantiagoDt)**


</div>
