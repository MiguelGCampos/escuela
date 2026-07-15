package com.miguel.escuela.repositories;

import com.miguel.escuela.dto.datos.CalificacionPeriodo;
import com.miguel.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository extends JpaRepository <Inscripcion, Long>{
    boolean existsByAlumnoId(Long idAlumno);
}
