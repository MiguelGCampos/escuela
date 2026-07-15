package com.miguel.escuela.services.aulas;

import com.miguel.escuela.dto.aulas.AulaRequest;
import com.miguel.escuela.dto.aulas.AulaResponse;

import java.util.List;

public interface AulaService {
    List<AulaResponse> listar();

    AulaResponse obtenerPorId(Long id);

    AulaResponse registrar(AulaRequest request);

    AulaResponse actualizar(AulaRequest request, Long id);

    void eliminar(Long id);

}
