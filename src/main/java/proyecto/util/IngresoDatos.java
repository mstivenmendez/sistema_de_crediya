package proyecto.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import proyecto.conector.ConexionDB;
import java.sql.Statement;

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

   public void seleccionar(String sql, Consumer<ResultSet> lector, Consumer<PreparedStatement> configuracion) {

      try (Connection con = ConexionDB.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

         configuracion.accept(ps);

         try (ResultSet rs = ps.executeQuery()) {
            lector.accept(rs);
         }

      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null,
               "Error en la consulta: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
      }
   }

   public int ejecutarYObtenerID(String sql, Consumer<PreparedStatement> configuracion) {
      try (Connection con = ConexionDB.conectar();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

         configuracion.accept(ps);
         int filasAfectadas = ps.executeUpdate();

         if (filasAfectadas > 0) {
            // Obtener el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
               if (rs.next()) {
                  return rs.getInt(1); // Retorna el ID generado
               }
            }
         }
         return 0;

      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null, "Error al obtener ID: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
         return 0;
      }
   }

}
