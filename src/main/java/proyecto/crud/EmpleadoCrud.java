package proyecto.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import proyecto.personal.Empleado;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidacionUsuario;
import proyecto.validaciones.ValidarNumero;

public class EmpleadoCrud implements CrudEntity<Empleado> {

   Datos insertar = new Datos();
   IngresoDatos conexion = new IngresoDatos();
   Validacion validar = new Validacion();
   ValidarNumero validarNumero = new ValidarNumero();
   ValidacionUsuario usuario = new ValidacionUsuario();

   @Override
   public void Buscar(String filtro) {

      String sql = """
            SELECT *
            FROM vista_empleados
            WHERE cliente_id = ?
               OR cedula = ?
               OR rol = ?
            """;

      try {
         seleccionar(sql, rs -> {

            StringBuilder sb = new StringBuilder();
            int contador = 0;

            while (rs.next()) {
               contador++;
               sb.append("Cliente # ").append(contador).append("\n");
               sb.append("Cédula: ").append(rs.getString("cedula")).append("\n");
               sb.append("Usuario: ").append(rs.getString("nombre_usuario")).append("\n");
               sb.append("Nombre: ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("Apellido: ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("Teléfono: ").append(rs.getString("telefono")).append("\n");
               sb.append("Correo: ").append(rs.getString("correo")).append("\n");
               sb.append("Estado: ").append(rs.getString("estado")).append("\n");
               sb.append("Salario: ").append(rs.getDouble("salario")).append("\n");
               sb.append("---------------------------\n");
            }

            if (contador == 0) {
               JOptionPane.showMessageDialog(null, "No se encontraron resultados.");
            } else {
               JOptionPane.showMessageDialog(null, sb.toString());
            }

         }, ps -> {

            Integer id = null;
            try {
               id = Integer.parseInt(filtro);
            } catch (NumberFormatException e) {
            }

            if (id != null) {
               ps.setInt(1, id);
            } else {
               ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setString(2, filtro);
            ps.setString(3, filtro.toLowerCase());
         });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(
               null, "Error al buscar clientes: " + e.getMessage());
      }
   }

   @Override
   public int Guardar(Empleado entity, String dato) {

      try {
         // PASO 1: Validar y obtener datos del usuario
         entity.setCorreo(validar.ValidarEmail(insertar.Correo()));
         entity.setContraseña(validar.ValidarClave(insertar.Password()));
         entity.setUsuario(validar.ValidarUsuarioU(insertar.Usuario()));
         entity.setCargo("empleado");

         if (entity.getCorreo() == null || entity.getContraseña() == null || entity.getUsuario() == null) {
            JOptionPane.showMessageDialog(null, "Error de validación: datos de empleado inválidos.");
            return 0;
         }

         // PASO 2: Insertar en tabla usuario y obtener el ID generado
         final int[] usuarioId = { 0 };

         usuarioId[0] = conexion.ejecutarYObtenerID(
               "INSERT INTO usuario (correo, clave, nombre_usuario, rol) VALUES (?, ?, ?, ?)",
               ps -> {
                  try {
                     ps.setString(1, entity.getCorreo());
                     ps.setString(2, entity.getContraseña());
                     ps.setString(3, entity.getUsuario());
                     ps.setString(4, entity.getCargo());
                  } catch (SQLException e) {
                     throw new RuntimeException("Error al configurar usuario: " + e.getMessage(), e);
                  }
               });

         // Verificar que se insertó correctamente
         if (usuarioId[0] == 0) {
            JOptionPane.showMessageDialog(null,
                  "Error: No se pudo crear el un empleado en el sistema.",
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
         entity.setSueldo(validarNumero.solicitarDouble(insertar.valorSalario(), 1000000000)); // Validar y convertir la
                                                                                               // fecha de nacimiento
         String fechaNacimientoStr = insertar.FechaNacimiento();
         LocalDate fechaNacimiento = validar.ValidarFechaNacimiento(fechaNacimientoStr);

         if (fechaNacimiento == null) {
            JOptionPane.showMessageDialog(null,
                  "Fecha de nacimiento no válida. No se guardará el cliente.");
            return 0;
         }

         entity.setFechaNacimiento(fechaNacimiento);

         // PASO 4: Insertar en tabla informacion con el usuario_id_fk
         int resultado = conexion.ejecutar(dato, ps -> {
            try {
               ps.setString(1, entity.getNombre());
               ps.setString(2, entity.getNombre2());
               ps.setString(3, entity.getApellido());
               ps.setString(4, entity.getApellido2());
               ps.setString(5, entity.getDocumento());
               ps.setString(6, entity.getTelefono());
               ps.setDouble(8, entity.getSueldo());
               ps.setObject(9, entity.getFechaNacimiento());
               ps.setInt(10, usuarioId[0]); // ← AQUÍ SE USA EL ID DEL USUARIO
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
   public int Elimnar(Empleado entity, String sql, String id) {
      // Primero obtener el usuario_id y validar que sea empleado
      final Integer[] usuarioId = { null };
      final String[] rol = { null };

      String sqlValidar = "SELECT u.usuario_id, u.rol FROM usuario u " +
            "INNER JOIN informacion i ON u.usuario_id = i.usuario_id_fk " +
            "WHERE i.documento = ?";

      conexion.seleccionar(sqlValidar,
            rs -> {
               try {
                  if (rs.next()) {
                     usuarioId[0] = rs.getInt("usuario_id");
                     rol[0] = rs.getString("rol");
                  }
               } catch (SQLException e) {
                  e.printStackTrace();
               }
            },
            ps -> {
               try {
                  ps.setString(1, id);
               } catch (SQLException e) {
                  e.printStackTrace();
               }
            });

      if (usuarioId[0] == null) {
         JOptionPane.showMessageDialog(null,
               "Empleado no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
         return 0;
      }

      // Validar que el rol sea 'empleado'
      if (rol[0] == null || !rol[0].equalsIgnoreCase("empleado")) {
         JOptionPane.showMessageDialog(null,
               "Solo se pueden eliminar usuarios con rol 'empleado'.", "Error", JOptionPane.ERROR_MESSAGE);
         return 0;
      }

      // Eliminar de informacion primero (clave foránea)
      String sqlInfo = "DELETE FROM informacion WHERE usuario_id_fk = ?";
      conexion.ejecutar(sqlInfo, ps -> {
         try {
            ps.setInt(1, usuarioId[0]);
         } catch (SQLException e) {
            throw new RuntimeException(e);
         }
      });

      // Luego eliminar de usuario
      String sqlUser = "DELETE FROM usuario WHERE usuario_id = ?";
      int resultado = conexion.ejecutar(sqlUser, ps -> {
         try {
            ps.setInt(1, usuarioId[0]);
         } catch (SQLException e) {
            throw new RuntimeException(e);
         }
      });

      if (resultado > 0) {
         JOptionPane.showMessageDialog(null,
               "✅ Empleado eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(null,
               "❌ Error al eliminar empleado.", "Error", JOptionPane.ERROR_MESSAGE);
      }

      return resultado;
   }

   @Override
public int Actualizar(Empleado entity, String id, String campo) {

   String sql = """
         UPDATE informacion
         SET %s
         WHERE documento = ?
         """.formatted(campo);

   return conexion.ejecutar(sql, ps -> {
      try {
         int index = 1;

         if (campo.contains("primer_nombre = ?")) {
            entity.setNombre(validar.ValidarTexto(insertar.Nombre()));
            ps.setString(index++, entity.getNombre());
         } else if (campo.contains("segundo_nombre = ?")) {
            entity.setNombre2(validar.ValidarTexto(insertar.Nombre2()));
            ps.setString(index++, entity.getNombre2());
         } else if (campo.contains("primer_apellido = ?")) {
            entity.setApellido(validar.ValidarTexto(insertar.Apellido()));
            ps.setString(index++, entity.getApellido());
         } else if (campo.contains("segundo_apellido = ?")) {
            entity.setApellido2(validar.ValidarOpcional(insertar.Apellido2()));
            ps.setString(index++, entity.getApellido2());
         } else if (campo.contains("telefono = ?")) {
            entity.setTelefono(validar.ValidarTelefonoU(insertar.Telefono()));
            ps.setString(index++, entity.getTelefono());
         } else if (campo.contains("salario = ?")) {
            entity.setSueldo(validarNumero.solicitarDouble(insertar.valorSalario(), 50000000));
            ps.setDouble(index++, entity.getSueldo());
         }

         // ✅ FALTABA: Establecer el parámetro del WHERE (documento)
         ps.setString(index, id);

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al configurar parámetros: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         throw new RuntimeException(e);
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

   public void generarReporteEmpleados() {
      String sql = "SELECT * FROM vista_empleados WHERE rol = 'empleado' ORDER BY usuario_id";

      conexion.seleccionar(sql,
            rs -> {
               try {
                  StringBuilder sb = new StringBuilder();
                  sb.append("═══════════════════════════════════════════════════\n");
                  sb.append("           REPORTE DE EMPLEADOS\n");
                  sb.append("═══════════════════════════════════════════════════\n\n");

                  boolean hayResultados = false;
                  int contador = 0;

                  while (rs.next()) {
                     hayResultados = true;
                     contador++;

                     sb.append("╔════════════════════════════════════════════════╗\n");
                     sb.append("║  EMPLEADO #").append(contador).append("\n");
                     sb.append("╠════════════════════════════════════════════════╣\n");
                     sb.append("  ID Usuario      : ").append(rs.getInt("usuario_id")).append("\n");
                     sb.append("  Cédula          : ").append(rs.getString("documento")).append("\n");
                     sb.append("  Usuario         : ").append(rs.getString("nombre_usuario")).append("\n");
                     sb.append("  Nombre          : ").append(rs.getString("primer_nombre")).append(" ")
                           .append(rs.getString("segundo_nombre")).append("\n");
                     sb.append("  Apellido        : ").append(rs.getString("primer_apellido")).append(" ")
                           .append(rs.getString("segundo_apellido")).append("\n");
                     sb.append("  Teléfono        : ").append(rs.getString("telefono")).append("\n");
                     sb.append("  Correo          : ").append(rs.getString("correo")).append("\n");
                     sb.append("  Estado          : ").append(rs.getString("estado")).append("\n");
                     sb.append("  Salario         : $").append(String.format("%,.2f", rs.getDouble("salario"))).append("\n");
                     sb.append("╚════════════════════════════════════════════════╝\n\n");
                  }

                  if (!hayResultados) {
                     JOptionPane.showMessageDialog(null,
                           "No hay empleados registrados.",
                           "Sin Resultados",
                           JOptionPane.INFORMATION_MESSAGE);
                  } else {
                     // Resumen
                     sb.append("═══════════════════════════════════════════════════\n");
                     sb.append("Total de Empleados: ").append(contador).append("\n");
                     sb.append("═══════════════════════════════════════════════════");

                     // Generar archivo .txt
                     generarArchivoEmpleados(sb.toString());
                     JOptionPane.showMessageDialog(null,
                           "Reporte generado exitosamente",
                           "Éxito",
                           JOptionPane.INFORMATION_MESSAGE);
                  }
               } catch (SQLException e) {
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(null,
                        "Error al procesar empleados: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
               }
            },
            ps -> {
               // Sin parámetros
            });
   }

   /**
    * Genera un archivo .txt con el reporte de empleados
    */
   private void generarArchivoEmpleados(String contenido) {
      try {
         String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
         String nombreArchivo = "ReporteEmpleados_" + timestamp + ".txt";

         try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write(contenido);
         }

         System.out.println("Archivo generado: " + nombreArchivo);
      } catch (IOException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null,
               "Error al generar el archivo: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
      }
   }

}
