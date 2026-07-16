package com.miguel.escuela.dto.inscripciones;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InscripcionRequest(
        @NotNull(message = "El idAlumno es requerido")
        @Positive(message = "El idAlumno debe ser un número positivo")
        Long idAlumno,

        @NotNull(message = "El idMaestro es requerido")
        @Positive(message = "El idAlumno debe ser un número positivo")
        Long idGrupo
) {
}
