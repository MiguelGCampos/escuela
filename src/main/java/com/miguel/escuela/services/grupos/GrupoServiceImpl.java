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

        Curso curso = cursoRepository.findById(request.idCurso())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
        Maestro maestro = maestroRepository.findById(request.idMaestro())
                .orElseThrow(() -> new IllegalArgumentException("Maestro no encontrado"));
        Aula aula = aulaRepository.findById(request.idAula())
                .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada"));

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

        Grupo grupoExistente = grupoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupo no encontrado"));

        Curso curso = cursoRepository.findById(request.idCurso())
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado"));
        Maestro maestro = maestroRepository.findById(request.idMaestro())
                .orElseThrow(() -> new RecursoNoEncontradoException("Maestro no encontrado"));
        Aula aula = aulaRepository.findById(request.idAula())
                .orElseThrow(() -> new RecursoNoEncontradoException("Aula no encontrada"));

        boolean existeOtro = grupoRepository.existsByCursoAndMaestroAndAulaAndPeriodo(curso, maestro, aula, request.periodo())
                && !(grupoExistente.getCurso().equals(curso)
                && grupoExistente.getMaestro().equals(maestro)
                && grupoExistente.getAula().equals(aula)
                && grupoExistente.getPeriodo().equals(request.periodo()));

        if (existeOtro) {
            throw new IllegalArgumentException("Ya existe otro grupo con esa combinación de Curso + Maestro + Aula + Periodo");
        }

        Grupo grupoActualizado = grupoMapper.requestAEntidad(request, curso, maestro, aula)
                .toBuilder()
                .id(grupoExistente.getId())
                .build();

        // Guardar y devolver response
        Grupo grupoGuardado = grupoRepository.save(grupoActualizado);
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


}
