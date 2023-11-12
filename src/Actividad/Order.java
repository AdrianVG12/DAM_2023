package Actividad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * La clase Order representa la interfaz gráfica para recoger información sobre la fabricación de tetrominos.
 * Permite al usuario especificar la cantidad de cada tipo de tetromino y configurar la salida de Manufacture.
 */
public class Order extends JFrame {
    private JTextField quantityITextField;
    private JTextField quantityOTextField;
    private JTextField quantityTTextField;
    private JTextField quantityJTextField;
    private JTextField quantityLTextField;
    private JTextField quantitySTextField;
    private JTextField quantityZTextField;
    private JTextField outputFileNameTextField;

    
    /**
     * Constructor de la clase Order.
     * Inicializa la interfaz gráfica con campos de entrada y un botón de inicio de fabricación.
     */
    public Order() {
        super("Aplicación Terónimos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 2));

        add(new JLabel("Cantidad de piezas I:"));
        quantityITextField = new JTextField();
        add(quantityITextField);

        add(new JLabel("Cantidad de piezas O:"));
        quantityOTextField = new JTextField();
        add(quantityOTextField);

        add(new JLabel("Cantidad de piezas T:"));
        quantityTTextField = new JTextField();
        add(quantityTTextField);

        add(new JLabel("Cantidad de piezas J:"));
        quantityJTextField = new JTextField();
        add(quantityJTextField);

        add(new JLabel("Cantidad de piezas L:"));
        quantityLTextField = new JTextField();
        add(quantityLTextField);

        add(new JLabel("Cantidad de piezas S:"));
        quantitySTextField = new JTextField();
        add(quantitySTextField);

        add(new JLabel("Cantidad de piezas Z:"));
        quantityZTextField = new JTextField();
        add(quantityZTextField);

        add(new JLabel("Nombre del Archivo de Salida:"));
        outputFileNameTextField = new JTextField();
        add(outputFileNameTextField);

        JButton startButton = new JButton("Iniciar Fabricación");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarFabricacion();
            }
        });
        add(startButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    /**
     * Método privado que se llama cuando se presiona el botón de inicio de fabricación.
     * Recoge la información de la interfaz gráfica y crea un nuevo hilo para iniciar la fabricación.
     */
    
    private void iniciarFabricacion() {
        int cantidadI = Integer.parseInt(quantityITextField.getText());
        int cantidadO = Integer.parseInt(quantityOTextField.getText());
        int cantidadT = Integer.parseInt(quantityTTextField.getText());
        int cantidadJ = Integer.parseInt(quantityJTextField.getText());
        int cantidadL = Integer.parseInt(quantityLTextField.getText());
        int cantidadS = Integer.parseInt(quantitySTextField.getText());
        int cantidadZ = Integer.parseInt(quantityZTextField.getText());
        boolean archivoSalida = true; // Puedes modificar esto según la entrada del usuario
        String nombreArchivoSalida = outputFileNameTextField.getText();

        Thread manufacture = new Thread(new Manufacture(
                cantidadI, cantidadO, cantidadT, cantidadJ,
                cantidadL, cantidadS, cantidadZ,
                archivoSalida, nombreArchivoSalida
        ));
        manufacture.start();
    }

    /**
     * Método principal que crea una instancia de la clase Order y la muestra en el hilo de eventos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Order();
            }
        });
    }
}



