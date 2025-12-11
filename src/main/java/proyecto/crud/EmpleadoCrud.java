package proyecto.crud;

import java.util.List;

import proyecto.personal.Empleado;

public class EmpleadoCrud implements CrudEntity<Empleado>{

   @Override
   public int BuscarPor(Object[] args) {
      return 1;
   }

   @Override
   public List<Empleado> Buscar() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'buscar'");
   }

   @Override
   public int Guardar(Empleado entity, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Guardar'");
   }

   @Override
   public int Elimnar(Empleado entity, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Elimnar'");
   }

   @Override
   public int Actualizar(Empleado entity, int id, String dato) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'Actualizar'");
   }

}
