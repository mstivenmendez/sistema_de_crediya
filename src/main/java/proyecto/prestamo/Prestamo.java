package proyecto.prestamo;

import java.time.LocalDateTime;

public class Prestamo {

   private int prestamoId;
   private int clienteUsuarioId;
   private int empleadoUsuarioId;
   private Double valor;
   private Double valorTotal;
   public Double getValorTotal() {
      return valorTotal;
   }

   public void setValorTotal(Double valorTotal) {
      this.valorTotal = valorTotal;
   }

   private Double interes;
   private int cuotas;
   private LocalDateTime fechaInicio;
   private LocalDateTime fechaLimite;
   private Estado estado;
   private double valorCuota ;

   public double getValorCuota() {
      return valorCuota;
   }

   public void setValorCuota(double valorCuota) {
      this.valorCuota = valorCuota;
   }

   public Prestamo() {
   }

   public Prestamo(int prestamoId, int clienteUsuarioId, int empleadoUsuarioId, Double valor,
         Double interes, int cuotas, LocalDateTime fechaInicio,
         LocalDateTime fechaLimite, Estado estado, double valorCuota,Double valorTotal) {
      this.prestamoId = prestamoId;
      this.clienteUsuarioId = clienteUsuarioId;
      this.empleadoUsuarioId = empleadoUsuarioId;
      this.valor = valor;
      this.interes = interes;
      this.cuotas = cuotas;
      this.fechaInicio = fechaInicio;
      this.fechaLimite = fechaLimite;
      this.estado = estado;
      this.valorCuota = valorCuota;
      this.valorTotal = valorTotal;
   }

   // Getters y Setters
   public int getPrestamoId() {
      return prestamoId;
   }

   public void setPrestamoId(int prestamoId) {
      this.prestamoId = prestamoId;
   }

   public int getClienteUsuarioId() {
      return clienteUsuarioId;
   }

   public void setClienteUsuarioId(int clienteUsuarioId) {
      this.clienteUsuarioId = clienteUsuarioId;
   }

   public int getEmpleadoUsuarioId() {
      return empleadoUsuarioId;
   }

   public void setEmpleadoUsuarioId(int empleadoUsuarioId) {
      this.empleadoUsuarioId = empleadoUsuarioId;
   }

   public Double getValor() {
      return valor;
   }

   public void setValor(Double valor) {
      this.valor = valor;
   }

   public Double getInteres() {
      return interes;
   }

   public void setInteres(Double interes) {
      this.interes = interes;
   }

   public int getCuotas() {
      return cuotas;
   }

   public void setCuotas(int cuotas) {
      this.cuotas = cuotas;
   }

   public LocalDateTime getFechaInicio() {
      return fechaInicio;
   }

   public void setFechaInicio(LocalDateTime fechaInicio) {
      this.fechaInicio = fechaInicio;
   }

   public LocalDateTime getFechaLimite() {
      return fechaLimite;
   }

   public void setFechaLimite(LocalDateTime fechaLimite) {
      this.fechaLimite = fechaLimite;
   }

   public Estado getEstado() {
      return estado;
   }

   public void setEstado(Estado estado) {
      this.estado = estado;
   }

}
