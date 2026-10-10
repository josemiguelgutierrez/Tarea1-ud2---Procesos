package planificador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

/**
 * PSP · Tema 2 · Simulador de planificación para NexoData
 *
 * Punto de entrada. Este fichero YA ESTÁ HECHO: solo lee y comprueba los argumentos.
 * No cambies cómo se leen: el profesor ejecutará tu programa siempre así:
 *
 *   java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]
 *
 * Todo lo demás (modelo del proceso, lectura del CSV, algoritmos, métricas,
 * informe por consola...) lo diseñas y programas tú en este mismo paquete.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]");
            System.exit(1);
        }
        Path fichero = Path.of(args[0]);
        String algoritmo = args[1].toLowerCase();
        boolean traza = List.of(args).contains("--traza");
        int quantum = 2;
        if (args.length >= 3 && !args[2].startsWith("--")) {
            try {
                quantum = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("El quantum debe ser un número entero: " + args[2]);
                System.exit(1);
            }
        }
        if (!Files.exists(fichero)) {
            System.err.println("No encuentro el fichero " + fichero.toAbsolutePath()
                    + "\nComprueba el «Working directory» de la configuración de ejecución.");
            System.exit(1);
        }
        if (!List.of("fcfs", "sjf", "rr", "todos").contains(algoritmo)) {
            System.err.println("Algoritmo desconocido: " + algoritmo + " (usa fcfs, sjf, rr o todos)");
            System.exit(1);
        }

        System.out.println("Fichero: " + fichero + " | algoritmo: " + algoritmo
                + " | quantum: " + quantum + " | traza: " + traza);

        List<Proceso> procesos = Lector.leer(fichero.toString());

        // Preparar las simulaciones según el algoritmo elegido
        List<Planificador> simulacionesAComprobar = new ArrayList<>();

        switch (algoritmo) {
            case "fcfs":
                simulacionesAComprobar.add(new AlgoritmoFCFS(procesos, traza));
                break;
            case "sjf":
                simulacionesAComprobar.add(new AlgoritmoSJF(procesos, traza));
                break;
            case "rr":
                simulacionesAComprobar.add(new AlgoritmoRoundRobin(procesos, quantum, traza));
                break;
            case "todos":
                simulacionesAComprobar.add(new AlgoritmoFCFS(procesos, traza));
                simulacionesAComprobar.add(new AlgoritmoSJF(procesos, traza));
                simulacionesAComprobar.add(new AlgoritmoRoundRobin(procesos, quantum, traza));
                break;
        }

        //Se ejecuta la simulación y se imprimen los resultados por la consola
        for (Planificador planificador : simulacionesAComprobar) {
            planificador.simular();

            String nombreAlgoritmo = "";
            if (planificador instanceof AlgoritmoFCFS) nombreAlgoritmo = "FCFS";
            else if (planificador instanceof AlgoritmoSJF) nombreAlgoritmo = "SJF sin desalojo";
            else if (planificador instanceof AlgoritmoRoundRobin) nombreAlgoritmo = "Round Robin (q=" + quantum + ")";

            System.out.println("\n=== " + nombreAlgoritmo + " ===");

            // Se imprime el diagrama de Gantt
            System.out.print("t ");
            for (int i = 0; i < planificador.getGantt().size(); i++) {
                System.out.printf("%-2d ", i);
            }
            System.out.println();

            System.out.print("CPU ");
            for (String cpu : planificador.getGantt()) {
                System.out.printf("%-2s ", cpu);
            }
            System.out.println("\n");

            // Se imprime la tabla de Métricas
            System.out.printf("%-10s %-8s %-8s %-6s %-8s %-8s %-10s%n",
                    "Proceso", "Llegada", "Ráfaga", "Fin", "Retorno", "Espera", "Respuesta");

            double sumaRetorno = 0;
            double sumaEspera = 0;
            double sumaRespuesta = 0;
            List<Proceso> sims = planificador.getProcesosSimulacion();

            for (Proceso p : sims) {
                sumaRetorno += p.getRetorno();
                sumaEspera += p.getEspera();
                sumaRespuesta += p.getRespuesta();
                System.out.printf("%-10s %-8d %-8d %-6d %-8d %-8d %-10d%n", p.getNombre(), p.getLlegada(), p.getRafaga(), p.getFin(), p.getRetorno(), p.getEspera(), p.getRespuesta());
            }

            int totalP = sims.size();
            System.out.printf("Medias: retorno %.2f | espera %.2f | respuesta %.2f%n",
                    sumaRetorno / totalP, sumaEspera / totalP, sumaRespuesta / totalP);

            // Cambios de Contexto
            System.out.println("Cambios de contexto: " + planificador.getCambiosContexto());

            if (traza) {
                System.out.println("Traza de estados:");
                for (String evento : planificador.getTrazaEventos()) {
                    System.out.println(evento);
                }
            }
        }

    }
}



