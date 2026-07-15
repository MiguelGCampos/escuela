package com.miguel.escuela.dto.cursos;

import jakarta.validation.constraints.*;

public record CursoRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min=5, max=100, message = "El nombre es requerido y debe tener entre 5 y 100 caracteres")
        String nombre,

        @Size(max=200, message = "La descripcion es requerida y debe tener entre 5 y 200 caracteres")
        String descripcion,

        @NotNull(message = "Los creditos son requeridos")
        @Min(value = 1, message = "Los créditos mínimos son 1")
        @Max(value = 10, message = "Los créditos máximos son 10")
        Integer creditos
) {
}
