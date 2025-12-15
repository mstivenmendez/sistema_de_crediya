package proyecto.pagos;

import java.time.LocalDateTime;

public class Pago {

   private int pagoId;
   private int prestamoIdFk;
   private Double valor;
   private LocalDateTime fechaPago;
   private EstadoPago estado;

   public Pago() {
   }

   public Pago(int pagoId, int prestamoIdFk, Double valor, LocalDateTime fechaPago, EstadoPago estado) {
      this.pagoId = pagoId;
      this.prestamoIdFk = prestamoIdFk;
      this.valor = valor;
      this.fechaPago = fechaPago;
      this.estado = estado;
   }

   // Getters y Setters
   public int getPagoId() {
      return pagoId;
   }

   public void setPagoId(int pagoId) {
      this.pagoId = pagoId;
   }

   public int getPrestamoIdFk() {
      return prestamoIdFk;
   }

   public void setPrestamoIdFk(int prestamoIdFk) {
      this.prestamoIdFk = prestamoIdFk;
   }

   public Double getValor() {
      return valor;
   }

   public void setValor(Double valor) {
      this.valor = valor;
   }

   public LocalDateTime getFechaPago() {
      return fechaPago;
   }

   public void setFechaPago(LocalDateTime fechaPago) {
      this.fechaPago = fechaPago;
   }

   public EstadoPago getEstado() {
      return estado;
   }

   public void setEstado(EstadoPago estado) {
      this.estado = estado;
   }
}
