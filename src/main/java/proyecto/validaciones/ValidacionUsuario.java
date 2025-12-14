package proyecto.validaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import proyecto.conector.ConexionDB;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.util.SesionUsuario;

public class ValidacionUsuario {

   private IngresoDatos conexion = new IngresoDatos();
   private Validacion validar = new Validacion();
   private Datos insertar = new Datos();

   /**
    * Valida si existe un usuario con las credenciales proporcionadas
    *
    * @param nombreUsuario Nombre de usuario
    * @param clave         Contraseña del usuario
    * @return true si el usuario existe y las credenciales son correctas
    */
   public boolean validarCredenciales(String nombreUsuario, String clave) {
      final boolean[] existe = { false };
      final int[] userId = { 0 };

      String sql = "SELECT nombre_usuario, clave, usuario_id FROM usuario WHERE nombre_usuario = ? AND clave = ?";

      conexion.seleccionar(sql,
            rs -> {
               try {
                  if (rs.next()) {
                     existe[0] = true;
                     userId[0] = rs.getInt("usuario_id");
                  }
               } catch (Exception e) {
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(null,
                        "Error al validar credenciales: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
               }
            },
            ps -> {
               try {
                  ps.setString(1, nombreUsuario);
                  ps.setString(2, clave);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            });
      
      // Guardar el usuario_id en la sesión si las credenciales son válidas
      if (existe[0]) {
         SesionUsuario.setUsuarioId(userId[0]);
      }
      
      return existe[0];
   }

   /**
    * Valida si existe un usuario y solicita las credenciales con validación
    * 
    *
    * @return true si el usuario existe y las credenciales son válidas
    */
   public boolean ValidacionUsuarioExistente() {
      try {
         // Solicitar el nombre de usuario
         String nombreUsuario = insertar.EnterUser();


         // Validar que no sea nulo o vacío
         if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                  "Usuario no puede estar vacío",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(null,
                  "Usuario no puede estar vacío",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            return false;
         }

         // Validar formato del usuario
         String usuarioValidado = validar.ValidarUsuarioU(nombreUsuario);
         if (usuarioValidado == null) {
            return false;
         }

         // Solicitar la contraseña
         String clave = insertar.EnterPassword();


         // Validar que no sea nula o vacía
         if (clave == null || clave.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                  "Contraseña no puede estar vacía",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(null,
                  "Contraseña no puede estar vacía",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            return false;
         }

         // Validar formato de la contraseña
         String claveValidada = validar.ValidarClave(clave);
         if (claveValidada == null) {
            return false;
         }

         // Validar credenciales en la base de datos
         if (validarCredenciales(usuarioValidado, claveValidada)) {
            JOptionPane.showMessageDialog(null,
                  "✅ Inicio de sesión exitoso\n¡Bienvenido " + usuarioValidado + "!",
                  "Acceso Concedido",
                  JOptionPane.INFORMATION_MESSAGE);
            return true;
         } else {
            JOptionPane.showMessageDialog(null,
                  "❌ Usuario o contraseña incorrectos\nVerifique sus credenciales",
                  "Acceso Denegado",
                  JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null,
                  "❌ Usuario o contraseña incorrectos\nVerifique sus credenciales",
                  "Acceso Denegado",
                  JOptionPane.ERROR_MESSAGE);
            return false;
         }

      } catch (Exception e) {
         JOptionPane.showMessageDialog(null,
               "Error durante la validación: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         return false;
      }
   }

   /**
    * Valida si existe un administrador con las credenciales proporcionadas
    *
    * @return true si es un administrador válido
    */
   public boolean ValidacionAdminExistente() {
      try {
         // Solicitar el nombre de usuario
         String nombreUsuario = insertar.EnterUser();

         if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                  "Usuario no puede estar vacío",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            return false;
         }

         String usuarioValidado = validar.ValidarUsuarioU(nombreUsuario);
         if (usuarioValidado == null) {
            return false;
         }

         // Solicitar la contraseña
         String clave = insertar.EnterPassword();

         if (clave == null || clave.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                  "Contraseña no puede estar vacía",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            return false;
         }

         String claveValidada = validar.ValidarClave(clave);
         if (claveValidada == null) {
            return false;
         }

         // Validar que sea administrador
         final boolean[] esAdmin = { false };

         String sql = "SELECT nombre_usuario, clave, rol FROM usuario WHERE nombre_usuario = ? AND clave = ? AND rol = 'ADMIN'";

         conexion.seleccionar(sql,
               rs -> {
                  try {
                     if (rs.next()) {
                        esAdmin[0] = true;
                     }
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
               },
               ps -> {
                  try {
                     ps.setString(1, usuarioValidado);
                     ps.setString(2, claveValidada);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
               });

         if (esAdmin[0]) {
            JOptionPane.showMessageDialog(null,
                  "✅ Acceso de Administrador concedido\n¡Bienvenido Admin " + usuarioValidado + "!",
                  "Acceso Administrativo",
                  JOptionPane.INFORMATION_MESSAGE);
            return true;
         } else {
            JOptionPane.showMessageDialog(null,
                  "❌ Acceso denegado\nNo tiene permisos de administrador o credenciales incorrectas",
                  "Acceso Denegado",
                  JOptionPane.ERROR_MESSAGE);
            return false;
         }

      } catch (Exception e) {
         JOptionPane.showMessageDialog(null,
               "Error durante la validación de administrador: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         return false;
      }
   }

   /**
    * Obtiene el rol del usuario
    *
    * @param nombreUsuario Nombre de usuario
    * @param clave         Contraseña del usuario
    * @return El rol del usuario o null si no existe
    */
   public String obtenerRolUsuario(String nombreUsuario, String clave) {
      final String[] rol = { null };

      String sql = "SELECT rol FROM usuario WHERE nombre_usuario = ? AND clave = ?";

      conexion.seleccionar(sql,
            rs -> {
               try {
                  if (rs.next()) {
                     rol[0] = rs.getString("rol");
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            },
            ps -> {
               try {
                  ps.setString(1, nombreUsuario);
                  ps.setString(2, clave);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            });

      return rol[0];
   }

   /**
    * Valida credenciales y retorna el rol del usuario
    *
    * @return El rol del usuario (ADMIN o USUARIO) o null si falla
    */
   public String ValidacionConRol() {
      try {
         // Solicitar el nombre de usuario
         String nombreUsuario = insertar.EnterUser();

         if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                  "Usuario no puede estar vacío",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            return null;
         }

         String usuarioValidado = validar.ValidarUsuarioU(nombreUsuario);
         if (usuarioValidado == null) {
            return null;
         }

         // Solicitar la contraseña
         String clave = insertar.EnterPassword();

         if (clave == null || clave.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                  "Contraseña no puede estar vacía",
                  "Validación",
                  JOptionPane.WARNING_MESSAGE);
            return null;
         }

         String claveValidada = validar.ValidarClave(clave);
         if (claveValidada == null) {
            return null;
         }

         // Obtener el rol del usuario
         String rol = obtenerRolUsuario(usuarioValidado, claveValidada);

         if (rol != null) {
            String tipoUsuario = rol.equalsIgnoreCase("ADMIN") ? "Administrador" : "Usuario";
            JOptionPane.showMessageDialog(null,
                  "✅ Inicio de sesión exitoso\nBienvenido " + usuarioValidado + "\nTipo: " + tipoUsuario,
                  "Acceso Concedido",
                  JOptionPane.INFORMATION_MESSAGE);
            return rol;
         } else {
            JOptionPane.showMessageDialog(null,
                  "❌ Usuario o contraseña incorrectos",
                  "Acceso Denegado",
                  JOptionPane.ERROR_MESSAGE);
            return null;
         }

      } catch (Exception e) {
         JOptionPane.showMessageDialog(null,
               "Error durante la validación: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         return null;
      }
   }

   /**
    * Verifica si un correo ya está registrado en el sistema
    *
    * @param correo Correo a verificar
    * @return true si el correo ya existe
    */
   public boolean correoExiste(String correo) {
      final boolean[] existe = { false };

      String sql = "SELECT correo FROM usuario WHERE correo = ?";

      conexion.seleccionar(sql,
            rs -> {
               try {
                  if (rs.next()) {
                     existe[0] = true;
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            },
            ps -> {
               try {
                  ps.setString(1, correo);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            });

      return existe[0];
   }

   /**
    * Verifica si un nombre de usuario ya está registrado
    *
    * @param nombreUsuario Nombre de usuario a verificar
    * @return true si el nombre de usuario ya existe
    */
   public boolean nombreUsuarioExiste(String nombreUsuario) {
      final boolean[] existe = { false };

      String sql = "SELECT nombre_usuario FROM usuario WHERE nombre_usuario = ?";

      conexion.seleccionar(sql,
            rs -> {
               try {
                  if (rs.next()) {
                     existe[0] = true;
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            },
            ps -> {
               try {
                  ps.setString(1, nombreUsuario);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            });

      return existe[0];
   }

   /**
    * Obtiene información completa del usuario autenticado
    *
    * @param nombreUsuario Nombre de usuario
    * @param clave         Contraseña
    * @return Array con [id, nombre_usuario, correo, rol] o null si no existe
    */
   public void obtenerDatosUsuario(int userId) {
      // Consultar la base de datos para obtener los datos del usuario basado en
      // user_id_fk
      String sql = "SELECT primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, telefono, fecha_nacimiento FROM informacion WHERE usuario_id_fk = ?";

      try (Connection con = ConexionDB.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

         // Establecer el user_id_fk en la consulta
         ps.setInt(1, userId);

         try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
               // Recuperar los datos del usuario
               String primerNombre = rs.getString("primer_nombre");
               String segundoNombre = rs.getString("segundo_nombre");
               String primerApellido = rs.getString("primer_apellido");
               String segundoApellido = rs.getString("segundo_apellido");
               String telefono = rs.getString("telefono");
               String fechaNacimiento = rs.getString("fecha_nacimiento");

               // Mostrar los datos del usuario
               JOptionPane.showMessageDialog(null,
                     "Datos del usuario:\n" +
                           "Nombre: " + primerNombre + " " + segundoNombre + " " + primerApellido + " "
                           + segundoApellido + "\n" +
                           "Teléfono: " + telefono + "\n" +
                           "Fecha de nacimiento: " + fechaNacimiento);
            } else {
               JOptionPane.showMessageDialog(null, "Usuario no encontrado en la tabla 'informacion'.");
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null,
               "Error al obtener los datos del usuario: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
      }
   }

   public Integer validarCedulaYObtener(String documento) {
      // Consultar desde la vista que tiene usuario_id_fk
      String sql = "SELECT usuario_id_fk FROM informacion WHERE documento = ?";

      final Integer[] usuarioId = { null };

      conexion.seleccionar(sql,
            rs -> {
               try {
                  if (rs.next()) {
                     usuarioId[0] = rs.getInt("usuario_id_fk");
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            },
            ps -> {
               try {
                  ps.setString(1, documento);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            });

      // Mostrar mensaje si NO existe el documento
      if (usuarioId[0] == null) {
         JOptionPane.showMessageDialog(null,
               "El documento '" + documento + "' no está registrado en el sistema.",
               "Documento no encontrado",
               JOptionPane.WARNING_MESSAGE);
      }
      return usuarioId[0];
   }

   public boolean ValidarCedula(String documento) {
      String sql = "SELECT COUNT(*) FROM informacion WHERE documento = ?";

      // Variable para almacenar el resultado (usar array para modificar en lambda)
      final boolean[] existe = { false };

      conexion.seleccionar(sql,
            rs -> {
               try {
                  if (rs.next()) {
                     existe[0] = rs.getInt(1) > 0;
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            },
            ps -> {
               try {
                  ps.setString(1, documento);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            });

      // Mostrar mensaje si NO existe el documento
      if (!existe[0]) {
         JOptionPane.showMessageDialog(null,
               "El documento '" + documento + "' no está registrado en el sistema.",
               "Documento no encontrado",
               JOptionPane.WARNING_MESSAGE);
      }

      return existe[0];
   }
}

// ============================================
// 2. Datos.java - MÉTODOS AGREGADOS
// ============================================
/*
 * Agregar estos métodos a tu clase Datos.java existente:
 *
 * public String EnterPassword() {
 * String valorPasswordString = JOptionPane.showInputDialog(null,
 * " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
 * "Ingresa tu contraseña: ");
 * return valorPasswordString;
 * }
 *
 * public String EnterUser() {
 * String valorUserString = JOptionPane.showInputDialog(null,
 * " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
 * "Ingresa tu Usuario: ");
 * return valorUserString;
 * }
 */

// ============================================
// 3. Opcion.java - SIN CAMBIOS
// ============================================
/*
 * Tu archivo Opcion.java sigue funcionando igual:
 *
 * public void VistaInicioOpcion(Integer valor) {
 * if (valor == null)
 * return;
 *
 * switch (valor) {
 * case 1:
 * // Inicio de sesión como usuario
 * if (validacionUsuario.ValidacionUsuarioExistente()) {
 * // Si el login es exitoso, mostrar menú de usuario
 * VistaUsuarioOpcion(numero.solicitarEntero(ingreso.VistaUsuario(), 7));
 * }
 * break;
 * case 2:
 * // Inicio de sesión como administrador
 * if (validacionUsuario.ValidacionAdminExistente()) {
 * // Si el login es exitoso, mostrar menú de administrador
 * boolean continuarAdmin = true;
 * while (continuarAdmin) {
 * Integer opcionAdmin = numero.solicitarEntero(ingreso.VistaAdministrador(),
 * 5);
 * if (opcionAdmin == null)
 * continue;
 * if (opcionAdmin == 0) {
 * JOptionPane.showMessageDialog(null, "Cerrando sesión de administrador...");
 * continuarAdmin = false;
 * } else {
 * VistaAdministradorOpcion(opcionAdmin);
 * }
 * }
 * }
 * break;
 * case 0:
 * JOptionPane.showMessageDialog(null, "Regresando al menú principal...");
 * break;
 * }
 * }
 */
