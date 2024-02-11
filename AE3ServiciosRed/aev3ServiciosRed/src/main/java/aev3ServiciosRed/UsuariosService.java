package aev3ServiciosRed;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar usuarios autorizados.
 */
@Service
public class UsuariosService {

    private static final String ARCHIVO_AUTORIZADOS = "autorizados.txt";

    /**
     * Método para obtener la lista de usuarios autorizados desde el archivo.
     * @return Lista de usuarios autorizados.
     */
    public List<String> obtenerUsuariosAutorizados() {
        try {
            return Files.readAllLines(Paths.get(ARCHIVO_AUTORIZADOS));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método para verificar si un usuario está autorizado.
     * @param usuario El nombre de usuario a verificar.
     * @return true si el usuario está autorizado, false de lo contrario.
     */
    public boolean usuarioAutorizado(String usuario) {
        List<String> usuarios = obtenerUsuariosAutorizados();
        return usuarios != null && usuarios.contains(usuario);
    }

    /**
     * Método para registrar un nuevo usuario autorizado en el archivo.
     * @param usuario El nombre de usuario a registrar.
     */
    public void registrarUsuario(String usuario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_AUTORIZADOS, true))) {
            writer.write(usuario);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

