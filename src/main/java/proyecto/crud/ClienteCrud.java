package proyecto.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import proyecto.personal.Cliente;
import proyecto.personal.Empleado;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;

public class ClienteCrud implements CrudEntity<Cliente> {

   Datos insertar = new Datos();
   IngresoDatos conexion = new IngresoDatos();
   Validacion validar = new Validacion();

   @Override
   public int Guardar(Cliente entity, String sql) {
      
      try {
         // PASO 1: Validar y obtener datos del usuario
         entity.setCorreo(validar.ValidarEmail(insertar.Correo()));
         entity.setContraseña(validar.ValidarClave(insertar.Password()));
         entity.setUsuario(validar.ValidarUsuarioU(insertar.Usuario()));

         if (entity.getCorreo() == null || entity.getContraseña() == null || entity.getUsuario() == null) {
            JOptionPane.showMessageDialog(null, "Error de validación: datos de usuario inválidos.");
            return 0;
         }

         // PASO 2: Insertar en tabla usuario y obtener el ID generado
         final int[] usuarioId = {0};
         
         usuarioId[0] = conexion.ejecutarYObtenerID(
            "INSERT INTO usuario (correo, clave, nombre_usuario) VALUES (?, ?, ?)", 
            ps -> {
               try {
                  ps.setString(1, entity.getCorreo());
                  ps.setString(2, entity.getContraseña());
                  ps.setString(3, entity.getUsuario());
               } catch (SQLException e) {
                  throw new RuntimeException("Error al configurar usuario: " + e.getMessage(), e);
               }
            }
         );

         // Verificar que se insertó correctamente
         if (usuarioId[0] == 0) {
            JOptionPane.showMessageDialog(null, 
               "Error: No se pudo crear el usuario en el sistema.",
               "Error", 
               JOptionPane.ERROR_MESSAGE);
            return 0;
         }

         // PASO 3: Solicitar y validar datos personales
         entity.setNombre(validar.ValidarTexto(insertar.Nombre()));
         entity.setNombre2(validar.ValidarOpcional(insertar.Nombre2()));
         entity.setApellido(validar.ValidarTexto(insertar.Apellido()));
         entity.setApellido2(validar.ValidarOpcional(insertar.Apellido2()));
         entity.setDocumento(validar.ValidarDocumento(insertar.Cedula()));
         entity.setTelefono(validar.ValidarTelefonoU(insertar.Telefono()));

         // Validar y convertir la fecha de nacimiento
         String fechaNacimientoStr = insertar.FechaNacimiento();
         LocalDate fechaNacimiento = validar.ValidarFechaNacimiento(fechaNacimientoStr);

         if (fechaNacimiento == null) {
            JOptionPane.showMessageDialog(null, 
               "Fecha de nacimiento no válida. No se guardará el cliente.");
            return 0;
         }

         entity.setFechaNacimiento(fechaNacimiento);

         // PASO 4: Insertar en tabla informacion con el usuario_id_fk
         int resultado = conexion.ejecutar(sql, ps -> {
            try {
               ps.setString(1, entity.getNombre());
               ps.setString(2, entity.getNombre2());
               ps.setString(3, entity.getApellido());
               ps.setString(4, entity.getApellido2());
               ps.setString(5, entity.getDocumento());
               ps.setString(6, entity.getTelefono());
               ps.setObject(7, entity.getFechaNacimiento());
               ps.setInt(8, usuarioId[0]); // ← AQUÍ SE USA EL ID DEL USUARIO
            } catch (SQLException e) {
               throw new RuntimeException("Error al guardar información personal: " + e.getMessage(), e);
            }
         });

         if (resultado > 0) {
            JOptionPane.showMessageDialog(null, 
               "✅ Usuario registrado correctamente\n" +
               "ID de Usuario: " + usuarioId[0] + "\n" +
               "Usuario: " + entity.getUsuario(),
               "Registro Exitoso",
               JOptionPane.INFORMATION_MESSAGE);
         }

         return resultado;

      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null, 
            "Error al guardar el cliente: " + e.getMessage(),
            "Error", 
            JOptionPane.ERROR_MESSAGE);
         return 0;
      }
   }
   @Override
   public void Buscar(String filtro) {
      String sql = """
            SELECT DISTINCT *
            FROM vista_clientes
            WHERE usuario_id = ?
               OR documento = ?
               OR rol = ?
            """;

      try {

         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               sb.append("Usuario # ").append(contador).append("\n");
               sb.append("Cédula: ").append(rs.getString("documento")).append("\n");
               sb.append("Usuario: ").append(rs.getString("nombre_usuario")).append("\n");
               sb.append("Nombre: ").append(rs.getString("primer_nombre")).append(" ").append(rs.getString("segundo_nombre")).append("\n");
               sb.append("apellido: ").append(rs.getString("primer_apellido")).append(" ").append(rs.getString("segundo_apellido")).append("\n");
               sb.append("Teléfono: ").append(rs.getString("telefono")).append("\n");
               sb.append("Correo: ").append(rs.getString("correo")).append("\n");
               sb.append("Estado: ").append(rs.getString("estado")).append("\n");
               sb.append("---------------------------\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No se encontraron resultados.");
            } else {
               JOptionPane.showMessageDialog(null, sb.toString());
            }
         },
               ps -> {
                  Integer id = null;
                  try {
                     id = Integer.parseInt(filtro);
                  } catch (NumberFormatException e) {
                     // Si no es número, id quedará null
                  }
                  ps.setInt(1, id != null ? id : -1);
                  ps.setString(2, filtro);
                  ps.setString(3, filtro);
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar clientes: " + e.getMessage());
         e.printStackTrace();
      }
   }


   @Override
   public int Elimnar(Cliente entity, String sql, String id) {
      String valor = "DELETE FROM cliente WHERE " + sql + " = ?";

      return conexion.ejecutar(valor, ps -> {
         try {
            ps.setString(1, id);
         } catch (SQLException e) {
            e.printStackTrace();
         }
      });
   }

   @Override
   public int Actualizar(Cliente entity, String id, String dato) {
      return conexion.ejecutar(dato, ps -> {
         try {
            int index = 1;

            if (dato.contains("primer_nombre = ?")) {
               entity.setNombre(validar.ValidarTexto(insertar.Nombre()));
               ps.setString(index++, entity.getNombre());
            }
            if (dato.contains("segundo_nombre = ?")) {
               entity.setNombre2(validar.ValidarOpcional(insertar.Nombre2()));
               ps.setString(index++, entity.getNombre2());
            }
            if (dato.contains("primer_apellido = ?")) {
               entity.setApellido(validar.ValidarTexto(insertar.Apellido()));
               ps.setString(index++, entity.getApellido());
            }
            if (dato.contains("segundo_apellido = ?")) {
               entity.setApellido2(validar.ValidarOpcional(insertar.Apellido2()));
               ps.setString(index++, entity.getApellido2());
            }
            if (dato.contains("telefono = ?")) {
               entity.setTelefono(validar.ValidarTelefonoU(insertar.Telefono()));
               ps.setString(index++, entity.getTelefono());
            }

            // El ID va como último parámetro del WHERE
            ps.setString(index, id);

         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR EN LA ACTUALIZACION");
         }
      });
   }

   public int ActualizarUsuario(Cliente entity, String id, String dato) {
      return conexion.ejecutar(dato, ps -> {
         try {
            int index = 1;

            if (dato.contains("correo = ?")) {
               entity.setCorreo(validar.ValidarEmail(insertar.Correo()));
               ps.setString(index++, entity.getCorreo());
            }
            if (dato.contains("clave = ?")) {
               entity.setContraseña(validar.ValidarClave(insertar.Password()));
               ps.setString(index++, entity.getContraseña());
            }
            if (dato.contains("nombre_usuario = ?")) {
               entity.setUsuario(validar.ValidarUsuarioU(insertar.Usuario()));
               ps.setString(index++, entity.getUsuario());
            }
            try {
               ps.setString(index, dato);
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }

         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR EN LA ACTUALIZACION");
         }

      });
   }

   // Método auxiliar que debe estar implementado en la clase
   private void seleccionar(String sql,
         ResultSetConsumer rsConsumer,
         PreparedStatementConsumer psConsumer) throws SQLException {
      // Implementación del método que ejecuta la consulta
      // y llama a los consumers apropiados
   }

   // Interfaces funcionales para los lambdas
   @FunctionalInterface
   interface ResultSetConsumer {
      void accept(ResultSet rs) throws SQLException;
   }

   @FunctionalInterface
   interface PreparedStatementConsumer {
      void accept(PreparedStatement ps) throws SQLException;
   }

}
