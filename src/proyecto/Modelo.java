package proyecto;



import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;

import java.util.Date;
import java.util.List;





import javax.imageio.ImageIO;


import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.mongodb.client.model.Sorts;

/**
 * La clase Modelo se encarga de manejar la conexión a la base de datos MongoDB,
 * así como realizar operaciones relacionadas con la autenticación de usuarios,
 * registro de usuarios, obtención de imágenes desde la base de datos,
 * guardado de récords y consultas relacionadas con los registros de juego.
 */

public class Modelo {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String IMG_DIRECTORY = "img/";
    private String nombreUsuario;

    /**
     * Obtiene el nombre de usuario actualmente autenticado.
     *
     * @return Nombre de usuario.
     */
    public String getNombreUsuario() {
        return this.nombreUsuario;
    }
  
    /**
     * Establece el nombre de usuario actualmente autenticado.
     *
     * @param nombreUsuario Nuevo nombre de usuario.
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    /**
     * Inicializa la conexión a la base de datos MongoDB utilizando la configuración
     * proporcionada en el archivo "config.json".
     */
    public void inicializarConexion() {
        try {
            // Lee la configuración desde el archivo JSON
        	JSONObject config = new JSONObject(new JSONTokener(new FileReader("config.json")));

        	System.out.println("Contenido del objeto JSON: " + config.toString());
        	//Obtener el tmaño de la colecion
    		

            // Obtiene la información necesaria para la conexión a la base de datos
            String ip = config.getString("ip");
            int puerto = config.getInt("puerto");
            String nombreBaseDatos = "memory";

            // Crea una conexión usando la cadena de conexión URI de MongoDB
            MongoClientURI uri = new MongoClientURI("mongodb://" + ip + ":" + puerto);
            mongoClient = new MongoClient(uri);

            // Obtiene la base de datos
            database = mongoClient.getDatabase(nombreBaseDatos);
          System.out.println("Base de datos: "+ nombreBaseDatos);
          System.out.println("Colecciones disponibles en la base de datos: " + database.listCollectionNames().into(new ArrayList<>()));
            
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar errores de lectura del archivo JSON o conexión a la base de datos
        }
    }

    /**
     * Verifica si un usuario ya existe en la base de datos.
     *
     * @param user Nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existeUsuario(String user) {
        MongoCollection<Document> usuariosCollection = database.getCollection("usuarios");

        try {
            Bson filter = Filters.eq("user", user);
            long count = usuariosCollection.countDocuments(filter);

            return count > 0;
        } catch (Exception e) {
            e.printStackTrace(); // Manejar errores de consulta
            return false;
        }
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user           Nombre de usuario.
     * @param hashedPassword Contraseña hasheada del usuario.
     */
    public void registrarUsuario(String user, String hashedPassword) {
        MongoCollection<Document> usuariosCollection = database.getCollection("usuarios");

        Document nuevoUsuario = new Document("user", user)
                .append("pass", hashedPassword);

        usuariosCollection.insertOne(nuevoUsuario);
    }
    
    /**
     * Autentica a un usuario verificando su nombre de usuario y contraseña hasheada.
     *
     * @param username       Nombre de usuario.
     * @param hashedPassword Contraseña hasheada del usuario.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    public boolean autenticarUsuario(String username, String hashedPassword) {
        MongoCollection<Document> usuariosCollection = database.getCollection("usuarios");

        Document query = new Document("user", username).append("pass", hashedPassword);
        long count = usuariosCollection.countDocuments(query);

        return count > 0;
    }

    /**
     * Obtiene una lista de rutas de imágenes desde la base de datos.
     *
     * @param cantidad Cantidad de imágenes a obtener.
     * @return Lista de rutas de imágenes.
     */
    public List<String> obtenerImagenesDesdeBD(int cantidad) {
        List<String> rutasImagenes = new ArrayList<>();

        try {
            MongoCollection<Document> coleccionImagenes = database.getCollection("img");

            // Obtener las imágenes codificadas desde la base de datos
            FindIterable<Document> resultados = coleccionImagenes.find().limit(cantidad);
            int i = 0;
            for (Document resultado : resultados) {
                String imagenCodificada = resultado.getString("base64");

                // Convertir y guardar la imagen como archivo JPG
                String nombreArchivo = "imagen" + i + ".jpg";
                String rutaImagen = guardarImagenComoJPG(imagenCodificada, nombreArchivo);
                rutasImagenes.add(rutaImagen);
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace(); // Manejar errores de consulta
        }

        return rutasImagenes;
    }
    
    /**
     * Guarda una imagen codificada en Base64 como un archivo JPG en el directorio especificado.
     *
     * @param imagenBase64   Cadena Base64 que representa la imagen.
     * @param nombreArchivo  Nombre del archivo a guardar.
     * @return Ruta completa del archivo guardado o null si hay un error.
     */
    private String guardarImagenComoJPG(String imagenBase64, String nombreArchivo) {
        try {
            // Decodificar la cadena Base64
            byte[] btDataFile = Base64.getDecoder().decode(imagenBase64);

            // Crear un BufferedImage directamente desde el array de bytes
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(btDataFile));

            // Crear el directorio si no existe
            File directorio = new File(IMG_DIRECTORY);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Crear el archivo en el directorio img
            String rutaCompleta = IMG_DIRECTORY + nombreArchivo;
            FileOutputStream fos = new FileOutputStream(rutaCompleta);

            // Guardar el BufferedImage como archivo JPG
            ImageIO.write(bufferedImage, "jpg", fos);
            fos.close();

            return rutaCompleta;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * Guarda un nuevo récord en la base de datos.
     *
     * @param usuario          Nombre de usuario.
     * @param dificultad       Dificultad del juego.
     * @param timestamp        Marca de tiempo del récord.
     * @param tiempoTranscurrido Duración del juego.
     */
    public void guardarRecord(String usuario, String dificultad, String timestamp, long tiempoTranscurrido) {
        try {
            MongoCollection<Document> coleccionRecords = database.getCollection("records");

            Document nuevoRecord = new Document("usuario", usuario)
                .append("dificultad", dificultad)
                .append("timestamp", timestamp)
                .append("duracion", tiempoTranscurrido);

            coleccionRecords.insertOne(nuevoRecord);
        } catch (Exception e) {
            e.printStackTrace(); // Manejar errores de inserción
        }
    }

    /**
     * Obtiene la marca de tiempo actual en formato YYYYMMdd_HHmmss.
     *
     * @return Marca de tiempo actual.
     */
    public String obtenerTimestamp() {
        // Crear un objeto SimpleDateFormat con el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        // Obtener la fecha y hora actual
        Date fechaActual = new Date();

        // Formatear la fecha y hora como String
        return dateFormat.format(fechaActual);
    }

    /**
     * Verifica si el tiempo transcurrido es menor que el tiempo registrado para un usuario y dificultad específicos.
     *
     * @param usuario           Nombre de usuario.
     * @param dificultad        Dificultad del juego.
     * @param tiempoTranscurrido Tiempo transcurrido a comparar.
     * @return true si el tiempo transcurrido es menor, false en caso contrario o si hay algún error.
     */
	public boolean esMenorTiempo(String usuario, String dificultad, long tiempoTranscurrido) {
	    try {
	        MongoCollection<Document> coleccionRecords = database.getCollection("records");

	        // Crear un filtro para obtener registros específicos de usuario y dificultad
	        Bson filtro = Filters.and(
	                Filters.eq("usuario", usuario),
	                Filters.eq("dificultad", dificultad)
	        );

	        // Ordenar los registros por duración en orden ascendente
	        Bson orden = Sorts.ascending("duracion");

	        // Realizar una consulta para obtener el registro con el menor tiempo
	        Document registroMenorTiempo = coleccionRecords.find(filtro).sort(orden).first();

	        if (registroMenorTiempo != null) {
	            // Obtener la duración del registro con el menor tiempo
	            int menorTiempoRegistrado = registroMenorTiempo.getInteger("duracion");

	            // Comparar con el tiempo actual
	            return tiempoTranscurrido < menorTiempoRegistrado;
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); // Manejar errores de consulta
	    }

	    return false; // Devolver false si ocurre algún error
	}

	/**
     * Obtiene todos los registros de la colección "records" y los devuelve en forma de lista de Documentos.
     *
     * @return Lista de Documentos que representan los registros en la base de datos.
     */
	public List<Document> obtenerTodosLosRecords() {
	    List<Document> records = new ArrayList<>();

	    try {
	        MongoCollection<Document> coleccionRecords = database.getCollection("records");

	        // Obtener todos los documentos de la colección "records"
	        FindIterable<Document> resultados = coleccionRecords.find();

	        // Iterar sobre los documentos y agregarlos a la lista
	        try (MongoCursor<Document> cursor = resultados.iterator()) {
	            while (cursor.hasNext()) {
	                records.add(cursor.next());
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // Manejar errores de consulta
	    }

	    return records;
	}
	}
