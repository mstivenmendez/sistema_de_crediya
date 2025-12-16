package proyecto.crud;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import proyecto.util.IngresoDatos;
import proyecto.util.SesionUsuario;

/**
 * CRUD para todas las operaciones del usuario actualmente logueado.
 * Maneja la obtención de datos, actualizaciones y operaciones relacionadas
 * al usuario en sesión.
 */
public class UsuarioCrud {

    private IngresoDatos conexion = new IngresoDatos();

    /**
     * Obtiene y retorna todos los datos del usuario actualmente logueado
     *
     * @return Map con los datos del usuario (usuario_id, correo, clave, estado,
     *         nombres, apellidos, etc.)
     */
    public Map<String, Object> obtenerDatosUsuario() {
        return obtenerDatosUsuarioPorId(SesionUsuario.getUsuarioId());
    }

    /**
     * Obtiene los datos de un usuario específico por ID desde la vista
     *
     * @param usuarioId ID del usuario
     * @return Map con los datos del usuario
     */
    public Map<String, Object> obtenerDatosUsuarioPorId(int usuarioId) {
        final Map<String, Object> datos = new HashMap<>();

        String sql = "SELECT usuario_id, correo, clave, estado, fecha_creacion, nombre_usuario, " +
                "rol, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, " +
                "documento, telefono, salario, fecha_nacimiento FROM vista_usuarios_datos " +
                "WHERE usuario_id = ?";

        conexion.seleccionar(sql,
                rs -> {
                    try {
                        if (rs.next()) {
                            datos.put("usuario_id", rs.getInt("usuario_id"));
                            datos.put("correo", rs.getString("correo"));
                            datos.put("clave", rs.getString("clave"));
                            datos.put("estado", rs.getString("estado"));
                            datos.put("fecha_creacion", rs.getDate("fecha_creacion"));
                            datos.put("nombre_usuario", rs.getString("nombre_usuario"));
                            datos.put("rol", rs.getString("rol"));
                            datos.put("primer_nombre", rs.getString("primer_nombre"));
                            datos.put("segundo_nombre", rs.getString("segundo_nombre"));
                            datos.put("primer_apellido", rs.getString("primer_apellido"));
                            datos.put("segundo_apellido", rs.getString("segundo_apellido"));
                            datos.put("documento", rs.getString("documento"));
                            datos.put("telefono", rs.getString("telefono"));
                            datos.put("salario", rs.getBigDecimal("salario"));
                            datos.put("fecha_nacimiento", rs.getDate("fecha_nacimiento"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Error al procesar datos: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                },
                ps -> {
                    try {
                        ps.setInt(1, usuarioId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

        return datos;
    }

    /**
     * Actualiza el correo del usuario logueado
     *
     * @param nuevoCorreo Nuevo correo electrónico
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarCorreo(String nuevoCorreo) {
        return actualizarCampo(SesionUsuario.getUsuarioId(), "correo", nuevoCorreo);
    }

    /**
     * Actualiza el nombre de usuario del usuario logueado
     *
     * @param nuevoNombreUsuario Nuevo nombre de usuario
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarNombreUsuario(String nuevoNombreUsuario) {
        return actualizarCampo(SesionUsuario.getUsuarioId(), "nombre_usuario", nuevoNombreUsuario);
    }

    /**
     * Actualiza la contraseña del usuario logueado
     *
     * @param nuevaClave Nueva contraseña
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarContraseña(String nuevaClave) {
        return actualizarCampo(SesionUsuario.getUsuarioId(), "clave", nuevaClave);
    }

    /**
     * Actualiza el teléfono del usuario logueado
     *
     * @param nuevoTelefono Nuevo número de teléfono
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarTelefono(String nuevoTelefono) {
        return actualizarTelefonoInfo(SesionUsuario.getUsuarioId(), nuevoTelefono);
    }

    /**
     * Método auxiliar para actualizar campos en la tabla usuario
     *
     * @param usuarioId ID del usuario
     * @param campo     Nombre del campo a actualizar
     * @param valor     Nuevo valor
     * @return true si fue exitosa la actualización
     */
    private boolean actualizarCampo(int usuarioId, String campo, String valor) {
        String sql = "UPDATE usuario SET " + campo + " = ? WHERE usuario_id = ?";

        int resultado = conexion.ejecutar(sql, ps -> {
            try {
                ps.setString(1, valor);
                ps.setInt(2, usuarioId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return resultado > 0;
    }

    /**
     * Método auxiliar para actualizar el teléfono en la tabla informacion
     *
     * @param usuarioId     ID del usuario
     * @param nuevoTelefono Nuevo teléfono
     * @return true si fue exitosa la actualización
     */
    private boolean actualizarTelefonoInfo(int usuarioId, String nuevoTelefono) {
        String sql = "UPDATE informacion SET telefono = ? WHERE usuario_id_fk = ?";

        int resultado = conexion.ejecutar(sql, ps -> {
            try {
                ps.setString(1, nuevoTelefono);
                ps.setInt(2, usuarioId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return resultado > 0;
    }

    /**
     * Formatea y retorna los datos del usuario como String para mostrar en diálogos
     *
     * @return String con formato de los datos del usuario
     */
    public String obtenerDatosFormateados() {
        Map<String, Object> datos = obtenerDatosUsuario();

        if (datos.isEmpty()) {
            return "No se encontraron datos del usuario.";
        }

        return String.format(
                "═══════════════════════════════════════════════\n" +
                        "     INFORMACIÓN DEL USUARIO\n" +
                        "═══════════════════════════════════════════════\n" +
                        "Usuario ID: %d\n" +
                        "Nombre Usuario: %s\n" +
                        "Rol: %s\n\n" +
                        "DATOS PERSONALES\n" +
                        "───────────────────────────────────────────────\n" +
                        "Nombres: %s %s\n" +
                        "Apellidos: %s %s\n" +
                        "Documento: %s\n" +
                        "Teléfono: %s\n" +
                        "Fecha Nacimiento: %s\n\n" +
                        "INFORMACIÓN DE CUENTA\n" +
                        "───────────────────────────────────────────────\n" +
                        "Correo: %s\n" +
                        "Estado: %s\n" +
                        "Fecha Creación: %s\n" +
                        "═══════════════════════════════════════════════",
                datos.get("usuario_id"),
                datos.get("nombre_usuario"),
                datos.get("rol"),
                datos.get("primer_nombre"),
                datos.get("segundo_nombre"),
                datos.get("primer_apellido"),
                datos.get("segundo_apellido"),
                datos.get("documento"),
                datos.get("telefono"),
                datos.get("fecha_nacimiento"),
                datos.get("correo"),
                datos.get("estado"),
                datos.get("salario"),
                datos.get("fecha_creacion"));
    }
}
