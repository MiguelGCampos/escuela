package com.miguel.escuela.services.inscripciones;

import com.miguel.escuela.dto.inscripciones.InscripcionRequest;
import com.miguel.escuela.dto.inscripciones.InscripcionResponse;
import com.miguel.escuela.entities.*;
import com.miguel.escuela.exceptions.EntidadRelacionadaException;
import com.miguel.escuela.exceptions.RecursoNoEncontradoException;
import com.miguel.escuela.mappers.InscripcionMapper;
import com.miguel.escuela.repositories.AlumnoRepository;
import com.miguel.escuela.repositories.CalificacionRepository;
import com.miguel.escuela.repositories.GrupoRepository;
import com.miguel.escuela.repositories.InscripcionRepository;
import com.miguel.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class InscripcionServiceImpl implements InscripcionService{

    private final InscripcionRepository inscripcionRepository;

    private final AlumnoRepository alumnoRepository;
    private final GrupoRepository grupoRepository;

    private final InscripcionMapper inscripcionMapper;
    private final CalificacionRepository calificacionRepository;

    @Override
    public List<InscripcionResponse> listar() {
        log.info("Listando todos los horarios");

        return inscripcionRepository.findAll()
                .stream().map(inscripcionMapper::entidadAResponse).toList();
    }

    @Override
    public InscripcionResponse obtenerPorId(Long id) {
        return inscripcionMapper.entidadAResponse(obtenerInscripcion(id));
    }

    @Override
    public InscripcionResponse registrar(InscripcionRequest request) {
        log.info("Registrando nuevo grupo...");

        Alumno alumno = alumnoRepository.findById(request.idAlumno())
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado"));
        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupo no encontrado"));

        if (inscripcionRepository.existsByAlumnoAndGrupo(alumno, grupo)) {
            throw new IllegalArgumentException("El alumno ya está inscrito en este grupo");
        }

        long inscritos = inscripcionRepository.countByGrupo(grupo);
        int capacidad = grupo.getAula().getCapacidad();
        if (inscritos >= capacidad) {
            throw new IllegalArgumentException("El grupo ya alcanzó la capacidad máxima de su aula");
        }

        Inscripcion inscripcion = inscripcionMapper.requestAEntidad(request, alumno, grupo);

        Inscripcion guardada = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.entidadAResponse(guardada);
    }

    @Override
    public InscripcionResponse actualizar(InscripcionRequest request, Long id) {
        Inscripcion existente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada"));

        Alumno alumno = alumnoRepository.findById(request.idAlumno())
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado"));
        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupo no encontrado"));

        boolean existeOtra = inscripcionRepository.existsByAlumnoAndGrupo(alumno, grupo)
                && !(existente.getAlumno().equals(alumno) && existente.getGrupo().equals(grupo));

        if (existeOtra) {
            throw new IllegalArgumentException("Ya existe otra inscripción con ese Alumno + Grupo");
        }

        long inscritos = inscripcionRepository.countByGrupo(grupo);
        int capacidad = grupo.getAula().getCapacidad();

        boolean mismoGrupo = existente.getGrupo().equals(grupo);
        if (!mismoGrupo && inscritos >= capacidad) {
            throw new IllegalArgumentException("El grupo ya alcanzó la capacidad máxima de su aula");
        }

        existente.actualizar(alumno, grupo, LocalDate.now());

        Inscripcion guardada = inscripcionRepository.save(existente);
        return inscripcionMapper.entidadAResponse(guardada);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando inscripcion con id {}", id);

        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada"));

        if(calificacionRepository.existsByInscripcionId(id))
            throw new EntidadRelacionadaException("No se puede eliminar la inscripción ya tiene calificaciones asignados");

        inscripcionRepository.delete(inscripcion);
    }

    private Inscripcion obtenerInscripcion(Long id){
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }
}
