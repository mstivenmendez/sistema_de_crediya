package proyecto.personal;

import java.time.LocalDate;

import proyecto.prestamo.Estado;

public class Empleado extends Persona{

   private int idEmpleado;
   private String cargo;
   private double sueldo;
   private String usuario;
   private String contraseña;
   private Estado estado;
   private LocalDate fechaRegistro;


   public Empleado(String nombre, String nombre2, String apellido, String apellido2, String correo, String telefono,
         String documento, LocalDate fechaNacimiento, int idEmpleado, String cargo, double sueldo, String usuario,
         String contraseña, Estado estado, LocalDate fechaRegistro) {
      super(nombre, nombre2, apellido, apellido2, correo, telefono, documento, fechaNacimiento);
      this.idEmpleado = idEmpleado;
      this.cargo = cargo;
      this.sueldo = sueldo;
      this.usuario = usuario;
      this.contraseña = contraseña;
      this.estado = estado;
      this.fechaRegistro = fechaRegistro;
   }

   public String getUsuario() {
      return usuario;
   }
   public void setUsuario(String usuario) {
      this.usuario = usuario;
   }
   public String getContraseña() {
      return contraseña;
   }
   public void setContraseña(String contraseña) {
      this.contraseña = contraseña;
   }
   public Estado getEstado() {
      return estado;
   }
   public void setEstado(Estado estado) {
      this.estado = estado;
   }
   public LocalDate getFechaRegistro() {
      return fechaRegistro;
   }
   public void setFechaRegistro(LocalDate fechaRegistro) {
      this.fechaRegistro = fechaRegistro;
   }
   public Empleado() {
   }
   public int getIdEmpleado() {
      return idEmpleado;
   }
   public void setIdEmpleado(int idEmpleado) {
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





}
