package com.miguel.escuela.controllers;

import com.miguel.escuela.dto.aulas.AulaRequest;
import com.miguel.escuela.dto.aulas.AulaResponse;
import com.miguel.escuela.services.aulas.AulaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@AllArgsConstructor
@Validated
public class AulaController {
    private final AulaService aulaService;

    @GetMapping
    public ResponseEntity<List<AulaResponse>> listar(){
        return ResponseEntity.ok(aulaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaResponse> obtenerPorId(
        @PathVariable @Positive(message = "La id debe ser positiva") Long id
    ){
        return ResponseEntity.ok(aulaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AulaResponse> registrar(
            @RequestParam AulaRequest request){
        return ResponseEntity.ok(aulaService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AulaResponse> actualizar(
            @Valid @RequestBody AulaRequest request,
            @PathVariable @Positive(message = "La id debe ser positiva") Long id
    ){
        return ResponseEntity.ok(aulaService.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message = "La id debe ser positiva") Long id
    ){
        return  ResponseEntity.noContent().build();
    }
}
