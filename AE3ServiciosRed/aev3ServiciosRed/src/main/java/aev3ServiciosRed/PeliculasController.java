package aev3ServiciosRed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar las peticiones relacionadas con las películas.
 */
@RestController
@RequestMapping("/APIpelis")
public class PeliculasController {

    private final PeliculasService peliculasService;

    /**
     * Constructor de la clase PeliculasController.
     * @param peliculasService El servicio de películas a inyectar.
     */
    @Autowired
    public PeliculasController(PeliculasService peliculasService) {
        this.peliculasService = peliculasService;
    }

    /**
     * Método para obtener los títulos de las películas.
     * @param id El ID de la película a buscar (opcional).
     * @return ResponseEntity con la lista de películas o código de estado 404 si no se encontró la película.
     */
    @GetMapping("/t")
    public ResponseEntity<?> obtenerTitulos(@RequestParam(name = "id", required = false) String id) {
        if ("all".equals(id)) {
            List<Pelicula> peliculas = peliculasService.obtenerTodas();
            return new ResponseEntity<>(peliculas, HttpStatus.OK);
        } else {
            Pelicula pelicula = peliculasService.obtenerPorId(id);
            if (pelicula != null) {
                return new ResponseEntity<>(Collections.singletonList(pelicula), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Método para obtener las reseñas de una película específica.
     * @param id El ID de la película.
     * @return ResponseEntity con la película o código de estado 404 si no se encontró la película.
     */
    @GetMapping("/t/{id}")
    public ResponseEntity<?> obtenerResenyas(@PathVariable String id) {
        Pelicula pelicula = peliculasService.obtenerPorId(id);
        if (pelicula != null) {
            return new ResponseEntity<>(Collections.singletonList(pelicula), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método para agregar una nueva reseña a una película.
     * @param nuevaResenya Mapa que contiene el ID de la película, el usuario y la reseña.
     * @return ResponseEntity con el código de estado 200 (OK).
     */
    @PostMapping("/nuevaResenya")
    public ResponseEntity<?> agregarResenya(@RequestBody Map<String, String> nuevaResenya) {
        String idPelicula = nuevaResenya.get("id");
        String usuario = nuevaResenya.get("usuario");
        String resenya = nuevaResenya.get("resenya");

        peliculasService.agregarResenya(idPelicula, usuario, resenya);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Método para agregar una nueva película.
     * @param nuevaPelicula Mapa que contiene el título de la película.
     * @return ResponseEntity con el código de estado 200 (OK).
     */
    @PostMapping("/nuevaPeli")
    public ResponseEntity<?> agregarNuevaPelicula(@RequestBody Map<String, String> nuevaPelicula) {
        String titulo = nuevaPelicula.get("titulo");

        peliculasService.agregarNuevaPelicula(titulo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
