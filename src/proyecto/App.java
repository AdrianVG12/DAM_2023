package proyecto;

public class App {

	
	/**
     * Método principal que inicia la aplicación.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en esta aplicación).
     */
	public static void main(String[] args) {
        // Crear instancias del modelo, la vista y el controlador
        Modelo modelo = new Modelo();
        modelo.inicializarConexion();
        
        MemoryGameGUI vista = new MemoryGameGUI(modelo);
     
        // Iniciar la aplicación
        vista.iniciar();
    }

}
