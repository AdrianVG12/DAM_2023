package actividadAev2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;


/**
 * Clase controladora de la aplicación que gestiona la lógica de la interfaz de usuario
 * y las operaciones relacionadas con la base de datos.
 */
public class AppController {
    private DBConnectionManager connectionManager;
    private UserSessionManager sessionManager;
    private LoginView loginView;
    private MainView mainView;

    public AppController() {
        connectionManager = new DBConnectionManager();
        sessionManager = new UserSessionManager();
        loginView = new LoginView(new LoginButtonListener());

        try {
            // Cargar información de conexión desde el archivo XML para "client" al inicio
            connectionManager.loadConnectionInfoFromXML("client.xml");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la información de conexión", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ActionListener para el botón de inicio de sesión.
     * Realiza la verificación de las credenciales y gestiona la lógica de inicio de sesión.
     */
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            try {
                // Realizar verificación de credenciales en la base de datos
                connectionManager.loadConnectionInfoFromXML(username.equals("admin") ? "src/main/resources/admin.xml" : "src/main/resources/client.xml");

                // Manejar la lógica de inicio de sesión aquí
                if (username.equals("admin")) {
                    sessionManager.login("admin", "admin");
                } else if (username.equals("client")) {
                    sessionManager.login("client", "client");
                } else {
                    throw new SQLException("Usuario no reconocido");
                }

                mainView = new MainView(new LogoutButtonListener(), new CloseButtonListener());
                loginView.dispose(); // Cerrar la ventana de inicio de sesión
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginView, "Error al iniciar sesión", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * ActionListener para el botón de cierre de sesión.
     * Maneja la lógica de cierre de sesión y actualiza la interfaz de usuario.
     */
    private class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           
            sessionManager.logout();
            loginView = new LoginView(new LoginButtonListener());
            mainView.dispose(); // Cerrar la ventana principal
        }
    }
    
    
    /**
     * ActionListener para el botón de cierre de la aplicación.
     * Maneja la lógica de cierre de conexión a la base de datos y cierra la aplicación.
     */
    private class CloseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           
            try {
                connectionManager.disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainView, "Error al cerrar la conexión", "Error", JOptionPane.ERROR_MESSAGE);
            }

            System.exit(0); // Cerrar la aplicación
        }
    }

    /**
     * Método principal que inicia la aplicación creando una instancia de AppController.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        new AppController();
    }
}
    
