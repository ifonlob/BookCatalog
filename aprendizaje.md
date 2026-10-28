# Registro de aprendizaje del grupo

## Entrega 1

### Respuestas a las preguntas de comprensión

1. **Verbo HTTP y anotación.** `@GetMapping` atiende `GET`, `@PostMapping`
   atiende `POST`, `@PutMapping` atiende `PUT` y `@DeleteMapping` atiende
   `DELETE`. Cada método del controlador queda asociado a un verbo concreto, de
   modo que `GET /books` y `POST /books` son operaciones distintas aunque
   compartan la misma ruta.

2. **Ruta final de cada endpoint.** La ruta se compone de la anotación de clase
   más la del método. `@RequestMapping("/books")` en la clase y `@GetMapping`
   vacío en el método dan `/books`. `@GetMapping("/{id}")` añade el segmento
   variable y da `/books/{id}`. El segmento `{id}` debe coincidir con el nombre
   declarado en `@PathVariable`.

3. **`@PathVariable("id") Long id`.** Toma el valor del segmento `{id}` de la
   URL y lo inyecta como argumento. Spring convierte el texto de la URI al tipo
   declarado: `/books/42` llega como `"42"` y se convierte a `Long` mediante su
   servicio de conversión. Es `Long` porque el identificador es numérico y así
   podemos compararlo, ordenarlo o generar el siguiente sin conversiones
   manuales. Si la URL trae algo no convertible, por ejemplo `/books/abc`,
   Spring responde `400` antes de entrar al método. No hay que confundirlo con
   `@RequestParam`, que lee parámetros de query (`/books?id=42`), ni con
   `@RequestBody`, que lee el cuerpo de la petición.

4. **`produces = MediaType.TEXT_PLAIN_VALUE`.** Declara que este controlador
   solo produce `text/plain` por eso la respuesta lleva
   `Content-Type: text/plain charset=UTF-8`. Además de documentar el contrato,
   restringe la negociación de contenido, ya que si el cliente pide un tipo que no
   encaja, la petición puede rechazarse con `406 Not Acceptable`. Al devolver
   `String` con `@RestController`, el cuerpo se escribe tal cual mediante el
   conversor de cadenas, sin serialización JSON.

5. **`@RestController`.** Es una especialización de `@Controller` que añade
   `@ResponseBody` a todos los métodos. Eso significa que el valor devuelto se
   escribe en el cuerpo de la respuesta a través de los `HttpMessageConverter`,
   en lugar de interpretarse como el nombre de una vista. Por eso
   `return "GET /books"` produce literalmente el texto `GET /books`: no es una
   vista llamada así ni una redirección. Si la clase fuese solo `@Controller`
   sin `@ResponseBody`, Spring intentaría resolver `"GET /books"` como nombre de
   vista y la petición fallaría.

6. **Qué no existe todavía en el proyecto.** No hay modelo `Book` ni DTOs, no
   hay capa de servicio ni repositorio, no hay colección en memoria ni
   generación de identificadores, no hay validación con Bean Validation ni
   `@Valid`, no hay excepciones propias ni manejador de errores, y no se usa
   `ResponseEntity`, así que no hay control de códigos ni cabeceras. Tampoco hay
   persistencia, seguridad ni migraciones. Las consecuencias visibles son:
   `POST /books` no crea nada y devuelve texto, `GET /books` no lista libros,
   `DELETE /books/42` responde `200` aunque el recurso no exista y nunca se
   devuelve `404`. 

## Plantilla para las siguientes entregas

```markdown
## Entrega N

- Comportamiento que añadimos o contrato que evolucionamos:
- Test y rojo observado (distinguir compilación/configuración de comportamiento):
- Cambio mínimo y resultado verde:
- Refactorización:
- Decisiones sobre capas y contrato HTTP:
- Contribuciones, revisión y siguiente paso:
```
