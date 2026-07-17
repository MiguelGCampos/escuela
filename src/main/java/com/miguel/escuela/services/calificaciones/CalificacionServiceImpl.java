package com.miguel.escuela.services.calificaciones;

import com.miguel.escuela.dto.calificaciones.CalificacionRequest;
import com.miguel.escuela.dto.calificaciones.CalificacionResponse;
import com.miguel.escuela.entities.*;
import com.miguel.escuela.exceptions.RecursoNoEncontradoException;
import com.miguel.escuela.mappers.CalificacionMapper;
import com.miguel.escuela.repositories.CalificacionRepository;
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
@Slf4j
@Transactional
public class CalificacionServiceImpl implements CalificacionService{

    private final CalificacionRepository calificacionRepository;

    private final InscripcionRepository inscripcionRepository;

    private final CalificacionMapper calificacionMapper;

    @Override
    public List<CalificacionResponse> listar() {
        log.info("Listando todas los calificaciones");

        return calificacionRepository.findAll()
                .stream().map(calificacionMapper::entidadAResponse).toList();
    }

    @Override
    public CalificacionResponse obtenerPorId(Long id) {
        return calificacionMapper.entidadAResponse(obtenerCalificacion(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionRequest request) {
        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());

        if (calificacionRepository.existsByInscripcion(inscripcion)) {
            throw new IllegalArgumentException("Ya existe una calificación para esta inscripción");
        }

        Calificacion calificacion = new Calificacion();
        calificacion.registrar(request.calificacion(), LocalDate.now(), inscripcion);

        Calificacion guardada = calificacionRepository.save(calificacion);
        return calificacionMapper.entidadAResponse(guardada);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionRequest request, Long id) {
        Calificacion existente = obtenerCalificacion(id);

        Inscripcion nuevaInscripcion = obtenerInscripcion(request.idInscripcion());

        boolean existeOtra = calificacionRepository.existsByInscripcion(nuevaInscripcion)
                && !existente.getInscripcion().equals(nuevaInscripcion);

        if (existeOtra) {
            throw new IllegalArgumentException("Ya existe una calificación para esta inscripción");
        }

        if (existente.cambioEnDatos(request.idInscripcion(), request.calificacion())) {
            existente.actualizar(request.calificacion(), LocalDate.now(), nuevaInscripcion);
            log.info("Calificación {} actualizada para inscripción {}", id, nuevaInscripcion.getId());
        } else {
            log.info("No hubo cambios en los datos de la calificación {}", id);
        }

        Calificacion guardada = calificacionRepository.save(existente);
        return calificacionMapper.entidadAResponse(guardada);
    }


    @Override
    public void eliminar(Long id) {
        log.info("Eliminando calificacion con id: ", id);
        Calificacion calificacion = obtenerCalificacion(id);

        calificacion.getInscripcion().removerCalificacion();

        inscripcionRepository.save(calificacion.getInscripcion());
    }

    private Calificacion obtenerCalificacion(Long id){
        return ServiceUtils.obtenerEntidadOException(calificacionRepository, id, Calificacion.class);
    }

    private Inscripcion obtenerInscripcion(Long id){
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }
}
