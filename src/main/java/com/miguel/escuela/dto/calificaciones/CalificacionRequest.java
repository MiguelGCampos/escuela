package com.miguel.escuela.dto.calificaciones;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CalificacionRequest(
        @NotNull(message = "El idInscripcion es requerido")
        @Positive(message = "El idInscripcion debe ser un número positivo")
        Long idInscripcion,


        @NotNull(message = "La calificación es requerida")
        @DecimalMin(value = "0.0", inclusive = true, message = "La calificación mínima es 0.0")
        @DecimalMax(value = "10.0", inclusive = true, message = "La calificación máxima es 10.0")
        @Digits(integer = 2, fraction = 1, message = "La calificación debe tener máximo 2 enteros y 1 decimal")
        BigDecimal calificacion
) {
}
