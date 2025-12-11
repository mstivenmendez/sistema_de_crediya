package proyecto.crud;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import proyecto.personal.Cliente;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;

public class ClienteCrud implements CrudEntity<Cliente> {

   Datos insertar = new Datos();
   IngresoDatos conexion = new IngresoDatos();
   Validacion validar = new Validacion();

   @Override
   public int Guardar(Cliente entity, String sql) {

      int resultado = conexion.ejecutar(sql, ps -> {
         try {
            entity.setNombre(validar.ValidarTexto(insertar.Nombre()));
            entity.setNombre2(validar.ValidarTexto(insertar.Nombre2()));
            entity.setApellido(validar.ValidarTexto(insertar.Apellido()));
            entity.setApellido2(validar.ValidarTexto(insertar.Apellido2()));
            entity.setCorreo(insertar.Correo());
            entity.setDocumento(insertar.Telefono());
            ps.setString(1, entity.getNombre());
         } catch (SQLException e) {
            throw new RuntimeException(e);
         }
      });
      if (resultado > 0) {
         JOptionPane.showMessageDialog(null, " Guardo Usuario Correctamente");
      }
      return resultado;
   }

   @Override
   public int BuscarPor(Object[] args) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'buscarPor'");
   }

   @Override
   public List<Cliente> Buscar() {
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

}
