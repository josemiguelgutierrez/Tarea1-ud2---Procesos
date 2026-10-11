
# Simulador de planificación · NexoData

> PSP · Tema 2 · Procesos · 2.º A DAM · **José Miguel Gutiérrez López**

## 1. Qué hace

[Tres o cuatro líneas.]

## 2. Cómo compilar y ejecutar

**Desde la terminal**, en la carpeta del proyecto:

```bash
javac -encoding UTF-8 -d out src/planificador/*.java
java -cp out planificador.Main datos/ejemplo_clase.csv rr 2 --traza
```

**Desde IntelliJ IDEA:** [explica los pasos que has seguido: JDK, configuración de ejecución, argumentos y *working directory*.]

![Mi programa en ejecución](capturas/[nombre-de-tu-captura].png)

## 3. Diseño

[Qué clases hay, qué hace cada una y por qué las has organizado así.]

## 4. Verificación (tarea 4)

### 4.1 verificacion.csv resuelto a mano
[FCFS y Round Robin q=2: diagrama de Gantt, cola de listos en cada decisión (RR), métricas y cambios de contexto. Puedes usar tablas Markdown o una foto de tu hoja.]

### 4.2 Comparación con el programa
He hecho los cálculos a mano en el papel y luego lo he comparado paso a paso con la salida de ejecución del programa en Java, el programa de Java no ha fallado y funciona correctamente, lo que pasa es que yo me equivoqué al principio haciéndolo a mano me equivoqué al hacer el orden exacto de los turnos de cada proceso en el diagrama de Gantt en la parte del final del Round Robin, el error fue con los últimos bloques de ráfaga entre P1 y P4 cuando los demás procesos ya habían terminado.

### 4.3 hueco.csv
En esos momentos, los procesos que habían llegado al principio ya han finalizado su ejecución, pero los siguientes procesos del fichero todavía no han llegado al sistema. Entonces por eso, la cola de listos se queda vacía. Esto hace que la CPU se quede inactiva, y en el simulador eso lo muestra mostrando un guion.

## 5. Análisis y recomendación a NexoData (tarea 5)

| nocturno.csv | Retorno medio | Espera media | Respuesta media | Cambios de contexto |
|---|---|---|---|---|
| FCFS | 14,00 | 10,00 | 10,00 | 5 |
| SJF | 11,67 | 7,67 | 7,67 | 5 |
| RR q=1 | 12,33 | 8,33 | 1,50 | 22 |
| RR q=2 | 12,17 | 8,17 | 2,83 | 11 |
| RR q=4 | 14,50 | 10,50 | 6,17 | 8 |

1. [Respuesta 1]
2. [Respuesta 2]
3. [Respuesta 3]
4. [Respuesta 4]
5. [Respuesta 5]
6. [Respuesta 6, con la captura comentada de los estados de tu sistema]

### Recomendación
[De 5 a 10 líneas.]
=======
# Tarea1-ud2---Procesos
>>>>>>> d550cf126880dddee207df40e8d7d0d97ecd4dd5
