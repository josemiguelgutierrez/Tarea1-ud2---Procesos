package planificador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Planificador {
    protected List<Proceso> procesosOriginales;
    protected List<Proceso> procesosSimulacion;
    protected int quantum;
    protected boolean trazaActiva;

    // Resultados de la simulación
    protected List<String> gantt = new ArrayList<>();
    protected List<String> trazaEventos = new ArrayList<>();
    protected int cambiosContexto = 0;

    public Planificador(List<Proceso> procesos, int quantum, boolean trazaActiva) {
        this.procesosOriginales = procesos;
        this.quantum = quantum;
        this.trazaActiva = trazaActiva;

        // Clonamos los procesos para no alterar los originales (permite ejecutar "todos")
        this.procesosSimulacion = new ArrayList<>();
        for (Proceso p : procesos) {
            this.procesosSimulacion.add(p.copiar());
        }
    }

    // Método abstracto que cada algoritmo implementa para seleccionar el siguiente proces.
    public abstract Proceso seleccionarSiguiente(Queue<Proceso> colaListos, Proceso procesoActual);

    // Método abstracto para saber si el algoritmo expulsa al proceso por quantum
    public abstract boolean debeDesalojar(Proceso procesoActual, int ticksQuantumEnCPU);

    public void simular() {
        int tiempo = 0;
        Queue<Proceso> colaListos = new LinkedList<>();
        List<Proceso> procesosNuevos = new ArrayList<>(procesosSimulacion);
        Proceso cpuActual = null;
        int ticksEnCPU = 0;
        String ultimoProcesoEnCPU = "";

        // Mientras queden procesos por terminar o por llegar
        while (haTerminadoTodo(procesosSimulacion)) {

            // 1) QUIÉN LLEGA EN t (Regla 2 y Regla 3)
            List<Proceso> llegadosAhora = new ArrayList<>();
            for (Proceso p : procesosNuevos) {
                if (p.getLlegada() == tiempo) {
                    llegadosAhora.add(p);
                }
            }
            // Ordena por orden de aparición en el fichero en caso de empate de llegada
            llegadosAhora.sort(Comparator.comparingInt(Proceso::getOrdenFichero));

            for (Proceso p : llegadosAhora) {
                p.setEstado(EstadoProceso.LISTO);
                colaListos.add(p);
                registrarTraza(tiempo, p, EstadoProceso.NUEVO, EstadoProceso.LISTO, "llega al sistema");
            }
            procesosNuevos.removeAll(llegadosAhora);

            // Comprueba que el proceso en la CPU ha terminado o el quantum se ha agotado
            if (cpuActual != null) {
                if (cpuActual.getTiempoRestante() == 0) {
                    cpuActual.setEstado(EstadoProceso.TERMINADO);
                    cpuActual.setFin(tiempo);
                    registrarTraza(tiempo, cpuActual, EstadoProceso.EJECUCION, EstadoProceso.TERMINADO, "termina");
                    cpuActual = null;
                    ticksEnCPU = 0;
                } else if (debeDesalojar(cpuActual, ticksEnCPU)) {
                    // Si agota el quantum, comprobamos la Regla 3 (los que llegan en t ya entraron antes en cola)
                    cpuActual.setEstado(EstadoProceso.LISTO);
                    colaListos.add(cpuActual);
                    registrarTraza(tiempo, cpuActual, EstadoProceso.EJECUCION, EstadoProceso.LISTO, "agota el quantum");
                    cpuActual = null;
                    ticksEnCPU = 0;
                }
            }

            // Si la CPU está libre, elige el siguiente proceso
            if (cpuActual == null && !colaListos.isEmpty()) {
                cpuActual = seleccionarSiguiente(colaListos, cpuActual);
                colaListos.remove(cpuActual);

                if (cpuActual.getPrimeraEjecucion() == -1) {
                    cpuActual.setPrimeraEjecucion(tiempo);
                }

                EstadoProceso origen = (cpuActual.getEstado() == EstadoProceso.NUEVO) ? EstadoProceso.NUEVO : EstadoProceso.LISTO;
                cpuActual.setEstado(EstadoProceso.EJECUCION);
                registrarTraza(tiempo, cpuActual, origen, EstadoProceso.EJECUCION, "el planificador lo elige");

                // Contar cambio de contexto (la primera carga no cuenta, CPU ociosa previa sí cuenta)
                if (!ultimoProcesoEnCPU.isEmpty() && !ultimoProcesoEnCPU.equals(cpuActual.getNombre())) {
                    cambiosContexto++;
                } else if (ultimoProcesoEnCPU.isEmpty() && gantt.size() > 0 && gantt.get(gantt.size() - 1).equals("-")) {
                    cambiosContexto++;
                }
                ultimoProcesoEnCPU = cpuActual.getNombre();
            }


            if (cpuActual != null) {
                gantt.add(cpuActual.getNombre());
                cpuActual.ejecutarUnidad();
                ticksEnCPU++;
            } else {
                gantt.add("-");
                ultimoProcesoEnCPU = "";
            }

            tiempo++;
        }
    }

    private boolean haTerminadoTodo(List<Proceso> lista) {
        for (Proceso p : lista) {
            if (p.getEstado() != EstadoProceso.TERMINADO) {
                return true;
            }
        }
        return false;
    }

    protected void registrarTraza(int t, Proceso p, EstadoProceso origen, EstadoProceso destino, String motivo) {
        if (trazaActiva) {
            trazaEventos.add("t=" + t + " " + p.getNombre() + " " + origen + " -> " + destino + " (" + motivo + ")");
        }
    }

    // Getters para imprimir resultados
    public List<Proceso> getProcesosSimulacion() { return procesosSimulacion; }
    public List<String> getGantt() { return gantt; }
    public List<String> getTrazaEventos() { return trazaEventos; }
    public int getCambiosContexto() { return cambiosContexto; }
}