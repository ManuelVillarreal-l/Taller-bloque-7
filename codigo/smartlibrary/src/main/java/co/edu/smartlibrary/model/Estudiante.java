package co.edu.smartlibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Estudiante {

    @Id
    private Long id;
    private String nombre;
    private String correo;

    protected Estudiante() { } // requerido por JPA

    public Estudiante(Long id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}
