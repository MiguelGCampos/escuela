package com.miguel.escuela.dto.alumnos;

import com.miguel.escuela.dto.datos.CalificacionPeriodo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AlumnoResponse(
        Long id,
        String nombre,
        String email,
        String matricula,
        String fechaIngreso,
        List<CalificacionPeriodo> calificacion,
        BigDecimal promedio
) {
}
