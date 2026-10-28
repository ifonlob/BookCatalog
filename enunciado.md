# Ejercicio 02 — Book Catalog API

## Objetivo

Construir una API REST para un catálogo de libros de forma incremental, aplicando TDD y buenas prácticas de diseño en Spring Boot. Cada entrega añade una capa de complejidad: desde un controlador que responde texto plano hasta una API completa con ResponseEntity, capa de servicios, manejo de excepciones y un `@RestControllerAdvice` global.

## Contenidos y Recursos

Esta práctica se realizará **en grupo**, después de cerrar Mini Tasks individual.
Sus seis entregas progresivas pertenecen a una sola práctica del **20 % de UD2a**;
no son seis actividades con seis pesos nuevos.

## Repositorio plantilla y trabajo del grupo

Punto de partida: [**book-catalog-template**](https://github.com/IES-Rafael-Alberti/book-catalog-template).

1. Una persona del grupo usa **Use this template → Create a new repository** y
   añade al resto como colaboradores. No hace falta GitHub Classroom ni fork.
2. Cada integrante clona el mismo repositorio y hace commits con su propia cuenta.
3. El starter incluye el eco inicial, el wrapper y las semillas de las seis
   entregas; no incluye las soluciones posteriores. Los datos se guardan **en memoria**.
4. Activad solo la semilla correspondiente al checkpoint:

   ```bash
   java classroom/ActivarEntrega.java 2
   ./mvnw test
   ```

   Cambiad `2` por `1`–`6`. En Windows, `mvnw.cmd test`. El activador conserva
   los tests propios con otros nombres y se detiene si habéis editado una semilla.

La carpeta `classroom/` organiza los recursos, pero no implica una actividad
Classroom ni un autocalificador configurado. La CI ejecuta la entrega activa y
las pruebas propias; no asigna una nota por sí sola.

## Recursos de consulta

- `recursos/book-catalog-template/`: fuente local del repositorio plantilla.
- `recursos/book-catalog-template/classroom/tests-seed/`: semillas corregidas por
  checkpoint; ya están integradas en la plantilla, no se descargan por separado.
- [Orientación hasta la sesión 5 de Battleship](orientacion-hasta-sesion-5.md):
  progresión de las seis entregas, slices verticales, contrato HTTP y checklist.

Publicar esta orientación como consulta no obliga a empezar Book Catalog antes
de cerrar Mini Tasks. Las entregas posteriores se activan progresivamente; no se
copian todos los tests semilla al proyecto inicial de una vez.

## Tareas (entregas progresivas)

Realiza commits siguiendo el formato `Entrega X: ...`:

1. **Entrega 1** — Echo text: el controlador devuelve texto plano.
2. **Entrega 2** — JSON: crea la entidad `Book` y DTOs, responde en JSON.
3. **Entrega 3** — ResponseEntity: usa `ResponseEntity` para códigos HTTP precisos (201 Created, 204 No Content, Location header).
4. **Entrega 4** — Servicio: extrae la lógica de negocio a una capa de servicios.
5. **Entrega 5** — Excepciones: define excepciones propias y lanza errores desde el servicio.
6. **Entrega 6** — Global handler: implementa `@RestControllerAdvice` para manejo centralizado de errores.

### Ampliación evaluable: contrato REST verificable

Al completar las seis entregas, el grupo debe añadir pruebas MockMvc de las
operaciones creadas y justificar en el README los verbos, códigos de estado,
cabeceras `Location`, DTOs y errores JSON. La API debe responder de forma
coherente ante alta, consulta, modificación o borrado según las operaciones que
implemente el starter. No se exige aún autenticación, migraciones ni OpenAPI:
se añaden en Gestión de eventos.

## Entregables

- Repositorio Git con los 6 commits en el formato indicado.
- Declaración de uso de IA cumplimentada (ver plantilla en `00-recursos-comunes/plantillas/`).
- README con los ejemplos HTTP y las decisiones del grupo, más las pruebas del
  contrato y las contribuciones individuales de los slices. La entrega en Moodle
  es el enlace al repositorio y al issue de entrega del grupo, según las
  [condiciones de la unidad](https://github.com/IES-Rafael-Alberti/dwes/blob/main/ud02-api-rest/ud02a-spring-boot/EVALUACION.md).

## Requisitos técnicos

- Spring Boot **4.1.1**, Java **25**, Maven Wrapper **3.9.11** incluido.
- Almacenamiento en memoria: no se exige JPA, H2, seguridad ni migraciones en esta práctica.
- TDD: los tests se proporcionan como semilla — deben pasar antes de pasar a la siguiente entrega
- Cada entrega debe compilar y pasar los tests correspondientes antes de avanzar

## Política de IA

| Aspecto | |
|---------|-|
| Uso de IA permitido | Sí, para comprender errores de compilación o explicar conceptos |
| Declaración obligatoria | Sí |
| Herramientas permitidas | ChatGPT, Claude, Gemini, GitHub Copilot |
| Qué NO está permitido | Copiar la solución completa sin entender cada entrega |

## Evaluación

Ver `rubrica.md` y `ra-ce.md` en este directorio.

## Relación con Battleship

Este ejercicio alcanza las sesiones 1 a 6 de Battleship: TDD, controladores,
servicios, DTOs, reglas y manejo de errores. Consulta
las [sesiones de Battleship](https://github.com/IES-Rafael-Alberti/dwes/blob/main/ud02-api-rest/ud02a-spring-boot/02-ejemplos/battleship/docs/README.md) y
traslada cada concepto al catálogo antes de avanzar a la entrega siguiente.
Battleship es la referencia de aula, no una entrega.
