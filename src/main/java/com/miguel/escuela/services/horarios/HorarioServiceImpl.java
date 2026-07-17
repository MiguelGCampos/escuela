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

        Grupo grupo = obtenerGrupo(request.idGrupo());

        validarHorario.validarFormato(request.horaInicio());
        validarHorario.validarFormato(request.horaFin());
        validarHorario.validarOrden(request.horaInicio(), request.horaFin());

        DiaSemana diaNormalizado = request.dia();

        if (existeTraslapeGrupo(grupo, diaNormalizado, request.horaInicio(), request.horaFin())
                || existeTraslapeAula(grupo, diaNormalizado, request.horaInicio(), request.horaFin())) {
            throw new IllegalArgumentException("El horario se traslapa con otro del mismo grupo o aula");
        }

        Horario horario = horarioMapper.requestAEntidad(request, grupo);

        Horario guardado = horarioRepository.save(horario);
        return horarioMapper.entidadAResponse(guardado);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {
        log.info("Actualizando horario con id {}", id);

        Horario existente = obtenerHorario(id);
        Grupo grupo = obtenerGrupo(request.idGrupo());

        validarHorario.validarFormato(request.horaInicio());
        validarHorario.validarFormato(request.horaFin());
        validarHorario.validarOrden(request.horaInicio(), request.horaFin());

        DiaSemana diaNormalizado = request.dia();

        if (existeTraslapeGrupo(grupo, diaNormalizado, request.horaInicio(), request.horaFin())
                || existeTraslapeAula(grupo, diaNormalizado, request.horaInicio(), request.horaFin())) {
            throw new IllegalArgumentException("El horario se traslapa con otro del mismo grupo o aula");
        }

        if (existente.cambioEnDatos(grupo.getId(), diaNormalizado, request.horaInicio(), request.horaFin())) {
            existente.actualizar(grupo, diaNormalizado, request.horaInicio(), request.horaFin());
            log.info("Horario con id {} actualizado", id);
        }

        Horario guardado = horarioRepository.save(existente);
        return horarioMapper.entidadAResponse(guardado);
    }


    @Override
    public void eliminar(Long id) {
        log.info("Eliminando horario con id {}", id);

        Horario horario = obtenerHorario(id);

        horarioRepository.delete(horario);
    }

    private Horario obtenerHorario(Long id){
        return ServiceUtils.obtenerEntidadOException(horarioRepository, id, Horario.class);
    }

    private Grupo obtenerGrupo(Long id){
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private boolean existeTraslapeGrupo(Grupo grupo, DiaSemana dia, String inicio, String fin) {
        return horarioRepository.existsTraslapeGrupo(grupo.getId(), grupo.getPeriodo(), dia, inicio, fin);
    }

    private boolean existeTraslapeAula(Grupo grupo, DiaSemana dia, String inicio, String fin) {
        return horarioRepository.existsTraslapeAula(grupo.getAula().getId(), grupo.getPeriodo(), dia, inicio, fin);
    }

}


