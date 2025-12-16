package proyecto.reportes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import proyecto.crud.ClienteCrud;
import proyecto.notificacion.Notificacion;
import proyecto.personal.Cliente;
import proyecto.prestamo.Prestamo;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidacionUsuario;
import proyecto.validaciones.ValidarNumero;

public class Reportes {

   IngresoDatos conexion = new IngresoDatos();
   Cliente cliente = new Cliente();
   Prestamo prestamo = new Prestamo();
   Datos insertar = new Datos();
   Validacion validar = new Validacion();
   ValidarNumero numero = new ValidarNumero();
   ClienteCrud buscar = new ClienteCrud();
   ValidacionUsuario usuario = new ValidacionUsuario();
   Notificacion notificacion = new Notificacion();

   public void BuscarPrestamosReporte(String sql) {
      try {

         seleccionar(sql, rs -> {
            StringBuilder sb = new StringBuilder();
            boolean hayResultados = false;
            int contador = 0;

            // Procesar todos los resultados
            while (rs.next()) {
               hayResultados = true;
               contador++;
               sb.append("Prestamo # ").append(contador).append("\n");
               sb.append("Cédula: ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre: ").append(rs.getString("primer_nombre")).append(" ")
                     .append(rs.getString("segundo_nombre")).append("\n");
               sb.append("apellido: ").append(rs.getString("primer_apellido")).append(" ")
                     .append(rs.getString("segundo_apellido")).append("\n");
               sb.append("Numero Prestamo ").append(rs.getString("numero_prestamo")).append("\n");
               sb.append("Valor Total: ").append(rs.getString("valor_total")).append("\n");
               sb.append("Fecha pago ").append(rs.getString("fecha_pago")).append("\n");
               sb.append("Valor Pendiente: ").append(rs.getString("valor_pendiente")).append("\n");
               sb.append("Estado: ").append(rs.getString("estado")).append("\n");
               sb.append("Cuotas Faltantes ").append(rs.getString("cuotas_pendientes")).append("\n");
               sb.append("Cuotas Ha Pagar").append(rs.getString("cuotas")).append("\n");
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

               });

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null,
               "Error al buscar clientes: " + e.getMessage());
         e.printStackTrace();
      }
   }

   public int notificacionPersonalizada(Notificacion entity, String sql) {
      try {
         // 1) Destinatario
         String cedulaDest = validar.ValidarDocumento(insertar.Cedula());
         if (!usuario.ValidarCedula(cedulaDest)) {
            JOptionPane.showMessageDialog(null, "La cédula destinatario '" + cedulaDest + "' no está registrada.",
                  "Cédula no encontrada", JOptionPane.WARNING_MESSAGE);
            return 0;
         }
         int idDest = usuario.validarCedulaYObtener(cedulaDest);
         entity.setFk_usuario(idDest);

         // 2) Remitente (empleado)
         String cedulaEmpl = validar.ValidarDocumento(insertar.Cedula());
         if (!usuario.ValidarCedula(cedulaEmpl)) {
            JOptionPane.showMessageDialog(null, "La cédula remitente '" + cedulaEmpl + "' no está registrada.",
                  "Cédula no encontrada", JOptionPane.WARNING_MESSAGE);
            return 0;
         }
         int idEmpl = usuario.validarCedulaYObtener(cedulaEmpl);
         entity.setFk_empleado(idEmpl);

         // 3) Asunto y mensaje
         entity.setNombre(insertar.Motivo());
         entity.setMensaje(insertar.Mensaje());

         // 4) Ejecutar insert con parámetros ya validados
         int resultado = conexion.ejecutar(sql, ps -> {
            try {
               ps.setInt(1, entity.getFk_usuario());
               ps.setInt(2, entity.getFk_empleado());
               ps.setString(3, entity.getNombre());
               ps.setString(4, entity.getMensaje());
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }
         });

         if (resultado > 0) {
            JOptionPane.showMessageDialog(null, "La notificación fue enviada.");
         } else {
            JOptionPane.showMessageDialog(null, "No se pudo enviar la notificación.", "Error",
                  JOptionPane.ERROR_MESSAGE);
         }
         return resultado;

      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Ocurrió un fallo al procesar la notificación: " + e.getMessage(), "Error",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
         return 0;
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
