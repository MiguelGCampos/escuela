package com.miguel.escuela.services.maestros;

import com.miguel.escuela.dto.cursos.CursoRequest;
import com.miguel.escuela.dto.maestros.MaestroRequest;
import com.miguel.escuela.dto.maestros.MaestroResponse;
import com.miguel.escuela.entities.Maestro;
import com.miguel.escuela.exceptions.EntidadRelacionadaException;
import com.miguel.escuela.mappers.MaestroMapper;
import com.miguel.escuela.repositories.GrupoRepository;
import com.miguel.escuela.repositories.MaestroRepository;
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
public class MaestroServiceImpl implements MaestroService{

    private final MaestroRepository maestroRepository;

    private final GrupoRepository grupoRepository;

    private final MaestroMapper maestroMapper;

    @Override
    public List<MaestroResponse> listar() {
        log.info("Listando todos los maestros");

        return maestroRepository.findAll()
                .stream().map(maestroMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        return maestroMapper.entidadAResponse(obtenerMaestro(id));
    }

    @Override
    public MaestroResponse registrar(MaestroRequest request) {
        log.info("Registrando nuevo maestro...");

        validarDatosUnicos(request);

        Maestro maestro = maestroMapper.requestAEntidad(request);

        maestroRepository.save(maestro);

        log.info("Nuevo maestro {} registrado", maestro.getNombre());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        Maestro maestro = obtenerMaestro(id);

        log.info("Actualizando maestro con id: {}", id);

        validarCambiosUnicos(request, id);

        maestro.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono()
        );

        log.info("Maestro {} actualizando correctamente", maestro.getNombre());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public void eliminar(Long id) {
        Maestro maestro = obtenerMaestro(id);

        log.info("Eliminando maestro con id: {}", id);

        if(grupoRepository.existsByMaestroId(id))
            throw new EntidadRelacionadaException("No se puede eliminar al maestro ya tiene grupos asignados");

        maestroRepository.delete(maestro);

        log.info("Maestro con id {} eliminado", id);
    }

    private Maestro obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(maestroRepository, id, Maestro.class);
    }

    private void validarDatosUnicos(MaestroRequest request){
        log.info("Validando email único...");
        if(maestroRepository.existsByEmailIgnoreCase(request.email()))
            throw new IllegalArgumentException("Ya existe un maestro con el correo de :"
                    + request.email());

        log.info("Validando telefono único...");
        if(maestroRepository.existsByTelefono(request.telefono()))
            throw new IllegalArgumentException("Ya existe un maestro con el telefono de :"
                    + request.telefono());
    }

    private void validarCambiosUnicos(MaestroRequest request, Long id){
        log.info("Validando cambio con email único...");
        if(maestroRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), id))
            throw new IllegalArgumentException("Ya existe un maestro con el correo de :"
                    + request.email());

        log.info("Validando cambio con telefono único...");
        if(maestroRepository.existsByTelefonoAndIdNot(request.telefono(), id))
            throw new IllegalArgumentException("Ya existe un maestro registrado con el telefono de :"
                    + request.telefono());
    }
}
