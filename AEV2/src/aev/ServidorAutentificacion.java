package aev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorAutentificacion {
    private static final String ARCHIVO_CREDENCIALES = "credenciales.txt";
    private static Autenticador autenticador;
    private static List<HiloCliente> listaHilosClientes = new ArrayList<>();

    public static void main(String[] args) {
        autenticador = new Autenticador(ARCHIVO_CREDENCIALES);

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

                while (true) {
                	Object[] credenciales = (Object[]) entrada.readObject();
                	String nombreUsuario = (String) credenciales[0];
                	String contraseña = (String) credenciales[1];
                	
                    if (autenticador.autenticar(nombreUsuario, contraseña)) {
                        salida.writeObject("ok");
                        this.nombreUsuario = nombreUsuario;  // Actualizamos el nombre de usuario
                        System.out.println("Cliente autenticado: " + nombreUsuario);

                        // Enviar lista de usuarios conectados a este cliente
                        enviarListaUsuarios();

                        // Manejar mensajes del cliente
                        while (true) {
                            Object mensajeRecibido = entrada.readObject();

                            if (mensajeRecibido instanceof String) {
                                String mensaje = (String) mensajeRecibido;

                                if ("?".equals(mensaje)) {
                                    enviarListaUsuarios();
                                } else if (mensaje.startsWith("@")) {
                                    enviarMensajePrivado(mensaje, nombreUsuario);
                                } else if ("exit".equals(mensaje)) {
                                    desconectarCliente();
                                    break;
                                } else {
                                    enviarMensajeATodos(nombreUsuario, mensaje);
                                }
                            }
                        }
                    } else {
                        salida.writeObject("error");                      
                        System.out.println("Error de autenticación para: " + nombreUsuario);
                        System.out.println("Nombre de usuario recibido: " + nombreUsuario);
                        System.out.println("Contraseña recibida: " + contraseña);
                        if (nombreUsuario.contains(" ")) {
                            System.out.println("El nombre de usuario no puede contener espacios.");
                        } else {
                            System.out.println("Credenciales incorrectas.");
                            
                        }
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
            	e.printStackTrace();
                System.out.println("Cliente desconectado: " + nombreUsuario);
            } finally {
                desconectarCliente();
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

        private void enviarMensajePrivado(String mensaje, String remitente) {
            // Identificar destinatario y enviar mensaje privado
            String[] partes = mensaje.split(" ", 2);
            if (partes.length == 2) {
                String destinatario = partes[0].substring(1);
                String contenido = partes[1];
                
                // Verificar que el destinatario sea diferente del remitente
                if (!destinatario.equals(remitente)) {
                    for (HiloCliente hiloCliente : listaHilosClientes) {
                        if (hiloCliente.getNombreUsuario().equals(destinatario)) {
                            hiloCliente.enviarMensaje("[Privado de " + remitente + "]: " + contenido);
                            break;
                        }
                    }
                }
            }
        }

        private void enviarMensajeATodos(String remitente, String mensaje) {
            for (HiloCliente hiloCliente : listaHilosClientes) {
			    hiloCliente.enviarMensaje("[" + java.time.LocalTime.now() + "] " + remitente + ": " + mensaje);
			}
        }

        private void desconectarCliente() {
            listaHilosClientes.remove(this);
            System.out.println("Cliente desconectado: " + nombreUsuario);
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
