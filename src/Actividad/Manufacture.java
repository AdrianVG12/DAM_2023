package Actividad;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * La clase Manufacture representa la aplicación encargada de fabricar tetrominos en hilos independientes.
 * Controla la cantidad de máquinas disponibles y gestiona la fabricación y registro de las piezas.
 */
public class Manufacture implements Runnable {
    private static final int NUM_MAQUINAS = 8;
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUM_MAQUINAS);
    private static final BlockingQueue<String> piezasEnEspera = new LinkedBlockingQueue<>();
    private static final List<String> piezasFabricadas = new ArrayList<>();

    private int cantidadI;
    private int cantidadO;
    private int cantidadT;
    private int cantidadJ;
    private int cantidadL;
    private int cantidadS;
    private int cantidadZ;
    private boolean archivoSalida;
    private String nombreArchivoSalida;

    
    /**
     * Constructor de la clase Manufacture.
     * Inicializa los parámetros necesarios para la fabricación de tetrominos.
     *
     * @param cantidadI           Cantidad de tetrominos de tipo I a fabricar.
     * @param cantidadO           Cantidad de tetrominos de tipo O a fabricar.
     * @param cantidadT           Cantidad de tetrominos de tipo T a fabricar.
     * @param cantidadJ           Cantidad de tetrominos de tipo J a fabricar.
     * @param cantidadL           Cantidad de tetrominos de tipo L a fabricar.
     * @param cantidadS           Cantidad de tetrominos de tipo S a fabricar.
     * @param cantidadZ           Cantidad de tetrominos de tipo Z a fabricar.
     * @param archivoSalida       Indica si se debe escribir el log en un archivo.
     * @param nombreArchivoSalida Nombre del archivo de salida para el log.
     */
    
    public Manufacture(int cantidadI, int cantidadO, int cantidadT, int cantidadJ,
                           int cantidadL, int cantidadS, int cantidadZ,
                           boolean archivoSalida, String nombreArchivoSalida) {
        this.cantidadI = cantidadI;
        this.cantidadO = cantidadO;
        this.cantidadT = cantidadT;
        this.cantidadJ = cantidadJ;
        this.cantidadL = cantidadL;
        this.cantidadS = cantidadS;
        this.cantidadZ = cantidadZ;
        this.archivoSalida = archivoSalida;
        this.nombreArchivoSalida = nombreArchivoSalida;
    }

    /**
     * Método que se ejecuta al iniciar el hilo.
     * Inicia la fabricación de tetrominos y escribe el log en un archivo si es necesario.
     */
    @Override
    public void run() {
        fabricarPiezas("I", cantidadI, 1000);
        fabricarPiezas("O", cantidadO, 2000);
        fabricarPiezas("T", cantidadT, 3000);
        fabricarPiezas("J", cantidadJ, 4000);
        fabricarPiezas("L", cantidadL, 4000);
        fabricarPiezas("S", cantidadS, 5000);
        fabricarPiezas("Z", cantidadZ, 5000);

        executor.shutdown();
        try { // Metodo para que esepere hasta que se completen las tareas
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        escribirLogEnArchivo();//Metodo para escribir las tareas(piezas creadas) en un archivo.
    }

    
    /**
     * Método para fabricar la cantidad especificada de tetrominos de un tipo particular.
     *
     * @param tipo              Tipo de tetromino a fabricar.
     * @param cantidad          Cantidad de tetrominos a fabricar.
     * @param tiempoFabricacion Tiempo de fabricación en milisegundos.
     */
    private void fabricarPiezas(String tipo, int cantidad, int tiempoFabricacion) {
        for (int i = 0; i < cantidad; i++) {
            try {
                piezasEnEspera.put(tipo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < cantidad; i++) {
            executor.submit(() -> {
                String tipoPieza;
                try {
                    tipoPieza = piezasEnEspera.take();
                    procesoFabricacion(tipoPieza, tiempoFabricacion);
                    String idPieza = tipoPieza + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    piezasFabricadas.add(idPieza);
                    System.out.println(idPieza);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Método que simula el proceso de fabricación de una pieza.
     *
     * @param tipoPieza        Tipo de tetromino a fabricar.
     * @param tiempoFabricacion Tiempo de fabricación en milisegundos.
     */
    private void procesoFabricacion(String tipoPieza, int tiempoFabricacion) {
        long tiempoInicio = System.currentTimeMillis();
        long tiempoFin = tiempoInicio + tiempoFabricacion;

        while (System.currentTimeMillis() < tiempoFin) {
            int iteraciones = 0;
            while (iteraciones < 1000000) {
                iteraciones++;
            }
        }
    }

    /**
     * Método que escribe el log(registro de las piezas) de piezas fabricadas en un archivo.
     */
    private void escribirLogEnArchivo() {
        String marcaTiempo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "LOG_" + marcaTiempo + "_" + nombreArchivoSalida + ".txt";

        try (PrintWriter escritor = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (String pieza : piezasFabricadas) {
                escritor.println(pieza);
            }
            System.out.println("Log escrito en el archivo: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
