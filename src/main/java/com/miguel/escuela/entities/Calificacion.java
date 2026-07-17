package com.miguel.escuela.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
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
@Table(name = "CALIFICACIONES")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long id;

    //@Digits(integer = 2, fraction = 1, message = "Debe tener máximo 2 enteros y 1 decimal")
    //@DecimalMax(value = "10.0", message = "El valor no puede superar 10.0")
    @Column(name = "CALIFICACION")
    private BigDecimal calificacion;

    //@Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(name = "FECHA_REGISTRO")
    private LocalDate fechaRegistro;

    @OneToOne
    @JoinColumn(name = "ID_INSCRIPCION", nullable = false, unique = true)
    private Inscripcion inscripcion;

    public void registrar(BigDecimal valorCalificacion, LocalDate fechaRegistro, Inscripcion inscripcion) {
        validarDatos(valorCalificacion, fechaRegistro);
        if (inscripcion == null) {
            throw new IllegalArgumentException("La inscripción es requerida");
        }
        this.calificacion = valorCalificacion;
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : LocalDate.now();
        this.inscripcion = inscripcion;
    }

    public void actualizar(BigDecimal valorCalificacion, LocalDate fechaRegistro, Inscripcion inscripcion) {
        validarDatos(valorCalificacion, fechaRegistro);
        this.calificacion = valorCalificacion;
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : LocalDate.now();
        this.inscripcion = inscripcion;
    }

    private void validarDatos(BigDecimal calificacion, LocalDate fechaRegistro) {
        if (calificacion == null) {
            throw new IllegalArgumentException("La calificación es requerida");
        }
        if (calificacion.compareTo(BigDecimal.ZERO) < 0 ||
                calificacion.compareTo(BigDecimal.TEN) > 0) {
            throw new IllegalArgumentException("La calificación debe estar entre 0.0 y 10.0");
        }
        if (fechaRegistro != null && fechaRegistro.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de registro no puede ser futura");
        }
    }

    public boolean cambioEnDatos(Long id, BigDecimal calificacion){
        return  !this.id.equals(id) ||
                !this.calificacion.equals(calificacion);
    }
}
