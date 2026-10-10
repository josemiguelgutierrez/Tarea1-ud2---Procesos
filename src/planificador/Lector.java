package planificador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Lector {

    // Clase de utilidad
    private Lector() {
    }

    public static List<Proceso> leer(String ruta) {
        List<String> lineas = leerLineas(ruta);

        List<Proceso> procesos = new ArrayList<>();
        Set<String> nombresUsados = new HashSet<>();

        // Bucle con índice
        for (int i = 0; i < lineas.size(); i++) {
            int numLinea = i + 1;

            String linea = lineas.get(i).replace("\uFEFF", "").trim();

            // Líneas vacías y los comentarios se ignoran.
            if (linea.isEmpty() || linea.startsWith("#")) {
                continue;
            }

            // Número de campos. Tienen que ser exactamente 3.
            String[] campos = linea.split(";", -1);
            if (campos.length != 3) {
                throw error(numLinea, linea, "debe tener 3 campos separados por ';' (nombre;llegada;ráfaga) y tiene " + campos.length);
            }

            // Nombre del proceso. Aquí comprueba que el nombre no este vacío y no este repetido
            String nombre = campos[0].trim();
            if (nombre.isEmpty()) {
                throw error(numLinea, linea, "el nombre del proceso está vacío");
            }
            if (!nombresUsados.add(nombre)) {
                throw error(numLinea, linea, "el nombre '" + nombre + "' está repetido");
            }

            // Llegada del proceso. Aquí convierte el texto en número y si no es un número devuelve un error
            int llegada = leerEntero(campos[1], "llegada", numLinea, linea);
            if (llegada < 0) {
                throw error(numLinea, linea, "la llegada no puede ser negativa (es " + llegada + ")");
            }

            // Ráfaga del proceso.
            int rafaga = leerEntero(campos[2], "ráfaga", numLinea, linea);
            if (rafaga <= 0) {
                throw error(numLinea, linea, "la ráfaga debe ser mayor que 0 (es " + rafaga + ")");
            }

            procesos.add(new Proceso(nombre, llegada, rafaga, procesos.size()));
        }

        // Un simulador sin procesos no tiene sentido y fallaría más tarde con un error.
        if (procesos.isEmpty()) {
            throw new IllegalArgumentException(
                    "El fichero '" + ruta + "' no contiene ningún proceso (solo líneas vacías o comentarios).");
        }

        return procesos;
    }

    // Lee todas las líneas del fichero convirtiendo los fallos de E/S en mensajes legibles
    private static List<String> leerLineas(String ruta) {
        try {
            return Files.readAllLines(Path.of(ruta));
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("No encuentro el fichero '" + ruta + "'. "
                    + "Revisa la ruta y que el Working directory sea la carpeta del proyecto.");
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "No se pudo leer el fichero '" + ruta + "': " + e.getMessage());
        }
    }

    // Convierte string a entero; si no lo es, lanza un error claro en vez del de Java.
    private static int leerEntero(String texto, String campo, int numLinea, String linea) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw error(numLinea, linea,
                    "la " + campo + " '" + texto.trim() + "' no es un número entero");
        }
    }

    // Hace el mensaje de error
    private static IllegalArgumentException error(int numLinea, String linea, String motivo) {
        return new IllegalArgumentException(
                "Línea " + numLinea + " (\"" + linea + "\"): " + motivo + ".");
    }
}