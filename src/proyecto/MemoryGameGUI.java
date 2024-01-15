package proyecto;

import javax.swing.*;

import org.bson.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class MemoryGameGUI {
    private JFrame frame;
    private JButton registerButton;
    private JButton loginButton;
    private JButton playButton;
    private JButton saveButton;
    private JButton hallOfFameButton;
    private JButton startGameButton;
    private JLabel timerLabel;
    private Timer gameTimer;
    private JComboBox<String> difficultyComboBox;
    private JPanel imagesPanel;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private String user;
    private Modelo modelo;
    private List<JButton> visibleButtons;
    private Timer visibilityTimer;
    
    private JButton primerBotonClicado = null;
    private int cantidadImagenesVisibles = 0;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private long tiempoInicio; // Para almacenar el tiempo en que se inicia el juego
    private long tiempoTranscurrido; // Para almacenar el tiempo transcurrido en segundos

    
   
    
   
    /**
     * Constructor que inicializa la interfaz gráfica de usuario (GUI) de Memory Game.
     *
     * @param modelo La instancia del modelo que se utilizará en la GUI.
     * @wbp.parser.entryPoint
     */
    public MemoryGameGUI(Modelo modelo) {
    	this.modelo = modelo;
        this.modelo.inicializarConexion();  // Asegúrate de inicializar la conexión aquí
       // this.gameController = new GameController(this, modelo);
        
        frame = new JFrame("Memory Game");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        textFieldUsername = new JTextField();
        passwordField = new JPasswordField();

        JPanel topPanel = new JPanel();
        registerButton = new JButton("RESGITRO");
        loginButton = new JButton("INICIAR SESION");
        playButton = new JButton("PLAY");
        logoutButton = new JButton("CERRAR SESION");
        logoutButton.setVisible(false);
        timerLabel = new JLabel("TIEMPO");
        topPanel.add(timerLabel);
        topPanel.add(registerButton);
        topPanel.add(loginButton);
        welcomeLabel = new JLabel("MEMORY GAME");
        topPanel.add(welcomeLabel);
        topPanel.add(playButton);
        topPanel.add(logoutButton);
        
        
        
        imagesPanel = new JPanel();

        JPanel bottomPanel = new JPanel();
        saveButton = new JButton("SAVE");
        hallOfFameButton = new JButton("HALL OF FAME"); 
        difficultyComboBox = new JComboBox<>(new String[]{"8", "16"});
        difficultyComboBox.setVisible(false);
        bottomPanel.add(saveButton);
        bottomPanel.add(hallOfFameButton);
        bottomPanel.add(new JLabel("DIFICULTAD:"));
        bottomPanel.add(difficultyComboBox);

        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(imagesPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        /**
         * ActionListener para el botón de registro (registerButton) que muestra una ventana emergente
         * para que el usuario ingrese el nombre de usuario y la contraseña.
         */
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Muestra una ventana emergente para que el usuario ingrese el nombre de usuario y la contraseña
                String username = JOptionPane.showInputDialog(frame, "Indica user:");
                String password = JOptionPane.showInputDialog(frame, "Indica contraseña:");

                try {
                    // Convertir la contraseña a SHA-256
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] hashedPasswordBytes = md.digest(password.getBytes());

                    // Convertir el hash a una representación hexadecimal
                    String hashedPassword = convertirBytesAHexadecimal(hashedPasswordBytes);

                    // Verificar si el usuario ya existe
                    if (modelo.existeUsuario(username)) {
                        JOptionPane.showMessageDialog(frame, "El usuario ya existe. Por favor, elija otro nombre de usuario.");
                        return; // Salir del método si el usuario ya existe
                    }

                    // Continuar con el registro si el usuario no existe
                    modelo.registrarUsuario(username, hashedPassword);
                } catch (Exception ex) {
                    ex.printStackTrace(); // Manejar errores de hashing
                }
            }
        
        

			private static String convertirBytesAHexadecimal(byte[] bytes) {
					StringBuilder hexStringBuilder = new StringBuilder();
					for (byte b : bytes) {
						hexStringBuilder.append(String.format("%02x", b));
					}
					return hexStringBuilder.toString();
			}	

        });

        /**
         * ActionListener para el botón de inicio de sesión (loginButton) que muestra una ventana emergente
         * para que el usuario ingrese el nombre de usuario y la contraseña.
         */
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Muestra una ventana emergente para que el usuario ingrese el nombre de usuario y la contraseña
                String username = JOptionPane.showInputDialog(frame, "Indica user:");
                String password = JOptionPane.showInputDialog(frame, "Indica contraseña:");

                try {
                    // Convertir la contraseña a SHA-256
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] hashedPasswordBytes = md.digest(password.getBytes());
                    String hashedPassword = convertirBytesAHexadecimal(hashedPasswordBytes);

                    // Verificar si el usuario existe y la contraseña es correcta
                    if (modelo.autenticarUsuario(username, hashedPassword)) {
                        // Usuario autenticado correctamente, puedes realizar las acciones correspondientes
                        // por ejemplo, abrir la ventana de juego, mostrar récords, etc.
                        JOptionPane.showMessageDialog(frame, "¡Bienvenido, " + username + "!");
                        logoutButton.setVisible(true);      
                        difficultyComboBox.setVisible(true);

                        // Establecer el nombre de usuario en el modelo
                        modelo.setNombreUsuario(username);

                        // Mostrar la parrilla
                        mostrarParrilla("2x4");
                        // Realiza las acciones correspondientes al login exitoso
                    } else {
                        // Usuario o contraseña incorrectos, muestra un mensaje de error
                        JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // Manejar errores de hashing
                }
            }

            private String convertirBytesAHexadecimal(byte[] bytes) {
                StringBuilder hexStringBuilder = new StringBuilder();
                for (byte b : bytes) {
                    hexStringBuilder.append(String.format("%02x", b));
                }
                return hexStringBuilder.toString();
            }
        });

        /**
         * ActionListener para el botón de cierre de sesión (logoutButton) que realiza acciones al hacer clic,
         * como restablecer el mensaje de bienvenida y ocultar el botón de cierre de sesión.
         */
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Restablece el mensaje de bienvenida y oculta el botón de cierre de sesión
                welcomeLabel.setText("BIENVENIDO");
                logoutButton.setVisible(false);

                // Otras acciones que puedas necesitar, como limpiar campos, ocultar elementos, etc.

                limpiarParrilla();
            }
            private void limpiarParrilla() {
                imagesPanel.removeAll();
                imagesPanel.revalidate();
                imagesPanel.repaint();
            }
        });
        
        /**
         * ActionListener para el JComboBox de dificultad (difficultyComboBox) que realiza acciones al seleccionar un elemento,
         * como obtener la dificultad seleccionada y mostrar la parrilla correspondiente.
         */
        difficultyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                mostrarParrilla(selectedDifficulty);
            }
            
            
        });
        
        
        /**
         * ActionListener para el JButton "Play" (playButton) que realiza acciones al hacer clic,
         * como detener el temporizador si está activo, obtener la dificultad seleccionada,
         * iniciar el temporizador y mostrar la parrilla.
         */
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica del botón "Play" aquí

                // Detener el temporizador si está activo
                detenerTemporizador();

                // Obtener la dificultad seleccionada
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();

                // Iniciar el temporizador almacenando el tiempo actual
                tiempoInicio = System.currentTimeMillis();

                // Mostrar la parrilla
                mostrarParrilla(selectedDifficulty);
            }
            
            /**
             * Método privado para iniciar el temporizador.
             *
             * @param username           El nombre de usuario.
             * @param selectedDifficulty La dificultad seleccionada.
             */
            private void iniciarTemporizador(String username, String selectedDifficulty) {
                
                gameTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tiempoTranscurrido++;

                        // Actualizar el JLabel con el tiempo transcurrido utilizando invokeLater
                        SwingUtilities.invokeLater(() -> {
                            timerLabel.setText("Tiempo: " + tiempoTranscurrido + " segundos");
                        });

                        // Verificar si todas las imágenes están visibles
                        if (todasImagenesVisibles()) {
                            detenerTemporizador();

                            // Guardar el registro en la base de datos
                            modelo.guardarRecord(username, selectedDifficulty, modelo.obtenerTimestamp(), tiempoTranscurrido);
                        }
                    }
                });
                
                // Iniciar el temporizador
                gameTimer.start();
            }
            
        
            /**
             * Método privado para verificar si todas las imágenes de los botones son visibles.
             *
             * @return true si todas las imágenes son visibles, false en caso contrario.
             */
            private boolean todasImagenesVisibles() {
                Component[] components = imagesPanel.getComponents();

                // Verificar si todas las imágenes de los botones son visibles
                for (Component component : components) {
                    if (component instanceof JButton) {
                        JButton button = (JButton) component;
                        if (!button.isVisible()) {
                            return false;
                        }
                    }
                }
                return true;
            }
            
            /**
             * Método privado para detener el temporizador de Swing si está en funcionamiento.
             */
            private void detenerTemporizador() {
                // Detener el temporizador de swing si está en funcionamiento
                if (gameTimer != null && gameTimer.isRunning()) {
                    gameTimer.stop();
                }
            }
        });

        /**
         * ActionListener para el JButton "Hall of Fame" (hallOfFameButton) que realiza acciones al hacer clic,
         * como obtener y mostrar los registros de la base de datos.
         */
        hallOfFameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para obtener y mostrar los registros
                mostrarRegistros();
            }

            /**
             * Método privado para mostrar los registros en una ventana emergente.
             */
            private void mostrarRegistros() {
                try {
                    // Obtener registros desde el modelo
                    List<Document> records = modelo.obtenerTodosLosRecords();

                    // Ordenar los registros por duración en orden ascendente
                    records.sort(Comparator.comparing(record -> getDuracionFromRecord(record)));

                    // Verificar si hay registros
                    if (records.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "No hay registros en la colección 'records'.");
                        return;
                    }

                    // Crear un StringBuilder para construir el contenido
                    StringBuilder recordsText = new StringBuilder("<html><body>");

                    // Agregar los registros al StringBuilder
                    for (Document record : records) {
                        String usuario = record.getString("usuario");

                        // Corregir la obtención de la dificultad
                        Object dificultadObject = record.get("dificultad");
                        String dificultad = (dificultadObject != null) ? dificultadObject.toString() : "Desconocida";

                        // Corregir la obtención de la duración
                        int duracion = getDuracionFromRecord(record);

                        // Formatear el registro
                        String registro = String.format("%s - Dificultad: %s - Duración: %d segundos<br>", usuario, dificultad, duracion);
                        recordsText.append(registro);
                    }

                    // Cerrar las etiquetas HTML
                    recordsText.append("</body></html>");

                    // Mostrar la ventana emergente con la lista de registros
                    JTextPane textPane = new JTextPane();
                    textPane.setContentType("text/html");
                    textPane.setEditable(false);
                    textPane.setText(recordsText.toString());

                    JScrollPane scrollPane = new JScrollPane(textPane);
                    JOptionPane.showMessageDialog(frame, scrollPane, "Hall of Fame", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error al recuperar registros: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            /**
             * Método privado para obtener la duración desde el documento, maneja tanto Integer como Long.
             *
             * @param record Documento que contiene la duración.
             * @return Duración extraída del documento.
             */
            private int getDuracionFromRecord(Document record) {
                Object duracionObject = record.get("duracion");
                if (duracionObject instanceof Integer) {
                    return (Integer) duracionObject;
                } else if (duracionObject instanceof Long) {
                    return ((Long) duracionObject).intValue();
                }
                return 0;
            }
        });
        
        /**
         * ActionListener para el botón "Save". Contiene la lógica asociada al botón "Save".
         */
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica del botón "Save" aquí

                // Detener el temporizador y calcular el tiempo transcurrido en segundos
                detenerTemporizador();
                long tiempoActual = System.currentTimeMillis();
                tiempoTranscurrido = (tiempoActual - tiempoInicio) / 1000;

                // Obtener la dificultad seleccionada
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();

                // Guardar el registro en la base de datos
                modelo.guardarRecord(modelo.getNombreUsuario(), selectedDifficulty, modelo.obtenerTimestamp(), tiempoTranscurrido);

                // Verificar si es el menor tiempo y mostrar un mensaje de enhorabuena
                if (modelo.esMenorTiempo(modelo.getNombreUsuario(), selectedDifficulty, tiempoTranscurrido)) {
                    JOptionPane.showMessageDialog(frame, "¡Enhorabuena! Has obtenido el menor tiempo para esta dificultad.");
                }
            }

            /**
             * Detiene el temporizador y reinicia variables relacionadas al tiempo.
             */
            private void detenerTemporizador() {
                // Detener el temporizador y reiniciar variables relacionadas al tiempo
                if (gameTimer != null && gameTimer.isRunning()) {
                    gameTimer.stop();
                    tiempoInicio = 0;
                    tiempoTranscurrido = 0;
                    SwingUtilities.invokeLater(() -> {
                        timerLabel.setText("Tiempo: 0 segundos");
                    });
                }
            }
        });


        frame.getContentPane().add(mainPanel);
    }

   

    /**
     * Método para iniciar la interfaz gráfica.
     */
	public void iniciar() {
        frame.setVisible(true);
    }
	
	/**
	 * Método para mostrar la parrilla del juego de memoria según la dificultad seleccionada.
	 * 
	 * @param dificultad Dificultad del juego (por ejemplo, "2x4" o "4x4").
	 */
	private void mostrarParrilla(String dificultad) {
        int totalBotones;
        int columnas;

        if (dificultad.equals("8")) {
            totalBotones = 8;
            columnas = 4;
        } else if (dificultad.equals("16")) {
            totalBotones = 16;
            columnas = 4;
        } else {
            // Manejar el caso de dificultad desconocida o error
            System.out.println("Dificultad desconocida: " + dificultad);
            return;
        }

        crearBotonesParrilla(totalBotones, columnas);

        List<String> imagenes = modelo.obtenerImagenesDesdeBD(totalBotones / 2);
        imagenes.addAll(new ArrayList<>(imagenes));
        Collections.shuffle(imagenes);

        asignarIconosAButtons(imagenes);
    }


	/**
	 * Método para crear los botones de la parrilla del juego de memoria.
	 * 
	 * @param totalBotones Número total de botones en la parrilla.
	 * @param columnas Número de columnas en la parrilla.
	 */
	private void crearBotonesParrilla(int totalBotones, int columnas) {
	    imagesPanel.removeAll(); // Limpiar el panel antes de agregar nuevos botones
	    imagesPanel.setLayout(new GridLayout(totalBotones / columnas, columnas));

	    for (int i = 0; i < totalBotones; i++) {
	        JButton imageButton = new JButton();
	        imagesPanel.add(imageButton);

	        // Agregar ActionListener a cada botón
	        imageButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                onButtonClick((JButton) e.getSource());
	            }

	            /**
	             * Maneja la acción cuando se hace clic en un botón de imagen.
	             *
	             * @param button El botón en el que se hizo clic.
	             */
	            private void onButtonClick(JButton button) {
	                // Verificar si ya hay dos imágenes visibles
	                if (cantidadImagenesVisibles == 2) {
	                    return; // No permitir más clics hasta que se oculten las imágenes actuales
	                }

	                // Obtener la ruta de la imagen desde la propiedad del cliente
	                String rutaImagen = (String) button.getClientProperty("rutaImagen");

	                // Hacer visible la imagen en el botón
	                ImageIcon icon = new ImageIcon(rutaImagen);
	                button.setIcon(icon);

	                // Lógica para comparar imágenes cuando se ha clicado en dos botones
	                if (primerBotonClicado == null) {
	                    // Este es el primer botón clicado
	                    primerBotonClicado = button;
	                    cantidadImagenesVisibles++;
	                } else {
	                    // Se ha clicado un segundo botón, comparar imágenes
	                    String rutaImagenPrimerBoton = (String) primerBotonClicado.getClientProperty("rutaImagen");

	                    if (!rutaImagen.equals(rutaImagenPrimerBoton)) {
	                        // Las imágenes son diferentes, ocultar después de un breve período
	                        SwingUtilities.invokeLater(() -> ocultarBotonesDespuesDeDelay(button, primerBotonClicado));
	                    } else {
	                        // Las imágenes son iguales, déjalas visibles
	                        primerBotonClicado = null;
	                        cantidadImagenesVisibles = 0;
	                    }
	                }
	            }


	            /**
	             * Oculta dos botones después de un breve período de espera.
	             *
	             * @param boton1 El primer botón a ocultar.
	             * @param boton2 El segundo botón a ocultar.
	             */
	            private void ocultarBotonesDespuesDeDelay(JButton boton1, JButton boton2) {
	                try {
	                    Thread.sleep(1000); // Esperar 1 segundo antes de ocultar las imágenes
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }

	                boton1.setIcon(null);
	                boton2.setIcon(null);
	                primerBotonClicado = null;
	                cantidadImagenesVisibles = 0;
	            }
	            
	        });

	        // Puedes agregar oyentes de eventos u otras configuraciones necesarias para los botones aquí
	    }

	    frame.validate();
	    frame.repaint();
	}

	/**
	 * Asigna las rutas de las imágenes a los botones en el panel.
	 *
	 * @param imagenes Lista de rutas de imágenes a asignar a los botones.
	 */
	private void asignarIconosAButtons(List<String> imagenes) {
	    Component[] components = imagesPanel.getComponents(); // Obtener todos los componentes en el panel

	    for (int i = 0; i < components.length; i++) {
	        if (components[i] instanceof JButton && i < imagenes.size()) {
	            // Verificar que el componente sea un botón y que haya imágenes disponibles
	            JButton button = (JButton) components[i];

	            // Guardar la ruta de la imagen como propiedad del cliente del botón
	            button.putClientProperty("rutaImagen", imagenes.get(i));
	        }
	    }
	}


	/**
	 * Punto de entrada principal de la aplicación. Inicializa la interfaz gráfica de usuario y el modelo,
	 * y lanza la aplicación en un hilo de despacho de eventos de Swing.
	 *
	 * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
	 */
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Modelo modelo = new Modelo();
            modelo.inicializarConexion();

            MemoryGameGUI gui = new MemoryGameGUI(modelo);
            gui.iniciar();
        });
    }
}

