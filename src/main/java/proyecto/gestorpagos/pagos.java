package proyecto.gestorpagos;

import java.time.LocalDate;

public class pagos {
    private int id_pago;
    private double monton;
    private LocalDate fecha_pago;
    
    public pagos(){}

    

    public pagos(int id_pago, double monton, LocalDate fecha_pago) {
        this.id_pago = id_pago;
        this.monton = monton;
        this.fecha_pago = fecha_pago;
    }



    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public double getMonton() {
        return monton;
    }

    public void setMonton(double monton) {
        this.monton = monton;
    }

    public LocalDate getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(LocalDate fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    

}
