package proyecto.pagos;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import proyecto.crud.ClienteCrud;
import proyecto.crud.CrudEntity;
import proyecto.personal.Cliente;
import proyecto.prestamo.CrudPrestamo;
import proyecto.prestamo.Prestamo;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidacionUsuario;
import proyecto.validaciones.ValidarNumero;

public class CrudPago implements CrudEntity<Pago> {

   IngresoDatos conexion = new IngresoDatos();
   Cliente cliente = new Cliente();
   Prestamo prestamo = new Prestamo();
   Datos insertar = new Datos();
   Validacion validar = new Validacion();
   ValidarNumero numero = new ValidarNumero();
   ClienteCrud buscar = new ClienteCrud();
   ValidacionUsuario usuario = new ValidacionUsuario();
   CrudPrestamo crudPrestamo = new CrudPrestamo();

   @Override
   public int Guardar(Pago entity, String sql) {
      try {
         // 1. Solicitar y validar cédula
         String cedula = validar.ValidarDocumento(insertar.Cedula());

         if (!usuario.ValidarCedula(cedula)) {
            JOptionPane.showMessageDialog(null,
                  "La cédula '" + cedula + "' no está registrada en el sistema.",
                  "Cédula no encontrada",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 2. Obtener ID del usuario por cédula
         Integer idUsuario = usuario.validarCedulaYObtener(cedula);
         if (idUsuario == null) {
            JOptionPane.showMessageDialog(null,
                  "No se pudo obtener el ID del usuario.",
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
            return 0;
         }

         // 3. Obtener ID del préstamo del usuario
         Integer idPrestamo = usuario.ObtenerIdPrestamo(idUsuario);
         if (idPrestamo == null) {
            JOptionPane.showMessageDialog(null,
                  "El usuario no tiene préstamos registrados.",
                  "Préstamo no encontrado",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 4. Validar que el préstamo exista en la base de datos
         if (!usuario.validarNumeroPrestamoExiste(idPrestamo)) {
            JOptionPane.showMessageDialog(null,
                  "El préstamo no existe o está inactivo.",
                  "Préstamo inválido",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 5. Solicitar el valor del pago
         double valorPago = numero.solicitarDouble(insertar.valorCuotas(), 1000000000);

         if (valorPago <= 0) {
            JOptionPane.showMessageDialog(null,
                  "El valor del pago debe ser mayor a 0.",
                  "Valor inválido",
                  JOptionPane.WARNING_MESSAGE);
            return 0;
         }

         // 6. Establecer el valor en la entidad
         entity.setValor(valorPago);

         // 7. Ejecutar INSERT (solo el valor)
         int resultado = conexion.ejecutar(sql, ps -> {
            try {
               ps.setDouble(1, entity.getValor());
            } catch (SQLException e) {
               JOptionPane.showMessageDialog(null,
                     "Error al configurar parámetros: " + e.getMessage(),
                     "Error",
                     JOptionPane.ERROR_MESSAGE);
               throw new RuntimeException(e);
            }
         });

         if (resultado > 0) {
            JOptionPane.showMessageDialog(null,
                  "✓ Pago registrado exitosamente.\n\n" +
                        "Valor: $" + String.format("%,.2f", entity.getValor()),
                  "Éxito",
                  JOptionPane.INFORMATION_MESSAGE);
         } else {
            JOptionPane.showMessageDialog(null,
                  "No se pudo registrar el pago.",
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
         }

         return resultado;

      } catch (Exception e) {
         JOptionPane.showMessageDialog(null,
               "Error durante el registro de pago: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int Elimnar(Pago entity, String dato, String id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Elimnar'");
   }

   @Override
   public int Actualizar(Pago entity, String id, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Actualizar'");
   }

   @Override
   public void Buscar(String dato) {
      String sql = """
            SELECT
               p.pago_id,
               p.valor,
               p.estado,
               p.fecha_pago,
               pr.prestamo_id,
               pr.valor AS valor_prestamo,
               i.documento,
               i.primer_nombre,
               i.segundo_nombre,
               i.primer_apellido,
               i.segundo_apellido
            FROM pago p
            INNER JOIN prestamo pr ON p.prestamo_id_fk = pr.prestamo_id
            INNER JOIN informacion i ON pr.usuario_id_fk = i.usuario_id_fk
            ORDER BY p.fecha_pago DESC
            """;

      try {
         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;
            double totalPagos = 0;

            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                    REPORTE DE TODOS LOS PAGOS                  ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               double valor = rs.getDouble("valor");
               totalPagos += valor;

               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Pago # ").append(contador).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Cédula Cliente        : ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre               : ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("Apellido             : ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("ID Pago              : ").append(rs.getInt("pago_id")).append("\n");
               sb.append("Número del Préstamo  : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("Valor Pagado         : $").append(String.format("%,.2f", valor)).append("\n");
               sb.append("Valor Préstamo       : $").append(String.format("%,.2f", rs.getDouble("valor_prestamo")))
                     .append("\n");
               sb.append("Estado del Pago      : ").append(rs.getString("estado")).append("\n");
               sb.append("Fecha de Pago        : ").append(rs.getDate("fecha_pago")).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No se encontraron pagos registrados.",
                     "Sin resultados",
                     JOptionPane.INFORMATION_MESSAGE);
            } else {
               sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
               sb.append("║  Total de pagos encontrados: ").append(contador).append("\n");
               sb.append("║  Total Pagado: $").append(String.format("%,.2f", totalPagos)).append("\n");
               sb.append("╚════════════════════════════════════════════════════════════════╝\n");

               // Generar archivo
               generarPlan(sb.toString());

               JOptionPane.showMessageDialog(null,
                     "✓ Se encontraron " + contador + " pagos.\n\n" +
                           "Total pagado: $" + String.format("%,.2f", totalPagos) + "\n" +
                           "Archivo generado: Plan de Pagos.txt",
                     "Búsqueda exitosa",
                     JOptionPane.INFORMATION_MESSAGE);
            }
         },
               ps -> {
                  // Sin parámetros, trae todos los pagos
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar pagos: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   public void BuscarActivos(String dato) {
      String sql = """
            SELECT
               p.pago_id,
               p.valor,
               p.estado,
               p.fecha_pago,
               pr.prestamo_id,
               pr.valor AS valor_prestamo,
               i.documento,
               i.primer_nombre,
               i.segundo_nombre,
               i.primer_apellido,
               i.segundo_apellido,
               (pr.valor - COALESCE(SUM(p.valor), 0)) AS valor_pendiente
            FROM pago p
            INNER JOIN prestamo pr ON p.prestamo_id_fk = pr.prestamo_id
            INNER JOIN informacion i ON pr.usuario_id_fk = i.usuario_id_fk
            WHERE p.estado = 'pendiente'
            GROUP BY p.pago_id, pr.prestamo_id, pr.valor, i.documento,
                     i.primer_nombre, i.segundo_nombre, i.primer_apellido,
                     i.segundo_apellido, p.valor, p.fecha_pago
            ORDER BY p.fecha_pago DESC
            """;

      try {
         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;
            double totalPendiente = 0;

            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║              REPORTE DE PAGOS PENDIENTES                       ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               double valor = rs.getDouble("valor");
               double valorPendiente = rs.getDouble("valor_pendiente");
               totalPendiente += valorPendiente;

               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Pago # ").append(contador).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n");
               sb.append("Cédula Cliente        : ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre               : ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("Apellido             : ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("ID Pago              : ").append(rs.getInt("pago_id")).append("\n");
               sb.append("Número del Préstamo  : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("Valor Pagado         : $").append(String.format("%,.2f", valor)).append("\n");
               sb.append("Valor Préstamo       : $").append(String.format("%,.2f", rs.getDouble("valor_prestamo")))
                     .append("\n");
               sb.append("Valor Pendiente      : $").append(String.format("%,.2f", valorPendiente)).append("\n");
               sb.append("Estado del Pago      : ").append(rs.getString("estado")).append("\n");
               sb.append("Fecha de Pago        : ").append(rs.getDate("fecha_pago")).append("\n");
               sb.append("───────────────────────────────────────────────────────────────\n\n");
            }

            if (!hayResultados) {
               JOptionPane.showMessageDialog(null,
                     "No se encontraron pagos pendientes.",
                     "Sin resultados",
                     JOptionPane.INFORMATION_MESSAGE);
            } else {
               sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
               sb.append("║  Total de pagos pendientes: ").append(contador).append("\n");
               sb.append("║  Total Pendiente: $").append(String.format("%,.2f", totalPendiente)).append("\n");
               sb.append("╚════════════════════════════════════════════════════════════════╝\n");

               // Generar archivo
               generarPlan(sb.toString());

               JOptionPane.showMessageDialog(null,
                     "✓ Se encontraron " + contador + " pagos pendientes.\n\n" +
                           "Total pendiente: $" + String.format("%,.2f", totalPendiente) + "\n" +
                           "Archivo generado: Plan de Pagos.txt",
                     "Búsqueda exitosa",
                     JOptionPane.INFORMATION_MESSAGE);
            }
         },
               ps -> {
                  // Sin parámetros, trae todos los pagos pendientes
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar pagos pendientes: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   public void GenerarCuotas(Prestamo entity, String prestamo) {

      conexion.ejecutarProcedimiento(
            "sp_plan_cuotas(?)",

            // Lambda 1: Procesar ResultSet
            rs -> {
               try {
                  StringBuilder sb = new StringBuilder();
                  sb.append("═══════════════════════════════════════════════════\n");
                  sb.append("         PLAN DE CUOTAS \n");
                  sb.append("═══════════════════════════════════════════════════\n\n");
                  double totalPendiente = 0;
                  while (rs.next()) {
                     double valorCuota = rs.getDouble("valor_cuota");
                     double cuotas = rs.getDouble("cuotas");
                     double valorPrestamo = rs.getDouble("valor");
                     double valortotal = rs.getDouble("total");
                     LocalDate fechaInicial = rs.getDate("fecha_inicio").toLocalDate();

                     LocalDate fechaCuota = fechaInicial;

                     sb.append("╔════════════════════════════════════════════════╗\n");
                     sb.append("║                   PRÉSTAMO                     ║ \n");
                     sb.append("╠════════════════════════════════════════════════╣\n");
                     sb.append("  Numero Préstamo  :  ").append(rs.getInt("prestamo_id")).append("\n");
                     sb.append("  Nombre Cliente   :  ").append(rs.getString("primer_nombre")).append(" ")
                           .append(rs.getString("segundo_nombre")).append("\n");
                     sb.append("  Apellido Cliente :  ").append(rs.getString("primer_apellido")).append(" ")
                           .append(rs.getString("segundo_apellido")).append("\n");
                     sb.append("  Documento Cliente:  ").append(rs.getString("documento")).append("\n");
                     sb.append("  Nombre Asesor    :  ").append(rs.getString("primer_nombre")).append(" ")
                           .append(rs.getString("segundo_nombre")).append("\n");
                     sb.append("  Apellido Asesor         : ").append(rs.getString("primer_apellido")).append(" ")
                           .append(rs.getString("segundo_apellido")).append("\n");
                     sb.append("  Documento Empleado: ").append(rs.getString("documento")).append("\n");
                     sb.append("  Valor Préstamo   : $").append(String.format("%,.2f", valortotal)).append("\n");
                     sb.append("  Valor Total      : $").append(String.format("%,.2f", valorPrestamo)).append("\n");
                     sb.append("  Interés          :  ").append(rs.getDouble("interes")).append("%\n");
                     sb.append("  Cuotas           :  ").append(rs.getInt("cuotas")).append("\n");
                     sb.append("  Fecha Inicio     :  ").append(rs.getDate("fecha_inicio")).append("\n");
                     sb.append("  Fecha Límite     :  ").append(rs.getDate("fecha_limite")).append("\n");
                     sb.append("  Estado           :  ").append(rs.getString("estado")).append("\n");
                     sb.append("╚════════════════════════════════════════════════╝\n\n");
                     for (int i = 0; i <= cuotas; i++) {
                        fechaCuota = fechaCuota.plusMonths(1);
                        sb.append("═══════════════════════════════════════════════════\n");
                        sb.append("                       CUOTA #\n").append(i).append("\n");
                        sb.append("═══════════════════════════════════════════════════\n");
                        sb.append("  Fecha Pago : ").append(String.format("%,.2f", fechaCuota)).append("\n");
                        sb.append("  Valor Pagar         : $").append(String.format("%,.2f", valorCuota)).append("\n");
                        sb.append("  Saldo Pendiente              : $")
                              .append(String.format("%,.2f", totalPendiente - valorCuota)).append("\n");
                        sb.append("═══════════════════════════════════════════════════");
                     }
                  }
                  generarPlan(sb.toString());
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
                  cs.setString(1, prestamo);
               } catch (SQLException e) {
                  e.printStackTrace();
                  throw new RuntimeException("Error al configurar parámetros", e);
               }
            });
   }

   public void VerNumeroPrestamo(Prestamo entity, String cedula) {
      conexion.ejecutarProcedimiento("", rs -> {
         try {
            StringBuilder sb = new StringBuilder();
            int contador = 0;
            while (rs.next()) {
               contador++;

               sb.append("╔════════════════════════════════════════════════╗\n");
               sb.append("║  PRÉSTAMO #").append(contador).append("\n");
               sb.append("╠════════════════════════════════════════════════╣\n");
               sb.append("  ID Préstamo      : ").append(rs.getInt("prestamo_id")).append("\n");
               sb.append("  Valor Préstamo   : $").append(String.format("%,.2f", rs.getDouble("valor"))).append("\n");
               sb.append("  Valor Total      : $").append(String.format("%,.2f", rs.getDouble("valor_total")))
                     .append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());

         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR");
         }
      }, cs -> {
         try {
            cs.setString(1, cedula);
         } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al configurar parámetros", e);
         }
      });
   }

   public void generarPlan(String contenido) {
      try {
         String nombreArchivo = "Plan de Pagos.txt";

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
