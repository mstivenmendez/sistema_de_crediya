package proyecto.crud;


public interface CrudEntity<T> {

   public int Guardar(T entity, String dato );

   public int Elimnar(T entity, String dato,  String id);

   public int Actualizar(T entity, String id, String dato);

   public void Buscar(String dato);

}
