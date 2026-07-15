package com.miguel.escuela.services.cursos;

import com.miguel.escuela.dto.aulas.AulaRequest;
import com.miguel.escuela.dto.cursos.CursoRequest;
import com.miguel.escuela.dto.cursos.CursoResponse;
import com.miguel.escuela.entities.Aula;
import com.miguel.escuela.entities.Curso;
import com.miguel.escuela.exceptions.RecursoNoEncontradoException;
import com.miguel.escuela.mappers.CursoMapper;
import com.miguel.escuela.repositories.CursoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CursoServiceImpl implements CursoService{

    private final CursoRepository cursoRepository;

    private final CursoMapper cursoMapper;

    @Override
    public List<CursoResponse> listar() {
        log.info("Listando todos cursos");
        return cursoRepository.findAll().stream()
                .map(cursoMapper::entidadAResponse).toList();
    }

    @Override
    public CursoResponse obtenerPorId(Long id) {
        return cursoMapper.entidadAResponse(cursoRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("No se encontro un curso con la id: "+id)
        ));
    }

    @Override
    public CursoResponse registrar(CursoRequest request) {
        log.info("Registrando nuevo producto");

        validarDatosUnicos(request);

        Curso curso = cursoMapper.requestAEntidad(request);

        cursoRepository.save(curso);

        log.info("Nueva sucursal {} registrada", curso.getNombre());

        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, Long id) {
        Curso curso = obtenerCursoOException(id);

        log.info("Actualizando sucursal con id: {}", id);

        validarCambiosUnicos(request, id);

        curso.actualizar(
                request.nombre(),
                request.descripcion(),
                request.creditos());

        log.info("Curso con id {} actualizado", id);

        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public void eliminar(Long id) {
        Curso curso = obtenerCursoOException(id);

        cursoRepository.delete(curso);

        log.info("Curso con id {} eliminada", id);
    }

    private void validarDatosUnicos(CursoRequest request){
        log.info("Validando nombre único...");
        if(cursoRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe un curso con el nombre de :"
                    + request.nombre());
    }

        private void validarCambiosUnicos(CursoRequest request, Long id){
        log.info("Validando nombre único...");
        if(cursoRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe un curso con el nombre de :"
                    + request.nombre());
    }

    private Curso obtenerCursoOException(Long id){
        log.info("Buscando aula con id: {}", id);

        return cursoRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("Curso no encontrado con id: "+id)
        );
    }
}
