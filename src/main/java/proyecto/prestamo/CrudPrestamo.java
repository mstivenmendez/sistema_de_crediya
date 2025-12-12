package proyecto.prestamo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import proyecto.crud.ClienteCrud;
import proyecto.crud.CrudEntity;
import proyecto.personal.Cliente;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
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

   @Override
   public int Guardar(Prestamo entity, String sql) {
      int resultado = conexion.ejecutar(sql, ps -> {
         try {

            String cedula = validar.ValidarDocumento(insertar.Cedula());
            if (usuario.ValidarCedula(cedula)) {
               int id = usuario.validarCedulaYObtener(cedula);
               entity.setClienteUsuarioId(id);
               entity.setEmpleadoUsuarioId(2);
               entity.setValor(numero.solicitarDouble(insertar.valorPrestamo(), 1000000000));
               entity.setInteres(numero.solicitarDouble(insertar.IdPrestamo(), 100));
               entity.setCuotas(numero.solicitarEntero(insertar.valorCuotas(), 240));

               ps.setInt(1, entity.getClienteUsuarioId());
               ps.setInt(2, entity.getEmpleadoUsuarioId());
               ps.setDouble(3, entity.getValor());
               ps.setDouble(4, entity.getInteres());
               ps.setInt(5, entity.getCuotas());
            }

         } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un fallo en el registro");
            throw new RuntimeException(e);
         }
      });

      if (resultado > 0) {
         JOptionPane.showMessageDialog(null, "Guardó Usuario Correctamente");
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

   @Override
   public void Buscar(String filtro) {
      String sql = """
            SELECT DISTINCT *
            FROM vista_clientes
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
               sb.append("Prestamo # ").append(contador).append("\n");
               sb.append("Cédula: ").append(rs.getString("documento")).append("\n");
               sb.append("Nombre: ").append(rs.getString("primer_nombre")).append(" ").append(rs.getString("segundo_nombre")).append("\n");
               sb.append("apellido: ").append(rs.getString("primer_apellido")).append(" ").append(rs.getString("segundo_apellido")).append("\n");
               sb.append("Valor: ").append(rs.getString("valor")).append("\n");
               sb.append("Cuotas: ").append(rs.getString("cuotas")).append("\n");
               sb.append("Estado: ").append(rs.getString("estado")).append("\n");
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

   private void seleccionar(String sql,
         ResultSetConsumer rsConsumer,
         PreparedStatementConsumer psConsumer) throws SQLException {
      // Implementación del método que ejecuta la consulta
      // y llama a los consumers apropiados
   }

   @FunctionalInterface
   interface ResultSetConsumer {
      void accept(ResultSet rs) throws SQLException;
   }

   @FunctionalInterface
   interface PreparedStatementConsumer {
      void accept(PreparedStatement ps) throws SQLException;
   }

}
