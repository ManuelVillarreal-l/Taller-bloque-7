package co.edu.smartlibrary.repository;

import co.edu.smartlibrary.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
