package co.edu.smartlibrary.service;

import co.edu.smartlibrary.exception.LibroNoDisponibleException;
import co.edu.smartlibrary.exception.RecursoNoEncontradoException;
import co.edu.smartlibrary.model.Estudiante;
import co.edu.smartlibrary.model.Libro;
import co.edu.smartlibrary.model.Prestamo;
import co.edu.smartlibrary.repository.EstudianteRepository;
import co.edu.smartlibrary.repository.LibroRepository;
import co.edu.smartlibrary.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final EstudianteRepository estudianteRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(EstudianteRepository estudianteRepository,
                           LibroRepository libroRepository,
                           PrestamoRepository prestamoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /** Caso de uso «Registrar préstamo». Todo o nada (R3 + R4). */
    @Transactional
    public Prestamo registrarPrestamo(Long estudianteId, Long libroId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", estudianteId));
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro", libroId));

        if (!libro.isDisponible()) {                       // R1 y R5
            throw new LibroNoDisponibleException(libroId);
        }
        libro.marcarPrestado();                            // R3
        Prestamo prestamo = new Prestamo(estudiante, libro, LocalDate.now()); // R2

        libroRepository.save(libro);   // explícito para la trazabilidad (JPA también lo detectaría)
        return prestamoRepository.save(prestamo);
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }
}
