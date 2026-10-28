import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Activa una sola semilla y preserva los tests propios del alumnado. */
public class ActivarEntrega {
    public static void main(String[] args) throws Exception {
        if (args.length != 1 || !args[0].matches("[1-6]")) {
            throw new IllegalArgumentException("Uso: java classroom/ActivarEntrega.java <1..6>");
        }
        Path root = Path.of("").toAbsolutePath();
        Path seeds = root.resolve("classroom/tests-seed");
        Path tests = root.resolve("src/test/java/com/example/catalog");
        if (!Files.isRegularFile(root.resolve("pom.xml")) || !Files.isDirectory(seeds)) {
            throw new IllegalStateException("Ejecuta desde la raíz de Book Catalog, donde está pom.xml");
        }
        int stage = Integer.parseInt(args[0]);
        Path source = seeds.resolve("entrega-%02d/BookCheckpoint%02dTest.java".formatted(stage, stage));
        if (!Files.isRegularFile(source)) throw new IllegalStateException("Falta la semilla: " + source);
        // Primero validar todo; no borrar ninguna prueba si hay una edición propia.
        for (int i = 1; i <= 6; i++) {
            String name = "BookCheckpoint%02dTest.java".formatted(i);
            Path active = tests.resolve(name);
            Path original = seeds.resolve("entrega-%02d/".formatted(i)).resolve(name);
            if (Files.exists(active) && (!Files.exists(original) || Files.mismatch(active, original) != -1)) {
                throw new IllegalStateException("Semilla modificada: " + active
                        + ". Conserva los cambios como un test propio con otro nombre antes de activar.");
            }
        }
        Files.createDirectories(tests);
        for (int i = 1; i <= 6; i++) Files.deleteIfExists(tests.resolve("BookCheckpoint%02dTest.java".formatted(i)));
        Files.copy(source, tests.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        Files.writeString(root.resolve("classroom/entrega-activa.txt"), stage + "\n");
        System.out.println("Entrega " + stage + " activada. Ejecuta ./mvnw test (Windows: mvnw.cmd test).");
    }
}
