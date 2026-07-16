package com.miguel.escuela.repositories;

import com.miguel.escuela.entities.Calificacion;
import com.miguel.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    boolean existsByInscripcionId(Long idInscripcion);
    boolean existsByInscripcion(Inscripcion inscripcion);
    Optional<Calificacion> findByInscripcionId(Long idInscripcion);
}
