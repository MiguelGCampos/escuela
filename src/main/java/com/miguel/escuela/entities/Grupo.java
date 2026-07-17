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
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GRUPOS", uniqueConstraints = @UniqueConstraint(
        name = "GRUPO_CU_MA_AU_PE_UK",
        columnNames = {"ID_CURSO", "ID_MAESTRO", "ID_AULA", "PERIODO"}
))
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", nullable = false, length = 20, unique = true)
    private String periodo;

    @Builder.Default
    @OneToMany(mappedBy = "grupo")
    private List<Horario> horarios = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "grupo")
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Grupo(String periodo) {
        validarPeriodo(periodo);
        this.periodo = periodo.trim();
    }

    public void actualizar(Curso curso, Maestro maestro, Aula aula, String periodo) {
        validarDatos(curso, maestro, aula, periodo);

        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
        this.periodo = periodo.trim();
    }

    public boolean cambioEnDatos(Long idCurso, Long idMaestro,
                                 Long idAula, String periodo) {
        return !this.curso.getId().equals(idCurso) ||
                !this.maestro.getId().equals(idMaestro) ||
                !this.aula.getId().equals(idAula) ||
                !this.periodo.equals(periodo);
    }

    private void validarDatos(Curso curso, Maestro maestro, Aula aula, String periodo) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        if (maestro == null) {
            throw new IllegalArgumentException("El maestro no puede ser nulo");
        }
        if (aula == null) {
            throw new IllegalArgumentException("El aula no puede ser nula");
        }
        validarPeriodo(periodo);
    }

    private void validarPeriodo(String periodo) {
        StringCustomUtils.validarNoVacio(periodo, "El periodo es requerido");
        StringCustomUtils.validarTamanio(periodo, 5, 20,
                "El periodo debe tener entre 5 y 20 caracteres");
    }
}
