package actividadAev2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Clase que gestiona la conexión a la base de datos utilizando la información proporcionada en un archivo XML.
 */
public class DBConnectionManager {
    private Connection connection;

    
    /**
     * Establece una conexión a la base de datos utilizando la URL, nombre de usuario y contraseña proporcionados.
     *
     * @param url      La URL de la base de datos.
     * @param user     El nombre de usuario para la conexión.
     * @param password La contraseña para la conexión.
     * @return La conexión establecida.
     * @throws SQLException Si ocurre un error durante la conexión.
     */
    public Connection connect(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos, si está abierta.
     *
     * @throws SQLException Si ocurre un error al cerrar la conexión.
     */
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexión cerrada exitosamente.");
        } else {
            System.out.println("No hay conexión para cerrar.");
        }
    }

    /**
     * Obtiene la conexión actual a la base de datos.
     *
     * @return La conexión actual.
     */
    public Connection getConnection() {
        return connection;
    }

    // Puedes agregar métodos para realizar consultas SQL u otras operaciones necesarias
    
    /**
     * Carga la información de conexión desde un archivo XML y establece la conexión a la base de datos.
     *
     * @param xmlFileName El nombre del archivo XML que contiene la información de conexión.
     * @throws SQLException Si ocurre un error durante la carga o establecimiento de la conexión.
     */
    public void loadConnectionInfoFromXML(String xmlFileName) throws SQLException {
        try {
            // Obtener la ruta relativa al directorio resources
        	System.out.println("Cargando archivo XML desde: " + xmlFileName);
            InputStream xmlStream = getClass().getClassLoader().getResourceAsStream(xmlFileName);

            if (xmlStream == null) {
                throw new FileNotFoundException("Archivo XML no encontrado en el classpath: " + xmlFileName);
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlStream);

            Node urlNode = document.getElementsByTagName("url").item(0);
            Node userNode = document.getElementsByTagName("user").item(0);
            Node passwordNode = document.getElementsByTagName("password").item(0);

            String url = urlNode.getTextContent();
            String user = userNode.getTextContent();
            String password = passwordNode.getTextContent();

            connect(url, user, password);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new SQLException("Error al cargar la información de conexión desde el archivo XML", e);
        }
    }


    
    /**
     * Ejecuta una consulta SQL y devuelve el resultado como un conjunto de resultados.
     *
     * @param sql La consulta SQL a ejecutar.
     * @return El conjunto de resultados de la consulta.
     * @throws SQLException Si ocurre un error durante la ejecución de la consulta.
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        // Lógica para ejecutar una consulta SQL y devolver el resultado

        Statement statement = connection.createStatement();
       
        ResultSet resultSet = statement.executeQuery(sql);
        
        return resultSet;
    }

}

	