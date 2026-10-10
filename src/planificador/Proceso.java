package planificador;

/**
 * Versión simplificada del PCB: lo que el planificador necesita saber de cada proceso.
 */
public class Proceso {

    // Datos del fichero que no van a cambiar nunca
    private final String nombre;
    private final int llegada;
    private final int rafaga;
    private final int ordenFichero;

    // Datos que pueden cambiar durante la simulación
    private int tiempoRestante;
    private EstadoProceso estado;

    private int primeraEjecucion = -1;
    private int fin = -1;

    public Proceso(String nombre, int llegada, int rafaga, int ordenFichero) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.ordenFichero = ordenFichero;
        this.tiempoRestante = rafaga;
        this.estado = EstadoProceso.NUEVO;
    }


    public Proceso copiar() {
        return new Proceso(nombre, llegada, rafaga, ordenFichero);
    }


    public void ejecutarUnidad() {
        if (tiempoRestante <= 0) {
            throw new IllegalStateException("El proceso " + nombre + " ya no tiene tiempo restante");
        }
        tiempoRestante--;
    }

    // Métricas

    public int getRetorno() {
        if (fin < 0) {
            throw new IllegalStateException("El proceso " + nombre + " aún no ha terminado");
        }
        return fin - llegada;
    }

    public int getEspera() {
        return getRetorno() - rafaga;
    }


    public int getRespuesta() {
        if (primeraEjecucion < 0) {
            throw new IllegalStateException("El proceso " + nombre + " aún no ha usado la CPU");
        }
        return primeraEjecucion - llegada;
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }
    public int getLlegada() {
        return llegada;
    }
    public int getRafaga() {
        return rafaga;
    }
    public int getOrdenFichero() {
        return ordenFichero;
    }
    public int getTiempoRestante() {
        return tiempoRestante;
    }
    public EstadoProceso getEstado() {
        return estado;
    }
    public int getFin() {
        return fin;
    }
    public int getPrimeraEjecucion() {
        return primeraEjecucion;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }
    public void setFin(int fin) {
        this.fin = fin;
    }
    public void setPrimeraEjecucion(int primeraEjecucion) {
        this.primeraEjecucion = primeraEjecucion;
    }

    @Override
    public String toString() {
        return nombre + "(llegada=" + llegada + ", ráfaga=" + rafaga + ")";
    }
}