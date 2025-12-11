package proyecto.validaciones;

import javax.swing.JOptionPane;

public class ValidarNumero {

   public Integer solicitarEntero(String ingreso, int valorMaximo) {

      if (ingreso == null) {
         JOptionPane.showMessageDialog(null,
            "⚠️ Operación cancelada",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
         return null;
      }

      ingreso = ingreso.trim();

      if (ingreso.isEmpty()) {
         JOptionPane.showMessageDialog(null,
            "❌ ERROR: El campo no puede estar vacío.\nPor favor ingrese un número.",
            "Entrada Inválida",
            JOptionPane.ERROR_MESSAGE);
         return null;
      }

      try {
         int numero = Integer.parseInt(ingreso);

         if (numero < 0 || numero > valorMaximo) {
            JOptionPane.showMessageDialog(null,
               "❌ ERROR: Número fuera del rango permitido.\n" +
               "Debe ingresar un valor entre 0 y " + valorMaximo,
               "Rango Inválido",
               JOptionPane.ERROR_MESSAGE);
            return null;
         }

         return numero;

      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(null,
            "❌ ERROR: Entrada inválida.\n" +
            "Debe ingresar un número entero, no letras ni caracteres especiales.",
            "Formato Incorrecto",
            JOptionPane.ERROR_MESSAGE);
         return null;
      }
   }

   public Double solicitarDouble(String ingreso, double valorMaximo) {

      if (ingreso == null) {
         JOptionPane.showMessageDialog(null,
            "⚠️ Operación cancelada",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
         return null;
      }

      ingreso = ingreso.trim();

      if (ingreso.isEmpty()) {
         JOptionPane.showMessageDialog(null,
            "❌ ERROR: El campo no puede estar vacío.\nPor favor ingrese un número.",
            "Entrada Inválida",
            JOptionPane.ERROR_MESSAGE);
         return null;
      }

      try {
         double numero = Double.parseDouble(ingreso);

         if (numero < 0 || numero > valorMaximo) {
            JOptionPane.showMessageDialog(null,
               "❌ ERROR: Número fuera del rango permitido.\n" +
               "Debe ingresar un valor entre 0 y " + valorMaximo,
               "Rango Inválido",
               JOptionPane.ERROR_MESSAGE);
            return null;
         }

         return numero;

      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(null,
            "❌ ERROR: Entrada inválida.\n" +
            "Debe ingresar un número válido (puede usar decimales con punto o coma).",
            "Formato Incorrecto",
            JOptionPane.ERROR_MESSAGE);
         return null;
      }
   }
}
