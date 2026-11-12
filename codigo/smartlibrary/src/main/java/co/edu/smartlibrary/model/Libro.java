package co.edu.smartlibrary.model;

import co.edu.smartlibrary.exception.LibroNoDisponibleException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Libro {

    @Id
    private Long id;
    private String titulo;
    private boolean disponible;

    protected Libro() { } // requerido por JPA

    public Libro(Long id, String titulo, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.disponible = disponible;
    }

    /** R3: al prestarse, el libro deja de estar disponible. Protege también R1. */
    public void marcarPrestado() {
        if (!disponible) {
            throw new LibroNoDisponibleException(id);
        }
        this.disponible = false;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public boolean isDisponible() { return disponible; }
}
