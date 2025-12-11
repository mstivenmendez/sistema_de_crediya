package proyecto;

import java.sql.SQLException;

import proyecto.conector.ConexionDB;
import proyecto.crud.ClienteCrud;
import proyecto.personal.Cliente;
import proyecto.ui.Inicio;
import proyecto.util.IngresoDatos;


public class Main {
   public static void main(String[] args) throws SQLException {
      // ConexionDB conexion = new ConexionDB();
      // IngresoDatos ingreso = new IngresoDatos();
      // try {
      //    conexion.conectar();
      //    System.out.println("Conexión exitosa a la base de datos.");
      //    ingreso.ejecutar("INSERT INTO usuario (correo, clave) VALUES (?, ?)",ps->{
      //       try {
      //          ps.setString(1, "ejemplito@gmail.com");
      //       } catch (SQLException e) {
      //          // TODO Auto-generated catch block
      //          e.printStackTrace();
      //       }
      //       try {
      //          ps.setString(2, "ClaveSegura123");
      //       } catch (SQLException e) {
      //          // TODO Auto-generated catch block
      //          e.printStackTrace();
      //       }});
      // } catch (SQLException e) {
      //    System.err.println("Error al conectar a la base de datos: " + e.getMessage());
      //    return; // Salir si no se puede conectar a la base de datos
      // }
      Inicio comenzar = new Inicio();

      comenzar.Iniciar();

   }
}
