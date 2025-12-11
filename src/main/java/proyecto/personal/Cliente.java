package proyecto.personal;

import java.time.LocalDate;

import proyecto.prestamo.Estado;

public class Cliente extends Persona {

   private String idCliente;
   private String usuario;
   private String contraseña;
   private Estado estado;
   private LocalDate fechaRegistro;

   public Cliente(String nombre, String nombre2, String apellido, String apellido2, String correo, String telefono,
         String documento, LocalDate fechaNacimiento, String idCliente, String usuario, String contraseña,
         Estado estado, LocalDate fechaRegistro) {
      super(nombre, nombre2, apellido, apellido2, correo, telefono, documento, fechaNacimiento);
      this.idCliente = idCliente;
      this.usuario = usuario;
      this.contraseña = contraseña;
      this.estado = estado;
      this.fechaRegistro = fechaRegistro;
   }

   public Cliente() {
   }

   public String getIdCliente() {
      return idCliente;
   }
   public void setIdCliente(String idCliente) {
      this.idCliente = idCliente;
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



}
