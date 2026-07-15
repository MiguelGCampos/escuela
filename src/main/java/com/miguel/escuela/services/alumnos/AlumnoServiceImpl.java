package com.miguel.escuela.services.alumnos;

import com.miguel.escuela.dto.alumnos.AlumnoRequest;
import com.miguel.escuela.dto.alumnos.AlumnoResponse;
import com.miguel.escuela.entities.Alumno;
import com.miguel.escuela.exceptions.EntidadRelacionadaException;
import com.miguel.escuela.mappers.AlumnoMapper;
import com.miguel.escuela.repositories.InscripcionRepository;
import com.miguel.escuela.repositories.AlumnoRepository;
import com.miguel.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AlumnoServiceImpl implements AlumnoService{

    private final AlumnoRepository alumnoRepository;

    private final AlumnoMapper alumnoMapper;
    private final InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true
    )
    public List<AlumnoResponse> listar() {
        log.info("Listando todos los alumnos");

        return alumnoRepository.findAll()
                .stream().map(alumnoMapper::entidadAResponse).toList();
    }

    @Override
    public AlumnoResponse obtenerPorId(Long id) {
        return alumnoMapper.entidadAResponse(obtenerAlumno(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnoRequest request) {
        log.info("Registrando nuevo alumno...");

        Alumno alumno = alumnoMapper.requestAEntidad(
                request,
                generarEmail(request),
                generarMatricula(request));


        alumnoRepository.save(alumno);

        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {
        Alumno alumno = obtenerAlumno(id);

        log.info("Actualizando alumno con id: {}", id);

        if(alumno.cambioEnDatos(request.nombre().trim(), request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim())){
            alumno.actualizar(
                    request.nombre(),
                    request.apellidoPaterno(),
                    request.apellidoMaterno(),
                    generarEmail(request),
                    generarMatricula(request)
            );

            log.info("Datos académicos regenerados para el alumno con id: {}",id);

        }

        return alumnoMapper.entidadAResponse(alumno);

    }

    @Override
    public void eliminar(Long id) {
        Alumno alumno =obtenerAlumno(id);

        log.info("Eliminando alumno con id: ", id);

        if(inscripcionRepository.existsByAlumnoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar al alumno ya que tiene inscripciones asignadas"
            );

        alumnoRepository.delete(alumno);

        log.info("Alumno con id: {} eliminado ", id);
    }

    private Alumno obtenerAlumno(Long id){
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumno.class);
    }

    private String generarEmail(AlumnoRequest request){
        log.info("Generando email...");
        return alumnoRepository.generarEmail(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim());
    }

    private String generarMatricula(AlumnoRequest request){
        log.info("Generando matricula...");
        return alumnoRepository.generarMatricula(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim());
    }
}
