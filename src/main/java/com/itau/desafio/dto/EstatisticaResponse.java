package com.itau.desafio.dto;

public record EstatisticaResponse(

        long count,
        double sum,
        double avg,
        double min,
        double max) {

}