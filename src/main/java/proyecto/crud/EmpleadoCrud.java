package proyecto.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import proyecto.personal.Empleado;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidarNumero;

public class EmpleadoCrud implements CrudEntity<Empleado> {

   Datos insertar = new Datos();
   IngresoDatos conexion = new IngresoDatos();
   Validacion validar = new Validacion();
   ValidarNumero validarNumero = new ValidarNumero();


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
               sb.append("Empleados # ").append(contador).append("\n");
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
   public int Guardar(Empleado entity, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Guardar'");
   }

   @Override
   public int Elimnar(Empleado entity, String sql, String id) {
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
   public int Actualizar(Empleado entity, String id, String dato) {
      return conexion.ejecutar(dato, ps -> {
         try {
            int index = 1;

            if (dato.contains("primer_nombre = ?")) {
               entity.setNombre(validar.ValidarTexto(insertar.Nombre()));
               ps.setString(index++, entity.getNombre());
            }
            if (dato.contains("segundo_nombre = ?")) {
               entity.setNombre2(validar.ValidarTexto(insertar.Nombre2()));
               ps.setString(index++, entity.getNombre2());
            }
            if (dato.contains("primer_apellido = ?")) {
               entity.setApellido(validar.ValidarTexto(insertar.Apellido()));
               ps.setString(index++, entity.getApellido());
            }
            if (dato.contains("segundo_apellido = ?")) {
               entity.setApellido(validar.ValidarOpcional(insertar.Apellido()));
               ps.setString(index++, entity.getApellido2());
            }
            if (dato.contains("telefono = ?")) {
               entity.setTelefono(validar.ValidarTelefonoU(insertar.Telefono()));
               ps.setString(index++, entity.getTelefono());
            }
            if (dato.contains("salario = ?")) {
               entity.setSueldo(validarNumero.solicitarDouble(insertar.valorSalario(), 50000000));
               ps.setDouble(index++,entity.getSueldo());
            }

            ps.setString(index, id);

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
