package proyecto.prestamo;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import proyecto.crud.ClienteCrud;
import proyecto.crud.CrudEntity;
import proyecto.solicitud.Datos;
import proyecto.util.IngresoDatos;
import proyecto.validaciones.Validacion;
import proyecto.validaciones.ValidarNumero;

public class CrudPrestamo implements CrudEntity<Prestamo> {

   Datos insertar = new Datos();
   IngresoDatos conexion = new IngresoDatos();
   Validacion validar = new Validacion();
   ValidarNumero numero = new ValidarNumero();
   ClienteCrud buscar = new ClienteCrud();

   @Override
   public int Guardar(Prestamo entity, String sql) {
      int resultado = conexion.ejecutar(sql, ps -> {
         try {

            entity.setValor(numero.solicitarDouble(insertar.valorPrestamo(), 1000000000));
            entity.setInteres(numero.solicitarDouble(insertar.IdPrestamo(),100));
            entity.setCuotas(numero.solicitarEntero(insertar.valorCuotas(), 240));
            ps.setInt(3,entity.getCuotas());

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
   public int Elimnar(Prestamo entity, String dato, String id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Elimnar'");
   }

   @Override
   public int Actualizar(Prestamo entity, String id, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Actualizar'");
   }

   @Override
   public int BuscarPor(Object[] args) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'BuscarPor'");
   }

   @Override
   public void Buscar(String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Buscar'");
   }


}
