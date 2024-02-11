package aev3ServiciosRed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar las películas.
 */
@Service
public class PeliculasService {

    private static final String DIRECTORIO_PELIS = "pelis";

    /**
     * Método para obtener todas las películas.
     * @return Lista de todas las películas.
     */
    public List<Pelicula> obtenerTodas() {
        List<Pelicula> peliculas = new ArrayList<>();
        File directorio = new File(DIRECTORIO_PELIS);
        File[] ficheros = directorio.listFiles();
        if (ficheros != null) {
            for (File fichero : ficheros) {
                if (fichero.isFile()) {
                    Pelicula pelicula = leerPeliculaDesdeArchivo(fichero);
                    peliculas.add(pelicula);
                }
            }
        }
        return peliculas;
    }

    /**
     * Método para obtener una película por su ID.
     * @param id El ID de la película.
     * @return La película correspondiente al ID o null si no se encuentra.
     */
    public Pelicula obtenerPorId(String id) {
        File fichero = new File(DIRECTORIO_PELIS, id + ".txt");
        if (fichero.exists()) {
            return leerPeliculaDesdeArchivo(fichero);
        } else {
            return null;
        }
    }

    /**
     * Método para agregar una reseña a una película.
     * @param idPelicula El ID de la película.
     * @param usuario El usuario que realiza la reseña.
     * @param resenya La reseña a agregar.
     */
    public void agregarResenya(String idPelicula, String usuario, String resenya) {
        File fichero = new File(DIRECTORIO_PELIS, idPelicula + ".txt");
        if (fichero.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero, true))) {
                writer.newLine(); // Agregar una nueva línea antes de la nueva reseña
                writer.write(usuario + ": " + resenya);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para agregar una nueva película.
     * @param titulo El título de la nueva película.
     */
    public void agregarNuevaPelicula(String titulo) {
        int ultimoId = obtenerUltimoId();
        String nuevoId = String.valueOf(ultimoId + 1);
        File fichero = new File(DIRECTORIO_PELIS, nuevoId + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero))) {
            writer.write("Título: " + titulo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método privado para leer una película desde un archivo.
     * @param fichero El archivo que contiene la película.
     * @return La película leída desde el archivo.
     */
    private Pelicula leerPeliculaDesdeArchivo(File fichero) {
        Pelicula pelicula = new Pelicula();
        pelicula.setId(fichero.getName().replace(".txt", ""));
        try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
            String titulo = reader.readLine().replace("Título: ", "");
            pelicula.setTitulo(titulo);
            String linea;
            List<String> resenyas = new ArrayList<>();
            while ((linea = reader.readLine()) != null) {
                resenyas.add(linea);
            }
            pelicula.setResenyas(resenyas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pelicula;
    }

    /**
     * Método privado para obtener el último ID de película utilizado.
     * @return El último ID de película.
     */
    private int obtenerUltimoId() {
        File directorio = new File(DIRECTORIO_PELIS);
        File[] ficheros = directorio.listFiles();
        List<Integer> ids = new ArrayList<>();
        if (ficheros != null) {
            for (File fichero : ficheros) {
                if (fichero.isFile()) {
                    String nombre = fichero.getName();
                    String idStr = nombre.replace(".txt", "");
                    ids.add(Integer.parseInt(idStr));
                }
            }
        }
        if (ids.isEmpty()) {
            return 0;
        } else {
            return Collections.max(ids);
        }
    }
}
