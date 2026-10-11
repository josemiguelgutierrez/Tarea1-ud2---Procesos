
# Simulador de planificación · NexoData

> PSP · Tema 2 · Procesos · 2.º A DAM · **José Miguel Gutiérrez López**

## 1. Qué hace

Este programa es un simulador de planificación de CPU desarrollado en Java. Su función principal es leer un fichero de datos en formato CSV con diferentes procesos y simular cómo los distintos algoritmos de planificación de sistemas operativos como FCFS, SJF o Round Robin, gestionan el turno de la CPU, calculando los tiempos clave y generando una traza detallada de la ejecución.

## 2. Cómo compilar y ejecutar

**Desde la terminal**, en la carpeta del proyecto:

```bash
javac -encoding UTF-8 -d out src/planificador/*.java
java -cp out planificador.Main datos/ejemplo_clase.csv rr 2 --traza
```

**Desde IntelliJ IDEA:** Para ejecutarlo desde el entorno, abrimos el proyecto y nos aseguramos de tener configurada una JDK compatible. Luego, nos vamos a arriba a la derecha a la izquierda del play y seleccionamos la opción de Edit Configurations para acceder a la Run Configuration y ahí, en la clase principal indicamos planificador.Main, añadimos los argumentos necesarios como, el archivo CSV, el algoritmo que queremos que use y el quantum si usamo RR, y también por último, comprobamos que el working directory apunta correctamente a la carpeta de nuestro proyecto.

<img width="2540" height="1380" alt="image" src="https://github.com/user-attachments/assets/a34d2272-f563-4b59-b828-00df254edd36" />


## 3. Diseño

El proyecto está organizado en varias clases orientadas a objetos para separar responsabilidades de manera limpia:

- Proceso: Tiene toda la información individual de cada tarea como su identificador, el instante de llegada, la ráfaga, los tiempos de espera, el retorno y la respuesta.

- Planificador: Tiene la lógica de control de cada política de planificación como FCFS, SJF y Round Robin, y gestiona las colas de listos y los turnos de la CPU.

- Main: Es la clase principal que se encarga de leer los argumentos por consola, abrir y procesar los ficheros CSV, y lanza el algoritmo correspondiente y  después nos muestra por pantalla los resultados y las métricas finales.

## 4. Verificación (tarea 4)

### 4.1 verificacion.csv resuelto a mano
<img width="2104" height="3264" alt="Verificación resuelta a mano - Parte 1" src="https://github.com/user-attachments/assets/e4100995-3b97-40a8-b44c-8a88b7a84f8d" />

<img width="2328" height="3264" alt="Verificación resuelta a mano - Parte 2" src="https://github.com/user-attachments/assets/61d8dc96-5f85-44da-aa79-71bd5816e9bc" />

### 4.2 Comparación con el programa
He hecho los cálculos a mano en el papel y luego lo he comparado paso a paso con la salida de ejecución del programa en Java, el programa de Java no ha fallado y funciona correctamente, lo que pasa es que yo me equivoqué al principio haciéndolo a mano me equivoqué al hacer el orden exacto de los turnos de cada proceso en el diagrama de Gantt en la parte del final del Round Robin, el error fue que me equivoqué con P1 la última vez que entraba después de cola, y puse antes en la parte final del diagrama P4, antes que P1.

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

1. El que da la menor espera media es el SJF, con 7,67. Esto pasa porque este algoritmo da prioridad a los procesos más cortos, entonces eso hace que salgan rápido de la cola y no tengan que hacer toda la espera de los procesos más grandes.
2. En FCFS, un proceso pequeño como LOGS tiene que hacer una espera grande si le toca detrás de los procesos más largos que llegaron antes. Este fenómeno se conoce como el efecto convoy, y lo provocan los procesos largos que bloquean el turno por delante.
3. En el SJF el que sale perdiendo es el proceso más largo de todos, ya que al meter siempre a los más cortos delante se queda atrás del todo de la cola. Si no paran de llegar procesos cortos nuevos, le pasaría la inanición, quedándose sin ejecutar jamás. Para solucionar esto se usa el envejecimiento, que consiste en subirle la prioridad con el tiempo para que le toque turno.
4. Cuando el quantum baja de 4 a 1, la respuesta media mejora, ya que pasa de 6,17 a 1,50, pero los cambios de contexto hay muchos más, ya que pasa de 8 a 22. Un quantum de 1 minuto no es realista en un sistema real porque el procesador se pasaría el día entero haciendo cambios de contexto en lugar de trabajar los procesos.
5. Sí que sorprende un poco porque el Round Robin con quantum 4 da una espera media de 10,50, que es un poco peor que la del FCFS que está en 10,00, cuando Round Robin es más eficiente que FCFS, lo que daría a pensar que tendría una espera media más baja que FCFS. Esto pasa porque con un quantum tan grande, a veces el rendimiento baja en comparación con dejar procesar a los procesos seguidos como hace el FCFS.
6. Mi simulador nunca pone a nadie en bloqueado porque está programado solo para simular la planificación de la CPU, es decir, que no trabaja con procesos que necesitan de Entrada/Salida (E/S). 
<img width="753" height="501" alt="image" src="https://github.com/user-attachments/assets/b70211e7-8b5c-491d-97dd-9d429b0b4583" />


### Recomendación
Para los trabajos nocturnos de NexoData, el algoritmo ideal que recomendaría es el SJF. Como por las noches no hay usuarios conectados esperando en directo, lo que busca la empresa es maximizar la eficiencia y terminar todo cuanto antes, logrando la menor espera media posible.

Pero, cambiaría de opinión si fuese un servidor con usuarios conectados interactuando en tiempo real. Ahí, ya sería eficiente el SJF, entonces recomendaría Round Robin con un quantum pequeño, ya que lo importante para los usuarios es la agilidad, que el sistema responda rápido y que nadie se quede esperando mucho tiempo.

=======
# Tarea1-ud2---Procesos
