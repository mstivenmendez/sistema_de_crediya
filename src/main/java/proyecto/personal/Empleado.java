package proyecto.personal;

import java.time.LocalDate;

public class Empleado extends Persona{

   private String idEmpleado;
   private String cargo;
   private double sueldo;
   private LocalDate fechaContratacion;


   public Empleado(String nombre, String nombre2, String apellido, String apellido2, String correo, String telefono,
         String documento, LocalDate fechaNacimiento, String idEmpleado, String cargo, double sueldo,
         LocalDate fechaContratacion) {
      super(nombre, nombre2, apellido, apellido2, correo, telefono, documento, fechaNacimiento);
      this.idEmpleado = idEmpleado;
      this.cargo = cargo;
      this.sueldo = sueldo;
      this.fechaContratacion = fechaContratacion;
   }

   public Empleado() {
   }

   public String getIdEmpleado() {
      return idEmpleado;
   }
   public void setIdEmpleado(String idEmpleado) {
      this.idEmpleado = idEmpleado;
   }
   public String getCargo() {
      return cargo;
   }
   public void setCargo(String cargo) {
      this.cargo = cargo;
   }
   public double getSueldo() {
      return sueldo;
   }
   public void setSueldo(double sueldo) {
      this.sueldo = sueldo;
   }
   public LocalDate getFechaContratacion() {
      return fechaContratacion;
   }
   public void setFechaContratacion(LocalDate fechaContratacion) {
      this.fechaContratacion = fechaContratacion;
   }





}
