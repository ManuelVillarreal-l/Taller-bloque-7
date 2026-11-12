package co.edu.smartlibrary.dto;

import co.edu.smartlibrary.model.Prestamo;
import java.time.LocalDate;

public record PrestamoResponse(Long id, Long estudianteId, String nombreEstudiante,
                               Long libroId, String tituloLibro, LocalDate fechaPrestamo) {

    public static PrestamoResponse desde(Prestamo p) {
        return new PrestamoResponse(p.getId(),
                p.getEstudiante().getId(), p.getEstudiante().getNombre(),
                p.getLibro().getId(), p.getLibro().getTitulo(),
                p.getFechaPrestamo());
    }
}
