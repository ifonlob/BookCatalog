# Book Catalog — plantilla de la práctica de grupo

**DWES · 2.º DAW · 2026/2027 · Spring Boot 4.1.1 · Java 25 · Maven Wrapper 3.9.11.**

Construid una API REST de catálogo de libros en seis entregas progresivas.
**Book Catalog se realiza en grupo y representa el 20 % de UD2a.** Se empieza
cuando el profesor cierre Mini Tasks individual; publicar la plantilla no
adelanta esa fecha. Battleship continúa siendo el code-along de clase.

## 1. Crear el repositorio del grupo

1. Una persona usa **Use this template → Create a new repository** en esta
   plantilla, elige un nombre para el grupo y añade al resto como colaboradores.
2. Cada integrante clona el mismo repositorio y trabaja con su propia cuenta.
3. Conservad contribuciones identificables: un slice propio por integrante y
   revisión de otro cambio. No compartáis credenciales.
4. En Moodle se entrega el mismo enlace al repositorio y al issue de entrega.
   Los seis checkpoints son una sola práctica, no seis notas independientes.

No hace falta GitHub Classroom ni crear un fork para este procedimiento.
El código inicial solo resuelve el eco de la entrega 1; **no incluye la solución
de las entregas 2–6**. Entender el código proporcionado no equivale a haberlo
desarrollado con TDD: aplicad rojo–verde–refactor a los comportamientos nuevos.

## 2. Arranque y comprobación inicial

Necesitáis **JDK 25**, configurado también en IntelliJ. Desde la raíz:

```bash
java -version
./mvnw test
./mvnw spring-boot:run
```

En Windows: `mvnw.cmd test` y `mvnw.cmd spring-boot:run`. No hace falta Maven
global. La primera ejecución necesita conexión para descargar Maven/dependencias.
El puerto es 8080; detened Battleship si lo está utilizando.

```bash
curl http://localhost:8080/books
# GET /books
```

No se incorpora JPA, H2 ni seguridad: esta práctica usa **almacenamiento en
memoria** y se reinicia al parar la aplicación. Persistencia, autenticación,
migraciones y OpenAPI se trabajan en sus tramos correspondientes del módulo.

## 3. Activar los tests de cada entrega

La entrega 1 está activa y verde. Antes de empezar otra etapa:

```bash
java classroom/ActivarEntrega.java 2
./mvnw test
```

Cambiad `2` por `1`–`6`. El programa se ejecuta con Java 25 en Linux y Windows.
Sustituye **solo** los archivos semilla `BookCheckpoint01Test.java`…`06` de esta
práctica; conserva los tests propios que tengan otros nombres. Si habéis editado
una semilla, primero guardad el cambio como un test propio para no perderlo: el
activador comprueba los contenidos y se detiene si detecta modificaciones.
Al convertir una semilla en un test propio, cambiad **el nombre del archivo y
el de la clase** para que no duplique la clase que activa el checkpoint.

Las semillas futuras viven en `classroom/tests-seed/`, fuera de `src/test`:
**no las copiéis todas juntas**. El eco de texto de entrega 1 y el JSON de entrega
2 son contratos sucesivos, no expectativas simultáneas. Lo mismo ocurre con
POST `200` en entrega 2 frente a `201` desde entrega 3.

Tras activar una entrega nueva, los tests deben revelar lo que falta. Si aún no
existen los DTOs o la firma del servicio exigidos, cread primero su estructura
mínima para compilar; después observad el rojo de comportamiento e implementad.
Conservad en Git la historia de las etapas y mantened vuestros tests propios
adaptados al contrato vigente. No hay perfiles Maven ocultos.

## 4. Contrato y progresión

El contrato completo, tipos y criterios de cada checkpoint están en
`classroom/entregas.md`. Resumen:

| Entrega | Objetivo | Resultado observable |
|---|---|---|
| 1 | Leer, ejecutar y explicar el eco proporcionado | Cinco rutas devuelven texto |
| 2 | Modelo, DTOs y CRUD en memoria | Entrada/salida JSON; éxito `200` |
| 3 | Semántica HTTP con `ResponseEntity` | Crear `201 + Location`, borrar `204` |
| 4 | Servicio y delegación | Controlador delgado; tests con `@MockitoBean` |
| 5 | Excepciones propias y mapeo local | Recurso inexistente `404`, rechazo `400` |
| 6 | Advice global y error uniforme | JSON `error`, `message`, `timestamp` |

Los tests son una semilla, no toda la evaluación: añadid pruebas relevantes,
documentad el contrato de vuestro slice y explicad las decisiones. Al cerrar cada
etapa, suite verde y commit `Entrega X: ...`. La CI ejecuta la suite que hayáis
activado y los tests propios, no solamente el eco inicial.

## 5. Evidencias dentro del repositorio

El repositorio distribuido incluye `enunciado.md`, `orientacion-hasta-sesion-5.md`,
`rubrica.md` y `ra-ce.md`, además de plantillas `equipo.md`, `aprendizaje.md` y
`declaracion-uso-ia.md`. Completadlas y documentad los endpoints con ejemplos HTTP.
La rúbrica y los RA/CE de esta edición son los vigentes: no hay ponderaciones
alternativas por cada etapa ni se piden capturas como evidencia principal.

La **sesión 5 de Battleship** enseña el advice que se aplica a la **entrega 6 de
Book Catalog**. No confundir los dos números. Al acabar, cada integrante debe
poder ejecutar y defender su slice, sus pruebas y su contribución en Git.

## 6. Contrato implementado — Entrega 3 (`ResponseEntity`)

Estado del contrato tras la entrega 3. Los datos viven en un
`Map<Long, Book>` dentro del controlador y se pierden al parar la aplicación.
La entrada y la salida usan DTOs (`BookRequestDTO` y `BookResponseDTO`); el
modelo interno `Book` no se expone. Todos los métodos devuelven `ResponseEntity`
para que el código de estado y las cabeceras sean explícitos y no dependan de lo
que Spring elija por defecto.

| Verbo | Ruta | Estado | Cuerpo de la respuesta |
|---|---|---|---|
| `GET` | `/books` | `200` | array JSON; `[]` si el catálogo está vacío |
| `GET` | `/books/{id}` | `200` | libro o cuerpo vacío si no existe |
| `POST` | `/books` | `201` | libro creado + `Location: /books/{id}` |
| `PUT` | `/books/{id}` | `200` | libro actualizado, conserva el `id` de la ruta |
| `DELETE` | `/books/{id}` | `204` | sin cuerpo |

Entrada JSON:

```json
{"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
```

Salida JSON:

```json
{"id":1,"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
```

Ejemplos con `curl`:

```bash
./mvnw spring-boot:run

# listado (vacío en un arranque limpio)
curl -i http://localhost:8080/books

# alta: el id lo genera la aplicación y la respuesta trae la Location
curl -i -X POST http://localhost:8080/books \
  -H 'Content-Type: application/json' \
  -d '{"isbn":"978-1","title":"DDD","author":"Evans","year":2003}'
# HTTP/1.1 201
# Location: /books/1
# {"id":1,"isbn":"978-1","title":"DDD","author":"Evans","year":2003}

# consulta reutilizando la Location devuelta
curl -i http://localhost:8080/books/1

# modificación completa (reemplaza título y año, mismo id)
curl -i -X PUT http://localhost:8080/books/1 \
  -H 'Content-Type: application/json' \
  -d '{"isbn":"978-1","title":"DDD 2.ª ed.","author":"Evans","year":2004}'

# borrado: 204 y cuerpo vacío
curl -i -X DELETE http://localhost:8080/books/1
```

Decisiones de esta etapa:

- La `Location` es **relativa** y se construye con
  `URI.create("/books/" + dto.id())`, no con `ServletUriComponentsBuilder`. El
  *builder* produciría una URL absoluta (`http://localhost:8080/books/1`)
  atada al host y al puerto de la petición: no es portable entre entornos y no
  coincide con la ruta que se compara en las pruebas. Una `Location` relativa
  la resuelve el cliente contra el host de la petición, así que sigue siendo
  utilizable: el test hace `GET` directamente sobre el valor devuelto.
- El `id` de la cabecera sale del DTO que se devuelve en el cuerpo, de modo que
  ambos no pueden divergir.
- `DELETE` responde **`204`** sin cuerpo (`ResponseEntity.noContent().build()`),
  también cuando el id no existía: sin excepciones propias no hay forma de
  distinguir «borrado» de «no encontrado», y el `404` llega en la entrega 5.
- `GET` y `PUT` conservan `200` con representación; en esta etapa solo cambia la
  semántica del alta y del borrado.
- La colección y la generación de ids seguían en el controlador en esta etapa;
  la entrega 4 las traslada al servicio (ver sección 7). `PUT` hace un reemplazo
  completo: no conserva campos que no vengan en el cuerpo.

Pruebas de la suite (`./mvnw test`: 22 en verde):

| Test | Qué verifica |
|---|---|
| `BookCheckpoint05Test` (semilla) | con `@MockitoBean`, que el controlador traduce `BookNotFoundException` a `404`, `InvalidBookException` a `400` y sigue delegando en el alta |
| `BookServiceTest` (propia) | el servicio real: ids incrementales, orden de inserción, `update` con el id de la ruta, borrado selectivo, entrada inválida y recurso inexistente |
| `ErroresPropiosTest` (propia) | por HTTP: `400` en alta y modificación inválidas, y `404` en consulta, modificación y borrado de un id inexistente |
| `EscrituraPropiaTest` (propia) | regresión del CRUD real: crear → consultar → actualizar → borrar, con `201` y `204` |
| `BookListadoJsonTest` (propia) | `GET /books` responde `200` con `Content-Type: application/json` y `[]` |
| `CatalogApplicationTests` | el contexto de la aplicación arranca |

## 7. Arquitectura — entrega 4 (servicio)

El contrato HTTP **no cambia**: `201` + `Location` en el alta, `204` sin cuerpo en
el borrado y `200` con representación en consulta y modificación. Lo que cambia
es dónde vive el estado y quién aplica las reglas.

```text
Petición HTTP → BookController → BookService → Map<Long, Book> en memoria
                    │                 │
              ResponseEntity      DTOs y reglas
```

- El controlador recibe `BookService` **por constructor** y queda reducido a
  traducir HTTP: rutas, `@RequestBody`, códigos de estado y la cabecera
  `Location`. Ya no guarda la colección ni genera ids.
- `BookService` es una clase concreta anotada con `@Service`. Guarda un
  `LinkedHashMap<Long, Book>` (mantiene el orden de inserción del listado) y un
  `AtomicLong` para los ids. Devuelve `BookRequestDTO`/`BookResponseDTO` y no
  conoce tipos HTTP: ni `ResponseEntity` ni `Location` dentro del servicio.
- La semilla de este checkpoint es `@WebMvcTest(BookController.class)` con
  `@MockitoBean BookService`: prueba **solo la delegación** y que el controlador
  usa el `id` devuelto por el servicio para construir la `Location`. No
  demuestra que los libros se guarden.
- Esa parte la cubren los tests propios: `BookServiceTest`, unitario y sin
  Spring, y `EscrituraPropiaTest`, que sigue siendo la regresión del CRUD real
  contra la aplicación completa.

## 8. Errores — entrega 5 (excepciones propias)

El servicio deja de responder «vacío» cuando algo no existe y pasa a lanzar
excepciones de aplicación. El controlador las traduce a HTTP **de forma local**,
todavía sin un formato de cuerpo común.

| Situación | Excepción | Estado |
|---|---|---:|
| `GET /books/{id}` con id inexistente | `BookNotFoundException` | `404` |
| `PUT /books/{id}` con id inexistente | `BookNotFoundException` | `404` |
| `DELETE /books/{id}` con id inexistente | `BookNotFoundException` | `404` |
| Alta o modificación con datos inválidos | `InvalidBookException` | `400` |

```java
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) { super(message); }
}
```

```java
public class InvalidBookException extends RuntimeException {
    public InvalidBookException(String message) { super(message); }
}
```

Reglas de validación del servicio: `isbn`, `title` y `author` no pueden ser
`null` ni estar en blanco, y `year` no puede ser negativo. La comprobación de
existencia va **antes** que la de validez, de modo que un id inexistente siempre
da `404` y no `400`.

Mapeo local en el controlador:

```java
@ExceptionHandler(BookNotFoundException.class)
public ResponseEntity<Void> notFound(BookNotFoundException exception) {
    return ResponseEntity.notFound().build();
}

@ExceptionHandler(InvalidBookException.class)
public ResponseEntity<Void> badRequest(InvalidBookException exception) {
    return ResponseEntity.badRequest().build();
}
```

Ejemplos con `curl`:

```bash
curl -i http://localhost:8080/books/999       # 404
curl -i -X DELETE http://localhost:8080/books/999   # 404

curl -i -X POST http://localhost:8080/books \
  -H 'Content-Type: application/json' \
  -d '{"isbn":"","title":"DDD","author":"Evans","year":2003}'   # 400
```

Decisiones de esta etapa:

- **No** se usa `@ResponseStatus` en las excepciones: fijaría el código en la
  capa de servicio y el contrato pide que el mapeo sea del controlador. En la
  entrega 6 ese mapeo se mueve a un `@RestControllerAdvice`.
- Las respuestas de error todavía **no llevan cuerpo uniforme**; solo el código.
  El JSON con `error`, `message` y `timestamp` es de la entrega 6.
- `get` deja de devolver `null`: el «`200` con cuerpo vacío» desaparece y se
  convierte en `404`. Es el cambio de comportamiento que arrastra la etapa.
- `PUT` y `DELETE` también distinguen el recurso inexistente (`404`) en lugar de
  responder `200`/`204` sin efecto.
- El `400` se reserva para entradas rechazadas; no se usa `404` como cajón de
  sastre ni `null` en lugar de una excepción específica.

## 9. Errores uniformes — entrega 6 (advice global)

Los mapeos locales desaparecen del controlador y pasan a
`com.example.catalog.web.GlobalExceptionHandler`, anotado con
`@RestControllerAdvice`. Todas las respuestas de error comparten el mismo cuerpo:

```json
{
  "error": "NOT_FOUND",
  "message": "No existe el libro 999",
  "timestamp": "2026-10-29T16:02:34.665120298Z"
}
```

| Situación | HTTP | `error` |
|---|---:|---|
| `BookNotFoundException` | `404` | `NOT_FOUND` |
| `InvalidBookException` | `400` | `BAD_REQUEST` |
| `MethodArgumentNotValidException` | `400` | `VALIDATION_ERROR` |
| JSON ilegible (`HttpMessageNotReadableException`) | `400` | `BAD_REQUEST` |
| Fallo inesperado | `500` | `INTERNAL_ERROR` |

Entrada validada con Bean Validation en `BookRequestDTO`: `@NotBlank` en
`isbn`, `title` y `author` y `@Min(0)` en `year`, activadas con `@Valid` en el
alta y en la modificación. Si la validación falla, el controlador **no llega a
llamar al servicio**.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(String error, String message, Instant timestamp) {}

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status)
                .body(new ApiError(code, message, Instant.now()));
    }
}
```

Decisiones de esta etapa:

- **`timestamp` generado en ejecución** con `Instant.now()` en cada respuesta; no
  es una constante ni se calcula al arrancar.
- El `500` devuelve siempre el mensaje literal `Unexpected error`: no se
  propaga el texto de la excepción ni la traza, para no filtrar detalles
  internos. Un fallo inesperado se registra en el servidor, no en la respuesta.
- Se retiran los `@ExceptionHandler` locales del controlador; si se quedan,
  tienen prioridad sobre el advice y las respuestas salen sin cuerpo.
- `BAD_REQUEST` cubre dos casos distintos: el JSON ilegible (detectado antes de
  entrar al controlador) y el rechazo del servicio (`InvalidBookException`),
  que tras `@Valid` queda para reglas que no expresa Bean Validation.
- El contrato de éxito no cambia: `201` + `Location` relativa, `204` sin cuerpo
  y `200` con representación.

Ejemplos con `curl`:

```bash
curl -i http://localhost:8080/books/999       # 404 NOT_FOUND
curl -i -X POST http://localhost:8080/books \
  -H 'Content-Type: application/json' \
  -d '{"isbn":"978-1","title":" ","author":"Evans","year":2003}'   # 400 VALIDATION_ERROR
curl -i -X POST http://localhost:8080/books \
  -H 'Content-Type: application/json' -d '{'                        # 400 BAD_REQUEST
```

Pruebas de la suite (`./mvnw test`: 26 en verde):

| Test | Qué verifica |
|---|---|
| `BookCheckpoint06Test` (semilla) | el advice con `@MockitoBean`: `NOT_FOUND`, `BAD_REQUEST`, `VALIDATION_ERROR` sin llamar al servicio, `INTERNAL_ERROR` sin detalles y el `201` de éxito |
| `AdvicePropioTest` (propia) | cuerpo completo del `404`: `error`, `message` y `timestamp` |
| `ErroresPropiosTest` (propia) | por HTTP real: `400` de validación, `400` de JSON ilegible y `404` en consulta, modificación y borrado |
| `BookServiceTest` (propia) | el servicio real, incluidas las excepciones de entrada inválida y recurso inexistente |
| `EscrituraPropiaTest` (propia) | regresión del CRUD real: crear → consultar → actualizar → borrar |
| `BookListadoJsonTest` (propia) | `GET /books` responde `200` con `Content-Type: application/json` |
| `CatalogApplicationTests` | el contexto arranca |
