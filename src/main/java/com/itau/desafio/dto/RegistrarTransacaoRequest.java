package com.itau.desafio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.OffsetDateTime;

public record RegistrarTransacaoRequest(

        @NotNull
        @PositiveOrZero
        double valor,

        @NotNull
        @Past
        OffsetDateTime dataHora) {

}