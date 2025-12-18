package proyecto.exception;

public class PrestamoNoEncontrado extends Exception {

    private static final long serialVersionUID = 1L;

    public PrestamoNoEncontrado (String mensaje, Long id) {
        super(mensaje);
    }

    public long getSerialversionuid() {
        return serialVersionUID;
    }

    public PrestamoNoEncontrado(){
    }
}
