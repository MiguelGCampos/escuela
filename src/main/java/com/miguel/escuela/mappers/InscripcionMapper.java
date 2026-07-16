package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.datos.DatosAlumno;
import com.miguel.escuela.dto.datos.DatosGrupo;
import com.miguel.escuela.dto.horarios.HorarioRequest;
import com.miguel.escuela.dto.horarios.HorarioResponse;
import com.miguel.escuela.dto.inscripciones.InscripcionRequest;
import com.miguel.escuela.dto.inscripciones.InscripcionResponse;
import com.miguel.escuela.entities.Alumno;
import com.miguel.escuela.entities.Grupo;
import com.miguel.escuela.entities.Horario;
import com.miguel.escuela.entities.Inscripcion;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class InscripcionMapper implements CommonMapper<InscripcionRequest, InscripcionResponse, Inscripcion>{

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Inscripcion requestAEntidad(InscripcionRequest request) {
        return Inscripcion.builder().build();
    }

    public Inscripcion requestAEntidad(InscripcionRequest request, Alumno alumno, Grupo grupo) {
        if (request == null) return null;

        Inscripcion base = requestAEntidad(request);

        return base.toBuilder()
                .alumno(alumno)
                .grupo(grupo)
                .build();
    }

    @Override
    public InscripcionResponse entidadAResponse(Inscripcion entidad) {
        if (entidad == null) return null;

        return new InscripcionResponse(
                entidad.getId(),
                entidadAAlumno(entidad),
                entidadAGrupo(entidad),
                entidad.getCalificacion()!= null
                        ? entidad.getCalificacion().getCalificacion()
                        : null,
                entidad.getFechaInscripcion().format(formatter)
        );
    }

    public DatosAlumno entidadAAlumno(Inscripcion entidad) {
        if (entidad == null || entidad.getGrupo() == null) {
            return null;
        }

        Alumno alumno = entidad.getAlumno();

        return new DatosAlumno(
                String.join(" ",
                        alumno.getNombre(),
                        alumno.getApellidoPaterno(),
                        alumno.getApellidoMaterno()),
                alumno.getMatricula(),
                alumno.getEmail(),
                alumno.getFechaIngreso().format(formatter)
        );
    }

    public DatosGrupo entidadAGrupo(Inscripcion entidad) {
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
