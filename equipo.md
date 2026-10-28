# Equipo

| Nombre | Usuario GitHub | Slice o responsabilidad | Revisión realizada |
|---|---|---|---|
| Irene | `@ifonlob` | Slice de **consulta**: `GET /books` y `GET /books/{id}`. Modelo `Book`, `BookResponseDTO`, `list()` y `get(Long)` en el servicio, `BookNotFoundException` en la consulta y mapeo `404` en el advice. Tests MockMvc de consulta y tests unitarios del servicio. Documentación del contrato en README y `ejemplos.http`. | Revisión del slice de escritura de Jordi (PR de `POST`/`PUT`/`DELETE`): códigos `201`/`204`, `Location` y validación. |
| Jordi | `@jvazlop` | Slice de **escritura**: `POST /books`, `PUT /books/{id}` y `DELETE /books/{id}`. `BookRequestDTO`, generación de ids, `create`/`update`/`delete` en el servicio, `201` + `Location`, `204`, `InvalidBookException` y mapeo `400`/`VALIDATION_ERROR`. Tests de alta, modificación y borrado. Mantenimiento del workflow de CI. | Revisión del slice de consulta de Irene (PR de `GET` y `GET /{id}`): DTO de respuesta, `404` y tests. |

## Reparto por entrega

| Entrega | Irene | Jordi |
|---|---|---|
| 1 — Eco de texto | Ejecuta la app, documenta las respuestas HTTP y redacta las respuestas de comprensión. | Verifica el contrato con `curl -i`, revisa el cambio y actualiza las evidencias de equipo. |
| 2 — JSON | `Book`, `BookResponseDTO` y los `GET`. | `BookRequestDTO`, colección en memoria, generación de ids, `POST`, `PUT` y `DELETE`. |
| 3 — `ResponseEntity` | `ResponseEntity.ok` en consultas y revisión del contrato. | `201` + `Location` en el alta y `204` en el borrado. |
| 4 — Servicio | `list()` y `get(Long)` + tests unitarios del servicio. | `create`, `update` y `delete` + controlador delgado. |
| 5 — Excepciones | `BookNotFoundException` lanzada desde `get` y su mapeo a `404`. | `InvalidBookException` desde `create`/`update` y su mapeo a `400`. |
| 6 — Advice global | `GlobalExceptionHandler` para `NOT_FOUND` y `VALIDATION_ERROR`. | `BAD_REQUEST` por JSON ilegible, `INTERNAL_ERROR` inesperado y `@Valid` en el DTO. |
| Cierre | README, `ejemplos.http` y pruebas MockMvc de consulta. | CI, regresión del CRUD real y pruebas de escritura. |
