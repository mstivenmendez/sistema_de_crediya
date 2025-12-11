package proyecto.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import proyecto.conector.ConexionDB;

public class IngresoDatos {

   public int ejecutar(String sql, Consumer<PreparedStatement> configuracion) {
      try (Connection con = ConexionDB.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

         configuracion.accept(ps);

         return ps.executeUpdate();

      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null, "Error en la operación: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
         return 0;
      }
   }

}
