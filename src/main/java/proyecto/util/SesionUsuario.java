package proyecto.util;

/**
 * Clase para gestionar la sesión del usuario actualmente logueado
 */
public class SesionUsuario {
    
    private static int usuarioId = 0;
    private static String nombreUsuario = "";
    private static String rol = "";
    
    /**
     * Establece el ID del usuario en la sesión
     */
    public static void setUsuarioId(int id) {
        usuarioId = id;
    }
    
    /**
     * Obtiene el ID del usuario en la sesión actual
     */
    public static int getUsuarioId() {
        return usuarioId;
    }
    
    /**
     * Establece el nombre de usuario en la sesión
     */
    public static void setNombreUsuario(String nombre) {
        nombreUsuario = nombre;
    }
    
    /**
     * Obtiene el nombre de usuario en la sesión actual
     */
    public static String getNombreUsuario() {
        return nombreUsuario;
    }
    
    /**
     * Establece el rol del usuario en la sesión
     */
    public static void setRol(String userRol) {
        rol = userRol;
    }
    
    /**
     * Obtiene el rol del usuario en la sesión actual
     */
    public static String getRol() {
        return rol;
    }
    
    /**
     * Cierra la sesión actual
     */
    public static void cerrarSesion() {
        usuarioId = 0;
        nombreUsuario = "";
        rol = "";
    }
    
    /**
     * Verifica si hay una sesión activa
     */
    public static boolean sesionActiva() {
        return usuarioId > 0;
    }
}
