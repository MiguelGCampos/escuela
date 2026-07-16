package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.datos.DatosAula;
import com.miguel.escuela.dto.datos.DatosCurso;
import com.miguel.escuela.dto.datos.DatosMaestro;
import com.miguel.escuela.dto.grupos.GrupoRequest;
import com.miguel.escuela.dto.grupos.GrupoResponse;
import com.miguel.escuela.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GrupoMapper implements CommonMapper<GrupoRequest, GrupoResponse, Grupo>{

    private final AulaMapper aulaMapper;
    private final CursoMapper cursoMapper;
    private final MaestroMapper maestroMapper;

    @Override
    public Grupo requestAEntidad(GrupoRequest request) {
        if (request == null) return null;

        return Grupo.builder()
                .periodo(request.periodo())
                .build();
    }

    public Grupo requestAEntidad(GrupoRequest request, Curso curso, Maestro maestro, Aula aula) {
        if (request == null) return null;

        Grupo base = requestAEntidad(request);

        return base.toBuilder()
                .curso(curso)
                .maestro(maestro)
                .aula(aula)
                .build();
    }

    @Override
    public GrupoResponse entidadAResponse(Grupo entidad) {
        if (entidad == null) return null;

        return new GrupoResponse(
                entidad.getId(),
                entidadACurso(entidad),
                entidadAMaestro(entidad),
                entidadAAula(entidad),
                entidadAHorarios(entidad),
                entidad.getPeriodo()
        );
    }

    public DatosCurso entidadACurso(Grupo entidad) {
        if (entidad == null || entidad.getCurso() == null) {
            return null;
        }

        Curso curso = entidad.getCurso();

        return new DatosCurso(
                curso.getNombre(),
                curso.getDescripcion(),
                curso.getCreditos()
        );
    }

    public DatosMaestro entidadAMaestro(Grupo entidad) {
        if (entidad == null || entidad.getMaestro() == null) {
            return null;
        }

        Maestro maestro = entidad.getMaestro();

        return new DatosMaestro(
                maestro.getNombre(),
                maestro.getEmail(),
                maestro.getTelefono()
        );
    }

    public DatosAula entidadAAula(Grupo entidad) {
        if (entidad == null || entidad.getAula() == null) {
            return null;
        }

        Aula aula = entidad.getAula();

        return new DatosAula(
                aula.getNombre(),
                aula.getCapacidad()
        );
    }

    public List<String> entidadAHorarios(Grupo entidad) {
        if (entidad == null || entidad.getHorarios() == null) {
            return List.of();
        }

        return entidad.getHorarios().stream()
                .map(horario -> String.format("%s %s - %s",
                        horario.getDiaSemana().getDescripcion(),
                        horario.getHoraInicio(),
                        horario.getHoraFin()
                ))
                .toList();
    }

}
