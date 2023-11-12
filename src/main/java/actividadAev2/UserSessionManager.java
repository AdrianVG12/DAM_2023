package actividadAev2;


/**
 * Clase que gestiona la sesión de usuario, incluyendo el inicio de sesión, cierre de sesión y verificación del estado de la sesión.
 */
public class UserSessionManager {
    private String currentUser;
    private boolean isAdmin;

   
    /**
     * Inicia sesión con las credenciales proporcionadas y establece el estado de la sesión.
     *
     * @param username El nombre de usuario para iniciar sesión.
     * @param password La contraseña asociada al nombre de usuario.
     */
    public void login(String username, String password) {
        
        if (username.equals("admin") && password.equals("admin")) {
            currentUser = "admin";
            isAdmin = true;
        } else if (username.equals("client") && password.equals("client")) {
            currentUser = "client";
            isAdmin = false;
        } else {
            currentUser = null;
            isAdmin = false;
        }
    }

    /**
     * Cierra la sesión actual y limpia el estado de la sesión.
     */
    public void logout() {
        // Limpiar el estado de la sesión al cerrar sesión
        currentUser = null;
        isAdmin = false;
    }

    /**
     * Verifica si hay un usuario actualmente autenticado.
     *
     * @return `true` si hay un usuario autenticado, de lo contrario, `false`.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Verifica si el usuario actual es un administrador.
     *
     * @return `true` si el usuario actual es un administrador, de lo contrario, `false`.
     */
    public boolean isAdmin() {
        return isAdmin;
    }
}
