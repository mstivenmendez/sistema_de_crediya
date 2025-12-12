package proyecto.crud;

import java.util.List;

public interface CrudEntity<T> {

   public int Guardar(T entity, String dato );

   public int Elimnar(T entity, String dato,  String id);

   public int Actualizar(T entity, String id, String dato);

   public int BuscarPor(Object[] args);

   public void Buscar(String dato);

}
