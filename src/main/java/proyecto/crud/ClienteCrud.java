package proyecto.crud;

import java.sql.SQLException;
import java.time.LocalDate;
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
         try {
            int n = conexion.ejecutar("INSERT INTO usuario (correo, clave,nombre_usuario) VALUES (?, ?, ?)", ps1 -> {
               try {

                  entity.setCorreo(validar.ValidarEmail(insertar.Correo()));
                  entity.setContraseña(validar.ValidarClave(insertar.Password()));
                  entity.setUsuario(validar.ValidarUsuarioU(insertar.Usuario()));
                  if (entity.getCorreo() == null || entity.getContraseña() == null || entity.getUsuario() == null) {
                     throw new RuntimeException("Error de validación: uno de los datos ESTÁ MAL.");
                  }
                  ps1.setString(2, entity.getContraseña());
                  ps1.setString(1, entity.getCorreo());
                  ps1.setString(3, entity.getUsuario());
                  return;
               } catch (SQLException e) {
                  throw new RuntimeException(e);
               }
            });

         } catch (Exception e) {
            throw new RuntimeException(e);
         }

         // Solicitar y validar todos los datos
         entity.setNombre(validar.ValidarTexto(insertar.Nombre()));
         entity.setNombre2(validar.ValidarOpcional(insertar.Nombre2()));
         entity.setApellido(validar.ValidarTexto(insertar.Apellido()));
         entity.setApellido2(validar.ValidarOpcional(insertar.Apellido2()));
         entity.setDocumento(validar.ValidarDocumento(insertar.Cedula()));
         entity.setTelefono(validar.ValidarTelefonoU(insertar.Telefono())); // Corregido: usar ValidarTelefonoU
         
         // Validar y convertir la fecha de nacimiento UNA SOLA VEZ
         String fechaNacimientoStr = insertar.FechaNacimiento();
         LocalDate fechaNacimiento = validar.ValidarFechaNacimiento(fechaNacimientoStr);

         if (fechaNacimiento == null) {
            JOptionPane.showMessageDialog(null, "Fecha de nacimiento no válida. No se guardará el cliente.");
            return; // Si la fecha es inválida, no continuar con el guardado
         }
         
         entity.setFechaNacimiento(fechaNacimiento);

         // Configurar los parámetros del PreparedStatement
         ps.setString(1, entity.getNombre());
         ps.setString(2, entity.getNombre2());
         ps.setString(3, entity.getApellido());
         ps.setString(4, entity.getApellido2());
         ps.setString(5, entity.getDocumento());
         ps.setString(6, entity.getTelefono());
         ps.setObject(7, entity.getFechaNacimiento()); // LocalDate se maneja con setObject
         
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   });
   
   if (resultado > 0) {
      JOptionPane.showMessageDialog(null, "Guardó Usuario Correctamente");
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
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'buscar'");
   }

   @Override
   public int Elimnar(Cliente entity, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Elimnar'");
   }

   @Override
   public int Actualizar(Cliente entity, int id, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Actualizar'");
   }
}
