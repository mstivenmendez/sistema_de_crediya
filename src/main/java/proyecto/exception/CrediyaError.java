package proyecto.exception;

public class CrediyaError extends Exception {
    private static final long serialVersionUID = 1L;

    public CrediyaError(String mensaje, Long id) {
        super(mensaje);
    }

    public long getSerialversionuid() {
        return serialVersionUID;
    }

}