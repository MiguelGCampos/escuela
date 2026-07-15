package com.miguel.escuela.dto.alumnos;

import jakarta.validation.constraints.*;

public record AlumnoRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min=1, max=50, message = "El nombre es requerido y debe tener entre 1 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min=1, max=50, message = "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min=1, max=50, message = "El apellido materno es requerido y debe tener entre 1 y 50 caracteres")
        String apellidoMaterno
) {
}
