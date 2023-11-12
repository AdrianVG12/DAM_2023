package actividadAev2;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que representa la interfaz gráfica de usuario para el inicio de sesión.
 */
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    
    /**
     * Constructor que crea una instancia de la interfaz de inicio de sesión.
     *
     * @param loginButtonListener ActionListener para el botón de inicio de sesión.
     */
    public LoginView(ActionListener loginButtonListener) {
        super("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(3, 2));

        getContentPane().add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        getContentPane().add(usernameField);

        getContentPane().add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        getContentPane().add(passwordField);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(loginButtonListener);
        getContentPane().add(loginButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Obtiene el nombre de usuario ingresado por el usuario.
     *
     * @return Nombre de usuario ingresado.
     */
    public String getUsername() {
        return usernameField.getText();
    }

    /**
     * Obtiene la contraseña ingresada por el usuario.
     *
     * @return Contraseña ingresada.
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
