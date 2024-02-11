package aev3ServiciosRed;

import java.util.List;

/**
 * Clase que representa una película con su título y lista de reseñas.
 */
public class Pelicula {
    private String id;
    private String titulo;
    private List<String> resenyas;

    /**
     * Constructor de la clase Pelicula.
     */
    public Pelicula() {
        this.id = id;
        this.titulo = titulo;
        this.resenyas = resenyas;
    }

    /**
     * Método getter para obtener el ID de la película.
     * @return El ID de la película.
     */
    public String getId() {
        return id;
    }

    /**
     * Método setter para establecer el ID de la película.
     * @param id El ID de la película.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Método getter para obtener el título de la película.
     * @return El título de la película.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Método setter para establecer el título de la película.
     * @param titulo El título de la película.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Método getter para obtener la lista de reseñas de la película.
     * @return La lista de reseñas de la película.
     */
    public List<String> getResenyas() {
        return resenyas;
    }

    /**
     * Método setter para establecer la lista de reseñas de la película.
     * @param resenyas La lista de reseñas de la película.
     */
    public void setResenyas(List<String> resenyas) {
        this.resenyas = resenyas;
    }

    /**
     * Método para agregar una nueva reseña a la película.
     * @param resenya La nueva reseña a agregar.
     */
    public void agregarResenya(String resenya) {
        this.resenyas.add(resenya);
    }
}

