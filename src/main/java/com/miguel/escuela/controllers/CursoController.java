package com.miguel.escuela.controllers;

import com.miguel.escuela.dto.cursos.CursoRequest;
import com.miguel.escuela.dto.cursos.CursoResponse;
import com.miguel.escuela.services.cursos.CursoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@AllArgsConstructor
@Validated
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoResponse>> listar(){
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> obtenerPorId(
            @PathVariable @Positive(message = "La id debe ser positiva") Long id
    ){
        return ResponseEntity.ok(cursoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoResponse> registrar(
            @Valid @RequestBody CursoRequest request
    ){
        return ResponseEntity.ok(cursoService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponse> actualizar(
            @Valid @RequestBody CursoRequest request,
            @PathVariable @Positive(message = "La id debe ser positiva") Long id){
        return ResponseEntity.ok(cursoService.actualizar(request, id));
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message = "La id debe ser positiva") Long id
    ){
        return ResponseEntity.noContent().build();
    }
}
