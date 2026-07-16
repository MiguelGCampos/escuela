package com.miguel.escuela.repositories;

import com.miguel.escuela.entities.Grupo;
import com.miguel.escuela.entities.Horario;
import com.miguel.escuela.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    boolean existsByGrupoId(Long idGrupo);
    boolean existsByGrupoAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
            Grupo grupo,
            DiaSemana dia,
            String horaFin,
            String horaInicio
    );
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END " +
            "FROM Horario h " +
            "WHERE h.grupo.id = :idGrupo " +
            "AND h.diaSemana = :dia " +
            "AND h.horaInicio < :horaFin " +
            "AND h.horaFin > :horaInicio")
    boolean existsTraslapeGrupo(@Param("idGrupo") Long idGrupo,
                           @Param("dia") DiaSemana dia,
                           @Param("horaInicio") String horaInicio,
                           @Param("horaFin") String horaFin);

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END " +
            "FROM Horario h " +
            "WHERE h.grupo.aula.id = :idAula " +
            "AND h.diaSemana = :dia " +
            "AND h.horaInicio < :horaFin " +
            "AND h.horaFin > :horaInicio")
    boolean existsTraslapeAula(@Param("idAula") Long idAula,
                               @Param("dia") DiaSemana dia,
                               @Param("horaInicio") String horaInicio,
                               @Param("horaFin") String horaFin);

}
