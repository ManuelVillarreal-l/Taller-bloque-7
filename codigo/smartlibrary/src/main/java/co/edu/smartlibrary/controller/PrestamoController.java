package co.edu.smartlibrary.controller;

import co.edu.smartlibrary.dto.PrestamoRequest;
import co.edu.smartlibrary.dto.PrestamoResponse;
import co.edu.smartlibrary.model.Prestamo;
import co.edu.smartlibrary.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<PrestamoResponse> registrar(@Valid @RequestBody PrestamoRequest request) {
        Prestamo prestamo = prestamoService.registrarPrestamo(request.estudianteId(), request.libroId());
        return ResponseEntity
                .created(URI.create("/api/prestamos/" + prestamo.getId()))   // 201 Created
                .body(PrestamoResponse.desde(prestamo));
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listar() {
        return ResponseEntity.ok(prestamoService.listarPrestamos().stream()   // 200 OK
                .map(PrestamoResponse::desde).toList());
    }
}
