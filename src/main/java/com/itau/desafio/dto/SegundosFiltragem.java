package com.itau.desafio.dto;

import jakarta.validation.constraints.Positive;

public record SegundosFiltragem(

        @Positive
        Long segundos

) {

    public SegundosFiltragem {
        if (segundos == null) {
            segundos = 60L;
        }
    }

}