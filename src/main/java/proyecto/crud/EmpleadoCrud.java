package proyecto.crud;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import proyecto.personal.Empleado;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;

public class EmpleadoCrud implements CrudEntity<Empleado> {

   Datos insertar = new Datos();
   IngresoDatos conexion = new IngresoDatos();
   Validacion validar = new Validacion();

   @Override
   public int BuscarPor(Object[] args) {
      return 1;
   }

   @Override
   public List<Empleado> Buscar() {
      return null;
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
               entity.setTelefono(validar.ValidarTelefonoU(insertar.Password()));
               ps.setString(index++, entity.getTelefono());
            }

            ps.setString(index, id);

         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR EN LA ACTUALIZACION");
         }

      });
   }

}
