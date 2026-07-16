package com.miguel.escuela.dto.horarios;

import com.miguel.escuela.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HorarioRequest(
        @NotNull(message = "El idGrupo es requerido")
        Long idGrupo,

        @NotNull(message = "El día es requerido")
        DiaSemana dia,

        @NotNull(message = "La hora de inicio es requerida")
        @Size(min = 5, max = 5, message = "Formato HH:mm")
        String horaInicio,

        @NotNull(message = "La hora de fin es requerida")
        @Size(min = 5, max = 5, message = "Formato HH:mm")
        String horaFin
) {
}
