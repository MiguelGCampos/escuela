package com.miguel.escuela.dto.grupos;

import com.miguel.escuela.dto.datos.DatosAula;
import com.miguel.escuela.dto.datos.DatosCurso;
import com.miguel.escuela.dto.datos.DatosMaestro;

import java.util.List;

public record GrupoResponse(
        Long id,
        DatosCurso cursos,
        DatosMaestro maestros,
        DatosAula aulas,
        List<String> horarios,
        String periodo
) {
}
