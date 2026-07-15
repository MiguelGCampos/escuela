package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.cursos.CursoRequest;
import com.miguel.escuela.dto.cursos.CursoResponse;
import com.miguel.escuela.dto.datos.DatosCurso;
import com.miguel.escuela.entities.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper implements CommonMapper<CursoRequest, CursoResponse, Curso>{

    public Curso requestAEntidad(CursoRequest request){
        if(request==null) return null;

        String descripcion = request.descripcion() != null
                ? request.descripcion().trim() : null;

        return Curso.builder()
                .nombre(request.nombre().trim())
                .descripcion(descripcion)
                .creditos(request.creditos())
                .build();
    }


    public CursoResponse entidadAResponse(Curso entidad){
        if(entidad==null) return null;

        String descripcion = entidad.getDescripcion() != null
                ? entidad.getDescripcion() : "Sin descripción";

        return new CursoResponse(
                entidad.getId(),
                entidad.getNombre(),
                descripcion,
                entidad.getCreditos()
        );
    }

    public DatosCurso entidadADatosCurso(Curso entidad){
        if(entidad==null) return null;

        String descripcion = entidad.getDescripcion() != null
                ? entidad.getDescripcion() : "Sin descripción";

        return new DatosCurso(
                entidad.getNombre(),
                descripcion,
                entidad.getCreditos()
        );
    }
}
