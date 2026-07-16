package com.miguel.escuela.services.aulas;

import com.miguel.escuela.dto.aulas.AulaRequest;
import com.miguel.escuela.dto.aulas.AulaResponse;
import com.miguel.escuela.entities.Aula;
import com.miguel.escuela.exceptions.RecursoNoEncontradoException;
import com.miguel.escuela.mappers.AulaMapper;
import com.miguel.escuela.repositories.AulaRepository;
import com.miguel.escuela.repositories.GrupoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional //Siempre hace commit o rollback
@Slf4j
public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;

    private final GrupoRepository grupoRepository;

    private final AulaMapper aulaMapper;

    @Override
    public List<AulaResponse> listar() {
        log.info("Listando todas las aulas");
        return aulaRepository.findAll().stream()
                .map(aulaMapper::entidadAResponse).toList();
    }

    @Override
    public AulaResponse obtenerPorId(Long id) {
        return aulaMapper.entidadAResponse(aulaRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("No se encontró un aula con el id: "+id)
        ));
    }

    @Override
    public AulaResponse registrar(AulaRequest request) {
        log.info("Registrando nueva aula");

        validarDatosUnicos(request);

        Aula aula = aulaMapper.requestAEntidad(request);

        aulaRepository.save(aula);

        log.info("Nueva aula {} registrada", aula.getNombre());

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public AulaResponse actualizar(AulaRequest request, Long id) {
        Aula aula = obtenerAulaOException(id);

        log.info("Actualizando aula con id: {}", id);

        validarCambiosUnicos(request, id);

        aula.actualizar(
                request.nombre(),
                request.capacidad()
        );

        log.info("Aula con id {} actualizado", id);

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public void eliminar(Long id) {
        Aula aula =  obtenerAulaOException(id);
        if(grupoRepository.existsByAulaId(id))
            throw new IllegalArgumentException("No se puede eliminar el aula porque ya tiene grupos asignados");
        aulaRepository.delete(aula);

        log.info("Aula con id {} eliminado", id);
    }

    private void validarDatosUnicos(AulaRequest request){
        log.info("Validando nombre único...");
        if(aulaRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe una aula con el nombre de :"
                    + request.nombre());
    }

    private void validarCambiosUnicos(AulaRequest request, Long id){
        log.info("Validando nombre único...");
        if(aulaRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe una aula con el nombre de :"
                    + request.nombre());
    }

    private Aula obtenerAulaOException(Long id){
        log.info("Buscando aula con id: {}", id);

        return aulaRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("Aula no encontrado con id: "+id)
        );
    }
}
