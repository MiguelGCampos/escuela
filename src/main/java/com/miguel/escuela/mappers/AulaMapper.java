package com.miguel.escuela.mappers;

import com.miguel.escuela.dto.aulas.AulaRequest;
import com.miguel.escuela.dto.aulas.AulaResponse;
import com.miguel.escuela.entities.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper {

    public Aula requestAEntidad(AulaRequest request){
        if (request == null) return null;

        return Aula.builder()
                .nombre(request.nombre().trim())
                .capacidad(request.capacidad())
                .build();
    }

    public AulaResponse entidadAResponse(Aula aula){
        if (aula == null) return null;

        return new AulaResponse(
                aula.getId(),
                aula.getNombre(),
                aula.getCapacidad()
        );
    }
}
