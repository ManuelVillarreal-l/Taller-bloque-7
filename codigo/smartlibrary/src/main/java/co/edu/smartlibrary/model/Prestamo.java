package co.edu.smartlibrary.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion; // [0..1]: null mientras el préstamo está activo

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    protected Prestamo() { } // requerido por JPA

    /** R2: el préstamo registra estudiante, libro y fecha. El id lo genera la BD. */
    public Prestamo(Estudiante estudiante, Libro libro, LocalDate fechaPrestamo) {
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
    }

    public boolean estaActivo() { return fechaDevolucion == null; }

    public Long getId() { return id; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public Estudiante getEstudiante() { return estudiante; }
    public Libro getLibro() { return libro; }
}
