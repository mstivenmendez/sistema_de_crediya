package proyecto.personal;

import java.time.LocalDate;

public class Persona {

   private String nombre,nombre2,apellido,apellido2,correo,telefono, documento ;
   private LocalDate fechaNacimiento;



   public Persona(String nombre, String nombre2, String apellido, String apellido2, String correo, String telefono,
         String documento, LocalDate fechaNacimiento) {
      this.nombre = nombre;
      this.nombre2 = nombre2;
      this.apellido = apellido;
      this.apellido2 = apellido2;
      this.correo = correo;
      this.telefono = telefono;
      this.documento = documento;
      this.fechaNacimiento = fechaNacimiento;
   }

   public Persona() {
   }



   public String getNombre() {
      return nombre;
   }
   public void setNombre(String nombre) {
      this.nombre = nombre;
   }
   public String getApellido() {
      return apellido;
   }
   public void setApellido(String apellido) {
      this.apellido = apellido;
   }
   public String getCorreo() {
      return correo;
   }
   public void setCorreo(String correo) {
      this.correo = correo;
   }
   public String getTelefono() {
      return telefono;
   }
   public void setTelefono(String telefono) {
      this.telefono = telefono;
   }
   public String getDocumento() {
      return documento;
   }
   public void setDocumento(String cedula) {
      this.documento = cedula;
   }
   public LocalDate getFechaNacimiento() {
      return fechaNacimiento;
   }
   public void setFechaNacimiento(LocalDate fechaNacimiento) {
      this.fechaNacimiento = fechaNacimiento;
   }

   public String getNombre2() {
      return nombre2;
   }

   public void setNombre2(String nombre2) {
      this.nombre2 = nombre2;
   }

   public String getApellido2() {
      return apellido2;
   }

   public void setApellido2(String apellido2) {
      this.apellido2 = apellido2;
   }
}
