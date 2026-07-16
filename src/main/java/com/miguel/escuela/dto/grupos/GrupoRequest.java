package com.miguel.escuela.dto.grupos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record GrupoRequest(
        @NotNull(message = "El idCurso es requerido")
        @Positive(message = "El idCurso debe ser un número positivo")
        Long idCurso,

        @NotNull(message = "El idMaestro es requerido")
        @Positive(message = "El idMaestro debe ser un número positivo")
        Long idMaestro,

        @NotNull(message = "El idAula es requerido")
        @Positive(message = "El idAula debe ser un número positivo")
        Long idAula,

        @NotBlank(message = "El periodo es requerido")
        @Pattern(regexp = "\\d{4}-\\d{2}", message = "El periodo debe tener el formato YYYY-MM")
        String periodo
) {
}
