package com.miguel.escuela.services.grupos;

import com.miguel.escuela.dto.grupos.GrupoRequest;
import com.miguel.escuela.dto.grupos.GrupoResponse;
import com.miguel.escuela.entities.*;
import com.miguel.escuela.exceptions.EntidadRelacionadaException;
import com.miguel.escuela.exceptions.RecursoNoEncontradoException;
import com.miguel.escuela.mappers.GrupoMapper;
import com.miguel.escuela.repositories.*;
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
public class GrupoServiceImpl implements GrupoService{

    private final GrupoRepository grupoRepository;

    private final CursoRepository cursoRepository;
    private final MaestroRepository maestroRepository;
    private final AulaRepository aulaRepository;
    private final InscripcionRepository inscripcionRepository;
    private final HorarioRepository horarioRepository;

    private final GrupoMapper grupoMapper;

    @Override
    public List<GrupoResponse> listar() {
        log.info("Listando todos los alumnos");

        return grupoRepository.findAll()
                .stream().map(grupoMapper::entidadAResponse).toList();
    }

    @Override
    public GrupoResponse obtenerPorId(Long id) {
        return grupoMapper.entidadAResponse(obtenerGrupo(id));
    }

    @Override
    public GrupoResponse registrar(GrupoRequest request) {
        log.info("Registrando nuevo grupo...");

        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        if (grupoRepository.existsByCursoAndMaestroAndAulaAndPeriodo(curso, maestro, aula, request.periodo())) {
            throw new IllegalArgumentException("Ya existe un grupo con esa combinación de Curso + Maestro + Aula + Periodo");
        }

        Grupo grupo = grupoMapper.requestAEntidad(request, curso, maestro, aula);

        Grupo grupoGuardado = grupoRepository.save(grupo);

        return grupoMapper.entidadAResponse(grupoGuardado);
    }

    @Override
    public GrupoResponse actualizar(GrupoRequest request, Long id) {
        log.info("Actualizando grupo con id {}", id);

        Grupo grupoExistente = obtenerGrupo(id);

        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        if (existeOtroGrupo(grupoExistente, curso, maestro, aula, request.periodo())) {
            throw new IllegalArgumentException("Ya existe otro grupo con esa combinación de Curso + Maestro + Aula + Periodo");
        }

        if (grupoExistente.cambioEnDatos(request.idCurso(), request.idMaestro(), request.idAula(), request.periodo())) {
            grupoExistente.actualizar(curso, maestro, aula, request.periodo());
            log.info("Grupo con id {} actualizado", id);
        }

        Grupo grupoGuardado = grupoRepository.save(grupoExistente);
        return grupoMapper.entidadAResponse(grupoGuardado);
    }



    @Override
    public void eliminar(Long id) {
        Grupo grupo =obtenerGrupo(id);

        log.info("Eliminando grupo con id: ", id);

        if(inscripcionRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar al grupo ya que tiene inscripciones asignadas"
            );
        if(horarioRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar al grupo ya que tiene horarios asignados"
            );

        grupoRepository.delete(grupo);

        log.info("Grupo con id: {} eliminado ", id);
    }

    private Grupo obtenerGrupo(Long id){
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }
    private Curso obtenerCurso(Long id){
        return ServiceUtils.obtenerEntidadOException(cursoRepository, id, Curso.class);
    }
    private Maestro obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(maestroRepository, id, Maestro.class);
    }
    private Aula obtenerAula(Long id){
        return ServiceUtils.obtenerEntidadOException(aulaRepository, id, Aula.class);
    }

    private boolean existeOtroGrupo(Grupo grupoExistente, Curso curso, Maestro maestro, Aula aula, String periodo) {
        return grupoRepository.existsByCursoAndMaestroAndAulaAndPeriodo(curso, maestro, aula, periodo)
                && !(grupoExistente.getCurso().equals(curso)
                && grupoExistente.getMaestro().equals(maestro)
                && grupoExistente.getAula().equals(aula)
                && grupoExistente.getPeriodo().equals(periodo));
    }

}
