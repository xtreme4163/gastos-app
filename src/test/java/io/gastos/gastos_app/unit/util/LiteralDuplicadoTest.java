package io.gastos.gastos_app.unit.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LiteralDuplicadoTest {

    // Cambia el patrón a tu estructura real de carpetas
    private static final Path I18N_DIR = Paths.get("src/main/resources/mensajes");

    @Test
    @DisplayName("No debe haber claves duplicadas en ningún *.properties")
    void noDuplicateKeys() throws IOException {

        List<Path> files;
        try (var walk = Files.walk(I18N_DIR)) {
            files = walk
                    .filter(p -> p.toString().endsWith(".properties"))
                    .toList();
        }

        List<String> duplicates = new ArrayList<>();

        for (Path file : files) {
            Set<String> seen = new HashSet<>();

            try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                String line;
                int n = 0;
                while ((line = br.readLine()) != null) {
                    n++;
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#") || line.startsWith("!")) {
                        continue;               // comentarios / líneas en blanco
                    }
                    String key = line.split("[:=]", 2)[0].trim();
                    if (!seen.add(key)) {
                        duplicates.add(file + " (línea " + n + "): " + key);
                    }
                }
            }
        }

        assertTrue(duplicates.isEmpty(),
                "Hay claves duplicadas en los *.properties:\n" + String.join("\n", duplicates));
    }
}
