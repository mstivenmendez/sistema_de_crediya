package proyecto.notificacion;

import java.time.LocalDate;

public class Notificacion {
   private int id_notificacion;
   private String nombre ;
   private LocalDate fecha_envio;
   private String mensaje;
   private int fk_usuario;
   private int fk_empleado;



   public int getFk_empleado() {
      return fk_empleado;
   }

   public void setFk_empleado(int fk_empleado) {
      this.fk_empleado = fk_empleado;
   }

   public int getFk_usuario() {
      return fk_usuario;
   }

   public void setFk_usuario(int fk_usuario) {
      this.fk_usuario = fk_usuario;
   }

   public Notificacion(int id_notificacion, String nombre, LocalDate fecha_envio, String mensaje, int fk_usuario, int fk_empleado){
      this.id_notificacion = id_notificacion;
      this.nombre = nombre;
      this.fecha_envio = fecha_envio;
      this.mensaje = mensaje;
      this.fk_usuario = fk_usuario;
      this.fk_empleado = fk_empleado;
   }

   public Notificacion(){

   }

   public int getId_notificacion() {
      return id_notificacion;
   }
   public void setId_notificacion(int id_notificacion) {
      this.id_notificacion = id_notificacion;
   }
   public String getNombre() {
      return nombre;
   }
   public void setNombre(String nombre) {
      this.nombre = nombre;
   }
   public LocalDate getFecha_envio() {
      return fecha_envio;
   }
   public void setFecha_envio(LocalDate fecha_envio) {
      this.fecha_envio = fecha_envio;
   }
   public String getMensaje() {
      return mensaje;
   }
   public void setMensaje(String mensaje) {
      this.mensaje = mensaje;
   }



}
