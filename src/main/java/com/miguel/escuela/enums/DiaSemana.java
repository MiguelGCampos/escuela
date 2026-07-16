package com.miguel.escuela.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.miguel.escuela.exceptions.RecursoNoEncontradoException;
import com.miguel.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiaSemana {

    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado");

    private final String descripcion;

    public static DiaSemana obtenerDiaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requerida");
        String descripciónNormalizada = StringCustomUtils.quitarAcentos(descripcion.trim());
        for (DiaSemana diaSemana : values()){
            if (StringCustomUtils.quitarAcentos(diaSemana.descripcion).equalsIgnoreCase(descripciónNormalizada))
                return diaSemana;
        }
        throw new RecursoNoEncontradoException("No existe un día con la descripción: " + descripcion);
    }

    @JsonCreator
    public static DiaSemana fromJson(String value) {
        return obtenerDiaPorDescripcion(value);
    }

    @JsonValue
    public String toJson() {
        return descripcion; // así se serializa como "Lunes"
    }

}
