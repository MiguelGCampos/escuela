package com.miguel.escuela.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidarHorario {

    private static final Pattern HORA_PATTERN = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

    public void validarFormato(String hora) {
        if (hora == null) {
            throw new IllegalArgumentException("La hora no puede ser nula");
        }

        if (!HORA_PATTERN.matcher(hora).matches()) {
            throw new IllegalArgumentException("Formato de hora inválido. Debe ser HH:MM (ej. 14:30)");
        }
    }

    public void validarOrden(String inicio, String fin) {
        if (inicio == null || fin == null) {
            return;
        }
        if (inicio.compareTo(fin) >= 0) {
            throw new IllegalStateException("La hora de inicio debe ser anterior a la de fin");
        }
    }
}
