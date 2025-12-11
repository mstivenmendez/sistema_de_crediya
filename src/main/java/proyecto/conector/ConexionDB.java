package proyecto.conector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
   private static final String URL = "jdbc:mysql://localhost:3306/crediya_db";
   private static final String USER = "root";
   private static final String PASSWORD = "quiopro123";

   public static Connection conectar() throws SQLException {
      return DriverManager.getConnection(URL, USER, PASSWORD);
   }

}
