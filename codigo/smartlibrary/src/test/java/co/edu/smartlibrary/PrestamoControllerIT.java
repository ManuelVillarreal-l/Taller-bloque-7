package co.edu.smartlibrary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Evidencia automatizada de los escenarios de la Actividad 6 (cada prueba hace rollback). */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrestamoControllerIT {

    @Autowired MockMvc mvc;

    private String cuerpo(long estudianteId, long libroId) {
        return "{\"estudianteId\":" + estudianteId + ",\"libroId\":" + libroId + "}";
    }

    @Test
    void prestamoDeLibroDisponible_devuelve201() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 1)))
           .andExpect(status().isCreated())
           .andExpect(header().exists("Location"))
           .andExpect(jsonPath("$.libroId").value(1));
        // R3: el mismo libro ya no se puede volver a prestar
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(2, 1)))
           .andExpect(status().isConflict());
    }

    @Test
    void libroYaPrestado_devuelve409_sinNuevoPrestamo() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 3)))
           .andExpect(status().isConflict());
        mvc.perform(get("/api/prestamos")).andExpect(jsonPath("$.length()").value(1)); // R4
    }

    @Test
    void estudianteInexistente_devuelve404() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(99, 2)))
           .andExpect(status().isNotFound());
    }

    @Test
    void libroInexistente_devuelve404() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 99)))
           .andExpect(status().isNotFound());
    }

    @Test
    void consultaDePrestamos_devuelve200() throws Exception {
        mvc.perform(get("/api/prestamos"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].tituloLibro").value("Clean Architecture"));
    }

    @Test
    void peticionSinEstudiante_devuelve400() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content("{\"libroId\":2}"))
           .andExpect(status().isBadRequest());
    }
}
