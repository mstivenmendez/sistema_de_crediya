package proyecto.pagos;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;
import proyecto.crud.CrudEntity;
import proyecto.personal.Cliente;
import proyecto.prestamo.Prestamo;
import proyecto.util.IngresoDatos;

public class CrudPago implements CrudEntity<Pago> {

   IngresoDatos conexion = new IngresoDatos();
   Cliente cliente = new Cliente();
   Prestamo prestamo = new Prestamo();

   @Override
   public int Guardar(Pago entity, String sql) {
      return 1;
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
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Buscar'");
   }


}
