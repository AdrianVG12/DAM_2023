package proyect;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JFileChooser;



public class ventanaUI extends JFrame {
    
	private static final long serialVersionUID = 1L; // Salia adverrtencia, aplique primera solucion para garantizar la compatibilidad entre diferentes versiones de la clase 
	private JTextField directorioContent;
    private JTextArea resultadoArea;
    private JRadioButton nombreAscendente, nombreDescendente, tamañoAscendente, tamañoDescendente, fechaAscendente, fechaDescendente;
    private JButton buscarCoincidenciasButton;
    private JTextField cadenaBusqueda; 
    private JButton fusionTxt;
    private JTextField nombreNuevoArchivo;
    private JFileChooser fileChooser;

    

    public ventanaUI() {
    	
        getContentPane().setBackground(new Color(200, 191, 185));
        setTitle("GESTOR DE ARCHIVOS DE TEXTO (.txt) ");
        setSize(750, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel de entrada y botón
        JPanel entradaPanel = new JPanel();
        directorioContent = new JTextField(40);
        directorioContent.setBackground(new Color(192, 192, 192));
        JButton buscarButton = new JButton("BUSCAR");
        buscarButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		buscarArchivos();
        	}
        });
        buscarButton.setBackground(new Color(187, 187, 255));

        // Usar GridLayout para organizar la entrada y el botón
        entradaPanel.setLayout(new GridLayout(1, 2));// Usar GridLayout para organizar la entrada y el botón
        entradaPanel.add(new JLabel("INGRESA RUTA DIRECTORIO:"));
        entradaPanel.add(directorioContent);
        entradaPanel.add(buscarButton);
        getContentPane().add(entradaPanel, BorderLayout.NORTH); // Agregar el panel de entrada en la parte superior (NORTH) del BorderLayout

        // Panel de búsqueda de coincidencias

        
        resultadoArea = new JTextArea();
        resultadoArea.setBackground(new Color(255, 251, 251));
        resultadoArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultadoArea); // Agregar el resultadoArea al centro (CENTER) del BorderLayout,  JScrollPane PARA DESPLAZARSZE.
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        //Campo de texto para la cadena de busqueda
        cadenaBusqueda = new JTextField(20);
        cadenaBusqueda.setToolTipText("");
        scrollPane.setColumnHeaderView(cadenaBusqueda);
        cadenaBusqueda.setHorizontalAlignment(SwingConstants.CENTER);
        
        //Crear boton coincidendicas
        buscarCoincidenciasButton = new JButton("BUSCAR COINCIDENCIAS EN LOS TXT");
        scrollPane.setRowHeaderView(buscarCoincidenciasButton);
        
        // Asignar ActionListener al botón de búsqueda de coincidencias
        buscarCoincidenciasButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cadenaBusquedaTexto = cadenaBusqueda.getText();
                if (cadenaBusquedaTexto.isEmpty()) {
                    // Si la cadena de búsqueda está vacía, muestra un mensaje
                    cadenaBusqueda.setText("Indica aqui lo que quieres buscar en los txt:");
                } else {
                    // Realiza la búsqueda de coincidencias solo si la cadena de búsqueda no está vacía
                    buscarCoincidenciasEnArchivos(cadenaBusquedaTexto);
                }
            }
        });

        // Panel de opciones de ordenación
        JPanel opcionesOrdenacionPanel = new JPanel();
        opcionesOrdenacionPanel.setLayout(new GridLayout(3, 5));

        //Creacion botones ordenación 
        nombreAscendente = new JRadioButton("Nombre Ascendente");
        nombreAscendente.setBackground(new Color(156, 254, 198));
        nombreDescendente = new JRadioButton("Nombre Descendente");
        nombreDescendente.setBackground(new Color(255, 190, 183));
        tamañoAscendente = new JRadioButton("Tamaño Ascendente");
        tamañoAscendente.setBackground(new Color(156, 254, 198));
        tamañoDescendente = new JRadioButton("Tamaño Descendente");
        tamañoDescendente.setBackground(new Color(255, 190, 183));
        fechaAscendente = new JRadioButton("Fecha Ascendente");
        fechaAscendente.setBackground(new Color(156, 254, 198));
        fechaDescendente = new JRadioButton("Fecha Descendente");
        fechaDescendente.setBackground(new Color(255, 190, 183));
        
        //Creacon grupo ordenacion para que se selecione 1 solo de entre todos.
        ButtonGroup grupoOrdenacion = new ButtonGroup();
        grupoOrdenacion.add(nombreAscendente);
        grupoOrdenacion.add(nombreDescendente);
        grupoOrdenacion.add(tamañoAscendente);
        grupoOrdenacion.add(tamañoDescendente);
        grupoOrdenacion.add(fechaAscendente);
        grupoOrdenacion.add(fechaDescendente);

        //Los JRadioButtons se agregan al panel opcionesOrdenacionPanel.
        opcionesOrdenacionPanel.add(nombreAscendente);
        opcionesOrdenacionPanel.add(nombreDescendente);
        opcionesOrdenacionPanel.add(tamañoAscendente);
        opcionesOrdenacionPanel.add(tamañoDescendente);
        opcionesOrdenacionPanel.add(fechaAscendente);
        opcionesOrdenacionPanel.add(fechaDescendente);
       
        getContentPane().add(opcionesOrdenacionPanel, BorderLayout.SOUTH);//Orden botones de ordenacion pegados al SUR del panel. 

        
        /**
         * ActionListener utilizado para capturar eventos de ordenación en la interfaz de usuario.
         */
        ActionListener ordenacionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cuando se hace clic en un botón de ordenación, se llamará al método buscarArchivos() para buscar y mostrar archivos en función del orden seleccionado.
                buscarArchivos();
            }
        };
        
        
        //Registro de los ActionListener en los botones ordenacion.
        nombreAscendente.addActionListener(ordenacionListener);
        nombreDescendente.addActionListener(ordenacionListener);
        tamañoAscendente.addActionListener(ordenacionListener);
        tamañoDescendente.addActionListener(ordenacionListener);
        fechaAscendente.addActionListener(ordenacionListener);
        fechaDescendente.addActionListener(ordenacionListener);
        
        // Boton ordenacion por defecto.
        nombreAscendente.setSelected(true);

        
        //Crear el botón para fusionar archivos
        fusionTxt = new JButton("FUSIONAR TXT");
        fusionTxt.setBackground(new Color(156, 254, 198));
        entradaPanel.add(fusionTxt);

        //Crear un cuadro de texto para el nombre del nuevo archivo
        nombreNuevoArchivo = new JTextField(20);
        nombreNuevoArchivo.setHorizontalAlignment(SwingConstants.CENTER);
        nombreNuevoArchivo.setText("NOMBRE FUSION TXT");
        entradaPanel.add(nombreNuevoArchivo);

        // Agregar ActionListener al botón de fusión de archivos
        fusionTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fusionarArchivos();
            }
        });
   
        //filechooser para que pueda elegir que txt fusionar
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

    }
    
    /**
     * Obtiene un comparador utilizado para ordenar archivos en función de la opción de ordenación seleccionada por el usuario utilizando el boton correspondiente.
     * @return Un comparador que define el criterio de ordenación de los archivos.
     */
    private Comparator<File> obtenerComparador() {
        if (nombreDescendente.isSelected()) {
            return Comparator.comparing(File::getName);
        } else if (nombreAscendente.isSelected()) {
            return Comparator.comparing(File::getName).reversed(); 
        } else if (tamañoDescendente.isSelected()) {
            return Comparator.comparingLong(File::length);
        } else if (tamañoAscendente.isSelected()) {
            return Comparator.comparingLong(File::length).reversed();
        } else if (fechaDescendente.isSelected()) {
            return Comparator.comparingLong(File::lastModified);
        } else if (fechaAscendente.isSelected()) {
            return Comparator.comparingLong(File::lastModified).reversed();
        } else {
            // Por defecto, ordenar por nombre ascendente
            return Comparator.comparing(File::getName);
        }
    }
     
    /**
     * Busca archivos con extensión .txt en el directorio especificado y muestra los resultados en un área de texto.
     * @param directorio El directorio en el que se buscarán los archivos .txt.
     * @return No retorna ningún valor.
     */
    private void buscarArchivos() {
        String directorio = directorioContent.getText();
        File dir = new File(directorio);

        // Verificar si el directorio existe y es una carpeta
        if (dir.exists() && dir.isDirectory()) {
            
            File[] archivos = dir.listFiles((dir1, nombre) -> nombre.endsWith(".txt")); // Obtener una lista de archivos con extensión .txt en el directorio

            if (archivos != null && archivos.length > 0) {
                //si se encuentran archivos, ordenar los archivos según la opción seleccionada
                Arrays.sort(archivos, obtenerComparador());

                // Formatea la fecha para mostrar la última modificación 
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                // Configurar el área de texto con los resultados
                resultadoArea.setText("Estos son los archivos .txt encontrados en el directorio: " + directorio + ":\n\n");
                for (File archivo : archivos) {
                    resultadoArea.append("Nombre: " + archivo.getName() + "\n");
                    resultadoArea.append("Extensión: .txt\n");
                    resultadoArea.append("Tamaño: " + archivo.length() + " bytes\n");
                    resultadoArea.append("Última modificación: " + dateFormat.format(archivo.lastModified()) + "\n\n");
                }
            } else {
                resultadoArea.setText("No se encontraron archivos .txt en el directorio.");
            }
        } else {
            resultadoArea.setText("El directorio no existe o no es válido.");
        }
    }

    /**
     * Busca coincidencias de la cadena de búsqueda en los archivos .txt y muestra los resultados.
     * @param cadenaBusqueda La cadena que se busca en los archivos.
     */
    private void buscarCoincidenciasEnArchivos(String cadenaBusqueda) {
        String directorio = directorioContent.getText();
        File dir = new File(directorio);

        if (dir.exists() && dir.isDirectory()) {
            File[] archivos = dir.listFiles((dir1, nombre) -> nombre.endsWith(".txt"));

            if (archivos != null && archivos.length > 0) {
                // Formatea la fecha para mostrar la última modificación
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                // Configurar el área de texto con los resultados
                resultadoArea.setText("Resultados de búsqueda en archivos .txt en el directorio: " + directorio + ":\n\n");

                for (File archivo : archivos) {
                    resultadoArea.append("Nombre: " + archivo.getName() + "\n");
                    resultadoArea.append("Extensión: .txt\n");
                    resultadoArea.append("Tamaño: " + archivo.length() + " bytes\n");
                    resultadoArea.append("Última modificación: " + dateFormat.format(archivo.lastModified()) + "\n");
                    resultadoArea.append("Coincidencias: " + coincidenciasEnArchivo(archivo, cadenaBusqueda) + "\n\n");
                }
            } else {
                resultadoArea.setText("No se encontraron archivos .txt en el directorio.");
            }
        } else {
            resultadoArea.setText("El directorio no existe o no es válido.");
        }
    }

    /**
     * Cuenta el número de coincidencias de una cadena de búsqueda en un archivo .txt.
     * @param archivo El archivo .txt en el que se busca.
     * @param cadenaBusqueda La cadena que se busca.
     * @return El número de coincidencias en el archivo.
     */
    private int coincidenciasEnArchivo(File archivo, String cadenaBusqueda) {
        int coincidencias = 0;
        try {
            FileReader fileReader = new FileReader(archivo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                coincidencias += contarCoincidenciasEnLinea(linea, cadenaBusqueda);
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coincidencias;
    }

    /** 
     * Contador para obtener el numero de coincidencias en cad linea
     * @param linea Lineas de la cadena de busqueda
     * @param cadenaBusqueda Cadena donde se buscan las lineas.
     * @return  El número de coincidencias encontradas en la línea.
     */
    private int contarCoincidenciasEnLinea(String linea, String cadenaBusqueda) {
        int contador = 0;
        int indice = linea.indexOf(cadenaBusqueda);
        while (indice != -1) {
            contador++;
            indice = linea.indexOf(cadenaBusqueda, indice + 1);
        }
        return contador;
    }
    /**
     * Fusiona varios archivos seleccionados en uno solo y lo guarda en el directorio del primer archivo seleccionado.
     * El nombre del nuevo archivo se especifica a través del campo de texto `nombreNuevoArchivo`.
     * Muestra los resultados en el área de texto `resultadoArea`.
     * @throws IOException Si se produce un error al fusionar los archivos.
     */
    private void fusionarArchivos() {
        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File[] archivosSeleccionados = fileChooser.getSelectedFiles();

            if (archivosSeleccionados.length >= 2) {
                try {
                    String nombreNuevo = nombreNuevoArchivo.getText() + ".txt";
                    File archivoNuevo = new File(archivosSeleccionados[0].getParentFile(), nombreNuevo);

                    FileWriter writer = new FileWriter(archivoNuevo);

                    for (File archivo : archivosSeleccionados) {
                        BufferedReader reader = new BufferedReader(new FileReader(archivo));
                        String linea;
                        while ((linea = reader.readLine()) != null) {
                            writer.write(linea + "\n");
                        }
                        reader.close();
                    }

                    writer.close();
                    resultadoArea.setText("Archivos fusionados en " + nombreNuevo);
                } catch (IOException e) {
                    resultadoArea.setText("Error al fusionar archivos: " + e.getMessage());
                }
            } else {
                resultadoArea.setText("Seleccione al menos dos archivos para fusionar.");
            }
        }
    }

    /**
     * Método principal que inicia la aplicación y muestra la interfaz de usuario.
     * @param args Los argumentos de la línea de comandos (no se utilizan en esta aplicación).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventanaUI ventana = new ventanaUI();
                ventana.setVisible(true);
            }
        });
    }
    
}



