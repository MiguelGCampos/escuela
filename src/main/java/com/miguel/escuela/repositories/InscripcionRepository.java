package com.miguel.escuela.repositories;

import com.miguel.escuela.entities.Alumno;
import com.miguel.escuela.entities.Grupo;
import com.miguel.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository <Inscripcion, Long>{
    boolean existsByAlumnoId(Long idAlumno);
    boolean existsByGrupoId(Long idGrupo);
    boolean existsByAlumnoAndGrupo(Alumno alumno, Grupo grupo);
    boolean existsByCalificacionId(Long idCalificacion);
    long countByGrupo(Grupo grupo);
}
