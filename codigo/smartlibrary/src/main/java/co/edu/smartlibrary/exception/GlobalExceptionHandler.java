package co.edu.smartlibrary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());       // 404
    }

    @ExceptionHandler(LibroNoDisponibleException.class)
    public ProblemDetail noDisponible(LibroNoDisponibleException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());        // 409
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail invalida(MethodArgumentNotValidException ex) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage()).reduce((a, b) -> a + "; " + b).orElse("Petición inválida");
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalle);             // 400
    }
}
