package com.miguel.escuela.entities;

import com.miguel.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MAESTROS")
public class Maestro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MAESTRO", nullable = false)
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    //@Pattern(regexp = "^\\d{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
    @Column(name = "TELEFONO", nullable = false, length = 10, unique = true)
    private String telefono;

    @Builder.Default
    @OneToMany(mappedBy = "maestro")
    private List<Grupo> grupos = new ArrayList<>();

    public void actualizar (String nombre, String apellidoPaterno, String apellidoMaterno,
                            String email, String telefono){
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono);

        this.nombre=nombre;
        this.apellidoPaterno=apellidoPaterno;
        this.apellidoMaterno=apellidoMaterno;
        this.email=email.toLowerCase().trim();
        this.telefono=telefono;
    }

    private void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno,
                              String email, String telefono) {

        StringCustomUtils.validarTamanio(nombre, 1,50,
                "El nombre es requerido y debe tener máximo 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno, 1,50,
                "El apellido paterno es requerido y debe máximo 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno, 1,50,
                "El apellido materno es requerido y debe máximo 50 caracteres");

        StringCustomUtils.validarTamanio(email, 5,100,
                "El nombre es requerido y debe tener entre 5 y 100 caracteres");

        StringCustomUtils.validarTamanio(telefono.trim(), 10,10,
                "El teléfono es requerido y debe tener exactamente 10 caracteres");
    }
}
