# To-Do Web App | Evolución de Arquitectura con Spring Boot 

**Trabajo de 1º Programación de DAM.** Este proyecto es una aplicación web completa para la gestión de tareas (To-Do List) desarrollada con Java y Spring Boot. 

Lo más destacado de este repositorio es que muestra la **evolución técnica de la aplicación a través de 3 fases distintas**, demostrando capacidad de adaptación a diferentes sistemas de almacenamiento de datos.

## Estructura del Repositorio (Versiones)

El proyecto está dividido en tres carpetas, cada una representando una evolución en la persistencia de datos:

1. **`01-version-memoria`**: La implementación inicial. Los datos se almacenan en estructuras de datos temporales (en memoria). Ideal para testear la lógica de los controladores MVC sin depender de servicios externos.
2. **`02-version-mariadb`**: Evolución a persistencia real. Conexión directa a una base de datos relacional MariaDB mediante consultas SQL para garantizar que los datos sobrevivan al reinicio del servidor.
3. **`03-version-persistencia`**: Refactorización final implementando frameworks de persistencia avanzados. Mapeo Objeto-Relacional (ORM) para un código más limpio, escalable y mantenible.

## 🛠️ Tecnologías Utilizadas
- **Backend Framework:** Java, Spring Boot, Spring MVC.
- **Bases de Datos:** MariaDB.
- **Arquitectura:** Patrón MVC (Modelo-Vista-Controlador).
- **Gestión de Datos:** Operaciones CRUD (Crear, Leer, Actualizar, Borrar).
