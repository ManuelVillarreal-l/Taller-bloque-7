package co.edu.smartlibrary.repository;

import co.edu.smartlibrary.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}
