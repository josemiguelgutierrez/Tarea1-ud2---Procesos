package planificador;

import java.util.List;
import java.util.Queue;

public class AlgoritmoRR extends Planificador {
    public AlgoritmoRR(List<Proceso> procesos, int quantum, boolean trazaActiva) {
        super(procesos, quantum, trazaActiva);
    }

    @Override
    public Proceso seleccionarSiguiente(Queue<Proceso> colaListos, Proceso procesoActual) {
        return colaListos.poll();
    }

    @Override
    public boolean debeDesalojar(Proceso procesoActual, int ticksQuantumEnCPU) {
        // Desaloja si se agota el quantum asignado y todavía le queda tiempo restante al proceso
        return ticksQuantumEnCPU >= quantum && procesoActual.getTiempoRestante() > 0;
    }
}