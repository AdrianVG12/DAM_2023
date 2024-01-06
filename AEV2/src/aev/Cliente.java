package aev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())
        ) {
            System.err.println("Conexión establecida. Puede comenzar a chatear.");

            // Iniciamos un hilo para recibir mensajes del servidor
            new Thread(new ReceptorMensajes(entrada)).start();

            // Esperamos que el usuario ingrese mensajes y los enviamos al servidor
            while (true) {
                System.err.print("-");
                String mensaje = scanner.nextLine();

                // Si el usuario ingresa "exit", salimos del bucle y cerramos la aplicación
                if ("exit".equals(mensaje)) {
                	
                    salida.writeObject(mensaje);
                    System.out.println("HAS ABANDONADO LA SESION");
                    
                }

                // Verificar si el mensaje es un mensaje privado
                if (mensaje.startsWith("@")) {
                    salida.writeObject(mensaje); // Enviamos el mensaje privado al servidor
                } else {
                    // Si no es un mensaje privado, enviamos el mensaje normal al servidor
                    salida.writeObject(mensaje);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private static class ReceptorMensajes implements Runnable {
        private ObjectInputStream entrada;
        private SimpleDateFormat formatoTimestamp = new SimpleDateFormat("HH:mm:ss");
        
        public ReceptorMensajes(ObjectInputStream entrada) {
            this.entrada = entrada;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object mensaje = entrada.readObject();
                    if (mensaje instanceof List) {
                        System.err.println(formatoTimestamp.format(new Date()) + " - Usuarios conectados: " + mensaje);
                    } else if (mensaje instanceof String) {
                        System.out.println(formatoTimestamp.format(new Date()) + " - " + (String) mensaje);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}


