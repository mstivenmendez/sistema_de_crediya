package proyecto.ui;

import javax.swing.JOptionPane;
import proyecto.validaciones.ValidarNumero;

public class Inicio {
   ValidarNumero ingreso = new ValidarNumero();
   Opcion valor = new Opcion();
   Menu vista = new Menu();

   public void Iniciar(){
      boolean continuar = true;

      JOptionPane.showMessageDialog(null,
         "🎉 ¡BIENVENIDO AL SISTEMA DE COBROS DE CARTERA! 🎉\n\n" +
         "Por favor, seleccione una opción del menú.",
         "Bienvenida",
         JOptionPane.INFORMATION_MESSAGE);

      while (continuar) {
         String opcion = vista.VistaSesion();
         Integer numero = ingreso.solicitarEntero(opcion, 2);

         if(numero == null) {
            continue;
         }

         if(numero == 0){
            int confirmacion = JOptionPane.showConfirmDialog(null,
               "¿Está seguro que desea salir del sistema?",
               "Confirmar Salida",
               JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE);

            if(confirmacion == JOptionPane.YES_OPTION){
               JOptionPane.showMessageDialog(null,
                  "✅ GRACIAS POR UTILIZAR NUESTRO PROGRAMA\n\n" +
                  "¡Hasta pronto! 👋",
                  "Despedida",
                  JOptionPane.INFORMATION_MESSAGE);
               continuar = false;
            }
         } else {
            valor.VistaSesionOpcion(numero);
         }
      }
   }
}
