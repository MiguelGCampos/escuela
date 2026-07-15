package com.miguel.escuela.services.cursos;

import com.miguel.escuela.dto.cursos.CursoRequest;
import com.miguel.escuela.dto.cursos.CursoResponse;

import java.util.List;

public interface CursoService{
    List<CursoResponse> listar();

    CursoResponse obtenerPorId(Long id);

    CursoResponse registrar(CursoRequest request);

    CursoResponse actualizar(CursoRequest request, Long id);

    void eliminar(Long id);
}
