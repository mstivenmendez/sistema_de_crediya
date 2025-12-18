package proyecto.excepciones;

/**
 * Excepción personalizada para operaciones relacionadas con empleados
 */
public class EmpleadoException extends Exception {

    /**
     * Constructor con mensaje
     * @param mensaje Descripción del error
     */
    public EmpleadoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción que causó el error
     */
    public EmpleadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor con causa
     * @param causa Excepción que causó el error
     */
    public EmpleadoException(Throwable causa) {
        super(causa);
    }
}
