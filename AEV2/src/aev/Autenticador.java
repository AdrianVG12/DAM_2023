package aev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class Autenticador {
    private String archivoCredenciales;  // Ruta del archivo que almacena las credenciales
    private Set<String> usuariosRegistrados;  // Conjunto para almacenar los nombres de usuario registrados

    // Constructor que inicializa la instancia del Autenticador
    public Autenticador(String archivoCredenciales) {
        this.archivoCredenciales = archivoCredenciales;
        this.usuariosRegistrados = cargarUsuariosRegistrados();  // Cargar usuarios registrados al iniciar
    }

    // Método privado para cargar los nombres de usuario registrados desde el archivo
    private Set<String> cargarUsuariosRegistrados() {
        Set<String> usuarios = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCredenciales))) {
            String linea;
            // Leer cada línea del archivo y agregar el nombre de usuario al conjunto
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 2) {
                    String usuario = partes[0];
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método para autenticar a un usuario
    public boolean autenticar(String nombreUsuario, String contraseña) {
        // Verificar si el nombre de usuario contiene espacios o ya está registrado
        if (nombreUsuario.contains(" ") || usuariosRegistrados.contains(nombreUsuario)) {
            return false;  // Nombre de usuario no válido o ya registrado
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCredenciales))) {
            String linea;
            // Leer cada línea del archivo y verificar si las credenciales son válidas
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 2) {
                    String usuario = partes[0];
                    String clave = partes[1];
                    if (usuario.trim().equalsIgnoreCase(nombreUsuario.trim()) && clave.trim().equals(contraseña.trim())) {
                        return true;  // Credenciales válidas
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // Credenciales no válidas
    }

    // Método para registrar un nuevo usuario
    public void registrarUsuario(String nombreUsuario, String contraseña) {
        // Verificar si el nombre de usuario no contiene espacios y no está registrado
        if (!nombreUsuario.contains(" ") && !usuariosRegistrados.contains(nombreUsuario)) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(archivoCredenciales, true))) {
                writer.println(nombreUsuario + ":" + contraseña);  // Agregar nuevas credenciales al archivo
                usuariosRegistrados.add(nombreUsuario);  // Agregar el nombre de usuario al conjunto
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
