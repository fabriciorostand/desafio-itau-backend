package com.itau.desafio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.OffsetDateTime;

public record RegistrarTransacaoRequest(

        @NotNull
        @PositiveOrZero
        Double valor,

        @NotNull
        @PastOrPresent
        OffsetDateTime dataHora) {

}