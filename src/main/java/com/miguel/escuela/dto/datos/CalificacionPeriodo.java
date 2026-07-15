package com.miguel.escuela.dto.datos;

import java.math.BigDecimal;

public record CalificacionPeriodo(
        String curso,
        String periodo,
        BigDecimal calificacion
) {
}
