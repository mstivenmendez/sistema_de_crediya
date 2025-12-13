package proyecto.solicitud;

import javax.swing.JOptionPane;

public class Datos {

   public String Nombre() {
      String nombre = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el nombre \n");
      return nombre;
   }
   public String Nombre2() {
      String nombre = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el 2do nombre(opcional) \n");
      return nombre;
   }

   public String Usuario() {
      String usuario = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el Nombre de usuario \n" +
         "Solo se permiten letras y números\n" +
         "Sin espacios ni caracteres especiales\n" +
         "Ejemplo: Usuario123");
      return usuario;
   }

   public String Apellido() {
      String apellido = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el apellido \n");
      return apellido;
   }

   public String Apellido2() {
      String apellido = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el 2do apellido (opcional)\n");
      return apellido;
   }

   public String Password() {
      String password = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
            "Ingrese la contraseña \n" +
            " La contraseña debe tener: \n" +
            "- Mínimo 8 caracteres\n" +
            "- Al menos una letra mayúscula\n" +
            "- Al menos una letra minúscula\n" +
            "- Al menos un número\n" +
            "- Al menos un carácter especial (!@#$%^&*.)");
      return password;
   }


   public String Cedula() {
      String cedula = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "POr favor ingrese su numero de documento\n" +
         "Debe contener solo números\n" +
         "Sin espacios ni letras\n" +
         "Entre 7 y 11 dígitos\n" +
         "Ejemplo: 1234567 o 12345678901");
      return cedula;
   }

   public String Correo() {
      String correo = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el correo \n" +
         "Formato de correo inválido\n" +
         "Ejemplo: usuario@ejemplo.com");
      return correo;
   }

   public String Telefono() {
      String telefono = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el telefono \n" +
         "El teléfono debe tener exactamente 10 números\n" +
         "Sin letras ni signos\n" +
         "Ejemplo: 3001234567");
      return telefono;
   }

   public String FechaNacimiento() {
      String fechaNacimiento = JOptionPane.showInputDialog(null,
            " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
            "Ingrese la fecha de nacimiento (Formato: yyyy-MM-dd) \n" +
            "Ejemplo: 1995-04-23");
      return fechaNacimiento;
   }

   public String IdPrestamo() {
      String prestamo = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el valor del prestamo ");
      return prestamo;
   }

   public String valorPrestamo() {
      String valorPrestamo = JOptionPane.showInputDialog(null,  ///simular prestamo
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el valor del prestamo");
      return valorPrestamo;
   }

   public String valorInteres() {
      String valorIntere = JOptionPane.showInputDialog(null,  ///simular prestamo
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese los intereses del prestamo ");
      return valorIntere;
   }

   ///inicio de sesion

   public String EnterPassword() {
      String valorPasswordString = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingresa tu contraseña: ");
      return valorPasswordString;
   }

   public String EnterUser() {
      String valorUserString = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingresa tu Usuario: ");
      return valorUserString;
   }

   public String valorCuotas() {
      String valorIntere = JOptionPane.showInputDialog(null, ///simular prestamo
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el  numero de Cuotas ");
      return valorIntere;
   }

   public String valorSalario() {
      String valorIntere = JOptionPane.showInputDialog(null,
         " 🏦💰 SISTEMA DE COBROS DE CARTERA 💰🏦 \n" +
         "Ingrese el  numero de Cuotas ");
      return valorIntere;
   }
}
