package com.miguel.escuela.controllers;

import com.miguel.escuela.dto.grupos.GrupoRequest;
import com.miguel.escuela.dto.grupos.GrupoResponse;
import com.miguel.escuela.services.grupos.GrupoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController extends CommonController<GrupoRequest, GrupoResponse, GrupoService>{
    public GrupoController(GrupoService service) {
        super(service);
    }
}
