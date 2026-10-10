package planificador;

import java.util.List;
import java.util.Queue;

public class AlgoritmoFCFS extends Planificador {
    public AlgoritmoFCFS(List<Proceso> procesos, boolean trazaActiva) {
        super(procesos, Integer.MAX_VALUE, trazaActiva);
    }

    @Override
    public Proceso seleccionarSiguiente(Queue<Proceso> colaListos, Proceso procesoActual) {
        return colaListos.peek();
    }

    @Override
    public boolean debeDesalojar(Proceso procesoActual, int ticksQuantumEnCPU) {
        return false;
    }
}