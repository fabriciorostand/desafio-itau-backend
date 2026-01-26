package com.itau.desafio.dto;

public record EstatisticaResponse(

        Long count,
        Double sum,
        Double avg,
        Double min,
        Double max) {

}