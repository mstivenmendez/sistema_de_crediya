package proyecto.excepciones;

/**
 * Excepción personalizada cuando no hay datos disponibles
 */
public class DatosVaciosException extends Exception {

    private String tipoEntidad;
    private int cantidadEsperada;

    /**
     * Constructor con mensaje simple
     * @param mensaje Descripción del error
     */
    public DatosVaciosException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con tipo de entidad
     * @param mensaje Descripción del error
     * @param tipoEntidad Tipo de entidad que está vacía (Empleado, Cliente, etc.)
     */
    public DatosVaciosException(String mensaje, String tipoEntidad) {
        super(mensaje);
        this.tipoEntidad = tipoEntidad;
    }

    /**
     * Constructor completo con información detallada
     * @param mensaje Descripción del error
     * @param tipoEntidad Tipo de entidad que está vacía
     * @param cantidadEsperada Cantidad mínima esperada
     */
    public DatosVaciosException(String mensaje, String tipoEntidad, int cantidadEsperada) {
        super(mensaje);
        this.tipoEntidad = tipoEntidad;
        this.cantidadEsperada = cantidadEsperada;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public int getCantidadEsperada() {
        return cantidadEsperada;
    }

    @Override
    public String toString() {
        if (tipoEntidad != null && cantidadEsperada > 0) {
            return String.format("DatosVaciosException: %s [Entidad: %s, Esperado: %d]", 
                getMessage(), tipoEntidad, cantidadEsperada);
        } else if (tipoEntidad != null) {
            return String.format("DatosVaciosException: %s [Entidad: %s]", 
                getMessage(), tipoEntidad);
        }
        return super.toString();
    }
}
