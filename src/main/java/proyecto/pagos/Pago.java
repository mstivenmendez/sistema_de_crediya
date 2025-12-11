package proyecto.pagos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {

   private int pagoId;
   private int prestamoIdFk;
   private BigDecimal valor;
   private LocalDateTime fechaPago; 
   private EstadoPago estado;

   public Pago() {
   }

   public Pago(int pagoId, int prestamoIdFk, BigDecimal valor, LocalDateTime fechaPago, EstadoPago estado) {
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

   public BigDecimal getValor() {
      return valor;
   }

   public void setValor(BigDecimal valor) {
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
