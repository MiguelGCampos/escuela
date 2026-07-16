package com.miguel.escuela.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INSCRIPCIONES", uniqueConstraints = @UniqueConstraint(
        name = "INSCRIPCION_ALU_GRU_UK",
        columnNames = {"ID_ALUMNO", "ID_GRUPO"}
))
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Builder.Default
    @Column(name = "FECHA_INSCRIPCION", nullable = false)
    private LocalDate fechaInscripcion = LocalDate.now();

    @OneToOne(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Calificacion calificacion;

    public void removerCalificacion() {
        this.calificacion = null;
    }

    public void actualizar(Alumno alumno, Grupo grupo, LocalDate fechaInscripcion) {
        if (alumno == null) throw new IllegalArgumentException("Alumno requerido");
        if (grupo == null) throw new IllegalArgumentException("Grupo requerido");
        if (fechaInscripcion == null) throw new IllegalArgumentException("Fecha requerida");

        this.alumno = alumno;
        this.grupo = grupo;
        this.fechaInscripcion = fechaInscripcion;
    }
}
