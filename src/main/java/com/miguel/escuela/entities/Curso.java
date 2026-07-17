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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CURSOS")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO", nullable = false)
    private Long id;

    @Column(name = "NOMBRE", length = 100, nullable = false, unique = true)
    private String nombre;

    @Column(name= "DESCRIPCION", length = 200, nullable = false)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false)
    private Integer creditos;

    @Builder.Default
    @OneToMany(mappedBy = "curso")
    private List<Grupo> grupos = new ArrayList<>();

    public void actualizar (String nombre, String descripcion, Integer creditos){
        validarDatos(nombre, descripcion, creditos);

        this.nombre=nombre;
        this.descripcion=descripcion;
        this.creditos=creditos;
    }

    private void validarDatos(String nombre, String descripcion, Integer creditos) {

        StringCustomUtils.validarTamanio(nombre, 5,100,
                "El nombre es requerido y debe tener entre 5 y 100 caracteres");

        StringCustomUtils.validarTamanio(descripcion, 5,200,
                "El nombre es requerido y debe tener entre 5 y 200 caracteres");

        if(creditos==null||creditos<0)
            throw new IllegalArgumentException("La cantidad es requerida y debe ser positiva");
    }

    public boolean cambioEnDatos(String nombre, String descripcion,
                                 Integer creditos){
        return  !this.nombre.equals(nombre) ||
                !this.descripcion.equals(descripcion) ||
                !this.creditos.equals(creditos);
    }
}
