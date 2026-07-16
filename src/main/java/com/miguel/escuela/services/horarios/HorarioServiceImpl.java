package com.miguel.escuela.services.horarios;

import com.miguel.escuela.dto.horarios.HorarioRequest;
import com.miguel.escuela.dto.horarios.HorarioResponse;
import com.miguel.escuela.entities.Grupo;
import com.miguel.escuela.entities.Horario;
import com.miguel.escuela.enums.DiaSemana;
import com.miguel.escuela.exceptions.RecursoNoEncontradoException;
import com.miguel.escuela.mappers.HorarioMapper;
import com.miguel.escuela.repositories.GrupoRepository;
import com.miguel.escuela.repositories.HorarioRepository;
import com.miguel.escuela.utils.ServiceUtils;
import com.miguel.escuela.utils.ValidarHorario;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class HorarioServiceImpl implements HorarioService{

    private final HorarioRepository horarioRepository;

    private final GrupoRepository grupoRepository;

    private final HorarioMapper horarioMapper;

    private ValidarHorario validarHorario;

    @Override
    public List<HorarioResponse> listar() {
        log.info("Listando todos los horarios");

        return horarioRepository.findAll()
                .stream().map(horarioMapper::entidadAResponse).toList();
    }

    @Override
    public HorarioResponse obtenerPorId(Long id) {

        return horarioMapper.entidadAResponse(obtenerHorario(id));
    }

    @Override
    public HorarioResponse registrar(HorarioRequest request) {
        log.info("Registrando nuevo horario...");

        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupo no encontrado"));

        validarHorario.validarFormato(request.horaInicio());
        validarHorario.validarFormato(request.horaFin());
        validarHorario.validarOrden(request.horaInicio(), request.horaFin());

        DiaSemana diaNormalizado = request.dia();

        boolean traslapeGrupo = horarioRepository.existsTraslapeGrupo(
                grupo.getId(),
                grupo.getPeriodo(),
                diaNormalizado,
                request.horaInicio(),
                request.horaFin()
        );

        boolean traslapeAula = horarioRepository.existsTraslapeAula(
                grupo.getAula().getId(),
                grupo.getPeriodo(),
                diaNormalizado,
                request.horaInicio(),
                request.horaFin()
        );

        if (traslapeGrupo || traslapeAula) {
            throw new IllegalArgumentException("El horario se traslapa con otro del mismo grupo o aula");
        }

        Horario horario = Horario.builder()
                .grupo(grupo)
                .diaSemana(diaNormalizado)
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();

        Horario guardado = horarioRepository.save(horario);
        return horarioMapper.entidadAResponse(guardado);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {
        log.info("Actualizando horario con id {}", id);

        Horario existente = horarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Horario no encontrado"));

        Grupo grupo = existente.getGrupo();

        validarHorario.validarFormato(request.horaInicio());
        validarHorario.validarFormato(request.horaFin());
        validarHorario.validarOrden(request.horaInicio(), request.horaFin());

        DiaSemana diaNormalizado = request.dia();

        boolean traslapeGrupo = horarioRepository.existsTraslapeGrupoActualizar(
                grupo.getId(),
                grupo.getPeriodo(),
                diaNormalizado,
                request.horaInicio(),
                request.horaFin(),
                existente.getId()
        );

        boolean traslapeAula = horarioRepository.existsTraslapeAulaActualizar(
                grupo.getAula().getId(),
                grupo.getPeriodo(),
                diaNormalizado,
                request.horaInicio(),
                request.horaFin(),
                existente.getId()
        );

        if ((traslapeGrupo || traslapeAula) &&
                !(request.horaInicio().equals(existente.getHoraInicio())
                        && request.horaFin().equals(existente.getHoraFin())
                        && request.dia().equals(existente.getDiaSemana()))) {
            throw new IllegalArgumentException("El horario se traslapa con otro del mismo grupo o aula en el mismo periodo");
        }

        Horario actualizado = Horario.builder()
                .id(existente.getId())
                .grupo(grupo)
                .diaSemana(diaNormalizado)
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();

        Horario guardado = horarioRepository.save(actualizado);
        return horarioMapper.entidadAResponse(guardado);
    }


    @Override
    public void eliminar(Long id) {
        log.info("Eliminando horario con id {}", id);

        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Horario no encontrado"));

        horarioRepository.delete(horario);
    }

    private Horario obtenerHorario(Long id){
        return ServiceUtils.obtenerEntidadOException(horarioRepository, id, Horario.class);
    }


}


