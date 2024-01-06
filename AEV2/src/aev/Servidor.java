package aev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static List<HiloCliente> listaHilosClientes = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("Servidor conectado. Esperando conexiones de clientes...");
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                HiloCliente hiloCliente = new HiloCliente(clienteSocket);
                listaHilosClientes.add(hiloCliente);
                new Thread(hiloCliente).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class HiloCliente implements Runnable {
        private Socket clienteSocket;
        private ObjectOutputStream salida;
        private ObjectInputStream entrada;
        private String nombreUsuario;

        public HiloCliente(Socket clienteSocket) {
            this.clienteSocket = clienteSocket;
        }

        @Override
        public void run() {
            try {
                salida = new ObjectOutputStream(clienteSocket.getOutputStream());
                entrada = new ObjectInputStream(clienteSocket.getInputStream());

                // Solicitar al cliente que ingrese un nombre de usuario
                salida.writeObject("Ingrese su nombre de usuario:");
                nombreUsuario = (String) entrada.readObject();
                System.err.println("Cliente autenticado: " + nombreUsuario);

                // Enviar lista de usuarios conectados a este cliente
                enviarListaUsuarios();

                // Manejar mensajes del cliente
                while (true) {
                    Object mensajeRecibido = entrada.readObject();

                    if (mensajeRecibido instanceof String) {
                        String mensaje = (String) mensajeRecibido;
                       
                        System.out.println("[" + obtenerTimestamp() + "] " + nombreUsuario + ": " + mensaje);
                        
                        if ("?".equals(mensaje)) {
                            enviarListaUsuarios();
                        } else if (mensaje.startsWith("@")) {
                            // Separar el destinatario y el contenido del mensaje privado
                            String[] partes = mensaje.split(" ", 2);
                            if (partes.length == 2) {
                                String destinatario = partes[0].substring(1);
                                String contenido = partes[1];
                                enviarMensajePrivado(contenido, nombreUsuario, destinatario);
                            }
                            
                        } else if ("exit".equals(mensaje)) {                           
                            System.out.println(nombreUsuario + " ha salido de la sesion.");
                            desconectarCliente();
                            break;
                        } else {
                            enviarMensajeATodos(nombreUsuario, mensaje);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private String obtenerTimestamp() {
            // Obtiene la hora actual en formato HH:mm:ss
            return java.time.LocalTime.now().withNano(0).toString();
        }

		private void enviarMensajePrivado(String mensaje, String remitente, String destinatario) {
            for (HiloCliente hiloCliente : listaHilosClientes) {
                if (hiloCliente.getNombreUsuario().equals(destinatario)) {
                    hiloCliente.enviarMensaje("[Privado de " + remitente + "]: " + mensaje);
                    break;
                }
            }
        }

		private void enviarListaUsuarios() {
            List<String> usuariosConectados = new ArrayList<>();
            for (HiloCliente hiloCliente : listaHilosClientes) {
                usuariosConectados.add(hiloCliente.getNombreUsuario());
            }

            try {
                salida.writeObject(usuariosConectados);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void enviarMensajeATodos(String remitente, String mensaje) {
            for (HiloCliente hiloCliente : listaHilosClientes) {
                hiloCliente.enviarMensaje(">> " + remitente + ": " + mensaje);
            }
        }

        private void desconectarCliente() {
            listaHilosClientes.remove(this);
            System.err.println("Cliente desconectado: " + nombreUsuario);
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        private void enviarMensaje(String mensaje) {
            try {
                salida.writeObject(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

