package proyecto.excepciones;

/**
 * Excepción personalizada para errores de validación de datos
 */
public class ValidacionException extends RuntimeException {

    private String campo;
    private Object valorInvalido;

    /**
     * Constructor con mensaje
     * @param mensaje Descripción del error de validación
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y campo
     * @param mensaje Descripción del error
     * @param campo Nombre del campo que falló la validación
     */
    public ValidacionException(String mensaje, String campo) {
        super(mensaje);
        this.campo = campo;
    }

    /**
     * Constructor completo
     * @param mensaje Descripción del error
     * @param campo Nombre del campo que falló la validación
     * @param valorInvalido Valor que causó el error
     */
    public ValidacionException(String mensaje, String campo, Object valorInvalido) {
        super(mensaje);
        this.campo = campo;
        this.valorInvalido = valorInvalido;
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción que causó el error
     */
    public ValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public String getCampo() {
        return campo;
    }

    public Object getValorInvalido() {
        return valorInvalido;
    }

    @Override
    public String toString() {
        if (campo != null && valorInvalido != null) {
            return String.format("ValidacionException: %s [Campo: %s, Valor: %s]", 
                getMessage(), campo, valorInvalido);
        } else if (campo != null) {
            return String.format("ValidacionException: %s [Campo: %s]", 
                getMessage(), campo);
        }
        return super.toString();
    }
}
