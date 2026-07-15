package com.miguel.escuela.dto.aulas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AulaRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min=5, max=100, message = "El nombre es requerido y debe tener entre 5 y 100 caracteres")
        String nombre,

        @NotNull(message = "La capacidad es requerida")
        @Positive(message = "La capacidad debe ser positiva")
        Integer capacidad
) {
}
