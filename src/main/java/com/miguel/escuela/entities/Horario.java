package com.miguel.escuela.entities;

import com.miguel.escuela.enums.DiaSemana;
import com.miguel.escuela.utils.ValidarHorario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HORARIOS")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Column(name = "DIA", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

    public void registrar(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin) {
        validarDatos(grupo, dia, horaInicio, horaFin);

        this.grupo = grupo;
        this.diaSemana = dia;
        this.horaInicio = horaInicio.trim();
        this.horaFin = horaFin.trim();
    }

    public void actualizar(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin) {
        validarDatos(grupo, dia, horaInicio, horaFin);

        this.grupo = grupo;
        this.diaSemana = dia;
        this.horaInicio = horaInicio.trim();
        this.horaFin = horaFin.trim();
    }

    public boolean cambioEnDatos(Long idGrupo, DiaSemana dia, String horaInicio, String horaFin) {
        return !this.grupo.getId().equals(idGrupo) ||
                !this.diaSemana.equals(dia) ||
                !this.horaInicio.equals(horaInicio) ||
                !this.horaFin.equals(horaFin);
    }

    private void validarDatos(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin) {
        if (grupo == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo");
        }
        if (dia == null) {
            throw new IllegalArgumentException("El día es requerido");
        }
    }
}
