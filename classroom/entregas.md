# Contrato de las seis entregas de Book Catalog

Cada etapa es un checkpoint de **la misma práctica de grupo**. Activad solo su
semilla con `java classroom/ActivarEntrega.java N`; después `./mvnw test`.
El código inicial solo implementa el eco de la primera etapa.

## Tipos y rutas del contrato

Desde entrega 2, entrada JSON:

```json
{"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
```

Respuesta: esos cuatro campos más `id` numérico generado por la aplicación.
Se usarán estos nombres para poder compartir las pruebas del grupo:

- `com.example.catalog.dto.BookRequestDTO(String isbn, String title, String author, int year)`.
- `com.example.catalog.dto.BookResponseDTO(Long id, String isbn, String title, String author, int year)`.
- Modelo interno `Book` en el paquete `model`, con la forma que decidáis;
  **no es una entidad JPA**. El controlador devuelve DTOs.

La ruta del catálogo es **`/books`**, no `/api/books`. El almacenamiento es en
memoria y no hay usuarios, autenticación ni base de datos obligatoria.

| Operación | Entrega 1 | Entrega 2 | Desde entrega 3 |
|---|---|---|---|
| `GET /books` | `200`, `GET /books` | `200`, array JSON | Igual |
| `GET /books/{id}` existente | `200`, eco con id | `200`, DTO JSON | Igual |
| `POST /books` | `200`, `POST /books` | `200`, DTO con id nuevo | **`201 + Location: /books/{id}`**, DTO |
| `PUT /books/{id}` existente | `200`, eco con id | `200`, DTO actualizado, mismo id | Igual |
| `DELETE /books/{id}` existente | `200`, eco con id | `200` | **`204`, sin cuerpo** |

Al pasar a JSON, retirad el `produces = text/plain` de la clase. No mantengáis
tests de eco como si ese siguiera siendo el contrato de entrega 2. Los tests
propios que siguen vigentes se adaptan y conservan; el historial guarda lo anterior.
En las pruebas, cread vuestro recurso antes de borrarlo y seguid el id/Location
devuelto: no supongáis que un libro 1 ya existe ni compartáis datos entre tests.

## Entrega 1 — Entender el esqueleto proporcionado

Arranque, cinco endpoints de eco y suite inicial verde. Explicad los verbos,
rutas y argumentos; registrad `Entrega 1: ...`. El eco está implementado como
punto de partida: no lo presentéis como TDD propio. Podéis añadir casos propios.

## Entrega 2 — CRUD JSON en memoria

Introducid modelo, DTOs, colección y generación de ids. Implementad alta,
consulta, modificación y borrado. El primer POST de esta etapa aún devuelve 200.
La semilla de integración crea un contexto limpio después de cada test para
evitar que la colección de una prueba contamine las demás (`@DirtiesContext`).
Eso tiene un coste de arranque asumible para estos pocos escenarios; no es una
regla que haya que aplicar a todos los tests del proyecto.

## Entrega 3 — ResponseEntity

Cambiad la creación a `201` con una `Location` utilizable y el borrado a `204`
sin cuerpo. Conservad el resto del contrato. Las pruebas de entrega 2 se
sustituyen por las de este checkpoint porque el POST ya no tiene el mismo código.

## Entrega 4 — Extraer el servicio

El controlador delega por constructor en `com.example.catalog.service.BookService`
(clase o interfaz, con una implementación `@Service` si se elige interfaz):

```java
List<BookResponseDTO> list();
BookResponseDTO get(Long id);
BookResponseDTO create(BookRequestDTO request);
BookResponseDTO update(Long id, BookRequestDTO request);
void delete(Long id);
```

La colección y las reglas dejan de estar en el controlador. El servicio no
conoce tipos HTTP. La semilla MVC lo simula con **`@MockitoBean`** y prepara el
DTO que devolverá el mock; no deja que `create` devuelva `null` por accidente.
Añadid pruebas propias unitarias del servicio para demostrar su funcionamiento
real: una verificación de delegación no prueba que se guarden los libros.

## Entrega 5 — Excepciones específicas y mapeo local

En `com.example.catalog.service`, definid:

- `BookNotFoundException(String message)` para un recurso inexistente → `404`.
- `InvalidBookException(String message)` para una entrada o regla rechazada → `400`.

Ambas son excepciones de aplicación, derivadas de `RuntimeException`. El servicio
las lanza y el controlador las traduce con `try/catch` o handlers locales.
Todavía no es obligatorio un formato uniforme de cuerpo. No uses 404 para todos
los errores ni devuelvas `null` como sustituto de una excepción específica.

## Entrega 6 — Advice global

Crear `com.example.catalog.web.GlobalExceptionHandler` con `@RestControllerAdvice`;
retirar los mapeos locales para que no tengan prioridad sobre el advice.

| Excepción o situación | HTTP | `error` |
|---|---:|---|
| `BookNotFoundException` | 404 | `NOT_FOUND` |
| `InvalidBookException` | 400 | `BAD_REQUEST` |
| `MethodArgumentNotValidException` | 400 | `VALIDATION_ERROR` |
| JSON ilegible | 400 | `BAD_REQUEST` |
| Fallo inesperado | 500 | `INTERNAL_ERROR` |

El cuerpo contiene **`error`, `message`, `timestamp`**. En errores esperables,
`message` explica el fallo. En el inesperado, el mensaje es **`Unexpected error`**,
sin detalles internos. El timestamp se genera en ejecución, no es una constante.

Activad `@Valid` en las entradas y restricciones de Bean Validation: `@NotBlank`
en isbn/title/author y `@Min(0)` en year. No se exige validar ISBN reales ni
añadir reglas de duplicados si no forman parte del slice acordado.

## Cierre de la práctica

Además de las semillas: pruebas propias del servicio y del flujo real, README
con decisiones HTTP, contribución de cada integrante, revisión de otro slice,
declaración de IA y seis checkpoints comprensibles en Git. Los tests de entrega
6 son un slice MVC con servicio mock: **no sustituyen una regresión de vuestro
CRUD real**. Conservad la cobertura propia aunque cambie la semilla activa.
