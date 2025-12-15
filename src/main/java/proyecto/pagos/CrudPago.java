package proyecto.pagos;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import proyecto.crud.ClienteCrud;
import proyecto.crud.CrudEntity;
import proyecto.personal.Cliente;
import proyecto.prestamo.CrudPrestamo;
import proyecto.prestamo.Prestamo;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.util.SesionUsuario;
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
      int resultado = conexion.ejecutar(sql, ps -> {
         try {
            String cedula = validar.ValidarDocumento(insertar.Cedula());
            if (usuario.ValidarCedula(cedula)) {
               int id_usuario = usuario.validarCedulaYObtener(cedula);
               int id_prestamo = usuario.ObtenerIdPrestamo(id_usuario);
               entity.setPrestamoIdFk(id_prestamo);
               entity.setValor(numero.solicitarDouble(insertar.valorCuotas(), 1000000000));

               ps.setInt(1, entity.getPrestamoIdFk());
               ps.setDouble(2, entity.getValor());
            }

         } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un fallo en el registro");
            throw new RuntimeException(e);
         }
      });

      if (resultado > 0) {
         JOptionPane.showMessageDialog(null, "Guardó el pago ");
      }
      return resultado;
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
            SELECT prestamos
            FROM *
            WHERE cliente_id = ?
               OR cedula = ?
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
               sb.append("Pagos # ").append(contador).append("\n");
               sb.append("Cédula: ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre: ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("apellido: ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("Valor: ").append(rs.getString("valor")).append("\n");
               sb.append("Numero de Pago ").append(rs.getString("numero_pago")).append("\n");
               sb.append("Numero del Prestamo: ").append(rs.getString("numero_prestamo")).append("\n");
               sb.append("Fecha pago ").append(rs.getString("fecha_pago")).append("\n");
               sb.append("Valor Pendiente: ").append(rs.getString("valor_pendiente")).append("\n");
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
                     id = Integer.parseInt(dato);
                  } catch (NumberFormatException e) {
                     // Si no es número, id quedará null
                  }
                  ps.setInt(1, id != null ? id : -1);
                  ps.setString(2, dato);
                  ps.setString(3, dato);
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar clientes: " + e.getMessage());
         e.printStackTrace();
      }
   }

   public void BuscarActivos(String dato) {
      String sql = """
            SELECT prestamos
            FROM *
            WHERE cliente_id = ?
               OR cedula = ?
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
               sb.append("Pagos # ").append(contador).append("\n");
               sb.append("Cédula: ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre: ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("apellido: ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("Valor: ").append(rs.getString("valor")).append("\n");
               sb.append("Numero del Prestamo: ").append(rs.getString("numero_prestamo")).append("\n");
               sb.append("Fecha pago ").append(rs.getString("fecha_pago")).append("\n");
               sb.append("Valor Pendiente: ").append(rs.getString("valor_pendiente")).append("\n");
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
                     id = Integer.parseInt(dato);
                  } catch (NumberFormatException e) {
                     // Si no es número, id quedará null
                  }
                  ps.setInt(1, id != null ? id : -1);
                  ps.setString(2, dato);
                  ps.setString(3, dato);
               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar clientes: " + e.getMessage());
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
