package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.datos.DatosCurso;
import com.miguel.escuela.dto.maestros.MaestroRequest;
import com.miguel.escuela.dto.maestros.MaestroResponse;
import com.miguel.escuela.entities.Maestro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MaestroMapper implements CommonMapper<MaestroRequest, MaestroResponse, Maestro> {

    private final CursoMapper cursoMapper;

    @Override
    public Maestro requestAEntidad(MaestroRequest request) {
        if(request == null) return null;

        return Maestro.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .email(request.email())
                .telefono(request.telefono())
                .build();
    }

    @Override
    public MaestroResponse entidadAResponse(Maestro entidad) {
        if(entidad == null) return null;

        List<DatosCurso> cursos=entidadADatosCurso(entidad);

        return new MaestroResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getTelefono(),
                cursos
        );
    }

    private List<DatosCurso> entidadADatosCurso(Maestro entidad){
        if(entidad == null)return List.of();

        return entidad.getGrupos().stream()
                .map(grupo->cursoMapper.entidadADatosCurso(grupo.getCurso())).toList();
    }
}
