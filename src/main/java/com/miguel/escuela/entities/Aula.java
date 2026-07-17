package com.miguel.escuela.entities;

import com.miguel.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AULAS")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA", nullable = false)
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Builder.Default
    @OneToMany(mappedBy = "aula")
    private List<Grupo> grupos = new ArrayList<>();

    public boolean cambioEnDatos(String nombre, Integer capacidad){
        return  !this.nombre.equals(nombre) ||
                !this.capacidad.equals(capacidad);
    }

    public void actualizar(String nombre, Integer capacidad){
        validarDatos(nombre, capacidad);

        this.nombre=nombre;
        this.capacidad=capacidad;
    }

    private void validarDatos(String nombre, Integer capacidad) {

        StringCustomUtils.validarTamanio(nombre, 5,50,
                "El nombre es requerido y debe tener entre 5 y 50 caracteres");

        if(capacidad==null||capacidad<0)
            throw new IllegalArgumentException("La cantidad es requerida y debe ser positiva");
    }
}
