package planificador;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class AlgoritmoSJF extends Planificador {
    public AlgoritmoSJF(List<Proceso> procesos, boolean trazaActiva) {
        super(procesos, Integer.MAX_VALUE, trazaActiva);
    }

    @Override
    public Proceso seleccionarSiguiente(Queue<Proceso> colaListos, Proceso procesoActual) {
        // Lo que hace es buscar en la cola de listos el proceso con menor ráfaga.
        return colaListos.stream()
                .min(Comparator.comparingInt(Proceso::getRafaga)
                        .thenComparingInt(Proceso::getLlegada)
                        .thenComparingInt(Proceso::getOrdenFichero))
                .orElse(null);
    }

    @Override
    public boolean debeDesalojar(Proceso procesoActual, int ticksQuantumEnCPU) {
        return false;
    }
}
