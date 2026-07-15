package com.miguel.escuela.services.maestros;

import com.miguel.escuela.dto.maestros.MaestroRequest;
import com.miguel.escuela.dto.maestros.MaestroResponse;
import com.miguel.escuela.services.CrudService;

import java.util.List;

public interface MaestroService extends CrudService <MaestroRequest, MaestroResponse>{

    List<MaestroResponse> listar();

    MaestroResponse obtenerPorId(Long id);

    MaestroResponse registrar(MaestroRequest request);

    MaestroResponse actualizar(MaestroRequest request, Long id);

    void eliminar(Long id);

}
