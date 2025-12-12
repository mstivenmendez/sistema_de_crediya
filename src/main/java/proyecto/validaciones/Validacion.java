package proyecto.validaciones;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;

public class Validacion {

   public String ValidarDocumento(String valor) {
      if (valor == null) {
         return null;
      }

      if (!valor.matches("^[0-9]{7,11}$")) {
         JOptionPane.showMessageDialog(null,
               "Debe contener solo números\n");
         return null;
      }

      // ✅ Número válido
      return valor;
   }

   public String ValidarClave(String valor) {

      if (valor == null) {
         return null;
      }

      // Validación única con regex completo
      if (!valor.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$.%^&*]).{8,}$")) {
         JOptionPane.showMessageDialog(null,
               "No cumple con los requistos \n");
         return null;
      }

      return valor;
   }

   public String ValidarEmail(String valor) {

      if (valor == null) {
         return null;
      }

      if (!valor.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
         JOptionPane.showMessageDialog(null,
               "Formato de correo inválido\n");
         return null;
      }
      return valor;
   }

   public String ValidarUsuarioU(String valor) {
      if (valor == null) {
         return null;
      }

      // Validación 1: ¿Está vacío?
      if (valor.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "El valor no puede estar vacío");
         return null;
      }

      // Validación 2: Solo letras y números, sin espacios ni caracteres especiales
      if (!valor.matches("^[a-zA-Z0-9]+$")) {
         JOptionPane.showMessageDialog(null,
               "Solo se permiten letras y números\n");
         return null;
      }
      return valor;
   }

   public String ValidarTelefonoU(String valor) {
      if (valor == null) {
         return null;
      }

      if (!valor.matches("^[0-9]{10}$")) {
         JOptionPane.showMessageDialog(null,
               "El teléfono Incorrecto\n");
         return null;
      }
      return valor;
   }

   public String ValidarTexto(String valor) {

      if (valor == null || valor.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "ingrese un valor que no tenga espacios o que no esta vacio");
         return null;
      }

      if (!valor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
         JOptionPane.showMessageDialog(null, "Ingrese un valor coherente");
         return null;
      }

      if (valor.trim().length() < 2) {
         JOptionPane.showMessageDialog(null, "El rango de palabras es muy corta");
         return null;
      }
      return valor;
   }

   public String ValidarOpcional(String valor) {

      if (!valor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
         JOptionPane.showMessageDialog(null, "Ingrese un valor coherente");
         return null;
      }

      if (valor.trim().length() < 2) {
         JOptionPane.showMessageDialog(null, "El texto es demasiado corto, debe tener al menos 2 caracteres");
         return null;
      }

      return valor;
   }

   public LocalDate ValidarFechaNacimiento(String fechaNacimiento) {
      // Verificar si el valor es nulo o vacío
      if (fechaNacimiento == null || fechaNacimiento.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede estar vacía.");
         return null;
      }

      // Definir el formato esperado para la fecha
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      try {
         // Intentar parsear el String a LocalDate
         LocalDate fecha = LocalDate.parse(fechaNacimiento, formatter);

         // Verificar que la fecha no sea futura
         LocalDate hoy = LocalDate.now();
         if (fecha.isAfter(hoy)) {
            JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser una fecha futura.");
            return null; // Si es una fecha futura, no seguimos
         }

         return fecha; // Retornar la fecha como LocalDate

      } catch (DateTimeParseException e) {
         // Si el formato es incorrecto
         JOptionPane.showMessageDialog(null, "Fecha inválida. Debe ser en formato yyyy-MM-dd. Ejemplo: 1995-04-23");
         return null; // Si la fecha no es válida, retornar null
      }
   }

   public LocalDate ConvertirStringALocalDate(String fechaNacimiento) {
      // Verificar si el valor es nulo o vacío
      if (fechaNacimiento == null || fechaNacimiento.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede estar vacía.");
         return null;
      }

      // Definir el formato esperado para la fecha
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      try {
         // Intentar parsear el String a LocalDate
         LocalDate fecha = LocalDate.parse(fechaNacimiento, formatter);

         return fecha; // Retornar la fecha como LocalDate

      } catch (DateTimeParseException e) {
         // Si el formato es incorrecto
         JOptionPane.showMessageDialog(null, "Fecha inválida. Debe ser en formato yyyy-MM-dd. Ejemplo: 1995-04-23");
         return null; // Si la fecha no es válida, retornar null
      }
   }

   
}
