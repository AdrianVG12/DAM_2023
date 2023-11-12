package actividadAev2;


import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Vista principal de la aplicación que muestra los resultados de las consultas
 * y proporciona botones para cerrar sesión y cerrar la conexión a la base de datos.
 */
public class MainView extends JFrame {
    private JTextArea resultArea;
    private DBConnectionManager dbConnectionManager;
    private JTextField queryField;
    
    
    /**
     * Constructor de la clase MainView.
     *
     * @param logoutButtonListener ActionListener para el botón de cerrar sesión.
     * @param closeButtonListener ActionListener para el botón de cerrar conexión.
     */
    public MainView(ActionListener logoutButtonListener, ActionListener closeButtonListener) {
        super("Aplicación MVC - Base de Datos");
        this.dbConnectionManager = dbConnectionManager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        resultArea = new JTextArea();
        getContentPane().add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(logoutButtonListener);
        getContentPane().add(logoutButton, BorderLayout.NORTH);

        JButton executeButton = new JButton("Ejecutar Consulta");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = queryField.getText();
                try {
                    ResultSet resultSet = dbConnectionManager.executeQuery(query);

                    StringBuilder resultText = new StringBuilder();
                    while (resultSet.next()) {
                        resultText.append(resultSet.getString(1)).append("\n");
                        // Ajusta este código según la estructura de tu base de datos y las columnas que deseas mostrar
                    }
                    resultArea.setText(resultText.toString());

                    // Cerrar el ResultSet y liberar recursos
                    resultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    resultArea.setText("Error al ejecutar la consulta: " + ex.getMessage());
                }
            }
        });
        getContentPane().add(executeButton, BorderLayout.EAST);
        
        JButton closeButton = new JButton("Cerrar Conexión");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Llamada al método para cerrar la conexión en DBConnectionManager
                    dbConnectionManager.disconnect();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MainView.this, "Error al cerrar la conexión", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        closeButton.addActionListener(closeButtonListener);
        getContentPane().add(closeButton, BorderLayout.SOUTH);
       

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Obtiene el área de texto utilizada para mostrar los resultados de las consultas.
     *
     * @return Área de texto de resultados.
     */
    public JTextArea getResultArea() {
        return resultArea;
    }
}
