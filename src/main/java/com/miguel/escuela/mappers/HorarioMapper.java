package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.datos.DatosGrupo;
import com.miguel.escuela.dto.horarios.HorarioRequest;
import com.miguel.escuela.dto.horarios.HorarioResponse;
import com.miguel.escuela.entities.*;
import org.springframework.stereotype.Component;

@Component
public class HorarioMapper implements CommonMapper<HorarioRequest, HorarioResponse, Horario>{
    @Override
    public Horario requestAEntidad(HorarioRequest request) {
        if(request == null) return null;

        return Horario.builder()
                .diaSemana(request.dia())
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();
    }
    public Horario requestAEntidad(HorarioRequest request, Grupo grupo) {
        if (request == null) return null;

        Horario base = requestAEntidad(request);

        return base.toBuilder()
                .grupo(grupo)
                .build();
    }

    @Override
    public HorarioResponse entidadAResponse(Horario entidad) {
        if (entidad == null) return null;

        return new HorarioResponse(
                entidad.getId(),
                entidadAGrupo(entidad),
                String.join(" ",
                        entidad.getDiaSemana().getDescripcion(),
                        entidad.getHoraInicio(),
                        entidad.getHoraFin())
        );
    }

    public DatosGrupo entidadAGrupo(Horario entidad) {
        if (entidad == null || entidad.getGrupo() == null) {
            return null;
        }

        Grupo grupo = entidad.getGrupo();

        return new DatosGrupo(
                grupo.getCurso().getNombre(),
                grupo.getMaestro().getNombre(),
                grupo.getAula().getNombre(),
                grupo.getPeriodo()
        );
    }
}
