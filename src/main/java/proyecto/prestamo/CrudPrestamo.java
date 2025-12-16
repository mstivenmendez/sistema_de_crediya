package proyecto.prestamo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import proyecto.crud.ClienteCrud;
import proyecto.crud.CrudEntity;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.util.SesionUsuario;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidacionUsuario;
import proyecto.validaciones.ValidarNumero;

public class CrudPrestamo implements CrudEntity<Prestamo> {

   Datos insertar = new Datos();
   IngresoDatos conexion = new IngresoDatos();
   Validacion validar = new Validacion();
   ValidarNumero numero = new ValidarNumero();
   ClienteCrud buscar = new ClienteCrud();
   ValidacionUsuario usuario = new ValidacionUsuario();

   public void buscarMisPrestamos() {
      int usuarioId = SesionUsuario.getUsuarioId();
      buscarPrestamosPorUsuarioId(usuarioId);
   }

   /**
    * Busca préstamos de un cliente específico por su usuario_id
    * Permite que administradores vean los préstamos de cualquier cliente
    *
    * @param usuarioId ID del usuario/cliente
    */
   public void buscarPrestamosDeCliente(int usuarioId) {
      buscarPrestamosPorUsuarioId(usuarioId);
   }

   public void buscarEmpleado() {
      int idEmpleado = SesionUsuario.getUsuarioId();
      buscarPrestamosAprobadosPorEmpleado(idEmpleado);
   }

   @Override
   public int Guardar(Prestamo entity, String sql) {
      int resultado = conexion.ejecutar(sql, ps -> {
         try {

            String cedula = validar.ValidarDocumento(insertar.Cedula());
            if (usuario.ValidarCedula(cedula)) {
               int id = usuario.validarCedulaYObtener(cedula);
               entity.setClienteUsuarioId(id);
               ps.setInt(1, entity.getClienteUsuarioId());
               String cedulaEmpleado = validar.ValidarDocumento(insertar.CedulaEmpleado());
               if (usuario.ValidarCedula(cedulaEmpleado)) {
                  int idEmpleado = usuario.validarCedulaYObtener(cedulaEmpleado);
                  entity.setEmpleadoUsuarioId(idEmpleado);
                  entity.setValor(numero.solicitarDouble(insertar.valorPrestamo(), 1000000000));
                  entity.setCuotas(numero.solicitarEntero(insertar.valorCuotas(), 240));
                  entity.setInteres(numero.solicitarDouble(insertar.valorInteres(), 20)); // Interés fijo del 12.5%

                  ps.setInt(1, entity.getClienteUsuarioId());
                  ps.setInt(2, entity.getEmpleadoUsuarioId());
                  ps.setDouble(3, entity.getValor());
                  ps.setDouble(4, entity.getInteres());
                  ps.setInt(5, entity.getCuotas());
               }
            }

         } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un fallo en el registro");
            throw new RuntimeException(e);
         }
      });

      if (resultado > 0) {
         JOptionPane.showMessageDialog(null, "Guardó el  prestamo exitoso ");
      }
      return resultado;
   }

   @Override
   public int Elimnar(Prestamo entity, String sql, String id) {
      String valor = "DELETE FROM prestamo WHERE " + sql + " = ?";

      return conexion.ejecutar(valor, ps -> {
         try {
            ps.setString(1, id);
         } catch (SQLException e) {
            e.printStackTrace();
         }
      });
   }

   @Override
   public int Actualizar(Prestamo entity, String id, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Actualizar'");
   }

   public void buscarPrestamosPorDocumento(String documento) {

      // Primero obtener el usuario_id usando el documento
      int usuarioId = usuario.validarCedulaYObtener(documento);

      if (usuarioId <= 0) {
         JOptionPane.showMessageDialog(null,
               "No se encontró un cliente con el documento: " + documento,
               "Cliente no encontrado",
               JOptionPane.WARNING_MESSAGE);
         return;
      }

      // Llamar al procedimiento con el usuario_id obtenido
      buscarPrestamosPorUsuarioId(usuarioId);
   }

   /**
    * Busca préstamos por usuario_id usando el procedimiento almacenado
    * Método privado usado internamente
    *
    * @param usuarioId ID del usuario/cliente
    */
   private void buscarPrestamosPorUsuarioId(int usuarioId) {

      conexion.ejecutarProcedimiento(
            "sp_prestamos_por_cliente(?)",

            // Lambda 1: Procesar ResultSet
            rs -> {
               try {
                  StringBuilder sb = new StringBuilder();
                  sb.append("═══════════════════════════════════════════════════\n");
                  sb.append("              MIS PRÉSTAMOS\n");
                  sb.append("═══════════════════════════════════════════════════\n\n");

                  boolean hayResultados = false;
                  int contador = 0;
                  double totalValor = 0;
                  double totalPendiente = 0;

                  while (rs.next()) {
                     hayResultados = true;
                     contador++;

                     double valor = rs.getDouble("valor");
                     double valorTotal = rs.getDouble("valor_total");
                     double valorPendiente = rs.getDouble("valor_pendiente");

                     totalValor += valor;
                     totalPendiente += valorPendiente;

                     sb.append("╔════════════════════════════════════════════════╗\n");
                     sb.append("║  PRÉSTAMO #").append(contador).append("\n");
                     sb.append("╠════════════════════════════════════════════════╣\n");
                     sb.append("  ID Préstamo      : ").append(rs.getInt("prestamo_id")).append("\n");
                     sb.append("  Número Préstamo  : ").append(rs.getString("numero_prestamo")).append("\n");
                     sb.append("  Valor Préstamo   : $").append(String.format("%,.2f", valor)).append("\n");
                     sb.append("  Valor Total      : $").append(String.format("%,.2f", valorTotal)).append("\n");
                     sb.append("  Valor Pendiente  : $").append(String.format("%,.2f", valorPendiente)).append("\n");
                     sb.append("  Interés          : ").append(rs.getDouble("interes")).append("%\n");
                     sb.append("  Cuotas           : ").append(rs.getInt("cuotas")).append("\n");
                     sb.append("  Fecha Inicio     : ").append(rs.getDate("fecha_inicio")).append("\n");
                     sb.append("  Fecha Límite     : ").append(rs.getDate("fecha_limite")).append("\n");
                     sb.append("  Estado           : ").append(rs.getString("estado")).append("\n");
                     sb.append("╚════════════════════════════════════════════════╝\n\n");
                  }

                  if (!hayResultados) {
                     JOptionPane.showMessageDialog(null,
                           "Este cliente no tiene préstamos registrados.",
                           "Sin Préstamos",
                           JOptionPane.INFORMATION_MESSAGE);
                  } else {
                     // Resumen final
                     sb.append("═══════════════════════════════════════════════════\n");
                     sb.append("                     RESUMEN\n");
                     sb.append("═══════════════════════════════════════════════════\n");
                     sb.append("  Total de Préstamos    : ").append(contador).append("\n");
                     sb.append("  Suma Total Prestada   : $").append(String.format("%,.2f", totalValor)).append("\n");
                     sb.append("  Total Pendiente       : $").append(String.format("%,.2f", totalPendiente))
                           .append("\n");
                     sb.append("═══════════════════════════════════════════════════");

                     // Generar archivo .txt
                     generarArchivoMisPrestamos(sb.toString());
                     JOptionPane.showMessageDialog(null,
                           "Archivo generado exitosamente",
                           "Éxito",
                           JOptionPane.INFORMATION_MESSAGE);
                  }

               } catch (SQLException e) {
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(null,
                        "Error al procesar préstamos: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
               }
            },

            // Lambda 2: Configurar parámetros del CallableStatement
            cs -> {
               try {
                  cs.setInt(1, usuarioId);
               } catch (SQLException e) {
                  e.printStackTrace();
                  throw new RuntimeException("Error al configurar parámetros", e);
               }
            });
   }

   /**
    * Busca los préstamos que ha aprobado un empleado y genera un archivo .txt
    *
    * @param empleadoUsuarioId ID del empleado que aprobó los préstamos
    */
   public void buscarPrestamosAprobadosPorEmpleado(int empleadoUsuarioId) {

      conexion.ejecutarProcedimiento(
            "sp_prestamos_por_empleado(?)",

            // Lambda 1: Procesar ResultSet
            rs -> {
               try {
                  StringBuilder sb = new StringBuilder();
                  sb.append("═══════════════════════════════════════════════════\n");
                  sb.append("         PRÉSTAMOS APROBADOS POR EMPLEADO\n");
                  sb.append("═══════════════════════════════════════════════════\n\n");

                  boolean hayResultados = false;
                  int contador = 0;
                  double totalValor = 0;
                  double totalPendiente = 0;

                  while (rs.next()) {
                     hayResultados = true;
                     contador++;

                     double valor = rs.getDouble("valor");
                     double valorTotal = rs.getDouble("valor_total");
                     double valorPendiente = rs.getDouble("valor_pendiente");

                     totalValor += valor;
                     totalPendiente += valorPendiente;

                     sb.append("╔════════════════════════════════════════════════╗\n");
                     sb.append("║  PRÉSTAMO #").append(contador).append("\n");
                     sb.append("╠════════════════════════════════════════════════╣\n");
                     sb.append("  ID Préstamo      : ").append(rs.getInt("prestamo_id")).append("\n");
                     sb.append("  Número Préstamo  : ").append(rs.getString("numero_prestamo")).append("\n");
                     sb.append("  Nombre           : ").append(rs.getString("primer_nombre")).append(" ")
                           .append(rs.getString("segundo_nombre")).append("\n");
                     sb.append("  apellido         : ").append(rs.getString("primer_apellido")).append(" ")
                           .append(rs.getString("segundo_apellido")).append("\n");
                     sb.append("  Documento Cliente: ").append(rs.getString("cliente_documento")).append("\n");
                     sb.append("  Valor Préstamo   : $").append(String.format("%,.2f", valor)).append("\n");
                     sb.append("  Valor Total      : $").append(String.format("%,.2f", valorTotal)).append("\n");
                     sb.append("  Valor Pendiente  : $").append(String.format("%,.2f", valorPendiente)).append("\n");
                     sb.append("  Interés          : ").append(rs.getDouble("interes")).append("%\n");
                     sb.append("  Cuotas           : ").append(rs.getInt("cuotas")).append("\n");
                     sb.append("  Fecha Inicio     : ").append(rs.getDate("fecha_inicio")).append("\n");
                     sb.append("  Fecha Límite     : ").append(rs.getDate("fecha_limite")).append("\n");
                     sb.append("  Estado           : ").append(rs.getString("estado")).append("\n");
                     sb.append("╚════════════════════════════════════════════════╝\n\n");
                  }

                  if (!hayResultados) {
                     JOptionPane.showMessageDialog(null,
                           "Este empleado no tiene préstamos aprobados.",
                           "Sin Préstamos",
                           JOptionPane.INFORMATION_MESSAGE);
                  } else {
                     // Resumen final
                     sb.append("═══════════════════════════════════════════════════\n");
                     sb.append("                     RESUMEN\n");
                     sb.append("═══════════════════════════════════════════════════\n");
                     sb.append("  Total de Préstamos Aprobados : ").append(contador).append("\n");
                     sb.append("  Suma Total Prestada          : $").append(String.format("%,.2f", totalValor))
                           .append("\n");
                     sb.append("  Total Pendiente              : $").append(String.format("%,.2f", totalPendiente))
                           .append("\n");
                     sb.append("═══════════════════════════════════════════════════");

                     // Generar archivo .txt
                     generarArchivoPrestamosAprobados(sb.toString());
                     JOptionPane.showMessageDialog(null,
                           "Archivo generado exitosamente",
                           "Éxito",
                           JOptionPane.INFORMATION_MESSAGE);
                  }

               } catch (SQLException e) {
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(null,
                        "Error al procesar préstamos: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
               }
            },

            // Lambda 2: Configurar parámetros del CallableStatement
            cs -> {
               try {
                  cs.setInt(1, empleadoUsuarioId);
               } catch (SQLException e) {
                  e.printStackTrace();
                  throw new RuntimeException("Error al configurar parámetros", e);
               }
            });
   }

   /**
    * Genera un archivo .txt con los préstamos del usuario
    */
   public void generarArchivoMisPrestamos(String contenido) {
      try {
         String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
         int usuarioId = SesionUsuario.getUsuarioId();
         String nombreArchivo = "MisPrestamos_Usuario" + usuarioId + "_" + timestamp + ".txt";

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

   /**
    * Genera un archivo .txt con los préstamos aprobados por un empleado
    */
   public void generarArchivoPrestamosAprobados(String contenido) {
      try {
         String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
         int empleadoId = SesionUsuario.getUsuarioId();
         String nombreArchivo = "PrestamosAprobados_Empleado" + empleadoId + "_" + timestamp + ".txt";

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

   @Override
   public void Buscar(String filtro) {
      String sql = """
            SELECT DISTINCT
               prestamo_id,
               numero_prestamo,
               valor,
               valor_total,
               valor_pendiente,
               interes,
               cuotas,
               estado,
               fecha_inicio,
               fecha_limite,
               documento,
               primer_nombre,
               segundo_nombre,
               primer_apellido,
               segundo_apellido,
               cliente_usuario_id_fk,
               empleado_usuario_id_fk
            FROM vista_prestamos
            WHERE cliente_usuario_id_fk = ?
               OR empleado_usuario_id_fk = ?
               OR estado = ?
               OR documento LIKE ?
               OR CONCAT(primer_nombre, ' ', segundo_nombre) LIKE ?
            ORDER BY fecha_inicio DESC
            """;

      try {
         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;
            double totalValor = 0;
            double totalPendiente = 0;

            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                  BÚSQUEDA DE PRÉSTAMOS                         ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               double valor = rs.getDouble("valor");
               double valorPendiente = rs.getDouble("valor_pendiente");
               totalValor += valor;
               totalPendiente += valorPendiente;

               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Préstamo # ").append(contador).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("ID Préstamo          : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("Número Préstamo      : ").append(rs.getString("numero_prestamo")).append("\n");
               sb.append("Cédula Cliente       : ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre               : ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("Apellido             : ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("Valor Préstamo       : $").append(String.format("%,.2f", valor)).append("\n");
               sb.append("Valor Total          : $").append(String.format("%,.2f", rs.getDouble("valor_total")))
                     .append("\n");
               sb.append("Valor Pendiente      : $").append(String.format("%,.2f", valorPendiente)).append("\n");
               sb.append("Interés              : ").append(rs.getDouble("interes")).append("%\n");
               sb.append("Cuotas               : ").append(rs.getInt("cuotas")).append("\n");
               sb.append("Estado               : ").append(rs.getString("estado")).append("\n");
               sb.append("Fecha Inicio         : ").append(rs.getDate("fecha_inicio")).append("\n");
               sb.append("Fecha Límite         : ").append(rs.getDate("fecha_limite")).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No se encontraron préstamos con los criterios de búsqueda.",
                     "Sin resultados",
                     JOptionPane.INFORMATION_MESSAGE);
            } else {
               sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
               sb.append("║  Total de préstamos encontrados: ").append(contador).append("\n");
               sb.append("║  Total Valor: $").append(String.format("%,.2f", totalValor)).append("\n");
               sb.append("║  Total Pendiente: $").append(String.format("%,.2f", totalPendiente)).append("\n");
               sb.append("╚════════════════════════════════════════════════════════════════╝\n");

               JOptionPane.showMessageDialog(null,
                     sb.toString(),
                     "Resultados de búsqueda",
                     JOptionPane.INFORMATION_MESSAGE);
            }
         },
               ps -> {
                  try {
                     Integer id = null;
                     try {
                        id = Integer.parseInt(filtro);
                     } catch (NumberFormatException e) {
                        // Si no es número, id quedará null
                     }

                     ps.setInt(1, id != null ? id : -1);
                     ps.setInt(2, id != null ? id : -1);
                     ps.setString(3, filtro);
                     ps.setString(4, "%" + filtro + "%");
                     ps.setString(5, "%" + filtro + "%");
                  } catch (SQLException e) {
                     e.printStackTrace();
                  }
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar préstamos: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

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
