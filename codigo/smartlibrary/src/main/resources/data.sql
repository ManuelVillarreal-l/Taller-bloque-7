INSERT INTO estudiante (id, nombre, correo) VALUES (1, 'Ana Gómez', 'ana@uni.edu.co');
INSERT INTO estudiante (id, nombre, correo) VALUES (2, 'Luis Pérez', 'luis@uni.edu.co');

INSERT INTO libro (id, titulo, disponible) VALUES (1, 'Diseño de Software', TRUE);
INSERT INTO libro (id, titulo, disponible) VALUES (2, 'Patrones GRASP', TRUE);
INSERT INTO libro (id, titulo, disponible) VALUES (3, 'Clean Architecture', FALSE);

-- El libro 3 ya tiene un préstamo activo (coherente con R5)
INSERT INTO prestamo (fecha_prestamo, fecha_devolucion, estudiante_id, libro_id) VALUES (CURRENT_DATE, NULL, 2, 3);
