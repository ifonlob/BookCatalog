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
