package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.calificaciones.CalificacionRequest;
import com.miguel.escuela.dto.calificaciones.CalificacionResponse;
import com.miguel.escuela.dto.datos.DatosAlumno;
import com.miguel.escuela.dto.datos.DatosGrupo;
import com.miguel.escuela.dto.datos.DatosInscripcion;
import com.miguel.escuela.entities.Calificacion;
import com.miguel.escuela.entities.Inscripcion;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CalificacionMapper implements CommonMapper<CalificacionRequest, CalificacionResponse, Calificacion>{

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public Calificacion requestAEntidad(CalificacionRequest request) {
        if(request == null) return null;

        return Calificacion.builder()
                .calificacion(request.calificacion())
                .build();
    }

    public Calificacion requestAEntidad(CalificacionRequest request, Inscripcion inscripcion) {
        if (request == null) return null;

        Calificacion base = requestAEntidad(request);

        return base.toBuilder()
                .inscripcion(inscripcion)
                .build();
    }

    @Override
    public CalificacionResponse entidadAResponse(Calificacion entidad) {
        if (entidad == null) return null;

        return new CalificacionResponse(
                entidad.getId(),
                entidadAInscripcion(entidad),
                entidad.getCalificacion(),
                entidad.getFechaRegistro().format(formatter)
        );
    }

    public DatosInscripcion entidadAInscripcion(Calificacion entidad) {
        if (entidad == null || entidad.getInscripcion() == null) {
            return null;
        }

        Inscripcion inscripcion = entidad.getInscripcion();

        return new DatosInscripcion(
                new DatosAlumno(
                        String.join(" ",
                                inscripcion.getAlumno().getNombre(),
                                inscripcion.getAlumno().getApellidoPaterno(),
                                inscripcion.getAlumno().getApellidoMaterno()),
                        inscripcion.getAlumno().getMatricula(),
                        inscripcion.getAlumno().getEmail(),
                        inscripcion.getAlumno().getFechaIngreso().format(formatter)
                ),
                new DatosGrupo(
                        inscripcion.getGrupo().getCurso().getNombre(),
                        String.join(" ",
                                inscripcion.getGrupo().getMaestro().getNombre(),
                                inscripcion.getGrupo().getMaestro().getApellidoPaterno(),
                                inscripcion.getGrupo().getMaestro().getApellidoMaterno()),
                        inscripcion.getGrupo().getAula().getNombre(),
                        inscripcion.getGrupo().getPeriodo()
                ),
                inscripcion.getFechaInscripcion().format(formatter)
        );
    }
}
