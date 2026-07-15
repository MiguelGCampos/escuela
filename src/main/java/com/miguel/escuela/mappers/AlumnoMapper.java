package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.alumnos.AlumnoRequest;
import com.miguel.escuela.dto.alumnos.AlumnoResponse;
import com.miguel.escuela.dto.datos.CalificacionPeriodo;
import com.miguel.escuela.entities.Alumno;
import com.miguel.escuela.utils.StringCustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class AlumnoMapper implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumno>{

    public Alumno requestAEntidad(AlumnoRequest request) {
        if(request==null) return null;

        return Alumno.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .fechaIngreso(LocalDate.now())
                .build();
    }

    public Alumno requestAEntidad(AlumnoRequest request, String email, String matricula) {
        if(request==null) return null;

        Alumno alumno = requestAEntidad(request);

        alumno.asignarDatosAcademicos(email, matricula);

        return alumno;
    }

    public AlumnoResponse entidadAResponse(Alumno entidad) {
        if(entidad==null) return null;

        List<CalificacionPeriodo> calificaciones = entidadADatosCalificacion(entidad);

        return new AlumnoResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getMatricula(),
                StringCustomUtils.localDateString(entidad.getFechaIngreso()),
                calificaciones,
                entidad.calcularPromedio()
        );
    }

    public List<CalificacionPeriodo> entidadADatosCalificacion(Alumno entidad) {
        if(entidad==null || entidad.getInscripciones() == null || entidad.getInscripciones().isEmpty())
            return List.of();
        return entidad.getInscripciones().stream()
                .map(inscripcion -> new CalificacionPeriodo(
                        inscripcion.getGrupo().getCurso().getNombre(),
                        inscripcion.getGrupo().getPeriodo(),
                        inscripcion.getCalificacion() != null
                        ? inscripcion.getCalificacion().getCalificacion()
                                : null
                )).toList();
    }


}
