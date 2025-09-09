# 📋 TaskManager API

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge\&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-green?style=for-the-badge\&logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-Database-green?style=for-the-badge\&logo=mongodb)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge\&logo=docker)

*Una API REST simple para gestión básica de tareas*

[📖 API](#-api-endpoints) • [🚀 Instalación](#-instalación-rápida) • [🛠️ Configuración](#️-configuración)

</div>

---

## Funcionalidades

* **CRUD de tareas** - Crear, leer, actualizar y eliminar tareas
* **Filtrado básico** - Por estado, prioridad, etiquetas y fechas
* **Paginación** - Para listas de tareas
* **Multi-usuario** - Separación por usuario mediante header
* **Gestión de estados** - PENDING, IN\_PROGRESS, DONE
* **Sistema de etiquetas** - Para categorizar tareas
* **Fechas de vencimiento** - Con validaciones básicas
* **Estimación de tiempo** - En minutos

## Arquitectura

```
TaskManager/
├── Controller Layer    # REST Endpoints
├── Service Layer       # Lógica de negocio  
├── Repository Layer    # Acceso a datos
├── Domain Models       # Entidades
└── DTOs               # Transferencia de datos
```

**Stack Tecnológico:**

* **Java 21** - Lenguaje principal
* **Spring Boot 3.5.4** - Framework web
* **Spring Data MongoDB** - Persistencia
* **Spring Validation** - Validaciones
* **Lombok** - Reducir boilerplate
* **Docker + Docker Compose** - Contenedorización
* **OpenAPI 3** - Documentación automática

---

##  Instalación Rápida

### Con Docker Compose

```bash
# 1. Clonar el repositorio
git clone https://github.com/SantiagoDt/TaskManager.git
cd TaskManager

# 2. Configurar variables de entorno
cp .env.example .env

# 3. Ejecutar con Docker Compose
docker compose up -d
```

### Con Maven Local

```bash
# Prerequisitos: Java 21 + MongoDB ejecutándose
cd tasks-svc
mvn spring-boot:run
```

---

## 📖 API Endpoints

### Gestión de Tareas

| Método   | Endpoint      | Descripción               | Headers Requeridos |
| -------- | ------------- | ------------------------- | ------------------ |
| `POST`   | `/tasks`      | Crear nueva tarea         | `X-USER-ID`        |
| `GET`    | `/tasks/{id}` | Obtener tarea específica  | `X-USER-ID`        |
| `GET`    | `/tasks`      | Listar tareas con filtros | `X-USER-ID`        |
| `PATCH`  | `/tasks/{id}` | Actualizar tarea          | `X-USER-ID`        |
| `DELETE` | `/tasks/{id}` | Eliminar tarea            | `X-USER-ID`        |

### Modelo de Datos

```json
{
  "id": "string",
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

## Ejemplos de Uso

### Crear Tarea

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

### Obtener Tareas Filtradas

```bash
curl "http://localhost:8080/tasks?status=PENDING&priority=HIGH&size=5" \
  -H "X-USER-ID: user123"
```

### Actualizar Estado

```bash
curl -X PATCH http://localhost:8080/tasks/{taskId} \
  -H "Content-Type: application/json" \
  -H "X-USER-ID: user123" \
  -d '{"status": "DONE"}'
```

### Estados y Prioridades

**Estados:**

* `PENDING` - Tarea pendiente
* `IN_PROGRESS` - En progreso
* `DONE` - Completada

**Prioridades:**

* `LOW` - Baja prioridad
* `MEDIUM` - Prioridad media
* `HIGH` - Alta prioridad

---

## 📚 Documentación API

Una vez ejecutando la aplicación:

* **Swagger UI**: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)
* **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
* **Mongo Express**: [http://localhost:8081](http://localhost:8081)

---

## Validaciones

### Creación de Tareas

* **Título**: Obligatorio, máximo 20 caracteres
* **Descripción**: Opcional, máximo 200 caracteres
* **Fecha vencimiento**: Debe ser presente o futura
* **Minutos estimados**: Entre 1 y 1440 (24 horas)

---

## 🤝 Contribuir

1. Fork el repositorio
2. Crea tu feature branch (`git checkout -b feature/NuevaFeature`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/NuevaFeature`)
5. Abre un Pull Request

---

<div align="center">

Hecho con ❤️ por [Santiago](https://github.com/SantiagoDt)

</div>
