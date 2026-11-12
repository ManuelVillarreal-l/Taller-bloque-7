package co.edu.smartlibrary.exception;

public class LibroNoDisponibleException extends RuntimeException {
    public LibroNoDisponibleException(Long libroId) {
        super("El libro con id " + libroId + " no está disponible");
    }
}
