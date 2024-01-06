
package aev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClienteAutentificacion {
    public static void main(String[] args) {
        try (
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())
        ) {
            boolean autenticado = false;

            while (!autenticado) {
                // Solicitamos al usuario que ingrese su nombre de usuario y contraseña
                System.out.print("Ingrese su nombre de usuario: ");
                String nombreUsuario = scanner.nextLine();

                System.out.print("Ingrese su contraseña: ");
                String contraseña = scanner.nextLine();

                // Enviamos el nombre de usuario y la contraseña al servidor
                salida.writeObject(new Object[] {nombreUsuario, contraseña});

                // Recibimos la respuesta del servidor
                String respuestaServidor = (String) entrada.readObject();

                // Verificamos si la autenticación fue exitosa
                if ("ok".equals(respuestaServidor)) {
                    autenticado = true;
                    System.out.println("Conexión establecida. Puede comenzar a chatear.");

                    // Iniciamos un hilo para recibir mensajes del servidor
                    new Thread(new ReceptorMensajes(entrada)).start();

                    // Esperamos que el usuario ingrese mensajes y los enviamos al servidor
                    while (true) {
                        System.out.print(">> ");
                        String mensaje = scanner.nextLine();
                        salida.writeObject(mensaje);

                        // Si el usuario ingresa "exit", salimos del bucle y cerramos la aplicación
                        if ("exit".equals(mensaje)) {
                            break;
                        }
                    }
                } else {
                    // Si la autenticación falla, mostramos un mensaje de error y continuamos el bucle
                    System.out.println("Error de autenticación. Vuelve a intentarlo.");
                    System.out.println("Nombre de usuario recibido: " + nombreUsuario);
                    System.out.println("Contraseña recibida: " + contraseña);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class ReceptorMensajes implements Runnable {
        private ObjectInputStream entrada;

        public ReceptorMensajes(ObjectInputStream entrada) {
            this.entrada = entrada;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object mensaje = entrada.readObject();
                    if (mensaje instanceof List) {
                        System.out.println("Usuarios conectados: " + mensaje);
                    } else if (mensaje instanceof String) {
                        System.out.println((String) mensaje);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}